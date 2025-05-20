import os, re
import mysql.connector
from dotenv import load_dotenv
from openai import OpenAI
from pinecone import Pinecone
from upstage_chat_llm import UpstageChatLLM
from langchain.schema import HumanMessage

# 환경변수 로드
load_dotenv(os.path.join(os.path.dirname(__file__), "..", ".env"))

# 설정
DB_CONF = {
    "host":     os.getenv("MYSQL_HOST", "localhost"),
    "user":     os.getenv("MYSQL_USER", "root"),
    "password": os.getenv("MYSQL_PASSWORD", ""),
    "database": os.getenv("MYSQL_DATABASE", "bapsang"),
    "charset":  "utf8mb4"
}
UPSTAGE_API_TOKEN = os.getenv("UPSTAGE_API_TOKEN")
PINECONE_API_KEY   = os.getenv("PINECONE_API_KEY")
PINECONE_INDEX     = os.getenv("PINECONE_INDEX_NAME", "recipe-index")

# DB 커넥션 (모듈 레벨에서 한 번만)
conn   = mysql.connector.connect(**DB_CONF)
cursor = conn.cursor()

# OpenAI / Pinecone 클라이언트
openai = OpenAI(api_key=UPSTAGE_API_TOKEN, base_url="https://api.upstage.ai/v1")
pc     = Pinecone(api_key=PINECONE_API_KEY)
index  = pc.Index(PINECONE_INDEX)

def get_embedding(text: str) -> list[float]:
    resp = openai.embeddings.create(input=text, model="embedding-query")
    return resp.data[0].embedding

def fetch_recipe(recipe_id: str) -> dict:
    cursor.execute(
        "SELECT name, description, review, time, difficulty, portion, method, material_type, image_url "
      + "FROM Recipe WHERE recipe_id = %s", (recipe_id,))
    row = cursor.fetchone()
    if not row:
        return {}
    name, desc, review, time, diff, portion, method, mat_type, img = row
    cursor.execute(
        "SELECT rim.name "
      + "FROM RecipeIngredient ri "
      + "JOIN RecipeIngredientMaster rim ON ri.ingredient_id = rim.ingredient_id "
      + "WHERE ri.recipe_id = %s", (recipe_id,))
    ingredients = [r[0] for r in cursor.fetchall()]
    return {
        "recipe_id":     recipe_id,
        "name":          name,
        "ingredients":   ingredients,
        "descriptions":  desc,
        "review":        review,
        "time":          time,
        "difficulty":    diff,
        "portion":       portion,
        "method":        method,
        "material_type": mat_type,
        "image_url":     img
    }

def recommend(food_name: str, top_k: int = 100) -> dict:
    # 1) Embedding
    vec = get_embedding(food_name)
    # 2) Pinecone query
    pc_res = index.query(vector=vec, top_k=top_k, include_metadata=True)
    matches = pc_res["matches"]
    # 3) DB 조회 & 조립
    dishes = []
    for m in matches:
        rid   = m["id"]
        score = m["score"]
        rec   = fetch_recipe(rid)
        if rec:
            rec["score"] = score
            dishes.append(rec)
    return {
        "food_name": food_name,
        "message":   "음식 이름이 성공적으로 저장되었습니다.",
        "recommended_dishes": dishes
    }

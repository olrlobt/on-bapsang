import os
import mysql.connector
import time
from dotenv import load_dotenv
from upstage_chat_llm import UpstageChatLLM
from langchain.schema import HumanMessage

dotenv_path = os.path.join(os.path.dirname(__file__), "..", "..", ".env")
load_dotenv(dotenv_path)

UPSTAGE_API_TOKEN = os.environ.get("UPSTAGE_API_TOKEN")

# DB 연결
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='bjh0112525',
    database='bapsang',
    charset='utf8mb4'
)
cursor = conn.cursor()

# Upstage 준비
llm = UpstageChatLLM(api_token=UPSTAGE_API_TOKEN, model="solar-pro")

# 실패한 항목 모음
failed_list = []

# 가져오기 (이미지 있는 것만)
cursor.execute("SELECT recipe_id, name, description, review FROM Recipe WHERE image_url IS NOT NULL AND image_url != '' AND instruction IS NULL LIMIT 15000")
recipes = cursor.fetchall()

for recipe_id, name, description, review in recipes:
    # 재료 가져오기
    ingredient_cursor = conn.cursor()
    ingredient_cursor.execute("""
        SELECT rim.name, ri.amount
        FROM RecipeIngredient ri
        JOIN RecipeIngredientMaster rim ON ri.ingredient_id = rim.ingredient_id
        WHERE ri.recipe_id = %s
    """, (recipe_id,))
    parsed_ingredients = ingredient_cursor.fetchall()
    ingredient_cursor.close()

    prompt_text = f"""다음 한식 요리를 만들기 위한 단계별 조리 순서를 요리사처럼 정리해줘.
요리 이름: {name}
요약 설명: {description}
리뷰: {review}
재료:"""
    for ing_name, ing_amount in parsed_ingredients:
        prompt_text += f"\n- {ing_name} {ing_amount}"

    prompt_text += "\n\n결과는 numbered list로:\n1. ~~~~\n2. ~~~~\n...\n설명은 간단 명료"

    print(f"🔍 {recipe_id}: {name} - AI 요청 중...")
    try:
        response = llm.invoke([HumanMessage(content=prompt_text)])
        content_str = response.content if hasattr(response, "content") else response
        print(f"✅ 생성 완료: {recipe_id}")

        # DB 업데이트
        cursor.execute("UPDATE Recipe SET instruction = %s WHERE recipe_id = %s", (content_str, recipe_id))
        conn.commit()

    except Exception as e:
        print(f"⚠ 에러 (recipe_id={recipe_id}): {e}")
        failed_list.append(recipe_id)

# 실패 항목 재시도
if failed_list:
    print(f"\n🔄 실패한 {len(failed_list)}개 재시도 시작...")
    for recipe_id in failed_list:
        cursor.execute("SELECT name, description, review FROM Recipe WHERE recipe_id = %s", (recipe_id,))
        name, description, review = cursor.fetchone()

        ingredient_cursor = conn.cursor()
        ingredient_cursor.execute("""
            SELECT rim.name, ri.amount
            FROM RecipeIngredient ri
            JOIN RecipeIngredientMaster rim ON ri.ingredient_id = rim.ingredient_id
            WHERE ri.recipe_id = %s
        """, (recipe_id,))
        parsed_ingredients = ingredient_cursor.fetchall()
        ingredient_cursor.close()

        prompt_text = f"""다음 한식 요리를 만들기 위한 단계별 조리 순서를 요리사처럼 정리해줘.
요리 이름: {name}
요약 설명: {description}
리뷰: {review}
재료:"""
        for ing_name, ing_amount in parsed_ingredients:
            prompt_text += f"\n- {ing_name} {ing_amount}"

        prompt_text += "\n\n결과는 'Introduction:'으로 시작하고, numbered list로:\n1. ~~~~\n2. ~~~~\n...\n설명은 간단 명료"

        print(f"🔄 재시도: {recipe_id}: {name} - AI 요청 중...")
        try:
            response = llm.invoke([HumanMessage(content=prompt_text)])
            content_str = response.content if hasattr(response, "content") else response
            print(f"✅ 재시도 완료: {recipe_id}")

            cursor.execute("UPDATE Recipe SET instruction = %s WHERE recipe_id = %s", (content_str, recipe_id))
            conn.commit()

        except Exception as e:
            print(f"❌ 재시도 실패 (recipe_id={recipe_id}): {e}")

cursor.close()
conn.close()
print("🎉 모든 작업 완료")

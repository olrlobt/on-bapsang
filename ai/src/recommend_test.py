import os
import json
import mysql.connector
from dotenv import load_dotenv
from upstage_chat_llm import UpstageChatLLM
from langchain.schema import HumanMessage

# 환경변수 로드
dotenv_path = os.path.join(os.path.dirname(__file__), "..", "..", ".env")
load_dotenv(dotenv_path)
UPSTAGE_API_TOKEN = os.environ.get("UPSTAGE_API_TOKEN")

# DB 연결 (메인)
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='bjh0112525',
    database='bapsang',
    charset='utf8mb4'
)
cursor = conn.cursor()

# 한식 요리 이름들 가져오기 (최대 300개)
cursor.execute("SELECT name FROM Recipe LIMIT 300")
recipes = [row[0] for row in cursor.fetchall()]

# 사용자 요청
foreign_dish = input("해외 음식 이름을 입력하세요: ")

# 프롬프트 만들기
prompt_text = f"""
'{foreign_dish}'라는 해외 음식과 가장 유사하거나 추천할 만한 한식 요리 TOP 5를 아래 리스트 중에서 골라줘.
한식 요리 리스트: {', '.join(recipes)}

결과는 JSON 형식으로:
{{
  "recommendations": [
    {{"name": "추천 요리1", "reason": "추천 이유"}},
    ...
  ]
}}
"""

# Upstage LLM 준비
llm = UpstageChatLLM(api_token=UPSTAGE_API_TOKEN, model="solar-pro")

# AI 호출
print(" AI에게 추천 요청 중...")
response = llm.invoke([HumanMessage(content=prompt_text)])

# 결과 출력 + DB에서 상세 정보 추가
content_str = response.content if hasattr(response, "content") else response
try:
    result = json.loads(content_str)
    recommendations = result.get("recommendations", [])

    if not recommendations:
        print("⚠ 추천 결과가 비어 있습니다.")
    else:
        print("✅ 추천 결과:")
        for idx, rec in enumerate(recommendations, start=1):
            rec_name = rec['name']
            reason = rec['reason']
            print(f"\n{idx}. {rec_name} - {reason}")

            # 메인 커서로 레시피 상세 조회
            cursor.execute("SELECT recipe_id, description, review FROM Recipe WHERE name = %s", (rec_name,))
            recipe_data = cursor.fetchone()
            cursor.fetchall()  # 💥 남은 결과 정리 (중복 충돌 방지)

            if recipe_data:
                recipe_id, description, review = recipe_data
                print(f"   📖 설명: {description}")
                print(f"   📝 리뷰: {review}")

                # 🔥 재료 조회용 별도 connection + cursor 사용
                ingredient_conn = mysql.connector.connect(
                    host='localhost',
                    user='root',
                    password='bjh0112525',
                    database='bapsang',
                    charset='utf8mb4'
                )
                ingredient_cursor = ingredient_conn.cursor()
                ingredient_cursor.execute("""
                    SELECT rim.name, ri.amount
                    FROM RecipeIngredient ri
                    JOIN RecipeIngredientMaster rim ON ri.ingredient_id = rim.ingredient_id
                    WHERE ri.recipe_id = %s
                """, (recipe_id,))
                ingredients = ingredient_cursor.fetchall()
                ingredient_cursor.close()
                ingredient_conn.close()

                if ingredients:
                    print("재료:")
                    for ing_name, amount in ingredients:
                        print(f"     - {ing_name}: {amount}")
                else:
                    print("재료 정보가 없습니다.")
            else:
                print("해당 레시피가 DB에서 발견되지 않았습니다.")

except json.JSONDecodeError:
    print("⚠ AI 응답 파싱 실패, 원본 출력:")
    print(content_str)

# 메인 connection 닫기
conn.close()

import os
import json
import mysql.connector
from dotenv import load_dotenv
from upstage_chat_llm import UpstageChatLLM
from langchain.schema import HumanMessage
import re

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
cursor.execute("SELECT name FROM Recipe LIMIT 1000")
recipes = [row[0] for row in cursor.fetchall()]

# 사용자 요청
foreign_dish = input("해외 음식 이름을 입력하세요: ")

# 프롬프트 만들기
prompt_text = f"""
You are a professional Korean chef.  You must choose **exactly 5** recipes from the provided list that are most similar or most appropriate to recommend for the foreign dish '{foreign_dish}'. 
- **절대** 후보 리스트 외의 요리는 언급하지 마세요.
- 1차 기준: 주재료 기반(면 vs 밥 등)
- 2차 기준: 맛 프로파일(매운맛, 단맛, 신맛, 짭짤함, 감칠맛 등)
- 추천 순위 이해를 돕기 위해 0부터 100까지의 유사도 점수(score)도 함께 제공하세요.

#### 입력 리스트(후보 300개):
{', '.join(recipes)}

#### 출력 형식(JSON):
{{
  "recommendations": [
    {{
      "name": "요리명",
      "score": 87,
      "reason": "1) 면 요리라는 점에서… 2) 소스의 매콤함이 유사해서…"
    }},
    … (총 5개)
  ]
}}
"""


# Upstage LLM 준비
llm = UpstageChatLLM(api_token=UPSTAGE_API_TOKEN, model="solar-pro")

# AI 호출
print(" AI에게 추천 요청 중...")
response = llm.invoke([HumanMessage(content=prompt_text)])

# 응답 문자열 추출
content_str = response.content if hasattr(response, "content") else response

# ✅ 코드블럭 (```json ... ```) 제거 (있을 경우만)
content_str = re.sub(r"```json\s*([\s\S]*?)\s*```", r"\1", content_str).strip()

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

            # 레시피 기본 정보 조회
            cursor.execute("SELECT recipe_id, description, review FROM Recipe WHERE name = %s", (rec_name,))
            recipe_data = cursor.fetchone()
            cursor.fetchall()  # 혹시 있을 잔여 결과 정리

            if recipe_data:
                recipe_id, description, review = recipe_data
                print(f"   📖 설명: {description}")
                print(f"   📝 리뷰: {review}")

                # 같은 커넥션으로 재료까지 조회
                cursor.execute("""
                    SELECT rim.name, ri.amount
                    FROM RecipeIngredient ri
                    JOIN RecipeIngredientMaster rim ON ri.ingredient_id = rim.ingredient_id
                    WHERE ri.recipe_id = %s
                """, (recipe_id,))
                ingredients = cursor.fetchall()

                if ingredients:
                    print("   🧂 재료:")
                    for ing_name, amount in ingredients:
                        amount_display = amount if amount and amount.strip() else "수량 정보 없음"
                        print(f"     • {ing_name:<10}: {amount_display}")
            else:
                print("⚠ 해당 레시피가 DB에서 발견되지 않았습니다.")

except json.JSONDecodeError:
    print("⚠ AI 응답 파싱 실패, 원본 출력:")
    print(content_str)
# 메인 connection 닫기
conn.close()

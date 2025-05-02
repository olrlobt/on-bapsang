import pandas as pd
import mysql.connector
import re
import chardet

def detect_file_encoding(filepath):
    with open(filepath, 'rb') as f:
        raw_data = f.read(10000)
    result = chardet.detect(raw_data)
    encoding = result['encoding']
    if not encoding:
        print(f"⚠ Encoding detection failed for {filepath}, fallback to cp949")
        encoding = 'cp949'
    else:
        print(f"✅ Detected encoding for {filepath}: {encoding}")
    return encoding

def safe_read_csv(file, encoding):
    print(f"🔍 Safely reading file: {file}")
    lines = []
    with open(file, 'r', encoding=encoding, errors='ignore') as f:
        for line in f:
            lines.append(line)
    from io import StringIO
    data = ''.join(lines)
    df = pd.read_csv(StringIO(data))
    return df

def parse_ingredients(raw_text):
    block_pattern = r'\[(.*?)\]\s*([^[]+)'
    blocks = re.findall(block_pattern, raw_text)
    result = []
    for category, items in blocks:
        item_pattern = r'([\w가-힣]+)\s*((?:\d+[./]?\d*\s*[가-힣a-zA-Z]*)|약간|적당량|톡톡|조금)?'
        items = re.split(r'\||,', items)
        for item in items:
            item = item.strip()
            match = re.match(item_pattern, item)
            if match:
                name = match.group(1)
                amount = match.group(2).strip() if match.group(2) else ''
                result.append({
                    'category': category,
                    'name': name,
                    'amount': amount
                })
    return result

# DB 연결
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='bjh0112525',
    database='bapsang',
    charset='utf8mb4'
)
cursor = conn.cursor()

def insert_ingredient_master_if_not_exists(cursor, name, ing_type):
    cursor.execute("SELECT ingredient_id FROM RecipeIngredientMaster WHERE name = %s", (name,))
    result = cursor.fetchone()
    if not result:
        cursor.execute("INSERT INTO RecipeIngredientMaster (name, type) VALUES (%s, %s)", (name, ing_type))
        conn.commit()

def get_ingredient_id_by_name(cursor, name):
    cursor.execute("SELECT ingredient_id FROM RecipeIngredientMaster WHERE name = %s", (name,))
    return cursor.fetchone()[0]

def insert_recipe(cursor, recipe_id, name, description, review, time, difficulty, portion, method, material_type):
    cursor.execute(
        """
        INSERT IGNORE INTO Recipe 
        (recipe_id, name, description, review, time, difficulty, portion, method, material_type) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """,
        (recipe_id, name, description, review, time, difficulty, portion, method, material_type)
    )
    conn.commit()

def insert_recipe_ingredient(cursor, recipe_id, ingredient_id, amount):
    cursor.execute("INSERT INTO RecipeIngredient (recipe_id, ingredient_id, amount) VALUES (%s, %s, %s)",
                   (recipe_id, ingredient_id, amount))
    conn.commit()

recipe_files = [
    '../../data/raw_recipes/TB_RECIPE_SEARCH_241226.csv',
    '../../data/raw_recipes/TB_RECIPE_SEARCH-220701.csv',
    '../../data/raw_recipes/TB_RECIPE_SEARCH-20231130.csv',
]

for file in recipe_files:
    detected_encoding = detect_file_encoding(file)
    df = safe_read_csv(file, detected_encoding)
    print(f"✅ Loaded {file} with {len(df)} rows.")

    for _, row in df.iterrows():
        recipe_id = row['RCP_SNO']
        name = str(row['CKG_NM']).strip()
        description = str(row['RCP_TTL']).strip()
        review = str(row['CKG_IPDC']).strip()
        time = str(row['CKG_TIME_NM']).strip()
        difficulty = str(row['CKG_DODF_NM']).strip()
        portion = str(row['CKG_INBUN_NM']).strip()
        method = str(row['CKG_MTH_ACTO_NM']).strip()
        material_type = str(row['CKG_MTRL_ACTO_NM']).strip()
        raw_ingredient_text = str(row['CKG_MTRL_CN']).strip()

        if not name or name == 'nan':
            print(f"⚠ Skipping recipe_id={recipe_id} (missing name)")
            continue

        if raw_ingredient_text == 'nan':
            continue

        insert_recipe(cursor, recipe_id, name, description, review, time, difficulty, portion, method, material_type)

        parsed_ingredients = parse_ingredients(raw_ingredient_text)
        for ing in parsed_ingredients:
            if ing['amount'] == '':
                continue
            insert_ingredient_master_if_not_exists(cursor, ing['name'], ing['category'])
            ingredient_id = get_ingredient_id_by_name(cursor, ing['name'])
            print(f"🍳 Inserting: recipe_id={recipe_id}, ingredient_id={ingredient_id}, amount='{ing['amount']}'")
            insert_recipe_ingredient(cursor, recipe_id, ingredient_id, ing['amount'])

conn.close()

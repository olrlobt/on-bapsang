# on-bapsang

한식 기반 AI 추천 서비스

## Setup Database

1️⃣ MySQL 실행  
2️⃣ 실행:
mysql -u root -p < db/init_schema.sql

### 🐳 Docker로 DB 실행하기

- 터미널에서 프로젝트 루트로 이동한 뒤:

```
docker-compose up -d
```

- 최초 실행 시 initdb/full_dump.sql 덤프 파일이 자동 실행되어 데이터가 로드된다.

### db 접속

```
docker exec -it bapsang-mysql mysql -u root -p
```

- 접속 후,

```
USE bapsang;
SHOW TABLES;
SELECT COUNT(*) FROM Recipe;
```

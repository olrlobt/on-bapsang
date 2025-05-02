# on-bapsang

한식 기반 AI 추천 서비스

### 1.덤프 파일 다운로드

1. 아래 링크에서 `full_dump.sql` 다운로드
   [Download full_dump.sql](https://bapsang-dump.s3.ap-southeast-2.amazonaws.com/full_dump.sql)

2. 프로젝트 루트의 `initdb` 폴더에 넣어주세요.

```
/initdb/full_dump.sql
```

### 2. Docker로 DB 실행하기

- 터미널에서 프로젝트 루트로 이동한 뒤:

```
docker-compose up -d
```

- 최초 실행 시 initdb/full_dump.sql 덤프 파일이 자동 실행되어 데이터가 로드된다.

### 3. db 접속

```
docker exec -it bapsang-mysql mysql -u root -p
```

- 접속 후,

```
USE bapsang;
SHOW TABLES;
SELECT COUNT(*) FROM Recipe;
```

해서

```
+----------+
| COUNT(*) |
+----------+
|   207369 |
+----------+
```

개로 나온다면 정상적

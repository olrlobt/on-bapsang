FROM python:3.10-slim


# netcat-openbsd 설치
RUN apt-get update \
  && apt-get install -y netcat-openbsd \
  && rm -rf /var/lib/apt/lists/*
WORKDIR /app

# requirements
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 소스 복사
COPY ai/src/ .

# entrypoint 스크립트 복사
COPY ai/wait_and_start.sh /wait_and_start.sh
RUN chmod +x /wait_and_start.sh

EXPOSE 8000
ENTRYPOINT ["/wait_and_start.sh"]

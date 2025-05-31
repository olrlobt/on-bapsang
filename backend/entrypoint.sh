set -e

: "${MYSQL_HOST:=db}"
: "${MYSQL_PORT:=3306}"

echo "▶ Waiting for MySQL at $MYSQL_HOST:$MYSQL_PORT ..."
while ! nc -z $MYSQL_HOST $MYSQL_PORT; do
  sleep 1
done

echo "✔ MySQL is up — starting Spring Boot"
exec java -jar app.jar

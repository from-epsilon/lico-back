FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appuser \
    && adduser -S -G appuser -u 10001 appuser

# CI에서 만든 bootJar 결과물(app.jar)만 이미지에 포함
COPY build/libs/app.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
USER appuser
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

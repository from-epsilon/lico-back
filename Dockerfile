FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appuser \
    && adduser -S -G appuser -u 10001 appuser

RUN mkdir -p /otel \
    && chown -R appuser:appuser /otel

# CI에서 만든 bootJar 결과물(app.jar)만 이미지에 포함
COPY build/libs/app.jar /app/app.jar

COPY otel/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar

EXPOSE 8080
ENV JAVA_OPTS=""
USER appuser
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

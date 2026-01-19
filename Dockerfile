#############################
# 1) 빌드 스테이지
#############################
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon -q dependencies

COPY src/ src/
RUN ./gradlew --no-daemon clean bootJar -x test

#############################
# 2) 런타임 스테이지
#############################
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S appuser \
    && adduser -S -G appuser -u 10001 appuser

COPY --from=build /workspace/build/libs/app.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
USER appuser
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

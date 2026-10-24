# ---- Build stage ----------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Cache dependencies separately from application sources
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Runtime stage --------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S finance && adduser -S finance -G finance

WORKDIR /app
COPY --from=build /build/target/finance-account-2.0.0.jar /app/app.jar

RUN chown -R finance:finance /app
USER finance

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=5 \
    CMD wget -qO- http://127.0.0.1:8080/actuator/health >/dev/null || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]

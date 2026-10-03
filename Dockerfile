# Stage 1: Build the jar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Bake in DocumentDB's TLS trust store (same keytool trick as before, done at build time)
RUN curl -o /tmp/global-bundle.pem https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem && \
    cp /tmp/global-bundle.pem /app/global-bundle.pem && \
    keytool -importcert -noprompt -trustcacerts \
      -alias docdb-ca \
      -file /tmp/global-bundle.pem \
      -keystore "$JAVA_HOME/lib/security/cacerts" \
      -storepass changeit

COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
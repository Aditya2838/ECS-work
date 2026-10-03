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
    cd /tmp && \
    awk 'BEGIN{n=0} /-----BEGIN CERTIFICATE-----/{n++} {print > "cert" n ".pem"}' global-bundle.pem && \
    for f in cert*.pem; do \
      keytool -importcert -noprompt -trustcacerts -alias "docdb-$f" -file "$f" -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit; \
    done

COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
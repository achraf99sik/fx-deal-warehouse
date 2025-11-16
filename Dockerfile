FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*


COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

CMD ["java", "-jar", "target/fx-deal-warehouse-1.0-SNAPSHOT.jar"]

FROM maven:3.8.5-jdk-8-slim
WORKDIR /usr/src/node-maven-plugin
COPY src ./src
COPY pom.xml .
CMD ["mvn", "test"]
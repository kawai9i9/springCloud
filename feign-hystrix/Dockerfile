FROM openjdk:8-jdk-alpine
MAINTAINER 646154945@qq.com
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
VOLUME ["/data/logs"]
EXPOSE 8081
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
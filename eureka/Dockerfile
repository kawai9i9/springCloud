FROM openjdk:8-jdk-alpine
MAINTAINER 646154945@qq.com
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
ENV EUREKA_IP localhost
VOLUME ["/data/logs"]
EXPOSE 8761
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
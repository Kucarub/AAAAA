FROM openjdk:17-jdk-slim

LABEL maintainer="ruoyi"

WORKDIR /ruoyi

COPY ruoyi-admin/target/ruoyi-admin.jar app.jar

EXPOSE 18080

ENTRYPOINT ["java", "-jar", "app.jar"]

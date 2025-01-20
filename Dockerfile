FROM eclipse-temurin:17-jdk
ADD target/delivery-kata.jar delivery-kata.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/delivery-kata.jar"]
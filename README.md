# Delivery Kata

## How to start the project

1. **Compile the application :**
Run the following command to clean and package the application:
  ```
./mvnw  clean package
  ```
2. **Run Docker compose**
Start the necessary Docker containers using:
  ```
docker compose up
  ```
3. **Launch the application**
Start the application using the Spring Boot plugin:
  ```
./mvnw  spring-boot:run
  ```
4. **Access the Application** :
   
**REST API Documentation:** Open the Swagger UI in your browser:
   ```
    http://localhost:8080/swagger-ui.html
   ```

## Frameworks and Tools
- Java 17
- Spring boot 3
- Spring data jpa
- Spring webFlux
- Spring Security
- Postgres (DataBase)
- Axon framework (event sourcing)
- Flyway (database migration tool)
- OpenAPi (Documenting the REST API)
- githubAction (CI/CD)

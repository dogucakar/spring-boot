# HOW TO RUN APPLICATION

# Dockerized way
* First run mvn clean package to create jar file under /target. This creates target/core-1.0.jar
* There is a dockerfile under project root.<br> 
  Run docker build -t digitalwallet-app in this directory. Be sure your docker engine is running.
* Run this command docker run -p 8080:8080 digitalwallet-app <br>
  <b>-p 8080:8080:</b> Maps port 8080 on your machine to port 8080 in the container (adjust this if your app runs on another port) <br>
  <b>digitalwallet-app:</b> The name of the image you built in step 2

# Run using IntelliJ
* Import project to your IntelliJ IDE.
* After project successfully imported run a maven clean install and build project.
* Creata a run configuration for the application
* Run this configuration and your project will be ready in the http://localhost:8080
* For controller swagger endpoint go to http://localhost:8080/swagger-ui/index.html

# USERS AND ROLES
Two users created with below roles.Under src/main/resources data.sql script automatically inserts data at application startup.
* USER:admin  PASSWORD:test ROLE:EMPLOYEE (Can do all operations)
* USER:test   PASSWORD:test ROLE:CUSTOMER (Only can do operations for it's own)

# HOW TO TEST 
Add Authorization Header Basic Auth username: {username} password: test. You can use POSTMAN tool.
H2 In memory database console is available at: http://localhost:8080/h2-console username:sa password:sa. Here you can see tables in H2 GUI

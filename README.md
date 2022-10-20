# URL-Shortner-Spring-Boot
This is production ready url-shortner app developed for future reference for production projects
This application takes input url and save it to hashmap and gives back shorten url which can be used to navigate to perticular url saved.

## Technologies and libraries used
1. Spring security 
2. JWT

## Data Structures Used
1. HashMap

## How to install
To clone repository use following command
```shell
git clone https://github.com/aditya-deshmukh-tech/simple-url-shortner-spring-boot.git
```

once cloned create jar of project
```shell
mvn clean install -DskipTests
```
## Environment variables 
This project requires 4 environment variables export them using following commands
1. jwt_secret = for hashing of jwt
2. jwt_validity = for validity of jwt token
3. jwt_refreshTokenValidity = validity of refresh token
4. jwt_expiredTokenValidity = validity of expired token

validities should be mentioned in milliseconds as default
```shell
export jwt_secret=random jwt_validity=60000 jwt_refreshTokenValidity=60000 jwt_expiredTokenValidity=120000
```
## Run the project
Once environment variables set run jar using following command
```shell
java -jar target/UrlShortner-0.0.1-SNAPSHOT.jar
```

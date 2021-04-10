# R2dbc MySQL Test
* R2dbc + MySQL 테스트를 위한 단순한 API 제공 서버 어플리케이션

## 환경

* 개발 환경: Mac, Java 11
* 터미널 명령어 실행은 프로젝트 루트 폴더 기준

### DB 세팅

* 필요 사항: docker-compose 사용 가능 환경

```sh
# Start Mysql instance 
$ docker-compose -f ./docker/mysql/docker-compose.yml up -d

# Set DB Scheme
$ ./mvnw -Dflyway.configFiles=src/main/resources/db/migration/flyway.conf flyway:migrate
```

### Spring Boot 실행

```sh
$ ./mvnw spring-boot:run
```

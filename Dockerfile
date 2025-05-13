FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY target/*.jar /app/app.jar

ENTRYPOINT ["java" , "-jar" , "/app/app.jar", \
"--spring.data.mongodb.uri=mongodb://host.docker.internal:27017/supportportal", \
  "--spring.datasource.url=jdbc:mysql://host.docker.internal:3306/supportportal?createDatabaseIfNotExist=true", \
  "--spring.datasource.username=root", \
  "--spring.datasource.password=root", \
  "--spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver", \
  "--spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect", \
  "--spring.jpa.hibernate.ddl-auto=update", \
  "--spring.jpa.show-sql=true", \
  "--spring.jpa.properties.hibernate.format_sql=true"]



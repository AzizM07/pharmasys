FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY api ./api
COPY dao ./dao
COPY model ./model
COPY lib ./lib

RUN mkdir out \
 && javac -d out api/**/*.java dao/**/*.java model/**/*.java

EXPOSE 8080

CMD ["java", "-cp", "out:lib/*", "api.ServerApp"]

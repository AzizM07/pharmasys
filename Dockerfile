FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY api ./api
COPY dao ./dao
COPY model ./model
COPY lib ./lib

RUN mkdir out \
 && find api dao model -name "*.java" > sources.txt \
 && javac -cp "lib/*" -d out @sources.txt

EXPOSE 8080

CMD ["java", "-cp", "out:lib/*", "api.ServerApp"]

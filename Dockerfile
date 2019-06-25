FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/antoniotino/SudokuGame.git

FROM maven:3.6-jdk-11
WORKDIR /app
COPY --from=0 /app/SudokuGame /app
RUN mvn package

FROM openjdk:openjdk:11-jre-slim
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
COPY --from=1 /app/target/tmp.jar /app

CMD /usr/bin/java -jar tmp.jar -m $MASTERIP -id $ID
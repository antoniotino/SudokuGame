FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/antoniotino/SudokuGame.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/SudokuGame /app
RUN mvn package

FROM openjdk:11.0.3-stretch
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0
COPY --from=1 /app/target/sudokugame-1.0-jar-with-dependencies.jar /app

WORKDIR /app
COPY --from=2 /app/SudokuGame/unsolvedSudoku.json /app

WORKDIR /app
COPY --from=3 /app/SudokuGame/solvedSudoku.json /app

CMD /usr/bin/java -jar sudokugame-1.0-jar-with-dependencies.jar -m $MASTERIP -id $ID
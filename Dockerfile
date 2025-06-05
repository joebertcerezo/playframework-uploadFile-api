FROM sbtscala/scala-sbt:eclipse-temurin-jammy-11.0.20.1_1_1.9.7_3.3.1

WORKDIR /app

COPY . .

RUN sbt reload
RUN sbt compile
RUN sbt stage

EXPOSE 9000

CMD ["./target/universal/stage/bin/playframework-uploadFile", "-Dplay.http.secret.key=changeme", "-Dhttp.port=9000"]:contentReference[oaicite:48]{index=48}

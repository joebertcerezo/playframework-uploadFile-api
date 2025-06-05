# Use the official sbt image with JDK 17
FROM sbtscala/scala-sbt:eclipse-temurin-17.0.8_1.9.9_3.3.0

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Install dependencies first to cache them
RUN sbt update

# Stage the Play app (prod build)
RUN sbt stage

# Expose Play Framework's default port
EXPOSE 9000

# Run the app
CMD ["./target/universal/stage/bin/playframework-uploadFile", "-Dplay.http.secret.key=changeme", "-Dhttp.port=9000"]

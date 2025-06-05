# Use official image with Java 17, SBT 1.9.9, and Scala 3.3.1
FROM hseeberger/scala-sbt:17.0.9_1.9.9_3.3.1

# Set working directory
WORKDIR /app

# Copy everything into the container
COPY . .

# Download dependencies early to cache layer
RUN sbt update

# Build the app with staging for production
RUN sbt stage

# Expose default Play Framework port
EXPOSE 9000

# Run the staged executable
CMD ["./target/universal/stage/bin/playframework-uploadFile", "-Dplay.http.secret.key=changeme", "-Dhttp.port=9000"]

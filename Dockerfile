FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:InitialRAMPercentage=50.0", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Xlog:gc*:file=/tmp/gc.log:time,uptime,level,tags", \
  "-jar", "app.jar"]
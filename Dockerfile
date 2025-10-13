# --- BUILD STAGE ---
FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- RUN STAGE ---
FROM docker.io/library/tomcat:9.0-jdk17
WORKDIR /usr/local/tomcat/webapps/

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Sửa Tomcat để nghe đúng port Render cung cấp
RUN sed -i 's/port="8080"/port="${PORT}"/' /usr/local/tomcat/conf/server.xml \
 && sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]

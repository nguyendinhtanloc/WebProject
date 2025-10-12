# ====================================
# Multi-stage Dockerfile for Java Web Application
# Stage 1: Build với Maven
# Stage 2: Runtime với Tomcat
# ====================================

# =============== BUILD STAGE ===============
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml và download dependencies trước (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests=true

# =============== RUNTIME STAGE ===============
FROM tomcat:10.1-jdk17-openjdk-slim

# Metadata
LABEL maintainer="busbooking-team"
LABEL description="Bus Booking Web Application"
LABEL version="1.0"

# Set environment variables
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"

# Environment variables for email configuration
ENV EMAIL_SMTP_HOST=smtp.gmail.com
ENV EMAIL_SMTP_PORT=587
ENV EMAIL_FROM=busbookingservice24@gmail.com
ENV EMAIL_PASSWORD=defaultpassword

# Environment variables for database
ENV DB_URL=jdbc:postgresql://localhost:5432/busbooking
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=password

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file từ build stage
COPY --from=builder /app/target/Web-project.war /usr/local/tomcat/webapps/ROOT.war

# Create directories for logs và temp files
RUN mkdir -p /usr/local/tomcat/logs /usr/local/tomcat/temp

# Set proper permissions
RUN chmod +x /usr/local/tomcat/bin/catalina.sh

# Expose port (Render sẽ tự động detect)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Install curl cho health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create startup script
RUN echo '#!/bin/bash\n\
echo "🚀 Starting Bus Booking Application..."\n\
echo "📧 Email Config: $EMAIL_FROM @ $EMAIL_SMTP_HOST:$EMAIL_SMTP_PORT"\n\
echo "🗄️ Database: $DB_URL"\n\
echo "💾 Memory: $JAVA_OPTS"\n\
\n\
# Set Java system properties\n\
export JAVA_OPTS="$JAVA_OPTS \
-DEMAIL_SMTP_HOST=$EMAIL_SMTP_HOST \
-DEMAIL_SMTP_PORT=$EMAIL_SMTP_PORT \
-DEMAIL_FROM=$EMAIL_FROM \
-DEMAIL_PASSWORD=$EMAIL_PASSWORD \
-DDB_URL=$DB_URL \
-DDB_USERNAME=$DB_USERNAME \
-DDB_PASSWORD=$DB_PASSWORD"\n\
\n\
echo "✅ Java Options: $JAVA_OPTS"\n\
\n\
# Start Tomcat\n\
exec catalina.sh run' > /usr/local/tomcat/bin/start.sh

RUN chmod +x /usr/local/tomcat/bin/start.sh

# Run the startup script
CMD ["/usr/local/tomcat/bin/start.sh"]
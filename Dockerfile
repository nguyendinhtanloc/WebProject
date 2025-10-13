# =========================
# Stage 1: Build với Maven
# =========================
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build file .war
RUN mvn clean package

# =========================
# Stage 2: Deploy với Tomcat
# =========================
FROM tomcat:9.0-jdk17

# Khai báo cổng cho Render biết
ENV PORT=8080

# Sửa file cấu hình Tomcat để lắng nghe đúng cổng
RUN sed -i 's/port="8080"/port="'$PORT'"/' /usr/local/tomcat/conf/server.xml

# Xóa ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy file .war đã build từ stage 1
COPY --from=build /app/target/BusBooking.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8081
EXPOSE 8080

# Chạy Tomcat
CMD ["catalina.sh", "run"]
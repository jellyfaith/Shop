# --- 第一阶段：构建阶段 ---
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
# 复制 pom.xml 和源代码
COPY pom.xml .
COPY src ./src
# 执行打包，跳过测试以加快速度
RUN mvn clean package -DskipTests

# --- 第二阶段：运行阶段 ---
FROM openjdk:17-jdk-slim
WORKDIR /app
# 从第一阶段复制生成的 jar 包
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# --- 第一阶段：构建阶段 ---
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# 1. 先复制 Maven Wrapper 相关文件
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# 2. 赋予 mvnw 执行权限 (非常重要！)
RUN chmod +x mvnw

# 3. 预下载依赖 (这一步可选，但能利用缓存加速后续构建)
# 如果网络不好，这一步可能会卡住，可以注释掉直接跑下面的 package
# RUN ./mvnw dependency:go-offline

# 4. 复制源代码
COPY src ./src

# 5. 执行打包
# 使用 ./mvnw 而不是 mvn
RUN ./mvnw clean package -DskipTests

# --- 第二阶段：运行阶段 ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

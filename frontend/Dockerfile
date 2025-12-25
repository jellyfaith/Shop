# --- 第一阶段：构建阶段 ---
FROM node:18 AS build-stage
WORKDIR /app
COPY package*.json ./
# 使用淘宝源可能会快一点，或者直接 npm install
RUN npm install --registry=https://registry.npmmirror.com
COPY . .
RUN npm run build

# --- 第二阶段：运行阶段 ---
FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
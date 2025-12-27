# 🛍️ Modern Shop Platform

<div align="center">

![Vue](https://img.shields.io/badge/Vue-3.3+-4FC08D?style=flat-square&logo=vue.js&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-007396?style=flat-square&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
[![Build CI](https://github.com/jellyfaith/Shop/actions/workflows/build.yml/badge.svg)](https://github.com/jellyfaith/Shop/actions/workflows/build.yml)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-pink?style=flat-square)

**一个基于 Spring Boot 3 和 Vue 3 构建的现代化全栈电商平台。**
*A modern, full-stack e-commerce platform built with Spring Boot 3 and Vue 3.*

[查看演示 (Demo)](https://your-demo-link.com) · [报告 Bug (Issues)](https://github.com/your-username/shop/issues) · [请求功能 (Feature Request)](https://github.com/your-username/shop/issues)

</div>

---

## 📖 目录 (Table of Contents)

- [🛍️ Modern Shop Platform](#️-modern-shop-platform)
  - [📖 目录 (Table of Contents)](#-目录-table-of-contents)
  - [📝 简介 (Introduction)](#-简介-introduction)
  - [✨ 功能特性 (Features)](#-功能特性-features)
  - [🛠 技术栈 (Tech Stack)](#-技术栈-tech-stack)
    - [Frontend (前端)](#frontend-前端)
    - [Backend (后端)](#backend-后端)
  - [📂 项目结构 (Project Structure)](#-项目结构-project-structure)
  - [🚀 快速开始 (Getting Started)](#-快速开始-getting-started)
    - [环境要求](#环境要求)
    - [1. 克隆项目](#1-克隆项目)
    - [2. 后端启动 (Backend)](#2-后端启动-backend)
    - [3. 前端启动 (Frontend)](#3-前端启动-frontend)
    - [4. 打包构建 (Build)](#4-打包构建-build)
  - [📸 项目截图 (Screenshots)](#-项目截图-screenshots)
  - [🤝 贡献指南 (Contributing)](#-贡献指南-contributing)
  - [📄 许可证 (License)](#-许可证-license)

---

## 📝 简介 (Introduction)

**Modern Shop Platform** 是一套前后端分离的开源电商解决方案。它旨在为开发者提供一个轻量级、高性能且易于扩展的电商系统脚手架。

项目采用行业领先的技术栈：后端使用 **Spring Boot 3.2** 配合 **MyBatis Plus** 确保高效开发，前端采用 **Vue 3** 全家桶结合 **Tailwind CSS** 打造流畅的用户体验。无论是用于学习全栈开发，还是作为中小型电商项目的起点，它都是一个绝佳的选择。

---

## ✨ 功能特性 (Features)

- 🔐 **安全认证**：基于 JWT 的用户认证与授权系统，支持拦截器鉴权。
- 🛒 **商城核心**：完整的商品展示、购物车管理、订单创建流程。
- 📊 **管理后台**：独立的后台管理界面，支持商品 CRUD 与数据看板。
- 🌍 **国际化 (i18n)**：内置中英文多语言切换支持 (`vue-i18n`)。
- 🎨 **现代化 UI**：使用 Tailwind CSS 构建的响应式界面，适配移动端与桌面端。
- 📄 **API 文档**：集成 Knife4j (Swagger 3)，自动生成在线接口文档。
- ⚡ **高性能**：集成 Redis 缓存，提升系统响应速度。

---

## 🛠 技术栈 (Tech Stack)

### Frontend (前端)
- **Core**: Vue 3, Vite 5
- **State Management**: Pinia
- **Routing**: Vue Router 4
- **UI Framework**: Tailwind CSS
- **HTTP Client**: Axios
- **I18n**: Vue I18n

### Backend (后端)
- **Core**: Spring Boot 3.2, Java 17
- **Database**: MySQL 8.0, MyBatis Plus
- **Cache**: Redis
- **Security**: JWT (JSON Web Token)
- **API Docs**: Knife4j (Swagger 3)
- **Tools**: Lombok, Maven

---

## 📂 项目结构 (Project Structure)

```tree
Shop/
├── deploy/                 # Nginx 部署配置
├── frontend/               # Vue 3 前端项目源码
│   ├── src/
│   │   ├── components/     # 公共组件
│   │   ├── layout/         # 布局文件 (Shop/Backend)
│   │   ├── locales/        # 国际化资源文件
│   │   ├── stores/         # Pinia 状态管理
│   │   └── views/          # 页面视图 (Admin/Shop)
│   └── vite.config.js      # Vite 配置
├── src/                    # Spring Boot 后端源码
│   └── main/
│       ├── java/com/shop/  # Java 源代码
│       │   ├── config/     # 全局配置 (Web, Redis, Swagger)
│       │   ├── controller/ # 控制器层
│       │   ├── entity/     # 数据库实体
│       │   └── service/    # 业务逻辑层
│       └── resources/      # 配置文件与 SQL 脚本
├── pom.xml                 # Maven 依赖管理
└── README.md               # 项目说明文档
```

---

## 🚀 快速开始 (Getting Started)

### 环境要求
在开始之前，请确保你的开发环境满足以下要求：
*   **Java**: JDK 17+
*   **Node.js**: 18.0+
*   **Database**: MySQL 8.0+
*   **Cache**: Redis 6.0+

### 1. 克隆项目
```bash
git clone https://github.com/your-username/shop.git
cd Shop
```

### 2. 后端启动 (Backend)

1.  创建 MySQL 数据库 `shop_db` 并导入 `src/main/resources/sql/schema.sql` 和 `data.sql`。
2.  修改配置文件 `src/main/resources/application-secret.properties` (如果不存在请创建)，配置你的数据库和 Redis 信息：
    ```properties
    DB_USERNAME=root
    DB_PASSWORD=your_password
    JWT_SECRET=your_secret_key
    ```
3.  运行 Spring Boot 应用：
    ```bash
    # Windows
    .\mvnw spring-boot:run

    # Linux/Mac
    ./mvnw spring-boot:run
    ```
    后端服务将在 `http://localhost:8080` 启动。

### 3. 前端启动 (Frontend)

进入前端目录并安装依赖：

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```
前端页面将在 `http://localhost:3000` (默认) 启动。

### 4. 打包构建 (Build)

```bash
# 构建前端资源
cd frontend
npm run build

# 构建后端 Jar 包 (会自动包含前端构建产物，视配置而定)
cd ..
.\mvnw clean package
```

---

## 📸 项目截图 (Screenshots)

| 商城首页 (Home) | 后台管理 (Dashboard) |
|:---:|:---:|
| ![Home Page](https://via.placeholder.com/400x250?text=Shop+Home+Page) | ![Admin Dashboard](https://via.placeholder.com/400x250?text=Admin+Dashboard) |
| *响应式商品展示* | *数据概览与管理* |

| 购物车 (Cart) | 移动端适配 (Mobile) |
|:---:|:---:|
| ![Cart](https://via.placeholder.com/400x250?text=Shopping+Cart) | ![Mobile View](https://via.placeholder.com/400x250?text=Mobile+Responsive) |

---

## 🤝 贡献指南 (Contributing)

我们非常欢迎社区的贡献！如果你有任何改进建议或发现了 Bug，请随时提交 Pull Request 或 Issue。

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

---

## 📄 许可证 (License)

本项目基于 [MIT License](LICENSE) 开源。

---

<div align="center">
  <b>Happy Coding! 🚀</b>
</div>

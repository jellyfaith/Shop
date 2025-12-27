# Shop - 简易电商商城系统

## 个人信息
- **学号**: [202330452221]
- **姓名**: [曾宪哲]
- **代码仓库**: [请填写您的 GitHub/Gitee 仓库地址]

## 项目介绍
本项目是一个基于前后端分离架构的简易电商系统。后端采用 Spring Boot + MyBatis Plus，前端采用 Vue 3 + Element Plus。实现了用户注册登录、商品浏览、购物车、下单购买、以及后台管理（商品管理、用户管理、日志查看）等核心功能。

## 模块说明

### 1. 认证授权模块
基于 JWT (JSON Web Token) 实现无状态认证。
- **普通用户**: 注册、登录、购物。
- **管理员**: 登录后台、管理商品和用户。
- **实现**: 通过拦截器 (`JwtInterceptor`) 拦截请求，解析 Token 中的用户信息。

### 2. 商品模块
- **前台**: 商品列表展示、详情页、搜索。
- **后台**: 商品的增删改查、上下架管理。
- **实现**: `ProductController` 提供公共查询接口，`AdminProductController` 提供管理接口。

### 3. 订单模块
- 核心业务逻辑，处理订单创建、库存扣减。
- 支持查看订单详情和历史订单。
- **实现**: `OrderServiceImpl` 处理事务，确保库存扣减和订单生成的一致性。

### 4. 用户行为日志模块
- 记录用户的关键操作（浏览商品、购买商品）。
- 后台管理员可查看特定用户的行为日志。
- **实现**: 采用“发送即忘”模式，在业务关键节点（如查看详情、下单成功）异步或同步调用 `UserLogService` 写入日志。

### 5. 基础设施
- 全局异常处理 (`GlobalExceptionHandler`)。
- Swagger 接口文档集成。
- 跨域配置 (`WebMvcConfig`)。

## 文件结构说明

### 后端 (src/main/java/com/shop)
- `config/`: 配置类 (WebMvc, Swagger, MyBatis等)
- `controller/`: 控制器层 (API 接口)
    - `admin/`: 后台管理接口 (如 `AdminUserController`, `AdminProductController`)
    - `shop/`: 前台商城接口 (如 `ShopProductController`, `ShopOrderController`)
- `entity/`: 数据库实体类 (`User`, `Product`, `OrderMaster`, `UserLog`)
- `service/`: 业务逻辑层接口及实现 (`UserService`, `ProductService`, `UserLogService`)
- `repository/`: 数据访问层 (Mapper)
- `utils/`: 工具类 (`JwtUtil`, `Result` 等)

### 前端 (frontend/src)
- `views/`: 页面组件
    - `backend/`: 后台管理页面 (`UserManage.vue`, `ProductManage.vue`)
    - `shop/`: 前台商城页面 (`Home.vue`, `ProductDetail.vue`)
- `components/`: 公共组件
- `router/`: 路由配置
- `stores/`: Pinia 状态管理 (`user.js`, `cart.js`)
- `utils/`: 工具函数 (`request.js` 封装 Axios)

## 运行说明

### 后端
1. 确保本地安装 MySQL 8.0+ 和 JDK 17+。
2. 创建数据库并导入 `src/main/resources/sql` 目录下的 SQL 文件。
3. 修改 `src/main/resources/application.properties` 中的数据库连接信息。
4. 运行 `ShopApplication.java` 启动服务。

### 前端
1. 确保本地安装 Node.js 16+。
2. 进入 `frontend` 目录。
3. 运行 `npm install` 安装依赖。
4. 运行 `npm run dev` 启动开发服务器。

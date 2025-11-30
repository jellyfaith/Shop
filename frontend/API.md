# 接口文档 (Frontend Requirements)

## 待办事项 (TODO)
- [ ] 完善订单模块接口
- [ ] 补充错误码定义表
- [ ] 完成用户登录接口定义
- [ ] 确认图片上传接口格式

本文档用于定义前端所需的后端接口格式。

## 通用说明
- **Base URL**: `/api`
- **数据格式**: JSON
- **通用响应结构**:
  ```json
  {
    "code": 200,      // 200: 成功, 其他: 错误码
    "message": "success", // 错误信息
    "data": { ... }   // 具体数据
  }
  ```

---

## 1. 用户模块 (User)

### 1.1 用户登录
- **URL**: `/user/login`
- **Method**: `POST`
- **描述**: 用户登录获取 Token
- **请求参数**:
  ```json
  {
    "username": "admin",
    "password": "password123"
  }
  ```
- **响应数据**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1Ni...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "avatar": "https://example.com/avatar.jpg",
      "role": "admin" // "admin" | "user"
    }
  }
  ```

### 1.2 获取用户信息
- **URL**: `/user/info`
- **Method**: `GET`
- **Header**: `Authorization: Bearer <token>`
- **响应数据**:
  ```json
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com"
  }
  ```

---

## 2. 商品模块 (Product)

### 2.1 获取商品列表
- **URL**: `/product/list`
- **Method**: `GET`
- **Query 参数**:
  - `page`: 页码 (默认 1)
  - `size`: 每页数量 (默认 10)
  - `keyword`: 搜索关键字 (可选)
- **响应数据**:
  ```json
  {
    "total": 100,
    "list": [
      {
        "id": 101,
        "name": "无线耳机",
        "price": 299.00,
        "cover": "https://example.com/p1.jpg",
        "stock": 50
      }
    ]
  }
  ```

---

## 3. 购物车模块 (Cart)

### 3.1 添加购物车
- **URL**: `/cart/add`
- **Method**: `POST`
- **请求参数**:
  ```json
  {
    "productId": 101,
    "count": 1
  }
  ```

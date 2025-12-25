# 接口文档 (Frontend Requirements)

## 待办事项 (TODO)
- [ ] 补充错误码定义表
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
- **URL**: `/shop/user/login`
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
  "eyJhbGciOiJIUzI1Ni..." // Token 字符串
  ```

### 1.2 用户注册
- **URL**: `/shop/user/register`
- **Method**: `POST`
- **请求参数**:
  ```json
  {
    "username": "user1",
    "password": "password123",
    "email": "user1@example.com",
    "phone": "13800138000",
    "address": "Default Address"
  }
  ```
- **响应数据**: "注册成功"

### 1.3 获取用户信息
- **URL**: `/shop/user/info`
- **Method**: `GET`
- **Header**: `Authorization: Bearer <token>`
- **响应数据**:
  ```json
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "phone": "13800138000",
    "address": "User Address",
    "createTime": "2023-01-01T12:00:00"
  }
  ```

### 1.4 更新用户信息
- **URL**: `/shop/user/update`
- **Method**: `PUT`
- **请求参数**:
  ```json
  {
    "email": "new@example.com",
    "phone": "13900139000",
    "address": "New Address"
  }
  ```
- **响应数据**: "更新成功"

---

## 2. 商品模块 (Product)

### 2.1 获取商品列表
- **URL**: `/shop/product/list`
- **Method**: `GET`
- **Query 参数**:
  - `page`: 页码 (默认 1)
  - `size`: 每页数量 (默认 20)
  - `keyword`: 搜索关键字 (可选)
  - `categoryId`: 分类ID (可选)
  - `minPrice`: 最低价格 (可选)
  - `maxPrice`: 最高价格 (可选)
- **响应数据**:
  ```json
  {
    "records": [
      {
        "id": 101,
        "categoryId": 1,
        "name": "无线耳机",
        "description": "高音质...",
        "mainImage": "https://example.com/p1.jpg",
        "minPrice": 299.00,
        "status": 1
      }
    ],
    "total": 100,
    "size": 20,
    "current": 1
  }
  ```

### 2.2 获取商品详情
- **URL**: `/shop/product/{id}`
- **Method**: `GET`
- **响应数据**:
  ```json
  {
    "product": {
      "id": 101,
      "name": "无线耳机",
      "minPrice": 299.00,
      ...
    },
    "skus": [
      {
        "id": 1,
        "productId": 101,
        "name": "白色",
        "price": 299.00,
        "stock": 100,
        ...
      }
    ]
  }
  ```

---

## 3. 分类模块 (Category)

### 3.1 获取所有分类
- **URL**: `/category/list`
- **Method**: `GET`
- **响应数据**:
  ```json
  [
    {
      "id": 1,
      "name": "电子产品",
      "sort": 1
    },
    {
      "id": 2,
      "name": "服装",
      "sort": 2
    }
  ]
  ```

---

## 4. 购物车模块 (Cart)

### 4.1 获取购物车列表
- **URL**: `/shop/cart`
- **Method**: `GET`
- **响应数据**:
  ```json
  {
    "items": [
      {
        "productId": 101,
        "skuId": 1,
        "productName": "无线耳机",
        "skuName": "白色",
        "price": 299.00,
        "quantity": 2,
        "image": "..."
      }
    ],
    "totalPrice": 598.00,
    "totalQuantity": 2
  }
  ```

### 4.2 添加商品到购物车
- **URL**: `/shop/cart`
- **Method**: `POST`
- **请求参数**:
  ```json
  {
    "productId": 101,
    "skuId": 1,
    "quantity": 1
  }
  ```
- **响应数据**: "添加成功"

### 4.3 更新购物车商品数量
- **URL**: `/shop/cart`
- **Method**: `PUT`
- **请求参数**:
  ```json
  {
    "productId": 101,
    "quantity": 3
  }
  ```
- **响应数据**: "更新成功"

### 4.4 删除购物车商品
- **URL**: `/shop/cart/{productId}`
- **Method**: `DELETE`
- **响应数据**: "删除成功"

---

## 5. 订单模块 (Order)

### 5.1 创建订单
- **URL**: `/shop/order/create`
- **Method**: `POST`
- **请求参数**:
  ```json
  {
    "receiverId": 1, // 可选，选择已有地址
    "receiverName": "张三", // 如果未选地址，需填
    "receiverPhone": "13800000000",
    "receiverAddress": "北京市...",
    "paymentMethod": "WECHAT",
    "remark": "尽快发货"
  }
  ```
- **响应数据**: "202312260001" (订单编号)

### 5.2 获取订单列表
- **URL**: `/shop/order/list`
- **Method**: `GET`
- **响应数据**:
  ```json
  [
    {
      "orderNo": "202312260001",
      "totalAmount": 299.00,
      "status": 0, // 0:待支付, 1:待发货, 2:待收货, 3:已完成, 4:已取消
      "createTime": "2023-12-26T10:00:00",
      "items": [...]
    }
  ]
  ```

### 5.3 获取订单详情
- **URL**: `/shop/order/{orderNo}`
- **Method**: `GET`
- **响应数据**:
  ```json
  {
    "orderNo": "202312260001",
    "totalAmount": 299.00,
    "status": 0,
    "receiverName": "张三",
    "receiverPhone": "13800000000",
    "receiverAddress": "北京市...",
    "items": [
      {
        "productId": 101,
        "productName": "无线耳机",
        "price": 299.00,
        "quantity": 1,
        ...
      }
    ]
  }
  ```

### 5.4 模拟支付
- **URL**: `/shop/order/pay/{orderNo}`
- **Method**: `POST`
- **响应数据**: "支付成功"

---

## 6. 收货地址模块 (Receiver)

### 6.1 获取收货地址列表
- **URL**: `/shop/receiver/list`
- **Method**: `GET`
- **响应数据**:
  ```json
  [
    {
      "id": 1,
      "name": "张三",
      "phone": "13800000000",
      "address": "北京市...",
      "isDefault": 1
    }
  ]
  ```

### 6.2 添加收货地址
- **URL**: `/shop/receiver/add`
- **Method**: `POST`
- **请求参数**:
  ```json
  {
    "name": "李四",
    "phone": "13900000000",
    "address": "上海市...",
    "isDefault": 0
  }
  ```
- **响应数据**: "添加成功"

### 6.3 更新收货地址
- **URL**: `/shop/receiver/update`
- **Method**: `PUT`
- **请求参数**:
  ```json
  {
    "id": 2,
    "name": "李四",
    "phone": "13900000000",
    "address": "上海市...",
    "isDefault": 1
  }
  ```
- **响应数据**: "更新成功"

### 6.4 删除收货地址
- **URL**: `/shop/receiver/{id}`
- **Method**: `DELETE`
- **响应数据**: "删除成功"

### 6.5 设为默认地址
- **URL**: `/shop/receiver/default/{id}`
- **Method**: `POST`
- **响应数据**: "设置成功"


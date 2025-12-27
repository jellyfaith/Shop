-- MySQL dump 10.13  Distrib 9.4.0, for Win64 (x86_64)
--
-- Host: localhost    Database: shop_db
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `shop_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `shop_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `shop_db`;

--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

LOCK TABLES `admin_user` WRITE;
/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES (1,'admin','$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGqq0jd2zm6adLI.ungxuRnFoldDOu',NULL,NULL,'2025-12-22 00:46:00','2025-12-22 00:46:00',0);
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (13,'jellyfaith',5,1,'2025-12-22 02:34:18','2025-12-22 02:34:18');
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称快照',
  `specs` json DEFAULT NULL COMMENT '规格快照',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格快照',
  `quantity` int NOT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (1,1,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,1,'2025-12-07 18:34:05'),(2,2,43,43,'Logitech G502','{\"color\": \"黑色\"}',299.00,1,'2025-12-07 18:34:05'),(3,3,6,6,'MacBook Pro 14','{\"chip\": \"M3 Max\", \"color\": \"深空黑\"}',14999.00,1,'2025-12-07 18:34:05'),(4,4,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,2,NULL),(5,5,2,2,'Samsung Galaxy S24','{\"color\": \"钛灰\", \"storage\": \"256GB\"}',5999.00,1,NULL),(6,6,1,52,'iPhone 15 Pro','{\"color\": \"白色钛金属\", \"storage\": \"512GB\"}',9999.00,1,'2025-12-09 03:10:28'),(7,7,2,54,'Samsung Galaxy S24','{\"color\": \"钛黑\", \"storage\": \"512GB\"}',6999.00,1,'2025-12-09 03:13:15'),(8,8,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,4,'2025-12-09 03:15:56'),(9,9,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,3,'2025-12-09 03:22:22'),(10,9,2,54,'Samsung Galaxy S24','{\"color\": \"钛黑\", \"storage\": \"512GB\"}',6999.00,3,'2025-12-09 03:22:22'),(11,10,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,1,'2025-12-09 12:49:09'),(12,11,1,52,'iPhone 15 Pro','{\"color\": \"白色钛金属\", \"storage\": \"512GB\"}',9999.00,4,'2025-12-22 02:08:58'),(13,11,1,52,'iPhone 15 Pro','{\"color\": \"白色钛金属\", \"storage\": \"512GB\"}',9999.00,4,'2025-12-22 02:08:58'),(14,12,1,51,'iPhone 15 Pro','{\"color\": \"蓝色钛金属\", \"storage\": \"256GB\"}',7999.00,1,'2025-12-22 02:19:10'),(15,13,1,1,'iPhone 15 Pro','{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,90,'2025-12-22 15:47:55');
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_master`
--

DROP TABLE IF EXISTS `order_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_master` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态 0-待付款 1-已付款 2-已发货 3-已完成 4-已取消',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_master`
--

LOCK TABLES `order_master` WRITE;
/*!40000 ALTER TABLE `order_master` DISABLE KEYS */;
INSERT INTO `order_master` VALUES (1,'ORD20231125001',1,7999.00,3,NULL,NULL,NULL,'2025-12-02 18:34:05','2025-12-07 18:34:05',0,NULL,NULL,NULL,NULL),(2,'ORD20231125002',1,299.00,2,NULL,NULL,NULL,'2025-12-05 18:34:05','2025-12-07 18:34:05',0,NULL,NULL,NULL,NULL),(3,'ORD20231125003',2,14999.00,1,NULL,NULL,NULL,'2025-12-07 18:34:05','2025-12-07 18:34:05',0,NULL,NULL,NULL,NULL),(4,'d9dbfc8caba9433f955edfda3bf3af53',4,15998.00,1,'2025-12-09 02:54:25',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(5,'4664a3ff71184b78b4be9d5f73e4dc77',4,5999.00,1,'2025-12-09 02:56:08',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),(6,'770ce1c182bf491e9cb8673689c17c30',4,9999.00,2,'2025-12-09 03:10:33','2025-12-22 02:32:22',NULL,'2025-12-09 03:10:28','2025-12-09 03:10:28',0,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区',NULL),(7,'02fab534ad0242299d6d504c0ed5ba77',4,6999.00,1,'2025-12-09 03:21:54',NULL,NULL,'2025-12-09 03:13:15','2025-12-09 03:13:15',0,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区',NULL),(8,'a81ea99c36af4393adbde0980502e9e9',4,31996.00,1,'2025-12-09 03:15:57',NULL,NULL,'2025-12-09 03:15:56','2025-12-09 03:15:56',0,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区',NULL),(9,'2025120903222267550004',4,44994.00,1,'2025-12-09 03:22:26',NULL,NULL,'2025-12-09 03:22:22','2025-12-09 03:22:22',0,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区','WECHAT'),(10,'2025120912490837320004',4,7999.00,2,'2025-12-22 00:50:07','2025-12-22 15:49:02',NULL,'2025-12-09 12:49:09','2025-12-09 12:49:09',0,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区','ALIPAY'),(11,'2025122202085783580004',4,79992.00,1,'2025-12-22 02:09:00',NULL,NULL,'2025-12-22 02:08:58','2025-12-22 02:08:58',0,'jellyZ1122','19200801080','广州市番禺区大学城外环东路382号华南理工大学大学城校区','WECHAT'),(12,'2025122202191069640004',4,7999.00,2,'2025-12-22 02:19:15','2025-12-22 15:49:00',NULL,'2025-12-22 02:19:10','2025-12-22 02:19:10',0,'jellyZ1122','19200801080','广州市番禺区大学城外环东路382号华南理工大学大学城校区','WECHAT'),(13,'2025122215475491808386',2003009175596048386,719910.00,2,'2025-12-22 15:47:59','2025-12-22 15:48:55',NULL,'2025-12-22 15:47:55','2025-12-22 15:47:55',0,'1','1','1','WECHAT');
/*!40000 ALTER TABLE `order_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `main_image` varchar(255) DEFAULT NULL COMMENT '商品主图',
  `min_price` decimal(10,2) DEFAULT NULL COMMENT '最低展示价',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1-上架 0-下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表(SPU)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,'iPhone 15 Pro','钛金属设计，A17 Pro 芯片','https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=500&q=60',7999.00,1,'2025-12-07 18:33:53','2025-12-26 03:32:54',1),(2,1,'Samsung Galaxy S24','AI 手机，超视觉夜拍','https://images.unsplash.com/photo-1610945265078-3858a0828671?auto=format&fit=crop&w=500&q=60',5999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(3,1,'Xiaomi 14','徕卡光学镜头，骁龙8 Gen3','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',3999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(4,1,'Google Pixel 8','Google AI，纯净安卓','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',4999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(5,1,'OnePlus 12','十年超越之作','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',4299.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(6,2,'MacBook Pro 14','M3 Max 芯片，深空黑','https://images.unsplash.com/photo-1517336714731-489689fd1ca4?auto=format&fit=crop&w=500&q=60',14999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(7,2,'Dell XPS 13','微边框设计，轻薄便携','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',9999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(8,2,'ThinkPad X1 Carbon','商务旗舰，碳纤维机身','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',11999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(9,2,'HP Spectre x360','翻转触控，OLED 屏幕','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',10999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(10,2,'ASUS ROG Zephyrus','游戏本，RTX 4090','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',18999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(11,3,'iPad Pro 12.9','M2 芯片，XDR 显示屏','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',8499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(12,3,'iPad Air 5','M1 芯片，多彩设计','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',4799.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(13,3,'Samsung Galaxy Tab S9','AMOLED 屏幕，S Pen','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',5499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(14,4,'Apple Watch Ultra 2','极限运动，双频 GPS','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',6499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(15,4,'Apple Watch Series 9','S9 SiP，双指互点','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',2999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(16,5,'AirPods Pro 2','主动降噪，自适应通透','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',1899.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(17,5,'Sony WH-1000XM5','头戴式降噪耳机','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',2499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(18,5,'Bose QC45','消噪耳机，舒适佩戴','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',1999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(19,6,'Anker 65W Charger','氮化镓充电器','https://images.unsplash.com/photo-1583863788434-e58a36330cf0?auto=format&fit=crop&w=500&q=60',199.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(20,6,'Logitech MX Master 3S','无线鼠标，静音滚轮','https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?auto=format&fit=crop&w=500&q=60',699.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(21,1,'Huawei Mate 60 Pro','卫星通话，昆仑玻璃','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',6999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(22,1,'Honor Magic 6','鹰眼相机，青海湖电池','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',4399.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(23,1,'Vivo X100','蔡司影像，蓝晶芯片','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',3999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(24,1,'Oppo Find X7','哈苏大师影像','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',3999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(25,2,'Microsoft Surface Laptop','触控屏，Alcantara 材质','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',8999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(26,2,'Razer Blade 15','灵刃游戏本','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',15999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(27,2,'LG Gram 16','超轻薄，长续航','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',9999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(28,3,'Microsoft Surface Pro 9','二合一平板电脑','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',7999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(29,3,'Lenovo Tab P12','大屏平板，学习办公','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',2999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(30,4,'Huawei Watch GT 4','时尚设计，科学运动','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',1488.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(31,4,'Garmin Fenix 7','户外运动手表','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',5999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(32,5,'Marshall Major IV','摇滚复古，无线蓝牙','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',1299.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(33,5,'JBL Flip 6','便携蓝牙音箱','https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=500&q=60',899.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(34,6,'Keychron K2','机械键盘，Mac 适配','https://images.unsplash.com/photo-1587829741301-dc798b91a603?auto=format&fit=crop&w=500&q=60',499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(35,6,'SanDisk SSD 1TB','移动固态硬盘','https://images.unsplash.com/photo-1597872252165-4828a1561780?auto=format&fit=crop&w=500&q=60',699.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(36,1,'Redmi K70','性价比之王','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',2499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(37,1,'Realme GT 5','极速秒充','https://images.unsplash.com/photo-1598327105666-5b89351aff23?auto=format&fit=crop&w=500&q=60',2999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(38,2,'Acer Swift Go','轻薄本，OLED','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',5999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(39,2,'MacBook Air 15','大屏轻薄，M2','https://images.unsplash.com/photo-1517336714731-489689fd1ca4?auto=format&fit=crop&w=500&q=60',10499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(40,3,'iPad mini 6','小屏旗舰','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',3999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(41,4,'Xiaomi Band 8','智能手环','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',249.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(42,5,'Sony WF-1000XM5','降噪豆','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',1999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(43,6,'Logitech G502','游戏鼠标','https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?auto=format&fit=crop&w=500&q=60',299.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(44,6,'Samsung T7','移动硬盘','https://images.unsplash.com/photo-1597872252165-4828a1561780?auto=format&fit=crop&w=500&q=60',599.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(45,1,'iPhone 14','超值入手','https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=500&q=60',5399.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(46,1,'iPhone 13','经典款','https://images.unsplash.com/photo-1695048133142-1a20484d2569?auto=format&fit=crop&w=500&q=60',4399.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(47,2,'Lenovo Legion R9000P','拯救者游戏本','https://images.unsplash.com/photo-1593642632823-8f78536788c6?auto=format&fit=crop&w=500&q=60',8999.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(48,3,'Huawei MatePad Pro','鸿蒙平板','https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=500&q=60',4299.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(49,4,'Amazfit GTR 4','长续航手表','https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?auto=format&fit=crop&w=500&q=60',1099.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(50,5,'Beats Studio Pro','头戴式耳机','https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=60',2499.00,1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1,'智能手机',1,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(2,'笔记本电脑',2,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(3,'平板电脑',3,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(4,'智能穿戴',4,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(5,'影音娱乐',5,'2025-12-07 18:33:53','2025-12-07 18:33:53',0),(6,'数码配件',6,'2025-12-07 18:33:53','2025-12-07 18:33:53',0);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_sku`
--

DROP TABLE IF EXISTS `product_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` bigint NOT NULL COMMENT '关联的主商品ID',
  `specs` json DEFAULT NULL COMMENT '规格参数',
  `price` decimal(10,2) NOT NULL COMMENT '该规格的价格',
  `stock` int NOT NULL DEFAULT '0' COMMENT '该规格的库存',
  `image` varchar(255) DEFAULT NULL COMMENT '该规格的专属图片',
  `sku_code` varchar(64) DEFAULT NULL COMMENT '商家编码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品规格(SKU)表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_sku`
--

LOCK TABLES `product_sku` WRITE;
/*!40000 ALTER TABLE `product_sku` DISABLE KEYS */;
INSERT INTO `product_sku` VALUES (1,1,'{\"color\": \"原色钛金属\", \"storage\": \"256GB\"}',7999.00,0,NULL,'IP15P-TI-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(2,2,'{\"color\": \"钛灰\", \"storage\": \"256GB\"}',5999.00,99,NULL,'S24-GR-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(3,3,'{\"color\": \"黑色\", \"storage\": \"256GB\"}',3999.00,100,NULL,'MI14-BK-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(4,4,'{\"color\": \"黑曜石\", \"storage\": \"128GB\"}',4999.00,100,NULL,'PXL8-BK-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(5,5,'{\"color\": \"留白\", \"storage\": \"256GB\"}',4299.00,100,NULL,'OP12-WH-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(6,6,'{\"chip\": \"M3 Max\", \"color\": \"深空黑\"}',14999.00,50,NULL,'MBP14-BK-M3','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(7,7,'{\"cpu\": \"i7\", \"color\": \"银色\"}',9999.00,50,NULL,'XPS13-SV-I7','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(8,8,'{\"cpu\": \"i7\", \"color\": \"黑色\"}',11999.00,50,NULL,'X1C-BK-I7','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(9,9,'{\"cpu\": \"i7\", \"color\": \"皇室蓝\"}',10999.00,50,NULL,'HP360-BL-I7','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(10,10,'{\"gpu\": \"RTX4090\", \"color\": \"黑色\"}',18999.00,20,NULL,'ROG-BK-4090','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(11,11,'{\"color\": \"深空灰\", \"storage\": \"128GB\"}',8499.00,100,NULL,'IPAD-PRO-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(12,12,'{\"color\": \"蓝色\", \"storage\": \"64GB\"}',4799.00,100,NULL,'IPAD-AIR-64','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(13,13,'{\"color\": \"灰色\", \"storage\": \"128GB\"}',5499.00,100,NULL,'TAB-S9-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(14,14,'{\"band\": \"高山回环\", \"color\": \"钛金属\"}',6499.00,100,NULL,'AW-ULTRA-2','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(15,15,'{\"size\": \"45mm\", \"color\": \"午夜色\"}',2999.00,100,NULL,'AW-S9-45','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(16,16,'{\"color\": \"白色\"}',1899.00,200,NULL,'APP2-WH','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(17,17,'{\"color\": \"黑色\"}',2499.00,100,NULL,'XM5-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(18,18,'{\"color\": \"黑色\"}',1999.00,100,NULL,'QC45-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(19,19,'{\"color\": \"黑色\"}',199.00,500,NULL,'ANKER-65W','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(20,20,'{\"color\": \"石墨灰\"}',699.00,200,NULL,'MX3S-GR','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(21,21,'{\"color\": \"雅川青\", \"storage\": \"512GB\"}',6999.00,100,NULL,'MATE60-GR-512','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(22,22,'{\"color\": \"海湖青\", \"storage\": \"256GB\"}',4399.00,100,NULL,'MAGIC6-GR-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(23,23,'{\"color\": \"星迹蓝\", \"storage\": \"256GB\"}',3999.00,100,NULL,'X100-BL-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(24,24,'{\"color\": \"大漠银月\", \"storage\": \"256GB\"}',3999.00,100,NULL,'X7-SV-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(25,25,'{\"cpu\": \"i5\", \"color\": \"亮铂金\"}',8999.00,50,NULL,'SL-SV-I5','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(26,26,'{\"gpu\": \"RTX4070\", \"color\": \"黑色\"}',15999.00,30,NULL,'RAZER-15-4070','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(27,27,'{\"cpu\": \"i5\", \"color\": \"白色\"}',9999.00,50,NULL,'GRAM16-WH-I5','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(28,28,'{\"cpu\": \"i5\", \"color\": \"亮铂金\"}',7999.00,50,NULL,'SP9-SV-I5','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(29,29,'{\"color\": \"灰色\", \"storage\": \"128GB\"}',2999.00,100,NULL,'TAB-P12-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(30,30,'{\"size\": \"46mm\", \"color\": \"黑色\"}',1488.00,100,NULL,'GT4-BK-46','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(31,31,'{\"size\": \"47mm\", \"color\": \"黑色\"}',5999.00,50,NULL,'FENIX7-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(32,32,'{\"color\": \"黑色\"}',1299.00,100,NULL,'MAJOR4-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(33,33,'{\"color\": \"黑色\"}',899.00,100,NULL,'FLIP6-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(34,34,'{\"light\": \"RGB\", \"switch\": \"红轴\"}',499.00,100,NULL,'K2-RED-RGB','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(35,35,'{\"color\": \"黑色\", \"capacity\": \"1TB\"}',699.00,200,NULL,'SSD-1TB','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(36,36,'{\"color\": \"墨羽\", \"storage\": \"256GB\"}',2499.00,100,NULL,'K70-BK-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(37,37,'{\"color\": \"流银幻镜\", \"storage\": \"256GB\"}',2999.00,100,NULL,'GT5-SV-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(38,38,'{\"cpu\": \"i5\", \"color\": \"银色\"}',5999.00,50,NULL,'SWIFT-SV-I5','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(39,39,'{\"color\": \"午夜色\", \"storage\": \"256GB\"}',10499.00,50,NULL,'MBA15-BK-256','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(40,40,'{\"color\": \"紫色\", \"storage\": \"64GB\"}',3999.00,100,NULL,'MINI6-PL-64','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(41,41,'{\"color\": \"黑色\"}',249.00,500,NULL,'BAND8-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(42,42,'{\"color\": \"黑色\"}',1999.00,100,NULL,'XM5-EAR-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(43,43,'{\"color\": \"黑色\"}',299.00,200,NULL,'G502-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(44,44,'{\"color\": \"钛灰\", \"capacity\": \"1TB\"}',599.00,200,NULL,'T7-GR-1TB','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(45,45,'{\"color\": \"蓝色\", \"storage\": \"128GB\"}',5399.00,100,NULL,'IP14-BL-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(46,46,'{\"color\": \"粉色\", \"storage\": \"128GB\"}',4399.00,100,NULL,'IP13-PK-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(47,47,'{\"gpu\": \"RTX4060\", \"color\": \"灰色\"}',8999.00,50,NULL,'R9000P-GR-4060','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(48,48,'{\"color\": \"曜金黑\", \"storage\": \"128GB\"}',4299.00,100,NULL,'MATEPAD-BK-128','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(49,49,'{\"color\": \"黑色\"}',1099.00,100,NULL,'GTR4-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(50,50,'{\"color\": \"黑色\"}',2499.00,100,NULL,'STUDIO-BK','2025-12-07 18:33:53','2025-12-07 18:33:53',0),(51,1,'{\"color\": \"蓝色钛金属\", \"storage\": \"256GB\"}',7999.00,49,NULL,'IP15P-BL-256','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(52,1,'{\"color\": \"白色钛金属\", \"storage\": \"512GB\"}',9999.00,21,NULL,'IP15P-WH-512','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(53,1,'{\"color\": \"黑色钛金属\", \"storage\": \"1TB\"}',11999.00,20,NULL,'IP15P-BK-1TB','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(54,2,'{\"color\": \"钛黑\", \"storage\": \"512GB\"}',6999.00,46,NULL,'S24-BK-512','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(55,2,'{\"color\": \"钛紫\", \"storage\": \"256GB\"}',5999.00,80,NULL,'S24-PL-256','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(56,6,'{\"chip\": \"M3 Pro\", \"color\": \"银色\"}',12999.00,40,NULL,'MBP14-SV-M3PRO','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(57,6,'{\"chip\": \"M3 Pro\", \"color\": \"深空黑\"}',12999.00,40,NULL,'MBP14-BK-M3PRO','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(58,16,'{\"color\": \"定制黑\"}',2199.00,10,NULL,'APP2-BK-CUSTOM','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(59,34,'{\"light\": \"白光\", \"switch\": \"青轴\"}',459.00,80,NULL,'K2-BLUE-WHITE','2025-12-07 18:48:25','2025-12-07 18:48:25',0),(60,34,'{\"light\": \"RGB\", \"switch\": \"茶轴\"}',499.00,60,NULL,'K2-BROWN-RGB','2025-12-07 18:48:25','2025-12-07 18:48:25',0);
/*!40000 ALTER TABLE `product_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_stock`
--

DROP TABLE IF EXISTS `product_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_stock`
--

LOCK TABLES `product_stock` WRITE;
/*!40000 ALTER TABLE `product_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receiver`
--

DROP TABLE IF EXISTS `receiver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receiver` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `address` varchar(255) NOT NULL COMMENT '收货地址',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收货地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receiver`
--

LOCK TABLES `receiver` WRITE;
/*!40000 ALTER TABLE `receiver` DISABLE KEYS */;
INSERT INTO `receiver` VALUES (1,4,'曾宪哲','13005319911','广州市番禺区大学城外环东路382号华南理工大学大学城校区',1,'2025-12-09 03:09:34','2025-12-09 03:09:34'),(2,4,'jellyZ1122','19200801080','广州市番禺区大学城外环东路382号华南理工大学大学城校区',0,'2025-12-09 03:09:45','2025-12-09 03:09:45');
/*!40000 ALTER TABLE `receiver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_report`
--

DROP TABLE IF EXISTS `sales_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `date` date NOT NULL COMMENT '统计日期',
  `total_sales` decimal(10,2) DEFAULT '0.00' COMMENT '总销售额',
  `total_orders` int DEFAULT '0' COMMENT '总订单数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_report`
--

LOCK TABLES `sales_report` WRITE;
/*!40000 ALTER TABLE `sales_report` DISABLE KEYS */;
INSERT INTO `sales_report` VALUES (1,'2025-11-24',15000.00,5,'2025-11-25 02:28:35'),(2,'2025-11-23',23000.00,8,'2025-11-25 02:28:35'),(3,'2025-11-22',12000.00,4,'2025-11-25 02:28:35'),(4,'2025-11-21',35000.00,12,'2025-11-25 02:28:35'),(5,'2025-11-20',18000.00,6,'2025-11-25 02:28:35'),(6,'2025-12-06',15000.00,5,'2025-12-07 18:31:05'),(7,'2025-12-05',23000.00,8,'2025-12-07 18:31:05'),(8,'2025-12-04',12000.00,4,'2025-12-07 18:31:05'),(9,'2025-12-03',35000.00,12,'2025-12-07 18:31:05'),(10,'2025-12-02',18000.00,6,'2025-12-07 18:31:05');
/*!40000 ALTER TABLE `sales_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2003009175596048387 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test_user','e10adc3949ba59abbe56e057f20f883e','test@example.com','13800138000','北京市朝阳区','2025-12-07 18:34:05','2025-12-07 18:34:05',0),(2,'vip_user','e10adc3949ba59abbe56e057f20f883e','vip@example.com','13900139000','上海市浦东新区','2025-12-07 18:34:05','2025-12-07 18:34:05',0),(4,'jellyfaith','$2a$10$LvhOeH9VmbpQjwpreAEdket.YVXcAk7DjmXRsz/3LmEdUBY0Y2Wem','554899470@qq.com','19200801080','广州市番禺区大学城外环东路382号华南理工大学大学城校区',NULL,'2025-12-09 03:27:26',0),(2003009175596048386,'zdp','$2a$10$whw07Lh/WidpTjPNZyCobuGybQg2XEdbZzyKcTsYMoilnIzLZga2u','zhangdapao264376@163.com','123','123','2025-12-22 15:46:16','2025-12-22 15:46:16',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-26 23:58:34

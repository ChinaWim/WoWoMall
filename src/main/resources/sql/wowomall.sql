-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: wowomall
-- ------------------------------------------------------
-- Server version	5.7.21-1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `wowomall_cart`
--

DROP TABLE IF EXISTS `wowomall_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户表id',
  `product_id` int(11) DEFAULT NULL COMMENT '商品id',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `checked` int(11) DEFAULT '1' COMMENT '是否选择，1=已勾选，0=未勾选',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_cart`
--

LOCK TABLES `wowomall_cart` WRITE;
/*!40000 ALTER TABLE `wowomall_cart` DISABLE KEYS */;
INSERT INTO `wowomall_cart` VALUES (12,1,1,1,0,'2018-09-08 14:46:32','2018-09-08 14:46:55');
/*!40000 ALTER TABLE `wowomall_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_category`
--

DROP TABLE IF EXISTS `wowomall_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父类别id当id=0时说明是根节点，一级类别',
  `name` varchar(50) NOT NULL COMMENT '类别名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '类别状态1-正常,2-已报废',
  `sort_order` int(4) DEFAULT NULL COMMENT '排序编号，同类展开顺序，数值相等则自然排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_category`
--

LOCK TABLES `wowomall_category` WRITE;
/*!40000 ALTER TABLE `wowomall_category` DISABLE KEYS */;
INSERT INTO `wowomall_category` VALUES (1,0,'修改名字',1,NULL,'2018-08-29 15:37:00','2018-08-30 14:53:23'),(2,1,'手机2',1,NULL,'2018-08-29 16:33:24','2018-08-30 22:42:27'),(3,2,'手机3',1,NULL,'2018-08-29 16:38:48','2018-08-30 22:42:27'),(4,3,'测试',1,NULL,'2018-08-29 19:30:23','2018-08-30 22:47:52'),(5,3,'手机',1,NULL,'2018-08-29 16:38:48','2018-08-30 22:48:02'),(6,1,'2测试',1,NULL,'2018-08-29 20:44:32','2018-08-30 22:48:06'),(7,0,'11111111',1,NULL,'2018-08-29 21:13:17','2018-08-30 22:48:13');
/*!40000 ALTER TABLE `wowomall_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_order`
--

DROP TABLE IF EXISTS `wowomall_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `shipping_id` int(11) DEFAULT NULL,
  `payment` decimal(20,2) DEFAULT NULL COMMENT '实际付款金额,单位是元,保留两位小数',
  `payment_type` int(4) DEFAULT NULL COMMENT '支付类型,1-在线支付',
  `postage` int(10) DEFAULT NULL COMMENT '运费,单位是元',
  `status` int(10) DEFAULT NULL COMMENT '订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `send_time` datetime DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_order`
--

LOCK TABLES `wowomall_order` WRITE;
/*!40000 ALTER TABLE `wowomall_order` DISABLE KEYS */;
INSERT INTO `wowomall_order` VALUES (9,1536388226514,1,2,1.50,1,0,60,NULL,NULL,NULL,NULL,'2018-09-08 14:30:26','2018-09-17 11:47:00'),(10,1536388655212,1,2,1.01,1,0,60,NULL,NULL,NULL,NULL,'2018-09-08 14:37:35','2018-09-17 11:47:00'),(11,1536389344798,1,2,0.11,1,0,60,NULL,'2018-09-08 14:55:20',NULL,NULL,'2018-09-08 14:49:05','2018-09-17 11:47:00');
/*!40000 ALTER TABLE `wowomall_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_order_item`
--

DROP TABLE IF EXISTS `wowomall_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单明细表id',
  `user_id` int(11) NOT NULL,
  `order_no` bigint(20) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `product_image` varchar(500) DEFAULT NULL COMMENT '商品图片地址',
  `current_unit_price` decimal(20,2) DEFAULT NULL COMMENT '生成订单时的商品单价，单位是元，保留两位小数',
  `quantity` int(10) DEFAULT NULL COMMENT '商品数量',
  `total_price` decimal(20,2) DEFAULT NULL COMMENT '商品总价，单位元，保留两位小数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_no_index` (`order_no`) USING BTREE,
  KEY `order_no_user_id_index` (`user_id`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_order_item`
--

LOCK TABLES `wowomall_order_item` WRITE;
/*!40000 ALTER TABLE `wowomall_order_item` DISABLE KEYS */;
INSERT INTO `wowomall_order_item` VALUES (15,1,1536388226514,1,'thinkpad','mainImage',0.50,3,1.50,'2018-09-08 14:30:26','2018-09-08 14:30:26'),(16,1,1536388655212,1,'thinkpad','mainImage',0.50,2,1.00,'2018-09-08 14:37:35','2018-09-08 14:37:35'),(17,1,1536388655212,3,'iphone7',NULL,0.01,1,0.01,'2018-09-08 14:37:35','2018-09-08 14:37:35'),(18,1,1536389344798,2,'iphone6',NULL,0.10,1,0.10,'2018-09-08 14:49:04','2018-09-08 14:49:04'),(19,1,1536389344798,3,'iphone7',NULL,0.01,1,0.01,'2018-09-08 14:49:04','2018-09-08 14:49:04');
/*!40000 ALTER TABLE `wowomall_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_pay_info`
--

DROP TABLE IF EXISTS `wowomall_pay_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_pay_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `order_no` bigint(20) NOT NULL COMMENT '订单号',
  `pay_platform` int(11) DEFAULT NULL COMMENT '支付平台：1-支付宝2-微信',
  `platform_number` varchar(200) DEFAULT NULL COMMENT '支付宝支付流水号',
  `platform_status` varchar(20) DEFAULT NULL COMMENT '支付宝支付状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_pay_info`
--

LOCK TABLES `wowomall_pay_info` WRITE;
/*!40000 ALTER TABLE `wowomall_pay_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `wowomall_pay_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_product`
--

DROP TABLE IF EXISTS `wowomall_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `subtitle` varchar(200) DEFAULT NULL COMMENT '商品副标题',
  `main_image` varchar(500) DEFAULT NULL COMMENT '产品主图，url相对地址',
  `sub_images` text COMMENT '图片地址， json格式，扩展用',
  `detail` text COMMENT '商品详情',
  `price` decimal(20,2) NOT NULL COMMENT '价格，单位(元)保留两位小数',
  `stock` int(11) NOT NULL COMMENT '库存数量',
  `status` int(6) DEFAULT '1' COMMENT ' 商品的状态 1在售。2-下架，3-删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_product`
--

LOCK TABLES `wowomall_product` WRITE;
/*!40000 ALTER TABLE `wowomall_product` DISABLE KEYS */;
INSERT INTO `wowomall_product` VALUES (1,1,'thinkpad','subtitle','mainImage','subImages','detail',0.50,5,1,'2018-08-29 22:15:16','2018-09-17 11:47:00'),(2,2,'iphone6',NULL,NULL,NULL,NULL,0.10,1,1,'2018-08-29 22:15:37','2018-09-17 11:47:00'),(3,2,'iphone7',NULL,NULL,NULL,NULL,0.01,2,1,'2018-08-30 10:00:07','2018-09-17 11:47:00'),(4,3,'iphone8',NULL,NULL,NULL,NULL,22.00,0,1,'2018-08-30 22:54:27','2018-09-17 11:23:18'),(5,4,'iphone9',NULL,NULL,NULL,NULL,1.00,0,1,'2018-08-30 22:54:45','2018-09-17 11:23:18');
/*!40000 ALTER TABLE `wowomall_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_shipping`
--

DROP TABLE IF EXISTS `wowomall_shipping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_shipping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `receiver_name` varchar(20) DEFAULT NULL COMMENT '收货姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货固定电话',
  `receiver_mobile` varchar(20) DEFAULT NULL COMMENT '收货移动电话',
  `receiver_province` varchar(20) DEFAULT NULL COMMENT '省份',
  `receiver_city` varchar(20) DEFAULT NULL COMMENT '城市',
  `receiver_district` varchar(20) DEFAULT NULL COMMENT '区/县',
  `receiver_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `receiver_zip` varchar(6) DEFAULT NULL COMMENT '邮编',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_shipping`
--

LOCK TABLES `wowomall_shipping` WRITE;
/*!40000 ALTER TABLE `wowomall_shipping` DISABLE KEYS */;
INSERT INTO `wowomall_shipping` VALUES (2,1,'Ming','010','18688888888','北京','北京市','海淀区','中关村','100000','2018-09-01 12:20:33','2018-09-07 10:09:38'),(3,1,'geely','010','18688888888','北京','北京市',NULL,'中关村','100000','2018-09-01 15:05:52','2018-09-01 15:05:52');
/*!40000 ALTER TABLE `wowomall_shipping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wowomall_user`
--

DROP TABLE IF EXISTS `wowomall_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wowomall_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户表id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '用户密码 md5加密',
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `question` varchar(100) DEFAULT NULL COMMENT '找回密码问题',
  `answer` varchar(100) DEFAULT NULL COMMENT '找回密码答案',
  `role` int(4) NOT NULL DEFAULT '1' COMMENT '角色  0-管理员，1-普通用户,待扩展',
  `status` tinyint(1) DEFAULT '1' COMMENT '0-未激活 1-已激活',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wowomall_user`
--

LOCK TABLES `wowomall_user` WRITE;
/*!40000 ALTER TABLE `wowomall_user` DISABLE KEYS */;
INSERT INTO `wowomall_user` VALUES (1,'ming','A99EE914E87124FA9F34267FF6CDD778','123','123','123','321',1,1,'2018-05-27 14:04:41','2018-09-17 11:06:12'),(2,'ming2','A99EE914E87124FA9F34267FF6CDD778','111111','123','123','321',0,1,'2018-08-29 13:08:06','2018-09-17 11:06:12');
/*!40000 ALTER TABLE `wowomall_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-18 19:37:15

-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE  IF NOT EXISTS flashsale ;
-- 使用数据库
use flashsale;
CREATE TABLE commodity_info(
    `commodity_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT 'sale commodity ID,商品库存ID',
    `name` VARCHAR(120) NOT NULL COMMENT 'commodity name,商品名称',
  `amount` int NOT NULL COMMENT 'stock quantity,库存数量',
  `start_time` TIMESTAMP  NOT NULL COMMENT 'when the flash sale will start,秒杀开始时间',
  `end_time`   TIMESTAMP   NOT NULL COMMENT 'when the flash sale will end,秒杀结束时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (commodity_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='commodity information in the flash sale,秒杀库存表';

-- 初始化数据
INSERT into commodity_info(name,amount,start_time,end_time)
VALUES
  ('iphone6 with 50% discount ',100,'2022-09-04 00:00:00','2022-09-06 00:00:00'),
  ('ipad4 with 25% discount',200,'2022-09-04 00:00:00','2022-09-06 00:00:00'),
  ('mac book pro with 15% discount',300,'2022-09-04 00:00:00','2022-09-06 00:00:00'),
  ('iMac3 with 20% discount',400,'2022-09-04 00:00:00','2022-09-06 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE sale_order(
  `commodity_id` BIGINT NOT NULL COMMENT 'sale commodity ID,秒杀商品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT 'order state:-1:invalid,无效 0:success, 成功 1:paid,已付款 2:in deliver,已发货',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(commodity_id,user_phone),
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='orders successfully made by users,用户成功下单的信息';


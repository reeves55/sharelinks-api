DROP TABLE IF EXISTS t_like;
CREATE TABLE IF NOT EXISTS t_like(
  fid INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  fuser VARCHAR(255) NOT NULL DEFAULT '' COMMENT '用户名',
  fpost_id INT(11) NOT NULL DEFAULT 0 COMMENT '链接ID',
  fadded_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  fupdate_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  fcolortag TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏颜色标记：0->白色（未标记），1->红色，2->橙色，3->黄色，4->绿色，5->蓝色，6->紫色，7->灰色',
  ftag VARCHAR(100) NOT NULL DEFAULT '默认' COMMENT '标签',
  fenable VARCHAR(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY(fid)
)ENGINE=INNODB, CHARSET=utf8mb4;
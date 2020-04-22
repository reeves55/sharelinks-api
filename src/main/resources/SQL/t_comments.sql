DROP TABLE IF EXISTS t_comments;
CREATE TABLE IF NOT EXISTS t_comments(
  fid INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  fuser_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户名',
  fpost_id INT(11) NOT NULL DEFAULT 0 COMMENT '链接ID',
  fcontent VARCHAR(500) NOT NULL DEFAULT '' COMMENT '评论内容',
  fadded_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  fupdate_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  fenable VARCHAR(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY(fid)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
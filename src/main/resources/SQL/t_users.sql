DROP TABLE IF EXISTS t_users;
CREATE TABLE IF NOT EXISTS t_users(
  fid INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	fname VARCHAR(50) NOT NULL COMMENT '用户名，唯一',
	ftype TINYINT NOT NULL DEFAULT 0 COMMENT '用户角色，0=>普通用户,1=>管理员',
	fpassword VARCHAR(500) NOT NULL DEFAULT '' COMMENT '密码',
	fadded_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	fupdate_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	fmail VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱',
	fenable VARCHAR(1) NOT NULL DEFAULT 'Y' COMMENT '是否有效',
	PRIMARY KEY(fid)
)ENGINE=INNODB,CHARSET=utf8mb4;
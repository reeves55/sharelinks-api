CREATE TABLE IF NOT EXISTS t_posts (
  fid INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	ftitle VARCHAR(255) NOT NULL DEFAULT '' COMMENT '链接标题',
	furl VARCHAR(500) NOT NULL DEFAULT '' COMMENT '链接URL',
	fadded_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
	fupdate_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	fauthor VARCHAR(50) NOT NULL DEFAULT '' COMMENT '添加人',
	fcategory VARCHAR(20) NOT NULL DEFAULT '' COMMENT '链接分类',
	flike SMALLINT NOT NULL DEFAULT 0 COMMENT '收藏数',
	freported SMALLINT NOT NULL DEFAULT 0 COMMENT '举报数',
	fshare VARCHAR(1) NOT NULL DEFAULT 'N',
	ftag VARCHAR(100) NOT NULL DEFAULT '',
	fenable VARCHAR(1) NOT NULL DEFAULT 'Y'
	PRIMARY KEY(`fid`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- PDF转换临时表
CREATE TABLE `pdf_conversion` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `attachment_id` bigint unsigned NOT NULL COMMENT '源附件ID',
  `original_filename` varchar(255) NOT NULL COMMENT '源文件名',
  `pdf_filename` varchar(255) NOT NULL COMMENT 'PDF文件名',
  `pdf_store_path` varchar(500) NOT NULL COMMENT 'PDF存储路径',
  `pdf_file_size` bigint DEFAULT NULL COMMENT 'PDF文件大小（字节）',
  `last_access_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_attachment_id` (`attachment_id`),
  KEY `idx_last_access_time` (`last_access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PDF转换临时表';

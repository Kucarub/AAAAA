DROP TABLE IF EXISTS p_document_version;
DROP TABLE IF EXISTS p_document;
DROP TABLE IF EXISTS p_project;
DROP TABLE IF EXISTS attachment;

-- 项目表
CREATE TABLE `p_project`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(255) NOT NULL COMMENT '项目名称',
    `code`         varchar(100) DEFAULT NULL COMMENT '项目编码',
    `description`  text COMMENT '项目描述',
    `status`       tinyint      DEFAULT 1 COMMENT '状态：1启用，0禁用',
    `created_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY            `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 资料表（每个项目下的资料实体）
CREATE TABLE `p_document`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `project_id`   bigint unsigned NOT NULL COMMENT '所属项目ID',
    `name`         varchar(255) NOT NULL COMMENT '资料名称',
    `description`  text COMMENT '资料描述',
    `remark`       text COMMENT '资料备注（整体备注）',
    `created_by`   bigint   DEFAULT NULL COMMENT '创建人ID（关联用户表）',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY            `idx_project_id` (`project_id`),
    CONSTRAINT `fk_document_project` FOREIGN KEY (`project_id`) REFERENCES `p_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料表';

-- 资料版本表（每个版本对应一个附件）
CREATE TABLE `p_document_version`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '版本ID',
    `document_id`    bigint unsigned NOT NULL COMMENT '所属资料ID',
    `version_number` varchar(50)  NOT NULL COMMENT '版本号（如v1.0, 2）',
    `file_name`      varchar(255) NOT NULL COMMENT '附件原始文件名',
    `file_path`      varchar(500) NOT NULL COMMENT '附件存储路径（用于预览和下载）',
    `file_size`      bigint       DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`      varchar(100) DEFAULT NULL COMMENT '文件MIME类型（如application/pdf）',
    `remark`         text COMMENT '版本备注（本版本的变更说明等）',
    `is_current`     tinyint(1) DEFAULT 0 COMMENT '是否为当前版本：1是，0否',
    `upload_by`      bigint       DEFAULT NULL COMMENT '上传人ID',
    `upload_time`    datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `created_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`   datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_document_version` (`document_id`, `version_number`),
    KEY              `idx_document_id` (`document_id`),
    KEY              `idx_is_current` (`document_id`, `is_current`),
    CONSTRAINT `fk_version_document` FOREIGN KEY (`document_id`) REFERENCES `p_document` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料版本表';-- 项目表
CREATE TABLE `p_project`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(255) NOT NULL COMMENT '项目名称',
    `code`         varchar(100) DEFAULT NULL COMMENT '项目编码',
    `description`  text COMMENT '项目描述',
    `status`       tinyint      DEFAULT 1 COMMENT '状态：1启用，0禁用',
    `created_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY            `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- 资料表（每个项目下的资料实体）
CREATE TABLE `p_document`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `project_id`   bigint unsigned NOT NULL COMMENT '所属项目ID',
    `name`         varchar(255) NOT NULL COMMENT '资料名称',
    `description`  text COMMENT '资料描述',
    `remark`       text COMMENT '资料备注（整体备注）',
    `created_by`   bigint   DEFAULT NULL COMMENT '创建人ID（关联用户表）',
    `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY            `idx_project_id` (`project_id`),
    CONSTRAINT `fk_document_project` FOREIGN KEY (`project_id`) REFERENCES `p_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料表';

-- 资料版本表（每个版本对应一个附件）
CREATE TABLE `p_document_version`
(
    `id`             bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '版本ID',
    `document_id`    bigint unsigned NOT NULL COMMENT '所属资料ID',
    `version_number` varchar(50)  NOT NULL COMMENT '版本号（如v1.0, 2）',
    `file_name`      varchar(255) NOT NULL COMMENT '附件原始文件名',
    `file_path`      varchar(500) NOT NULL COMMENT '附件存储路径（用于预览和下载）',
    `file_size`      bigint       DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`      varchar(100) DEFAULT NULL COMMENT '文件MIME类型（如application/pdf）',
    `remark`         text COMMENT '版本备注（本版本的变更说明等）',
    `is_current`     tinyint(1) DEFAULT 0 COMMENT '是否为当前版本：1是，0否',
    `upload_by`      bigint       DEFAULT NULL COMMENT '上传人ID',
    `upload_time`    datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `created_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`   datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_document_version` (`document_id`, `version_number`),
    KEY              `idx_document_id` (`document_id`),
    KEY              `idx_is_current` (`document_id`, `is_current`),
    CONSTRAINT `fk_version_document` FOREIGN KEY (`document_id`) REFERENCES `p_document` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料版本表';

CREATE TABLE `attachment`
(
    `id`            bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '附件ID',
    `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
    `store_name`    varchar(255) NOT NULL COMMENT '存储文件名（通常为UUID或时间戳命名）',
    `store_path`    varchar(500) NOT NULL COMMENT '存储路径（相对路径或绝对路径）',
    `full_url`      varchar(500) DEFAULT NULL COMMENT '完整访问URL',
    `file_size`     bigint       DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`     varchar(100) DEFAULT NULL COMMENT '文件MIME类型',
    `suffix`        varchar(20)  DEFAULT NULL COMMENT '文件后缀',
    `upload_by`     bigint       DEFAULT NULL COMMENT '上传人ID（关联用户表）',
    `upload_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `remark`        varchar(500) DEFAULT NULL COMMENT '备注',
    `created_time`  datetime     DEFAULT CURRENT_TIMESTAMP,
    `updated_time`  datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY             `idx_upload_by` (`upload_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

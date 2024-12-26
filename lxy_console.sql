show databases;
use mp;

-- 导出  表 mp.address 结构
CREATE TABLE IF NOT EXISTS `address` (
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `user_id` bigint DEFAULT NULL COMMENT '用户ID',
                                         `province` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '省',
                                         `city` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市',
                                         `town` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '县/区',
                                         `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
                                         `street` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '详细地址',
                                         `contact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人',
                                         `is_default` bit(1) DEFAULT b'0' COMMENT '是否是默认 1默认 0否',
                                         `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                                         `deleted` bit(1) DEFAULT b'0' COMMENT '逻辑删除',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT;

-- 正在导出表  mp.address 的数据：~11 rows (大约)

INSERT INTO `address` (`id`, `user_id`, `province`, `city`, `town`, `mobile`, `street`, `contact`, `is_default`, `notes`, `deleted`) VALUES
                                                                                                                                         (59, 2, '北京', '北京', '朝阳区', '13900112222', '金燕龙办公楼', 'Rose', b'1', NULL, b'0'),
                                                                                                                                         (60, 1, '北京', '北京', '朝阳区', '13700221122', '修正大厦', 'Jack', b'0', NULL, b'0'),
                                                                                                                                         (61, 1, '上海', '上海', '浦东新区', '13301212233', '航头镇航头路', 'Jack', b'1', NULL, b'0'),
                                                                                                                                         (63, 2, '广东', '佛山', '永春', '13301212233', '永春武馆', 'Rose', b'0', NULL, b'0'),
                                                                                                                                         (64, 3, '浙江', '杭州', '拱墅区', '13567809102', '浙江大学', 'Hope', b'1', NULL, b'0'),
                                                                                                                                         (65, 3, '浙江', '杭州', '拱墅区', '13967589201', '左岸花园', 'Hope', b'0', NULL, b'0'),
                                                                                                                                         (66, 4, '湖北', '武汉', '汉口', '13967519202', '天天花园', 'Thomas', b'1', NULL, b'0'),
                                                                                                                                         (67, 3, '浙江', '杭州', '拱墅区', '13967589201', '左岸花园', 'Hopey', b'0', NULL, b'0'),
                                                                                                                                         (68, 4, '湖北', '武汉', '汉口', '13967519202', '天天花园', 'Thomas', b'1', NULL, b'0'),
                                                                                                                                         (69, 3, '浙江', '杭州', '拱墅区', '13967589201', '左岸花园', 'Hopey', b'0', NULL, b'0'),
                                                                                                                                         (70, 4, '湖北', '武汉', '汉口', '13967519202', '天天花园', 'Thomas', b'1', NULL, b'0');

-- 导出  表 mp.user 结构
CREATE TABLE `user` (
                        `id` BIGINT(19) NOT NULL AUTO_INCREMENT COMMENT '用户id',
                        `username` VARCHAR(50) NOT NULL COMMENT '用户名' COLLATE 'utf8_general_ci',
                        `password` VARCHAR(128) NOT NULL COMMENT '密码' COLLATE 'utf8_general_ci',
                        `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '注册手机号' COLLATE 'utf8_general_ci',
                        `info` JSON NOT NULL COMMENT '详细信息',
                        `status` INT(10) NULL DEFAULT '1' COMMENT '使用状态（1正常 2冻结）',
                        `balance` INT(10) NULL DEFAULT NULL COMMENT '账户余额',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE INDEX `username` (`username`) USING BTREE
)
    COMMENT='用户表'
    COLLATE='utf8_general_ci'
    ENGINE=InnoDB
    ROW_FORMAT=COMPACT
    AUTO_INCREMENT=5
;

-- 正在导出表  mp.user 的数据：~4 rows (大约)

INSERT INTO `user` (`id`, `username`, `password`, `phone`, `info`, `status`, `balance`, `create_time`, `update_time`) VALUES
                                                                                                                          (1, 'Jack', '123', '13900112224', '{"age": 20, "intro": "佛系青年", "gender": "male"}', 1, 1600, '2023-05-19 20:50:21', '2023-06-19 20:50:21'),
                                                                                                                          (2, 'Rose', '123', '13900112223', '{"age": 19, "intro": "青涩少女", "gender": "female"}', 1, 600, '2023-05-19 21:00:23', '2023-06-19 21:00:23'),
                                                                                                                          (3, 'Hope', '123', '13900112222', '{"age": 25, "intro": "上进青年", "gender": "male"}', 1, 100000, '2023-06-19 22:37:44', '2023-06-19 22:37:44'),
                                                                                                                          (4, 'Thomas', '123', '17701265258', '{"age": 29, "intro": "伏地魔", "gender": "male"}', 1, 800, '2023-06-19 23:44:45', '2023-06-19 23:44:45');

delete from tb_user where id>5;

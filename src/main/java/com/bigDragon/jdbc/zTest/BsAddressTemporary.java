package com.bigDragon.jdbc.zTest;

import lombok.Data;

//CREATE TABLE `bs_address_temporary` (
//        `id` varchar(36) NOT NULL,
//        `area_id` varchar(128),
//        `parent_id` varchar(128),
//        `addr_name` varchar(128),
//        `area_name` varchar(128),
//        PRIMARY KEY (`id`) USING BTREE
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='bs同步地址表';
@Data
public class BsAddressTemporary {
    private String id;
    private String AreaId;
    private String ParentId;
    private String AreaName;
}

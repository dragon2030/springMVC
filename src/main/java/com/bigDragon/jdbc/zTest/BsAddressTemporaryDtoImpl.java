package com.bigDragon.jdbc.zTest;

import com.bigDragon.jdbc.dao.BaseDAO2;

import java.sql.Connection;

public class BsAddressTemporaryDtoImpl extends BaseDAO2<BsAddressTemporary> {
    public void insert(Connection conn, BsAddressTemporary addr) {
        String sql = "insert into bs_address_temporary(id,area_id,parent_id,addr_name)values(?,?,?,?)";
        update(conn, sql,"1",addr.getAreaId(),addr.getParentId(),addr.getAreaName());
    }

}

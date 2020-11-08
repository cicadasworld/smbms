package com.jin.dao.role;

import com.jin.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {

    // 通过条件查询用户角色列表
    List<Role> getRoleList(Connection connection) throws SQLException;
}

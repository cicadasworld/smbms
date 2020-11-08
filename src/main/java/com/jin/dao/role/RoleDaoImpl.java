package com.jin.dao.role;

import com.jin.dao.BaseDao;
import com.jin.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleDaoImpl implements RoleDao {

    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> roleList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (connection != null) {
            String sql = "select r.* from smbms_role r";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {};
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.closeResource(null, pstmt, rs);
        }
        return roleList;
    }
}

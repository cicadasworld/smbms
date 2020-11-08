package com.jin.dao.user;

import com.jin.dao.BaseDao;
import com.jin.pojo.User;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, pstmt, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement pstmt = null;
        int execute = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {password, id};
            execute = BaseDao.execute(connection, pstmt, sql, params);
            BaseDao.closeResource(null, pstmt, null);
        }
        return execute;
    }

    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        if (connection != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("select count(1) as count from smbms_user u, smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sb.append("and u.userName like ?");
                list.add("%" + userName + "%");
            }

            if (userRole > 0) {
                sb.append(" and u.userRole = ?");
                list.add(userRole);
            }
            String sql = sb.toString();
            if (log.isDebugEnabled()) {
                log.info("sql statement: {}", sql);
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            if (rs.next()) {
                count = rs.getInt("count");
            }
            BaseDao.closeResource(null, pstmt, rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        List<User> userList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (connection != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("select u.*, r.roleName from smbms_user u, smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sb.append("and u.userName like ?");
                list.add("%" + userName + "%");
            }

            if (userRole > 0) {
                sb.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sb.append(" order by creationDate desc limit ?, ?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            String sql = sb.toString();
            if (log.isDebugEnabled()) {
                log.info("sql statement: {}", sql);
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("roleName"));
                userList.add(user);
            }
            BaseDao.closeResource(null, pstmt, rs);
        }
        return userList;
    }

    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "insert into smbms_user (userCode, userName, userPassword, " +
                    "userRole, gender, birthday, phone, address, creationDate, createdBy) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                        user.getUserRole(), user.getGender(), user.getBirthday(),
                        user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
            updateRows = BaseDao.execute(connection, pstmt, sql, params);
            BaseDao.closeResource(null, pstmt, null);
        }
        return updateRows;
    }

    @Override
    public int findByUserCodeCount(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        if (connection != null) {
            String sql = "select count(1) as count from smbms_user where userCode = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            while (rs.next()) {
                count = rs.getInt("count");
            }
        }
        return count;
    }

    @Override
    public int deleteUserById(Connection connection, int id) throws SQLException {
        PreparedStatement pstmt = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "delete from smbms_user where id = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {id};
            updateRows = BaseDao.execute(connection, pstmt, sql, params);
            BaseDao.closeResource(null, pstmt, null);
        }
        return updateRows;
    }

    @Override
    public User getUserById(Connection connection, int id) throws SQLException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (connection != null) {
            String sql = "select u.*, r.roleName as userRoleName from smbms_user u, smbms_role r " +
                    "where u.id = ? and u.userRole = r.id";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {id};
            rs = BaseDao.execute(connection, pstmt, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.closeResource(null, pstmt, rs);
        }
        return user;
    }

    @Override
    public int modify(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "update smbms_user set userName = ?, gender = ?, birthday = ?, phone = ?, " +
                    "address = ?, userRole = ?, modifyBy = ?, modifyDate = ? where id = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql statement: {}", sql);
            }
            Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                    user.getModifyDate(), user.getId()};
            updateRows = BaseDao.execute(connection, pstmt, sql, params);
            BaseDao.closeResource(null, pstmt, null);
        }
        return updateRows;
    }
}

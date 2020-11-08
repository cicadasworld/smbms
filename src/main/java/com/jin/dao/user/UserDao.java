package com.jin.dao.user;

import com.jin.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    // 根据用户编码获取登录用户信息
    User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改当前用户密码
    int updatePwd(Connection connection, int id, String password) throws SQLException;

    // 根据用户名和用户角色查询用户总数
    int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    // 通过条件查询用户列表
    List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    int add(Connection connection, User user) throws SQLException;

    int findByUserCodeCount(Connection connection, String userCode) throws SQLException;

    int deleteUserById(Connection connection, int id) throws SQLException;

    User getUserById(Connection connection, int id) throws SQLException;

    int modify(Connection connection, User user) throws SQLException;
}

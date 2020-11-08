package com.jin.service.user;

import com.jin.pojo.User;

import java.util.List;

public interface UserService {

    User login(String userCode, String password);

    boolean updatePwd(int id, String password);

    int getUserCount(String username, int userRole);

    List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    boolean add(User user);

    boolean selectUserCodeExist(String userCode);

    boolean deleteUserById(int id);

    User getUserById(int id);

    boolean modify(User user);
}

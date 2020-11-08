package com.jin;

import com.jin.pojo.User;
import com.jin.service.user.UserService;
import com.jin.service.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserServiceTest {

    @Test
    void testLogin() {
        UserService userService = new UserServiceImpl();
        User admin = userService.login("admin", "1234567");
        assertEquals("1234567", admin.getUserPassword(), "admin password is correct");
    }

    @Test
    void testGetUserCount() {
        UserService userService = new UserServiceImpl();
        int count = userService.getUserCount(null, 1);
        assertEquals(1, count, "total count of admin account user");
    }

    @Test
    void testGetUserList() {
        UserService userService = new UserServiceImpl();
        List<User> userList = userService.getUserList(null, 1, 1, 5);
        assertEquals(1, userList.size(), "count of admin account user is 1");
    }

}

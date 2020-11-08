package com.jin;

import com.jin.pojo.Role;
import com.jin.service.role.RoleService;
import com.jin.service.role.RoleServiceImpl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleServiceTest {

    @Test
    void testGetRoleList() {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        assertEquals(3, roleList.size(), "3 kind of role");
    }
}

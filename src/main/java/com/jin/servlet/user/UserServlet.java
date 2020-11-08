package com.jin.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jin.pojo.Role;
import com.jin.pojo.User;
import com.jin.service.role.RoleService;
import com.jin.service.role.RoleServiceImpl;
import com.jin.service.user.UserService;
import com.jin.service.user.UserServiceImpl;
import com.jin.util.Constants;
import com.jin.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("savepwd")) {
            updatePwd(req, resp);
        } else if (method != null && method.equals("pwdmodify")) {
            pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            query(req, resp);
        } else if (method != null && method.equals("add")) {
            addUser(req, resp);
        } else if (method != null && method.equals("deluser")) {
            delUser(req, resp);
        } else if (method != null && method.equals("ucexist")) {
            userCodeExist(req, resp);
        } else if (method != null && method.equals("modify")) {
            getUserById(req, resp, "usermodify.jsp");
        } else if (method != null && method.equals("modifyexe")) {
            modifyUser(req, resp);
        } else if (method != null && method.equals("view")) {
            getUserById(req, resp, "userview.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if (o != null && newpassword != null) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag) {
                req.setAttribute("message", "修改密码成功，请退出，使用新密码登录。");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败。");
            }
        } else {
            req.setAttribute("message", "新密码设置不正确。");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if (o == null) { // session timeout
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) { //old password is empty
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) o).getUserPassword();
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        writer.write(mapper.writeValueAsString(resultMap));
        writer.flush();
        writer.close();
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 从前端获取请求参数
        String queryUserName = req.getParameter("queryname");
        String queryUserRoleReq = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        // 获取用户列表
        UserService userService = new UserServiceImpl();
        List<User> userList = null;

        int pageSize = 5; // 可通过配置文件配置
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }

        if (queryUserRoleReq != null && queryUserRoleReq.length() > 0) {
            try {
                queryUserRole = Integer.parseInt(queryUserRoleReq);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                resp.sendRedirect("error.jsp");
            }
        }

        if (pageIndex != null) {
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                resp.sendRedirect("error.jsp");
            }
        }

        // 获取用户总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        // 控制首页和尾页
        int totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        // 获取用于前端展示的用户列表
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);

        // 获取用于前端展示的角色列表
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);

        // 返回给前端
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<>();
        if (userCode == null || userCode.length() == 0) {
            resultMap.put("result", "error");
        } else {
            UserService userService = new UserServiceImpl();
            boolean exist = userService.selectUserCodeExist(userCode);
            if (exist) {
                resultMap.put("result", "exist");
            } else {
                resultMap.put("result", "notexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonMapper mapper = new JsonMapper();
        writer.write(mapper.writeValueAsString(resultMap));
        writer.flush();
        writer.close();
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);

        try {
            user.setGender(Integer.valueOf(gender));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }

        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }

        user.setPhone(phone);
        user.setAddress(address);
        try {
            user.setUserRole(Integer.valueOf(userRole));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        user.setCreationDate(new Date());

        User currentUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(currentUser.getId());

        UserService userService = new UserServiceImpl();
        if (userService.add(user)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }

    }

    private void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        int id = 0;
        try {
            id = Integer.parseInt(uid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        Map<String, String> resultMap = new HashMap<>();
        if (id <= 0) {
            resultMap.put("deleteResult", "notexist");
        } else {
            UserService userService = new UserServiceImpl();
            boolean deleted = userService.deleteUserById(id);
            if (deleted) {
                resultMap.put("deleteResult", "true");
            } else {
                resultMap.put("deleteResult", "false");
            }
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        JsonMapper mapper = new JsonMapper();
        writer.write(mapper.writeValueAsString(resultMap));
        writer.flush();
        writer.close();
    }


    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        if (uid != null && uid.length() > 0) {
            int id = Integer.parseInt(uid);
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void modifyUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        try {
            user.setId(Integer.parseInt(uid));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        user.setUserName(userName);
        try {
            user.setGender(Integer.parseInt(gender));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        user.setPhone(phone);
        user.setAddress(address);
        try {
            user.setUserRole(Integer.parseInt(userRole));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
        User currentUser = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        user.setModifyBy(currentUser.getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if (userService.modify(user)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }

}

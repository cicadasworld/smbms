package com.jin.pojo;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;

@Data
public class User {
    private int id;
    private String userCode;
    private String userName;
    private String userPassword;
    private Integer gender;
    private Date birthday;
    private String phone;
    private String address;
    private Integer userRole;
    private Integer createdBy;
    private Date creationDate;
    private Integer modifyBy;
    private Date modifyDate;

    private Integer age;
    private String userRoleName;

    public Integer getAge() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int now = calendar.get(Calendar.YEAR);
        calendar.setTime(birthday);
        int dob = calendar.get(Calendar.YEAR);
        Integer age = now - dob;
        return age;
    }
}

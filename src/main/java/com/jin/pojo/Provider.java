package com.jin.pojo;

import java.util.Date;

import lombok.Data;

@Data
public class Provider {
    private Integer id;
    private String proCode;
    private String proName;
    private String proDesc;
    private String proContact;
    private String proPhone;
    private String proAddress;
    private String proFax;
    private Integer createBy;
    private Date creationDate;
    private Integer modifyBy;
    private Date modifyDate;
}

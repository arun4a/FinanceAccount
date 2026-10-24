package com.laxmifinance.model;

import lombok.Data;

@Data
public class AppUser {
    private Integer userId;
    private String userName;
    private String phoneNumber;
    private String address;
    private String fatherName;
    private String surName;
    private String firstName;
    private String loginDate;
    private String loginTime;
}

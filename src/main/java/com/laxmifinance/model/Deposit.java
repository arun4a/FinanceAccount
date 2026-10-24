package com.laxmifinance.model;

import lombok.Data;

@Data
public class Deposit {
    private String depositId;
    private String depositName;
    private String amount;
    private String accountNumber;
    private String mobileNo;
    private String openDate;
    private String closeDate;
    private String actualOpenDate;
    private String actualCloseDate;
    private String depositType;
    private String time;
    private String closingTime;
    private String interest;
    private String paidAmount;
    private String paidAmountFromActual;
    private String dueAmount;
}

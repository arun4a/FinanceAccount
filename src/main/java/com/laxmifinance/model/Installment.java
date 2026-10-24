package com.laxmifinance.model;

import lombok.Data;

@Data
public class Installment {
    private String dueAmount;
    private String actualAmount;
    private String status;
    private String paidAmount;
    private String accountId;
    private String openingBalance;
    private String depositAmount;
    private String depositByPartner;
    private String customerId;
    private String customerName;
    private String installment;
    private String accountNumber;
    private String depositName;
    private String createdBy;
    private String date;
    private String time;
}

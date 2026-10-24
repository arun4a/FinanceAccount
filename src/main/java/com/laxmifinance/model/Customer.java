package com.laxmifinance.model;

import lombok.Data;

@Data
public class Customer {
    private Integer customerId;
    private String customerName;
    private String customerFatherName;
    private String customerMobileNo;
    private String customerAddress;
    private String customerOccupation;
    private String suretyName;
    private String suretyAddress;
    private String suretyMobileNo;
    private String suretyOccupation;
    private String suretyFname;
    private String cheque;
    private String passbook;
    private String atm;
    private String amount;
    private String dateOfIssue;
    private String dateOfIssueRaw;
    private String issuedBy;
    private String installments;
    private String originalAmount;
    private String accountNumber;
    private String accountNo;
    private String surRel;
    private String custRel;
    private String customerSecMobileNo;
    private String suretySecMobileNo;
    private String status;
    private String closingDate;
    private String closingDateRaw;
    private String monthName;
    private String closingMonthName;
    private String groupMonth;
    private boolean showMonthHeader;
    private double paidAmount;
    private int completedInstallments;
    private double dueAmount;
}

package com.laxmifinance.form;

import lombok.Data;

@Data
public class InstallmentForm {
    private String accountId;
    private String accountNumber;
    private String accountNumberNew;
    private String openingBalance;
    private String depositAmount;
    private String depositByPartner;
    private String customerName;
    private String date;
    private String actualAmount;
    private String paidAmount;
    private String installment;
    private String depositName;
    private String close;
    private String startDate;
    private String endDate;
}

package com.laxmifinance.model;

import lombok.Data;

@Data
public class MonthlyPayment {
    private String depositsId;
    private String depositAccountNo;
    private String paidAmount;
    private String paidAmountFromActual;
    private String crtDate;
    private String actualCrtDate;
    private String crtTime;
    private String amount;
    private String updateDate;
    private String depositName;
}

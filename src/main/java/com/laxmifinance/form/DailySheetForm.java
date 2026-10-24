package com.laxmifinance.form;

import lombok.Data;

@Data
public class DailySheetForm {
    private String date;
    private String openingBalance;
    private String depositAmount;
    private String depositName;
    private String depositBy;
    private String accountNumber;
    private String customerName;
    private String actaulAmount;
    private String paidAmount;
    private String installment;
    private String close;
}

package com.laxmifinance.form;

import lombok.Data;

@Data
public class MonthlyPaymentForm {
    private String depositsIdUpdate;
    private String date;
    private String depositAccountNumber;
    private String depositAccountNoUpdate;
    private String paidAmountFromActualUpdate;
    private String paidAmountUpdate;
}

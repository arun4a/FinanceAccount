package com.laxmifinance.form;

import lombok.Data;

/** Bond issue / update form (fields match the legacy dashboard and update screens). */
@Data
public class BondForm {
    private String getId;
    private String customerId;
    private String accountNumberNew;
    private String accountNo;
    private String customerNameN;
    private String custRel;
    private String fatherName;
    private String occupation;
    private String address;
    private String mobileNumber;
    private String suretyName;
    private String sureRel;
    private String suretyFatherName;
    private String suretyOccupation;
    private String suretyAddress;
    private String suretyMobileNumber;
    private String customerSecMobile;
    private String suretySecMobile;
    private String amountNew;
    private String originalAmount;
    private String installmentsN;
    private String atm;
    private String cheque;
    private String passBook;
    private String issuedDate;
}

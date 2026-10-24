/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.deposit;

/**
 *
 * @author sys1089
 */
public class MonthlyMaster {
   
    
    public String paidAmountFromActual,depositsPaidId,  depositName,actualCrtDate,  crtDate,    crtTime,    depositAccountNo,    paidAmount,    updateDate;

    public String getPaidAmountFromActual() {
        return paidAmountFromActual;
    }

    public void setPaidAmountFromActual(String paidAmountFromActual) {
        this.paidAmountFromActual = paidAmountFromActual;
    }

    public String getActualCrtDate() {
        return actualCrtDate;
    }

    public void setActualCrtDate(String actualCrtDate) {
        this.actualCrtDate = actualCrtDate;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getDepositsPaidId() {
        return depositsPaidId;
    }

    public void setDepositsPaidId(String depositsPaidId) {
        this.depositsPaidId = depositsPaidId;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }

    public String getDepositAccountNo() {
        return depositAccountNo;
    }

    public void setDepositAccountNo(String depositAccountNo) {
        this.depositAccountNo = depositAccountNo;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
        
}

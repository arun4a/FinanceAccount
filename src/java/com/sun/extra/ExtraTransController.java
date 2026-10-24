/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.extra;

import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author Arun
 */
public class ExtraTransController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    HttpServletRequest request;
    HttpServletResponse response;
    private String extraIdSave, res_msg;
    private String extraAmountSave;
    private String extraPurposeSave;
    private String extraDateSave;
    private String extraSearchDate;

    public String getExtraSearchDate() {
        return extraSearchDate;
    }

    public void setExtraSearchDate(String extraSearchDate) {
        this.extraSearchDate = extraSearchDate;
    }

    List<ExtraMaster> displayExtraTransaction;

    public List<ExtraMaster> getDisplayExtraTransaction() {
        return displayExtraTransaction;
    }

    public void setDisplayExtraTransaction(List<ExtraMaster> displayExtraTransaction) {
        this.displayExtraTransaction = displayExtraTransaction;
    }

    public String getExtraIdSave() {
        return extraIdSave;
    }

    public void setExtraIdSave(String extraIdSave) {
        this.extraIdSave = extraIdSave;
    }

    public String getRes_msg() {
        return res_msg;
    }

    public void setRes_msg(String res_msg) {
        this.res_msg = res_msg;
    }

    public String getExtraAmountSave() {
        return extraAmountSave;
    }

    public void setExtraAmountSave(String extraAmountSave) {
        this.extraAmountSave = extraAmountSave;
    }

    public String getExtraPurposeSave() {
        return extraPurposeSave;
    }

    public void setExtraPurposeSave(String extraPurposeSave) {
        this.extraPurposeSave = extraPurposeSave;
    }

    public String getExtraDateSave() {
        return extraDateSave;
    }

    public void setExtraDateSave(String extraDateSave) {
        this.extraDateSave = extraDateSave;
    }

    @Override
    public void setServletRequest(HttpServletRequest hsr) {
        request = hsr;
    }

    @Override
    public void setServletResponse(HttpServletResponse hsr) {
        response = hsr;
    }
    
    
    public String extraTransactionUpdate() {
        ExtraTransAction action = null;
        try {
            action = new ExtraTransAction();
            int i=action.extraUpdate(getExtraAmountSave(),getExtraDateSave(),getExtraIdSave(),getExtraPurposeSave());
            if(i>=0){
                setRes_msg("Successfully Updated ");
            }else{
                 setRes_msg("Record Failed To Update");
            }
            
            displayExtraTransaction = action.DisplayExtraDetails(getExtraSearchDate());
        } catch (Exception e) {
        }
        return SUCCESS;
    }
    
    
    
    public String extraTransactionSave() {
        ExtraTransAction action = null;
        try {
            action = new ExtraTransAction();
            
             int i=action.extraSave(getExtraAmountSave(),getExtraDateSave(),getExtraPurposeSave());
            if(i>=0){
                setRes_msg("Successfully Inserted ");
            }else{
                 setRes_msg("Record Failed To Insert");
            }
             
            displayExtraTransaction = action.DisplayExtraDetails(getExtraSearchDate());
        } catch (Exception e) {
            
        }
        return SUCCESS;
    }
    

    public String extraTransactionDelete() {
        ExtraTransAction action = null;
        try {
            action = new ExtraTransAction(); 
            int i=action.extraDelete(getExtraIdSave());
            if(i>=0){
                setRes_msg("Successfully Deleted ");
            }else{
                 setRes_msg("Record Failed To Delete");
            }
            
            displayExtraTransaction = action.DisplayExtraDetails(getExtraSearchDate());
        } catch (Exception e) {
                      System.out.println(" Exception Occured in ExtraTransAction(extraTransactionDelete) :: " + e.getMessage());
        }
        return SUCCESS;
    }

    public String extraTransaction() {

       ExtraTransAction action = null;
        try {
            action = new ExtraTransAction();
            displayExtraTransaction = action.DisplayExtraDetails(getExtraSearchDate());
        } catch (Exception e) {
        }
        return SUCCESS;
    }
}

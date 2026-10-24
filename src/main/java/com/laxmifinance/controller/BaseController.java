package com.laxmifinance.controller;

import com.laxmifinance.form.BondForm;
import com.laxmifinance.model.Customer;
import com.laxmifinance.repository.AccountsRepository;
import com.laxmifinance.util.Dates;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.math.BigDecimal;

public abstract class BaseController {

    @Autowired
    protected AccountsRepository accountsRepository;

    protected void addHeaderTotals(Model model) {
        model.addAttribute("collected", accountsRepository.collectedTotals());
        model.addAttribute("issued", accountsRepository.issuedAndExtraTotals());
    }

    protected Integer userId(HttpSession session) {
        Object value = session.getAttribute("userId");
        if (value instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }

    protected String userIdText(HttpSession session) {
        return String.valueOf(userId(session));
    }

    protected String userName(HttpSession session) {
        Object value = session.getAttribute("userName");
        return value == null ? "" : value.toString();
    }

    protected BondForm toBondForm(Customer customer) {
        BondForm form = new BondForm();
        form.setCustomerId(String.valueOf(customer.getCustomerId()));
        form.setAccountNumberNew(nullToEmpty(customer.getAccountNumber()));
        form.setAccountNo(nullToEmpty(customer.getAccountNo()));
        form.setCustomerNameN(nullToEmpty(customer.getCustomerName()));
        form.setCustRel(relation(customer.getCustRel()));
        form.setFatherName(nullToEmpty(customer.getCustomerFatherName()));
        form.setOccupation(nullToEmpty(customer.getCustomerOccupation()));
        form.setAddress(nullToEmpty(customer.getCustomerAddress()));
        form.setMobileNumber(nullToEmpty(customer.getCustomerMobileNo()));
        form.setSuretyName(nullToEmpty(customer.getSuretyName()));
        form.setSureRel(relation(customer.getSurRel()));
        form.setSuretyFatherName(nullToEmpty(customer.getSuretyFname()));
        form.setSuretyOccupation(nullToEmpty(customer.getSuretyOccupation()));
        form.setSuretyAddress(nullToEmpty(customer.getSuretyAddress()));
        form.setSuretyMobileNumber(nullToEmpty(customer.getSuretyMobileNo()));
        form.setCustomerSecMobile(nullToEmpty(customer.getCustomerSecMobileNo()));
        form.setSuretySecMobile(nullToEmpty(customer.getSuretySecMobileNo()));
        form.setAmountNew(nullToEmpty(customer.getAmount()));
        form.setOriginalAmount(nullToEmpty(customer.getOriginalAmount()));
        form.setInstallmentsN(nullToEmpty(customer.getInstallments()));
        form.setAtm(nullToEmpty(customer.getAtm()));
        form.setCheque(customer.getCheque() == null || customer.getCheque().isBlank() ? "0" : customer.getCheque());
        form.setPassBook(nullToEmpty(customer.getPassbook()));
        form.setIssuedDate(Dates.isValid(customer.getDateOfIssueRaw())
                ? customer.getDateOfIssueRaw() : Dates.today());
        return form;
    }

    protected String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    protected String clean(String value) {
        if (value == null || value.isBlank() || "0".equals(value.trim())) {
            return "";
        }
        return value.trim();
    }

    protected boolean isNumber(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            new BigDecimal(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected boolean isInteger(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String relation(String value) {
        return value == null || value.isBlank() ? "S/o" : value;
    }
}

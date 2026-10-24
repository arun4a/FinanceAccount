package com.laxmifinance.controller;

import com.laxmifinance.form.BondForm;
import com.laxmifinance.form.DailySheetForm;
import com.laxmifinance.model.Customer;
import com.laxmifinance.repository.CustomerRepository;
import com.laxmifinance.util.Dates;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class DashboardController extends BaseController {

    private final CustomerRepository customers;

    public DashboardController(CustomerRepository customers) {
        this.customers = customers;
    }

    @GetMapping({"/successAction", "/dailySheetSuccess"})
    public String dashboard(HttpSession session, Model model) {
        return renderDashboard(model, session, newDailyForm(session), newBondForm(session), null, null);
    }

    @PostMapping("/getCustomerDailySheet")
    public String customerForDailySheet(@ModelAttribute("daily") DailySheetForm daily,
                                        HttpSession session, Model model) {
        String accountNumber = clean(daily.getAccountNumber());
        if (accountNumber.isEmpty()) {
            model.addAttribute("message", "Please enter Account Number");
        } else {
            Map<String, String> details = customers.findCustomerNameByAccount(accountNumber);
            if (details.isEmpty()) {
                daily.setCustomerName("");
                model.addAttribute("message", "No customer found for Account Number " + accountNumber);
            } else {
                daily.setCustomerName(details.getOrDefault("getDetailsCustomerName", ""));
            }
        }
        return renderDashboard(model, session, daily, newBondForm(session),
                daily.getCustomerName(), accountNumber);
    }

    @PostMapping("/successActionGet")
    public String customerForBond(@ModelAttribute("bond") BondForm bond,
                                  HttpSession session, Model model) {
        if ("get".equals(bond.getGetId())) {
            String accountNumber = clean(bond.getAccountNumberNew());
            Customer customer = accountNumber.isEmpty() ? null : customers.findByAccountNumber(accountNumber);
            if (customer == null) {
                model.addAttribute("message", "No customer found for A/C No " + accountNumber);
            } else {
                bond = toBondForm(customer);
            }
        }
        return renderDashboard(model, session, newDailyForm(session), bond, null, null);
    }

    @PostMapping("/dailySheet")
    public String saveDailySheet(@ModelAttribute DailySheetForm form, HttpSession session,
                                 RedirectAttributes redirect) {
        if (clean(form.getAccountNumber()).isEmpty() || clean(form.getCustomerName()).isEmpty()
                || !Dates.isValid(form.getDate())) {
            redirect.addFlashAttribute("message", "Please fill Date, Account Number and Customer Name");
            return "redirect:/successAction";
        }
        if (!isNumber(form.getActaulAmount()) || !isNumber(form.getPaidAmount())
                || !isInteger(form.getInstallment())) {
            redirect.addFlashAttribute("message", "Please enter numbers only");
            return "redirect:/successAction";
        }
        int inserted = accountsRepository.insertDailySheet(form, userIdText(session));
        session.setAttribute("dailySheetDate", form.getDate());
        if (inserted > 0) {
            redirect.addFlashAttribute("message", form.getCustomerName() + " " + form.getInstallment()
                    + " Installment  Inserted Successfully");
        } else {
            redirect.addFlashAttribute("message", form.getCustomerName() + " Insert Failed");
        }
        return "redirect:/successAction";
    }

    @PostMapping("/customerDetails")
    public String saveBond(@ModelAttribute BondForm form, HttpSession session,
                           RedirectAttributes redirect) {
        if (clean(form.getAccountNumberNew()).isEmpty() || clean(form.getCustomerNameN()).isEmpty()
                || !Dates.isValid(form.getIssuedDate())) {
            redirect.addFlashAttribute("message",
                    "Please fill Date, Account Number and Customer Name");
            return "redirect:/successAction";
        }
        if (!isNumber(form.getAmountNew()) || !isNumber(form.getOriginalAmount())
                || !isInteger(form.getInstallmentsN())) {
            redirect.addFlashAttribute("message", "Please enter numbers only");
            return "redirect:/successAction";
        }
        int inserted = customers.insertBond(form, userIdText(session));
        session.setAttribute("issuedBondDate", form.getIssuedDate());
        if (inserted > 0) {
            redirect.addFlashAttribute("message", form.getCustomerNameN() + " Details Added Successfully");
        } else {
            redirect.addFlashAttribute("message", form.getCustomerNameN() + " Insert Failed");
        }
        return "redirect:/successAction";
    }

    private String renderDashboard(Model model, HttpSession session, DailySheetForm daily,
                                   BondForm bond, String detailsName, String detailsAccount) {
        addHeaderTotals(model);
        model.addAttribute("daily", daily);
        model.addAttribute("bond", bond);
        if (detailsName != null) {
            model.addAttribute("getDetailsCustomerName", detailsName);
        }
        if (detailsAccount != null) {
            model.addAttribute("getDetailsAccountNumber", detailsAccount);
        }
        return "dashboard";
    }

    private DailySheetForm newDailyForm(HttpSession session) {
        DailySheetForm form = new DailySheetForm();
        String date = (String) session.getAttribute("dailySheetDate");
        form.setDate(Dates.isValid(date) ? date : Dates.today());
        form.setDepositAmount("0");
        form.setDepositName("0");
        form.setDepositBy("0");
        form.setClose("No");
        return form;
    }

    private BondForm newBondForm(HttpSession session) {
        BondForm form = new BondForm();
        String date = (String) session.getAttribute("issuedBondDate");
        form.setIssuedDate(Dates.isValid(date) ? date : Dates.today());
        form.setCustRel("S/o");
        form.setSureRel("S/o");
        form.setCheque("0");
        return form;
    }

}

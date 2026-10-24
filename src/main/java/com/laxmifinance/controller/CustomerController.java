package com.laxmifinance.controller;

import com.laxmifinance.form.BondForm;
import com.laxmifinance.form.MobileForm;
import com.laxmifinance.model.Customer;
import com.laxmifinance.repository.CustomerRepository;
import com.laxmifinance.util.Dates;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
public class CustomerController extends BaseController {

    private final CustomerRepository customers;

    public CustomerController(CustomerRepository customers) {
        this.customers = customers;
    }

    @GetMapping("/customerTab")
    public String openCustomerList(@RequestParam(defaultValue = "") String customerNameSearch,
                                   @RequestParam(defaultValue = "") String accountNumber,
                                   @RequestParam(defaultValue = "") String mobileNumber,
                                   Model model) {
        return renderList("open", customerNameSearch, accountNumber, mobileNumber, model);
    }

    @GetMapping("/closingCustomerAction")
    public String closingCustomerList(@RequestParam(defaultValue = "") String customerNameSearch,
                                      @RequestParam(defaultValue = "") String accountNumber,
                                      @RequestParam(defaultValue = "") String mobileNumber,
                                      Model model) {
        return renderList("close", customerNameSearch, accountNumber, mobileNumber, model);
    }

    @GetMapping("/customerDetailsUpdate")
    public String openCustomerUpdate(@RequestParam(required = false) String customerId,
                                     Model model, RedirectAttributes redirect) {
        return renderUpdate(customerId, "open", model, redirect);
    }

    @GetMapping("/customerDetailsUpdateClose")
    public String closingCustomerUpdate(@RequestParam(required = false) String customerId,
                                        Model model, RedirectAttributes redirect) {
        return renderUpdate(customerId, "close", model, redirect);
    }

    @PostMapping("/customerDetailsUpdates")
    public String updateOpenCustomer(@ModelAttribute("bond") BondForm form, HttpSession session,
                                     RedirectAttributes redirect) {
        return updateCustomer(form, "open", session, redirect);
    }

    @PostMapping("/customerDetailsUpdatesClose")
    public String updateClosingCustomer(@ModelAttribute("bond") BondForm form, HttpSession session,
                                        RedirectAttributes redirect) {
        return updateCustomer(form, "close", session, redirect);
    }

    @PostMapping("/customerDetailsDelete")
    public String deleteOpenCustomer(@RequestParam(required = false) String customerId,
                                     RedirectAttributes redirect) {
        return deleteCustomer(customerId, "open", redirect);
    }

    @PostMapping("/customerDetailsDeleteClose")
    public String deleteClosingCustomer(@RequestParam(required = false) String customerId,
                                        RedirectAttributes redirect) {
        return deleteCustomer(customerId, "close", redirect);
    }

    @GetMapping("/mobileNumber")
    public String mobileNumbers(@RequestParam(defaultValue = "") String customerNameSearch,
                                @RequestParam(defaultValue = "") String accountNumber,
                                Model model) {
        String nameFilter = clean(customerNameSearch);
        String accountFilter = clean(accountNumber);
        boolean hasFilter = !nameFilter.isEmpty() || !accountFilter.isEmpty();
        List<com.laxmifinance.model.MobileRow> rows = hasFilter
                ? customers.findMobileRows(nameFilter, accountFilter)
                : List.of();
        addHeaderTotals(model);
        model.addAttribute("mobileRows", rows);
        model.addAttribute("customerNameSearch", nameFilter);
        model.addAttribute("accountNumber", accountFilter);
        model.addAttribute("customerMobId", rows.isEmpty() ? "" : rows.get(rows.size() - 1).getCustomerId());
        return "mobileSearch";
    }

    @PostMapping("/mobileNumberAdd")
    public String addMobileNumbers(@ModelAttribute MobileForm form, RedirectAttributes redirect) {
        String customerId = clean(form.getCustomerMobId());
        if (customerId.isEmpty()) {
            redirect.addFlashAttribute("message", "Please search and select a customer first");
        } else {
            int updated = customers.updateSecondMobiles(customerId, form.getCustomer2mob(), form.getSurety2mob());
            redirect.addFlashAttribute("message", updated > 0
                    ? "Mobile Numbers Updated Successfully"
                    : "Mobile Numbers Update Failed");
        }
        redirect.addAttribute("customerNameSearch", clean(form.getCustomerNameSearch()));
        redirect.addAttribute("accountNumber", clean(form.getAccountNumber()));
        return "redirect:/mobileNumber";
    }

    private String renderList(String status, String customerName, String accountNumber, String mobileNumber,
                              Model model) {
        List<Customer> rows = customers.findBonds(customerName, accountNumber, status, mobileNumber);
        markMonthGroups(rows, "close".equals(status));
        addHeaderTotals(model);
        model.addAttribute("customers", rows);
        model.addAttribute("customerNameSearch", clean(customerName));
        model.addAttribute("accountNumber", clean(accountNumber));
        model.addAttribute("mobileNumber", clean(mobileNumber));
        model.addAttribute("mode", status);
        return "close".equals(status) ? "customer/closingCustomers" : "customer/customerDetails";
    }

    private String renderUpdate(String customerId, String mode, Model model, RedirectAttributes redirect) {
        Customer customer = clean(customerId).isEmpty() ? null : customers.findById(customerId);
        if (customer == null) {
            redirect.addFlashAttribute("message", "Customer not found");
            return redirectList(mode);
        }
        addHeaderTotals(model);
        model.addAttribute("bond", toBondForm(customer));
        model.addAttribute("mode", mode);
        model.addAttribute("customer", customer);
        return "customer/updateCustomer";
    }

    private String updateCustomer(BondForm form, String mode, HttpSession session,
                                  RedirectAttributes redirect) {
        String customerId = clean(form.getCustomerId());
        if (customerId.isEmpty()) {
            redirect.addFlashAttribute("message", "Customer not found");
            return redirectList(mode);
        }
        if (clean(form.getCustomerNameN()).isEmpty() || clean(form.getAccountNumberNew()).isEmpty()
                || !Dates.isValid(form.getIssuedDate())) {
            redirect.addFlashAttribute("message", "Please fill Date, Account Number and Customer Name");
            return redirectUpdate(customerId, mode);
        }
        if (!isNumber(form.getAmountNew()) || !isNumber(form.getOriginalAmount())
                || !isInteger(form.getInstallmentsN())) {
            redirect.addFlashAttribute("message", "Please enter numbers only");
            return redirectUpdate(customerId, mode);
        }
        int updated = customers.updateBond(form, userIdText(session));
        if (updated > 0) {
            redirect.addFlashAttribute("message", form.getCustomerNameN() + " Details Updated Successfully ");
        } else {
            redirect.addFlashAttribute("message", form.getCustomerNameN() + " Update Failed ");
        }
        return redirectList(mode);
    }

    private String deleteCustomer(String customerId, String mode, RedirectAttributes redirect) {
        Customer customer = clean(customerId).isEmpty() ? null : customers.findById(customerId);
        String name = customer == null ? "" : customer.getCustomerName();
        int deleted = clean(customerId).isEmpty() ? 0 : customers.deleteBond(customerId);
        if (deleted > 0) {
            redirect.addFlashAttribute("message", name + " Details Deleted Successfully ");
        } else {
            redirect.addFlashAttribute("message", name + " Deleted Failed ");
        }
        return redirectList(mode);
    }

    private void markMonthGroups(List<Customer> rows, boolean closing) {
        String previous = null;
        for (Customer customer : rows) {
            String month = closing ? customer.getClosingMonthName() : customer.getMonthName();
            customer.setGroupMonth(month);
            customer.setShowMonthHeader(!Objects.equals(previous, month));
            previous = month;
        }
    }

    private String redirectList(String mode) {
        return "close".equals(mode) ? "redirect:/closingCustomerAction" : "redirect:/customerTab";
    }

    private String redirectUpdate(String customerId, String mode) {
        String path = "close".equals(mode) ? "/customerDetailsUpdateClose" : "/customerDetailsUpdate";
        return "redirect:" + path + "?customerId=" + customerId;
    }
}

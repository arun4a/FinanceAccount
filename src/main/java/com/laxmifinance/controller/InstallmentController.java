package com.laxmifinance.controller;

import com.laxmifinance.form.InstallmentForm;
import com.laxmifinance.model.Installment;
import com.laxmifinance.repository.AccountsRepository;
import com.laxmifinance.util.Dates;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Controller
public class InstallmentController extends BaseController {

    private static final String NO_INSTALLMENTS = "No Installments Paid";

    @GetMapping("/instalOpenAction")
    public String openInstallmentsEntry() {
        return "redirect:/customerTab";
    }

    @GetMapping("/instalCloseAction")
    public String closeInstallmentsEntry() {
        return "redirect:/closingCustomerAction";
    }

    @GetMapping("/installDetails")
    public String openInstallments(@RequestParam(name = "accountNumberNew", required = false) String accountNumberNew,
                                   @RequestParam(name = "accountNumber", required = false) String accountNumber,
                                   Model model, RedirectAttributes redirect) {
        return loadInstallments(accountNumberNew, accountNumber, "open", model, redirect);
    }

    @GetMapping("/installDetailsClose")
    public String closeInstallments(@RequestParam(name = "accountNumberNew", required = false) String accountNumberNew,
                                    @RequestParam(name = "accountNumber", required = false) String accountNumber,
                                    Model model, RedirectAttributes redirect) {
        return loadInstallments(accountNumberNew, accountNumber, "close", model, redirect);
    }

    @GetMapping("/displayInstall")
    public String displayOpenInstallment(@RequestParam(required = false) String accountId,
                                         @RequestParam(required = false) String accountNumber,
                                         Model model, RedirectAttributes redirect) {
        return displayInstallment(accountId, accountNumber, "open", model, redirect);
    }

    @GetMapping("/displayInstallClose")
    public String displayCloseInstallment(@RequestParam(required = false) String accountId,
                                          @RequestParam(required = false) String accountNumber,
                                          Model model, RedirectAttributes redirect) {
        return displayInstallment(accountId, accountNumber, "close", model, redirect);
    }

    @PostMapping("/updateInstallDetails")
    public String updateOpenInstallment(@ModelAttribute InstallmentForm form, HttpSession session,
                                        RedirectAttributes redirect) {
        return updateInstallment(form, "open", session, redirect);
    }

    @PostMapping("/updateInstallDetailsClose")
    public String updateCloseInstallment(@ModelAttribute InstallmentForm form, HttpSession session,
                                         RedirectAttributes redirect) {
        return updateInstallment(form, "close", session, redirect);
    }

    @PostMapping("/deleteInstallDetails")
    public String deleteOpenInstallment(@ModelAttribute InstallmentForm form, RedirectAttributes redirect) {
        return deleteInstallment(form, "open", redirect);
    }

    @PostMapping("/deleteInstallDetailsClose")
    public String deleteCloseInstallment(@ModelAttribute InstallmentForm form, RedirectAttributes redirect) {
        return deleteInstallment(form, "close", redirect);
    }

    @GetMapping("/getDailySheetDetailsInReport")
    public String dailySheetReportDetails(@RequestParam(required = false) String accountId,
                                          @RequestParam(required = false) String accountNumber,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        Installment selected = clean(accountId).isEmpty()
                ? null : accountsRepository.findInstallmentById(accountId);
        String selectedAccount = selected == null ? clean(accountNumber) : clean(selected.getAccountNumber());
        List<Installment> report = accountsRepository.findInstallmentsBetween(start, end);
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("selected", selected);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("accountNumber", selectedAccount);
        addTotals(model, report);
        return "report/dailySheetReport";
    }

    @PostMapping("/updateInstallDetailsInReports")
    public String updateReportInstallment(@ModelAttribute InstallmentForm form, HttpSession session,
                                          RedirectAttributes redirect) {
        String accountId = clean(form.getAccountId());
        if (accountId.isEmpty()) {
            redirect.addFlashAttribute("message", "Installment not found");
            return redirectReport(form, redirect);
        }
        if (clean(form.getAccountNumber()).isEmpty() || !Dates.isValid(form.getDate())) {
            redirect.addFlashAttribute("message", "Please fill Date, Account Number and Customer Name");
            return redirectReport(form, redirect);
        }
        if (!validNumbers(form)) {
            redirect.addFlashAttribute("message", "Please enter numbers only");
            return redirectReport(form, redirect);
        }
        int updated = accountsRepository.updateInstallment(form, userIdText(session));
        if (updated > 0) {
            redirect.addFlashAttribute("message", form.getCustomerName() + " Details Updated Successfully ");
        } else {
            redirect.addFlashAttribute("message", form.getCustomerName() + " Update Failed ");
        }
        return redirectReport(form, redirect);
    }

    @PostMapping("/deleteInstallDetailsInReports")
    public String deleteReportInstallment(@ModelAttribute InstallmentForm form, RedirectAttributes redirect) {
        Installment existing = clean(form.getAccountId()).isEmpty()
                ? null : accountsRepository.findInstallmentById(form.getAccountId());
        String customerName = clean(form.getCustomerName());
        if (customerName.isEmpty() && existing != null) {
            customerName = clean(existing.getCustomerName());
        }
        int deleted = clean(form.getAccountId()).isEmpty()
                ? 0 : accountsRepository.deleteInstallment(form.getAccountId());
        if (deleted > 0) {
            redirect.addFlashAttribute("message", customerName + " Details Deleted Successfully ");
        } else {
            redirect.addFlashAttribute("message", customerName + " Deleted Failed ");
        }
        redirect.addAttribute("accountNumber", clean(form.getAccountNumber()));
        redirect.addAttribute("startDate", Dates.orDefaultStart(form.getStartDate()));
        redirect.addAttribute("endDate", Dates.orDefaultEnd(form.getEndDate()));
        return "redirect:/getDailySheetDetailsInReport";
    }

    private String loadInstallments(String accountNumberNew, String accountNumber, String mode,
                                    Model model, RedirectAttributes redirect) {
        String account = firstNonBlank(accountNumberNew, accountNumber);
        if (account.isEmpty()) {
            redirect.addFlashAttribute("message", "Please enter Account Number");
            return redirectCustomers(mode);
        }
        List<Installment> installments = accountsRepository.findInstallmentsByAccount(account);
        if (installments.isEmpty()) {
            redirect.addFlashAttribute("message", NO_INSTALLMENTS);
            return redirectCustomers(mode);
        }
        return renderInstallments(mode, account, installments, null, model);
    }

    private String displayInstallment(String accountId, String accountNumber, String mode,
                                      Model model, RedirectAttributes redirect) {
        Installment selected = clean(accountId).isEmpty()
                ? null : accountsRepository.findInstallmentById(accountId);
        String account = selected == null ? clean(accountNumber) : clean(selected.getAccountNumber());
        if (account.isEmpty()) {
            redirect.addFlashAttribute("message", "Please enter Account Number");
            return redirectCustomers(mode);
        }
        List<Installment> installments = accountsRepository.findInstallmentsByAccount(account);
        if (installments.isEmpty()) {
            redirect.addFlashAttribute("message", NO_INSTALLMENTS);
            return redirectCustomers(mode);
        }
        if (selected == null) {
            redirect.addFlashAttribute("message", "Installment not found");
            return redirectList(mode, account);
        }
        return renderInstallments(mode, account, installments, selected, model);
    }

    private String updateInstallment(InstallmentForm form, String mode, HttpSession session,
                                     RedirectAttributes redirect) {
        String account = clean(form.getAccountNumber());
        if (clean(form.getAccountId()).isEmpty() || account.isEmpty()) {
            redirect.addFlashAttribute("message", "Installment not found");
            return redirectList(mode, account);
        }
        if (!Dates.isValid(form.getDate())) {
            redirect.addFlashAttribute("message", "Please select a valid date");
            return redirectList(mode, account);
        }
        if (!validNumbers(form)) {
            redirect.addFlashAttribute("message", "Please enter numbers only");
            return redirectList(mode, account);
        }
        int updated = accountsRepository.updateInstallment(form, userIdText(session));
        if (updated > 0) {
            redirect.addFlashAttribute("message", form.getCustomerName() + " Details Updated Successfully ");
        } else {
            redirect.addFlashAttribute("message", form.getCustomerName() + " Update Failed ");
        }
        return redirectList(mode, account);
    }

    private String deleteInstallment(InstallmentForm form, String mode, RedirectAttributes redirect) {
        Installment existing = clean(form.getAccountId()).isEmpty()
                ? null : accountsRepository.findInstallmentById(form.getAccountId());
        String account = clean(form.getAccountNumber());
        if (account.isEmpty() && existing != null) {
            account = clean(existing.getAccountNumber());
        }
        String customerName = clean(form.getCustomerName());
        if (customerName.isEmpty() && existing != null) {
            customerName = clean(existing.getCustomerName());
        }
        int deleted = clean(form.getAccountId()).isEmpty()
                ? 0 : accountsRepository.deleteInstallment(form.getAccountId());
        if (deleted > 0) {
            redirect.addFlashAttribute("message", customerName + " Details Deleted Successfully ");
        } else {
            redirect.addFlashAttribute("message", customerName + " Deleted Failed ");
        }
        return redirectList(mode, account);
    }

    private String renderInstallments(String mode, String accountNumber, List<Installment> installments,
                                      Installment selected, Model model) {
        addHeaderTotals(model);
        model.addAttribute("installments", installments);
        model.addAttribute("selected", selected);
        model.addAttribute("accountNumber", accountNumber);
        model.addAttribute("mode", mode);
        model.addAttribute("listUrl", "close".equals(mode) ? "/installDetailsClose" : "/installDetails");
        model.addAttribute("displayUrl", "close".equals(mode) ? "/displayInstallClose" : "/displayInstall");
        model.addAttribute("updateUrl", "close".equals(mode) ? "/updateInstallDetailsClose" : "/updateInstallDetails");
        model.addAttribute("deleteUrl", "close".equals(mode) ? "/deleteInstallDetailsClose" : "/deleteInstallDetails");
        model.addAttribute("backUrl", "close".equals(mode) ? "/closingCustomerAction" : "/customerTab");
        addTotals(model, installments);
        return "installment/installmentDetails";
    }

    private void addTotals(Model model, List<Installment> installments) {
        model.addAttribute("actualTotal", total(installments, Installment::getActualAmount));
        model.addAttribute("paidTotal", total(installments, Installment::getPaidAmount));
        model.addAttribute("dueTotal", total(installments, Installment::getDueAmount));
    }

    private double total(List<Installment> installments, Function<Installment, String> getter) {
        double total = 0;
        for (Installment installment : installments) {
            try {
                total += Double.parseDouble(getter.apply(installment));
            } catch (RuntimeException ignored) {
                // blank or non numeric legacy values contribute zero
            }
        }
        return total;
    }

    private boolean validNumbers(InstallmentForm form) {
        return isNumber(form.getOpeningBalance()) && isNumber(form.getDepositAmount())
                && isNumber(form.getActualAmount()) && isNumber(form.getPaidAmount())
                && isInteger(form.getInstallment());
    }

    private String redirectList(String mode, String accountNumber) {
        String path = "close".equals(mode) ? "/installDetailsClose" : "/installDetails";
        String account = accountNumber == null ? "" : URLEncoder.encode(accountNumber, StandardCharsets.UTF_8);
        return "redirect:" + path + "?accountNumberNew=" + account;
    }

    private String redirectCustomers(String mode) {
        return "close".equals(mode) ? "redirect:/closingCustomerAction" : "redirect:/customerTab";
    }

    private String redirectReport(InstallmentForm form, RedirectAttributes redirect) {
        redirect.addAttribute("accountId", clean(form.getAccountId()));
        redirect.addAttribute("accountNumber", clean(form.getAccountNumber()));
        redirect.addAttribute("startDate", Dates.orDefaultStart(form.getStartDate()));
        redirect.addAttribute("endDate", Dates.orDefaultEnd(form.getEndDate()));
        return "redirect:/getDailySheetDetailsInReport";
    }

    private String firstNonBlank(String first, String second) {
        String value = clean(first);
        return value.isEmpty() ? clean(second) : value;
    }
}

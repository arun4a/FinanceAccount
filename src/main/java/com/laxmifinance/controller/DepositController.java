package com.laxmifinance.controller;

import com.laxmifinance.form.DepositForm;
import com.laxmifinance.form.MonthlyPaymentForm;
import com.laxmifinance.model.Deposit;
import com.laxmifinance.model.MonthlyPayment;
import com.laxmifinance.repository.DepositRepository;
import com.laxmifinance.util.Dates;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DepositController extends BaseController {

    private final DepositRepository deposits;

    public DepositController(DepositRepository deposits) {
        this.deposits = deposits;
    }

    @GetMapping({"/depositsAction", "/displayDepositDetails", "/searchDeposits"})
    public String depositPage(@RequestParam(defaultValue = "") String depositNameSearch,
                              @RequestParam(defaultValue = "") String depositAccountNumber,
                              HttpServletRequest request, Model model) {
        DepositForm form = editForm(request);
        renderDepositsPage(depositNameSearch, depositAccountNumber, form, null, model);
        return "deposit/depositDetails";
    }

    @PostMapping("/depositDetailsSave")
    public String saveDeposit(@ModelAttribute DepositForm form, RedirectAttributes redirect) {
        if (!validDepositForm(form, false)) {
            redirect.addFlashAttribute("message", "Please fill Deposit Name, Account Number, Date and Amount");
            return "redirect:/displayDepositDetails";
        }
        if (deposits.accountExists(form.getAccountNumberSave())) {
            redirect.addFlashAttribute("message", "Sorry record already existed!");
            return "redirect:/displayDepositDetails";
        }
        int inserted = deposits.insertDeposit(form);
        redirect.addFlashAttribute("message", inserted > 0
                ? form.getDepositNameSave() + " Insertion Successfully!"
                : form.getDepositNameSave() + " Insertion Failed!");
        return "redirect:/displayDepositDetails";
    }

    @PostMapping("/depositDetailsUpdate")
    public String updateDeposit(@ModelAttribute DepositForm form, RedirectAttributes redirect) {
        if (!validDepositForm(form, true)) {
            redirect.addFlashAttribute("message", "Please fill Deposit Name, Account Number, Date and Amount");
            return "redirect:/displayDepositDetails";
        }
        int updated = deposits.updateDeposit(form);
        redirect.addFlashAttribute("message", updated > 0
                ? form.getDepositNameSave() + " Updation Successfully!"
                : form.getDepositNameSave() + " Updation Failed!");
        return "redirect:/displayDepositDetails";
    }

    @PostMapping("/depositDetailsDelete")
    public String deleteDeposit(@ModelAttribute DepositForm form, RedirectAttributes redirect) {
        if (clean(form.getDepositIdSave()).isEmpty()) {
            redirect.addFlashAttribute("message", "Please select a deposit to delete");
            return "redirect:/displayDepositDetails";
        }
        int deleted = deposits.deleteDeposit(form.getDepositIdSave());
        redirect.addFlashAttribute("message", deleted > 0
                ? form.getDepositNameSave() + " Deleted Successfully!"
                : form.getDepositNameSave() + " Delete Failed!");
        return "redirect:/displayDepositDetails";
    }

    @PostMapping("/depositMonthPayment")
    public String saveMonthlyPayment(@RequestParam(required = false) String accountNumberMonth,
                                     @RequestParam(required = false) String paymentDate,
                                     @RequestParam(required = false) String paidAmountA,
                                     @RequestParam(required = false) String amountMonth,
                                     RedirectAttributes redirect) {
        String account = clean(accountNumberMonth);
        if (account.isEmpty() || !Dates.isValid(paymentDate)
                || !isNumber(paidAmountA) || !isNumber(amountMonth)) {
            redirect.addFlashAttribute("message", "Please enter Account Number, valid Date and Amounts");
            return "redirect:/displayDepositDetails";
        }
        MonthlyPaymentForm form = new MonthlyPaymentForm();
        form.setDate(paymentDate);
        form.setDepositAccountNoUpdate(account);
        form.setPaidAmountUpdate(amountMonth);
        form.setPaidAmountFromActualUpdate(paidAmountA);
        int inserted = deposits.insertMonthlyPayment(form);
        redirect.addFlashAttribute("message", inserted > 0
                ? account + " Monthly Interest record Inserted successfully "
                : account + " Monthly Interest record Failed To Inserted ");
        return "redirect:/displayDepositDetails";
    }

    @GetMapping(value = "/ajaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> interestDetails(@RequestParam(required = false) String accountNumberMonth,
                                               @RequestParam(required = false) String paymentDate) {
        String account = clean(accountNumberMonth);
        if (account.isEmpty()) {
            Map<String, String> empty = new LinkedHashMap<>();
            empty.put("Interest", "0");
            empty.put("EnterAccountNumber", "");
            empty.put("InterestForDays", "");
            empty.put("EnterPaymentDate", Dates.isValid(paymentDate) ? paymentDate : Dates.today());
            return empty;
        }
        return deposits.interestDetails(account, Dates.isValid(paymentDate) ? paymentDate : Dates.today());
    }

    @PostMapping("/ajaxAction")
    public String interestDetailsPost(@RequestParam(required = false) String accountNumberMonth,
                                      @RequestParam(required = false) String paymentDate,
                                      @RequestParam(defaultValue = "") String depositNameSearch,
                                      @RequestParam(defaultValue = "") String depositAccountNumber,
                                      Model model) {
        String account = clean(accountNumberMonth);
        Map<String, String> details = account.isEmpty()
                ? Map.of()
                : deposits.interestDetails(account, Dates.isValid(paymentDate) ? paymentDate : Dates.today());
        renderDepositsPage(depositNameSearch, depositAccountNumber, new DepositForm(), details, model);
        return "deposit/depositDetails";
    }

    @GetMapping("/displayMonthlyDetails")
    public String monthlyDetails(@RequestParam(defaultValue = "") String depositAccountNumber,
                                 Model model) {
        return renderMonthlyDetails(depositAccountNumber, null, model);
    }

    @GetMapping("/displayPaymentInterestDetails")
    public String paymentInterestDetails(@RequestParam(defaultValue = "") String depositAccountNumber,
                                         @RequestParam(required = false) String depositsIdDispaly,
                                         Model model) {
        MonthlyPayment selected = clean(depositsIdDispaly).isEmpty()
                ? null : deposits.findMonthlyPaymentById(depositsIdDispaly);
        return renderMonthlyDetails(depositAccountNumber, selected, model);
    }

    @PostMapping("/updateMonthPayment")
    public String updateMonthlyPayment(@ModelAttribute MonthlyPaymentForm form, RedirectAttributes redirect) {
        if (clean(form.getDepositsIdUpdate()).isEmpty() || clean(form.getDepositAccountNoUpdate()).isEmpty()
                || !Dates.isValid(form.getDate())
                || !isNumber(form.getPaidAmountUpdate()) || !isNumber(form.getPaidAmountFromActualUpdate())) {
            redirect.addFlashAttribute("message", "Please fill Account Number, valid Date and Amounts");
            return redirectMonthly(form);
        }
        int updated = deposits.updateMonthlyPayment(form);
        redirect.addFlashAttribute("message", updated > 0
                ? form.getDepositAccountNoUpdate() + " Monthly Interest record Updated successfully "
                : form.getDepositAccountNoUpdate() + " Monthly Interest record Failed To Update ");
        return redirectMonthly(form);
    }

    @PostMapping("/deleteMonthPayment")
    public String deleteMonthlyPayment(@ModelAttribute MonthlyPaymentForm form, RedirectAttributes redirect) {
        if (clean(form.getDepositsIdUpdate()).isEmpty()) {
            redirect.addFlashAttribute("message", "Please select a monthly interest record");
            return redirectMonthly(form);
        }
        int deleted = deposits.deleteMonthlyPayment(form.getDepositsIdUpdate());
        redirect.addFlashAttribute("message", deleted > 0
                ? form.getDepositAccountNoUpdate() + " Monthly Interest record Deleted successfully "
                : form.getDepositAccountNoUpdate() + " Monthly Interest record Failed To delete ");
        return redirectMonthly(form);
    }

    private void renderDepositsPage(String depositNameSearch, String depositAccountNumber, DepositForm form,
                                    Map<String, String> interest, Model model) {
        String nameSearch = clean(depositNameSearch);
        String accountSearch = clean(depositAccountNumber);
        List<Deposit> list = deposits.findDeposits(accountSearch, nameSearch);
        addHeaderTotals(model);
        model.addAttribute("deposits", list);
        model.addAttribute("totals", deposits.totals());
        model.addAttribute("depositForm", form);
        model.addAttribute("depositNameSearch", nameSearch);
        model.addAttribute("depositAccountNumber", accountSearch);
        if (interest != null) {
            model.addAttribute("interest", interest);
        }
    }

    private String renderMonthlyDetails(String depositAccountNumber, MonthlyPayment selected, Model model) {
        String account = clean(depositAccountNumber);
        if (account.isEmpty() && selected != null) {
            account = clean(selected.getDepositAccountNo());
        }
        List<MonthlyPayment> payments = account.isEmpty()
                ? List.of() : deposits.findMonthlyPayments(account);
        addHeaderTotals(model);
        model.addAttribute("payments", payments);
        model.addAttribute("selected", selected);
        model.addAttribute("depositAccountNumber", account);
        return "deposit/depositMonthlyDetails";
    }

    private DepositForm editForm(HttpServletRequest request) {
        DepositForm form = new DepositForm();
        String editId = request.getParameter("depositId");
        if (editId != null && !editId.isBlank()) {
            Deposit deposit = deposits.findDepositById(editId);
            if (deposit != null) {
                form.setDepositIdSave(deposit.getDepositId());
                form.setDepositNameSave(nullToEmpty(deposit.getDepositName()));
                form.setAccountNumberSave(nullToEmpty(deposit.getAccountNumber()));
                form.setOpendDateSave(nullToEmpty(deposit.getActualOpenDate()));
                form.setAmountSave(nullToEmpty(deposit.getAmount()));
                form.setCloseDateSave(nullToEmpty(deposit.getActualCloseDate()));
                form.setMobileNoSave(nullToEmpty(deposit.getMobileNo()));
                form.setDepositTypeSave(deposit.getDepositType() == null || deposit.getDepositType().isBlank()
                        ? "In" : deposit.getDepositType());
            }
        }
        form.setPaidAmountA("0");
        form.setAmountMonth("0");
        return form;
    }

    private boolean validDepositForm(DepositForm form, boolean requireId) {
        return (!requireId || !clean(form.getDepositIdSave()).isEmpty())
                && !clean(form.getDepositNameSave()).isEmpty()
                && !clean(form.getAccountNumberSave()).isEmpty()
                && Dates.isValid(form.getOpendDateSave())
                && isNumber(form.getAmountSave())
                && (form.getCloseDateSave() == null || form.getCloseDateSave().isBlank()
                    || Dates.isValid(form.getCloseDateSave()));
    }

    private String redirectMonthly(MonthlyPaymentForm form) {
        String account = clean(form.getDepositAccountNumber());
        if (account.isEmpty()) {
            account = clean(form.getDepositAccountNoUpdate());
        }
        return "redirect:/displayMonthlyDetails?depositAccountNumber="
                + java.net.URLEncoder.encode(account, java.nio.charset.StandardCharsets.UTF_8);
    }
}

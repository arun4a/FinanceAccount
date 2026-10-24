package com.laxmifinance.controller;

import com.laxmifinance.form.ExtraForm;
import com.laxmifinance.model.ExtraTransaction;
import com.laxmifinance.repository.ExtraTransactionRepository;
import com.laxmifinance.util.Dates;
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

@Controller
public class ExtraTransactionController extends BaseController {

    private final ExtraTransactionRepository extras;

    public ExtraTransactionController(ExtraTransactionRepository extras) {
        this.extras = extras;
    }

    @GetMapping({"/extraTransaction", "/searchExtraDetails", "/displayExtraDetails"})
    public String page(@RequestParam(required = false) String extraSearchDate,
                       @RequestParam(required = false) String openDate,
                       @RequestParam(required = false) String extraId,
                       @RequestParam(required = false) String extraAmount,
                       @RequestParam(required = false) String extraPurpose,
                       Model model) {
        String searchDate = Dates.isValid(extraSearchDate) ? extraSearchDate : "";
        List<ExtraTransaction> transactions = extras.findByDate(searchDate);
        ExtraForm form = new ExtraForm();
        form.setExtraSearchDate(searchDate);
        if (!clean(extraId).isEmpty()) {
            form.setExtraIdSave(clean(extraId));
            form.setExtraAmountSave(nullToEmpty(extraAmount));
            form.setExtraPurposeSave(nullToEmpty(extraPurpose));
            form.setExtraDateSave(Dates.isValid(openDate) ? openDate : Dates.today());
        } else {
            form.setExtraDateSave(Dates.isValid(searchDate) ? searchDate : Dates.today());
        }
        addHeaderTotals(model);
        model.addAttribute("transactions", transactions);
        model.addAttribute("total", extras.totalForDate(searchDate));
        model.addAttribute("extraSearchDate", searchDate);
        model.addAttribute("extraForm", form);
        model.addAttribute("editing", !clean(extraId).isEmpty());
        return "extraTransactions";
    }

    @PostMapping("/extraTransactionSave")
    public String save(@ModelAttribute ExtraForm form, RedirectAttributes redirect) {
        if (!valid(form)) {
            redirect.addFlashAttribute("message", "Please fill Purpose, valid Date and Amount");
            return redirectList(form);
        }
        int inserted = extras.insert(form);
        redirect.addFlashAttribute("message", inserted > 0
                ? "Successfully Inserted " : "Record Failed To Insert");
        return redirectList(form);
    }

    @PostMapping("/extraTransactionUpdate")
    public String update(@ModelAttribute ExtraForm form, RedirectAttributes redirect) {
        if (clean(form.getExtraIdSave()).isEmpty() || !valid(form)) {
            redirect.addFlashAttribute("message", "Please fill Purpose, valid Date and Amount");
            return redirectList(form);
        }
        int updated = extras.update(form);
        redirect.addFlashAttribute("message", updated > 0
                ? "Successfully Updated " : "Record Failed To Update");
        return redirectList(form);
    }

    @PostMapping("/extraTransactionDelete")
    public String delete(@ModelAttribute ExtraForm form, RedirectAttributes redirect) {
        if (clean(form.getExtraIdSave()).isEmpty()) {
            redirect.addFlashAttribute("message", "Please select a transaction to delete");
            return redirectList(form);
        }
        int deleted = extras.delete(form.getExtraIdSave());
        redirect.addFlashAttribute("message", deleted > 0
                ? "Successfully Deleted " : "Record Failed To Delete");
        return redirectList(form);
    }

    private boolean valid(ExtraForm form) {
        return !clean(form.getExtraPurposeSave()).isEmpty()
                && Dates.isValid(form.getExtraDateSave())
                && isNumber(form.getExtraAmountSave());
    }

    private String redirectList(ExtraForm form) {
        String searchDate = Dates.isValid(form.getExtraSearchDate()) ? form.getExtraSearchDate() : "";
        return "redirect:/extraTransaction?extraSearchDate="
                + URLEncoder.encode(searchDate, StandardCharsets.UTF_8);
    }
}

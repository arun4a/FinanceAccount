package com.laxmifinance.controller;

import com.laxmifinance.model.Customer;
import com.laxmifinance.model.Deposit;
import com.laxmifinance.model.ExtraTransaction;
import com.laxmifinance.model.Installment;
import com.laxmifinance.model.MonthlyPayment;
import com.laxmifinance.repository.CustomerRepository;
import com.laxmifinance.repository.DepositRepository;
import com.laxmifinance.repository.ExtraTransactionRepository;
import com.laxmifinance.repository.NetAmountRepository;
import com.laxmifinance.service.PdfExportService;
import com.laxmifinance.util.Dates;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReportController extends BaseController {

    private final CustomerRepository customers;
    private final DepositRepository deposits;
    private final ExtraTransactionRepository extras;
    private final NetAmountRepository netAmounts;
    private final PdfExportService pdfExport;

    public ReportController(CustomerRepository customers, DepositRepository deposits,
                            ExtraTransactionRepository extras, NetAmountRepository netAmounts,
                            PdfExportService pdfExport) {
        this.customers = customers;
        this.deposits = deposits;
        this.extras = extras;
        this.netAmounts = netAmounts;
        this.pdfExport = pdfExport;
    }

    @GetMapping("/report")
    public String issuedBondsReport(@RequestParam(required = false) String startDate,
                                    @RequestParam(required = false) String endDate,
                                    @RequestParam(required = false) String status,
                                    Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        String filter = clean(status);
        List<Customer> report = customers.findIssuedBondsReport(start, end, filter);
        double amountTotal = 0;
        double originalTotal = 0;
        double paidTotal = 0;
        double dueTotal = 0;
        double profitTotal = 0;
        for (Customer customer : report) {
            double amount = parse(customer.getAmount());
            double original = parse(customer.getOriginalAmount());
            amountTotal += amount;
            originalTotal += original;
            paidTotal += customer.getPaidAmount();
            dueTotal += original - customer.getPaidAmount();
            profitTotal += customer.getPaidAmount() - original;
        }
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("status", filter);
        model.addAttribute("amountTotal", amountTotal);
        model.addAttribute("originalTotal", originalTotal);
        model.addAttribute("paidTotal", paidTotal);
        model.addAttribute("dueTotal", dueTotal);
        model.addAttribute("profitTotal", profitTotal);
        return "report/report";
    }

    @GetMapping("/reportDailySheet")
    public String dailySheetReport(@RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String endDate,
                                   Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        List<Installment> report = accountsRepository.findInstallmentsBetween(start, end);
        double actualTotal = 0;
        double paidTotal = 0;
        double dueTotal = 0;
        for (Installment installment : report) {
            actualTotal += parse(installment.getActualAmount());
            paidTotal += parse(installment.getPaidAmount());
            dueTotal += parse(installment.getDueAmount());
        }
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("selected", null);
        model.addAttribute("accountNumber", "");
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("actualTotal", actualTotal);
        model.addAttribute("paidTotal", paidTotal);
        model.addAttribute("dueTotal", dueTotal);
        return "report/dailySheetReport";
    }

    @GetMapping("/reportDeposits")
    public String depositReport(@RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate,
                                @RequestParam(required = false) String status,
                                Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        String filter = clean(status);
        List<Deposit> report = deposits.depositReport(start, end, filter);
        double amountTotal = 0;
        double paidTotal = 0;
        for (Deposit deposit : report) {
            amountTotal += parse(deposit.getAmount());
            paidTotal += parse(deposit.getPaidAmount());
        }
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("status", filter);
        model.addAttribute("amountTotal", amountTotal);
        model.addAttribute("paidTotal", paidTotal);
        return "report/reportDeposit";
    }

    @GetMapping("/reportMonthlyInterest")
    public String monthlyInterestReport(@RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate,
                                        Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        List<MonthlyPayment> report = deposits.monthlyInterestReport(start, end);
        double paidTotal = 0;
        for (MonthlyPayment payment : report) {
            paidTotal += parse(payment.getPaidAmount());
        }
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("paidTotal", paidTotal);
        return "report/reportMonthlyInterest";
    }

    @GetMapping("/reportExtraTransaction")
    public String extraTransactionReport(@RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate,
                                         Model model) {
        String start = Dates.orDefaultStart(startDate);
        String end = Dates.orDefaultEnd(endDate);
        List<ExtraTransaction> report = extras.findBetween(start, end);
        double amountTotal = 0;
        for (ExtraTransaction transaction : report) {
            amountTotal += parse(transaction.getExtraAmount());
        }
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("amountTotal", amountTotal);
        return "report/extraReport";
    }

    @GetMapping("/monthlyReportDetails")
    public String monthlyReport(@RequestParam(required = false) String startDate, Model model) {
        String start = Dates.orDefaultStart(startDate);
        var report = netAmounts.monthlyReportDetails(start);
        double collections = parse(report.get("TotalCollections"));
        double lastMonthNet = parse(report.get("LasMonthNetAmount"));
        double depositIn = parse(report.get("TotalDepositsInAmount"));
        double bonds = parse(report.get("TotalBondsAmount"));
        double interest = parse(report.get("InterestAmount"));
        double extra = parse(report.get("ExtraAmount"));
        double depositOut = parse(report.get("TotalDepositsOutAmount"));
        double collectionsWithPrevious = collections + lastMonthNet;
        double netAmountTotal = collectionsWithPrevious + depositIn - bonds;
        double amountTotal = netAmountTotal - interest - extra;
        double monthlyNet = amountTotal - depositOut;
        addHeaderTotals(model);
        model.addAttribute("report", report);
        model.addAttribute("startDate", start);
        model.addAttribute("lastMonthNet", lastMonthNet);
        model.addAttribute("collections", collections);
        model.addAttribute("collectionsWithPrevious", collectionsWithPrevious);
        model.addAttribute("depositIn", depositIn);
        model.addAttribute("monthlyInTotal", parse(report.get("MonthlyInAmountTotal")));
        model.addAttribute("bonds", bonds);
        model.addAttribute("netAmountTotal", netAmountTotal);
        model.addAttribute("interest", interest);
        model.addAttribute("extra", extra);
        model.addAttribute("amountTotal", amountTotal);
        model.addAttribute("depositOut", depositOut);
        model.addAttribute("monthlyNet", monthlyNet);
        return "report/monthlyReport";
    }

    @RequestMapping(value = "/exportMonthlydetails", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> exportMonthlyDetails(@RequestParam(required = false) String startDate) {
        String date = Dates.isValid(startDate) ? startDate : Dates.defaultStartDate();
        byte[] pdf = pdfExport.monthlyDueList(date);
        String fileName = "MonthlDueList_" + date + "_.pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName).build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private double parse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (RuntimeException e) {
            return 0;
        }
    }
}

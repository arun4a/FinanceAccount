package com.laxmifinance.repository;

import com.laxmifinance.form.DailySheetForm;
import com.laxmifinance.form.InstallmentForm;
import com.laxmifinance.model.Installment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountsRepository {

    private final JdbcTemplate jdbc;

    public AccountsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public int insertDailySheet(DailySheetForm form, String createdBy) {
        int inserted = jdbc.update("""
                        INSERT INTO accounts(deposit_amount, deposit_by_partner, account_number, customer_name,
                            installment, deposit_name, created_by, date, time, customer_paid_amount, amount)
                        VALUES (?,?,?,?,?,?,?,?,now(),?,?)
                        """,
                amount(form.getDepositAmount()), blankToNull(form.getDepositBy()), form.getAccountNumber(),
                form.getCustomerName(), integer(form.getInstallment()), blankToNull(form.getDepositName()),
                createdBy, form.getDate(), amount(form.getPaidAmount()), amount(form.getActaulAmount()));
        if ("Yes".equalsIgnoreCase(form.getClose())) {
            updateCustomerStatus(form.getAccountNumber(), "close", form.getDate());
        }
        return inserted;
    }

    @Transactional
    public int updateInstallment(InstallmentForm form, String updatedBy) {
        int updated = jdbc.update("""
                        UPDATE accounts SET opening_balance=?, deposit_amount=?, deposit_by_partner=?,
                            account_number=?, customer_name=?, installment=?, deposit_name=?, created_by=?,
                            date=?, amount=?, customer_paid_amount=?
                        WHERE account_id=?
                        """,
                amount(form.getOpeningBalance()), amount(form.getDepositAmount()),
                blankToNull(form.getDepositByPartner()), form.getAccountNumber(), form.getCustomerName(),
                integer(form.getInstallment()), blankToNull(form.getDepositName()), updatedBy,
                form.getDate(), amount(form.getActualAmount()), amount(form.getPaidAmount()),
                form.getAccountId());
        if ("Yes".equalsIgnoreCase(form.getClose())) {
            updateCustomerStatus(form.getAccountNumber(), "close", form.getDate());
        } else {
            updateCustomerStatus(form.getAccountNumber(), "open", null);
        }
        return updated;
    }

    public int deleteInstallment(String accountId) {
        return jdbc.update("DELETE FROM accounts WHERE account_id = ?", accountId);
    }

    public int updateCustomerStatus(String accountNumber, String status, String closingDate) {
        return jdbc.update("UPDATE customer SET status = ?, closing_date = ? WHERE account_number = ?",
                status, closingDate, accountNumber);
    }

    public List<Installment> findInstallmentsByAccount(String accountNumber) {
        return jdbc.query("""
                        SELECT (ifnull(a.amount,0)-ifnull(a.customer_paid_amount,0)) AS dueAmount,
                               a.customer_paid_amount, a.amount, a.account_id, a.opening_balance,
                               a.deposit_amount, a.deposit_by_partner, a.customer_id, a.customer_name,
                               a.installment, a.account_number, a.deposit_name, a.created_by,
                               DATE_FORMAT(a.date,'%d-%b-%Y') AS date_display, a.time, c.status
                        FROM accounts a
                        LEFT JOIN customer c ON c.account_number = a.account_number
                        WHERE a.account_number = ?
                        ORDER BY a.date, a.account_id
                        """,
                (rs, rowNum) -> {
                    Installment installment = map(rs);
                    installment.setDate(rs.getString("date_display"));
                    installment.setStatus(rs.getString("status"));
                    return installment;
                }, accountNumber);
    }

    public List<Installment> findInstallmentsBetween(String startDate, String endDate) {
        return jdbc.query("""
                        SELECT (ifnull(a.amount,0)-ifnull(a.customer_paid_amount,0)) AS dueAmount,
                               a.customer_paid_amount, a.amount, a.account_id, a.opening_balance,
                               a.deposit_amount, a.deposit_by_partner, a.customer_id, a.customer_name,
                               a.installment, a.account_number, a.deposit_name, a.created_by,
                               DATE_FORMAT(a.date,'%d-%b-%Y') AS date_display, a.time
                        FROM accounts a
                        WHERE a.date BETWEEN ? AND ?
                        ORDER BY a.date, a.account_id
                        """,
                (rs, rowNum) -> {
                    Installment installment = map(rs);
                    installment.setDate(rs.getString("date_display"));
                    return installment;
                }, startDate, endDate);
    }

    public Installment findInstallmentById(String accountId) {
        List<Installment> rows = jdbc.query("""
                        SELECT (ifnull(a.amount,0)-ifnull(a.customer_paid_amount,0)) AS dueAmount,
                               a.customer_paid_amount, a.amount, a.account_id, a.opening_balance,
                               a.deposit_amount, a.deposit_by_partner, a.customer_id, a.customer_name,
                               a.installment, a.account_number, a.deposit_name, a.created_by, a.date, a.time,
                               c.status
                        FROM accounts a
                        LEFT JOIN customer c ON c.account_number = a.account_number
                        WHERE a.account_id = ?
                        """,
                (rs, rowNum) -> {
                    Installment installment = map(rs);
                    installment.setDate(rs.getString("date"));
                    installment.setStatus(rs.getString("status"));
                    return installment;
                }, accountId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public Map<String, String> collectedTotals() {
        Map<String, String> totals = new LinkedHashMap<>();
        totals.put("totalCollectedAmountDaily", decimal("""
                SELECT coalesce(sum(ifnull(customer_paid_amount,0)),0) AS amount
                FROM accounts WHERE date = curdate()
                """));
        totals.put("totalCollectedAmountMonth", decimal("""
                SELECT coalesce(sum(ifnull(customer_paid_amount,0)),0) AS amount
                FROM accounts WHERE month(date)=month(curdate()) AND year(date)=year(curdate())
                """));
        return totals;
    }

    public Map<String, String> issuedAndExtraTotals() {
        Map<String, String> totals = new LinkedHashMap<>();
        totals.put("totalIssuedAmountDaily", decimal("""
                SELECT coalesce(sum(original_amount),0) AS amount FROM customer WHERE date_of_issue = curdate()
                """));
        totals.put("totalIssuedAmountMonth", decimal("""
                SELECT coalesce(sum(original_amount),0) AS amount FROM customer
                WHERE month(date_of_issue)=month(curdate()) AND year(date_of_issue)=year(curdate())
                """));
        totals.put("TotalOpeningBalance", decimal("""
                SELECT ((SELECT coalesce(sum(ifnull(customer_paid_amount,0)),0) FROM accounts)
                    - (SELECT coalesce(sum(amount),0) FROM extra_transactions)
                    - (SELECT coalesce(sum(original_amount),0) FROM customer WHERE status='open')) AS amount
                """));
        totals.put("TotalDailyExtraTransaction", decimal("""
                SELECT coalesce(sum(amount),0) AS amount FROM extra_transactions WHERE crt_date = curdate()
                """));
        totals.put("TotalMonthlyExtraTransaction", decimal("""
                SELECT coalesce(sum(amount),0) AS amount FROM extra_transactions
                WHERE month(crt_date)=month(curdate()) AND year(crt_date)=year(curdate())
                """));
        return totals;
    }

    private Installment map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Installment installment = new Installment();
        installment.setDueAmount(rs.getString("dueAmount"));
        installment.setActualAmount(rs.getString("amount"));
        installment.setPaidAmount(rs.getString("customer_paid_amount"));
        installment.setAccountId(rs.getString("account_id"));
        installment.setOpeningBalance(rs.getString("opening_balance"));
        installment.setDepositAmount(rs.getString("deposit_amount"));
        installment.setDepositByPartner(rs.getString("deposit_by_partner"));
        installment.setCustomerId(rs.getString("customer_id"));
        installment.setCustomerName(rs.getString("customer_name"));
        installment.setInstallment(rs.getString("installment"));
        installment.setAccountNumber(rs.getString("account_number"));
        installment.setDepositName(rs.getString("deposit_name"));
        installment.setCreatedBy(rs.getString("created_by"));
        installment.setTime(rs.getString("time"));
        return installment;
    }

    private String decimal(String sql) {
        Double value = jdbc.queryForObject(sql, Double.class);
        return String.valueOf(value == null ? 0 : value);
    }

    private String amount(String value) {
        return value == null || value.isBlank() ? "0" : value.trim();
    }

    private Integer integer(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}

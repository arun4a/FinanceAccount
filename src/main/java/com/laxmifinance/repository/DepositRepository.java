package com.laxmifinance.repository;

import com.laxmifinance.form.DepositForm;
import com.laxmifinance.form.MonthlyPaymentForm;
import com.laxmifinance.model.Deposit;
import com.laxmifinance.model.MonthlyPayment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DepositRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public DepositRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    public int insertDeposit(DepositForm form) {
        return jdbc.update("""
                        INSERT INTO deposits(deposit_name, amount, deposit_account_no, created_date, time,
                            mobile_number, deposit_type)
                        VALUES (?,?,?,?,now(),?,?)
                        """,
                trim(form.getDepositNameSave()), amount(form.getAmountSave()),
                trim(form.getAccountNumberSave()), form.getOpendDateSave(),
                trim(form.getMobileNoSave()), trim(form.getDepositTypeSave()));
    }

    public boolean accountExists(String depositAccountNumber) {
        if (depositAccountNumber == null || depositAccountNumber.isBlank()) {
            return false;
        }
        Integer count = jdbc.queryForObject("SELECT count(*) FROM deposits WHERE deposit_account_no = ?",
                Integer.class, depositAccountNumber.trim());
        return count != null && count > 0;
    }

    public int updateDeposit(DepositForm form) {
        return jdbc.update("""
                        UPDATE deposits SET deposit_name=?, amount=?, deposit_account_no=?, created_date=?,
                            mobile_number=?, deposit_type=?, closing_date=?
                        WHERE deposit_id=?
                        """,
                trim(form.getDepositNameSave()), amount(form.getAmountSave()),
                trim(form.getAccountNumberSave()), form.getOpendDateSave(),
                trim(form.getMobileNoSave()), trim(form.getDepositTypeSave()),
                nullIfBlank(form.getCloseDateSave()), form.getDepositIdSave());
    }

    @Transactional
    public int deleteDeposit(String depositId) {
        List<String> accounts = jdbc.query("SELECT deposit_account_no FROM deposits WHERE deposit_id = ?",
                (rs, rowNum) -> rs.getString("deposit_account_no"), depositId);
        for (String account : accounts) {
            if (account != null && !account.isBlank()) {
                jdbc.update("DELETE FROM deposit_details WHERE deposit_account_no = ?", account);
            }
        }
        return jdbc.update("DELETE FROM deposits WHERE deposit_id = ?", depositId);
    }

    public Deposit findDepositById(String depositId) {
        List<Deposit> rows = jdbc.query("SELECT * FROM deposits WHERE deposit_id = ?",
                (rs, rowNum) -> {
                    Deposit deposit = new Deposit();
                    deposit.setDepositId(rs.getString("deposit_id"));
                    deposit.setDepositName(rs.getString("deposit_name"));
                    deposit.setAmount(rs.getString("amount"));
                    deposit.setAccountNumber(rs.getString("deposit_account_no"));
                    deposit.setOpenDate(rs.getString("created_date"));
                    deposit.setActualOpenDate(rs.getString("created_date"));
                    deposit.setCloseDate(rs.getString("closing_date"));
                    deposit.setActualCloseDate(rs.getString("closing_date"));
                    deposit.setMobileNo(rs.getString("mobile_number"));
                    deposit.setDepositType(rs.getString("deposit_type"));
                    deposit.setInterest(rs.getString("interest"));
                    return deposit;
                }, depositId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public List<Deposit> findDeposits(String accountNumber, String depositName) {
        StringBuilder sql = new StringBuilder("""
                SELECT ANY_VALUE(closing_date) AS closingDate,
                       ANY_VALUE(created_date) AS createdDate,
                       ANY_VALUE(deposit_id) AS deposit_id,
                       ANY_VALUE(deposit_name) AS deposit_name,
                       coalesce(sum(amount),0) AS amount,
                       ANY_VALUE(interest) AS interest,
                       mobile_number,
                       DATE_FORMAT(ANY_VALUE(created_date),'%d-%b-%Y') AS created_date_display,
                       ANY_VALUE(time) AS time,
                       ANY_VALUE(deposit_type) AS deposit_type,
                       DATE_FORMAT(ANY_VALUE(closing_date),'%d-%b-%Y') AS closing_date_display,
                       group_concat(deposit_account_no) AS deposit_account_no,
                       ANY_VALUE(closing_time) AS closing_time
                FROM deposits
                WHERE
                """);
        List<Object> args = new ArrayList<>();
        if (accountNumber == null || accountNumber.isBlank()) {
            sql.append(" deposit_account_no IS NOT NULL");
        } else {
            sql.append(" deposit_account_no = ?");
            args.add(accountNumber);
        }
        sql.append(" AND ");
        if (depositName == null || depositName.isBlank()) {
            sql.append(" deposit_name IS NOT NULL");
        } else {
            sql.append(" deposit_name LIKE ?");
            args.add("%" + depositName + "%");
        }
        sql.append(" GROUP BY mobile_number ORDER BY createdDate");
        List<Deposit> deposits = jdbc.query(sql.toString(), (rs, rowNum) -> {
            Deposit deposit = new Deposit();
            deposit.setDepositId(rs.getString("deposit_id"));
            deposit.setDepositName(rs.getString("deposit_name"));
            deposit.setAmount(rs.getString("amount"));
            deposit.setAccountNumber(rs.getString("deposit_account_no"));
            deposit.setMobileNo(rs.getString("mobile_number"));
            deposit.setOpenDate(rs.getString("created_date_display"));
            deposit.setActualOpenDate(rs.getString("createdDate"));
            deposit.setCloseDate(rs.getString("closing_date_display"));
            deposit.setActualCloseDate(rs.getString("closingDate"));
            deposit.setTime(rs.getString("time"));
            deposit.setDepositType(rs.getString("deposit_type"));
            deposit.setInterest(rs.getString("interest"));
            deposit.setClosingTime(rs.getString("closing_time"));
            return deposit;
        }, args.toArray());
        for (Deposit deposit : deposits) {
            fillPaidTotals(deposit);
        }
        return deposits;
    }

    private void fillPaidTotals(Deposit deposit) {
        List<String> accounts = splitAccounts(deposit.getAccountNumber());
        if (accounts.isEmpty()) {
            deposit.setPaidAmount("0");
            deposit.setPaidAmountFromActual("0");
            deposit.setDueAmount(deposit.getAmount());
            return;
        }
        MapSqlParameterSource params = new MapSqlParameterSource("accounts", accounts);
        Map<String, Object> sums = namedJdbc.queryForMap("""
                SELECT coalesce(sum(ifnull(amount,0)),0) AS paidAmount,
                       coalesce(sum(ifnull(paid_amount,0)),0) AS paidAmountFromActual
                FROM deposit_details
                WHERE deposit_account_no IN (:accounts)
                """, params);
        deposit.setPaidAmount(String.valueOf(sums.get("paidAmount")));
        deposit.setPaidAmountFromActual(String.valueOf(sums.get("paidAmountFromActual")));
        deposit.setDueAmount(String.valueOf(parse(deposit.getAmount()) - parse(deposit.getPaidAmountFromActual())));
    }

    public Map<String, String> totals() {
        Map<String, String> totals = new LinkedHashMap<>();
        double inAmount = decimal("SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposits WHERE deposit_type='In'");
        double outAmount = decimal("""
                SELECT coalesce((SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposits WHERE deposit_type='Out'),0)
                     + coalesce((SELECT coalesce(sum(ifnull(paid_amount,0)),0) FROM deposit_details
                          WHERE deposit_account_no IN (SELECT deposit_account_no FROM deposits WHERE deposit_type='In')),0)
                """);
        totals.put("TotalDepositInAmount", String.valueOf(inAmount));
        totals.put("TotalDepositOutAmount", String.valueOf(outAmount));
        totals.put("TotalDepositAmount", String.valueOf(inAmount + outAmount));
        totals.put("TotalMontlyPaidInterest", decimal2("""
                SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposit_details
                """));
        return totals;
    }

    public int insertMonthlyPayment(MonthlyPaymentForm form) {
        return jdbc.update("""
                        INSERT INTO deposit_details(crt_date, crt_time, deposit_account_no, amount, paid_amount)
                        VALUES (?, now(), ?, ?, ?)
                        """,
                form.getDate(), form.getDepositAccountNoUpdate(), amount(form.getPaidAmountUpdate()),
                amount(form.getPaidAmountFromActualUpdate()));
    }

    public int updateMonthlyPayment(MonthlyPaymentForm form) {
        return jdbc.update("""
                        UPDATE deposit_details SET crt_date=?, deposit_account_no=?, amount=?, paid_amount=?,
                            update_date=CURRENT_DATE
                        WHERE deposits_id=?
                        """,
                form.getDate(), form.getDepositAccountNoUpdate(), amount(form.getPaidAmountUpdate()),
                amount(form.getPaidAmountFromActualUpdate()), form.getDepositsIdUpdate());
    }

    public int deleteMonthlyPayment(String depositsId) {
        return jdbc.update("DELETE FROM deposit_details WHERE deposits_id = ?", depositsId);
    }

    public List<MonthlyPayment> findMonthlyPayments(String depositAccountNumber) {
        return jdbc.query("""
                        SELECT ifnull(paid_amount,0) AS paidAmountFromActual, deposits_id,
                               DATE_FORMAT(crt_date,'%d-%b-%Y') AS crt_date, crt_time,
                               deposit_account_no, amount, DATE_FORMAT(update_date,'%d-%b-%Y') AS update_date
                        FROM deposit_details WHERE deposit_account_no = ?
                        ORDER BY crt_date
                        """,
                (rs, rowNum) -> {
                    MonthlyPayment payment = new MonthlyPayment();
                    payment.setPaidAmountFromActual(rs.getString("paidAmountFromActual"));
                    payment.setCrtDate(rs.getString("crt_date"));
                    payment.setCrtTime(rs.getString("crt_time"));
                    payment.setDepositAccountNo(rs.getString("deposit_account_no"));
                    payment.setDepositsId(rs.getString("deposits_id"));
                    payment.setPaidAmount(rs.getString("amount"));
                    payment.setUpdateDate(rs.getString("update_date"));
                    return payment;
                }, depositAccountNumber);
    }

    public MonthlyPayment findMonthlyPaymentById(String depositsId) {
        List<MonthlyPayment> rows = jdbc.query("""
                        SELECT ifnull(paid_amount,0) AS paidAmountFromActual, crt_date AS crtDate, deposits_id,
                               DATE_FORMAT(crt_date,'%d-%b-%Y') AS crt_date, crt_time, deposit_account_no,
                               amount, DATE_FORMAT(update_date,'%d-%b-%Y') AS update_date
                        FROM deposit_details WHERE deposits_id = ?
                        """,
                (rs, rowNum) -> {
                    MonthlyPayment payment = new MonthlyPayment();
                    payment.setPaidAmountFromActual(rs.getString("paidAmountFromActual"));
                    payment.setActualCrtDate(rs.getString("crtDate"));
                    payment.setCrtDate(rs.getString("crt_date"));
                    payment.setCrtTime(rs.getString("crt_time"));
                    payment.setDepositAccountNo(rs.getString("deposit_account_no"));
                    payment.setDepositsId(rs.getString("deposits_id"));
                    payment.setPaidAmount(rs.getString("amount"));
                    payment.setUpdateDate(rs.getString("update_date"));
                    return payment;
                }, depositsId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public List<MonthlyPayment> monthlyInterestReport(String startDate, String endDate) {
        return jdbc.query("""
                        SELECT dd.deposits_id,
                               DATE_FORMAT(dd.crt_date,'%d-%b-%Y') AS crt_date,
                               dd.crt_time, dd.deposit_account_no, dd.amount,
                               DATE_FORMAT(dd.update_date,'%d-%b-%Y') AS update_date,
                               (SELECT d.deposit_name FROM deposits d
                                WHERE d.deposit_account_no = dd.deposit_account_no LIMIT 1) AS deposit_name
                        FROM deposit_details dd
                        WHERE dd.crt_date BETWEEN ? AND ?
                        ORDER BY dd.crt_date
                        """,
                (rs, rowNum) -> {
                    MonthlyPayment payment = new MonthlyPayment();
                    payment.setCrtDate(rs.getString("crt_date"));
                    payment.setCrtTime(rs.getString("crt_time"));
                    payment.setDepositAccountNo(rs.getString("deposit_account_no"));
                    payment.setDepositsId(rs.getString("deposits_id"));
                    payment.setPaidAmount(rs.getString("amount"));
                    payment.setUpdateDate(rs.getString("update_date"));
                    payment.setDepositName(rs.getString("deposit_name"));
                    return payment;
                }, startDate, endDate);
    }

    public List<Deposit> depositReport(String startDate, String endDate, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT deposit_id, deposit_name, amount, interest, mobile_number,
                       DATE_FORMAT(created_date,'%d-%b-%Y') AS created_date, time, deposit_type,
                       DATE_FORMAT(closing_date,'%d-%b-%Y') AS closing_date, deposit_account_no, closing_time
                FROM deposits
                WHERE created_date BETWEEN ? AND ?
                """);
        List<Object> args = new ArrayList<>();
        args.add(startDate);
        args.add(endDate);
        if (status != null && !status.isBlank()) {
            sql.append(" AND deposit_type = ?");
            args.add(status);
        }
        sql.append(" ORDER BY created_date");
        List<Deposit> deposits = jdbc.query(sql.toString(), (rs, rowNum) -> {
            Deposit deposit = new Deposit();
            deposit.setDepositId(rs.getString("deposit_id"));
            deposit.setDepositName(rs.getString("deposit_name"));
            deposit.setAmount(rs.getString("amount"));
            deposit.setAccountNumber(rs.getString("deposit_account_no"));
            deposit.setMobileNo(rs.getString("mobile_number"));
            deposit.setOpenDate(rs.getString("created_date"));
            deposit.setTime(rs.getString("time"));
            deposit.setCloseDate(rs.getString("closing_date"));
            deposit.setDepositType(rs.getString("deposit_type"));
            deposit.setInterest(rs.getString("interest"));
            return deposit;
        }, args.toArray());
        for (Deposit deposit : deposits) {
            Double paid = jdbc.queryForObject("""
                    SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposit_details WHERE deposit_account_no = ?
                    """, Double.class, deposit.getAccountNumber());
            deposit.setPaidAmount(String.valueOf(paid == null ? 0 : paid));
        }
        return deposits;
    }

    public Map<String, String> interestDetails(String accountNumber, String date) {
        Map<String, String> result = new LinkedHashMap<>();
        int days = 0;
        List<Integer> daysRows = jdbc.query("""
                        SELECT datediff(?, crt_date) AS days FROM deposit_details
                        WHERE deposit_account_no = ? ORDER BY crt_date DESC LIMIT 1
                        """,
                (rs, rowNum) -> rs.getInt("days"), date, accountNumber);
        if (!daysRows.isEmpty() && daysRows.get(0) != null) {
            days = daysRows.get(0);
        }
        List<Double> interestRows = jdbc.query("""
                        SELECT (((D.amount - coalesce(sum(ifnull(DD.paid_amount,0)),0))*2)/100) AS interest
                        FROM deposits D
                        LEFT JOIN deposit_details DD ON D.deposit_account_no = DD.deposit_account_no
                        WHERE D.deposit_account_no = ?
                        GROUP BY D.deposit_account_no, D.amount
                        """, (rs, rowNum) -> rs.getDouble("interest"), accountNumber);
        double value = interestRows.isEmpty() || interestRows.get(0) == null ? 0 : interestRows.get(0);
        if (days != 30 && days != 31) {
            value = Math.round((value / 30) * days);
        }
        result.put("Interest", String.valueOf(value));
        result.put("EnterAccountNumber", accountNumber);
        result.put("InterestForDays", "for " + days + " days  ");
        result.put("EnterPaymentDate", date);
        return result;
    }

    private List<String> splitAccounts(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .toList();
    }

    private double decimal(String sql) {
        Double value = jdbc.queryForObject(sql, Double.class);
        return value == null ? 0 : value;
    }

    private String decimal2(String sql) {
        Double value = jdbc.queryForObject(sql, Double.class);
        return String.valueOf(value == null ? 0 : value);
    }

    private double parse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private String amount(String value) {
        return value == null || value.isBlank() ? "0" : value.trim();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String nullIfBlank(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

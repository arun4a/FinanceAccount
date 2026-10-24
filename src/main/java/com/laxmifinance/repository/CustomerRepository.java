package com.laxmifinance.repository;

import com.laxmifinance.form.BondForm;
import com.laxmifinance.model.Customer;
import com.laxmifinance.model.MobileRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerRepository {

    private static final String BASE_SELECT = """
            SELECT concat(monthname(closing_date),'-',year(closing_date)) AS closeMonthName,
                   concat(monthname(date_of_issue),'-',year(date_of_issue)) AS monthName,
                   customer_id, customer_name, customer_fatherName, surety_name,
                   cus_permanent_address, surety_address, atm_submitted,
                   cus_mobile_number, surety_mobile_number, cheque, passbook, amount,
                   DATE_FORMAT(date_of_issue,'%d-%b-%Y') AS dateofissue,
                   issued_by, surety_occupation, customer_occupation, surety_fname, installments,
                   original_amount, status, account_number, acountNo, closing_date AS d,
                   DATE_FORMAT(closing_date,'%d-%b-%Y') AS closingDate,
                   date_of_issue AS dateOfIssueRaw, closing_date AS closingDateRaw,
                   sur_rel, cust_rel, cust_second_mobno, surety_second_mobno
            FROM customer
            """;

    private final JdbcTemplate jdbc;

    public CustomerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Customer> findBonds(String customerName, String accountNumber, String status, String mobileNumber) {
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> args = new ArrayList<>();
        if ("Any".equals(status)) {
            sql.append(" WHERE account_number = ?");
            args.add(accountNumber);
        } else {
            sql.append(" WHERE status = ?");
            args.add(status);
            if (isFilter(customerName)) {
                sql.append(" AND customer_name LIKE ?");
                args.add("%" + customerName + "%");
            }
            if (isFilter(accountNumber)) {
                sql.append(" AND account_number = ?");
                args.add(accountNumber);
            }
            if (isFilter(mobileNumber)) {
                sql.append(" AND cus_mobile_number = ?");
                args.add(mobileNumber);
            }
        }
        sql.append("close".equals(status) ? " ORDER BY d" : " ORDER BY date_of_issue");
        return jdbc.query(sql.toString(), (rs, rowNum) -> {
            Customer customer = mapBase(rs);
            fillPaidTotals(customer);
            customer.setDueAmount(parse(customer.getAmount()) - customer.getPaidAmount());
            return customer;
        }, args.toArray());
    }

    public List<Customer> findIssuedBondsReport(String startDate, String endDate, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT customer_id, customer_name, customer_fatherName, surety_name,
                       cus_permanent_address, surety_address, atm_submitted,
                       cus_mobile_number, surety_mobile_number, cheque, passbook, amount,
                       DATE_FORMAT(date_of_issue,'%d-%b-%Y') AS date_of_issue,
                       issued_by, surety_occupation, customer_occupation, surety_fname, installments,
                       original_amount, status, account_number, acountNo,
                       DATE_FORMAT(closing_date,'%d-%b-%Y') AS closingDate,
                       sur_rel, cust_rel, cust_second_mobno, surety_second_mobno
                FROM customer
                """);
        List<Object> args = new ArrayList<>();
        if ("close".equals(status)) {
            sql.append(" WHERE date(closing_date) BETWEEN ? AND ?");
        } else {
            sql.append(" WHERE date_of_issue BETWEEN ? AND ?");
        }
        args.add(startDate);
        args.add(endDate);
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ?");
            args.add(status);
        }
        sql.append(" ORDER BY date_of_issue");
        return jdbc.query(sql.toString(), (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setCustomerName(rs.getString("customer_name"));
            customer.setCustomerFatherName(rs.getString("customer_fatherName"));
            customer.setSuretyName(rs.getString("surety_name"));
            customer.setCustomerAddress(rs.getString("cus_permanent_address"));
            customer.setSuretyAddress(rs.getString("surety_address"));
            customer.setAtm(rs.getString("atm_submitted"));
            customer.setCustomerMobileNo(rs.getString("cus_mobile_number"));
            customer.setSuretyMobileNo(rs.getString("surety_mobile_number"));
            customer.setCheque(rs.getString("cheque"));
            customer.setPassbook(rs.getString("passbook"));
            customer.setAmount(rs.getString("amount"));
            customer.setDateOfIssue(rs.getString("date_of_issue"));
            customer.setIssuedBy(rs.getString("issued_by"));
            customer.setSuretyOccupation(rs.getString("surety_occupation"));
            customer.setCustomerOccupation(rs.getString("customer_occupation"));
            customer.setSuretyFname(rs.getString("surety_fname"));
            customer.setInstallments(rs.getString("installments"));
            customer.setOriginalAmount(rs.getString("original_amount"));
            customer.setStatus(rs.getString("status"));
            customer.setAccountNumber(rs.getString("account_number"));
            customer.setAccountNo(rs.getString("acountNo"));
            customer.setClosingDate(rs.getString("closingDate"));
            customer.setSurRel(rs.getString("sur_rel"));
            customer.setCustRel(rs.getString("cust_rel"));
            customer.setCustomerSecMobileNo(rs.getString("cust_second_mobno"));
            customer.setSuretySecMobileNo(rs.getString("surety_second_mobno"));
            fillPaidTotals(customer);
            customer.setDueAmount(parse(customer.getAmount()) - customer.getPaidAmount());
            return customer;
        }, args.toArray());
    }

    public Customer findByAccountNumber(String accountNumber) {
        List<Customer> rows = jdbc.query(BASE_SELECT + " WHERE account_number = ? LIMIT 1",
                (rs, rowNum) -> mapBase(rs), accountNumber);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public Customer findById(String customerId) {
        List<Customer> rows = jdbc.query("SELECT * FROM customer WHERE customer_id = ?",
                (rs, rowNum) -> {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setCustomerFatherName(rs.getString("customer_fatherName"));
                    customer.setCustomerMobileNo(rs.getString("cus_mobile_number"));
                    customer.setCustomerAddress(rs.getString("cus_permanent_address"));
                    customer.setCustomerOccupation(rs.getString("customer_occupation"));
                    customer.setSuretyName(rs.getString("surety_name"));
                    customer.setSuretyAddress(rs.getString("surety_address"));
                    customer.setSuretyMobileNo(rs.getString("surety_mobile_number"));
                    customer.setSuretyOccupation(rs.getString("surety_occupation"));
                    customer.setSuretyFname(rs.getString("surety_fname"));
                    customer.setCheque(rs.getString("cheque"));
                    customer.setPassbook(rs.getString("passbook"));
                    customer.setAtm(rs.getString("atm_submitted"));
                    customer.setAmount(rs.getString("amount"));
                    customer.setDateOfIssue(rs.getString("date_of_issue"));
                    customer.setDateOfIssueRaw(rs.getString("date_of_issue"));
                    customer.setIssuedBy(rs.getString("issued_by"));
                    customer.setInstallments(rs.getString("installments"));
                    customer.setOriginalAmount(rs.getString("original_amount"));
                    customer.setAccountNumber(rs.getString("account_number"));
                    customer.setAccountNo(rs.getString("acountNo"));
                    customer.setSurRel(rs.getString("sur_rel"));
                    customer.setCustRel(rs.getString("cust_rel"));
                    customer.setCustomerSecMobileNo(rs.getString("cust_second_mobno"));
                    customer.setSuretySecMobileNo(rs.getString("surety_second_mobno"));
                    customer.setStatus(rs.getString("status"));
                    customer.setDateOfIssueRaw(rs.getString("date_of_issue"));
                    customer.setClosingDateRaw(rs.getString("closing_date"));
                    return customer;
                }, customerId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    public Map<String, String> findCustomerNameByAccount(String accountNumber) {
        Map<String, String> result = new LinkedHashMap<>();
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT customer_name FROM customer WHERE account_number = ?", accountNumber);
        if (!rows.isEmpty()) {
            result.put("getDetailsCustomerName", String.valueOf(rows.get(0).get("customer_name")));
            result.put("getDetailsAccountNumber", accountNumber);
        }
        return result;
    }

    public int insertBond(BondForm form, String createdBy) {
        return jdbc.update("""
                        INSERT INTO customer(customer_name, customer_fatherName, cus_mobile_number,
                            cus_permanent_address, customer_occupation, surety_name, surety_address,
                            surety_mobile_number, surety_occupation, surety_fname, cheque, passbook,
                            atm_submitted, amount, date_of_issue, issued_by, installments,
                            original_amount, account_number, acountNo, sur_rel, cust_rel)
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                        """,
                form.getCustomerNameN(), form.getFatherName(), form.getMobileNumber(),
                form.getAddress(), form.getOccupation(), form.getSuretyName(), form.getSuretyAddress(),
                form.getSuretyMobileNumber(), form.getSuretyOccupation(), form.getSuretyFatherName(),
                form.getCheque(), form.getPassBook(), form.getAtm(), amount(form.getAmountNew()),
                form.getIssuedDate(), createdBy, integer(form.getInstallmentsN()),
                amount(form.getOriginalAmount()), form.getAccountNumberNew(), form.getAccountNo(),
                form.getCustRel(), form.getSureRel());
    }

    public int updateBond(BondForm form, String updatedBy) {
        return jdbc.update("""
                        UPDATE customer SET customer_name=?, customer_fatherName=?, cus_mobile_number=?,
                            cus_permanent_address=?, customer_occupation=?, surety_name=?, surety_address=?,
                            surety_mobile_number=?, surety_occupation=?, surety_fname=?, cheque=?, passbook=?,
                            atm_submitted=?, amount=?, date_of_issue=?, issued_by=?, installments=?,
                            original_amount=?, account_number=?, acountNo=?, sur_rel=?, cust_rel=?,
                            cust_second_mobno=?, surety_second_mobno=?
                        WHERE customer_id=?
                        """,
                form.getCustomerNameN(), form.getFatherName(), form.getMobileNumber(),
                form.getAddress(), form.getOccupation(), form.getSuretyName(), form.getSuretyAddress(),
                form.getSuretyMobileNumber(), form.getSuretyOccupation(), form.getSuretyFatherName(),
                form.getCheque(), form.getPassBook(), form.getAtm(), amount(form.getAmountNew()),
                form.getIssuedDate(), updatedBy, integer(form.getInstallmentsN()),
                amount(form.getOriginalAmount()), form.getAccountNumberNew(), form.getAccountNo(),
                form.getCustRel(), form.getSureRel(), form.getCustomerSecMobile(),
                form.getSuretySecMobile(), form.getCustomerId());
    }

    public int deleteBond(String customerId) {
        return jdbc.update("DELETE FROM customer WHERE customer_id = ?", customerId);
    }

    public List<MobileRow> findMobileRows(String customerName, String accountNumber) {
        StringBuilder sql = new StringBuilder("""
                SELECT customer_id, account_number, customer_name, surety_name,
                       cus_mobile_number, surety_mobile_number, cust_second_mobno, surety_second_mobno
                FROM customer WHERE status IS NOT NULL
                """);
        List<Object> args = new ArrayList<>();
        if (isFilter(customerName)) {
            sql.append(" AND customer_name = ?");
            args.add(customerName);
        }
        if (isFilter(accountNumber)) {
            sql.append(" AND account_number = ?");
            args.add(accountNumber);
        }
        sql.append(" ORDER BY customer_name");
        List<MobileRow> result = new ArrayList<>();
        jdbc.query(sql.toString(), rs -> {
            MobileRow primary = new MobileRow();
            primary.setCustomerId(rs.getString("customer_id"));
            primary.setAccountNo(rs.getString("account_number"));
            primary.setCustomerName(rs.getString("customer_name"));
            primary.setSuretyName(rs.getString("surety_name"));
            primary.setCustomerMobileNo(rs.getString("cus_mobile_number"));
            primary.setSuretyMobileNo(rs.getString("surety_mobile_number"));
            result.add(primary);
            String custSecond = rs.getString("cust_second_mobno");
            String suretySecond = rs.getString("surety_second_mobno");
            if (custSecond != null || suretySecond != null) {
                MobileRow secondary = new MobileRow();
                secondary.setCustomerId(rs.getString("customer_id"));
                secondary.setAccountNo(rs.getString("account_number"));
                secondary.setCustomerName(rs.getString("customer_name"));
                secondary.setSuretyName(rs.getString("surety_name"));
                secondary.setCustomerMobileNo(custSecond);
                secondary.setSuretyMobileNo(suretySecond);
                result.add(secondary);
            }
        }, args.toArray());
        return result;
    }

    public int updateSecondMobiles(String customerId, String customerMobile, String suretyMobile) {
        int updated = 0;
        if (customerMobile != null && !customerMobile.isBlank()) {
            updated += jdbc.update("UPDATE customer SET cust_second_mobno = ? WHERE customer_id = ?",
                    customerMobile.trim(), customerId);
        }
        if (suretyMobile != null && !suretyMobile.isBlank()) {
            updated += jdbc.update("UPDATE customer SET surety_second_mobno = ? WHERE customer_id = ?",
                    suretyMobile.trim(), customerId);
        }
        return updated;
    }

    private void fillPaidTotals(Customer customer) {
        Map<String, Object> totals = jdbc.queryForMap("""
                SELECT coalesce(sum(ifnull(customer_paid_amount,0)),0) AS paidAmount,
                       count(installment) AS completedInstallments
                FROM accounts WHERE account_number = ?
                """, customer.getAccountNumber());
        customer.setPaidAmount(((Number) totals.get("paidAmount")).doubleValue());
        customer.setCompletedInstallments(((Number) totals.get("completedInstallments")).intValue());
    }

    private Customer mapBase(java.sql.ResultSet rs) throws java.sql.SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setCustomerName(rs.getString("customer_name"));
        customer.setCustomerFatherName(rs.getString("customer_fatherName"));
        customer.setSuretyName(rs.getString("surety_name"));
        customer.setCustomerAddress(rs.getString("cus_permanent_address"));
        customer.setSuretyAddress(rs.getString("surety_address"));
        customer.setAtm(rs.getString("atm_submitted"));
        customer.setCustomerMobileNo(rs.getString("cus_mobile_number"));
        customer.setSuretyMobileNo(rs.getString("surety_mobile_number"));
        customer.setCheque(rs.getString("cheque"));
        customer.setPassbook(rs.getString("passbook"));
        customer.setAmount(rs.getString("amount"));
        customer.setDateOfIssue(rs.getString("dateofissue"));
        customer.setIssuedBy(rs.getString("issued_by"));
        customer.setSuretyOccupation(rs.getString("surety_occupation"));
        customer.setCustomerOccupation(rs.getString("customer_occupation"));
        customer.setSuretyFname(rs.getString("surety_fname"));
        customer.setInstallments(rs.getString("installments"));
        customer.setOriginalAmount(rs.getString("original_amount"));
        customer.setStatus(rs.getString("status"));
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setAccountNo(rs.getString("acountNo"));
        customer.setClosingDate(rs.getString("closingDate"));
        customer.setDateOfIssueRaw(rs.getString("dateOfIssueRaw"));
        customer.setClosingDateRaw(rs.getString("closingDateRaw"));
        customer.setMonthName(rs.getString("monthName"));
        customer.setClosingMonthName(rs.getString("closeMonthName"));
        customer.setSurRel(rs.getString("sur_rel"));
        customer.setCustRel(rs.getString("cust_rel"));
        customer.setCustomerSecMobileNo(rs.getString("cust_second_mobno"));
        customer.setSuretySecMobileNo(rs.getString("surety_second_mobno"));
        customer.setDueAmount(parse(customer.getAmount()) - customer.getPaidAmount());
        return customer;
    }

    private boolean isFilter(String value) {
        return value != null && !value.isBlank() && !"0".equals(value);
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

    private Integer integer(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}

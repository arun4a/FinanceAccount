package com.laxmifinance.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NetAmountRepository {

    private final JdbcTemplate jdbc;

    public NetAmountRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, String> monthlyReportDetails(String startDate) {
        Map<String, String> report = new LinkedHashMap<>();
        double totalCollections = decimal("""
                SELECT coalesce(sum(customer_paid_amount),0) FROM accounts
                WHERE monthname(date) = monthname(?) AND year(date) = year(?)
                """, startDate, startDate);
        double depositIn = decimal("""
                SELECT coalesce(sum(amount),0) FROM deposits
                WHERE deposit_type = 'In' AND monthname(created_date) = monthname(?) AND year(created_date) = year(?)
                """, startDate, startDate);
        Double lastMonthNet = nullableDecimal("""
                SELECT net_amount FROM net_amount_monthly
                WHERE month(date) = month(DATE_SUB(?, INTERVAL 1 MONTH))
                  AND year(date) = year(DATE_SUB(?, INTERVAL 1 MONTH))
                """, startDate, startDate);
        double bonds = decimal("""
                SELECT coalesce(sum(original_amount),0) FROM customer
                WHERE monthname(date_of_issue) = monthname(?) AND year(date_of_issue) = year(?)
                """, startDate, startDate);
        double extra = decimal("""
                SELECT coalesce(sum(ifnull(amount,0)),0) FROM extra_transactions
                WHERE monthname(crt_date) = monthname(?) AND year(crt_date) = year(?)
                """, startDate, startDate);
        double interest = decimal("""
                SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposit_details
                WHERE monthname(crt_date) = monthname(?) AND year(crt_date) = year(?)
                """, startDate, startDate);
        double depositOut = decimal("""
                SELECT coalesce((SELECT coalesce(sum(ifnull(amount,0)),0) FROM deposits
                                 WHERE deposit_type='Out' AND monthname(closing_date)=monthname(?)
                                   AND year(closing_date)=year(?)),0)
                     + coalesce((SELECT coalesce(sum(ifnull(paid_amount,0)),0) FROM deposit_details
                                 WHERE monthname(crt_date)=monthname(?) AND year(crt_date)=year(?)
                                   AND deposit_account_no IN (SELECT deposit_account_no FROM deposits WHERE deposit_type='In')),0)
                """, startDate, startDate, startDate, startDate);

        report.put("TotalCollections", String.valueOf(totalCollections));
        report.put("TotalDepositsInAmount", String.valueOf(depositIn));
        report.put("MonthlyInAmountTotal", String.valueOf(totalCollections + depositIn));
        report.put("LasMonthNetAmount", String.valueOf(lastMonthNet == null ? 0 : lastMonthNet));
        report.put("TotalBondsAmount", String.valueOf(bonds));
        report.put("ExtraAmount", String.valueOf(extra));
        report.put("InterestAmount", String.valueOf(interest));
        report.put("TotalDepositsOutAmount", String.valueOf(depositOut));
        double netAmountTotal = totalCollections + depositIn - bonds;
        double amountTotal = netAmountTotal - interest - extra;
        report.put("TotalmonthlyAmount", String.valueOf(amountTotal - depositOut));
        report.put("TotalMontlyCloseAmount", "0");
        return report;
    }

    public double netAmountForDate(String date) {
        Map<String, String> report = monthlyReportDetails(date);
        double collections = parse(report.get("TotalCollections"));
        double depositsIn = parse(report.get("TotalDepositsInAmount"));
        double bonds = parse(report.get("TotalBondsAmount"));
        double interest = parse(report.get("InterestAmount"));
        double extra = parse(report.get("ExtraAmount"));
        double depositsOut = parse(report.get("TotalDepositsOutAmount"));
        double previous = parse(report.get("LasMonthNetAmount"));
        return collections + depositsIn - bonds - interest - extra - depositsOut + previous;
    }

    public List<String> findAllDates() {
        return jdbc.query("SELECT date FROM net_amount_monthly", (rs, rowNum) -> rs.getString("date"));
    }

    public void upsert(String date, double netAmount) {
        int updated = jdbc.update("""
                UPDATE net_amount_monthly SET date = ?, net_amount = ?
                WHERE month(date) = month(?) AND year(date) = year(?)
                """, date, netAmount, date, date);
        if (updated == 0) {
            jdbc.update("INSERT INTO net_amount_monthly(date, net_amount) VALUES (?,?)", date, netAmount);
        }
    }

    private double decimal(String sql, Object... args) {
        Double value = jdbc.queryForObject(sql, Double.class, args);
        return value == null ? 0 : value;
    }

    private Double nullableDecimal(String sql, Object... args) {
        List<Double> values = jdbc.query(sql, (rs, rowNum) -> rs.getDouble("net_amount"), args);
        return values.isEmpty() ? null : values.get(0);
    }

    private double parse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }
}

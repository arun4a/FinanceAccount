package com.laxmifinance.repository;

import com.laxmifinance.form.ExtraForm;
import com.laxmifinance.model.ExtraTransaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExtraTransactionRepository {

    private final JdbcTemplate jdbc;

    public ExtraTransactionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ExtraTransaction> findByDate(String date) {
        if (date == null || date.isBlank()) {
            return jdbc.query("SELECT * FROM extra_transactions WHERE crt_date IS NOT NULL ORDER BY crt_date DESC",
                    (rs, rowNum) -> map(rs));
        }
        return jdbc.query("SELECT * FROM extra_transactions WHERE crt_date = ? ORDER BY transaction_id DESC",
                (rs, rowNum) -> map(rs), date);
    }

    public List<ExtraTransaction> findBetween(String startDate, String endDate) {
        return jdbc.query("""
                        SELECT * FROM extra_transactions WHERE crt_date BETWEEN ? AND ?
                        ORDER BY crt_date
                        """,
                (rs, rowNum) -> map(rs), startDate, endDate);
    }

    public String totalForDate(String date) {
        Double total;
        if (date == null || date.isBlank()) {
            total = jdbc.queryForObject("SELECT coalesce(sum(amount),0) FROM extra_transactions", Double.class);
        } else {
            total = jdbc.queryForObject("SELECT coalesce(sum(amount),0) FROM extra_transactions WHERE crt_date = ?",
                    Double.class, date);
        }
        return String.valueOf(total == null ? 0 : total);
    }

    public int insert(ExtraForm form) {
        return jdbc.update("INSERT INTO extra_transactions(amount, crt_date, purpose, crt_time) VALUES (?,?,?,now())",
                amount(form.getExtraAmountSave()), form.getExtraDateSave(), form.getExtraPurposeSave());
    }

    public int update(ExtraForm form) {
        return jdbc.update("UPDATE extra_transactions SET amount=?, crt_date=?, purpose=? WHERE transaction_id=?",
                amount(form.getExtraAmountSave()), form.getExtraDateSave(), form.getExtraPurposeSave(),
                form.getExtraIdSave());
    }

    public int delete(String extraId) {
        return jdbc.update("DELETE FROM extra_transactions WHERE transaction_id = ?", extraId);
    }

    private ExtraTransaction map(java.sql.ResultSet rs) throws java.sql.SQLException {
        ExtraTransaction transaction = new ExtraTransaction();
        transaction.setExtraId(rs.getString("transaction_id"));
        transaction.setExtraAmount(rs.getString("amount"));
        transaction.setExtraDate(rs.getString("crt_date"));
        transaction.setExtraPurpose(rs.getString("purpose"));
        return transaction;
    }

    private String amount(String value) {
        return value == null || value.isBlank() ? "0" : value.trim();
    }
}

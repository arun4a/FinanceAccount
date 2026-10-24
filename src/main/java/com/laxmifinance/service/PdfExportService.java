package com.laxmifinance.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {

    private static final float[] COLUMN_WIDTHS = {3, 4, 8, 8, 5, 3, 3};

    private final JdbcTemplate jdbc;

    public PdfExportService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public byte[] monthlyDueList(String reportDate) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(titleTable(reportDate));
            document.add(dueTable(reportDate));
            document.close();
            return output.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Unable to generate monthly due PDF", e);
        }
    }

    private PdfPTable titleTable(String reportDate) throws DocumentException {
        PdfPTable table = new PdfPTable(COLUMN_WIDTHS);
        Font titleFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLDITALIC, Color.BLUE);
        PdfPCell title = new PdfPCell(new Phrase(reportDate, titleFont));
        title.setColspan(7);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setPadding(12.0f);
        table.addCell(title);

        Font headerFont = new Font(Font.TIMES_ROMAN, 7, Font.NORMAL);
        String[] headers = {"S.No", "A/C No", "Customer Name", "Surety Name", "Installment", "Due", "Interest"};
        for (String header : headers) {
            table.addCell(cell(header, headerFont));
        }
        return table;
    }

    private PdfPTable dueTable(String reportDate) throws DocumentException {
        PdfPTable table = new PdfPTable(COLUMN_WIDTHS);
        Font normalFont = new Font(Font.TIMES_ROMAN, 7, Font.NORMAL);
        Font monthFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLDITALIC, Color.DARK_GRAY);

        List<CustomerDue> rows = jdbc.query("""
                        SELECT c.customer_name, c.surety_name, c.account_number, c.installments,
                               concat(monthname(c.date_of_issue),'-',year(c.date_of_issue)) AS monthName,
                               coalesce((SELECT coalesce(sum(a.amount),0) FROM accounts a
                                         WHERE a.account_number = c.account_number
                                           AND month(a.date) = month(?) AND year(a.date) = year(?)),0) AS accountAmount
                        FROM customer c
                        WHERE c.status = 'open'
                        ORDER BY c.date_of_issue
                        """,
                (rs, rowNum) -> new CustomerDue(
                        rs.getString("monthName"),
                        rs.getString("customer_name"),
                        rs.getString("surety_name"),
                        rs.getString("account_number"),
                        rs.getInt("installments"),
                        rs.getInt("accountAmount")),
                reportDate, reportDate);

        int recordCount = 1;
        int totalInstallments = 0;
        int groups = 0;
        String monthName = "";
        for (CustomerDue row : rows) {
            int difference = row.accountAmount - row.installments;
            boolean hasDue = row.accountAmount != 0 && difference < 0;
            int due = hasDue ? Math.abs(difference) : 0;
            int interest = hasDue ? Math.abs((difference / 100) * 5) : 0;

            if (monthName.isEmpty() || !monthName.equals(row.monthName)) {
                if (groups > 0) {
                    addSubtotal(table, totalInstallments, normalFont);
                    totalInstallments = 0;
                }
                groups++;
                recordCount = 1;
                totalInstallments += row.installments;
                PdfPCell month = new PdfPCell(new Phrase(row.monthName, monthFont));
                month.setColspan(7);
                month.setHorizontalAlignment(Element.ALIGN_CENTER);
                month.setPadding(12.0f);
                table.addCell(month);
                addRow(table, recordCount, row, due, interest, hasDue, normalFont);
                recordCount++;
            } else {
                totalInstallments += row.installments;
                addRow(table, recordCount, row, due, interest, hasDue, normalFont);
                recordCount++;
            }
            monthName = row.monthName;
        }
        addSubtotal(table, totalInstallments, normalFont);
        return table;
    }

    private void addRow(PdfPTable table, int recordCount, CustomerDue row, int due, int interest,
                        boolean hasDue, Font font) {
        table.addCell(cell(String.valueOf(recordCount), font));
        table.addCell(cell(row.accountNumber, font));
        table.addCell(cell(row.customerName, font));
        table.addCell(cell(row.suretyName, font));
        table.addCell(cell(String.valueOf(row.installments), font));
        table.addCell(cell(hasDue ? String.valueOf(due) : "", font));
        table.addCell(cell(hasDue ? String.valueOf(interest) : "", font));
    }

    private void addSubtotal(PdfPTable table, int totalInstallments, Font font) {
        table.addCell(cell("", font));
        table.addCell(cell("", font));
        table.addCell(cell("", font));
        table.addCell(cell("", font));
        table.addCell(cell(String.valueOf(totalInstallments), font));
        table.addCell(cell("", font));
        table.addCell(cell("", font));
    }

    private PdfPCell cell(String value, Font font) {
        return new PdfPCell(new Phrase(value == null ? "" : value, font));
    }

    private record CustomerDue(String monthName, String customerName, String suretyName,
                               String accountNumber, int installments, int accountAmount) {
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.Accounts;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import static com.itextpdf.text.html.HtmlTags.FONT;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.sun.DBOperations.DBAccessMain;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Arun
 */
public class ExportDao {

    public PdfPCell getNormalCell(String string, String language, float size)
            throws DocumentException, IOException {
        if (string != null && "".equals(string)) {
            return new PdfPCell();
        }
        Font f = getFontForThisLanguage(language);
        if (size < 0) {
            f.setColor(BaseColor.RED);
            size = -size;
        }
        f.setSize(size);
        PdfPCell cell = new PdfPCell(new Phrase(string, f));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    public Font getFontForThisLanguage(String language) {
        if ("czech".equals(language)) {
            return FontFactory.getFont(FONT, "Cp1250", true);
        }
        if ("greek".equals(language)) {
       
            return FontFactory.getFont(FONT, "Cp1253", true);
        }
        return FontFactory.getFont(FONT, null, true);
    }

    public PdfPTable generateTable(PdfPTable table, String isNewMonth, String header, String isTotal, String sNo, String acNo, String custname, String suretyName, String installment, String due, String interest) {

        Font f = null;
        PdfPCell cell = null;
        try {

            if (isNewMonth.equals("Y")) {
                f = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLDITALIC, GrayColor.GRAYBLACK);
                cell = new PdfPCell(new Phrase(header, f));
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(12.0f);
//                cell.setBackgroundColor(new BaseColor(0, 0, 0));
                table.addCell(cell);
            }

//            table.setWidthPercentage(100);
            if (isTotal.equals("-0")) {
                table.addCell(getNormalCell(sNo, "czech", 7));
                table.addCell(getNormalCell(acNo, "czech", 7));
                table.addCell(getNormalCell(custname, "greek", 7));
                table.addCell(getNormalCell(suretyName, "czech", 7));
                table.addCell(getNormalCell(installment, "greek", 7));
                table.addCell(getNormalCell(due, "czech", 7));
                table.addCell(getNormalCell(interest, "czech", 7));
            } else {
                table.addCell(getNormalCell(sNo, "czech", 7));
                table.addCell(getNormalCell(acNo, "czech", 7));
                table.addCell(getNormalCell(custname, "greek", 7));
                table.addCell(getNormalCell(suretyName, "czech", 7));
                table.addCell(getNormalCell(isTotal, "greek", 7));
                table.addCell(getNormalCell(due, "czech", 7));
                table.addCell(getNormalCell(interest, "czech", 7));
            }

        } catch (Exception e) {
        }
        return table;
    }

    public void generatePdf(Document document, String reportDate, Connection con) {
        PreparedStatement pstmt = null, ps = null;
        ResultSet res = null, rs = null;
        int recordCount = 1, totalAmount = 0;
        String monthName = "";
        int i = 0;
        try {
            float[] columnWidths = {3, 4, 8, 8, 5, 3, 3};
            PdfPTable table = new PdfPTable(columnWidths);
            pstmt = con.prepareStatement("  SELECT  status,concat(monthname(closing_date),'-',year(closing_date))  as closeMonthName,concat(monthname(date_of_issue),'-',year(date_of_issue))  as monthName,\n"
                    + " customer_id, customer_name,  surety_name, \n"
                    + "    cus_mobile_number,    amount, DATE_FORMAT(date_of_issue,'%d-%b-%Y') as  dateofissue ,\n"
                    + "  installments,  account_number, closing_date as d,DATE_FORMAT(closing_date,'%d-%b-%Y') as  closing_date\n"
                    + " FROM  customer  Where  status='open' order by date_of_issue  ");

            res = pstmt.executeQuery();
            while (res.next()) {
                String due = "";
                String interest = "";
                if (monthName.equals("") || !monthName.equals(res.getString("monthName"))) {
                    if (i > 0) {
                        generateTable(table, "N", res.getString("monthName"), totalAmount + "", "", "", "", "", "", "", "");
                        totalAmount = 0;
                    }

                    i++;
                    recordCount = 1;
                    totalAmount += res.getInt("installments");
                    ps = con.prepareStatement("SELECT  coalesce(sum(amount),0) as amount  FROM accounts where account_number=?  and month(date)=month(?) and year(date)=year(?) order by date desc ");
                    ps.setString(1, res.getString("account_number"));
                    ps.setString(2, reportDate);
                    ps.setString(3, reportDate);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        if (rs.getInt("amount") != 0) {
                            if ((rs.getInt("amount") - res.getInt("installments")) < 0) {
                                due = String.valueOf((rs.getInt("amount") - res.getInt("installments"))).replace("-", "");
                                interest = (((rs.getInt("amount") - res.getInt("installments")) / 100) * 5) + "";

                            }
                        }
                    }
                    generateTable(table, "Y", res.getString("monthName"), "-0", recordCount + "", res.getString("account_number") + "",
                            res.getString("customer_name") + "", res.getString("surety_name") + "", res.getString("installments") + "",
                            due + "", interest.replace("-", "") + "");

                    recordCount++;

                } else {
                    totalAmount += res.getInt("installments");

                    ps = con.prepareStatement("SELECT  coalesce(sum(amount),0) as amount  FROM accounts where account_number=?  and month(date)=month(?) and year(date)=year(?) order by date desc ");
                    ps.setString(1, res.getString("account_number"));
                    ps.setString(2, reportDate);
                    ps.setString(3, reportDate);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("amount") != 0) {
                            if ((rs.getInt("amount") - res.getInt("installments")) < 0) {
                                due = String.valueOf((rs.getInt("amount") - res.getInt("installments"))).replace("-", "");
                                interest = (((rs.getInt("amount") - res.getInt("installments")) / 100) * 5) + "";

                            }
                        }
                    }
                    generateTable(table, "N", res.getString("monthName"), "-0", recordCount + "", res.getString("account_number") + "",
                            res.getString("customer_name") + "", res.getString("surety_name") + "", res.getString("installments") + "",
                            due + "", interest.replace("-", "") + "");
                    recordCount++;
                }
                monthName = res.getString("monthName");
            }
            generateTable(table, "N", "", totalAmount + "", "", "", "", "", "", "", "");

            document.add(table);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in ExportDao(generateExcelReport) ::" + e.getMessage());
        } finally {

            DBAccessMain.closePrepareStatement(pstmt);
            DBAccessMain.closeResultSet(res);
        }
    }

    public void generateExcelReport(HSSFSheet mySheet, String reportDate, Connection con, HSSFWorkbook myWorkBook) {
        PreparedStatement pstmt = null, ps = null;
        ResultSet res = null, rs = null;
        HSSFRow myRow = null;
        HSSFCell myCell = null;
        int rowNum = 1;
        int recordCount = 1, totalAmount = 0;
        String monthName = "";
        HSSFFont font = null, font1 = null;
        HSSFCellStyle style = null, style1 = null;
        int i = 0;
        try {

            font = myWorkBook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 15);
            style = myWorkBook.createCellStyle();
            style.setFont(font);

            font1 = myWorkBook.createFont();
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font1.setFontName(HSSFFont.FONT_ARIAL);
            style1 = myWorkBook.createCellStyle();
            style1.setFont(font1);

            pstmt = con.prepareStatement("  SELECT  status,concat(monthname(closing_date),'-',year(closing_date))  as closeMonthName,concat(monthname(date_of_issue),'-',year(date_of_issue))  as monthName,\n"
                    + " customer_id, customer_name,  surety_name, \n"
                    + "    cus_mobile_number,    amount, DATE_FORMAT(date_of_issue,'%d-%b-%Y') as  dateofissue ,\n"
                    + "  installments,  account_number, closing_date as d,DATE_FORMAT(closing_date,'%d-%b-%Y') as  closing_date\n"
                    + " FROM  customer  Where  status='open' order by date_of_issue  ");

            res = pstmt.executeQuery();
            while (res.next()) {
//                System.out.println("" + res.getString("closeMonthName"));

                if (monthName.equals("") || !monthName.equals(res.getString("monthName"))) {
                    if (i > 0) {
                        myRow = mySheet.createRow(rowNum);

                        myCell = myRow.createCell((short) 0);
                        myCell.setCellValue("");

                        myCell = myRow.createCell((short) 1);
                        myCell.setCellValue("");

                        myCell = myRow.createCell((short) 2);
                        myCell.setCellValue("");

                        myCell = myRow.createCell((short) 3);
                        myCell.setCellValue("");

                        myCell = myRow.createCell((short) 4);
                        myCell.setCellStyle(style1);
                        myCell.setCellValue(totalAmount + "");

                        myCell = myRow.createCell((short) 5);
                        myCell.setCellValue("");

                        myCell = myRow.createCell((short) 6);
                        myCell.setCellValue("");

                        totalAmount = 0;

                        rowNum = rowNum + 1;
                    }

                    i++;
                    recordCount = 1;
                    myRow = mySheet.createRow(rowNum);
                    myRow.setHeight((short) 800);

                    myCell = myRow.createCell((short) 0);
                    myCell.setCellValue("");

                    myCell = myRow.createCell((short) 1);
                    myCell.setCellValue("");

                    myCell = myRow.createCell((short) 2);
                    myCell.setCellValue("");

                    myCell = myRow.createCell((short) 3);
                    myCell.setCellStyle(style);
                    myCell.setCellValue(res.getString("monthName") + "");

                    myCell = myRow.createCell((short) 4);
                    myCell.setCellValue("");

                    myCell = myRow.createCell((short) 5);
                    myCell.setCellValue("");

                    myCell = myRow.createCell((short) 6);
                    myCell.setCellValue("");
                    rowNum = rowNum + 1;

                    myRow = mySheet.createRow(rowNum);
                    myRow.setHeight((short) 400);
                    totalAmount += res.getInt("installments");
                    myCell = myRow.createCell((short) 0);
                    myCell.setCellValue(recordCount + "");

                    myCell = myRow.createCell((short) 1);
                    myCell.setCellValue(res.getString("account_number") + "");

                    myCell = myRow.createCell((short) 2);
                    myCell.setCellStyle(style1);
                    myCell.setCellValue(res.getString("customer_name") + "");

                    myCell = myRow.createCell((short) 3);
                    myCell.setCellValue(res.getString("surety_name") + "");

                    myCell = myRow.createCell((short) 4);
                    myCell.setCellStyle(style1);
                    myCell.setCellValue(res.getString("installments") + "");

                    ps = con.prepareStatement("SELECT  coalesce(sum(amount),0) as amount  FROM accounts where account_number=?  and month(date)=month(?) and year(date)=year(?) order by date desc ");
                    ps.setString(1, res.getString("account_number"));
                    ps.setString(2, reportDate);
                    ps.setString(3, reportDate);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        if (rs.getInt("amount") != 0) {
                            if ((rs.getInt("amount") - res.getInt("installments")) < 0) {
                                myCell = myRow.createCell((short) 5);
                                myCell.setCellValue(String.valueOf((rs.getInt("amount") - res.getInt("installments"))).replace("-", "") + "");

                                int interest = ((rs.getInt("amount") - res.getInt("installments")) / 100) * 5;

                                myCell = myRow.createCell((short) 6);
                                myCell.setCellValue(String.valueOf(interest).replace("-", "") + "");

                            }
                        }

                    } else {
                        myCell = myRow.createCell((short) 5);
                        myCell.setCellValue("");
                        myCell = myRow.createCell((short) 6);
                        myCell.setCellValue("" + "");
                    }

                    recordCount++;

                    rowNum = rowNum + 1;

                } else {
                    myRow = mySheet.createRow(rowNum);
                    myRow.setHeight((short) 400);

                    totalAmount += res.getInt("installments");
                    myCell = myRow.createCell((short) 0);
                    myCell.setCellValue(recordCount + "");

                    myCell = myRow.createCell((short) 1);
                    myCell.setCellValue(res.getString("account_number") + "");

                    myCell = myRow.createCell((short) 2);
                    myCell.setCellStyle(style1);
                    myCell.setCellValue(res.getString("customer_name") + "");

                    myCell = myRow.createCell((short) 3);
                    myCell.setCellValue(res.getString("surety_name") + "");

                    myCell = myRow.createCell((short) 4);
                    myCell.setCellStyle(style1);
                    myCell.setCellValue(res.getString("installments") + "");

                    ps = con.prepareStatement("SELECT  coalesce(sum(amount),0) as amount  FROM accounts where account_number=?  and month(date)=month(?) and year(date)=year(?) order by date desc ");
                    ps.setString(1, res.getString("account_number"));
                    ps.setString(2, reportDate);
                    ps.setString(3, reportDate);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        if (rs.getInt("amount") != 0) {
                            if ((rs.getInt("amount") - res.getInt("installments")) < 0) {
                                myCell = myRow.createCell((short) 5);
                                myCell.setCellValue(String.valueOf((rs.getInt("amount") - res.getInt("installments"))).replace("-", "") + "");

                                int interest = ((rs.getInt("amount") - res.getInt("installments")) / 100) * 5;

                                myCell = myRow.createCell((short) 6);
                                myCell.setCellValue(String.valueOf(interest).replace("-", "") + "");

                            }
                        }

                    } else {
                        myCell = myRow.createCell((short) 5);
                        myCell.setCellValue("");
                        myCell = myRow.createCell((short) 6);
                        myCell.setCellValue("" + "");
                    }

                    recordCount++;
                    rowNum = rowNum + 1;
                }
                monthName = res.getString("monthName");

            }

            myRow = mySheet.createRow(rowNum);
            myRow.setHeight((short) 400);
            myCell = myRow.createCell((short) 0);
            myCell.setCellValue("");

            myCell = myRow.createCell((short) 1);
            myCell.setCellValue("");

            myCell = myRow.createCell((short) 2);
            myCell.setCellValue("");

            myCell = myRow.createCell((short) 3);
            myCell.setCellValue("");

            myCell = myRow.createCell((short) 4);
            myCell.setCellStyle(style1);
            myCell.setCellValue(totalAmount + "");

            myCell = myRow.createCell((short) 5);
            myCell.setCellValue("");

            myCell = myRow.createCell((short) 6);
            myCell.setCellValue("");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occured in ExportDao(generateExcelReport) ::" + e.getMessage());
        } finally {

            DBAccessMain.closePrepareStatement(pstmt);
            DBAccessMain.closeResultSet(res);
        }
    }

}

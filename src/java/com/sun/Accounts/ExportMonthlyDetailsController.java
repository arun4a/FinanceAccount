/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.Accounts;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.DBOperations.DBAccessMain;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author sys1089
 */
public class ExportMonthlyDetailsController extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private String startDate;
    private InputStream inputStream;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String exportMonthlydetails() {
        ExportDao exportDao = null;
        Connection con = null;
        DBAccessMain dbAccess = null;
        SimpleDateFormat sdf = null, dbSdf = null;
        try {
            exportDao = new ExportDao();
            dbAccess = new DBAccessMain();
            con = dbAccess.getConnection();

            sdf = new SimpleDateFormat("dd-MM-yyyy");
            dbSdf = new SimpleDateFormat("yyyy-MM-dd");
            if (getStartDate() != null && !getStartDate().trim().equals("") && getStartDate().length() == 10) {
                setFileName("MonthlDueList_" + getStartDate() + "_.pdf");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, baos);

                Font f = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLDITALIC, GrayColor.BLUE);
                PdfPCell cell = new PdfPCell(new Phrase(getStartDate() + "", f));
                float[] columnWidths = {3, 4, 8, 8, 5, 3, 3};
                PdfPTable table = new PdfPTable(columnWidths);
                cell.setColspan(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(12.0f);
                table.addCell(cell);
                table.addCell(exportDao.getNormalCell("S.No", "czech", 7));
                table.addCell(exportDao.getNormalCell("A/C No", "czech", 7));
                table.addCell(exportDao.getNormalCell("Customer Name", "czech", 7));
                table.addCell(exportDao.getNormalCell("Surety Name", "czech", 7));
                table.addCell(exportDao.getNormalCell("Installment", "czech", 7));
                table.addCell(exportDao.getNormalCell("Due", "czech", 7));
                table.addCell(exportDao.getNormalCell("Interest", "czech", 7));
                document.open();//PDF document opened........	 
//            document.add(Chunk.NEWLINE);
                document.add(table);
                exportDao.generatePdf(document, startDate, con);
//            document.add(Chunk.NEWLINE);
                document.close();
//                ServletOutputStream outputStream = response.getOutputStream();
//
//                baos.writeTo(outputStream);
//
//                response.setHeader("Content-Disposition", "attachment; filename=\"stuReport.pdf\"");
//                response.setContentType("application/pdf");
                setInputStream(new ByteArrayInputStream(baos.toByteArray()));
//                outputStream.flush();
//                outputStream.close();
            }
        } catch (Exception e) {
            System.out.println("Exception occured in ContestController(exportMonthlydetails) ::" + e.getMessage());
        } finally {
            DBAccessMain.closeDbMainConnection(con);
        }
        return SUCCESS;
    }

    public HttpServletRequest request;
    public HttpServletResponse response;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}

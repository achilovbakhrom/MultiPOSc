package com.jim.multipos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.reports.summary_report.adapter.PairString;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleString;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExportUtils {

    public static final int EXCEL = 0;
    public static final int PDF = 1;

    public static void exportToExcel(Context context, String path, String filename, String description, String date, String filtered, String search, Object[][] objects, String[] titles, int[] weights, int[] dataTypes, Object[][][] statusTypes) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(filename);
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.report_two_dots) + filename);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.description_two_dots) + description);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + date);
        Row rowFiltered = sheet.createRow(4);
        Cell cellFiltered = rowFiltered.createCell(0);
        cellFiltered.setCellValue(context.getString(R.string.filtered_by) + filtered);
        Row rowSearch = sheet.createRow(5);
        Cell cellSearch = rowSearch.createCell(0);
        cellSearch.setCellValue(context.getString(R.string.searched_by) + search);
        for (int i = 0; i < objects.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(7 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(titles[j]);
                }
            } else {
                int statusCount = 0;
                Row row = sheet.createRow(7 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (objects[i - 1][j] instanceof Long) {
                        Long item = (Long) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof String) {
                        String item = (String) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.NAME:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                break;
                        }
                        cell.setCellValue(item);
                    } else if (objects[i - 1][j] instanceof Double) {
                        Double item = (Double) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.STATUS:
                                if (statusTypes != null) {
                                    for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                        if (item.intValue() == ((Integer) statusTypes[statusCount][k][0]).intValue()) {
                                            String status = (String) statusTypes[statusCount][k][1];
                                            cell.setCellValue(status);
                                        }
                                    }
                                    statusCount++;
                                }
                                break;
                            default:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    }
                }
            }

        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, weights[i] * 500);
        }
        File file = new File(path, filename + ".xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void exportToPdf(Context context, String path, String filename, String description, String date, String filtered, String search, Object[][] objects, String[] titles, int[] weights, int[] dataTypes, Object[][][] statusTypes) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            File file = new File(path, filename + ".pdf");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph descriptionParagraph = new Paragraph(new Phrase(context.getString(R.string.description_two_dots) + " " + description, bf12));
            descriptionParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(descriptionParagraph);
            Paragraph createDate = new Paragraph(new Phrase(context.getString(R.string.date_two_dots) + " " + date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph filteredParagraph = new Paragraph(new Phrase(context.getString(R.string.filtered_by) + " " + filtered, bf12));
            filteredParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(filteredParagraph);
            Paragraph searched = new Paragraph(new Phrase(context.getString(R.string.searched_by) + " " + search, bf12));
            searched.setAlignment(Element.ALIGN_RIGHT);
            document.add(searched);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            float[] columnWidths = new float[weights.length];
            for (int i = 0; i < weights.length; i++) {
                columnWidths[i] = (float) weights[i];
            }
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int i = 0; i < objects.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(titles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (dataTypes[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    int statusCount = 0;
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (objects[i - 1][j] instanceof Long) {
                            Long item = (Long) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof String) {
                            String item = (String) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Double) {
                            Double item = (Double) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.STATUS:
                                    align = Element.ALIGN_CENTER;
                                    if (statusTypes != null) {
                                        for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                            if (item.intValue() == ((Integer) statusTypes[statusCount][k][0]).intValue()) {
                                                text = (String) statusTypes[statusCount][k][1];
                                            }
                                        }
                                        statusCount++;
                                    }
                                    break;
                                default:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static void exportToExcelToUSB(Context context, UsbFile root, String filename, String description, String date, String filtered, String search, Object[][] objects, String[] titles, int[] weights, int[] dataTypes, Object[][][] statusTypes) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(filename);
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.report_two_dots) + filename);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.description_two_dots) + description);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + date);
        Row rowFiltered = sheet.createRow(4);
        Cell cellFiltered = rowFiltered.createCell(0);
        cellFiltered.setCellValue(context.getString(R.string.filtered_by) + filtered);
        Row rowSearch = sheet.createRow(5);
        Cell cellSearch = rowSearch.createCell(0);
        cellSearch.setCellValue(context.getString(R.string.searched_by) + search);
        for (int i = 0; i < objects.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(7 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(titles[j]);
                }
            } else {
                Row row = sheet.createRow(7 + i);
                int statusCount = 0;
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (objects[i - 1][j] instanceof Long) {
                        Long item = (Long) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof String) {
                        String item = (String) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.NAME:
                                cell.setCellValue(item);
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue("");
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof Double) {
                        Double item = (Double) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) objects[i - 1][j];
                        switch (dataTypes[j]) {
                            case ReportViewConstants.STATUS:
                                if (statusTypes != null) {
                                    for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                        if (item.intValue() == ((Integer) statusTypes[statusCount][k][0]).intValue()) {
                                            String status = (String) statusTypes[statusCount][k][1];
                                            cell.setCellValue(status);
                                        }
                                    }
                                    statusCount++;
                                }
                                break;
                            default:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    }
                }
            }

        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, weights[i] * 500);
        }
        try {
            UsbFile file = root.createFile(filename + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportToPdfToUSB(Context context, UsbFile root, String filename, String description, String date, String filtered, String search, Object[][] objects, String[] titles, int[] weights, int[] dataTypes, Object[][][] statusTypes) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(filename + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph descriptionParagraph = new Paragraph(new Phrase(context.getString(R.string.description_two_dots) + " " + description, bf12));
            descriptionParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(descriptionParagraph);
            Paragraph createDate = new Paragraph(new Phrase(context.getString(R.string.date_two_dots) + " " + date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph filteredParagraph = new Paragraph(new Phrase(context.getString(R.string.filtered_by) + " " + filtered, bf12));
            filteredParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(filteredParagraph);
            Paragraph searched = new Paragraph(new Phrase(context.getString(R.string.searched_by) + " " + search, bf12));
            searched.setAlignment(Element.ALIGN_RIGHT);
            document.add(searched);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            float[] columnWidths = new float[weights.length];
            for (int i = 0; i < weights.length; i++) {
                columnWidths[i] = (float) weights[i];
            }
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int i = 0; i < objects.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(titles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (dataTypes[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    int statusCount = 0;
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (objects[i - 1][j] instanceof Long) {
                            Long item = (Long) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof String) {
                            String item = (String) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Double) {
                            Double item = (Double) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) objects[i - 1][j];
                            switch (dataTypes[j]) {
                                case ReportViewConstants.STATUS:
                                    align = Element.ALIGN_CENTER;
                                    if (statusTypes != null) {
                                        for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                            if (item.intValue() == ((Integer) statusTypes[statusCount][k][0]).intValue()) {
                                                text = (String) statusTypes[statusCount][k][1];
                                            }
                                        }
                                        statusCount++;
                                    }
                                    break;
                                default:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.close();
            os.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportSummaryReportToExcel(Context context, String root, String filename, String description, String date, List<PairString> summary, List<PairString> analytics, List<TripleString> payments, List<PairString> paymentsAnalytics) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        //setting cell style
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //creating sheet
        Sheet sheet = workbook.createSheet(filename);
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.report_two_dots) + filename);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.description_two_dots) + description);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + date);
        //creating table
        int counter = 0;
        for (int i = 0; i < 16; i++) {
            Row row = sheet.createRow(4 + i);
            if (i == 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.sales_summary));
                cell.setCellStyle(cellStyle2);
                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(2);
                cell1.setCellValue(context.getString(R.string.sales_analitcs));
                cell1.setCellStyle(cellStyle2);
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
            } else if (i == 1) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.category));
                cell.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(context.getString(R.string.summary_amount));
                cell1.setCellStyle(cellStyle1);
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(cellStyle2);
                cell2.setCellValue(context.getString(R.string.category));
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
            } else {
                for (int j = 0; j < 4; j++) {
                    if (j == 0 && i <= 9) {
                        Cell cell = row.createCell(0);
                        cell.setCellValue(summary.get(counter).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(summary.get(counter).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                    if (j == 1) {
                        Cell cell = row.createCell(2);
                        cell.setCellValue(analytics.get(counter).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(3);
                        cell1.setCellValue(analytics.get(counter).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                }
                counter++;
            }
        }
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 15 * 500);
        }
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));

        int count = 0;
        for (int i = 0; i < 7; i++) {
            Row row = sheet.createRow(21 + i);
            if (i == 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.payments_summary));
                cell.setCellStyle(cellStyle2);
                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(3);
                cell1.setCellValue(context.getString(R.string.payment_analytics));
                cell1.setCellStyle(cellStyle2);
                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(cellStyle2);
                cell1.setCellStyle(cellStyle2);
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(cellStyle2);
            } else if (i == 1) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.payment_type));
                cell.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(context.getString(R.string.percentage));
                cell1.setCellStyle(cellStyle1);
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(cellStyle1);
                cell2.setCellValue(context.getString(R.string.summary_amount));
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
                cell3.setCellValue(context.getString(R.string.category));
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(cellStyle2);
            } else {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        Cell cell = row.createCell(0);
                        cell.setCellValue(payments.get(count).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(payments.get(count).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(cellStyle1);
                        cell3.setCellValue(payments.get(count).getThirdString());
                    }
                    if (j == 1 && i < 3) {
                        Cell cell = row.createCell(3);
                        cell.setCellValue(paymentsAnalytics.get(count).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(4);
                        cell1.setCellValue(paymentsAnalytics.get(count).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                }
                count++;
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(21, 21, 0, 2));
        sheet.addMergedRegion(new CellRangeAddress(21, 21, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(22, 22, 3, 4));

        File file = new File(root, filename + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportSummaryReportToExcelUSB(Context context, UsbFile root, String filename, String description, String date, List<PairString> summary, List<PairString> analytics, List<TripleString> payments, List<PairString> paymentsAnalytics) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        Sheet sheet = workbook.createSheet(filename);
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.report_two_dots) + filename);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.description_two_dots) + description);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + date);

        int counter = 0;
        for (int i = 0; i < 16; i++) {
            Row row = sheet.createRow(4 + i);
            if (i == 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.sales_summary));
                cell.setCellStyle(cellStyle2);
                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(2);
                cell1.setCellValue(context.getString(R.string.sales_analitcs));
                cell1.setCellStyle(cellStyle2);
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
            } else if (i == 1) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.category));
                cell.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(context.getString(R.string.summary_amount));
                cell1.setCellStyle(cellStyle1);
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(cellStyle2);
                cell2.setCellValue(context.getString(R.string.category));
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
            } else {
                for (int j = 0; j < 4; j++) {
                    if (j == 0 && i <= 9) {
                        Cell cell = row.createCell(0);
                        cell.setCellValue(summary.get(counter).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(summary.get(counter).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                    if (j == 1) {
                        Cell cell = row.createCell(2);
                        cell.setCellValue(analytics.get(counter).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(3);
                        cell1.setCellValue(analytics.get(counter).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                }
                counter++;
            }
        }
        for (int i = 0; i < 4; i++) {
            sheet.setColumnWidth(i, 15 * 500);
        }
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));

        int count = 0;
        for (int i = 0; i < 7; i++) {
            Row row = sheet.createRow(21 + i);
            if (i == 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.payments_summary));
                cell.setCellStyle(cellStyle2);
                Cell cell2 = row.createCell(1);
                cell2.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(3);
                cell1.setCellValue(context.getString(R.string.payment_analytics));
                cell1.setCellStyle(cellStyle2);
                Cell cell3 = row.createCell(2);
                cell3.setCellStyle(cellStyle2);
                cell1.setCellStyle(cellStyle2);
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(cellStyle2);
            } else if (i == 1) {
                Cell cell = row.createCell(0);
                cell.setCellValue(context.getString(R.string.payment_type));
                cell.setCellStyle(cellStyle2);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(context.getString(R.string.percentage));
                cell1.setCellStyle(cellStyle1);
                Cell cell2 = row.createCell(2);
                cell2.setCellStyle(cellStyle1);
                cell2.setCellValue(context.getString(R.string.summary_amount));
                Cell cell3 = row.createCell(3);
                cell3.setCellStyle(cellStyle2);
                cell3.setCellValue(context.getString(R.string.category));
                Cell cell4 = row.createCell(4);
                cell4.setCellStyle(cellStyle2);
            } else {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        Cell cell = row.createCell(0);
                        cell.setCellValue(payments.get(count).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(payments.get(count).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                        Cell cell3 = row.createCell(2);
                        cell3.setCellStyle(cellStyle1);
                        cell3.setCellValue(payments.get(count).getThirdString());
                    }
                    if (j == 1 && i < 4) {
                        Cell cell = row.createCell(3);
                        cell.setCellValue(paymentsAnalytics.get(count).getFirstString());
                        cell.setCellStyle(cellStyle2);
                        Cell cell1 = row.createCell(4);
                        cell1.setCellValue(paymentsAnalytics.get(count).getSecondString());
                        cell1.setCellStyle(cellStyle1);
                    }
                }
                count++;
            }
        }
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 15 * 500);
        }
        sheet.addMergedRegion(new CellRangeAddress(21, 21, 0, 2));
        sheet.addMergedRegion(new CellRangeAddress(21, 21, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(22, 22, 3, 4));

        try {
            UsbFile file = root.createFile(filename + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportSummaryReportToPdf(Context context, String root, String filename, String description, String date, List<PairString> summary, List<PairString> analytics, List<TripleString> payments, List<PairString> paymentsAnalytics) {
        try {
            File file = new File(root, filename + ".pdf");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph descriptionParagraph = new Paragraph(new Phrase(context.getString(R.string.description_two_dots) + " " + description, bf12));
            descriptionParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(descriptionParagraph);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);

            float[] columnWidths = {10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            int counter = 0;
            for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.sales_summary), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(2);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.sales_analitcs), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(2);
                    table.addCell(cell);
                    table.addCell(cell1);
                } else if (i == 1) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.summary_amount), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell1.setColspan(1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(2);
                    table.addCell(cell);
                    table.addCell(cell1);
                    table.addCell(cell2);
                } else {
                    for (int j = 0; j < 2; j++) {
                        if (j == 0 && i < 10) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(summary.get(counter).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(summary.get(counter).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            table.addCell(cell1);
                        }
                        if (j == 1) {
                            if (i >= 10) {
                                PdfPCell cell = new PdfPCell();
                                cell.setPhrase(new Phrase("", bf12));
                                cell.setPaddingLeft(8);
                                cell.setPaddingRight(8);
                                cell.setPaddingTop(8);
                                cell.setPaddingBottom(8);
                                cell.setColspan(1);
                                PdfPCell cell1 = new PdfPCell();
                                cell1.setPhrase(new Phrase("", bf12));
                                cell1.setPaddingLeft(8);
                                cell1.setPaddingRight(8);
                                cell1.setPaddingTop(8);
                                cell1.setPaddingBottom(8);
                                cell1.setColspan(1);
                                table.addCell(cell);
                                table.addCell(cell1);
                            }
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(analytics.get(counter).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(analytics.get(counter).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            table.addCell(cell1);
                        }
                    }
                    counter++;
                }
            }
            document.add(table);
            document.add(empty);
            document.add(empty);
            document.add(empty);
            PdfPTable table2 = new PdfPTable(new float[]{10, 10, 10, 10, 10});
            table2.setWidthPercentage(100f);
            int count = 0;
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.payments_summary), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(3);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.payment_analytics), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(2);
                    table2.addCell(cell);
                    table2.addCell(cell1);
                } else if (i == 1) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.payment_type), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.percentage), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell1.setColspan(1);
                    PdfPCell cell3 = new PdfPCell();
                    cell3.setPhrase(new Phrase(context.getString(R.string.summary_amount), bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(2);
                    table2.addCell(cell);
                    table2.addCell(cell1);
                    table2.addCell(cell2);
                    table2.addCell(cell3);
                } else {
                    for (int j = 0; j < 2; j++) {
                        if (j == 0) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(payments.get(count).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(payments.get(count).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            PdfPCell cell4 = new PdfPCell();
                            cell4.setPhrase(new Phrase(payments.get(count).getThirdString(), bf12));
                            cell4.setPaddingLeft(8);
                            cell4.setPaddingRight(8);
                            cell4.setPaddingTop(8);
                            cell4.setPaddingBottom(8);
                            cell4.setColspan(1);
                            cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table2.addCell(cell);
                            table2.addCell(cell1);
                            table2.addCell(cell4);
                            if (i >= 3) {
                                PdfPCell cell2 = new PdfPCell();
                                cell2.setPhrase(new Phrase("", bf12));
                                cell2.setPaddingLeft(8);
                                cell2.setPaddingRight(8);
                                cell2.setPaddingTop(8);
                                cell2.setPaddingBottom(8);
                                cell2.setColspan(1);
                                PdfPCell cell3 = new PdfPCell();
                                cell3.setPhrase(new Phrase("", bf12));
                                cell3.setPaddingLeft(8);
                                cell3.setPaddingRight(8);
                                cell3.setPaddingTop(8);
                                cell3.setPaddingBottom(8);
                                cell3.setColspan(1);
                                table2.addCell(cell2);
                                table2.addCell(cell3);
                            }
                        }
                        if (j == 1 && i < 3) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(paymentsAnalytics.get(count).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(paymentsAnalytics.get(count).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table2.addCell(cell);
                            table2.addCell(cell1);
                        }
                    }
                    count++;
                }
            }
            document.add(table2);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void exportSummaryReportToPdfUSB(Context context, UsbFile root, String filename, String description, String date, List<PairString> summary, List<PairString> analytics, List<TripleString> payments, List<PairString> paymentsAnalytics) {
        try {
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(filename + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph descriptionParagraph = new Paragraph(new Phrase(context.getString(R.string.description_two_dots) + " " + description, bf12));
            descriptionParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(descriptionParagraph);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            float[] columnWidths = {10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            int counter = 0;
            for (int i = 0; i < 16; i++) {
                if (i == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.sales_summary), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(2);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.sales_analitcs), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(2);
                    table.addCell(cell);
                    table.addCell(cell1);
                } else if (i == 1) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.summary_amount), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell1.setColspan(1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(2);
                    table.addCell(cell);
                    table.addCell(cell1);
                    table.addCell(cell2);
                } else {
                    for (int j = 0; j < 2; j++) {
                        if (j == 0 && i < 10) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(summary.get(counter).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(summary.get(counter).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            table.addCell(cell1);
                        }
                        if (j == 1) {
                            if (i >= 10) {
                                PdfPCell cell = new PdfPCell();
                                cell.setPhrase(new Phrase("", bf12));
                                cell.setPaddingLeft(8);
                                cell.setPaddingRight(8);
                                cell.setPaddingTop(8);
                                cell.setPaddingBottom(8);
                                cell.setColspan(1);
                                PdfPCell cell1 = new PdfPCell();
                                cell1.setPhrase(new Phrase("", bf12));
                                cell1.setPaddingLeft(8);
                                cell1.setPaddingRight(8);
                                cell1.setPaddingTop(8);
                                cell1.setPaddingBottom(8);
                                cell1.setColspan(1);
                                table.addCell(cell);
                                table.addCell(cell1);
                            }
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(analytics.get(counter).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(analytics.get(counter).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            table.addCell(cell1);
                        }
                    }
                    counter++;
                }
            }
            document.add(table);
            document.add(empty);
            document.add(empty);
            document.add(empty);
            PdfPTable table2 = new PdfPTable(new float[]{10, 10, 10, 10, 10});
            table2.setWidthPercentage(100f);
            int count = 0;
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.payments_summary), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(3);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.payment_analytics), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(2);
                    table2.addCell(cell);
                    table2.addCell(cell1);
                } else if (i == 1) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(context.getString(R.string.payment_type), bfBold12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(context.getString(R.string.percentage), bfBold12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell1.setColspan(1);
                    PdfPCell cell3 = new PdfPCell();
                    cell3.setPhrase(new Phrase(context.getString(R.string.summary_amount), bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(context.getString(R.string.category), bfBold12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(2);
                    table2.addCell(cell);
                    table2.addCell(cell1);
                    table2.addCell(cell2);
                    table2.addCell(cell3);
                } else {
                    for (int j = 0; j < 2; j++) {
                        if (j == 0) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(payments.get(count).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(payments.get(count).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            PdfPCell cell4 = new PdfPCell();
                            cell4.setPhrase(new Phrase(payments.get(count).getThirdString(), bf12));
                            cell4.setPaddingLeft(8);
                            cell4.setPaddingRight(8);
                            cell4.setPaddingTop(8);
                            cell4.setPaddingBottom(8);
                            cell4.setColspan(1);
                            cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table2.addCell(cell);
                            table2.addCell(cell1);
                            table2.addCell(cell4);
                            if (i >= 3) {
                                PdfPCell cell2 = new PdfPCell();
                                cell2.setPhrase(new Phrase("", bf12));
                                cell2.setPaddingLeft(8);
                                cell2.setPaddingRight(8);
                                cell2.setPaddingTop(8);
                                cell2.setPaddingBottom(8);
                                cell2.setColspan(1);
                                PdfPCell cell3 = new PdfPCell();
                                cell3.setPhrase(new Phrase("", bf12));
                                cell3.setPaddingLeft(8);
                                cell3.setPaddingRight(8);
                                cell3.setPaddingTop(8);
                                cell3.setPaddingBottom(8);
                                cell3.setColspan(1);
                                table2.addCell(cell2);
                                table2.addCell(cell3);
                            }
                        }
                        if (j == 1 && i < 3) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Phrase(paymentsAnalytics.get(count).getFirstString(), bf12));
                            cell.setPaddingLeft(8);
                            cell.setPaddingRight(8);
                            cell.setPaddingTop(8);
                            cell.setPaddingBottom(8);
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setColspan(1);
                            PdfPCell cell1 = new PdfPCell();
                            cell1.setPhrase(new Phrase(paymentsAnalytics.get(count).getSecondString(), bf12));
                            cell1.setPaddingLeft(8);
                            cell1.setPaddingRight(8);
                            cell1.setPaddingTop(8);
                            cell1.setPaddingBottom(8);
                            cell1.setColspan(1);
                            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table2.addCell(cell);
                            table2.addCell(cell1);
                        }
                    }
                    count++;
                }
            }
            document.add(table2);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportTillDetailsToExcel(Context context, String filename, String root, String date, double[][] values, String[] accounts, String[] names) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        //setting cell style
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //creating sheet
        for (int i = 0; i < accounts.length; i++) {
            Sheet sheet = workbook.createSheet(accounts[i]);
            Row rowReportName = sheet.createRow(1);
            Cell cellReportName = rowReportName.createCell(0);
            cellReportName.setCellValue(context.getString(R.string.account) + " " + accounts[i]);
            Row rowDate = sheet.createRow(2);
            Cell cellDate = rowDate.createCell(0);
            cellDate.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
            for (int j = 0; j < 12; j++) {
                Row row = sheet.createRow(4 + j);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle2);
                cell.setCellValue(names[j]);
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle1);
                cell1.setCellValue(decimalFormat.format(values[i][j]));
            }
            for (int j = 0; j < 2; j++) {
                sheet.setColumnWidth(j, 15 * 500);
            }
        }

        File file = new File(root, filename + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportTillDetailsToExcelUSB(Context context, String filename, UsbFile root, String date, double[][] values, String[] accounts, String[] names) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        //setting cell style
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //creating sheet

        for (int i = 0; i < accounts.length; i++) {
            Sheet sheet = workbook.createSheet(accounts[i]);
            Row rowReportName = sheet.createRow(1);
            Cell cellReportName = rowReportName.createCell(0);
            cellReportName.setCellValue(context.getString(R.string.account) + " " + accounts[i]);
            Row rowDate = sheet.createRow(2);
            Cell cellDate = rowDate.createCell(0);
            cellDate.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
            for (int j = 0; j < 12; j++) {
                Row row = sheet.createRow(4 + j);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle2);
                cell.setCellValue(names[j]);
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle1);
                cell1.setCellValue(decimalFormat.format(values[i][j]));
            }
            for (int j = 0; j < 2; j++) {
                sheet.setColumnWidth(j, 15 * 500);
            }
        }

        try {
            UsbFile file = root.createFile(filename + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportTillDetailsToPdf(Context context, String filename, String root, String date, double[][] values, String[] accounts, String[] names) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            File file = new File(root, filename + ".pdf");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph paragraph = new Paragraph(context.getString(R.string.date_two_dots) + " " + date, bf12);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            float[] columnWidths = {10, 10};
            for (int i = 0; i < accounts.length; i++) {
                Paragraph elements = new Paragraph(context.getString(R.string.account) + ": " + accounts[i], bf12);
                elements.setAlignment(Element.ALIGN_RIGHT);
                document.add(elements);
                PdfPTable table = new PdfPTable(columnWidths);
                table.setWidthPercentage(100f);
                for (int j = 0; j < values[i].length; j++) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(names[j], bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(decimalFormat.format(values[i][j]), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                }
                document.add(table);
                document.add(empty);
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportTillDetailsToPdfUSB(Context context, String filename, UsbFile root, String date, double[][] values, String[] accounts, String[] names) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(filename + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.report_two_dots) + " " + filename, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph paragraph = new Paragraph(context.getString(R.string.date_two_dots) + " " + date, bf12);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraph);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            float[] columnWidths = {10, 10};
            for (int i = 0; i < accounts.length; i++) {
                Paragraph elements = new Paragraph(context.getString(R.string.account) + ": " + accounts[i], bf12);
                elements.setAlignment(Element.ALIGN_RIGHT);
                document.add(elements);
                document.add(empty);
                PdfPTable table = new PdfPTable(columnWidths);
                table.setWidthPercentage(100f);
                for (int j = 0; j < values[i].length; j++) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(names[j], bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(decimalFormat.format(values[i][j]), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                }
                document.add(table);
                document.add(empty);
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportOrderDetailsToExcel(Context context, String filename, String root, String orderId, String customer, String date, Object[][] details, Object[][] payments, Object[][] lifecycle, String[] orderDetails, double[] orderValues, double[] paymentValues, String[] paymentDetails, int[] orderDetialsDataType, String[] orderDetialsTitles, int[] orderDetialsWeights, int[] paymentsDataType, String[] paymentsTitles, int[] paymentsWeights, int[] lifecycleDataType, String[] lifecycleTitles, int[] lifecycleWeights, Object[][][] lifecycleStatusTypes) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(context.getString(R.string.order_details));
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < orderDetails.length; i++) {
            Row row = sheet.createRow(5 + i);
            if (orderDetails[i].length() != 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(orderDetails[i]);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(orderValues[i]);
            }
        }
        for (int i = 0; i < details.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < orderDetialsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(orderDetialsTitles[j]);
                }
            } else {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < orderDetialsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (details[i - 1][j] instanceof Long) {
                        Long item = (Long) details[i - 1][j];
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (details[i - 1][j] instanceof String) {
                        String item = (String) details[i - 1][j];
                        cell.setCellValue(item);
                    } else if (details[i - 1][j] instanceof Double) {
                        Double item = (Double) details[i - 1][j];
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (details[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) details[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < orderDetialsTitles.length; i++) {
            sheet.setColumnWidth(i, orderDetialsWeights[i] * 500);
        }

        Sheet sheet1 = workbook.createSheet(context.getString(R.string.payments));
        Row rowReportName1 = sheet1.createRow(1);
        Cell cellReportName1 = rowReportName1.createCell(0);
        cellReportName1.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription1 = sheet1.createRow(2);
        Cell cellDescription1 = rowDescription1.createCell(0);
        cellDescription1.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate1 = sheet1.createRow(3);
        Cell cellDate1 = rowDate1.createCell(0);
        cellDate1.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < paymentDetails.length; i++) {
            Row row = sheet1.createRow(5 + i);
            Cell cell = row.createCell(0);
            cell.setCellValue(paymentDetails[i]);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(paymentValues[i]);
        }
        for (int i = 0; i < payments.length + 1; i++) {
            if (i == 0) {
                Row row = sheet1.createRow(11 + i);
                for (int j = 0; j < paymentsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(paymentsTitles[j]);
                }
            } else {
                Row row = sheet1.createRow(11 + i);
                for (int j = 0; j < paymentsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (payments[i - 1][j] instanceof Long) {
                        Long item = (Long) payments[i - 1][j];
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (payments[i - 1][j] instanceof String) {
                        String item = (String) payments[i - 1][j];
                        cell.setCellValue(item);
                    } else if (payments[i - 1][j] instanceof Double) {
                        Double item = (Double) payments[i - 1][j];
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (payments[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) payments[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < paymentsTitles.length; i++) {
            sheet1.setColumnWidth(i, paymentsWeights[i] * 500);
        }
        Sheet sheet2 = workbook.createSheet(context.getString(R.string.lifecycle));
        Row rowReportName2 = sheet2.createRow(1);
        Cell cellReportName2 = rowReportName2.createCell(0);
        cellReportName2.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription2 = sheet2.createRow(2);
        Cell cellDescription2 = rowDescription2.createCell(0);
        cellDescription2.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate2 = sheet2.createRow(3);
        Cell cellDate2 = rowDate2.createCell(0);
        cellDate2.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < lifecycle.length + 1; i++) {
            if (i == 0) {
                Row row = sheet2.createRow(5 + i);
                for (int j = 0; j < lifecycleTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(lifecycleTitles[j]);
                }
            } else {
                Row row = sheet2.createRow(5 + i);
                int statusCount = 0;
                for (int j = 0; j < lifecycleTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (lifecycle[i - 1][j] instanceof Long) {
                        Long item = (Long) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (lifecycle[i - 1][j] instanceof String) {
                        String item = (String) lifecycle[i - 1][j];
                        cell.setCellValue(item);
                    } else if (lifecycle[i - 1][j] instanceof Double) {
                        Double item = (Double) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (lifecycle[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.STATUS:
                                if (lifecycleStatusTypes != null) {
                                    for (int k = 0; k < lifecycleStatusTypes[statusCount].length; k++) {
                                        if (item.intValue() == ((Integer) lifecycleStatusTypes[statusCount][k][0]).intValue()) {
                                            String status = (String) lifecycleStatusTypes[statusCount][k][1];
                                            cell.setCellValue(status);
                                        }
                                    }
                                    statusCount++;
                                }
                                break;
                            default:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < lifecycleTitles.length; i++) {
            sheet2.setColumnWidth(i, lifecycleWeights[i] * 500);
        }
        File file = new File(root, filename + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportOrderDetailsToExcelUsb(Context context, String filename, UsbFile root, String orderId, String customer, String date, Object[][] details, Object[][] payments, Object[][] lifecycle, String[] orderDetails, double[] orderValues, double[] paymentValues, String[] paymentDetails, int[] orderDetialsDataType, String[] orderDetialsTitles, int[] orderDetialsWeights, int[] paymentsDataType, String[] paymentsTitles, int[] paymentsWeights, int[] lifecycleDataType, String[] lifecycleTitles, int[] lifecycleWeights, Object[][][] lifecycleStatusTypes) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(context.getString(R.string.order_details));
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < orderDetails.length; i++) {
            Row row = sheet.createRow(5 + i);
            if (orderDetails[i].length() != 0) {
                Cell cell = row.createCell(0);
                cell.setCellValue(orderDetails[i]);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(orderValues[i]);
            }
        }
        for (int i = 0; i < details.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < orderDetialsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(orderDetialsTitles[j]);
                }
            } else {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < orderDetialsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (details[i - 1][j] instanceof Long) {
                        Long item = (Long) details[i - 1][j];
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (details[i - 1][j] instanceof String) {
                        String item = (String) details[i - 1][j];
                        cell.setCellValue(item);
                    } else if (details[i - 1][j] instanceof Double) {
                        Double item = (Double) details[i - 1][j];
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (details[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) details[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < orderDetialsTitles.length; i++) {
            sheet.setColumnWidth(i, orderDetialsWeights[i] * 500);
        }

        Sheet sheet1 = workbook.createSheet(context.getString(R.string.payments));
        Row rowReportName1 = sheet1.createRow(1);
        Cell cellReportName1 = rowReportName1.createCell(0);
        cellReportName1.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription1 = sheet1.createRow(2);
        Cell cellDescription1 = rowDescription1.createCell(0);
        cellDescription1.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate1 = sheet1.createRow(3);
        Cell cellDate1 = rowDate1.createCell(0);
        cellDate1.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < paymentDetails.length; i++) {
            Row row = sheet1.createRow(5 + i);
            Cell cell = row.createCell(0);
            cell.setCellValue(paymentDetails[i]);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(paymentValues[i]);
        }
        for (int i = 0; i < payments.length + 1; i++) {
            if (i == 0) {
                Row row = sheet1.createRow(11 + i);
                for (int j = 0; j < paymentsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(paymentsTitles[j]);
                }
            } else {
                Row row = sheet1.createRow(11 + i);
                for (int j = 0; j < paymentsTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (payments[i - 1][j] instanceof Long) {
                        Long item = (Long) payments[i - 1][j];
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (payments[i - 1][j] instanceof String) {
                        String item = (String) payments[i - 1][j];
                        cell.setCellValue(item);
                    } else if (payments[i - 1][j] instanceof Double) {
                        Double item = (Double) payments[i - 1][j];
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (payments[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) payments[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < paymentsTitles.length; i++) {
            sheet1.setColumnWidth(i, paymentsWeights[i] * 500);
        }
        Sheet sheet2 = workbook.createSheet(context.getString(R.string.lifecycle));
        Row rowReportName2 = sheet2.createRow(1);
        Cell cellReportName2 = rowReportName2.createCell(0);
        cellReportName2.setCellValue(context.getString(R.string.order) + ": " + orderId);
        Row rowDescription2 = sheet2.createRow(2);
        Cell cellDescription2 = rowDescription2.createCell(0);
        cellDescription2.setCellValue(context.getString(R.string.customer) + ": " + customer);
        Row rowDate2 = sheet2.createRow(3);
        Cell cellDate2 = rowDate2.createCell(0);
        cellDate2.setCellValue(context.getString(R.string.date_two_dots) + " " + date);
        for (int i = 0; i < lifecycle.length + 1; i++) {
            if (i == 0) {
                Row row = sheet2.createRow(5 + i);
                for (int j = 0; j < lifecycleTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(lifecycleTitles[j]);
                }
            } else {
                Row row = sheet2.createRow(5 + i);
                int statusCount = 0;
                for (int j = 0; j < lifecycleTitles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (lifecycle[i - 1][j] instanceof Long) {
                        Long item = (Long) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (lifecycle[i - 1][j] instanceof String) {
                        String item = (String) lifecycle[i - 1][j];
                        cell.setCellValue(item);
                    } else if (lifecycle[i - 1][j] instanceof Double) {
                        Double item = (Double) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (lifecycle[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) lifecycle[i - 1][j];
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.STATUS:
                                if (lifecycleStatusTypes != null) {
                                    for (int k = 0; k < lifecycleStatusTypes[statusCount].length; k++) {
                                        if (item.intValue() == ((Integer) lifecycleStatusTypes[statusCount][k][0]).intValue()) {
                                            String status = (String) lifecycleStatusTypes[statusCount][k][1];
                                            cell.setCellValue(status);
                                        }
                                    }
                                    statusCount++;
                                }
                                break;
                            default:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < lifecycleTitles.length; i++) {
            sheet2.setColumnWidth(i, lifecycleWeights[i] * 500);
        }

        try {
            UsbFile file = root.createFile(filename + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportOrderDetailsToPdfUsb(Context context, String filename, UsbFile root, String orderId, String customer, String date, Object[][] details, Object[][] payments, Object[][] lifecycle, String[] orderDetails, double[] orderValues, double[] paymentValues, String[] paymentDetails, int[] orderDetialsDataType, String[] orderDetialsTitles, int[] orderDetialsWeights, int[] paymentsDataType, String[] paymentsTitles, int[] paymentsWeights, int[] lifecycleDataType, String[] lifecycleTitles, int[] lifecycleWeights, Object[][][] lifecycleStatusTypes) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(filename + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.order) + ": " + orderId, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph customerName = new Paragraph(new Phrase(context.getString(R.string.customer) + ": " + customer, bf12));
            customerName.setAlignment(Element.ALIGN_RIGHT);
            document.add(customerName);
            Paragraph createDate = new Paragraph(new Phrase(context.getString(R.string.date_two_dots) + " " + date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            Paragraph name1 = new Paragraph(new Phrase(context.getString(R.string.order_details), bf12));
            name1.setAlignment(Element.ALIGN_LEFT);
            document.add(name1);
            document.add(empty);
            for (int i = 0; i < orderDetails.length; i++) {
                if (orderDetails[i].length() != 0) {
                    Paragraph paragraph = new Paragraph(new Phrase(orderDetails[i] + ": " + decimalFormat.format(orderValues[i]), bf12));
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    document.add(paragraph);
                }
            }
            float[] orderDetailsWidth = new float[orderDetialsWeights.length];
            for (int i = 0; i < orderDetialsWeights.length; i++) {
                orderDetailsWidth[i] = (float) orderDetialsWeights[i];
            }
            PdfPTable table = new PdfPTable(orderDetailsWidth);
            table.setWidthPercentage(100f);
            for (int i = 0; i < details.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < orderDetialsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(orderDetialsTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < orderDetialsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (details[i - 1][j] instanceof Long) {
                            Long item = (Long) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof String) {
                            String item = (String) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof Double) {
                            Double item = (Double) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) details[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);

                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.add(empty);
            Paragraph name2 = new Paragraph(new Phrase(context.getString(R.string.payments), bf12));
            name2.setAlignment(Element.ALIGN_LEFT);
            document.add(name2);
            document.add(empty);
            for (int i = 0; i < paymentDetails.length; i++) {
                Paragraph paragraph = new Paragraph(new Phrase(paymentDetails[i] + ": " + decimalFormat.format(paymentValues[i]), bf12));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);
            }
            float[] paymentsWidth = new float[paymentsWeights.length];
            for (int i = 0; i < paymentsWeights.length; i++) {
                paymentsWidth[i] = (float) paymentsWeights[i];
            }
            PdfPTable table1 = new PdfPTable(paymentsWidth);
            table1.setWidthPercentage(100f);
            for (int i = 0; i < payments.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < paymentsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(paymentsTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table1.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < paymentsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (payments[i - 1][j] instanceof Long) {
                            Long item = (Long) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof String) {
                            String item = (String) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof Double) {
                            Double item = (Double) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) payments[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table1.addCell(cell);
                    }
                }
            }
            document.add(table1);
            Paragraph name3 = new Paragraph(new Phrase(context.getString(R.string.lifecycle), bf12));
            name3.setAlignment(Element.ALIGN_LEFT);
            document.add(name3);
            document.add(empty);
            float[] lifecycleWidth = new float[lifecycleWeights.length];
            for (int i = 0; i < lifecycleWeights.length; i++) {
                lifecycleWidth[i] = (float) lifecycleWeights[i];
            }
            PdfPTable table2 = new PdfPTable(lifecycleWidth);
            table2.setWidthPercentage(100f);
            for (int i = 0; i < lifecycle.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < lifecycleTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(lifecycleTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table2.addCell(cell);
                    }
                } else {
                    int statusCount = 0;
                    for (int j = 0; j < lifecycleTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (lifecycle[i - 1][j] instanceof Long) {
                            Long item = (Long) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof String) {
                            String item = (String) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof Double) {
                            Double item = (Double) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.STATUS:
                                    align = Element.ALIGN_CENTER;
                                    if (lifecycleStatusTypes != null) {
                                        for (int k = 0; k < lifecycleStatusTypes[statusCount].length; k++) {
                                            if (item.intValue() == ((Integer) lifecycleStatusTypes[statusCount][k][0]).intValue()) {
                                                text = (String) lifecycleStatusTypes[statusCount][k][1];
                                            }
                                        }
                                        statusCount++;
                                    }
                                    break;
                                default:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table2.addCell(cell);
                    }
                }
            }
            document.add(table2);
            document.add(empty);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportOrderDetailsToPdf(Context context, String filename, String root, String orderId, String customer, String date, Object[][] details, Object[][] payments, Object[][] lifecycle, String[] orderDetails, double[] orderValues, double[] paymentValues, String[] paymentDetails, int[] orderDetialsDataType, String[] orderDetialsTitles, int[] orderDetialsWeights, int[] paymentsDataType, String[] paymentsTitles, int[] paymentsWeights, int[] lifecycleDataType, String[] lifecycleTitles, int[] lifecycleWeights, Object[][][] lifecycleStatusTypes) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            File file = new File(root, filename + ".pdf");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.order) + ": " + orderId, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph customerName = new Paragraph(new Phrase(context.getString(R.string.customer) + ": " + customer, bf12));
            customerName.setAlignment(Element.ALIGN_RIGHT);
            document.add(customerName);
            Paragraph createDate = new Paragraph(new Phrase(context.getString(R.string.date_two_dots) + " " + date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            Paragraph name1 = new Paragraph(new Phrase(context.getString(R.string.order_details), bf12));
            name1.setAlignment(Element.ALIGN_RIGHT);
            document.add(name1);
            for (int i = 0; i < orderDetails.length; i++) {
                if (orderDetails[i].length() != 0) {
                    Paragraph paragraph = new Paragraph(new Phrase(orderDetails[i] + ": " + decimalFormat.format(orderValues[i]), bf12));
                    paragraph.setAlignment(Element.ALIGN_RIGHT);
                    document.add(paragraph);
                }
            }
            float[] orderDetailsWidth = new float[orderDetialsWeights.length];
            for (int i = 0; i < orderDetialsWeights.length; i++) {
                orderDetailsWidth[i] = (float) orderDetialsWeights[i];
            }
            PdfPTable table = new PdfPTable(orderDetailsWidth);
            table.setWidthPercentage(100f);
            for (int i = 0; i < details.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < orderDetialsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(orderDetialsTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (orderDetialsDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < orderDetialsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (details[i - 1][j] instanceof Long) {
                            Long item = (Long) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof String) {
                            String item = (String) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof Double) {
                            Double item = (Double) details[i - 1][j];
                            switch (orderDetialsDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (details[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) details[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);

                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.add(empty);
            Paragraph name2 = new Paragraph(new Phrase(context.getString(R.string.payments), bf12));
            name2.setAlignment(Element.ALIGN_RIGHT);
            document.add(name2);
            for (int i = 0; i < paymentDetails.length; i++) {
                Paragraph paragraph = new Paragraph(new Phrase(paymentDetails[i] + ": " + decimalFormat.format(paymentValues[i]), bf12));
                paragraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraph);
            }
            float[] paymentsWidth = new float[paymentsWeights.length];
            for (int i = 0; i < paymentsWeights.length; i++) {
                paymentsWidth[i] = (float) paymentsWeights[i];
            }
            PdfPTable table1 = new PdfPTable(paymentsWidth);
            table1.setWidthPercentage(100f);
            for (int i = 0; i < payments.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < paymentsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(paymentsTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (paymentsDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table1.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < paymentsTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (payments[i - 1][j] instanceof Long) {
                            Long item = (Long) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof String) {
                            String item = (String) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof Double) {
                            Double item = (Double) payments[i - 1][j];
                            switch (paymentsDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (payments[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) payments[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table1.addCell(cell);
                    }
                }
            }
            document.add(table1);
            Paragraph name3 = new Paragraph(new Phrase(context.getString(R.string.lifecycle), bf12));
            name3.setAlignment(Element.ALIGN_RIGHT);
            document.add(name3);
            float[] lifecycleWidth = new float[lifecycleWeights.length];
            for (int i = 0; i < lifecycleWeights.length; i++) {
                lifecycleWidth[i] = (float) lifecycleWeights[i];
            }
            PdfPTable table2 = new PdfPTable(lifecycleWidth);
            table2.setWidthPercentage(100f);
            for (int i = 0; i < lifecycle.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < lifecycleTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(lifecycleTitles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (lifecycleDataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table2.addCell(cell);
                    }
                } else {
                    int statusCount = 0;
                    for (int j = 0; j < lifecycleTitles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (lifecycle[i - 1][j] instanceof Long) {
                            Long item = (Long) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof String) {
                            String item = (String) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof Double) {
                            Double item = (Double) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (lifecycle[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) lifecycle[i - 1][j];
                            switch (lifecycleDataType[j]) {
                                case ReportViewConstants.STATUS:
                                    align = Element.ALIGN_CENTER;
                                    if (lifecycleStatusTypes != null) {
                                        for (int k = 0; k < lifecycleStatusTypes[statusCount].length; k++) {
                                            if (item.intValue() == ((Integer) lifecycleStatusTypes[statusCount][k][0]).intValue()) {
                                                text = (String) lifecycleStatusTypes[statusCount][k][1];
                                            }
                                        }
                                        statusCount++;
                                    }
                                    break;
                                default:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table2.addCell(cell);
                    }
                }
            }
            document.add(table2);
            document.add(empty);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportConsignmentToExcel(Context context, String filename, String root, Object[][] objects, int[] weights, int[] dataType, String[] titles, String vendor, String consignmentId, String date, String type, String[] values, String[] names) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(context.getString(R.string.consignment_details));
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(consignmentId);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(vendor);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(date);
        Row rowType = sheet.createRow(4);
        Cell cellType = rowType.createCell(0);
        cellType.setCellValue(type);

        for (int i = 0; i < names.length; i++) {
            Row row = sheet.createRow(6 + i);
            Cell cell = row.createCell(0);
            cell.setCellValue(names[i]);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(values[i]);
        }

        for (int i = 0; i < objects.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(titles[j]);
                }
            } else {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (objects[i - 1][j] instanceof Long) {
                        Long item = (Long) objects[i - 1][j];
                        switch (dataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof String) {
                        String item = (String) objects[i - 1][j];
                        cell.setCellValue(item);
                    } else if (objects[i - 1][j] instanceof Double) {
                        Double item = (Double) objects[i - 1][j];
                        switch (dataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) objects[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, weights[i] * 500);
        }

        File file = new File(root, filename + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportConsignmentToExcelUsb(Context context, String filename, UsbFile root, Object[][] objects, int[] weights, int[] dataType, String[] titles, String vendor, String consignmentId, String date, String type, String[] values, String[] names) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }

        Workbook workbook = new HSSFWorkbook();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Sheet sheet = workbook.createSheet(context.getString(R.string.consignment_details));
        Row rowReportName = sheet.createRow(1);
        Cell cellReportName = rowReportName.createCell(0);
        cellReportName.setCellValue(consignmentId);
        Row rowDescription = sheet.createRow(2);
        Cell cellDescription = rowDescription.createCell(0);
        cellDescription.setCellValue(vendor);
        Row rowDate = sheet.createRow(3);
        Cell cellDate = rowDate.createCell(0);
        cellDate.setCellValue(date);
        Row rowType = sheet.createRow(4);
        Cell cellType = rowType.createCell(0);
        cellType.setCellValue(type);

        for (int i = 0; i < names.length; i++) {
            Row row = sheet.createRow(6 + i);
            Cell cell = row.createCell(0);
            cell.setCellValue(names[i]);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(values[i]);
        }

        for (int i = 0; i < objects.length + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(titles[j]);
                }
            } else {
                Row row = sheet.createRow(11 + i);
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (objects[i - 1][j] instanceof Long) {
                        Long item = (Long) objects[i - 1][j];
                        switch (dataType[j]) {
                            case ReportViewConstants.DATE:
                                cell.setCellValue(simpleDateFormat.format(new Date(item)));
                                break;
                            case ReportViewConstants.ID:
                                cell.setCellValue(String.valueOf(item));
                                break;
                            case ReportViewConstants.ACTION:
                                cell.setCellValue(String.valueOf(item));
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof String) {
                        String item = (String) objects[i - 1][j];
                        cell.setCellValue(item);
                    } else if (objects[i - 1][j] instanceof Double) {
                        Double item = (Double) objects[i - 1][j];
                        switch (dataType[j]) {
                            case ReportViewConstants.AMOUNT:
                                cell.setCellValue(decimalFormat.format(item));
                                break;
                            case ReportViewConstants.QUANTITY:
                                cell.setCellValue(item);
                                break;
                        }
                    } else if (objects[i - 1][j] instanceof Integer) {
                        Integer item = (Integer) objects[i - 1][j];
                        cell.setCellValue(String.valueOf(item));
                    }
                }
            }
        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, weights[i] * 500);
        }


        try {
            UsbFile file = root.createFile(filename + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportConsignmentToPdf(Context context, String filename, String root, Object[][] objects, int[] weights, int[] dataType, String[] titles, String vendor, String consignmentId, String date, String type, String[] values, String[] names) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            File file = new File(root, filename + ".pdf");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(consignmentId, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph customerName = new Paragraph(new Phrase(vendor, bf12));
            customerName.setAlignment(Element.ALIGN_RIGHT);
            document.add(customerName);
            Paragraph createDate = new Paragraph(new Phrase(date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph typeName = new Paragraph(new Phrase(type, bf12));
            typeName.setAlignment(Element.ALIGN_RIGHT);
            document.add(typeName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            for (int i = 0; i < values.length; i++) {
                Paragraph paragraph = new Paragraph(new Phrase(names[i] + ": " + decimalFormat.format(values[i]), bf12));
                paragraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraph);
            }
            float[] orderDetailsWidth = new float[weights.length];
            for (int i = 0; i < weights.length; i++) {
                orderDetailsWidth[i] = (float) weights[i];
            }
            PdfPTable table = new PdfPTable(orderDetailsWidth);
            table.setWidthPercentage(100f);
            for (int i = 0; i < objects.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(titles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (dataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (objects[i - 1][j] instanceof Long) {
                            Long item = (Long) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof String) {
                            String item = (String) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Double) {
                            Double item = (Double) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) objects[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);

                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportConsignmentToPdfUsb(Context context, String filename, UsbFile root, Object[][] objects, int[] weights, int[] dataType, String[] titles, String vendor, String consignmentId, String date, String type, String[] values, String[] names) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bfBold12 = new Font(baseFont, 14, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(filename + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(consignmentId, bf12);
            reportName.setAlignment(Element.ALIGN_RIGHT);
            document.add(reportName);
            Paragraph customerName = new Paragraph(new Phrase(vendor, bf12));
            customerName.setAlignment(Element.ALIGN_RIGHT);
            document.add(customerName);
            Paragraph createDate = new Paragraph(new Phrase(date, bf12));
            createDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(createDate);
            Paragraph typeName = new Paragraph(new Phrase(type, bf12));
            typeName.setAlignment(Element.ALIGN_RIGHT);
            document.add(typeName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            for (int i = 0; i < values.length; i++) {
                Paragraph paragraph = new Paragraph(new Phrase(names[i] + ": " + decimalFormat.format(values[i]), bf12));
                paragraph.setAlignment(Element.ALIGN_RIGHT);
                document.add(paragraph);
            }
            float[] orderDetailsWidth = new float[weights.length];
            for (int i = 0; i < weights.length; i++) {
                orderDetailsWidth[i] = (float) weights[i];
            }
            PdfPTable table = new PdfPTable(orderDetailsWidth);
            table.setWidthPercentage(100f);
            for (int i = 0; i < objects.length + 1; i++) {
                if (i == 0) {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(titles[j], bfBold12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        int align = Element.ALIGN_RIGHT;
                        switch (dataType[j]) {
                            case ReportViewConstants.DATE:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ID:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.ACTION:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.AMOUNT:
                                align = Element.ALIGN_RIGHT;
                                break;
                            case ReportViewConstants.QUANTITY:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.STATUS:
                                align = Element.ALIGN_CENTER;
                                break;
                            case ReportViewConstants.NAME:
                                align = Element.ALIGN_LEFT;
                                break;
                        }
                        cell.setHorizontalAlignment(align);
                        table.addCell(cell);
                    }
                } else {
                    for (int j = 0; j < titles.length; j++) {
                        PdfPCell cell = new PdfPCell();
                        int align = Element.ALIGN_RIGHT;
                        String text = "";
                        if (objects[i - 1][j] instanceof Long) {
                            Long item = (Long) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.DATE:
                                    align = Element.ALIGN_CENTER;
                                    text = simpleDateFormat.format(new Date(item));
                                    break;
                                case ReportViewConstants.ID:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                                case ReportViewConstants.ACTION:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof String) {
                            String item = (String) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.NAME:
                                    align = Element.ALIGN_LEFT;
                                    text = item;
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Double) {
                            Double item = (Double) objects[i - 1][j];
                            switch (dataType[j]) {
                                case ReportViewConstants.AMOUNT:
                                    align = Element.ALIGN_RIGHT;
                                    text = decimalFormat.format(item);
                                    break;
                                case ReportViewConstants.QUANTITY:
                                    align = Element.ALIGN_CENTER;
                                    text = String.valueOf(item);
                                    break;
                            }
                        } else if (objects[i - 1][j] instanceof Integer) {
                            Integer item = (Integer) objects[i - 1][j];
                            align = Element.ALIGN_CENTER;
                            text = String.valueOf(item);

                        }
                        cell.setPhrase(new Phrase(text, bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setHorizontalAlignment(align);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportPriceList(Context context, String path, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.product), context.getString(R.string.price)};
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        for (int i = 0; i < productList.size() + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(values[0]);
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue(values[1]);
            } else {
                Row row = sheet.createRow(i);
                Product product = productList.get(i - 1);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(product.getName() + "(" + product.getSku() + ")");
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue(decimalFormat.format(product.getPrice()));
            }

        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        File file = new File(path, context.getString(R.string.price_list) + ".xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportPriceListToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.product), context.getString(R.string.price)};
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        for (int i = 0; i < productList.size() + 1; i++) {
            if (i == 0) {
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(values[0]);
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue(values[1]);
            } else {
                Row row = sheet.createRow(i);
                Product product = productList.get(i - 1);
                Cell cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(product.getName() + "(" + product.getSku() + ")");
                Cell cell1 = row.createCell(1);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue(decimalFormat.format(product.getPrice()));
            }

        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.price_list) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportPriceListPdf(Context context, String path, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            File file = new File(path, context.getString(R.string.price_list) + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.price_list), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.product), context.getString(R.string.price)};
            List<Product> productList = databaseManager.getAllProducts().blockingSingle();
            float[] columnWidths = {10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < productList.size() + 1; j++) {
                if (j == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(values[0], bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(values[1], bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                } else {
                    Product product = productList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(product.getName() + "(" + product.getSku() + ")", bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(decimalFormat.format(product.getPrice()), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportPriceListPdfToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(context.getString(R.string.price_list) + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.price_list), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.product), context.getString(R.string.price)};
            List<Product> productList = databaseManager.getAllProducts().blockingSingle();
            float[] columnWidths = {10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < productList.size() + 1; j++) {
                if (j == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(values[0], bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(values[1], bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                } else {
                    Product product = productList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(product.getName() + "(" + product.getSku() + ")", bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(decimalFormat.format(product.getPrice()), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportVendors(Context context, String path, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address), context.getString(R.string.contacts)};
        List<Vendor> vendorList = databaseManager.getVendors().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < vendorList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Vendor vendor = vendorList.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(vendor.getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(vendor.getContactName());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(vendor.getAddress());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            String contacts = "";
            if (vendor.getContacts().size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < vendor.getContacts().size(); j++) {
                    if (j == vendor.getContacts().size() - 1)
                        builder.append(vendor.getContacts().get(j).getName());
                    else
                        builder.append(vendor.getContacts().get(j).getName()).append("\n");
                }
                contacts = builder.toString();
            }
            cell3.setCellValue(contacts);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        File file = new File(path, context.getString(R.string.vendors) + ".xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportVendorsToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address), context.getString(R.string.contacts)};
        List<Vendor> vendorList = databaseManager.getVendors().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < vendorList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Vendor vendor = vendorList.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(vendor.getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(vendor.getContactName());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(vendor.getAddress());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            String contacts = "";
            if (vendor.getContacts().size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < vendor.getContacts().size(); j++) {
                    if (j == vendor.getContacts().size() - 1)
                        builder.append(vendor.getContacts().get(j).getName());
                    else
                        builder.append(vendor.getContacts().get(j).getName()).append("\n");
                }
                contacts = builder.toString();
            }
            cell3.setCellValue(contacts);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.vendors) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportVendorsPdfToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(context.getString(R.string.vendors) + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.vendors), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address), context.getString(R.string.contacts)};
            List<Vendor> vendorList = databaseManager.getVendors().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < vendorList.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Vendor vendor = vendorList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(vendor.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(vendor.getContactName(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(vendor.getAddress(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    String contacts = "";
                    if (vendor.getContacts().size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < vendor.getContacts().size(); i++) {
                            if (i == vendor.getContacts().size() - 1)
                                builder.append(vendor.getContacts().get(j).getName());
                            else
                                builder.append(vendor.getContacts().get(j).getName()).append("\n");
                        }
                        contacts = builder.toString();
                    }
                    cell3.setPhrase(new Phrase(contacts, bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    table.addCell(cell3);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportVendorsPdf(Context context, String root, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            File file = new File(root, context.getString(R.string.vendors) + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.vendors), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address), context.getString(R.string.contacts)};
            List<Vendor> vendorList = databaseManager.getVendors().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < vendorList.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Vendor vendor = vendorList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(vendor.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(vendor.getContactName(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(vendor.getAddress(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    String contacts = "";
                    if (vendor.getContacts().size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < vendor.getContacts().size(); i++) {
                            if (i == vendor.getContacts().size() - 1)
                                builder.append(vendor.getContacts().get(j).getName());
                            else
                                builder.append(vendor.getContacts().get(j).getName()).append("\n");
                        }
                        contacts = builder.toString();
                    }
                    cell3.setPhrase(new Phrase(contacts, bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    table.addCell(cell3);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCustomers(Context context, String path, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.customers));
        String[] values = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
        List<Customer> customers = databaseManager.getCustomers().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell name = row.createCell(i);
            name.setCellStyle(cellStyle);
            name.setCellValue(values[i]);
        }
        for (int i = 0; i < customers.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Customer customer = customers.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(customer.getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(customer.getPhoneNumber());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(customer.getAddress());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(customer.getQrCode());
            Cell cell4 = row1.createCell(4);
            cell4.setCellStyle(cellStyle);
            String groups = "";
            if (customer.getCustomerGroups().size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < customer.getCustomerGroups().size(); j++) {
                    if (j == customer.getCustomerGroups().size() - 1)
                        builder.append(customer.getCustomerGroups().get(j).getName());
                    else
                        builder.append(customer.getCustomerGroups().get(j).getName()).append("\n");
                }
                groups = builder.toString();
            }
            cell4.setCellValue(groups);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        File file = new File(path, context.getString(R.string.customers) + ".xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportCustomersPdf(Context context, String path, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            File file = new File(path, context.getString(R.string.customers) + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.customers), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
            List<Customer> customers = databaseManager.getCustomers().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < customers.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Customer customer = customers.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(customer.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(customer.getPhoneNumber(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(customer.getAddress(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    String groups = "";
                    if (customer.getCustomerGroups().size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < customer.getCustomerGroups().size(); i++) {
                            if (i == customer.getCustomerGroups().size() - 1)
                                builder.append(customer.getCustomerGroups().get(i).getName());
                            else
                                builder.append(customer.getCustomerGroups().get(i).getName()).append("\n");
                        }
                        groups = builder.toString();
                    }
                    cell3.setPhrase(new Phrase(groups, bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    PdfPCell cell4 = new PdfPCell();
                    cell4.setPhrase(new Phrase(customer.getQrCode(), bf12));
                    cell4.setPaddingLeft(8);
                    cell4.setPaddingRight(8);
                    cell4.setPaddingTop(8);
                    cell4.setPaddingBottom(8);
                    cell4.setColspan(1);
                    table.addCell(cell4);
                    table.addCell(cell3);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCustomersToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.customers));
        String[] values = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
        List<Customer> customers = databaseManager.getCustomers().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell name = row.createCell(i);
            name.setCellStyle(cellStyle);
            name.setCellValue(values[i]);
        }
        for (int i = 0; i < customers.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Customer customer = customers.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(customer.getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(customer.getPhoneNumber());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(customer.getAddress());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(customer.getQrCode());
            Cell cell4 = row1.createCell(4);
            cell4.setCellStyle(cellStyle);
            String groups = "";
            if (customer.getCustomerGroups().size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < customer.getCustomerGroups().size(); j++) {
                    if (j == customer.getCustomerGroups().size() - 1)
                        builder.append(customer.getCustomerGroups().get(j).getName());
                    else
                        builder.append(customer.getCustomerGroups().get(j).getName()).append("\n");
                }
                groups = builder.toString();
            }
            cell4.setCellValue(groups);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.customers) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCustomersPdfToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(context.getString(R.string.customers) + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.customers), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
            List<Customer> customers = databaseManager.getCustomers().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < customers.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Customer customer = customers.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(customer.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(customer.getPhoneNumber(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(customer.getAddress(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    String groups = "";
                    if (customer.getCustomerGroups().size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < customer.getCustomerGroups().size(); i++) {
                            if (i == customer.getCustomerGroups().size() - 1)
                                builder.append(customer.getCustomerGroups().get(i).getName());
                            else
                                builder.append(customer.getCustomerGroups().get(i).getName()).append("\n");
                        }
                        groups = builder.toString();
                    }
                    cell3.setPhrase(new Phrase(groups, bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    PdfPCell cell4 = new PdfPCell();
                    cell4.setPhrase(new Phrase(customer.getQrCode(), bf12));
                    cell4.setPaddingLeft(8);
                    cell4.setPaddingRight(8);
                    cell4.setPaddingTop(8);
                    cell4.setPaddingBottom(8);
                    cell4.setColspan(1);
                    table.addCell(cell4);
                    table.addCell(cell3);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportCustomersTemplate(Context context, String path) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] titles = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
        String[] values = {"John Smith", "+7 859 623 1523", "Baker Street 45, London, UK", "6901443182183", "VIP, Family"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titles[i]);
        }
        Row row1 = sheet.createRow(1);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        File file = new File(path, context.getString(R.string.customers_template) + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportCustomersTemplateToUsb(Context context, UsbFile root) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] titles = {context.getString(R.string.customer_name), context.getString(R.string.phone), context.getString(R.string.address), context.getString(R.string.barcode), context.getString(R.string.customer_groups)};
        String[] values = {"John Smith", "+7 859 623 1523", "Baker Street 45, London, UK", "6901443182183", "VIP, Family"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(titles[i]);
        }
        Row row1 = sheet.createRow(1);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < titles.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.customers_template) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importCustomers(Context context, String path, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {
            File file = new File(path);
            FileInputStream myInput = new FileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet customerSheet = myWorkBook.getSheetAt(0);
            String[][] customers = new String[customerSheet.getPhysicalNumberOfRows()][5];
            Iterator rowIter = customerSheet.rowIterator();
            int row = 0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    customers[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    customers[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else customers[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 0)
                        Toast.makeText(context, context.getString(R.string.customers_imported), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < customers.length; i++) {
                        if (i != 0)
                            if (!customers[i][0].isEmpty()) {
                                String group = customers[i][4];
                                String name = customers[i][0];
                                String phone = customers[i][1];
                                String address = customers[i][2];
                                String barcode = customers[i][3];
                                databaseManager.isCustomerExists(name).subscribe(isExists -> {
                                    if (!isExists) {
                                        Customer customer = new Customer();
                                        customer.setName(name);
                                        customer.setPhoneNumber(phone);
                                        customer.setAddress(address);
                                        customer.setQrCode(barcode);
                                        customer.setActive(true);
                                        List<Customer> customerList = databaseManager.getCustomers().blockingSingle();
                                        if (customerList.isEmpty()) {
                                            customer.setClientId(1L);
                                        } else
                                            customer.setClientId(customerList.get(0).getClientId() + 1);
                                        customer.setCreatedDate(System.currentTimeMillis());
                                        databaseManager.addCustomer(customer).blockingSingle();
                                        if (!group.isEmpty()) {
                                            int count = 0;
                                            for (int j = 0; j < group.length(); j++) {
                                                if (group.charAt(j) == ',') count++;
                                            }
                                            if (count > 0) {
                                                List<String> groups = Arrays.asList(group.split(","));
                                                for (int j = 0; j < groups.size(); j++) {
                                                    String groupName = groups.get(j);
                                                    databaseManager.isCustomerGroupExists(groupName).subscribe(isGroupExists -> {
                                                        if (isGroupExists) {
                                                            CustomerGroup customerGroup = databaseManager.getCustomerGroupByName(groupName).blockingSingle();
                                                            databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe();
                                                        } else {
                                                            CustomerGroup customerGroup = new CustomerGroup();
                                                            customerGroup.setCreatedDate(System.currentTimeMillis());
                                                            customerGroup.setName(groupName);
                                                            customerGroup.setIsActive(true);
                                                            databaseManager.addCustomerGroup(customerGroup).subscribe(aLong -> databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe());
                                                        }
                                                    });
                                                }
                                            } else {
                                                databaseManager.isCustomerGroupExists(group).subscribe(isGroupExists -> {
                                                    if (isGroupExists) {
                                                        CustomerGroup customerGroup = databaseManager.getCustomerGroupByName(group).blockingSingle();
                                                        databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe();
                                                    } else {
                                                        CustomerGroup customerGroup = new CustomerGroup();
                                                        customerGroup.setCreatedDate(System.currentTimeMillis());
                                                        customerGroup.setName(group);
                                                        customerGroup.setIsActive(true);
                                                        databaseManager.addCustomerGroup(customerGroup).subscribe(aLong -> databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe());
                                                    }
                                                });
                                            }

                                        }

                                    }
                                });
                            }
                    }
                    dialog.dismiss();
                    handler.sendEmptyMessage(0);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void importCustomersFromUsb(Context context, UsbFile file, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {
            InputStream myInput = new UsbFileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet customerSheet = myWorkBook.getSheetAt(0);
            String[][] customers = new String[customerSheet.getPhysicalNumberOfRows()][5];
            Iterator rowIter = customerSheet.rowIterator();
            int row = 0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    customers[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    customers[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else customers[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == 0)
                        Toast.makeText(context, context.getString(R.string.customers_imported), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < customers.length; i++) {
                        if (i != 0)
                            if (!customers[i][0].isEmpty()) {
                                String group = customers[i][4];
                                String name = customers[i][0];
                                String phone = customers[i][1];
                                String address = customers[i][2];
                                String barcode = customers[i][3];
                                databaseManager.isCustomerExists(name).subscribe(isExists -> {
                                    if (!isExists) {
                                        Customer customer = new Customer();
                                        customer.setName(name);
                                        customer.setPhoneNumber(phone);
                                        customer.setAddress(address);
                                        customer.setQrCode(barcode);
                                        customer.setActive(true);
                                        List<Customer> customerList = databaseManager.getCustomers().blockingSingle();
                                        if (customerList.isEmpty()) {
                                            customer.setClientId(1L);
                                        } else
                                            customer.setClientId(customerList.get(0).getClientId() + 1);
                                        customer.setCreatedDate(System.currentTimeMillis());
                                        databaseManager.addCustomer(customer).blockingSingle();
                                        if (!group.isEmpty()) {
                                            int count = 0;
                                            for (int j = 0; j < group.length(); j++) {
                                                if (group.charAt(j) == ',') count++;
                                            }
                                            if (count > 0) {
                                                List<String> groups = Arrays.asList(group.split(","));
                                                for (int j = 0; j < groups.size(); j++) {
                                                    String groupName = groups.get(j);
                                                    databaseManager.isCustomerGroupExists(groupName).subscribe(isGroupExists -> {
                                                        if (isGroupExists) {
                                                            CustomerGroup customerGroup = databaseManager.getCustomerGroupByName(groupName).blockingSingle();
                                                            databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe();
                                                        } else {
                                                            CustomerGroup customerGroup = new CustomerGroup();
                                                            customerGroup.setCreatedDate(System.currentTimeMillis());
                                                            customerGroup.setName(groupName);
                                                            customerGroup.setIsActive(true);
                                                            databaseManager.addCustomerGroup(customerGroup).subscribe(aLong -> databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe());
                                                        }
                                                    });
                                                }
                                            } else {
                                                databaseManager.isCustomerGroupExists(group).subscribe(isGroupExists -> {
                                                    if (isGroupExists) {
                                                        CustomerGroup customerGroup = databaseManager.getCustomerGroupByName(group).blockingSingle();
                                                        databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe();
                                                    } else {
                                                        CustomerGroup customerGroup = new CustomerGroup();
                                                        customerGroup.setCreatedDate(System.currentTimeMillis());
                                                        customerGroup.setName(group);
                                                        customerGroup.setIsActive(true);
                                                        databaseManager.addCustomerGroup(customerGroup).subscribe(aLong -> databaseManager.addCustomerToCustomerGroup(customer.getId(), customerGroup.getId()).subscribe());
                                                    }
                                                });
                                            }

                                        }

                                    }
                                });
                            }
                    }
                    dialog.dismiss();
                    handler.sendEmptyMessage(0);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void importVendors(Context context, String name, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {
            File file = new File(name);
            FileInputStream myInput = new FileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet vendorSheet = myWorkBook.getSheetAt(0);
            String[][] vendors = new String[vendorSheet.getPhysicalNumberOfRows()][4];
            Iterator rowIter = vendorSheet.rowIterator();
            int row = 0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    vendors[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    vendors[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else vendors[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(msg -> {
                if (msg.what == 0)
                    Toast.makeText(context, context.getString(R.string.vendors_imported), Toast.LENGTH_SHORT).show();
                return false;
            });
            new Thread(() -> {
                for (int i = 0; i < vendors.length; i++) {
                    if (i != 0) {
                        Vendor vendor = new Vendor();
                        vendor.setName(vendors[i][0]);
                        vendor.setContactName(vendors[i][1]);
                        vendor.setAddress(vendors[i][2]);
                        vendor.setActive(true);
                        vendor.setCreatedDate(System.currentTimeMillis());
                        if (!vendor.getName().isEmpty())
                            databaseManager.isVendorNameExist(vendor.getName()).subscribe(isExist -> {
                                if (!isExist) {
                                    databaseManager.addVendor(vendor).subscribe();
                                }
                            });
                    }
                }
                dialog.dismiss();
                handler.sendEmptyMessage(0);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void importVendorsFromUsb(Context context, UsbFile file, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {
            InputStream myInput = new UsbFileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet vendorSheet = myWorkBook.getSheetAt(0);
            String[][] vendors = new String[vendorSheet.getPhysicalNumberOfRows()][4];
            Iterator rowIter = vendorSheet.rowIterator();
            int row = 0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    vendors[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    vendors[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else vendors[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(msg -> {
                if (msg.what == 0)
                    Toast.makeText(context, context.getString(R.string.vendors_imported), Toast.LENGTH_SHORT).show();
                return false;
            });
            new Thread(() -> {
                for (int i = 0; i < vendors.length; i++) {
                    if (i != 0) {
                        Vendor vendor = new Vendor();
                        vendor.setName(vendors[i][0]);
                        vendor.setContactName(vendors[i][1]);
                        vendor.setAddress(vendors[i][2]);
                        vendor.setActive(true);
                        vendor.setCreatedDate(System.currentTimeMillis());
                        if (!vendor.getName().isEmpty())
                            databaseManager.isVendorNameExist(vendor.getName()).subscribe(isExist -> {
                                if (!isExist) {
                                    databaseManager.addVendor(vendor).subscribe();
                                }
                            });
                    }
                }
                dialog.dismiss();
                handler.sendEmptyMessage(0);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void importProducts(Context context, String name, DatabaseManager databaseManager, RxBus rxBus) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {

            File file = new File(name);
            FileInputStream myInput = new FileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet productSheet = myWorkBook.getSheetAt(0);
            String[][] products = new String[productSheet.getPhysicalNumberOfRows()][8];
            Iterator rowIter1 = productSheet.rowIterator();
            int row = 0;
            while (rowIter1.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter1.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    products[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    products[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else products[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            List<Integer> unitErrors = new ArrayList<>();
            List<Integer> skuErrors = new ArrayList<>();
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setMax(products.length);
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(msg -> {
                if (msg.what == 0) {
                    if (skuErrors.size() != 0 || unitErrors.size() != 0) {
                        UIUtils.showAlert(context, context.getString(R.string.ok),
                                context.getString(R.string.import_errors) + " ",
                                context.getString(R.string.sku_or_product_error_qty) + " " + skuErrors.size() + "\n" +
                                        context.getString(R.string.unit_format_error) + " " + unitErrors.size()
                                , () -> {

                                });
                    } else
                        Toast.makeText(context, context.getString(R.string.products_imported), Toast.LENGTH_SHORT).show();
                    rxBus.send(new ProductEvent(null, GlobalEventConstants.ADD));
                }
                return false;
            });
            new Thread(() -> {
                for (int i = 0; i < products.length; i++) {
                    if (i != 0) {
                        if (!products[i][0].isEmpty()) {
                            String categoryName = products[i][0];
                            String subcategoryName = products[i][1];
                            boolean isExists = databaseManager.isCategoryNameExists(categoryName).blockingSingle();
                            if (!isExists) {
                                Category category = new Category();
                                category.setActive(true);
                                category.setCreatedDate(System.currentTimeMillis());
                                category.setName(categoryName);
                                databaseManager.addCategory(category).subscribe();
                                Category subcategory = new Category();
                                subcategory.setName(subcategoryName);
                                subcategory.setCreatedDate(System.currentTimeMillis());
                                subcategory.setActive(true);
                                subcategory.setParentId(category.getId());
                                databaseManager.addCategory(subcategory).subscribe();
                                boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                if (!isSkuExists) {
                                    Product product = new Product();
                                    product.setName(products[i][2]);
                                    product.setBarcode(products[i][3]);
                                    product.setSku(products[i][4]);
                                    product.setPrice(Double.valueOf(products[i][6]));
                                    product.setActive(true);
                                    product.setIsDeleted(false);
                                    product.setNotModifyted(true);
                                    product.setPhotoPath("");
                                    product.setCreatedDate(System.currentTimeMillis());
                                    product.setCategoryId(subcategory.getId());
                                    product.setPriceCurrency(databaseManager.getMainCurrency());
                                    List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                    int count = 0;
                                    for (UnitCategory unitCategory : tempUnitCategories) {
                                        for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                            if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                product.setMainUnit(unitCategory.getUnits().get(j));
                                                product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                count = 1;
                                            }
                                        }
                                    }
                                    if (count == 0) {
                                        unitErrors.add(i);
                                        continue;
                                    }
                                } else {
                                    skuErrors.add(i);
                                }
                            } else {
                                Category category = databaseManager.getCategoryByName(categoryName).blockingSingle();
                                boolean isSubcategoryExists = databaseManager.isSubCategoryNameExists(category.getName(), subcategoryName).blockingSingle();
                                if (isSubcategoryExists) {
                                    Category subcategory = databaseManager.getSubCategoryByName(subcategoryName, category.getId()).blockingSingle();
                                    boolean isProductNameExits = databaseManager.isProductNameExists(products[i][1], subcategory.getId()).blockingSingle();
                                    boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                    if (!isProductNameExits && !isSkuExists) {
                                        Product product = new Product();
                                        product.setName(products[i][2]);
                                        product.setBarcode(products[i][3]);
                                        product.setSku(products[i][4]);
                                        product.setPhotoPath("");
                                        product.setPrice(Double.valueOf(products[i][6]));
                                        product.setActive(true);
                                        product.setIsDeleted(false);
                                        product.setNotModifyted(true);
                                        product.setCreatedDate(System.currentTimeMillis());
                                        product.setCategoryId(subcategory.getId());
                                        product.setPriceCurrency(databaseManager.getMainCurrency());
                                        List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                        int count = 0;
                                        for (UnitCategory unitCategory : tempUnitCategories) {
                                            for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                                if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                    product.setMainUnit(unitCategory.getUnits().get(j));
                                                    product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                    count = 1;
                                                }
                                            }
                                        }
                                        if (count == 0) {
                                            unitErrors.add(i);
                                            continue;
                                        }
                                    } else {
                                        skuErrors.add(i);
                                    }
                                } else {
                                    Category subcategory = new Category();
                                    subcategory.setName(subcategoryName);
                                    subcategory.setCreatedDate(System.currentTimeMillis());
                                    subcategory.setActive(true);
                                    subcategory.setParentId(category.getId());
                                    databaseManager.addCategory(subcategory).subscribe();
                                    boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                    if (!isSkuExists) {
                                        Product product = new Product();
                                        product.setName(products[i][2]);
                                        product.setBarcode(products[i][3]);
                                        product.setPhotoPath("");
                                        product.setSku(products[i][4]);
                                        product.setActive(true);
                                        product.setIsDeleted(false);
                                        product.setNotModifyted(true);
                                        product.setCreatedDate(System.currentTimeMillis());
                                        product.setCategoryId(subcategory.getId());
                                        product.setPrice(Double.valueOf(products[i][6]));
                                        product.setPriceCurrency(databaseManager.getMainCurrency());
                                        List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                        int count = 0;
                                        for (UnitCategory unitCategory : tempUnitCategories) {
                                            for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                                if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                    product.setMainUnit(unitCategory.getUnits().get(j));
                                                    product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                    count = 1;
                                                }
                                            }
                                        }
                                        if (count == 0) {
                                            unitErrors.add(i);
                                            continue;
                                        }
                                    } else {
                                        skuErrors.add(i);
                                    }
                                }
                            }
                        }
                    }
                }
                dialog.dismiss();
                handler.sendEmptyMessage(0);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void importProductsFromUsb(Context context, UsbFile file, DatabaseManager databaseManager, RxBus rxBus) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        try {
            InputStream myInput = new UsbFileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet productSheet = myWorkBook.getSheetAt(0);
            String[][] products = new String[productSheet.getPhysicalNumberOfRows()][100];
            Iterator rowIter1 = productSheet.rowIterator();
            int row = 0;
            while (rowIter1.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter1.next();
                int cell = 0;
                if (row != 0) {
                    for (int i = 0; i < myRow.getLastCellNum(); i++) {
                        HSSFCell myCell = myRow.getCell(i);
                        if (myCell != null) {
                            switch (myCell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    products[row][cell] = myCell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    products[row][cell] = String.valueOf(myCell.getNumericCellValue());
                                    break;
                            }
                        } else products[row][cell] = "";
                        cell++;
                    }
                }
                row++;
            }
            List<Integer> unitErrors = new ArrayList<>();
            List<Integer> skuErrors = new ArrayList<>();
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.importing));
            dialog.setMax(products.length);
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            Handler handler = new Handler(msg -> {
                if (msg.what == 0) {
                    if (skuErrors.size() != 0 || unitErrors.size() != 0) {
                        UIUtils.showAlert(context, context.getString(R.string.ok),
                                context.getString(R.string.import_errors) + " ",
                                context.getString(R.string.sku_or_product_error_qty) + " " + skuErrors.size() + "\n" +
                                        context.getString(R.string.unit_format_error) + " " + unitErrors.size()
                                , () -> {

                                });
                    } else
                        Toast.makeText(context, context.getString(R.string.products_imported), Toast.LENGTH_SHORT).show();
                    rxBus.send(new ProductEvent(null, GlobalEventConstants.ADD));
                }
                return false;
            });
            new Thread(() -> {
                for (int i = 0; i < products.length; i++) {
                    if (i != 0) {
                        if (!products[i][0].isEmpty()) {
                            String categoryName = products[i][0];
                            String subcategoryName = products[i][1];
                            boolean isExists = databaseManager.isCategoryNameExists(categoryName).blockingSingle();
                            if (!isExists) {
                                Category category = new Category();
                                category.setActive(true);
                                category.setCreatedDate(System.currentTimeMillis());
                                category.setName(categoryName);
                                databaseManager.addCategory(category).subscribe();
                                Category subcategory = new Category();
                                subcategory.setName(subcategoryName);
                                subcategory.setCreatedDate(System.currentTimeMillis());
                                subcategory.setActive(true);
                                subcategory.setParentId(category.getId());
                                databaseManager.addCategory(subcategory).subscribe();
                                boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                if (!isSkuExists) {
                                    Product product = new Product();
                                    product.setName(products[i][2]);
                                    product.setBarcode(products[i][3]);
                                    product.setSku(products[i][4]);
                                    product.setPrice(Double.valueOf(products[i][6]));
                                    product.setActive(true);
                                    product.setIsDeleted(false);
                                    product.setNotModifyted(true);
                                    product.setPhotoPath("");
                                    product.setCreatedDate(System.currentTimeMillis());
                                    product.setCategoryId(subcategory.getId());
                                    product.setPriceCurrency(databaseManager.getMainCurrency());
                                    List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                    int count = 0;
                                    for (UnitCategory unitCategory : tempUnitCategories) {
                                        for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                            if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                product.setMainUnit(unitCategory.getUnits().get(j));
                                                product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                count = 1;
                                            }
                                        }
                                    }
                                    if (count == 0) {
                                        unitErrors.add(i);
                                        continue;
                                    }
                                } else {
                                    skuErrors.add(i);
                                }
                            } else {
                                Category category = databaseManager.getCategoryByName(categoryName).blockingSingle();
                                boolean isSubcategoryExists = databaseManager.isSubCategoryNameExists(category.getName(), subcategoryName).blockingSingle();
                                if (isSubcategoryExists) {
                                    Category subcategory = databaseManager.getSubCategoryByName(subcategoryName, category.getId()).blockingSingle();
                                    boolean isProductNameExits = databaseManager.isProductNameExists(products[i][1], subcategory.getId()).blockingSingle();
                                    boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                    if (!isProductNameExits && !isSkuExists) {
                                        Product product = new Product();
                                        product.setName(products[i][2]);
                                        product.setBarcode(products[i][3]);
                                        product.setSku(products[i][4]);
                                        product.setPhotoPath("");
                                        product.setPrice(Double.valueOf(products[i][6]));
                                        product.setActive(true);
                                        product.setIsDeleted(false);
                                        product.setNotModifyted(true);
                                        product.setCreatedDate(System.currentTimeMillis());
                                        product.setCategoryId(subcategory.getId());
                                        product.setPriceCurrency(databaseManager.getMainCurrency());
                                        List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                        int count = 0;
                                        for (UnitCategory unitCategory : tempUnitCategories) {
                                            for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                                if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                    product.setMainUnit(unitCategory.getUnits().get(j));
                                                    product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                    count = 1;
                                                }
                                            }
                                        }
                                        if (count == 0) {
                                            unitErrors.add(i);
                                            continue;
                                        }
                                    } else {
                                        skuErrors.add(i);
                                    }
                                } else {
                                    Category subcategory = new Category();
                                    subcategory.setName(subcategoryName);
                                    subcategory.setCreatedDate(System.currentTimeMillis());
                                    subcategory.setActive(true);
                                    subcategory.setParentId(category.getId());
                                    databaseManager.addCategory(subcategory).subscribe();
                                    boolean isSkuExists = databaseManager.isProductSkuExists(products[i][4], subcategory.getId()).blockingGet();
                                    if (!isSkuExists) {
                                        Product product = new Product();
                                        product.setName(products[i][2]);
                                        product.setBarcode(products[i][3]);
                                        product.setPhotoPath("");
                                        product.setSku(products[i][4]);
                                        product.setActive(true);
                                        product.setIsDeleted(false);
                                        product.setNotModifyted(true);
                                        product.setCreatedDate(System.currentTimeMillis());
                                        product.setCategoryId(subcategory.getId());
                                        product.setPrice(Double.valueOf(products[i][6]));
                                        product.setPriceCurrency(databaseManager.getMainCurrency());
                                        List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                                        int count = 0;
                                        for (UnitCategory unitCategory : tempUnitCategories) {
                                            for (int j = 0; j < unitCategory.getUnits().size(); j++) {
                                                if (unitCategory.getUnits().get(j).getAbbr().toUpperCase().equals(products[i][5].toUpperCase())) {
                                                    product.setMainUnit(unitCategory.getUnits().get(j));
                                                    product.setMainUnitId(unitCategory.getUnits().get(j).getId());
                                                    count = 1;
                                                }
                                            }
                                        }
                                        if (count == 0) {
                                            unitErrors.add(i);
                                            continue;
                                        }
                                    } else {
                                        skuErrors.add(i);
                                    }
                                }
                            }
                        }
                    }
                }
                dialog.dismiss();
                handler.sendEmptyMessage(0);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.incorrect_file), Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportProductVendorTemplateToUsb(Context context, UsbFile root) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address)};
        String[] vendors = {"Louis and Co", "Louis Smith", "London, UK"};
        for (int i = 0; i < values.length; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        Row row2 = sheet.createRow(1);
        for (int i = 0; i < vendors.length; i++) {
            Cell cell = row2.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(vendors[i]);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        Workbook workbook1 = new HSSFWorkbook();
        CellStyle cellStyle1 = workbook1.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setDataFormat(format.getFormat("@"));
        Sheet sheet1 = workbook1.createSheet(context.getString(R.string.products));
        String[] labels = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.unit), context.getString(R.string.price)};
        String[] products = new String[]{"Food", "Vegatables", "Tomato", "1465442213", "tomato", "kg", "256.99"};
        for (int i = 0; i < products.length; i++) {
            sheet1.setDefaultColumnStyle(i, cellStyle1);
        }
        Row row1 = sheet1.createRow(0);
        for (int i = 0; i < labels.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(labels[i]);
        }
        Row row3 = sheet1.createRow(1);
        for (int i = 0; i < products.length; i++) {
            Cell cell = row3.createCell(i);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(products[i]);
        }
        for (int i = 0; i < labels.length; i++) {
            sheet1.setColumnWidth(i, 10 * 500);
        }
        Sheet sheet2 = workbook1.createSheet(context.getString(R.string.unit));
        Row row4 = sheet2.createRow(0);
        Cell cell1 = row4.createCell(0);
        cell1.setCellStyle(cellStyle1);
        cell1.setCellValue(context.getString(R.string.available_units));
        String[] units = new String[]{"pcs - piece", "kg - kilogram", "gr - gram", "tn - ton", "m - meter", "mm - millimeter", "cm - centimeter", "km - kilometer", "m2 - M2", "OnItemClickListener - are", "ac - acre", "ha - hectare", "m3 - M3", "l - liter", "ml - milliliter"};
        String[] unitsRU = new String[]{"шт. - штук", "кг. - килограмм", "гр. - грамм", "т. - тонна", "м. - метр", "мм. - миллиметр", "см. - сантиметр", "км. - километр", "м2. - М2", "сот. - сотка", "акр. - акр", "га. - гектар", "м3. - М3", "л. - литр", "мл. - миллилитр"};
        for (int i = 0; i < units.length; i++) {
            Row row5 = sheet2.createRow(1 + i);
            Cell cell = row5.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(units[i]);
            Cell cell2 = row5.createCell(1);
            cell2.setCellStyle(cellStyle1);
            cell2.setCellValue(unitsRU[i]);
        }
        for (int i = 0; i < units.length; i++) {
            sheet2.setColumnWidth(i, 12 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.products_vendor_template) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook1.write(os);
            UsbFile file2 = root.createFile(context.getString(R.string.vendor_template) + ".xls");
            OutputStream os2 = new UsbFileOutputStream(file2);
            workbook.write(os2);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportProductVendorTemplate(Context context, String path) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = {context.getString(R.string.vendor_name), context.getString(R.string.contact_name), context.getString(R.string.address)};
        String[] vendors = {"Louis and Co", "Louis Smith", "London, UK"};
        for (int i = 0; i < values.length; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        Row row2 = sheet.createRow(1);
        for (int i = 0; i < vendors.length; i++) {
            Cell cell = row2.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(vendors[i]);
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        Workbook workbook1 = new HSSFWorkbook();
        CellStyle cellStyle1 = workbook1.createCellStyle();
        cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setDataFormat(format.getFormat("@"));
        Sheet sheet1 = workbook1.createSheet(context.getString(R.string.products));
        String[] labels = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.unit), context.getString(R.string.price)};
        String[] products = new String[]{"Food", "Vegatables", "Tomato", "1465442213", "tomato", "kg", "256.99"};
        for (int i = 0; i < products.length; i++) {
            sheet1.setDefaultColumnStyle(i, cellStyle1);
        }
        Row row1 = sheet1.createRow(0);
        for (int i = 0; i < labels.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(labels[i]);
        }
        Row row3 = sheet1.createRow(1);
        for (int i = 0; i < products.length; i++) {
            Cell cell = row3.createCell(i);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(products[i]);
        }
        for (int i = 0; i < labels.length; i++) {
            sheet1.setColumnWidth(i, 10 * 500);
        }
        Sheet sheet2 = workbook1.createSheet(context.getString(R.string.unit));
        Row row4 = sheet2.createRow(0);
        Cell cell1 = row4.createCell(0);
        cell1.setCellStyle(cellStyle1);
        cell1.setCellValue(context.getString(R.string.available_units));
        String[] units = new String[]{"pcs - piece", "kg - kilogram", "gr - gram", "tn - ton", "m - meter", "mm - millimeter", "cm - centimeter", "km - kilometer", "m2 - M2", "OnItemClickListener - are", "ac - acre", "ha - hectare", "m3 - M3", "l - liter", "ml - milliliter"};
        String[] unitsRU = new String[]{"шт. - штук", "кг. - килограмм", "гр. - грамм", "т. - тонна", "м. - метр", "мм. - миллиметр", "см. - сантиметр", "км. - километр", "м2. - М2", "сот. - сотка", "акр. - акр", "га. - гектар", "м3. - М3", "л. - литр", "мл. - миллилитр"};
        for (int i = 0; i < units.length; i++) {
            Row row5 = sheet2.createRow(1 + i);
            Cell cell = row5.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell.setCellValue(units[i]);
            Cell cell2 = row5.createCell(1);
            cell2.setCellStyle(cellStyle1);
            cell2.setCellValue(unitsRU[i]);
        }
        for (int i = 0; i < units.length; i++) {
            sheet2.setColumnWidth(i, 12 * 500);
        }
        File file = new File(path, context.getString(R.string.products_vendor_template) + ".xls");
        File file2 = new File(path, context.getString(R.string.vendor_template) + ".xls");
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook1.write(fileOutputStream);
            fileOutputStream = new FileOutputStream(file2);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportProducts(Context context, String path, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.unit), context.getString(R.string.price)};
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < productList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Product product = productList.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(product.getCategory().getParentCategory().getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(product.getCategory().getName());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(product.getName());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(product.getBarcode());
            Cell cell4 = row1.createCell(4);
            cell4.setCellStyle(cellStyle);
            cell4.setCellValue(product.getSku());
            Cell cell6 = row1.createCell(5);
            cell6.setCellStyle(cellStyle);
            cell6.setCellValue(product.getMainUnit().getAbbr());
            Cell cell7 = row1.createCell(6);
            cell7.setCellStyle(cellStyle);
            cell7.setCellValue(product.getPrice());
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        File file = new File(path, context.getString(R.string.products) + ".xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportProductsToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return;
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setDataFormat(format.getFormat("@"));
        Sheet sheet = workbook.createSheet(context.getString(R.string.vendors));
        String[] values = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.unit), context.getString(R.string.price)};
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        Row row = sheet.createRow(0);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(values[i]);
        }
        for (int i = 0; i < productList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Product product = productList.get(i);
            Cell cell = row1.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(product.getCategory().getParentCategory().getName());
            Cell cell1 = row1.createCell(1);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue(product.getCategory().getName());
            Cell cell2 = row1.createCell(2);
            cell2.setCellStyle(cellStyle);
            cell2.setCellValue(product.getName());
            Cell cell3 = row1.createCell(3);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(product.getBarcode());
            Cell cell4 = row1.createCell(4);
            cell4.setCellStyle(cellStyle);
            cell4.setCellValue(product.getSku());
            Cell cell6 = row1.createCell(5);
            cell6.setCellStyle(cellStyle);
            cell6.setCellValue(product.getMainUnit().getAbbr());
            Cell cell7 = row1.createCell(6);
            cell7.setCellStyle(cellStyle);
            cell7.setCellValue(product.getPrice());
        }
        for (int i = 0; i < values.length; i++) {
            sheet.setColumnWidth(i, 10 * 500);
        }
        try {
            UsbFile file = root.createFile(context.getString(R.string.products) + ".xls");
            OutputStream os = new UsbFileOutputStream(file);
            workbook.write(os);
            os.close();
            Toast.makeText(context, R.string.successfully_exported_to_excel, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportProductsPdf(Context context, String path, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            File file = new File(path, context.getString(R.string.products) + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.products), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.vendor), context.getString(R.string.unit), context.getString(R.string.price)};
            List<Product> productList = databaseManager.getAllProducts().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10, 10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < productList.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Product product = productList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(product.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(product.getCategory().getParentCategory().getName(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(product.getCategory().getName(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    cell3.setPhrase(new Phrase(product.getName(), bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    table.addCell(cell3);
                    PdfPCell cell4 = new PdfPCell();
                    cell4.setPhrase(new Phrase(product.getBarcode(), bf12));
                    cell4.setPaddingLeft(8);
                    cell4.setPaddingRight(8);
                    cell4.setPaddingTop(8);
                    cell4.setPaddingBottom(8);
                    cell4.setColspan(1);
                    table.addCell(cell4);
                    PdfPCell cell5 = new PdfPCell();
                    cell5.setPhrase(new Phrase(product.getSku(), bf12));
                    cell5.setPaddingLeft(8);
                    cell5.setPaddingRight(8);
                    cell5.setPaddingTop(8);
                    cell5.setPaddingBottom(8);
                    cell5.setColspan(1);
                    table.addCell(cell5);
                    PdfPCell cell7 = new PdfPCell();
                    cell7.setPhrase(new Phrase(product.getMainUnit().getAbbr(), bf12));
                    cell7.setPaddingLeft(8);
                    cell7.setPaddingRight(8);
                    cell7.setPaddingTop(8);
                    cell7.setPaddingBottom(8);
                    cell7.setColspan(1);
                    table.addCell(cell7);
                    PdfPCell cell8 = new PdfPCell();
                    cell8.setPhrase(new Phrase(String.valueOf(product.getPrice()), bf12));
                    cell8.setPaddingLeft(8);
                    cell8.setPaddingRight(8);
                    cell8.setPaddingTop(8);
                    cell8.setPaddingBottom(8);
                    cell8.setColspan(1);
                    table.addCell(cell8);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportProductsPdfToUsb(Context context, UsbFile root, DatabaseManager databaseManager) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            decimalFormat.setDecimalFormatSymbols(symbols);

            String FONT = "assets/fonts/Roboto-Regular.ttf";
            BaseFont baseFont;
            baseFont = BaseFont.createFont(FONT, "Cp1251", BaseFont.EMBEDDED);
            Font bf12 = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 10, 10);
            UsbFile file = root.createFile(context.getString(R.string.products) + ".pdf");
            OutputStream os = new UsbFileOutputStream(file);
            PdfWriter.getInstance(document, os);
            Toast.makeText(context, R.string.successfully_exported_to_pdf, Toast.LENGTH_SHORT).show();
            document.open();
            Paragraph reportName = new Paragraph(context.getString(R.string.products), bf12);
            reportName.setAlignment(Element.ALIGN_CENTER);
            document.add(reportName);
            Paragraph empty = new Paragraph(" ");
            document.add(empty);
            String[] values = new String[]{context.getString(R.string.category), context.getString(R.string.subcategory), context.getString(R.string.product_name), context.getString(R.string.barcode), context.getString(R.string.sku), context.getString(R.string.vendor), context.getString(R.string.unit), context.getString(R.string.price)};
            List<Product> productList = databaseManager.getAllProducts().blockingSingle();
            float[] columnWidths = {10, 10, 10, 10, 10, 10, 10, 10};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            for (int j = 0; j < productList.size() + 1; j++) {
                if (j == 0) {
                    for (int i = 0; i < values.length; i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(values[i], bf12));
                        cell.setPaddingLeft(8);
                        cell.setPaddingRight(8);
                        cell.setPaddingTop(8);
                        cell.setPaddingBottom(8);
                        cell.setColspan(1);
                        table.addCell(cell);
                    }
                } else {
                    Product product = productList.get(j - 1);
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(product.getName(), bf12));
                    cell.setPaddingLeft(8);
                    cell.setPaddingRight(8);
                    cell.setPaddingTop(8);
                    cell.setPaddingBottom(8);
                    cell.setColspan(1);
                    table.addCell(cell);
                    PdfPCell cell1 = new PdfPCell();
                    cell1.setPhrase(new Phrase(product.getCategory().getParentCategory().getName(), bf12));
                    cell1.setPaddingLeft(8);
                    cell1.setPaddingRight(8);
                    cell1.setPaddingTop(8);
                    cell1.setPaddingBottom(8);
                    cell1.setColspan(1);
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell();
                    cell2.setPhrase(new Phrase(product.getCategory().getName(), bf12));
                    cell2.setPaddingLeft(8);
                    cell2.setPaddingRight(8);
                    cell2.setPaddingTop(8);
                    cell2.setPaddingBottom(8);
                    cell2.setColspan(1);
                    table.addCell(cell2);
                    PdfPCell cell3 = new PdfPCell();
                    cell3.setPhrase(new Phrase(product.getName(), bf12));
                    cell3.setPaddingLeft(8);
                    cell3.setPaddingRight(8);
                    cell3.setPaddingTop(8);
                    cell3.setPaddingBottom(8);
                    cell3.setColspan(1);
                    table.addCell(cell3);
                    PdfPCell cell4 = new PdfPCell();
                    cell4.setPhrase(new Phrase(product.getBarcode(), bf12));
                    cell4.setPaddingLeft(8);
                    cell4.setPaddingRight(8);
                    cell4.setPaddingTop(8);
                    cell4.setPaddingBottom(8);
                    cell4.setColspan(1);
                    table.addCell(cell4);
                    PdfPCell cell5 = new PdfPCell();
                    cell5.setPhrase(new Phrase(product.getSku(), bf12));
                    cell5.setPaddingLeft(8);
                    cell5.setPaddingRight(8);
                    cell5.setPaddingTop(8);
                    cell5.setPaddingBottom(8);
                    cell5.setColspan(1);
                    table.addCell(cell5);
                    PdfPCell cell7 = new PdfPCell();
                    cell7.setPhrase(new Phrase(product.getMainUnit().getAbbr(), bf12));
                    cell7.setPaddingLeft(8);
                    cell7.setPaddingRight(8);
                    cell7.setPaddingTop(8);
                    cell7.setPaddingBottom(8);
                    cell7.setColspan(1);
                    table.addCell(cell7);
                    PdfPCell cell8 = new PdfPCell();
                    cell8.setPhrase(new Phrase(String.valueOf(product.getPrice()), bf12));
                    cell8.setPaddingLeft(8);
                    cell8.setPaddingRight(8);
                    cell8.setPaddingTop(8);
                    cell8.setPaddingBottom(8);
                    cell8.setColspan(1);
                    table.addCell(cell8);
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

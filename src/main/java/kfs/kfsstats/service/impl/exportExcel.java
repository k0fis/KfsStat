package kfs.kfsstats.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;
import kfs.kfsstats.domain.*;
import kfs.kfsstats.utils.KfsStatException;

/**
 *
 * @author PaveDrim
 */
public class exportExcel {

    private final String dateCaption;
    private final SimpleDateFormat df;
    private final WritableWorkbook wb;
    private final WritableCellFormat headLineformat;
    private final WritableCellFormat titleFormat;
    private int shInx;

    public exportExcel(File outputFile, String dateFormat, String dateCaption) {
        this.dateCaption = dateCaption;
        df = new SimpleDateFormat(dateFormat);
        this.shInx = 1;
        WorkbookSettings wset = new WorkbookSettings();
        try {
            this.wb = Workbook.createWorkbook(outputFile, wset);
        } catch (IOException ex) {
            throw new KfsStatException("Cannot init workbook", ex);
        }

        titleFormat = new WritableCellFormat();
        titleFormat.setFont(new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD));
        headLineformat = new WritableCellFormat();
        headLineformat.setFont(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
    }

    public void done() {
        try {
            this.wb.write();
            this.wb.close();
        } catch (IOException | WriteException ex) {
            throw new KfsStatException("Cannot write workbook", ex);
        }
    }

    public void add(KfsStatSet set) {
        WritableSheet ws = this.wb.createSheet(set.getName(), shInx++);
        int row = 0;
        try {
            ws.addCell(new Label(0, row++, set.getName(), titleFormat));
            row++;
            ws.addCell(new Label(0, row, dateCaption));
            ws.addCell(new Label(1, row++, df.format(new Date())));
            row++; // empty line
            ws.addCell(new Label(0, row, set.getPeriodCaption(), headLineformat));
            ws.addCell(new Label(1, row++, set.getValueCaption(), headLineformat));
            for (KfsStatValue val : set.getLst()) {
                ws.addCell(new Label(0, row, val.getPeriod()));
                if (val.getValue() != null) {
                    ws.addCell(new Label(0, row++, val.getValue().toString()));
                }
            }

        } catch (WriteException ex) {
            throw new KfsStatException("Cannot write into workbook", ex);
        }
        row++;

    }

}

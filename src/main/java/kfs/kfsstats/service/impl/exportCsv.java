package kfs.kfsstats.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import kfs.kfsstats.domain.KfsStatSet;
import kfs.kfsstats.domain.KfsStatValue;
import kfs.kfsstats.utils.KfsStatException;

/**
 *
 * @author pavedrim
 */
public class exportCsv {

    private Writer w;
    public static final char CR = '\n';
    public static final char LF = '\r';
    private final char separator;
    private final char quotechar;
    private final String quotestr;
    private final String dquotestr;
    private final String newLine;
    private boolean added;

    public exportCsv(File fileanme) throws UnsupportedEncodingException, FileNotFoundException {
        this(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileanme), "UTF-8")));
    }

    public exportCsv(Writer w) {
        this(w, ',', '"', "\n");
    }

    public exportCsv(Writer w, char separator) {
        this(w, separator, '"', "\n");
    }

    public exportCsv(File fileanme, char separator, char quotechar) throws UnsupportedEncodingException, FileNotFoundException {
       this(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileanme), "UTF-8")), 
               separator, quotechar, "\n"); 
    }
    public exportCsv(Writer w, char separator, char quotechar, String newLine) {
        this.w = w;
        this.separator = separator;
        this.quotechar = quotechar;
        this.newLine = newLine;
        this.added = false;
        this.quotestr = "" + quotechar;
        this.dquotestr = "" + quotechar + quotechar;
    }

    public void done() {
        try {
            w.flush();
            w.close();
        } catch (IOException ex) {
            throw new KfsStatException("Cannot close csv file", ex);
        }
    }

    protected boolean useQuotes(String cell) {
        return (cell.indexOf(this.separator) >= 0) ||//
                (cell.indexOf(this.quotechar) >= 0) || //
                (cell.indexOf(CR) >= 0) || //
                (cell.indexOf(LF) >= 0)//
                ;
    }

    public exportCsv addCell() {
        internalAdd();
        return this;
    }

    public exportCsv addCell(Object cell) {
        return (cell != null) ? addCell(cell.toString()) : addCell();
    }

    public exportCsv addCell(String cell) {
        if (cell == null) {
            return addCell();
        }
        if (useQuotes(cell)) {
            return addQuotedCell(cell);
        } else {
            internalAdd();
            try {
                w.write(cell);
            } catch (IOException ex) {
                throw new KfsStatException("Cannot write cell into csv file", ex);
            }
        }
        return this;
    }

    public exportCsv addQuotedCell(String cell) {
        if (cell == null) {
            return addCell();
        }
        internalAdd();
        try {
            w.append(quotestr + ((cell.indexOf(quotechar) >= 0) ? //
                    cell.replaceAll(quotestr, dquotestr) : cell) + quotestr);
        } catch (IOException ex) {
            throw new KfsStatException("Cannot write cell into csv file", ex);
        }
        return this;
    }

    private void internalAdd() {
        if (added) {
            try {
                w.write(separator);
            } catch (IOException ex) {
                throw new KfsStatException("Cannot write separator into csv file", ex);
            }
        } else {
            added = true;
        }
    }

    public exportCsv newLine() {
        try {
            w.write(newLine);
        } catch (IOException ex) {
            throw new KfsStatException("Cannot write newLine into csv file", ex);
        }
        added = false;
        return this;
    }

    public void export(KfsStatSet set) {
        addCell(set.getPeriodCaption());
        addCell(set.getValueCaption());
        newLine();
        for (KfsStatValue val : set.getLst()) {
            addCell(val.getPeriod());
            addCell(val.getValue());
            newLine();
        }
    }

}

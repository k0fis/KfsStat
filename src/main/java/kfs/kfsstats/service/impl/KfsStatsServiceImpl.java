package kfs.kfsstats.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import kfs.kfsstats.domain.*;
import kfs.kfsstats.service.KfsStatsService;
import kfs.kfsstats.utils.KfsStatException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

/**
 *
 * @author pavedrim
 */
@Service
public class KfsStatsServiceImpl implements KfsStatsService {

    private String dateCaption = "Date";
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private char separator = ',';
    private char quotechar = '"';
    private SimpleDateFormat df = new SimpleDateFormat(dateFormat);

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        df = new SimpleDateFormat(dateFormat);
    }

    @Override
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    @Override
    public void setQuotechar(char quotechar) {
        this.quotechar = quotechar;
    }

    @Override
    public void setDateCaption(String dateCaption) {
        this.dateCaption = dateCaption;
    }

    @Override
    public void createExcel(File outputFile, List<KfsStatSet> setList) {
        exportExcel ee = new exportExcel(outputFile, dateFormat, dateCaption);
        for (KfsStatSet set : setList) {
            ee.add(set);
        }
        ee.done();
    }

    @Override
    public void createExcel(File outputFile, KfsStatSet set) {
        exportExcel ee = new exportExcel(outputFile, dateFormat, dateCaption);
        ee.add(set);
        ee.done();
    }

    @Override
    public void createCsv(File file, KfsStatSet set) {
        try {
            exportCsv ec = new exportCsv(file, separator, quotechar);
            ec.export(set);
            ec.done();
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            throw new KfsStatException("Cannot export data", ex);
        }
    }

    @Override
    public BufferedImage getLineGraph(List<KfsStatSet> setList, String numbersCaption, int width, int height ) {
        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        ValueAxis rangeAxis2 = new NumberAxis(numbersCaption);
        CategoryPlot plot = new CategoryPlot();
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        int inx = 0;
        for (KfsStatSet set : setList) {
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            for (KfsStatValue val : set.getLst()) {
                ds.addValue(val.getValue(), val.getValue(), set.getName());
            }
            plot.setRenderer(inx, renderer2);
            plot.setDataset(inx, ds);
            plot.setRangeAxis(inx, rangeAxis2);
            plot.setDomainAxis(inx, new CategoryAxis(set.getName()));
            inx++;
        }
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        for (KfsStatSet set : setList){
            chart.addSubtitle(new TextTitle(set.getName()));
        }
        return chart.createBufferedImage(width, height);
    }
}

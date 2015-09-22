package kfs.kfsstats.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import kfs.kfsstats.domain.KfsStatSet;

/**
 *
 * @author pavedrim
 */
public interface KfsStatsService {

    void setDateCaption(String dateCaption);
    void setDateFormat(String dateFormat);
    void setSeparator(char separator);
    void setQuotechar(char quotechar);
    
    void createExcel(File file, List<KfsStatSet> setList);
    void createExcel(File file, KfsStatSet set);
    void createCsv(File file, KfsStatSet set);

    BufferedImage getLineGraph(List<KfsStatSet> setList, String numbersCaption, int width, int height);

}

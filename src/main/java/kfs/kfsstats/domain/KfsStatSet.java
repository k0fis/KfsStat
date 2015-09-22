package kfs.kfsstats.domain;

import java.util.List;

/**
 *
 * @author pavedrim
 */
public class KfsStatSet {

    private String name;
    private List<KfsStatValue> lst;
    private String valueCaption;
    private String periodCaption;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KfsStatValue> getLst() {
        return lst;
    }

    public void setLst(List<KfsStatValue> lst) {
        this.lst = lst;
    }

    public String getValueCaption() {
        return valueCaption;
    }

    public void setValueCaption(String valueCaption) {
        this.valueCaption = valueCaption;
    }

    public String getPeriodCaption() {
        return periodCaption;
    }

    public void setPeriodCaption(String periodCaption) {
        this.periodCaption = periodCaption;
    }
}

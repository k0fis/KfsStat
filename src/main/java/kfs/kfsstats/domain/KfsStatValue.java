package kfs.kfsstats.domain;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author pavedrim
 */
@MappedSuperclass
public class KfsStatValue {

    @Id
    private String period;
    private Double value;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}

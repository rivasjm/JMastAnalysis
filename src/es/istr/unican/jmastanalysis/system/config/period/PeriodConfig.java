package es.istr.unican.jmastanalysis.system.config.period;

/**
 * Created by juanm on 17/08/2015.
 */
public class PeriodConfig {

    private PeriodDistributionOptions distribution;
    private Float base;
    private Float ratio;

    public PeriodConfig(PeriodDistributionOptions distribution, Float base, Float ratio) {
        this.distribution = distribution;
        this.base = base;
        this.ratio = ratio;
    }

    public PeriodConfig() {
        super();
    }

    public PeriodDistributionOptions getDistribution() {
        return distribution;
    }

    public void setDistribution(PeriodDistributionOptions distribution) {
        this.distribution = distribution;
    }

    public Float getBase() {
        return base;
    }

    public void setBase(Float base) {
        this.base = base;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }
}

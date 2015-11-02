package es.istr.unican.jmastanalysis.system.config.period;

/**
 * Created by juanm on 17/08/2015.
 */
public enum PeriodDistributionOptions {
    UNIFORM(0), LOG_UNIFORM(1);

    private final int value;

    PeriodDistributionOptions(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}

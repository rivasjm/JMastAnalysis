package es.istr.unican.jmastanalysis.system.config.load;

/**
 * Created by juanm on 17/08/2015.
 */
public enum WCETGenerationOptions {
    SCALE(0), UUNIFAST(1);

    private final int value;

    WCETGenerationOptions(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}

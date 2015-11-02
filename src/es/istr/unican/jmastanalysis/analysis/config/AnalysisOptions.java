package es.istr.unican.jmastanalysis.analysis.config;

/**
 * Created by juanm on 19/08/2015.
 */
public enum AnalysisOptions {
    HOLISTIC(0), OFFSET(1), OFFSET_OPT(2), OFFSET_SLANTED(3), BRUTE_FORCE(4);

    private final int value;

    AnalysisOptions(int i) {
        value = i;
    }

    public static String mapArg(AnalysisOptions a) {
        switch (a) {
            case BRUTE_FORCE:
                return "offset_based_brute_force";
            case HOLISTIC:
                return "holistic";
            case OFFSET:
                return "offset_based_approx";
            case OFFSET_OPT:
                return "offset_based_approx_w_pr";
            case OFFSET_SLANTED:
                return "offset_based_slanted";
            default:
                return "default";
        }
    }

    public int getValue() {
        return value;
    }
}

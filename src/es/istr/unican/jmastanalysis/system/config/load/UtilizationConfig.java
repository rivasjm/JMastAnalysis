package es.istr.unican.jmastanalysis.system.config.load;

/**
 * Created by juanm on 17/08/2015.
 */
public class UtilizationConfig {

    private Integer start;
    private Integer step;
    private Integer top;

    private Integer currentU; //Current utilization in the series

    private Float bcetFactor; //bcet = wcet*bcetFactor

    private WCETGenerationOptions wcetMethod;
    private LoadBalancingOptions balancing;

    public UtilizationConfig(Integer start, Integer step, Integer top, Float bcetFactor, WCETGenerationOptions wcetMethod, LoadBalancingOptions balancing) {
        this.start = start;
        this.step = step;
        this.top = top;
        if ((bcetFactor <= 1.0) && (bcetFactor >= 0.0)) {
            this.bcetFactor = bcetFactor;
        } else {
            throw new IllegalArgumentException("BCET factor " + bcetFactor.toString() + " not valid");
        }
        this.wcetMethod = wcetMethod;
        this.balancing = balancing;
    }

    public UtilizationConfig() {
        super();
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Float getBcetFactor() {
        return bcetFactor;
    }

    public void setBcetFactor(Float bcetFactor) {
        this.bcetFactor = bcetFactor;
    }

    public WCETGenerationOptions getWcetMethod() {
        return wcetMethod;
    }

    public void setWcetMethod(WCETGenerationOptions wcetMethod) {
        this.wcetMethod = wcetMethod;
    }

    public LoadBalancingOptions getBalancing() {
        return balancing;
    }

    public void setBalancing(LoadBalancingOptions balancing) {
        this.balancing = balancing;
    }

    public Integer getCurrentU() {
        return currentU;
    }

    public void setCurrentU(Integer currentU) {
        this.currentU = currentU;
    }
}

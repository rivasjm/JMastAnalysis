package es.istr.unican.jmastanalysis.system.config;


import es.istr.unican.jmastanalysis.system.config.deadline.DeadlineConfig;
import es.istr.unican.jmastanalysis.system.config.load.LoadBalancingOptions;
import es.istr.unican.jmastanalysis.system.config.load.UtilizationConfig;
import es.istr.unican.jmastanalysis.system.config.load.WCETGenerationOptions;
import es.istr.unican.jmastanalysis.system.config.localization.LocalizationOptions;
import es.istr.unican.jmastanalysis.system.config.period.PeriodConfig;
import es.istr.unican.jmastanalysis.system.config.period.PeriodDistributionOptions;

/**
 * Created by juanm on 17/08/2015.
 */
public class SystemConfig {

    private Integer seed;
    private Integer nProcs;
    private Integer nFlows;
    private Integer nSteps;
    private Boolean randomLength;
    private Float singleFlows;
    private String schedPolicy;

    private PeriodConfig period;
    private DeadlineConfig deadline;
    private LocalizationOptions localization;
    private UtilizationConfig utilization;


    public SystemConfig(LinearSystemConfigurableMap map) {
        PeriodConfig p = new PeriodConfig();
        UtilizationConfig u = new UtilizationConfig();
        DeadlineConfig deadline;

        for (LinearSystemConfigurableOptions o : map.keySet()) {
            String value = map.get(o).trim();
            switch (o.name()) {
                case "SEED":
                    this.setSeed(Integer.parseInt(value));
                    break;
                case "N_PROCESSORS":
                    this.setnProcs(Integer.parseInt(value));
                    break;
                case "N_FLOWS":
                    this.setnFlows(Integer.parseInt(value));
                    break;
                case "N_STEPS":
                    this.setnSteps(Integer.parseInt(value));
                    break;
                case "RANDOM_LENGTH":
                    this.setRandomLength(Boolean.parseBoolean(value));
                    break;
                case "SINGLE_FLOWS":
                    this.setSingleFlows(Float.parseFloat(value));
                    break;
                case "SCHED_POLICY":
                    this.setSchedPolicy(value);
                    break;
                case "PERIOD_DISTRIBUTION":
                    p.setDistribution(PeriodDistributionOptions.valueOf(value));
                    break;
                case "PERIOD_BASE":
                    p.setBase(Float.parseFloat(value));
                    break;
                case "PERIOD_RATIO":
                    p.setRatio(Float.parseFloat(value));
                    break;
                case "DEADLINE":
                    deadline = new DeadlineConfig(value.trim());
                    this.setDeadline(deadline);
                    break;
                case "TASK_LOCALIZATION":
                    this.setLocalization(LocalizationOptions.valueOf(value));
                    break;
                case "UTILIZATION_START":
                    u.setStart(Integer.parseInt(value));
                    break;
                case "UTILIZATION_STEP":
                    u.setStep(Integer.parseInt(value));
                    break;
                case "UTILIZATION_TOP":
                    u.setTop(Integer.parseInt(value));
                    break;
                case "UTILIZATION_BCET_FACTOR":
                    u.setBcetFactor(Float.parseFloat(value));
                    break;
                case "UTILIZATION_WCET_METHOD":
                    u.setWcetMethod(WCETGenerationOptions.valueOf(value));
                    break;
                case "UTILIZATION_BALANCING":
                    u.setBalancing(LoadBalancingOptions.valueOf(value));
                    break;
            }
        }

        this.setPeriod(p);
        u.setCurrentU(u.getStart());
        this.setUtilization(u);

    }

    public SystemConfig(Integer theSeed, Integer nProcs, Integer nFlows, Integer nSteps, Boolean randomLength, Float singleFlows, String schedPolicy, PeriodConfig period, DeadlineConfig deadline, LocalizationOptions localization, UtilizationConfig utilization) {
        this.seed = theSeed;
        this.nProcs = nProcs;
        this.nFlows = nFlows;
        this.nSteps = nSteps;
        this.randomLength = randomLength;
        this.singleFlows = singleFlows;
        this.schedPolicy = schedPolicy;
        this.period = period;
        this.deadline = deadline;
        this.localization = localization;
        this.utilization = utilization;
    }

    public SystemConfig() {
        super();
    }
//
//    public LinearSystemConfigurableMap getMap(){
//        LinearSystemConfigurableMap map = new LinearSystemConfigurableMap();
//
//        map.put(LinearSystemConfigurableOptions.SEED, seed.toString());
//        map.put(LinearSystemConfigurableOptions.N_PROCESSORS, nProcs.toString());
//        map.put(LinearSystemConfigurableOptions.N_FLOWS, nFlows.toString());
//        map.put(LinearSystemConfigurableOptions.N_STEPS, nSteps.toString());
//        map.put(LinearSystemConfigurableOptions.RANDOM_LENGTH, randomLength.toString());
//
//
//        return map;
//    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getnProcs() {
        return nProcs;
    }

    public void setnProcs(Integer nProcs) {
        this.nProcs = nProcs;
    }

    public Integer getnFlows() {
        return nFlows;
    }

    public void setnFlows(Integer nFlows) {
        this.nFlows = nFlows;
    }

    public Integer getnSteps() {
        return nSteps;
    }

    public void setnSteps(Integer nSteps) {
        this.nSteps = nSteps;
    }

    public Float getSingleFlows() {
        return singleFlows;
    }

    public void setSingleFlows(Float singleFlows) {
        this.singleFlows = singleFlows;
    }

    public Boolean getRandomLength() {
        return randomLength;
    }

    public void setRandomLength(Boolean randomLength) {
        this.randomLength = randomLength;
    }

    public String getSchedPolicy() {
        return schedPolicy;
    }

    public void setSchedPolicy(String schedPolicy) {
        this.schedPolicy = schedPolicy;
    }

    public PeriodConfig getPeriod() {
        return period;
    }

    public void setPeriod(PeriodConfig period) {
        this.period = period;
    }

    public DeadlineConfig getDeadline() {
        return deadline;
    }

    public void setDeadline(DeadlineConfig deadline) {
        this.deadline = deadline;
    }

    public LocalizationOptions getLocalization() {
        return localization;
    }

    public void setLocalization(LocalizationOptions localization) {
        this.localization = localization;
    }

    public UtilizationConfig getUtilization() {
        return utilization;
    }

    public void setUtilization(UtilizationConfig utilization) {
        this.utilization = utilization;
    }

}

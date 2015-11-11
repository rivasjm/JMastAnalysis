package es.istr.unican.jmastanalysis.analysis.config;


/**
 * Created by juanm on 19/08/2015.
 */
public class MastConfig {

    private String name;
    private String workPath;
    private String mastPath;
    private AnalysisOptions analysis;
    private Boolean sync;
    private AssignmentOptions assignment;
    private HOSPAConfig hospaConfig;
    private Float stopFactor;
    private Boolean gsd;        //Forces Global Scheduling Deadlines in local clock EDF
    private Integer dsFactor;     //Scaling factor for LC-EDF-DS
    private Boolean calculateSlack;
    private Boolean jitterAvoidance;

    public MastConfig(MastToolConfigurableMap map) {
        HOSPAConfig h = new HOSPAConfig();

        for (MastToolConfigurableOptions o : map.keySet()) {
            String value = map.get(o).trim();
            switch (o.name()) {

                case "NAME":
                    this.setName(value);
                    break;
                case "WORK_PATH":
                    this.setWorkPath(value);
                    break;
                case "MAST_PATH":
                    this.setMastPath(value);
                    break;
                case "ANALYSIS_TOOL":
                    this.setAnalysis(AnalysisOptions.valueOf(value));
                    break;
                case "SYNC":
                    this.setSync(Boolean.parseBoolean(value));
                    break;
                case "ASSIGNMENT_TOOL":
                    this.setAssignment(AssignmentOptions.valueOf(value));
                    break;
                case "HOSPA_INIT":
                    h.setInit(HOSPAConfig.InitOptions.valueOf(value));
                    break;
                case "HOSPA_Ka":
                    h.setKa(Float.parseFloat(value));
                    break;
                case "HOSPA_Kr":
                    h.setKr(Float.parseFloat(value));
                    break;
                case "HOSPA_ITERATIONS":
                    h.setIterations(Integer.parseInt(value));
                    break;
                case "HOSPA_OVERITERATIONS":
                    h.setOverIterations(Integer.parseInt(value));
                    break;
                case "ANALYSIS_STOP_FACTOR":
                    this.setStopFactor(Float.parseFloat(value));
                    break;
                case "LC_EDF_GSD":
                    ;
                    this.setGsd(Boolean.parseBoolean(value));
                    break;
                case "LC_EDF_DS_FACTOR":
                    this.setDsFactor(Integer.parseInt(value));
                    break;
                case "CALCULATE_SLACK":
                    this.setCalculateSlack(Boolean.parseBoolean(value));
                    break;
                case "JITTER_AVOIDANCE":
                    this.setJitterAvoidance(Boolean.parseBoolean(value));
                    break;

            }
        }

        this.setHospaConfig(h);

    }

    public MastConfig(String name, String workPath, String mastPath, AnalysisOptions analysis, Boolean sync, AssignmentOptions assignment, HOSPAConfig hospaConfig, Float stopFactor, Boolean gsd, Integer dsFactor, Boolean calculateSlack, Boolean jitterAvoidance) {
        this.name = name;
        this.workPath = workPath;
        this.mastPath = mastPath;
        this.analysis = analysis;
        this.sync = sync;
        this.assignment = assignment;
        this.hospaConfig = hospaConfig;
        this.stopFactor = stopFactor;
        this.gsd = gsd;
        this.dsFactor = dsFactor;
        this.calculateSlack = calculateSlack;
        this.jitterAvoidance = jitterAvoidance;
    }

    public MastConfig() {
        super();
        this.name = "MAST analysis";
        this.workPath = ".";
        this.mastPath = "mast_analysis.exe";
        this.sync = true;
        this.hospaConfig = new HOSPAConfig();
        this.stopFactor = 10.0f;
        this.gsd = false;
        this.dsFactor = 1;
        this.calculateSlack = false;
        this.jitterAvoidance = false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    public String getMastPath() {
        return mastPath;
    }

    public void setMastPath(String mastPath) {
        this.mastPath = mastPath;
    }

    public AnalysisOptions getAnalysis() {
        return analysis;
    }

    public void setAnalysis(AnalysisOptions analysis) {
        this.analysis = analysis;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public AssignmentOptions getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignmentOptions assignment) {
        this.assignment = assignment;
    }

    public HOSPAConfig getHospaConfig() {
        return hospaConfig;
    }

    public void setHospaConfig(HOSPAConfig hospaConfig) {
        this.hospaConfig = hospaConfig;
    }

    public Float getStopFactor() {
        return stopFactor;
    }

    public void setStopFactor(Float stopFactor) {
        this.stopFactor = stopFactor;
    }

    public Boolean getGsd() {
        return gsd;
    }

    public void setGsd(Boolean gsd) {
        this.gsd = gsd;
    }

    public Integer getDsFactor() {
        return dsFactor;
    }

    public void setDsFactor(Integer dsFactor) {
        this.dsFactor = dsFactor;
    }

    public Boolean getCalculateSlack() {
        return calculateSlack;
    }

    public void setCalculateSlack(Boolean calculateSlack) {
        this.calculateSlack = calculateSlack;
    }

    public Boolean getJitterAvoidance() {
        return jitterAvoidance;
    }

    public void setJitterAvoidance(Boolean jitterAvoidance) {
        this.jitterAvoidance = jitterAvoidance;
    }
}

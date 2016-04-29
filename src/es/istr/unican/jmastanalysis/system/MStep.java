package es.istr.unican.jmastanalysis.system;

import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by juanm on 02/11/2015.
 */
public class MStep {

    public enum StepType {ACTIVITY, DELAY};

    private String id;

    // Basic Characteristics
    private Double wcet;
    private Double bcet;
    private Double offset;
    private MTask task;
    private MFlow flow;
    private Integer overrridenPriority;
    private StepType type = StepType.ACTIVITY;


    // Results
    private Double wcrt;
    private Double bcrt;
    private Double jitter;
    private Double w; //similar to local response time, wcrt is calculated as the sum of the previous w's

    public MStep(Double wcet, Double bcet, MTask task){
        this.wcet = wcet;
        this.bcet = bcet;
        this.task = task;
        this.overrridenPriority = null;
        setTask(task);
    }

    public MStep(Double wcet, Double bcet, MTask task, Integer overridenPriority){
        this.wcet = wcet;
        this.bcet = bcet;
        this.task = task;
        this.overrridenPriority = overridenPriority;
        setTask(task);
    }

    public MStep(){
        super();
    }

    // Getters and Setters


    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    public MTask getTask() {
        return task;
    }

    public Double getWcet() {
        return wcet;
    }

    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }

    public void setWcet(Double wcet) {
        this.wcet = wcet;
    }

    public Double getBcet() {
        return bcet;
    }

    public void setBcet(Double bcet) {
        this.bcet = bcet;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public Integer getPriority() {
        return task.getPriority();
    }

    public Double getSchedulingDeadline() {
        return task.getSchedulingDeadline();
    }

    public MProcessor getProcessor() {
        return task.getProcessor();
    }

    public void setTask(MTask task) {
        this.task = task;
        task.addStep(this);
    }

    public Double getWcrt() {
        return wcrt;
    }

    public void setWcrt(Double wcrt) {
        this.wcrt = wcrt;
    }

    public Double getBcrt() {
        return bcrt;
    }

    public void setBcrt(Double bcrt) {
        this.bcrt = bcrt;
    }

    public Double getJitter() {
        return jitter;
    }

    public void setJitter(Double jitter) {
        this.jitter = jitter;
    }

    public MFlow getFlow() {
        return flow;
    }

    public void setFlow(MFlow flow) {
        this.flow = flow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void printOverview() {
        java.lang.System.out.format("(%f, %d, %d)", wcet, task.getProcessor().getId(), task.getPriority());
    }

    private void printResult(){
        System.out.printf("%f", wcrt);
    }

    public void writeOperation(PrintWriter pw) {

        if (type == StepType.ACTIVITY) {
            pw.format("Operation (\n");
            pw.format("     Type     => Simple,\n");
            pw.format("     Name     => O_%s,\n", getId());
            if (this.overrridenPriority != null) {
                pw.format("     New_Sched_Parameters       => \n");
                pw.format("        ( Type         => Overridden_Fixed_Priority,\n");
                pw.format("          The_Priority => %d),\n", this.overrridenPriority);
            }
            pw.format(Locale.US, "     Worst_Case_Execution_Time     => %f,\n", getWcet());
            pw.format(Locale.US, "     Best_Case_Execution_Time     => %f);\n\n", getBcet());
        }

    }



}

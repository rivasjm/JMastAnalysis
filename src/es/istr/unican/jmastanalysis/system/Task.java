package es.istr.unican.jmastanalysis.system;

import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by juanm on 02/11/2015.
 */
public class Task {

    private String id;

    // Basic Characteristics
    private Double wcet;
    private Double bcet;
    private Double offset;
    private Integer priority;
    private Double schedulingDeadline;
    private Processor processor;
    private Flow flow;

    // Results
    private Double wcrt;
    private Double bcrt;
    private Double jitter;
    private Double w; //similar to local response time, wcrt is calculated as the sum of the previous w's


    // Getters and Setters

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
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Double getSchedulingDeadline() {
        return schedulingDeadline;
    }

    public void setSchedulingDeadline(Double schedulingDeadline) {
        this.schedulingDeadline = schedulingDeadline;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
        processor.addTask(this);
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

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void printOverview() {
        java.lang.System.out.format("(%f, %d, %d)", wcet, processor.getId(), priority);
    }

    private void printResult(){
        System.out.printf("%f", wcrt);
    }

    public void writeOperation(PrintWriter pw) {

        pw.format("Operation (\n");
        pw.format("     Type     => Simple,\n");
        pw.format("     Name     => O_%s,\n", getId());
        pw.format(Locale.US, "     Worst_Case_Execution_Time     => %f,\n", getWcet());
        pw.format(Locale.US, "     Best_Case_Execution_Time     => %f);\n\n", getBcet());

    }

    public void writeSchedulingServer(PrintWriter pw) {

        pw.format("Scheduling_Server (\n");
        pw.format("        Type                       => Regular,\n");
        pw.format("        Name    => SS_%s,\n", getId());
        pw.format("        Server_Sched_Parameters         => (\n");

        if (getProcessor().getSchedulingPolicy().equals("FP")) {
            pw.format("                Type                    => Fixed_Priority_policy,\n");
            pw.format("                The_Priority            => %d,\n", getPriority());
        } else if (getProcessor().getSchedulingPolicy().equals("EDF")) {
            pw.format("                Type                    => EDF_policy,\n");
            pw.format("                Deadline                => %f,\n", getSchedulingDeadline());
        } else {
            throw new IllegalArgumentException("Scheduling policy " + getProcessor().getSchedulingPolicy() + " not valid");
        }

        pw.format("                Preassigned             => No),\n");
        pw.format("        Scheduler      => s%d);\n\n", getProcessor().getId());

    }

}

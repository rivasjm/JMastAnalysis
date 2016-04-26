package es.istr.unican.jmastanalysis.system;

import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by juanm on 02/11/2015.
 */
public class Step {

    private String id;

    // Basic Characteristics
    private Double wcet;
    private Double bcet;
    private Double offset;
    private Task task;
    private Flow flow;

    // Results
    private Double wcrt;
    private Double bcrt;
    private Double jitter;
    private Double w; //similar to local response time, wcrt is calculated as the sum of the previous w's

    public Step(Double wcet, Double bcet, Task task){
        this.wcet = wcet;
        this.bcet = bcet;
        this.task = task;
        setTask(task);
    }

    public Step(){
        super();
    }

    // Getters and Setters


    public Task getTask() {
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

    public Processor getProcessor() {
        return task.getProcessor();
    }

    public void setTask(Task task) {
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
        java.lang.System.out.format("(%f, %d, %d)", wcet, task.getProcessor().getId(), task.getPriority());
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



}

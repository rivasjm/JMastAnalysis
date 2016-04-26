package es.istr.unican.jmastanalysis.system;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrador on 26/04/2016.
 */
public class Task {

    private Integer id;

    private Processor processor;
    private Set<Step> steps;

    // Scheduling parameters
    private Integer priority;
    private Double schedulingDeadline;


    // Constructor

    public Task(Integer id) {
        this.id = id;
        steps = new HashSet<>();
    }

    public Task(Integer id, Processor processor) {
        this.id = id;
        this.processor = processor;
        steps = new HashSet<>();
    }

// Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public Set<Step> getSteps(){
        return steps;
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


    // Write methods

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

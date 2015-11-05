package es.istr.unican.jmastanalysis.system;

import es.istr.unican.jmastanalysis.system.config.load.WCETGenerationOptions;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.ceil;
import static java.lang.Math.pow;

/**
 * Created by juanm on 02/11/2015.
 */
public class Processor {

    private Integer id;
    private String schedulingPolicy;
    private List<Task> tasks;


    public Processor() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task aTask) {
        tasks.add(aTask);
    }


    // Utilization generation methods

    public void scaleUtilization(Double factor) {
        /*
        Scales the utilization of the processor by a factor of "factor"
         */
        for (Task t : tasks) {
            t.setWcet(t.getWcet() * factor);
        }
    }

    public void setUtilization(WCETGenerationOptions o, Double u, Random r) {
        switch (o) {
            case SCALE:
                for (Task t : tasks) {
                    t.setWcet(u * t.getFlow().getPeriod() / tasks.size());
                }
                break;

            case UUNIFAST:
                Double sumU = u;
                Double nextSumU;
                for (Task t : tasks) {

                    // Last task in the list
                    if (tasks.indexOf(t) + 1 == tasks.size()) {
                        t.setWcet(sumU * t.getFlow().getPeriod());
                        break;
                    }

                    nextSumU = sumU * pow(r.nextDouble(), 1.0 / (tasks.size() - tasks.indexOf(t) + 1));
                    t.setWcet((sumU - nextSumU) * t.getFlow().getPeriod());
                    sumU = nextSumU;
                }
                break;
        }
    }

    public void setBestCaseUtilization(Float factor) {
        for (Task t : tasks) {
            t.setBcet(t.getWcet() * factor);
        }
    }

    public Double getUtilization() {
        Double u = 0.0;
        for (Task t : tasks) {
            u += t.getWcet() / t.getFlow().getPeriod();
        }
        return u;
    }

    public void printOverview() {
        java.lang.System.out.printf("%f", this.getUtilization());
    }


    // Getter and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchedulingPolicy() {
        return schedulingPolicy;
    }

    public void setSchedulingPolicy(String schedulingPolicy) {
        this.schedulingPolicy = schedulingPolicy;
    }

    public void writeProcessingResource(PrintWriter pw) {

        pw.format("Processing_Resource (\n");
        pw.format("        Type                    => Regular_Processor,\n");
        pw.format("        Name                    => CPU_%d,\n", getId());
        pw.format("        Max_Interrupt_Priority  => 32767,\n");
        pw.format("        Min_Interrupt_Priority  => 32767);\n");

    }

    public void writeScheduler(PrintWriter pw) {

        pw.format("Scheduler (\n");
        pw.format("   Type            => Primary_Scheduler,\n");
        pw.format("   Name            => s%d,\n", getId());
        pw.format("   Host            => CPU_%d,\n", getId());

        if (getSchedulingPolicy().equals("FP")) {
            pw.format("   Policy          =>      (\n");
            pw.format("        Type                    => Fixed_Priority,\n");
            pw.format("        Max_Priority            => 32766,\n");
            pw.format("        Min_Priority            => 1));\n");
        } else if (getSchedulingPolicy().equals("EDF")) {
            pw.format("   Policy          =>      ( Type                 => EDF));\n");
        } else {
            throw new IllegalArgumentException("Scheduling policy " + getSchedulingPolicy() + " not valid");
        }
    }

    public void setDeadlineMonotonicPriorities(){
        // Deadline monotonic priority assignment
        tasks.sort((t1, t2) ->  Double.compare(t2.getSchedulingDeadline(), t1.getSchedulingDeadline())); // Sort tasks according to their scheduling deadlines (fisrt task has larger SD)
        for (int i=0; i<tasks.size(); i++){
            tasks.get(i).setPriority(i+1);
        }
    }

    public List<Task> getInterferentTasks(Task aTask){
        // Returns list of tasks with higher or equal priority than t in the processor (excluding t)
        List<Task> interferent = new ArrayList<>();
        for (Task t: tasks){
            if ((t.getPriority() >= aTask.getPriority()) && (t != aTask)) {
                interferent.add(t);
            }
        }
        return interferent;
    }

    public void calculateApproxLocalResponseTimes(){
        // Approximate response time analysis for independent tasks
        for (Task t: tasks){
            Double sum = t.getWcet();
            Double periodAnalysis = t.getFlow().getPeriod();

            List<Task> interferent = getInterferentTasks(t);
            for (Task it: interferent){
                Double periodCurrent = it.getFlow().getPeriod();
                Double wcetCurrent = it.getWcet();
                sum += ceil(periodAnalysis/periodCurrent)*wcetCurrent;
            }
            t.setW(sum);
        }
    }

    public void calculateApproxDeadlinesLocalResponseTimes(){
        // Approximate response time analysis for independent tasks, for D>T
        for (Task t: tasks){
            Double sum = t.getWcet();
            Double periodAnalysis = t.getFlow().getPeriod();
            Double deadlineAnalysis = t.getFlow().getDeadline();

            List<Task> interferent = getInterferentTasks(t);
            for (Task it: interferent){
                Double periodCurrent = it.getFlow().getPeriod();
                Double wcetCurrent = it.getWcet();
                sum += ceil(deadlineAnalysis/periodCurrent)*wcetCurrent;
            }
            t.setW(sum);
        }
    }

    public void calculateExactLocalResponseTime(){
        // Exact response time analysis for independent tasks

        for (Task ta: tasks){ // Task under analysis
            List<Task> interferent = getInterferentTasks(ta);

            double wcant = ta.getWcet();
            double wc;
            while (true){
                wc = ta.getWcet();
                for (Task ti: interferent) {
                    double periodCurrent = ti.getFlow().getPeriod();
                    double wcetCurrent = ti.getWcet();
                    wc += ceil(wcant/periodCurrent)*wcetCurrent;
                }
                if (wc == wcant) break;
                else wcant = wc;
            }
            ta.setW(wc);
        }
    }

}

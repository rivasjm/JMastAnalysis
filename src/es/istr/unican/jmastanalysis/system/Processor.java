package es.istr.unican.jmastanalysis.system;

import es.istr.unican.jmastanalysis.system.config.load.WCETGenerationOptions;

import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.ceil;
import static java.lang.Math.pow;

/**
 * Created by juanm on 02/11/2015.
 */
public class Processor {

    private Integer id;
    private String schedulingPolicy;
    private Set<Task> tasks;


    public Processor(Integer id){
        super();
        this.id = id;
        tasks = new HashSet<>();
    }

    public Processor(Integer id, String schedPolicy) {
        this.id = id;
        setSchedulingPolicy(schedPolicy);
        tasks = new HashSet<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }


    // Utilization generation methods

    private List<Step> getSteps(){
        List <Step> steps = new ArrayList<>();
        for (Task task: tasks){
            for (Step step: task.getSteps()){
                steps.add(step);
            }
        }
        return steps;
    }


    public void scaleUtilization(Double factor) {
        /*
        Scales the utilization of the processor by a factor of "factor"
         */
        for (Step t : getSteps()) {
            t.setWcet(t.getWcet() * factor);
        }
    }

    public void setUtilization(WCETGenerationOptions o, Double u, Random r) {
        ArrayList<Step> stepsList = new ArrayList<>(getSteps());
        switch (o) {
            case SCALE:
                for (Step t : getSteps()) {
                    t.setWcet(u * t.getFlow().getPeriod() / getSteps().size());
                }
                break;

            case UUNIFAST:
                Double sumU = u;
                Double nextSumU;
                for (Step t : getSteps()) {

                    // Last step in the list
                    if (stepsList.indexOf(t) + 1 == getSteps().size()) {
                        t.setWcet(sumU * t.getFlow().getPeriod());
                        break;
                    }

                    nextSumU = sumU * pow(r.nextDouble(), 1.0 / (getSteps().size() - stepsList.indexOf(t) + 1));
                    t.setWcet((sumU - nextSumU) * t.getFlow().getPeriod());
                    sumU = nextSumU;
                }
                break;
        }
    }

    public void setBestCaseUtilization(Float factor) {
        for (Step t : getSteps()) {
            t.setBcet(t.getWcet() * factor);
        }
    }

    public Double getUtilization() {
        Double u = 0.0;
        for (Step t : getSteps()) {
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

        if (schedulingPolicy.equals("FP") || schedulingPolicy.equals("EDF")) {
            this.schedulingPolicy = schedulingPolicy;
        } else {
            System.out.println("WARNING: %s scheduling policy not valid. Using FP instead");
            this.schedulingPolicy = "FP";
        }
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
        ArrayList<Step> stepsList = new ArrayList<>(getSteps());
        stepsList.sort((t1, t2) ->  Double.compare(t2.getSchedulingDeadline(), t1.getSchedulingDeadline())); // Sort steps according to their scheduling deadlines (fisrt step has larger SD)
        for (int i = 0; i< getSteps().size(); i++){
            stepsList.get(i).getTask().setPriority(i+1);
        }
    }

    public List<Step> getInterferentSteps(Step aStep){
        // Returns list of steps with higher or equal priority than t in the processor (excluding t)
        List<Step> interferent = new ArrayList<>();
        for (Step t: getSteps()){
            if ((t.getPriority() >= aStep.getPriority()) && (t != aStep)) {
                interferent.add(t);
            }
        }
        return interferent;
    }

    public void calculateApproxLocalResponseTimes(){
        // Approximate response time analysis for independent steps
        for (Step t: getSteps()){
            Double sum = t.getWcet();
            Double periodAnalysis = t.getFlow().getPeriod();

            List<Step> interferent = getInterferentSteps(t);
            for (Step it: interferent){
                Double periodCurrent = it.getFlow().getPeriod();
                Double wcetCurrent = it.getWcet();
                sum += ceil(periodAnalysis/periodCurrent)*wcetCurrent;
            }
            t.setW(sum);
        }
    }

    public void calculateApproxDeadlinesLocalResponseTimes(){
        // Approximate response time analysis for independent steps, for D>T
        for (Step t: getSteps()){
            Double sum = t.getWcet();
            Double periodAnalysis = t.getFlow().getPeriod();
            Double deadlineAnalysis = t.getFlow().getDeadline();

            List<Step> interferent = getInterferentSteps(t);
            for (Step it: interferent){
                Double periodCurrent = it.getFlow().getPeriod();
                Double wcetCurrent = it.getWcet();
                sum += ceil(deadlineAnalysis/periodCurrent)*wcetCurrent;
            }
            t.setW(sum);
        }
    }

    public void calculateExactLocalResponseTime(){
        // Exact response time analysis for independent steps

        for (Step ta: getSteps()){ // Step under analysis
            List<Step> interferent = getInterferentSteps(ta);

            double wcant = ta.getWcet();
            double wc;
            while (true){
                wc = ta.getWcet();
                for (Step ti: interferent) {
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

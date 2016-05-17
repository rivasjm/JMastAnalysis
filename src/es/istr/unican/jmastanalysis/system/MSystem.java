package es.istr.unican.jmastanalysis.system;

import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.system.config.SystemConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by juanm on 02/11/2015.
 */
public class MSystem {

    private String name;
    private List<MFlow> flows;
    private List<MProcessor> processors;
    private List<MTask> tasks;

    private Random random;

    private SystemConfig systemConfig;
    private MastConfig toolConfig;  //configuration of the tool with which the results where created

    private long toolTimeElapsed;   // Time elapsed to execute tool with this system (result)
    private Double systemSlack;     // System Slack (result)

    // Public methods

    public MSystem() {
        flows = new ArrayList<>();
        processors = new ArrayList<>();
        tasks = new ArrayList<>();
        random = new Random();
    }

    public MSystem(List<MFlow> flowList, List<MProcessor> procList, List<MTask> taskList){
        flows = flowList;
        processors = procList;
        tasks = taskList;
        random = new Random();
    }

    public MSystem(SystemConfig systemConfiguration) {
        flows = new ArrayList<>();
        processors = new ArrayList<>();
        random = new Random(systemConfiguration.getSeed());
        create(systemConfiguration);
    }

    public void addFlow(MFlow newFlow){
        flows.add(newFlow);
    }

    public void addProcessor(MProcessor newProcessor){
        processors.add(newProcessor);
    }

    public MastConfig getToolConfig() {
        return toolConfig;
    }

    public void setToolConfig(MastConfig toolConfig) {
        this.toolConfig = toolConfig;
    }

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    public long getToolTimeElapsed() {
        return toolTimeElapsed;
    }

    public void setToolTimeElapsed(long toolTimeElapsed) {
        this.toolTimeElapsed = toolTimeElapsed;
    }

    public Double getSystemUtilization() {
        Double u = 0.0;
        for (MProcessor p : processors) u += p.getUtilization();
        return u / processors.size();
    }

    public void printOverview() {
        for (MFlow f : flows) {
            f.printOverview();
        }
        //System.out.printf("\n");
        for (MProcessor p : processors) {
            p.printOverview();
            java.lang.System.out.printf(" ");
        }
        java.lang.System.out.printf(": %f\n", this.getSystemUtilization());
    }

    public void printResultsOverview() {
        for (MFlow f : flows) {
            f.printResultsOverview();
        }
    }

    public List<MProcessor> getProcessors() {
        return processors;
    }

    public List<MFlow> getFlows() {
        return flows;
    }

    public void setStepResults(int flowId, String stepID, Double bcrt, Double wcrt, Double jitter) {
        flows.get(flowId - 1).setStepResults(stepID, bcrt, wcrt, jitter);
    }

    public double getSystemAvgWCRT() {
        Double sum = 0.0;
        for (MFlow f : this.flows) {
            sum += f.getFlowWCRT();
        }
        return sum / this.flows.size();
    }

    public double[][] getStepsWCRTAsArray() {
        int maxLength = 0;
        for (MFlow f: flows){
            maxLength = (f.getSteps().size() > maxLength) ? f.getSteps().size() : maxLength;
        }
        double [][] wcrts = new double[flows.size()][maxLength];

        for (int i=0; i<flows.size(); i++){
            for (int j = 0; j< flows.get(i).getSteps().size(); j++){
                wcrts[i][j] = flows.get(i).getSteps().get(j).getWcrt();
            }
        }

        return wcrts;

    }

    public double getSystemSchedIndex(){
        List<Double> indices = new ArrayList<>();
        for (MFlow f: flows){
            indices.add(f.calculateSchedIndex());
        }
        return Collections.min(indices);
    }

    public boolean isSchedulable() {
        for (MFlow f : this.flows) {
            if (f.getFlowWCRT() > f.getDeadline()) {
                return false;
            }
        }
        return true;
    }

    public void setPDSchedulingDeadlines(){
        for (MFlow f: flows){
            f.setPDSchedulingDeadlines();
        }
    }

    public void setPDPriorities(){
        for (MFlow f: flows){
            f.setPDSchedulingDeadlines();
        }
        for (MProcessor p: processors){
            p.setDeadlineMonotonicPriorities();
        }
    }

    public void setDeadlineMonotonicPriorities(){
        for (MProcessor p: processors){
            p.setDeadlineMonotonicPriorities();
        }
    }

    public void calculateApproxLocalResponseTimes(){
        for (MProcessor p: processors){
            p.calculateApproxLocalResponseTimes();
        }
        for (MFlow f: flows){
            f.calculateWCRTfromW();
        }
    }

    public void calculateApproxDeadlinesLocalResponseTimes(){
        for (MProcessor p: processors){
            p.calculateApproxDeadlinesLocalResponseTimes();
        }
        for (MFlow f: flows){
            f.calculateWCRTfromW();
        }
    }

    public void calculateExactLocalResponseTime(){
        for (MProcessor p: processors){
            p.calculateExactLocalResponseTime();
        }
        for (MFlow f: flows){
            f.calculateWCRTfromW();
        }
    }

    // Private methods

    private void create(SystemConfig c) {
        try {

            this.setSystemConfig(c);

            // Add processors
            for (int i = 1; i <= c.getnProcs(); i++) {
                MProcessor proc = new MProcessor(i);
                proc.setSchedulingPolicy(c.getSchedPolicy());
                processors.add(proc);
            }

            // Add e2e flows and steps
            Integer singleFlows = (int) (c.getSingleFlows() / 100.0 * c.getnFlows());
            for (int i = 1; i <= c.getnFlows(); i++) {
                MFlow flow = new MFlow();
                flow.setId(i);

                // Add steps to e2e flow
                Integer nSteps;
                if (i <= singleFlows) {
                    nSteps = 1;
                } else if (c.getRandomLength()) {
                    nSteps = random.nextInt((c.getnSteps()+1) - 2) + 2; // random integer between [2, number of steps]
                } else {
                    nSteps = c.getnSteps();
                }

                MProcessor lastProc = null; // for step processor localization
                for (int j = 1; j <= nSteps; j++) {
                    MStep step = new MStep();
                    step.getTask().setPriority(1);
                    step.getTask().setSchedulingDeadline(1.0);
                    flow.addStep(step);
                }
                flows.add(flow);
            }

            System.out.println(flows.size());

            // Step localization, and flow periods and deadlines
            for (MFlow f : flows) {
                f.locateSteps((List<MProcessor>) processors, c.getLocalization(), random);
                f.setPeriod(c.getPeriod(), random);
                f.setDeadline(c.getDeadline(), random);
            }

            // Generate Load (step wcet)
            switch (c.getUtilization().getBalancing()) {

                case BALANCED: //every processor with the same utilization
                    for (MProcessor p : processors) {
                        p.setUtilization(c.getUtilization().getWcetMethod(), 0.01, random);
                        p.scaleUtilization((c.getUtilization().getCurrentU() / 100.0) / 0.01);
                    }
                    break;

                case NON_BALANCED: //each processor has a different utilization
                    // First create the utilization of each processor, so the system have 1% utilization
                    ArrayList<Double> us = new ArrayList<Double>();
                    for (MProcessor p : processors) {
                        us.add(random.nextDouble() + 0.5);
                    }
                    Double sum = 0.0;
                    for (Double u : us) {
                        sum += u;
                    }
                    ArrayList<Double> newUs = new ArrayList<Double>();
                    for (Double d : us) {
                        newUs.add(d / sum * processors.size() * 0.01);
                    }

                    // Scale processors utilizations to reach target
                    for (int i = 0; i < processors.size(); i++) {
                        processors.get(i).setUtilization(c.getUtilization().getWcetMethod(), newUs.get(i), random);
                        processors.get(i).scaleUtilization((c.getUtilization().getCurrentU() / 100.0) / 0.01);
                    }
                    break;
            }

            // Set BCET of steps
            for (MProcessor p : processors) {
                p.setBestCaseUtilization(c.getUtilization().getBcetFactor());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeSystem(File f) {
        try {
            FileOutputStream o = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(o);

            // Processing Resources
            for (MProcessor p : getProcessors()) {
                p.writeProcessingResource(pw);
                pw.write("\n");
            }
            pw.write("\n");


            // Schedulers
            for (MProcessor p : getProcessors()) {
                p.writeScheduler(pw);
                pw.write("\n");
            }
            pw.write("\n");


            // Operations
            for (MFlow mf : getFlows()) {
                mf.writeOperations(pw);
            }
            pw.write("\n");


            // Scheduling Servers
            for (MTask task : tasks) {
                task.writeSchedulingServer(pw);
            }
            pw.write("\n");


            // Transactions
            for (MFlow mf :  getFlows()) {
                mf.writeTransaction(pw);
            }

            pw.flush();
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

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
public class MastSystem {

    private String name;
    private List<Flow> flows;
    private List<Processor> processors;

    private Random random;

    private SystemConfig systemConfig;
    private MastConfig toolConfig; //configuration of the tool with which the results where created

    private long toolTimeElapsed; // Time elapsed to execute tool with this system


    // Public methods

    public MastSystem() {
        flows = new ArrayList<>();
        processors = new ArrayList<>();
        random = new Random();
    }

    public MastSystem(List<Flow> flowList, List<Processor> procList){
        flows = flowList;
        processors = procList;
        random = new Random();
    }

    public MastSystem(SystemConfig systemConfiguration) {
        flows = new ArrayList<>();
        processors = new ArrayList<>();
        random = new Random(systemConfiguration.getSeed());
        create(systemConfiguration);
    }

    public void addFlow(Flow newFlow){
        flows.add(newFlow);
    }

    public void addProcessor(Processor newProcessor){
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
        for (Processor p : processors) u += p.getUtilization();
        return u / processors.size();
    }

    public void printOverview() {
        for (Flow f : flows) {
            f.printOverview();
        }
        //System.out.printf("\n");
        for (Processor p : processors) {
            p.printOverview();
            java.lang.System.out.printf(" ");
        }
        java.lang.System.out.printf(": %f\n", this.getSystemUtilization());
    }

    public void printResultsOverview() {
        for (Flow f : flows) {
            f.printResultsOverview();
        }
    }

    public List<Processor> getProcessors() {
        return processors;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setTaskResults(int flowId, String taskID, Double bcrt, Double wcrt, Double jitter) {
        flows.get(flowId - 1).setTaskResults(taskID, bcrt, wcrt, jitter);
    }

    public double getSystemAvgWCRT() {
        Double sum = 0.0;
        for (Flow f : this.flows) {
            sum += f.getFlowWCRT();
        }
        return sum / this.flows.size();
    }

    public double[][] getTasksWCRTAsArray() {
        int maxLength = 0;
        for (Flow f: flows){
            maxLength = (f.getTasks().size() > maxLength) ? f.getTasks().size() : maxLength;
        }
        double [][] wcrts = new double[flows.size()][maxLength];

        for (int i=0; i<flows.size(); i++){
            for (int j=0; j< flows.get(i).getTasks().size(); j++){
                wcrts[i][j] = flows.get(i).getTasks().get(j).getWcrt();
            }
        }

        return wcrts;

    }

    public double getSystemSchedIndex(){
        List<Double> indices = new ArrayList<>();
        for (Flow f: flows){
            indices.add(f.calculateSchedIndex());
        }
        return Collections.min(indices);
    }

    public boolean isSchedulable() {
        for (Flow f : this.flows) {
            if (f.getFlowWCRT() > f.getDeadline()) {
                return false;
            }
        }
        return true;
    }

    public void setPDSchedulingDeadlines(){
        for (Flow f: flows){
            f.setPDSchedulingDeadlines();
        }
    }

    public void setPDPriorities(){
        for (Flow f: flows){
            f.setPDSchedulingDeadlines();
        }
        for (Processor p: processors){
            p.setDeadlineMonotonicPriorities();
        }
    }

    public void setDeadlineMonotonicPriorities(){
        for (Processor p: processors){
            p.setDeadlineMonotonicPriorities();
        }
    }

    public void calculateApproxLocalResponseTimes(){
        for (Processor p: processors){
            p.calculateApproxLocalResponseTimes();
        }
        for (Flow f: flows){
            f.calculateWCRTfromW();
        }
    }

    public void calculateApproxDeadlinesLocalResponseTimes(){
        for (Processor p: processors){
            p.calculateApproxDeadlinesLocalResponseTimes();
        }
        for (Flow f: flows){
            f.calculateWCRTfromW();
        }
    }

    public void calculateExactLocalResponseTime(){
        for (Processor p: processors){
            p.calculateExactLocalResponseTime();
        }
        for (Flow f: flows){
            f.calculateWCRTfromW();
        }
    }

    // Private methods

    private void create(SystemConfig c) {
        try {

            this.setSystemConfig(c);

            // Add processors
            for (int i = 1; i <= c.getnProcs(); i++) {
                Processor proc = new Processor();
                proc.setId(i);
                proc.setSchedulingPolicy(c.getSchedPolicy());
                processors.add(proc);
            }

            // Add e2e flows and tasks
            Integer singleFlows = (int) (c.getSingleFlows() / 100.0 * c.getnFlows());
            for (int i = 1; i <= c.getnFlows(); i++) {
                Flow flow = new Flow();
                flow.setId(i);

                // Add tasks to e2e flow
                Integer nTasks;
                if (i <= singleFlows) {
                    nTasks = 1;
                } else if (c.getRandomLength()) {
                    nTasks = random.nextInt((c.getnTasks()+1) - 2) + 2; // random integer between [2, number of tasks]
                } else {
                    nTasks = c.getnTasks();
                }

                Processor lastProc = null; // for task processor localization
                for (int j = 1; j <= nTasks; j++) {
                    Task task = new Task();
                    task.setPriority(1);
                    task.setSchedulingDeadline(1.0);
                    flow.addTask(task);
                }
                flows.add(flow);
            }

            // Task localization, and flow periods and deadlines
            for (Flow f : flows) {
                f.locateTasks((List<Processor>) processors, c.getLocalization(), random);
                f.setPeriod(c.getPeriod(), random);
                f.setDeadline(c.getDeadline(), random);
            }

            // Generate Load (task wcet)
            switch (c.getUtilization().getBalancing()) {

                case BALANCED: //every processor with the same utilization
                    for (Processor p : processors) {
                        p.setUtilization(c.getUtilization().getWcetMethod(), 0.01, random);
                        p.scaleUtilization((c.getUtilization().getCurrentU() / 100.0) / 0.01);
                    }
                    break;

                case NON_BALANCED: //each processor has a different utilization
                    // First create the utilization of each processor, so the system have 1% utilization
                    ArrayList<Double> us = new ArrayList<Double>();
                    for (Processor p : processors) {
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

            // Set BCET of tasks
            for (Processor p : processors) {
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
            for (Processor p : (List<Processor>) getProcessors()) {
                p.writeProcessingResource(pw);
                pw.write("\n");
            }
            pw.write("\n");

            // Schedulers
            for (Processor p : (List<Processor>) getProcessors()) {
                p.writeScheduler(pw);
                pw.write("\n");
            }
            pw.write("\n");

            // Operations
            for (Flow mf : (List<Flow>) getFlows()) {
                mf.writeOperations(pw);
            }
            pw.write("\n");

            // Scheduling Servers (Tasks)
            for (Flow mf : (List<Flow>) getFlows()) {
                mf.writeSchedulingServers(pw);
            }
            pw.write("\n");

            // Transactions
            for (Flow mf : (List<Flow>) getFlows()) {
                mf.writeTransaction(pw);
            }

            pw.flush();
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package es.istr.unican.jmastanalysis.system;

import es.istr.unican.jmastanalysis.system.config.deadline.DeadlineConfig;
import es.istr.unican.jmastanalysis.system.config.localization.LocalizationOptions;
import es.istr.unican.jmastanalysis.system.config.period.PeriodConfig;
import es.istr.unican.jmastanalysis.utils.Utils;

import java.io.PrintWriter;
import java.util.*;

import static java.lang.Math.*;

/**
 * Created by juanm on 02/11/2015.
 */
public class Flow {

    private Integer id;

    private Double period;
    private Double deadline;
    private List<Step> steps;

    public Flow() {
        steps = new ArrayList<>();
    }

    public Flow(Integer id, Double period, Double deadline){
        steps = new ArrayList<>();
        this.id = id;
        this.period = period;
        this.deadline = deadline;
    }

    public void addStep(Step aStep) {
        steps.add(aStep);
        aStep.setFlow(this);
        aStep.setId(String.format("%d%d", id, steps.indexOf(aStep) + 1));
    }

    public void locateSteps(List<Processor> procs, LocalizationOptions type, Random r) {

        switch (type) {
            case RANDOM:
                ArrayList<Integer> shuffledProcIndexes =
                        new ArrayList<>(Utils.shuffleToList(procs.size(), steps.size(), r));
                for (int i = 0; i < steps.size(); i++) {
                    steps.get(i).setProcessor(procs.get(shuffledProcIndexes.get(i)));
                }

                break;

            case RANDOM_B:
                // Completely random assignment, without common seed
                for (Step t: steps){
                    int randomProcIndex = Utils.getRandomInt(0, procs.size()-1);
                    t.setProcessor(procs.get(randomProcIndex));
                }
                break;

            case AVOID_CONSECUTIVE:
                Processor lastProc = null;
                Processor tempProc;
                for (Step t : steps) {
                    while (true) {
                        tempProc = procs.get(r.nextInt(procs.size()));
                        if ((tempProc != lastProc) || (procs.size() == 1)) {
                            t.setProcessor(tempProc);
                            lastProc = tempProc;
                            break;
                        }
                    }
                }
                break;
        }
    }

    public  void  setPeriod(Double period){
        this.period = period;
    }

    public void setDeadline(Double deadline){
        this.deadline = deadline;
    }

    public void setPeriod(PeriodConfig c, Random r) {
        switch (c.getDistribution()) {
            case UNIFORM:
                period = r.nextDouble() * (c.getBase() * c.getRatio() - c.getBase()) + c.getBase();
                break;
            case LOG_UNIFORM:
                Double low = log(c.getBase());
                Double high = log(c.getBase() * c.getRatio() + c.getBase());
                Double e = r.nextDouble() * (high - low) + low;
                Double p = exp(e);
                period = floor(p / c.getBase()) * c.getBase();
                break;
                /*
                    gran = period_base
                    e = numpy.random.uniform(low=numpy.log(period_base),high=numpy.log(period_base*period_ratio+gran), size=len(lengths))
                    periods = numpy.exp(e)
                    periods = numpy.floor(periods / gran) * gran
                    return [float(p) for p in periods]
                 */
        }
    }

    public void setDeadline(DeadlineConfig c, Random r) {
        int n = steps.size();
        switch (c.getValue()) {

            // Extremes of segment
            case T:
                deadline = period;
                break;
            case NT:
                deadline = n * period;
                break;
            case NT2:
                deadline = 2.0 * n * period;
                break;

            // NT-T segment divided in 4 subsegments
            case Q1:
                deadline = (period / 4.0) * (n + 3.0);
                break;
            case Q2:
                deadline = (period / 2.0) * (n + 1.0);
                break;
            case Q3:
                deadline = (period / 4.0) * (3.0 * n + 1.0);
                break;

            // NT-T segment divided in 4 subsegments
            case T1:
                deadline = (period / 3.0) * (n + 2.0);
                break;
            case T2:
                deadline = (period / 3.0) * (2.0 * n + 1.0);
                break;

            // Random + K factor
            case RANDOM:
                deadline = r.nextDouble() * (n * period - period) + period;
                break;
            case K:
                deadline = period * c.getValueK();
                break;

        }
    }

    public Double getPeriod() {
        return period;
    }

    public Double getDeadline() {
        return deadline;
    }

    public Double getSumWCET(){
        Double sum = 0.0;
        for (Step t: steps){
            sum += t.getWcet();
        }
        return sum;
    }

    public Double calculateSchedIndex(){
        return (this.getDeadline()-this.getFlowWCRT())/this.getDeadline()*100.0;
    }

    public void setPDSchedulingDeadlines(){
        Double sumWCET = this.getSumWCET();
        for (Step t: steps){
            t.setSchedulingDeadline(this.getDeadline()*t.getWcet()/sumWCET);
        }
    }

    public void calculateWCRTfromW(){
        Double sum = 0.0;
        for (Step t: steps){
            sum += t.getW();
            t.setWcrt(sum);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void printOverview() {
        java.lang.System.out.format("%f : ", period);
        for (Step t : steps) {
            t.printOverview();
            java.lang.System.out.printf(" ");
        }
        java.lang.System.out.printf(": %f\n", deadline);
    }

    public void printResultsOverview() {
        java.lang.System.out.format("%f : ", period);
        for (Step t : steps) {
            java.lang.System.out.printf("%f", t.getWcrt());
            java.lang.System.out.printf(" ");
        }
        java.lang.System.out.printf(": %f -> SI=%f%%\n", deadline, calculateSchedIndex());
    }

    public void setStepResults(String stepID, Double bcrt, Double wcrt, Double jitter) {
        for (Step t : steps) {
            if (t.getId().toLowerCase().equals(stepID.toLowerCase())) {
                t.setBcrt(bcrt);
                t.setWcrt(wcrt);
                t.setJitter(jitter);
            }
        }
    }

    public Double getFlowWCRT() {
        return this.steps.get(this.steps.size() - 1).getWcrt();
    }

    public void writeOperations(PrintWriter pw) {
        for (Step mt : (List<Step>) getSteps()) {
            mt.writeOperation(pw);
        }
    }

    public void writeSchedulingServers(PrintWriter pw) {
        for (Step mt : (List<Step>) getSteps()) {
            mt.writeSchedulingServer(pw);
        }
    }

    public void writeTransaction(PrintWriter pw) {
        pw.format("Transaction (\n");
        pw.format("      Type              => Regular,\n");
        pw.format("      Name              => T_%d,\n", getId());
        pw.format("      External_Events   => (\n");
        pw.format("                 (  Type        => Periodic,\n");
        pw.format("                    Name        => EE_%d,\n", getId());
        pw.format(Locale.US, "                    Period      => %f,\n", getPeriod());
        pw.format("                    Max_Jitter  => 0,\n");
        pw.format("                    Phase       => 0)),\n\n");

        pw.format("      Internal_Events   => (\n");
        Iterator<Step> iterator = ((List<Step>) getSteps()).iterator();
        while (true) {
            pw.format("              (Type     => Regular,\n");
            pw.format("               Name     => IE_%s", iterator.next().getId());
            if (iterator.hasNext()) {
                pw.format("),\n");
            } else {
                pw.format(",\n");
                pw.format("               Timing_Requirements      => (\n");
                pw.format("                           Type         => Hard_Global_Deadline,\n");
                pw.format(Locale.US, "                           Deadline     => %f,\n", getDeadline());
                pw.format("                           Referenced_Event     => EE_%d))),\n\n", getId());
                break;
            }
        }

        Iterator<Step> iterator2 = ((List<Step>) getSteps()).iterator(); //not possible to rewind iterator
        String prevID = String.format("EE_%d", getId());
        pw.format("      Event_Handlers            => (\n");
        Step mt = iterator2.next();
        while (true) {
            pw.format("              (Type                => Activity,\n");
            pw.format("               Input_Event         => %s,\n", prevID);
            prevID = String.format("IE_%s", mt.getId());
            pw.format("               Output_Event        => IE_%s,\n", mt.getId());
            pw.format("               Activity_Operation  => O_%s,\n", mt.getId());
            pw.format("               Activity_Server     => SS_%s)", mt.getId());
            if (iterator2.hasNext()) {
                pw.format(",\n");
                mt = iterator2.next();
            } else {
                pw.format("\n");
                break;
            }
        }
        pw.format("         )\n");

        pw.format(");\n\n");
    }
}

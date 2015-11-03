package es.istr.unican.jmastanalysis;

import es.istr.unican.jmastanalysis.analysis.MastTool;
import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
import es.istr.unican.jmastanalysis.analysis.config.HOSPAConfig;
import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.system.MastSystem;
import es.istr.unican.jmastanalysis.system.config.SystemConfig;
import es.istr.unican.jmastanalysis.system.config.deadline.DeadlineConfig;
import es.istr.unican.jmastanalysis.system.config.load.LoadBalancingOptions;
import es.istr.unican.jmastanalysis.system.config.load.UtilizationConfig;
import es.istr.unican.jmastanalysis.system.config.load.WCETGenerationOptions;
import es.istr.unican.jmastanalysis.system.config.localization.LocalizationOptions;
import es.istr.unican.jmastanalysis.system.config.period.PeriodConfig;
import es.istr.unican.jmastanalysis.system.config.period.PeriodDistributionOptions;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        // System Configuration
        SystemConfig s = new SystemConfig();
        PeriodConfig p = new PeriodConfig();
        UtilizationConfig u = new UtilizationConfig();
        s.setSeed(10);
        s.setnProcs(1);
        s.setnFlows(2);
        s.setnTasks(2);
        s.setRandomLength(false);
        s.setSingleFlows(0f);
        s.setSchedPolicy("FP");
        s.setLocalization(LocalizationOptions.RANDOM);
        p.setBase(10f);
        p.setDistribution(PeriodDistributionOptions.UNIFORM);
        p.setRatio(10f);
        s.setPeriod(p);
        u.setBalancing(LoadBalancingOptions.BALANCED);
        u.setWcetMethod(WCETGenerationOptions.SCALE);
        u.setBcetFactor(0f);
        u.setStart(10);
        u.setStep(1);
        u.setTop(80);
        u.setCurrentU(10);
        s.setUtilization(u);
        DeadlineConfig d = new DeadlineConfig("NT");
        s.setDeadline(d);

        // Mast Analysis Configuration
        MastConfig m = new MastConfig();
        HOSPAConfig h = new HOSPAConfig();
        m.setName("Ejemplo");
        m.setWorkPath(".");
        m.setMastPath("C:\\Users\\JuanCTR\\CTR\\MAST\\mast_analysis\\exe\\mast_analysis.exe");
        m.setAnalysis(AnalysisOptions.HOLISTIC);
        m.setSync(false);
        m.setAssignment(AssignmentOptions.NONE);
        m.setHospaConfig(h);
        m.setStopFactor(10.0f);
        m.setGsd(false);
        m.setDsFactor(1);
        m.setCalculateSlack(false);
        m.setJitterAvoidance(false);

        // Exact analysis for independent tasks
        MastSystem sys = new MastSystem(s);
        sys.setPDPriorities();
        sys.calculateExactResponseTimes();
        sys.printOverview();
        System.out.println("");
        System.out.println("Exact analysis for independent tasks");
        sys.printResultsOverview();
        System.out.println(sys.getSystemSchedIndex());
        System.out.println("");

        // Holistic Analysis
        System.out.println("Holistic analysis");
        MastTool.analyze(sys, m);
        sys.printResultsOverview();
        System.out.println(sys.getSystemSchedIndex());
        System.out.println("");



    }
}

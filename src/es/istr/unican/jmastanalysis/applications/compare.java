package es.istr.unican.jmastanalysis.applications;

import es.istr.unican.jmastanalysis.analysis.MastTool;
import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
import es.istr.unican.jmastanalysis.analysis.config.HOSPAConfig;
import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.exceptions.InterruptedAnalysis;
import es.istr.unican.jmastanalysis.system.MastSystem;
import es.istr.unican.jmastanalysis.system.config.SystemConfig;
import es.istr.unican.jmastanalysis.system.config.deadline.DeadlineConfig;
import es.istr.unican.jmastanalysis.system.config.load.LoadBalancingOptions;
import es.istr.unican.jmastanalysis.system.config.load.UtilizationConfig;
import es.istr.unican.jmastanalysis.system.config.load.WCETGenerationOptions;
import es.istr.unican.jmastanalysis.system.config.localization.LocalizationOptions;
import es.istr.unican.jmastanalysis.system.config.period.PeriodConfig;
import es.istr.unican.jmastanalysis.system.config.period.PeriodDistributionOptions;
import es.istr.unican.jmastanalysis.utils.Utils;

import javax.rmi.CORBA.Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * Created by juanm on 03/11/2015.
 */
public class compare {

    public static void main(String[] args) {
        // System Configuration
        SystemConfig s = new SystemConfig();
        PeriodConfig p = new PeriodConfig();
        UtilizationConfig u = new UtilizationConfig();
        //s.setSeed(10);
        s.setnProcs(3);
        s.setnFlows(5);
        //s.setnTasks(7);
        s.setRandomLength(false);
        s.setSingleFlows(0f);
        s.setSchedPolicy("FP");
        //s.setLocalization(LocalizationOptions.RANDOM);
        p.setBase(10f);
        p.setDistribution(PeriodDistributionOptions.UNIFORM);
        p.setRatio(10f);
        s.setPeriod(p);
        u.setBalancing(LoadBalancingOptions.BALANCED);
        u.setWcetMethod(WCETGenerationOptions.UUNIFAST);
        u.setBcetFactor(0f);
        u.setStart(10);
        u.setStep(1);
        u.setTop(80);
        //u.setCurrentU(50);
        s.setUtilization(u);
        DeadlineConfig d = new DeadlineConfig("NT");
        s.setDeadline(d);

        // Mast Analysis Configuration
        MastConfig m = new MastConfig();
        HOSPAConfig h = new HOSPAConfig();
        m.setName("Ejemplo");
        m.setWorkPath(".");
        m.setMastPath("C:\\Users\\JuanCTR\\CTR\\MAST\\mast_analysis\\exe\\mast_analysis.exe");
        //m.setMastPath("D:\\Development\\MAST\\mast_svn\\mast_analysis.exe");
        //m.setAnalysis(AnalysisOptions.HOLISTIC);
        m.setSync(false);
        m.setAssignment(AssignmentOptions.NONE);
        m.setHospaConfig(h);
        m.setStopFactor(10.0f);
        m.setGsd(false);
        m.setDsFactor(1);
        m.setCalculateSlack(false);
        m.setJitterAvoidance(false);


        int agreeOpt = 0;
        int agreeHol = 0;
        int agreeExact = 0;
        int agreeApprox = 0;

        MastSystem sysA = null;
        MastSystem sysB = null;
        for (int seed=1000; seed<=1100; seed++){

            s.setSeed(seed);
            int ntasks = Utils.getRandomInt(3, 10);
            int utilization = Utils.getRandomInt(10, 60);
            s.setnTasks(ntasks);
            u.setCurrentU(utilization);
            s.setUtilization(u);

            System.out.printf("%4d (L=%2d U=%2d)\tA -> opt=",
                    seed,
                    ntasks, utilization);

            // System A

            s.setLocalization(LocalizationOptions.RANDOM_B);
            sysA = new MastSystem(s);
            //System.out.println("System A");
            //sysA.printOverview();
            sysA.setPDPriorities();

            try {
                m.setAnalysis(AnalysisOptions.OFFSET_OPT);
                MastTool.analyze(sysA, m);
                Double siOptA = sysA.getSystemSchedIndex();
                System.out.printf("%2.2f hol=", siOptA);

                sysA = new MastSystem(s);
                sysA.setPDPriorities();
                m.setAnalysis(AnalysisOptions.HOLISTIC);
                MastTool.analyze(sysA, m);
                Double siHolA = sysA.getSystemSchedIndex();
                System.out.printf("%2.2f exact=", siHolA);

                sysA = new MastSystem(s);
                sysA.setPDPriorities();
                sysA.calculateExactLocalResponseTime();
                Double siExactA = sysA.getSystemSchedIndex();
                System.out.printf("%2.2f approx=", siExactA);

                sysA = new MastSystem(s);
                sysA.setPDPriorities();
                sysA.calculateApproxLocalResponseTimes();
                Double siApproxA = sysA.getSystemSchedIndex();
                System.out.printf("%2.2f\n\t\t\t\t\tB -> opt=", siApproxA);


                // System B

                s.setLocalization(LocalizationOptions.RANDOM_B);
                sysB = new MastSystem(s);
                //System.out.println("System B");
                //sysB.printOverview();
                sysB.setPDPriorities();

                m.setAnalysis(AnalysisOptions.OFFSET_OPT);
                MastTool.analyze(sysB, m);
                Double siOptB = sysB.getSystemSchedIndex();
                System.out.printf("%2.2f hol=", siOptB);

                sysB = new MastSystem(s);
                sysB.setPDPriorities();
                m.setAnalysis(AnalysisOptions.HOLISTIC);
                MastTool.analyze(sysB, m);
                Double siHolB = sysB.getSystemSchedIndex();
                System.out.printf("%2.2f exact=", siHolB);

                sysB = new MastSystem(s);
                sysB.setPDPriorities();
                sysB.calculateExactLocalResponseTime();
                Double siExactB = sysB.getSystemSchedIndex();
                System.out.printf("%2.2f approx=", siExactB);

                sysB = new MastSystem(s);
                sysB.setPDPriorities();
                sysB.calculateApproxLocalResponseTimes();
                Double siApproxB = sysB.getSystemSchedIndex();
                System.out.printf("%2.2f\n", siApproxB);

                // Comparamos A y B

                boolean ref = siOptA > siOptB; // usamos optimized analysis como referencia
                boolean hol = siHolA > siHolB;
                boolean exact = siExactA > siExactB;
                boolean approx = siApproxA > siApproxB;

                if (ref == ref) agreeOpt += 1; // as reference
                if (hol == ref) agreeHol += 1;
                if (exact == ref) agreeExact += 1;
                if (approx == ref) agreeApprox += 1;

            }  catch (InterruptedAnalysis e){
                e.printStackTrace();
                System.out.println("INTERRUPTED");
            }
        }

        System.out.println("\nConclusiones");
        System.out.printf("Reference : %d\n", agreeOpt);
        System.out.printf("Holistic  : %d\n", agreeHol);
        System.out.printf("Exact     : %d\n", agreeExact);
        System.out.printf("Approx    : %d\n", agreeApprox);

//        // Generate System
//        MastSystem sys = new MastSystem(s);
//        sys.setPDPriorities();
//        System.out.println("System Overview");
//        sys.printOverview();
//        System.out.println("");
//
//        // Approximate analysis for independent tasks
//        sys.calculateApproxLocalResponseTimes();
//        System.out.println("Approximate analysis for independent tasks");
//        sys.printResultsOverview();
//        System.out.println(sys.getSystemSchedIndex());
//        System.out.println("");
//
//        // Exact analysis for independent tasks
//        sys.calculateExactLocalResponseTime();
//        System.out.println("Exact analysis for independent tasks");
//        sys.printResultsOverview();
//        System.out.println(sys.getSystemSchedIndex());
//        System.out.println("");
//
//        // Holistic Analysis
//        System.out.println("Holistic analysis");
//        MastTool.analyze(sys, m);
//        sys.printResultsOverview();
//        System.out.println(sys.getSystemSchedIndex());
//        System.out.println("");

    }
}

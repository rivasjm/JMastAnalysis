package es.istr.unican.jmastanalysis.applications.mario;

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

/**
 * Created by JuanCTR on 05/11/2015.
 * Calculates agreement and false positives with respect Offset Based Optimized, of several techniques:
 *  - Holistic
 *  - Exact analysis for independent tasks
 *  - Approximate analysis for independent tasks and D>T
 *  - Approximate analysis
 */

public class compareForTrimming {

    public static void main(String[] args) {

        // System Configuration

        SystemConfig s = new SystemConfig();
        PeriodConfig p = new PeriodConfig();
        UtilizationConfig u = new UtilizationConfig();
        //s.setSeed(10);
        s.setnProcs(4);
        s.setnFlows(4);
        //s.setnSteps(7);
        s.setRandomLength(true);
        s.setSingleFlows(0f);
        s.setSchedPolicy("FP");
        s.setLocalization(LocalizationOptions.RANDOM_B);
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
        DeadlineConfig d = new DeadlineConfig("T2");
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


        // Generate ana analyze systems

        MastSystem sys = null;

        boolean optIsSched;
        boolean holIsSched;
        boolean exactIsSched;
        boolean approxDeadlineIsSched;
        boolean approxIsSched;

        int agreeOpt = 0; // Reference
        int agreeHol = 0;
        int agreeExact = 0;
        int agreeApproxDeadline = 0;
        int agreeApprox = 0;

        int notSchedulableOpt = 0;
        int falsePositiveHol = 0;
        int falsePositiveExact = 0;
        int falsePositiveApproxDeadline = 0;
        int falsePositiveApprox = 0;

        for (int seed=1; seed<=100; seed++){

            try {

                // Generates the system
                s.setSeed(seed);
                int nsteps = Utils.getRandomInt(10, 15);
                int utilization = Utils.getRandomInt(65, 70);
                s.setnSteps(nsteps);
                u.setCurrentU(utilization);
                s.setUtilization(u);
                sys = new MastSystem(s);
                sys.setPDPriorities();
                System.out.printf("%3d (nSteps=%3d, u=%3d) | opt=", seed, nsteps, utilization);

                // Offset based optimized (ref)
                m.setAnalysis(AnalysisOptions.OFFSET_OPT);
                MastTool.analyze(sys, m);
                optIsSched = sys.isSchedulable();
                System.out.printf("%b hol=", optIsSched);

                // Holistic
                m.setAnalysis(AnalysisOptions.HOLISTIC);
                MastTool.analyze(sys, m);
                holIsSched = sys.isSchedulable();
                System.out.printf("%b exact=", holIsSched);

                // Exact (for independent tasks)
                sys.calculateExactLocalResponseTime();
                exactIsSched = sys.isSchedulable();
                System.out.printf("%b approxD=", exactIsSched);

                // Approx (for independent tasks, D>T)
                sys.calculateApproxDeadlinesLocalResponseTimes();
                approxDeadlineIsSched = sys.isSchedulable();
                System.out.printf("%b approx=", approxDeadlineIsSched);

                // Approx (for independent tasks)
                sys.calculateApproxLocalResponseTimes();
                approxIsSched = sys.isSchedulable();
                System.out.printf("%b\n", approxIsSched);

                // Calculate agreements
                if (optIsSched == optIsSched) agreeOpt += 1;
                if (holIsSched == optIsSched) agreeHol += 1;
                if (exactIsSched == optIsSched) agreeExact += 1;
                if (approxDeadlineIsSched == optIsSched) agreeApproxDeadline += 1;
                if (approxIsSched == optIsSched) agreeApprox += 1;

                // Calculate false positives (opt not schedulable, but technique is schedulable)
                if (!optIsSched){
                    notSchedulableOpt += 1;
                    if (holIsSched) falsePositiveHol += 1;
                    if (exactIsSched) falsePositiveExact += 1;
                    if (approxDeadlineIsSched) falsePositiveApproxDeadline += 1;
                    if (approxIsSched) falsePositiveApprox += 1;
                }


            } catch (InterruptedAnalysis e) {
                System.out.println("INTERRUPTED");
            }

        }

        System.out.println("\n\nAgreement with Reference (Opt):");
        System.out.printf(" Ref: %d\n", agreeOpt);
        System.out.printf(" Holistic: %d (%2.2f%%)\n", agreeHol, agreeHol/(float)agreeOpt*100.0);
        System.out.printf(" Exact: %d (%2.2f%%)\n", agreeExact, agreeExact/(float)agreeOpt*100.0);
        System.out.printf(" Approx (D>T): %d (%2.2f%%)\n", agreeApproxDeadline, agreeApproxDeadline/(float)agreeOpt*100.0);
        System.out.printf(" Approx : %d (%2.2f%%)\n\n", agreeApprox, agreeApprox/(float)agreeOpt*100.0);

        System.out.println("False positives with Reference (Opt):");
        System.out.printf(" Non schedulable by Ref: %d\n", notSchedulableOpt);
        System.out.printf(" Holistic: %d\n", falsePositiveHol);
        System.out.printf(" Exact: %d\n", falsePositiveExact);
        System.out.printf(" Approx (D>T): %d\n", falsePositiveApproxDeadline);
        System.out.printf(" Approx : %d\n\n", falsePositiveApprox);

    }

}

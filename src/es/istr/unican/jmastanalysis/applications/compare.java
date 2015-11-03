package es.istr.unican.jmastanalysis.applications;

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

    private static String dbName = "results.db";

    public static void createTable(){
        Connection con = null;
        Statement stm = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbName));

            stm = con.createStatement();
            String sql = "CREATE TABLE RESULTS "+
                    "(SEED_A INT, NTASKS_A INT, U_A INT, SEED_B INT, NTASKS_B INT, U_B INT," +
                    "OPT BIT, HOL BIT, EXACT BIT, APPROX BIT," +
                    "TIME_HOL REAL, TIME_EXACT REAL, TIME_APPROX REAL";
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            System.out.println("Table created");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRow(int seedA, int ntasksA, int uA, int seedB, int ntasksB, int uB,
                              boolean opt, boolean hol, boolean exact, boolean approx,
                              double timeHol, double timeExact, double timeApprox) {
        Connection con = null;
        PreparedStatement p = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbName));

            String query = "INSERT INTO RESULTS (SEED,NTASKS,U,SI_OPT,SI_HOL,SI_EXACT,SI_APPROX,TIME_HOL,TIME_EXACT,TIME_APPROX) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            p = con.prepareStatement(query);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // System Configuration
        SystemConfig s = new SystemConfig();
        PeriodConfig p = new PeriodConfig();
        UtilizationConfig u = new UtilizationConfig();
        //s.setSeed(10);
        s.setnProcs(1);
        s.setnFlows(10);
        //s.setnTasks(7);
        s.setRandomLength(false);
        s.setSingleFlows(0f);
        s.setSchedPolicy("FP");
        s.setLocalization(LocalizationOptions.RANDOM);
        p.setBase(10f);
        p.setDistribution(PeriodDistributionOptions.UNIFORM);
        p.setRatio(100f);
        s.setPeriod(p);
        u.setBalancing(LoadBalancingOptions.BALANCED);
        u.setWcetMethod(WCETGenerationOptions.SCALE);
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
        //m.setMastPath("C:\\Users\\JuanCTR\\CTR\\MAST\\mast_analysis\\exe\\mast_analysis.exe");
        m.setMastPath("D:\\Development\\MAST\\mast_svn\\mast_analysis.exe");
        //m.setAnalysis(AnalysisOptions.HOLISTIC);
        m.setSync(false);
        m.setAssignment(AssignmentOptions.NONE);
        m.setHospaConfig(h);
        m.setStopFactor(100.0f);
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
        for (int seed=1; seed<=100; seed++){

            s.setSeed(seed);

            // System A

            int ntasks_A = Utils.getRandomInt(2, 10);
            int u_A = Utils.getRandomInt(10, 60);
            //System.out.printf("A: L=%d U=%d || ", ntasks_A, u_A);
            s.setnTasks(ntasks_A);
            u.setCurrentU(u_A);
            s.setUtilization(u);

            sysA = new MastSystem(s);
            sysA.setPDPriorities();
            m.setAnalysis(AnalysisOptions.OFFSET_OPT);
            MastTool.analyze(sysA, m);
            Double siOptA = sysA.getSystemSchedIndex();

            sysA = new MastSystem(s);
            sysA.setPDPriorities();
            m.setAnalysis(AnalysisOptions.HOLISTIC);
            MastTool.analyze(sysA, m);
            Double siHolA = sysA.getSystemSchedIndex();

            sysA = new MastSystem(s);
            sysA.setPDPriorities();
            sysA.calculateApproxLocalResponseTimes();
            Double siApproxA = sysA.getSystemSchedIndex();

            sysA = new MastSystem(s);
            sysA.setPDPriorities();
            sysA.calculateExactLocalResponseTime();
            Double siExactA = sysA.getSystemSchedIndex();


            // System B

            int ntasks_B = Utils.getRandomInt(2, 10);
            int u_B = Utils.getRandomInt(10, 70);
            //System.out.printf("B: L=%d U=%d\n", ntasks_B, u_B);
            s.setnTasks(ntasks_B);
            u.setCurrentU(u_B);
            s.setUtilization(u);

            sysB = new MastSystem(s);
            sysB.setPDPriorities();
            m.setAnalysis(AnalysisOptions.OFFSET_OPT);
            MastTool.analyze(sysB, m);
            Double siOptB = sysB.getSystemSchedIndex();

            sysB = new MastSystem(s);
            sysB.setPDPriorities();
            m.setAnalysis(AnalysisOptions.HOLISTIC);
            MastTool.analyze(sysB, m);
            Double siHolB = sysB.getSystemSchedIndex();

            sysB = new MastSystem(s);
            sysB.setPDPriorities();
            sysB.calculateApproxLocalResponseTimes();
            Double siApproxB = sysB.getSystemSchedIndex();

            sysB = new MastSystem(s);
            sysB.setPDPriorities();
            sysB.calculateExactLocalResponseTime();
            Double siExactB = sysB.getSystemSchedIndex();

            System.out.printf("%d\tA(L=%2d U=%2d) -> opt=%2.2f hol=%2.2f exact=%2.2f approx=%2.2f\n\tB(L=%2d U=%2d) -> opt=%2.2f hol=%2.2f exact=%2.2f approx=%2.2f\n",
                    seed,
                    ntasks_A, u_A, siOptA, siHolA, siExactA, siApproxA,
                    ntasks_B, u_B, siOptB, siHolB, siExactB, siApproxB);


            // Comparamos A y B

            boolean ref = siOptA > siOptB; // usamos optimized analysis como referencia
            boolean hol = siHolA > siHolB;
            boolean exact = siExactA > siExactB;
            boolean approx = siApproxA > siApproxB;

            if (ref == ref) agreeOpt += 1; // as reference
            if (hol == ref) agreeHol += 1;
            if (exact == ref) agreeExact += 1;
            if (approx == ref) agreeApprox += 1;
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

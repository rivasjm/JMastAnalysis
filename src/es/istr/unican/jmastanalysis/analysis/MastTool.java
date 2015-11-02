package es.istr.unican.jmastanalysis.analysis;

import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.analysis.results.REALTIMESITUATION;
import es.istr.unican.jmastanalysis.analysis.results.TimingResult;
import es.istr.unican.jmastanalysis.analysis.results.TransactionResults;
import es.istr.unican.jmastanalysis.system.MastSystem;
import org.apache.commons.io.FilenameUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by juanm on 19/08/2015.
 */
public class MastTool {

    public static void analyze(MastSystem system, MastConfig config) {


        // Checks if mast_analysis.exe exists, and is executable
        if (!new File(config.getMastPath()).canExecute()) {
            System.out.println("ERROR: mast_analysis.exe could not be found");
            return;
        }

        // Prepare input and output(results) files locations
        File baseDir = new File(config.getWorkPath());
        baseDir.mkdirs();

        String inputFilePath = FilenameUtils.concat(config.getWorkPath(), String.format("system%d.txt", new Double(system.getSystemUtilization() * 100).intValue()));
        String outputFilePath = FilenameUtils.concat(config.getWorkPath(), String.format("results%d.xml", new Double(system.getSystemUtilization() * 100).intValue()));

        //Write system to file
        system.writeSystem(new File(inputFilePath));

        //Prepares MAST arguments string
        ArrayList<String> args = new ArrayList<>();

        //MAST Path
        args.add(config.getMastPath());

        //Analysis Tool
        args.add(AnalysisOptions.mapArg(config.getAnalysis()));

        //Clock Synchronization
        if (!config.getSync()) args.add("-l");

        //Analysis stop factor
        if (config.getStopFactor() > 0.0)
            args.add(String.format(Locale.US, "-f %d", config.getStopFactor().intValue()));

        //Force global scheduling deadlines (LC-EDF-GSD)
        if (config.getGsd()) args.add("-gsd");

        //LC-EDF-DS factor
        if (config.getDsFactor() > 0 && config.getDsFactor() != 1)
            args.add(String.format(Locale.US, "-sf %f", config.getDsFactor()));

        //Slack
        if (config.getCalculateSlack()) args.add("-s");

        //Jitter Avoidance
        if (config.getJitterAvoidance()) args.add("-jitter_avoidance");

        //Scheduling Parameter assignment tool
        args.add(AssignmentOptions.mapArg(config.getAssignment()));

        //Input and output files
        args.add(inputFilePath);
        args.add(outputFilePath);

        //Create string
        String cmd = String.join(" ", args);
        //system.out.println(cmd);

        //Store tool configuration in system
        system.setToolConfig(config);

        //Execute MAST Tool
        long beforeTime = System.nanoTime();
        Runtime r = Runtime.getRuntime();
        Process p = null;

        //Check if input file is not yet created
//        while (true) {
//            System.out.println(inputFilePath);
//            System.out.println("File length: "+new File(inputFilePath).length());
//            break;
//        }

        try {
            p = r.exec(cmd);

            // IMPORTANTE: Cuando ejecuto el analisis de un sistema grande, el proceso nunca regresa. Esto ocurre pq la
            // salida del proceso es demasiado grande, y necesita ser leida para vaciar los bufferes.
            // (cuanto mas grande es el sistema, mas grande es lo que sale por pantalla cuando se analiza).
            // Leyendo el inputstream del proceso, vacio los bufferes, y el proceso puede finalizar.
            // java.lang
            // Class Process
            // Because some native platforms only provide limited buffer size for standard input and output streams,
            // failure to promptly write the input stream or read the output stream of the subprocess may cause the
            // subprocess to block, and even deadlock.

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((reader.readLine()) != null) {}

            p.waitFor();

//            System.out.println(cmd);
//            String text = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8.name());
//            System.out.println(text);

            long afterTime = System.nanoTime();
            integrateMASTResults(new File(outputFilePath), system);
            system.setToolTimeElapsed(afterTime - beforeTime);

            // Detele files
            new File(inputFilePath).delete();
            new File(outputFilePath).delete();
            new File("mast_parser.lis").delete();

        } catch (IOException | InterruptedException e) {
            System.out.println("ERROR executing :" + cmd);
            e.printStackTrace();
        }
    }

    private static void integrateMASTResults(File results, MastSystem system) {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(REALTIMESITUATION.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            REALTIMESITUATION element = (REALTIMESITUATION) jaxbUnmarshaller.unmarshal(results);

            Integer flowID;
            String taskID;
            Double bcrt;
            Double wcrt;
            Double jitter;

            for (Object o : element.getSlackOrTraceOrProcessingResource()) {
                if (o instanceof TransactionResults) {
                    TransactionResults tr = (TransactionResults) o;

                    flowID = Integer.valueOf(tr.getName().replaceAll("\\D+", ""));

                    for (Object to : tr.getSlackOrTimingResultOrSimulationTimingResult()) {
                        if (to instanceof TimingResult) {
                            TimingResult timing = (TimingResult) to;

                            taskID = String.format("%s", timing.getEventName().replaceAll("\\D+", ""));
                            bcrt = timing.getBestGlobalResponseTimes().getGlobalResponseTime().get(0).getTimeValue();
                            wcrt = timing.getWorstGlobalResponseTimes().getGlobalResponseTime().get(0).getTimeValue();
                            jitter = timing.getJitters().getGlobalResponseTime().get(0).getTimeValue();
                            system.setTaskResults(flowID, taskID, bcrt, wcrt, jitter);

                        }
                    }
                }
            }


        } catch (JAXBException e) {
            System.out.println("Error in results file: " + results.getAbsolutePath());
            e.printStackTrace();
        }
    }
}

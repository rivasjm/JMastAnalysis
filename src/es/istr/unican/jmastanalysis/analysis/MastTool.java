package es.istr.unican.jmastanalysis.analysis;

import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.analysis.results.REALTIMESITUATION;
import es.istr.unican.jmastanalysis.analysis.results.TimingResult;
import es.istr.unican.jmastanalysis.analysis.results.TransactionResults;
import es.istr.unican.jmastanalysis.exceptions.InterruptedAnalysis;
import es.istr.unican.jmastanalysis.system.MSystem;
import org.apache.commons.exec.*;
import org.apache.commons.io.FilenameUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by juanm on 19/08/2015.
 */
public class MastTool {

    public static void analyze(MSystem system, MastConfig config) throws InterruptedAnalysis{


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

        try {
            //Execute MAST Tool
            long beforeTime = System.nanoTime();
            launchCommand(cmd, 10000L, null);
            System.out.println(cmd);
            long afterTime = System.nanoTime();

            //Integrate results
            integrateMASTResults(new File(outputFilePath), system);
            system.setToolTimeElapsed(afterTime - beforeTime);
        } catch (InterruptedAnalysis e){
            throw e;
        } finally {
            // Detele files
            //new File(inputFilePath).delete();
            //new File(outputFilePath).delete();
            //new File("mast_parser.lis").delete();
        }
    }

    public static void launchCommand(String command, Long timeoutSeconds, String stdOutputFile) throws InterruptedAnalysis{
        try {
            // Define executor
            CommandLine commandLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);

            // Standard Output Handling
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);

            ExecuteWatchdog dog = null;
            if (timeoutSeconds != null) {
                // Define watchdog (for timeout)
                dog = new ExecuteWatchdog(timeoutSeconds);
                executor.setWatchdog(dog);
            }

            // Execute command
            int exitValue = executor.execute(commandLine);

            if (stdOutputFile != null){
                // Save standard output of the process
                OutputStream fileOutput = new FileOutputStream(stdOutputFile);
                outputStream.writeTo(fileOutput);
            }

            if (dog != null){
                if (dog.killedProcess()){
                    throw new InterruptedAnalysis();
                }
            }

        } catch (ExecuteException e){
            //e.printStackTrace();
        } catch (IOException e){
            //e.printStackTrace();
        }
    }

    private static void integrateMASTResults(File results, MSystem system) {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(REALTIMESITUATION.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            REALTIMESITUATION element = (REALTIMESITUATION) jaxbUnmarshaller.unmarshal(results);

            Integer flowID;
            String stepID;
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

                            stepID = String.format("%s", timing.getEventName().replaceAll("\\D+", ""));
                            bcrt = timing.getBestGlobalResponseTimes().getGlobalResponseTime().get(0).getTimeValue();
                            wcrt = timing.getWorstGlobalResponseTimes().getGlobalResponseTime().get(0).getTimeValue();
                            jitter = timing.getJitters().getGlobalResponseTime().get(0).getTimeValue();
                            system.setStepResults(flowID, stepID, bcrt, wcrt, jitter);

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

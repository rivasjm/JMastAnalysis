package es.istr.unican.jmastanalysis.tests;

import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by juanm on 04/11/2015.
 */
public class TestProcess {

    public static void main(String[] args) {

        try {
            String line = "mast_analysis.exe offset_based_approx_w_pr -v system39.txt system39.xml";
            CommandLine commandLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);

            executor.setExitValue(1);
            ExecuteWatchdog dog = new ExecuteWatchdog(10000L);
            executor.setWatchdog(dog);
            int exitValue = executor.execute(commandLine);
            System.out.println(exitValue);

            if (dog.killedProcess()){
                System.out.println("Dog killed the process");
            }

        } catch (ExecuteException e){
           // e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}

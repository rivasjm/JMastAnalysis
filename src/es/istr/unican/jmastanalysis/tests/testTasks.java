package es.istr.unican.jmastanalysis.tests;

import es.istr.unican.jmastanalysis.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrador on 26/04/2016.
 */
public class testTasks {

    public static void main(String[] args) {

        Integer nProcs = 5;
        Integer nFlows = 21;
        Integer nTasks = 21;
        Integer nSteps = 60;

        List<Processor> processors = new ArrayList<>();
        List<Flow> flows = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        for (int i=0; i<nProcs; i++){
            processors.add(new Processor(i+1, "FP"));
        }

        for (int i=0; i<nTasks; i++){
            Task task = new Task(i+1, processors.get(i%5));
            task.setPriority(i+10);
            tasks.add(task);
        }

        for (int i=0; i<nFlows; i++){
            Flow flow = new Flow(i+1, (i+1)*10000.0, (i+1)*10000.0*nSteps);

            for (int j=0; j<nSteps; j++){
                Step step = new Step((j+1)*2.0, 0.0, tasks.get(j % nTasks), 50);
                flow.addStep(step);
            }
            flows.add(flow);
        }

        MastSystem mastSystem = new MastSystem(flows, processors, tasks);

        mastSystem.writeSystem(new File("system.txt"));
    }

}

package es.istr.unican.jmastanalysis.tests;

import es.istr.unican.jmastanalysis.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrador on 29/04/2016.
 */
public class TestReference {

    public static void main(String[] args) {

        Integer nProcs = 5;
        Integer nFlows = 21;
        Integer nTasks = 21;
        Integer nSteps = 60;

        List<MProcessor> processors = new ArrayList<>();
        List<MFlow> flows = new ArrayList<>();
        List<MTask> tasks = new ArrayList<>();

        for (int i=0; i<nProcs; i++){
            processors.add(new MProcessor(i+1, "FP"));
        }

        MTask task = null;
        for (int i=0; i<nTasks; i++){
            task = new MTask(i+1, processors.get(i%5));
            task.setPriority(i+10);
            tasks.add(task);
        }

        for (int i=0; i<nFlows; i++){
            MFlow flow = new MFlow(i+1, (i+1)*10000.0, (i+1)*10000.0*nSteps);

            for (int j=0; j<nSteps; j++){
                MStep step = new MStep((j+1)*2.0, 0.0, tasks.get(j % nTasks), 50);
                flow.addStep(step);
            }
            flows.add(flow);
        }

        MSystem mastSystem = new MSystem(flows, processors, tasks);

        mastSystem.writeSystem(new File("system.txt"));

    }

}

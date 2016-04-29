package es.istr.unican.jmastanalysis.tests;

import es.istr.unican.jmastanalysis.system.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrador on 28/04/2016.
 */
public class testOverriden {

    public static void main(String[] args) {

        Integer nProcs = 1;
        Integer nFlows = 2;
        Integer nTasks = 2;
        Integer nSteps = 2;

        List<MProcessor> processors = new ArrayList<>();
        List<MFlow> flows = new ArrayList<>();
        List<MTask> tasks = new ArrayList<>();

        for (int i=0; i<nProcs; i++){
            processors.add(new MProcessor(i+1, "FP"));
        }

        for (int i=0; i<nTasks; i++){
            MTask task = new MTask(i+1, processors.get(i%5));
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

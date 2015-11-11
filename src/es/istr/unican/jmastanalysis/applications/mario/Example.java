package es.istr.unican.jmastanalysis.applications.mario;

import es.istr.unican.jmastanalysis.analysis.MastTool;
import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
import es.istr.unican.jmastanalysis.exceptions.InterruptedAnalysis;
import es.istr.unican.jmastanalysis.system.Flow;
import es.istr.unican.jmastanalysis.system.MastSystem;
import es.istr.unican.jmastanalysis.system.Processor;
import es.istr.unican.jmastanalysis.system.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by juanm on 11/11/2015.
 */
public class Example {

    public static void main(String[] args) {

        /*
            3 procesadores
         */
        List<Processor> processors = new ArrayList<>();
        for (int i=0; i<3; i++){
            Processor p = new Processor(i+1, "FP");
            processors.add(p);
        }

        /*
            2 flujos de 3 tareas

            Periodos:       10 20

            Plazos:         20 40

            Localizacion:   1 2 3
                            3 2 1

            WCET:           2 4 5
                            1 3 2

            Priotities      3 6 8
                            2 7 10
         */

        int[] periods = {10,20};
        int[] deadlines = {20,40};
        double[][] wcet = {{2, 4, 5}, {1, 3, 2}};
        int[][] priorities = {{3, 6, 8}, {2, 7, 10}};
        int[][] localization = {{1, 2, 3}, {3, 2, 1}};

        List<Flow> flows = new ArrayList<>();
        for (int i=0; i<2; i++){
            Flow f = new Flow(i+1, (double)periods[i], (double)deadlines[i]);
            for (int j=0; j<3; j++){
                Task t = new Task(wcet[i][j], 0.0, priorities[i][j], processors.get(localization[i][j]-1));
                f.addTask(t);
            }
            flows.add(f);
        }

        // Se crea el sistema con esos flujos y procesadores
        MastSystem mySystem = new MastSystem(flows, processors);
        System.out.println("Resumen del sistema:");
        mySystem.printOverview();   // Periodo : (wcet, procesador, prioridad) () ... : plazo
                                    // ...
                                    // Utilizacion Procesador 1 ... : Utilización media en el sistema

        // Configuración de la ejecución de MAST (analisis y/o asignación de prioridades)
        MastConfig config = new MastConfig();
        config.setAnalysis(AnalysisOptions.HOLISTIC);
        config.setAssignment(AssignmentOptions.HOSPA);


        // Ejecución de MAST sobre el sistema generado
        // Aunque se ejecute HOSPA, no se actualizan las prioridades en el MastSystem
        try {
            MastTool.analyze(mySystem, config);
        } catch (InterruptedAnalysis interruptedAnalysis) {
            interruptedAnalysis.printStackTrace();
        }

        // Resumen de los tiempos de respuesta de peor caso
        System.out.println("\nResumen de los resultados:");
        mySystem.printResultsOverview();    // Periodo : tiempo de respuesta ... : plazo -> Indice de planificabilidad

        // Tambien podemos obtener los tiempos de respuesta como un array bidimensional
        System.out.printf("\nTiempos de respuesta (array):\n%s", Arrays.deepToString(mySystem.getTasksWCRTAsArray()));

    }

}

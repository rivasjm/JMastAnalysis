//package es.istr.unican.jmastanalysis.applications.mario;
//
//import es.istr.unican.jmastanalysis.analysis.MastTool;
//import es.istr.unican.jmastanalysis.analysis.config.AnalysisOptions;
//import es.istr.unican.jmastanalysis.analysis.config.AssignmentOptions;
//import es.istr.unican.jmastanalysis.analysis.config.MastConfig;
//import es.istr.unican.jmastanalysis.exceptions.InterruptedAnalysis;
//import es.istr.unican.jmastanalysis.system.Flow;
//import es.istr.unican.jmastanalysis.system.MastSystem;
//import es.istr.unican.jmastanalysis.system.Processor;
////import es.istr.unican.jmastanalysis.system.Task;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by juanm on 11/11/2015.
// */
//public class Mario {
//
//    public static void main(String[] args) {
//
//        /*
//            6 procesadores
//         */
//        List<Processor> processors = new ArrayList<>();
//        for (int i=0; i<6; i++){
//            Processor p = new Processor(i+1, "FP");
//            processors.add(p);
//        }
//
//        /*
//            2 flujos de 3 tareas
//
//            Periodos:       10 20
//
//            Plazos:         20 40
//
//            Localizacion:   1 2 3
//                            3 2 1
//
//            WCET:           2 4 5
//                            1 3 2
//
//            Priotities      3 6 8
//                            2 7 10
//         */
//
//        double[] periods = {70.494352, 87.065464, 13.837622, 93.043997, 99.043698, 29.446658};
//        double[] deadlines = {352.471760, 435.327320, 69.188108, 465.219985, 495.218492, 147.233290};
//        double[][] wcet = {{7.049435, 8.459322, 8.459322, 10.574153, 7.049435}, {8.706546, 10.447856, 13.059820, 8.706546, 13.059820}, {1.660515, 1.660515, 1.383762, 2.075643, 1.383762}, {13.956600, 9.304400, 11.165280, 11.165280, 9.304400}, {14.856555, 11.885244, 9.904370, 14.856555, 9.904370}, {2.944666, 4.416999, 3.533599, 3.533599, 2.944666}};
//        int[][] priorities = {{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
//        int[][] localization = {{2, 2, 2, 0, 2}, {2, 3, 3, 2, 3}, {0, 0, 0, 0, 0}, {1, 3, 3, 4, 3}, {4, 4, 4, 4, 4}, {1, 1, 1, 1, 1}};
//
//        List<Flow> flows = new ArrayList<>();
//        for (int i=0; i<6; i++){
//            Flow f = new Flow(i+1, (double)periods[i], (double)deadlines[i]);
//            for (int j=0; j<5; j++){
//                //Task t = new Task(wcet[i][j]*1.33f, 0.0, priorities[i][j], processors.get(localization[i][j]));
//                //f.addTask(t);
//            }
//            flows.add(f);
//        }
//
//        // Se crea el sistema con esos flujos y procesadores
//        MastSystem mySystem = new MastSystem(flows, processors);
//        System.out.println("Resumen del sistema:");
//        mySystem.printOverview();   // Periodo : (wcet, procesador, prioridad) () ... : plazo
//                                    // ...
//                                    // Utilizacion Procesador 1 ... : Utilizaci�n media en el sistema
//
//        // Configuraci�n de la ejecuci�n de MAST (analisis y/o asignaci�n de prioridades)
//        MastConfig config = new MastConfig();
//        config.setAnalysis(AnalysisOptions.OFFSET_OPT);
//        config.setAssignment(AssignmentOptions.HOSPA);
//        config.setStopFactor(10.0f);
//
//
//        // Ejecuci�n de MAST sobre el sistema generado
//        // Aunque se ejecute HOSPA, no se actualizan las prioridades en el MastSystem
//        try {
//            MastTool.analyze(mySystem, config);
//
//            // Resumen de los tiempos de respuesta de peor caso
//            System.out.println("\nResumen de los resultados:");
//            mySystem.printResultsOverview();    // Periodo : tiempo de respuesta ... : plazo -> Indice de planificabilidad
//            System.out.printf("Schedulable : %s\n", mySystem.isSchedulable());
//
//            // Tambien podemos obtener los tiempos de respuesta como un array bidimensional
//            //System.out.printf("\nTiempos de respuesta (array):\n%s", Arrays.deepToString(mySystem.getTasksWCRTAsArray()));
//
//        } catch (InterruptedAnalysis interruptedAnalysis) {
//            interruptedAnalysis.printStackTrace();
//        }
//
//
//
//    }
//
//}

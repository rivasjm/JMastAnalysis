package es.istr.unican.jmastanalysis.tests;

import es.istr.unican.jmastanalysis.analysis.MastTool;
import es.istr.unican.jmastanalysis.system.MSystem;

import java.io.File;

/**
 * Created by Administrador on 02/12/2015.
 */
public class testIntegrateResults {

    public static void main(String[] args) {

        MSystem system = new MSystem();
        MastTool.integrateMASTResults(new File("fmtv16_NO_LABELS_ACET_200Mhz_RESULTS.xml"), system);

    }

}

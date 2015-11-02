package es.istr.unican.jmastanalysis.system.config;

import es.istr.unican.jmastanalysis.exceptions.ConfigurableOptionNotSet;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by JuanCTR on 01/09/2015.
 * Custom EnumMap that is forced to have all the LinearSystemConfigurableOptions keys
 */
public class LinearSystemConfigurableMap extends EnumMap<LinearSystemConfigurableOptions, String> {

    public LinearSystemConfigurableMap() {
        super(LinearSystemConfigurableOptions.class);
    }

    public LinearSystemConfigurableMap(Map<LinearSystemConfigurableOptions, String> map) throws ConfigurableOptionNotSet {
        super(LinearSystemConfigurableOptions.class);
        for (LinearSystemConfigurableOptions key : LinearSystemConfigurableOptions.values()) {
            if (!map.containsKey(key)) {
                throw new ConfigurableOptionNotSet();
            } else this.put(key, map.get(key));
        }
    }

}

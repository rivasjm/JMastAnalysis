//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.19 at 05:12:38 PM CEST 
//


package es.istr.unican.jmastanalysis.analysis.results;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Detailed_Utilization complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="Detailed_Utilization">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Total" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *       &lt;attribute name="Application" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *       &lt;attribute name="Context_Switch" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *       &lt;attribute name="Timer" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *       &lt;attribute name="Driver" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Detailed_Utilization")
public class DetailedUtilization {

    @XmlAttribute(name = "Total")
    protected Float total;
    @XmlAttribute(name = "Application")
    protected Float application;
    @XmlAttribute(name = "Context_Switch")
    protected Float contextSwitch;
    @XmlAttribute(name = "Timer")
    protected Float timer;
    @XmlAttribute(name = "Driver")
    protected Float driver;

    /**
     * Gets the value of the total property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setTotal(Float value) {
        this.total = value;
    }

    /**
     * Gets the value of the application property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setApplication(Float value) {
        this.application = value;
    }

    /**
     * Gets the value of the contextSwitch property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getContextSwitch() {
        return contextSwitch;
    }

    /**
     * Sets the value of the contextSwitch property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setContextSwitch(Float value) {
        this.contextSwitch = value;
    }

    /**
     * Gets the value of the timer property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getTimer() {
        return timer;
    }

    /**
     * Sets the value of the timer property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setTimer(Float value) {
        this.timer = value;
    }

    /**
     * Gets the value of the driver property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getDriver() {
        return driver;
    }

    /**
     * Sets the value of the driver property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setDriver(Float value) {
        this.driver = value;
    }

}

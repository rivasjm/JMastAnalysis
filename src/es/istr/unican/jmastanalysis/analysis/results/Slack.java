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
 * <p>Java class for Slack complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="Slack">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Value" use="required" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Percentage" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Slack")
public class Slack {

    @XmlAttribute(name = "Value", required = true)
    protected float value;

    /**
     * Gets the value of the value property.
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     */
    public void setValue(float value) {
        this.value = value;
    }

}

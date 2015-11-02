//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.19 at 05:12:38 PM CEST 
//


package es.istr.unican.jmastanalysis.analysis.results;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for Processing_Resource_Results complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="Processing_Resource_Results">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Slack" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Slack"/>
 *         &lt;element name="Utilization" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Utilization"/>
 *         &lt;element name="Detailed_Utilization" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Detailed_Utilization"/>
 *         &lt;element name="Ready_Queue_Size" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Ready_Queue_Size" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="Name" use="required" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Identifier" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Processing_Resource_Results", propOrder = {
        "slackOrUtilizationOrDetailedUtilization"
})
public class ProcessingResourceResults {

    @XmlElements({
            @XmlElement(name = "Slack", type = Slack.class),
            @XmlElement(name = "Utilization", type = Utilization.class),
            @XmlElement(name = "Detailed_Utilization", type = DetailedUtilization.class),
            @XmlElement(name = "Ready_Queue_Size", type = ReadyQueueSize.class)
    })
    protected List<Object> slackOrUtilizationOrDetailedUtilization;
    @XmlAttribute(name = "Name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String name;

    /**
     * Gets the value of the slackOrUtilizationOrDetailedUtilization property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slackOrUtilizationOrDetailedUtilization property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlackOrUtilizationOrDetailedUtilization().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Slack }
     * {@link Utilization }
     * {@link DetailedUtilization }
     * {@link ReadyQueueSize }
     */
    public List<Object> getSlackOrUtilizationOrDetailedUtilization() {
        if (slackOrUtilizationOrDetailedUtilization == null) {
            slackOrUtilizationOrDetailedUtilization = new ArrayList<Object>();
        }
        return this.slackOrUtilizationOrDetailedUtilization;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

}

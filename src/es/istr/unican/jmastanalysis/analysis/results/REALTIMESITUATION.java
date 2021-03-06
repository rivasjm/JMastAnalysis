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
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Slack" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Slack"/>
 *         &lt;element name="Trace" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Trace"/>
 *         &lt;element name="Processing_Resource" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Processing_Resource_Results"/>
 *         &lt;element name="Scheduling_Server" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Scheduling_Server_Results"/>
 *         &lt;element name="Shared_Resource" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Shared_Resource_Results"/>
 *         &lt;element name="Operation" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Operation_Results"/>
 *         &lt;element name="Transaction" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Transaction_Results"/>
 *       &lt;/choice>
 *       &lt;attribute name="Model_Name" use="required" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Identifier" />
 *       &lt;attribute name="Model_Date" use="required" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Date_Time" />
 *       &lt;attribute name="Generation_Tool" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Generation_Profile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Generation_Date" use="required" type="{http://mast.unican.es/xmlmast/xmlmast_1_4/Mast_Result}Date_Time" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "slackOrTraceOrProcessingResource"
})
@XmlRootElement(name = "REAL_TIME_SITUATION")
public class REALTIMESITUATION {

    @XmlElements({
            @XmlElement(name = "Slack", type = Slack.class),
            @XmlElement(name = "Trace", type = Trace.class),
            @XmlElement(name = "Processing_Resource", type = ProcessingResourceResults.class),
            @XmlElement(name = "Scheduling_Server", type = SchedulingServerResults.class),
            @XmlElement(name = "Shared_Resource", type = SharedResourceResults.class),
            @XmlElement(name = "Operation", type = OperationResults.class),
            @XmlElement(name = "Transaction", type = TransactionResults.class)
    })
    protected List<Object> slackOrTraceOrProcessingResource;
    @XmlAttribute(name = "Model_Name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String modelName;
    @XmlAttribute(name = "Model_Date", required = true)
    protected XMLGregorianCalendar modelDate;
    @XmlAttribute(name = "Generation_Tool", required = true)
    protected String generationTool;
    @XmlAttribute(name = "Generation_Profile")
    protected String generationProfile;
    @XmlAttribute(name = "Generation_Date", required = true)
    protected XMLGregorianCalendar generationDate;

    /**
     * Gets the value of the slackOrTraceOrProcessingResource property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slackOrTraceOrProcessingResource property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlackOrTraceOrProcessingResource().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Slack }
     * {@link Trace }
     * {@link ProcessingResourceResults }
     * {@link SchedulingServerResults }
     * {@link SharedResourceResults }
     * {@link OperationResults }
     * {@link TransactionResults }
     */
    public List<Object> getSlackOrTraceOrProcessingResource() {
        if (slackOrTraceOrProcessingResource == null) {
            slackOrTraceOrProcessingResource = new ArrayList<Object>();
        }
        return this.slackOrTraceOrProcessingResource;
    }

    /**
     * Gets the value of the modelName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * Sets the value of the modelName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModelName(String value) {
        this.modelName = value;
    }

    /**
     * Gets the value of the modelDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getModelDate() {
        return modelDate;
    }

    /**
     * Sets the value of the modelDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setModelDate(XMLGregorianCalendar value) {
        this.modelDate = value;
    }

    /**
     * Gets the value of the generationTool property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenerationTool() {
        return generationTool;
    }

    /**
     * Sets the value of the generationTool property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenerationTool(String value) {
        this.generationTool = value;
    }

    /**
     * Gets the value of the generationProfile property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenerationProfile() {
        return generationProfile;
    }

    /**
     * Sets the value of the generationProfile property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenerationProfile(String value) {
        this.generationProfile = value;
    }

    /**
     * Gets the value of the generationDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getGenerationDate() {
        return generationDate;
    }

    /**
     * Sets the value of the generationDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setGenerationDate(XMLGregorianCalendar value) {
        this.generationDate = value;
    }

}

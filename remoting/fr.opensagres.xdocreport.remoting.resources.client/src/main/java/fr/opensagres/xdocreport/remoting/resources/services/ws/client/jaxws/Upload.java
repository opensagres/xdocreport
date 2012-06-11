
package fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;


/**
 * <p>Classe Java pour upload complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="upload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="data" type="{http://ws.services.resources.remoting.xdocreport.opensagres.fr/}binaryData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "upload", propOrder = {
    "data"
})
public class Upload {

    protected BinaryData data;

    /**
     * Obtient la valeur de la propri�t� data.
     * 
     * @return
     *     possible object is
     *     {@link BinaryData }
     *     
     */
    public BinaryData getData() {
        return data;
    }

    /**
     * D�finit la valeur de la propri�t� data.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryData }
     *     
     */
    public void setData(BinaryData value) {
        this.data = value;
    }

}

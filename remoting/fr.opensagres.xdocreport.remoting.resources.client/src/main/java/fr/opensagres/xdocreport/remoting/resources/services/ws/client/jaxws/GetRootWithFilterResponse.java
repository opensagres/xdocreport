
package fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import fr.opensagres.xdocreport.remoting.resources.domain.Resource;


/**
 * <p>Classe Java pour getRootWithFilterResponse complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="getRootWithFilterResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.services.resources.remoting.xdocreport.opensagres.fr/}resource" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRootWithFilterResponse", propOrder = {
    "_return"
})
public class GetRootWithFilterResponse {

    @XmlElement(name = "return")
    protected Resource _return;

    /**
     * Obtient la valeur de la propri�t� return.
     * 
     * @return
     *     possible object is
     *     {@link Resource }
     *     
     */
    public Resource getReturn() {
        return _return;
    }

    /**
     * D�finit la valeur de la propri�t� return.
     * 
     * @param value
     *     allowed object is
     *     {@link Resource }
     *     
     */
    public void setReturn(Resource value) {
        this._return = value;
    }

}

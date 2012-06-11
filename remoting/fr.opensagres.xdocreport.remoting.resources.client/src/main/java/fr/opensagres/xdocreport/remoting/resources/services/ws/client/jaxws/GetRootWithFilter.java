
package fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import fr.opensagres.xdocreport.remoting.resources.domain.Filter;


/**
 * <p>Classe Java pour getRootWithFilter complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="getRootWithFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filter" type="{http://ws.services.resources.remoting.xdocreport.opensagres.fr/}filter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRootWithFilter", propOrder = {
    "filter"
})
public class GetRootWithFilter {

    protected Filter filter;

    /**
     * Obtient la valeur de la propri�t� filter.
     * 
     * @return
     *     possible object is
     *     {@link Filter }
     *     
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * D�finit la valeur de la propri�t� filter.
     * 
     * @param value
     *     allowed object is
     *     {@link Filter }
     *     
     */
    public void setFilter(Filter value) {
        this.filter = value;
    }

}

/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.remoting.resources.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour resource complex type.
 * <p>
 * Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="resource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="children" type="{http://domain.resources.remoting.xdocreport.opensagres.fr/}resource" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://domain.resources.remoting.xdocreport.opensagres.fr/}resourceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "resource", propOrder = { "children", "id", "name", "type" } )
public class Resource
{

    // Constants for properties of getter/setter of Resource which can be used
    // when you wish use reflection (ex:use Eclipse JFace Databinding, use
    // Commons BeanUtils, etc...)
    public static final String CHILDREN_PROPERTY = "children";

    public static final String ID_PROPERTY = "id";

    public static final String NAME_PROPERTY = "name";

    public static final String TYPE_PROPERTY = "type";

    public static final String ID_NOT_NULL_PROPERTY = "idNotNull";

    public static final String PATH_PROPERTY = "path";

    @XmlElement( nillable = true )
    private final List<Resource> children = new ResourceList( this );

    protected String id;

    protected String name;

    protected ResourceType type;

    @XmlTransient
    protected Resource parent;

    @XmlTransient
    protected String path;

    /**
     * Gets the value of the children property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the children property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getChildren().add( newItem );
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Resource }
     */
    public List<Resource> getChildren()
    {
        return this.children;
    }

    public String getIdNotNull()
    {
        if ( id != null && id.length() > 0 )
        {
            return id;
        }
        return getPath();
    }

    public String getPath()
    {
        if ( path == null )
        {
            path = ResourceHelper.getResourcePath( this );
        }
        return path;
    }

    /**
     * Obtient la valeur de la propri�t� id.
     * 
     * @return possible object is {@link String }
     */
    public String getId()
    {
        return id;
    }

    /**
     * D�finit la valeur de la propri�t� id.
     * 
     * @param value allowed object is {@link String }
     */
    public void setId( String value )
    {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propri�t� name.
     * 
     * @return possible object is {@link String }
     */
    public String getName()
    {
        return name;
    }

    /**
     * D�finit la valeur de la propri�t� name.
     * 
     * @param value allowed object is {@link String }
     */
    public void setName( String value )
    {
        this.name = value;
    }

    /**
     * Obtient la valeur de la propri�t� type.
     * 
     * @return possible object is {@link ResourceType }
     */
    public ResourceType getType()
    {
        return type;
    }

    /**
     * D�finit la valeur de la propri�t� type.
     * 
     * @param value allowed object is {@link ResourceType }
     */
    public void setType( ResourceType value )
    {
        this.type = value;
    }

    /**
     * Returns the parent resource.
     * 
     * @return
     */
    public Resource getParent()
    {
        return parent;
    }

    /**
     * Resource List
     */
    private static class ResourceList
        extends ArrayList<Resource>
    {

        private static final long serialVersionUID = -2498988261160544607L;

        private final Resource parent;

        public ResourceList( Resource parent )
        {
            this.parent = parent;
        }

        @Override
        public boolean add( Resource e )
        {
            e.parent = parent;
            return super.add( e );
        }

        @Override
        public void add( int index, Resource element )
        {
            element.parent = parent;
            super.add( index, element );
        }

    }
}

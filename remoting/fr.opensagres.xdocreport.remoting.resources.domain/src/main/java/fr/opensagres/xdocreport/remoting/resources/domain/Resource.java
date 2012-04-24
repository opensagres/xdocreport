/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Domain for defines a resource like folder and file.
 */
@XmlRootElement
public class Resource
{
    /**
     * Types of resource.
     */

    public static int FOLDER_TYPE = 0;

    public static int FILE_TYPE = 1;

    private String id;

    private String name;

    private List<Resource> children;

    private int type;

    /**
     * Returns the unique resource id.
     * 
     * @return the unique resource id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Set the unique resource id
     * 
     * @param id the unique resource id
     */
    public void setId( String id )
    {
        this.id = id;
    }

    /**
     * Returns the resource name.
     * 
     * @return the resource name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the resource name.
     * 
     * @param name the resource name.
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Returns the resource type (folder or file).
     * 
     * @return the resource type (folder or file).
     */
    public int getType()
    {
        return type;
    }

    /**
     * Set the resource type (folder or file).
     * 
     * @param type the resource type (folder or file).
     */
    public void setType( int type )
    {
        this.type = type;
    }

    /**
     * Returns the children resources of this resource.
     * 
     * @return the children resources of this resource.
     */
    public List<Resource> getChildren()
    {
        return children;
    }

    /**
     * Set the children resources of this resource.
     * 
     * @param children the children resources of this resource.
     */
    public void setChildren( List<Resource> children )
    {
        this.children = children;
    }
}

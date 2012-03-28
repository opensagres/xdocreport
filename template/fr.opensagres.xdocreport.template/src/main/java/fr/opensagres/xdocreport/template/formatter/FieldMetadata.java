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
package fr.opensagres.xdocreport.template.formatter;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * Field metadata.
 */
public class FieldMetadata
{

    private final FieldsMetadata fieldsMetadata;

    private final String fieldName;

    private String imageName;

    private boolean listType;

    private boolean imageType;

    private String syntaxKind;

    private String description;

    private boolean syntaxWithDirective;

    private NullImageBehaviour behaviour;

    private boolean useImageSize;

    public FieldMetadata( FieldsMetadata fieldsMetadata, String fieldName, boolean listType, String imageName,
                          String syntaxKind, boolean syntaxWithDirective )
    {
        this.fieldsMetadata = fieldsMetadata;
        this.fieldName = fieldName;
        setListType( listType );
        setImageName( imageName );
        this.setSyntaxKind( syntaxKind );
        this.setSyntaxWithDirective( syntaxWithDirective );
        this.behaviour = null;
        this.useImageSize = false;
        fieldsMetadata.fields.add( this );
    }

    /**
     * Returns the field name.
     * 
     * @return
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * Returns the image name.
     * 
     * @return
     */
    public String getImageName()
    {
        return imageName;
    }

    /**
     * Returns true if field is list type and false otherwise.
     * 
     * @return
     */
    public boolean isListType()
    {
        return listType;
    }

    /**
     * Set true if field is list type and false otherwise.
     * 
     * @param listType
     */
    public void setListType( boolean listType )
    {
        this.listType = listType;
        if ( listType )
        {
            fieldsMetadata.fieldsAsList.put( getFieldName(), this );
        }
        else
        {
            fieldsMetadata.fieldsAsList.remove( getFieldName() );
        }
    }

    /**
     * Returns true if field is image type and false otherwise.
     * 
     * @return
     */
    public boolean isImageType()
    {
        return imageType;
    }

    /**
     * Set image name.
     * 
     * @param imageName
     */
    public void setImageName( String imageName )
    {
        this.imageName = imageName;
        this.imageType = StringUtils.isNotEmpty( imageName );
        if ( imageType )
        {
            fieldsMetadata.fieldsAsImage.put( getImageName(), this );
        }
        else
        {
            fieldsMetadata.fieldsAsImage.remove( getImageName() );
        }
    }

    public String getSyntaxKind()
    {
        return syntaxKind;
    }

    public boolean isSyntaxWithDirective()
    {
        return syntaxWithDirective;
    }

    public void setSyntaxKind( String syntaxKind )
    {
        this.syntaxKind = syntaxKind;
        if ( StringUtils.isNotEmpty( syntaxKind ) )
        {
            fieldsMetadata.fieldsAsTextStyling.put( getFieldName(), this );
        }
        else
        {
            fieldsMetadata.fieldsAsTextStyling.remove( getFieldName() );
        }
    }

    public void setSyntaxWithDirective( boolean syntaxWithDirective )
    {
        this.syntaxWithDirective = syntaxWithDirective;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the "field" behavior to use when the stream of the image is null.
     * 
     * @return
     */
    public NullImageBehaviour getBehaviour()
    {
        return behaviour;
    }

    /**
     * Set the "field" behaviour to use when the stream of the image is null.
     * 
     * @param behaviour
     */
    public void setBehaviour( NullImageBehaviour behaviour )
    {
        this.behaviour = behaviour;
    }

    public boolean isUseImageSize()
    {
        return useImageSize;
    }

    public void setUseImageSize( boolean useImageSize )
    {
        this.useImageSize = useImageSize;
    }

}

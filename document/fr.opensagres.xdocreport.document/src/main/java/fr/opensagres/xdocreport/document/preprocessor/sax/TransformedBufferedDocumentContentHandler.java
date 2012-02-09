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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.template.DocumentContextHelper;
import fr.opensagres.xdocreport.template.formatter.DirectivesStack;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Document transformed to manage lazy loop for row table and dynamic image.
 */
public abstract class TransformedBufferedDocumentContentHandler<Document extends TransformedBufferedDocument>
    extends BufferedDocumentContentHandler<Document>
{

    private static final String AFTER_TOKEN = "@/";

    private static final String BEFORE_TOKEN = "@";

    private final FieldsMetadata fieldsMetadata;

    private final IDocumentFormatter formatter;

    private final Map<String, Object> sharedContext;

    private final DirectivesStack directives;

    private int nbLoopDirectiveToRemove = 0;

    private int variableIndex;

    private final String entryName;

    protected TransformedBufferedDocumentContentHandler( String entryName, FieldsMetadata fieldsMetadata,
                                                         IDocumentFormatter formater, Map<String, Object> sharedContext )
    {
        this.fieldsMetadata = fieldsMetadata;
        this.formatter = formater;
        this.sharedContext = sharedContext;
        this.directives = new DirectivesStack();
        this.variableIndex = 0;
        this.entryName = entryName;
    }

    @Override
    public void startDocument()
        throws SAXException
    {
        String directive = formatter != null ? formatter.getStartDocumentDirective() : null;
        if ( StringUtils.isNotEmpty( directive ) )
        {
            this.bufferedDocument.append( directive );
        }
        super.startDocument();
    }

    @Override
    public void endDocument()
        throws SAXException
    {
        String directive = formatter != null ? formatter.getEndDocumentDirective() : null;
        if ( StringUtils.isNotEmpty( directive ) )
        {
            this.bufferedDocument.append( directive );
        }
        super.endDocument();
    }

    @Override
    protected void flushCharacters( String characters )
    {
        super.flushCharacters( processRowIfNeeded( characters ) );
    }

    public String processRowIfNeeded( String content )
    {
        return processRowIfNeeded( content, false );
    }

    /**
     * If a row parsing, replace fields name with well script to manage lazy loop for table row.
     * 
     * @param content
     * @return
     */
    public String processRowIfNeeded( String content, boolean forceAsField )
    {
        ProcessRowResult result = getProcessRowResult( content, forceAsField );
        return result.getContent();
    }

    /**
     * If a row parsing, replace fields name with well script to manage lazy loop for table row.
     * 
     * @param content
     * @return
     */
    public ProcessRowResult getProcessRowResult( String content, boolean forceAsField )
    {
        RowBufferedRegion currentRow = bufferedDocument.getCurrentTableRow();
        if ( currentRow != null && formatter != null )
        {
            // characters parsing belong to a row
            // search if it contains fields list from metadata
            Collection<String> fieldsAsList = fieldsMetadata.getFieldsAsList();
            if ( !currentRow.isLoopTemplateDirectiveInitilalized() )
            {
                for ( final String fieldName : fieldsAsList )
                {
                    if ( content.contains( fieldName ) )
                    {
                        String itemNameList = formatter.extractItemNameList( content, fieldName, forceAsField );
                        if ( StringUtils.isNotEmpty( itemNameList ) )
                        {
                            currentRow.initializeLoopTemplateDirective( itemNameList, formatter );
                            break;
                        }
                    }
                }
            }

            if ( currentRow.isLoopTemplateDirectiveInitilalized() )
            {
                for ( final String fieldName : fieldsAsList )
                {
                    if ( content.contains( fieldName ) )
                    {
                        String newContent = formatter.formatAsFieldItemList( content, fieldName, forceAsField );
                        if ( newContent != null )
                        {
                            return new ProcessRowResult( newContent, fieldName, currentRow.getItemNameList(),
                                                         currentRow.getStartLoopDirective(),
                                                         currentRow.getEndLoopDirective() );
                        }
                    }
                }
            }
        }
        return new ProcessRowResult( content, null, null, null, null );
    }

    public Map<String, Object> getSharedContext()
    {
        return sharedContext;
    }

    public boolean hasSharedContext()
    {
        return sharedContext != null;
    }

    public FieldsMetadata getFieldsMetadata()
    {
        return fieldsMetadata;
    }

    public IDocumentFormatter getFormatter()
    {
        return formatter;
    }

    public DirectivesStack getDirectives()
    {
        return directives;
    }

    /**
     * Returns the before row token.
     * 
     * @return
     */
    protected String getBeforeRowToken()
    {
        if ( fieldsMetadata == null )
        {
            return FieldsMetadata.DEFAULT_BEFORE_ROW_TOKEN;
        }
        return fieldsMetadata.getBeforeRowToken();
    }

    /**
     * Returns the after row token.
     * 
     * @return
     */
    protected String getAfterRowToken()
    {
        if ( fieldsMetadata == null )
        {
            return FieldsMetadata.DEFAULT_AFTER_ROW_TOKEN;
        }
        return fieldsMetadata.getAfterRowToken();
    }

    /**
     * Returns the before row token.
     * 
     * @return
     */
    protected String getBeforeTableCellToken()
    {
        if ( fieldsMetadata == null )
        {
            return FieldsMetadata.DEFAULT_BEFORE_TABLE_CELL_TOKEN;
        }
        return fieldsMetadata.getBeforeTableCellToken();
    }

    /**
     * Returns the after row token.
     * 
     * @return
     */
    protected String getAfterTableCellToken()
    {
        if ( fieldsMetadata == null )
        {
            return FieldsMetadata.DEFAULT_AFTER_TABLE_CELL_TOKEN;
        }
        return fieldsMetadata.getAfterTableCellToken();
    }

    public int extractListDirectiveInfo( String characters, boolean dontRemoveListDirectiveInfo )
    {
        if ( formatter == null || characters == null )
        {
            return 0;
        }
        return formatter.extractListDirectiveInfo( characters, getDirectives(), dontRemoveListDirectiveInfo );
    }

    public int extractListDirectiveInfo( String characters )
    {
        int i = extractListDirectiveInfo( characters, bufferedDocument.getCurrentTableRow() != null );
        if ( i < 0 )
        {
            nbLoopDirectiveToRemove += -i;
        }
        return i;
    }

    /**
     * Returns true if current element is a table and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    protected boolean isTable( String uri, String localName, String name )
    {
        return bufferedDocument.isTable( uri, localName, name );
    }

    protected abstract String getTableRowName();

    protected abstract String getTableCellName();

    /**
     * Returns true if current element is a table row and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    protected boolean isTableRow( String uri, String localName, String name )
    {
        return bufferedDocument.isTableRow( uri, localName, name );
    }

    public boolean processScriptBefore( String fieldName )
    {
        int index = getIndexOfScript( fieldName, true );
        if ( index == -1 )
        {
            return false;
        }
        String beforeElementName = fieldName.substring( 0, index );
        if ( StringUtils.isNotEmpty( beforeElementName ) )
        {
            if ( beforeElementName.equals( getBeforeRowToken() ) )
            {
                beforeElementName = getTableRowName();
            }
            else if ( beforeElementName.equals( getBeforeTableCellToken() ) )
            {
                beforeElementName = getTableCellName();
            }
            BufferedElement elementInfo = super.findParentElementInfo( beforeElementName );
            if ( elementInfo == null )
            {
                return false;
            }
            String before = fieldName.substring( index, fieldName.length() );
            elementInfo.setContentBeforeStartTagElement( before );
            return true;
        }
        return false;
    }

    private int getIndexOfScript( String fieldName, boolean before )
    {
        if ( fieldName == null )
        {
            return -1;
        }
        if ( before )
        {
            if ( formatter == null )
            {
                if ( fieldName.startsWith( getBeforeRowToken() ) )
                {
                    return getBeforeRowToken().length();
                }
                if ( fieldName.startsWith( getBeforeTableCellToken() ) )
                {
                    return getBeforeTableCellToken().length();
                }
                return -1;
            }
            if ( !( fieldName.startsWith( BEFORE_TOKEN ) || fieldName.startsWith( getBeforeRowToken() ) || fieldName.startsWith( getBeforeTableCellToken() ) ) )
            {
                return -1;
            }

        }
        else
        {
            if ( formatter == null )
            {
                if ( fieldName.startsWith( getAfterRowToken() ) )
                {
                    return getAfterRowToken().length();
                }
                if ( fieldName.startsWith( getAfterTableCellToken() ) )
                {
                    return getAfterTableCellToken().length();
                }
                return -1;
            }
            if ( !( fieldName.startsWith( AFTER_TOKEN ) || fieldName.startsWith( getAfterRowToken() ) || fieldName.startsWith( getAfterTableCellToken() ) ) )
            {
                return -1;
            }
        }
        return formatter.getIndexOfScript( fieldName );
    }

    public boolean processScriptAfter( String fieldName )
    {
        int index = getIndexOfScript( fieldName, false );
        if ( index == -1 )
        {
            return false;
        }
        String afterElementName = fieldName.substring( 0, index );
        if ( StringUtils.isNotEmpty( afterElementName ) )
        {
            if ( afterElementName.equals( getAfterRowToken() ) )
            {
                afterElementName = getTableRowName();
            }
            else if ( afterElementName.equals( getAfterTableCellToken() ) )
            {
                afterElementName = getTableCellName();
            }
            BufferedElement elementInfo = super.findParentElementInfo( afterElementName );
            if ( elementInfo == null )
            {
                return false;
            }
            String after = fieldName.substring( index, fieldName.length() );
            elementInfo.setContentAfterEndTagElement( after );
            return true;
        }
        return false;
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        // remove list directive if needed
        if ( isTable( uri, localName, name ) )
        {
            if ( nbLoopDirectiveToRemove > 0 )
            {
                for ( int i = 0; i < nbLoopDirectiveToRemove; i++ )
                {
                    if ( !getDirectives().isEmpty() )
                    {
                        getDirectives().pop();

                    }
                }
                nbLoopDirectiveToRemove = 0;
            }
        }
        super.doEndElement( uri, localName, name );
    }

    public FieldMetadata getFieldAsTextStyling( String content )
    {
        if ( formatter != null && fieldsMetadata != null )
        {
            Collection<FieldMetadata> fieldsAsTextStyling = fieldsMetadata.getFieldsAsTextStyling();
            for ( FieldMetadata field : fieldsAsTextStyling )
            {
                if ( content.contains( field.getFieldName() ) )
                {
                    return field;
                }
            }
        }
        return null;
    }

    public String registerBufferedElement( BufferedElement element )
    {
        Map<String, BufferedElement> elements =
            (Map<String, BufferedElement>) getSharedContext().get( DocumentContextHelper.ELEMENTS_KEY);
        if ( element == null )
        {
            elements = new HashMap<String, BufferedElement>();
        }
        String id = System.currentTimeMillis() + "_id";
        elements.put( id, element );
        return id;
    }

    public long getVariableIndex()
    {
        return variableIndex++;
    }

    public String getEntryName()
    {
        return entryName;
    }

    protected abstract Document createDocument();
}

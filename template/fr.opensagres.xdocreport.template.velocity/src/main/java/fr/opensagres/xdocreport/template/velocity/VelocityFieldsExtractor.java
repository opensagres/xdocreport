package fr.opensagres.xdocreport.template.velocity;

import java.io.Reader;

import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.velocity.internal.ExtractVariablesVelocityVisitor;

public class VelocityFieldsExtractor
{

    private static final VelocityFieldsExtractor INSTANCE = new VelocityFieldsExtractor();

    public static VelocityFieldsExtractor getInstance()
    {
        return INSTANCE;
    }

    public void extractFields( Reader reader, String entryName, FieldsExtractor extractor )
        throws XDocReportException
    {
        try
        {
            SimpleNode document = RuntimeSingleton.parse( reader, entryName );
            ExtractVariablesVelocityVisitor visitor = new ExtractVariablesVelocityVisitor( extractor );
            visitor.setContext( null );
            document.jjtAccept( visitor, null );

        }
        catch ( ParseException e )
        {
            throw new XDocReportException( e );
        }
    }
}

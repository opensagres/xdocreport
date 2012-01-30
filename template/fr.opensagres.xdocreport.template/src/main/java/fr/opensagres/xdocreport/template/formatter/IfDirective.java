package fr.opensagres.xdocreport.template.formatter;

public class IfDirective
    extends Directive
{

    public IfDirective( Directive parent, String startDirective, String endDirective )
    {
        super( parent, startDirective, endDirective );
    }

    @Override
    public DirectiveType getType()
    {
        return DirectiveType.IF;
    }
}

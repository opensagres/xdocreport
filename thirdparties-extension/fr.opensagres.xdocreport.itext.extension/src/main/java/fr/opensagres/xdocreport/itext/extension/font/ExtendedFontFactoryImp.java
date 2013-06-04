package fr.opensagres.xdocreport.itext.extension.font;

import com.lowagie.text.FontFactoryImp;

public class ExtendedFontFactoryImp
    extends FontFactoryImp
{

    @Override
    public void register( String path, String alias )
    {
        super.register( path, alias );
        if ( path.endsWith( "symbol.ttf" ) )
        {
            registerFamily( "Symbol-Exp".toLowerCase(), "Symbol-Exp".toLowerCase(), path );
        }
        else if ( path.endsWith( "wingding.ttf" ) )
        {
            registerFamily( "Wingdings-Exp".toLowerCase(), "Wingdings-Exp".toLowerCase(), path );
        }
    }
}

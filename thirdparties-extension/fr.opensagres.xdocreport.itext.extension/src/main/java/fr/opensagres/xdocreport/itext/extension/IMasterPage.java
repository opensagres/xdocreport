package fr.opensagres.xdocreport.itext.extension;

public interface IMasterPage
{
    IMasterPageHeaderFooter getHeader();

    void setHeader( IMasterPageHeaderFooter header );

    IMasterPageHeaderFooter getFooter();

    void setFooter( IMasterPageHeaderFooter footer );

    String getName();
}

package fr.opensagres.xdocreport.template.textstyling;

public interface IDocumentVisitor {

	void startDocument();
	void endDocument();
	
	void startBold();
	void endBold();
	
	void startItalics();
	void endItalics();
	
	void handleString(String s);
	
//	
//    void endCaption();
//    void endDocument();
//    void endHeading1();
//    void endHeading2();
//    void endHeading3();
//    void endHeading4();
//    void endHeading5();
//    void endHeading6();
//    void endIndent();
//    void endItalics();
//    void endLiteral();
//    void endNormalLinkWithCaption();
//    void endOrderedList();
//    void endOrderedListItem();
//    void endParagraph();
//    void endPre();
//    void endSmartLinkWithCaption();
//    void endTable();
//    void endTableData();
//    void endTableHeader();
//    void endTableRecord();
//    void endUnorderedList();
//    void endUnorderedListItem();
//    void handleNormalLinkWithoutCaption(String string);
//    void handleNowiki(String nowiki);
//    void handleSmartLinkWithoutCaption(String string);
//    void handleString(String s);
//    
//    void startCaption(AttributeList captionOptions);
//    void startDocument();
//    void startHeading1();
//    void startHeading2();
//    void startHeading3();
//    void startHeading4();
//    void startHeading5();
//    void startHeading6();
//    void startIndent();
//    void startItalics();
//    void startLiteral();
//    void startNormalLinkWithCaption(String s);
//    void startOrderedList();
//    void startOrderedListItem();
//    void startParagraph();
//    void startPre();
//    void startSmartLinkWithCaption(String s);
//    void startTable(AttributeList tableOptions);
//    void startTableData(AttributeList options);
//    void startTableHeader(AttributeList list);
//    void startTableRecord(AttributeList rowOptions);
//    void startUnorderedList();
//    void startUnorderedListItem();

}

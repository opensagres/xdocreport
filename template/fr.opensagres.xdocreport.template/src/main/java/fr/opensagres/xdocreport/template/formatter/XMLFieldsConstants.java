package fr.opensagres.xdocreport.template.formatter;

public class XMLFieldsConstants
{

    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    // Attributes
    public static final String NAME_ATTR = "name";

    public static final String LIST_ATTR = "list";

    public static final String IMAGE_NAME_ATTR = "imageName";

    public static final String SYNTAX_KIND_ATTR = "syntaxKind";

    public static final String TEMPLATE_ENGINE_KIND_ATTR = "templateEngineKind";

    // Elements
    public static final String FIELDS_ELT = "fields";

    public static final String FIELD_ELT = "field";

    public static final String DESCRIPTION_ELT = "description";

    public static final String START_CDATA = "<![CDATA[";

    public static final String END_CDATA = "]]>";

    public static final String FIELDS_TAG_START_ELT = "<" + FIELDS_ELT;

    public static final String FIELDS_END_ELT = "</" + FIELDS_ELT + ">";

    public static final String FIELD_TAG_START_ELT = "<" + FIELD_ELT;

    public static final String FIELD_END_ELT = "</" + FIELD_ELT + ">";

    public static final String DESCRIPTION_START_ELT = "<" + DESCRIPTION_ELT + ">";

    public static final String DESCRIPTION_END_ELT = "</" + DESCRIPTION_ELT + ">";
}

package fr.opensagres.xdocreport.core.utils;

/**
 * HTTP Header Utilities.
 */
public class HttpHeaderUtils
{

    // HTTP Header constants
    public static final String SAT_6_MAY_1995_12_00_00_GMT = "Sat, 6 May 1995 12:00:00 GMT";

    public static final String EXPIRES = "Expires";

    public static final String POST_CHECK_0_PRE_CHECK_0 = "post-check=0, pre-check=0";

    public static final String NO_CACHE = "no-cache";

    public static final String PRAGMA = "Pragma";

    public static final String NO_STORE_NO_CACHE_MUST_REVALIDATE = "no-store, no-cache, must-revalidate";

    public static final String CACHE_CONTROL_HTTP_HEADER = "Cache-Control";

    // Content-Disposition HTTP response Header
    public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    public static final String ATTACHMENT_FILENAME_START = "attachment; filename=\"";

    public static final String ATTACHMENT_FILENAME_END = "\"";

    /**
     * Return the header attachment for the given filename.
     * 
     * @param fileName
     * @return
     */
    public static String getAttachmentFileName( String fileName )
    {
        StringBuilder attachment = new StringBuilder( ATTACHMENT_FILENAME_START );
        attachment.append( fileName );
        attachment.append( ATTACHMENT_FILENAME_END );
        return attachment.toString();
    }
}

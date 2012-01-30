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
package fr.opensagres.xdocreport.template.utils;

import fr.opensagres.xdocreport.core.io.IEntryInfo;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.cache.ITemplateCacheInfoProvider;
import fr.opensagres.xdocreport.template.internal.DynamicBean;

/**
 * Utilities for template cache, context.
 */
public class TemplateUtils
{

    // ---------------- Template cache utilities ---------------

    /**
     * Returns the template cache name used to cache the given entry name (which is a template) of the given report id.
     * 
     * @param reportId report id.
     * @param entryName entry name.
     * @return
     */
    public static String getCachedTemplateName( String reportId, String entryName )
    {
        StringBuilder templateName = new StringBuilder( reportId );
        templateName.append( '!' );
        templateName.append( entryName );
        return templateName.toString();
    }

    /**
     * Returns the last index of the token '!' from the cachedTemplateName which delimit reportId and entryName.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @return
     */
    public static int getIndexReportEntryName( String cachedTemplateName )
    {
        return cachedTemplateName.lastIndexOf( '!' );
    }

    /**
     * Returns the report of of the given cached template name.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @param index of the '!' character.
     * @return
     */
    public static String getReportId( String cachedTemplateName, int index )
    {
        return cachedTemplateName.substring( 0, index );
    }

    /**
     * Returns the entry name of of the given cached template name.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @param index of the '!' character.
     * @return
     */
    public static String getEntryName( String cachedTemplateName, int index )
    {
        return cachedTemplateName.substring( index + 1, cachedTemplateName.length() );
    }

    /**
     * @param templateCacheInfoProvider
     * @param templateName
     * @return
     */
    public static IEntryInfo getTemplateCacheInfo( ITemplateCacheInfoProvider templateCacheInfoProvider,
                                                   String templateName )
    {
        if ( templateCacheInfoProvider == null )
        {
            return null;
        }
        int index = TemplateUtils.getIndexReportEntryName( templateName );
        if ( index == -1 )
        {
            return null;
        }
        String reportId = TemplateUtils.getReportId( templateName, index );
        String entryName = TemplateUtils.getEntryName( templateName, index );
        return templateCacheInfoProvider.getTemplateCacheInfo( reportId, entryName );
    }

    // /**
    // * Returns the last modified of the entry identified with the given cached
    // * template name by using {@link XDocReportRegistry}.
    // *
    // * @param cachedTemplateName
    // * cached template name which follow pattern ($reportId '!'
    // * $entryName).
    // * @return
    // */
    // public static long getEntryLastModified(String cachedTemplateName) {
    // int index = getIndexReportEntryName(cachedTemplateName);
    // if (index == -1) {
    // return 0;
    // }
    // String reportId = getReportId(cachedTemplateName, index);
    // XDocArchive archive = TemplateUtils.getXDocArchive(reportId);
    // if (archive == null) {
    // return 0;
    // }
    // String entryName = getEntryName(cachedTemplateName, index);
    // return archive.getLastModifiedEntry(entryName);
    // }

    /**
     * Returns true if cached template name is an entry and false otherwise by using {@link XDocReportRegistry}.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @return
     */
    // public static boolean hasEntry(String cachedTemplateName) {
    // int index = getIndexReportEntryName(cachedTemplateName);
    // if (index == -1) {
    // return false;
    // }
    // String reportId = getReportId(cachedTemplateName, index);
    // XDocArchive archive = TemplateUtils.getXDocArchive(reportId);
    // if (archive == null) {
    // return false;
    // }
    // String entryName = getEntryName(cachedTemplateName, index);
    // return archive.hasEntry(entryName);
    // }

    /**
     * Returns the input stream of the cached template name by using {@link XDocReportRegistry} and null if entry is not
     * found.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @return
     */
    // public static InputStream getEntryInputStream(String cachedTemplateName)
    // {
    // int index = getIndexReportEntryName(cachedTemplateName);
    // if (index == -1) {
    // return null;
    // }
    // String reportId = getReportId(cachedTemplateName, index);
    // XDocArchive archive = TemplateUtils.getXDocArchive(reportId);
    // if (archive == null) {
    // return null;
    // }
    // String entryName = getEntryName(cachedTemplateName, index);
    // if (!archive.hasEntry(entryName)) {
    // return null;
    // }
    // return archive.getEntryInputStream(entryName);
    // }

    /**
     * Returns the reader of the cached template name by using {@link XDocReportRegistry} and null if entry is not
     * found.
     * 
     * @param cachedTemplateName cached template name which follow pattern ($reportId '!' $entryName).
     * @return
     */
    // public static Reader getEntryReader(String cachedTemplateName) {
    // int index = getIndexReportEntryName(cachedTemplateName);
    // if (index == -1) {
    // return null;
    // }
    // String reportId = getReportId(cachedTemplateName, index);
    // XDocArchive archive = TemplateUtils.getXDocArchive(reportId);
    // if (archive == null) {
    // return null;
    // }
    // String entryName = getEntryName(cachedTemplateName, index);
    // if (!archive.hasEntry(entryName)) {
    // return null;
    // }
    // return archive.getEntryReader(entryName);
    // }

    // /**
    // * Returns the {@link XDocArchive} of the report id by using
    // * {@link XDocReportRegistry} and null if report is not found.
    // *
    // * @param reportId
    // * report id
    // * @return
    // */
    // public static XDocArchive getXDocArchive(String reportId) {
    // IXDocReport report = XDocReportRegistry.getRegistry().getReport(
    // reportId);
    // if (report == null) {
    // return null;
    // }
    // return report.getDocumentArchive();
    // }

    // ---------------- Template context utilities ---------------

    /**
     * If key has '.' character in the key (ex : project.name), this method create a {@link DynamicBean} instance to
     * emulate the bean Project#getName().
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static Object putContextForDottedKey( IContext context, String key, Object value )
    {
        int index = key.indexOf( '.' );
        if ( index != -1 )
        {
            // key has '.' character ((ex : project.name)).
            String[] keys = key.split( "[.]" );
            // Get the key (ex : project)
            String newKey = keys[0];
            // Create or get a DynamicBean instance to register the value for
            // the next properties after key name (ex : name)
            DynamicBean bean = getDynamicBean( context, newKey );
            if ( bean != null )
            {
                bean.setValue( keys, value, 1 );
            }
            return bean;
        }
        return null;
    }

    /**
     * Returns DynamicBean from the context with the given key or create it if not exists.
     * 
     * @param context
     * @param key
     * @return
     */
    private static DynamicBean getDynamicBean( IContext context, String key )
    {
        Object result = context.get( key );
        if ( result == null )
        {
            DynamicBean bean = new DynamicBean();
            context.put( key, bean );
            return bean;
        }
        else if ( result instanceof DynamicBean )
        {
            return (DynamicBean) result;
        }
        return null;
    }
}

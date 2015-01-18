<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>

    All rights reserved.

    Permission is hereby granted, free  of charge, to any person obtaining
    a  copy  of this  software  and  associated  documentation files  (the
    "Software"), to  deal in  the Software without  restriction, including
    without limitation  the rights to  use, copy, modify,  merge, publish,
    distribute,  sublicense, and/or sell  copies of  the Software,  and to
    permit persons to whom the Software  is furnished to do so, subject to
    the following conditions:

    The  above  copyright  notice  and  this permission  notice  shall  be
    included in all copies or substantial portions of the Software.

    THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
    EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
    MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
    LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
    OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
    WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

-->
<!--

-->
<xsl:stylesheet
	xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
	xmlns:config="urn:oasis:names:tc:opendocument:xmlns:config:1.0"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	xmlns:presentation="urn:oasis:names:tc:opendocument:xmlns:presentation:1.0"
	xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
	xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
	xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
	xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
	xmlns:anim="urn:oasis:names:tc:opendocument:xmlns:animation:1.0"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:math="http://www.w3.org/1998/Math/MathML"
	xmlns:xforms="http://www.w3.org/2002/xforms"
	xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
	xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
	xmlns:smil="urn:oasis:names:tc:opendocument:xmlns:smil-compatible:1.0"
	xmlns:ooo="http://openoffice.org/2004/office"
	xmlns:ooow="http://openoffice.org/2004/writer"
	xmlns:oooc="http://openoffice.org/2004/calc"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="office text table draw presentation style"
	version="1.0">
	
	
	<xsl:template match="style:master-page">	
		<xsl:variable name="page-layout-name" select="@style:page-layout-name" />
		<xsl:variable name="page-layout-notes-name" select="presentation:notes/@style:page-layout-name" />
		
		<xsl:text>@media screen</xsl:text><xsl:value-of select="$linebreak"/>
		<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>.masterpage_</xsl:text><xsl:value-of select="@style:name" /><xsl:value-of select="$linebreak"/>
			<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:apply-templates select="$styles-xml-doc//style:page-layout[@style:name=$page-layout-name]" mode="CSS-attr" />
			<xsl:value-of select="$linebreak"/>
			<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
		<xsl:value-of select="$linebreak"/>
		<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
		
		<xsl:text>@media print</xsl:text><xsl:value-of select="$linebreak"/>
		<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>.masterpage_</xsl:text><xsl:value-of select="@style:name" /><xsl:value-of select="$linebreak"/>
			<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>width: 100%;</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
		<xsl:value-of select="$linebreak"/>
		<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
		
		<xsl:if test="presentation:notes">
		
			<xsl:text>@media screen</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
				<xsl:text>.masterpage_</xsl:text><xsl:value-of select="@style:name" />_notes<xsl:value-of select="$linebreak"/>
				<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
				<xsl:apply-templates select="//style:page-layout[@style:name=$page-layout-notes-name]" mode="CSS-attr" />
				<xsl:value-of select="$linebreak"/>
				<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:value-of select="$linebreak"/>
			<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
			
			<xsl:text>@media print</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
				<xsl:text>.masterpage_</xsl:text><xsl:value-of select="@style:name" />_notes<xsl:value-of select="$linebreak"/>
				<xsl:text>{</xsl:text><xsl:value-of select="$linebreak"/>
				<xsl:text>width: 100%;</xsl:text><xsl:value-of select="$linebreak"/>
				<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
			<xsl:value-of select="$linebreak"/>
			<xsl:text>}</xsl:text><xsl:value-of select="$linebreak"/>
			
		</xsl:if>
		
	</xsl:template>
	
	
	<xsl:template match="style:footer"/>
	
	
	<xsl:template match="style:header"/>
	
	
	<xsl:template match="style:page-layout"/>
	
	
	<xsl:template match="style:handout-master"/>
	
	
	<xsl:template match="style:page-layout" mode="CSS-attr" >
		/* style:page-layout begin */
		<xsl:call-template name="style_standard_default" mode="CSS-attr"/>
		<xsl:apply-templates select="@*" mode="CSS-attr"/>
		<xsl:apply-templates />
		/* style:page-layout end */
	</xsl:template>
	
	
	<xsl:template match="@fo:page-width" mode="CSS-attr">
		<xsl:text>width: </xsl:text>
		<xsl:variable name="width">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="."/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="margin-right">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="../@fo:margin-right"/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="margin-left">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="../@fo:margin-left"/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="width_normal" select="$width - $margin-right - $margin-left" />
		<xsl:value-of select="$width_normal"/><xsl:text>pt</xsl:text>
		<xsl:text>; </xsl:text>
	</xsl:template>
	
	
	<xsl:template match="@fo:page-height" mode="CSS-attr">
		<xsl:choose>
			<xsl:when test="//office:presentation">
				<xsl:text>height: </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>min-height: </xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		
		<xsl:variable name="height">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="."/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="margin-top">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="../@fo:margin-top"/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="margin-bottom">
			<xsl:call-template name="length-normalize">
				<xsl:with-param name="length" select="../@fo:margin-bottom"/>
				<xsl:with-param name="unit" select="''"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="height_normal" select="$height - $margin-top - $margin-bottom" />
		<xsl:value-of select="$height_normal"/><xsl:text>pt</xsl:text>
		<xsl:text>; </xsl:text>
	</xsl:template>
	
	
</xsl:stylesheet>

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
<!-- =========================================================================== -->
<!-- This template generate HTML meta by using information from the ODT meta.xml. -->
<!-- =========================================================================== -->

<xsl:stylesheet xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0" xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="office meta dc" version="2.0">

	<!-- ==================================== -->
	<!-- Generate HTML meta from ODT meta.xml -->
	<!-- ==================================== -->
	<xsl:template match="office:meta">
		<xsl:comment>
			office:metadata begin
		</xsl:comment>
		<xsl:apply-templates select="dc:title" />
		<xsl:apply-templates select="dc:creator" />
		<xsl:apply-templates select="dc:date" />
		<xsl:apply-templates select="dc:language" />
		<xsl:apply-templates select="dc:description" />
		<xsl:apply-templates select="meta:keyword" />
		<xsl:apply-templates select="meta:generator" />
		<xsl:apply-templates select="meta:document-statistic" />
		<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=utf-8" />
		<xsl:comment>
			office:metadata end
		</xsl:comment>
	</xsl:template>

	<xsl:template match="dc:title">
		<title>
			<xsl:apply-templates />
		</title>
		<meta name="DC.title" content="{current()}" />
	</xsl:template>

	<xsl:template match="dc:language">
		<meta http-equiv="content-language" content="{current()}" />
		<meta name="DC.language" content="{current()}" />
	</xsl:template>

	<xsl:template match="dc:creator">
		<meta name="author" content="{current()}" />
		<meta name="DC.creator" content="{current()}" />
	</xsl:template>

	<xsl:template match="dc:description">
		<meta name="description" content="{current()}" />
	</xsl:template>

	<xsl:template match="dc:date">
		<meta name="revised" content="{current()}" />
		<meta name="DC.date" content="{current()}" />
	</xsl:template>

	<xsl:template match="meta:keyword">
		<meta name="keywords" content="{current()}" />
	</xsl:template>

	<xsl:template match="meta:generator">
		<meta name="generator" content="{current()}" />
	</xsl:template>

	<xsl:template match="meta:document-statistic">
		<meta name="meta:page-count" content="{@meta:page-count}" />
		<meta name="meta:word-count" content="{@meta:word-count}" />
		<meta name="meta:image-count" content="{@meta:image-count}" />
		<meta name="meta:table-count" content="{@meta:table-count}" />
		<meta name="meta:object-count" content="{@meta:object-count}" />
		<meta name="meta:character-count" content="{@meta:character-count}" />
		<meta name="meta:paragraph-count" content="{@meta:paragraph-count}" />
	</xsl:template>

</xsl:stylesheet>

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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	xmlns:presentation="urn:oasis:names:tc:opendocument:xmlns:presentation:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	exclude-result-prefixes="office text table draw presentation style"
	version="2.0">

	<xsl:output method="xml" indent="no" omit-xml-declaration="yes"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		encoding="UTF-8" />

	<!-- ============ -->
	<!-- INCLUDES XSL -->
	<!-- ============ -->
	<xsl:include href="odt-utils.xsl" />
	<xsl:include href="xhtml/styles.xsl" />
	<xsl:include href="xhtml/meta.xsl" />

	<!-- =================== -->
	<!-- MAIN ROOT TEMPLATE -->
	<!-- =================== -->
	<xsl:template match="/office:document-content">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<!-- generate HTML meta -->
				<xsl:call-template name="GenerateHTMLMeta" />
				<!-- generate HTML style -->
				<xsl:call-template name="GenerateHTMLCSSStyle" />
			</head>
			<body>
				<!-- generate HTML body content -->
				<xsl:call-template name="GenerateHTMLBodyContent" />
			</body>
		</html>
	</xsl:template>

	<!-- ==================================== -->
	<!-- Generate HTML meta from ODT meta.xml -->
	<!-- ==================================== -->
	<xsl:template name="GenerateHTMLMeta">
		<xsl:apply-templates select="$meta-xml-doc//office:meta" />
	</xsl:template>

	<!-- ======================================================= -->
	<!-- Generate HTML CSS style from ODT content.xml+styles.xml -->
	<!-- ======================================================= -->
	<xsl:template name="GenerateHTMLCSSStyle">
		<style type="text/css">
			/* office:document-styles begin */
		</style>
	</xsl:template>

	<!-- ======================================= -->
	<!-- Generate HTML meta from ODT content.xml -->
	<!-- ======================================= -->
	<xsl:template name="GenerateHTMLBodyContent">
		<xsl:apply-templates select="office:body/office:text" />
	</xsl:template>

</xsl:stylesheet>                
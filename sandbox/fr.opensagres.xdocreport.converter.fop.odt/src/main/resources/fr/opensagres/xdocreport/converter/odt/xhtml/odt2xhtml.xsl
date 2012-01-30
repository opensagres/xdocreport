<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

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
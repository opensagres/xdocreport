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

	<xsl:include href="utils.xsl" />
	<xsl:include href="param.xsl" />
	<xsl:include href="styles.xsl" />
	<xsl:include href="css/attribute.xsl" />
	<xsl:include href="css/layout.xsl" />
	<xsl:include href="structures/text.xsl" />
	<xsl:include href="structures/meta.xsl" />

	<xsl:variable name="styles-xml-doc" select="document('styles.xml')" />
	<xsl:variable name="meta-xml-doc" select="document('meta.xml')" />

	<xsl:variable name="lineBreak">
		<xsl:text>
</xsl:text>
	</xsl:variable>

	<xsl:key name="listTypes" match="text:list-style" use="@style:name" />

	<!-- =================== -->
	<!-- MAIN ROOT TEMPLATE -->
	<!-- =================== -->
	<xsl:template match="/office:document-content">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<xsl:call-template name="GenerateHTMLMeta" />
				<xsl:call-template name="GenerateHTMLCSSStyles" />
			</head>
			<body>
				<xsl:apply-templates select="office:body/office:text" />
			</body>
		</html>

	</xsl:template>

	<xsl:template name="GenerateHTMLMeta">
		<xsl:apply-templates select="$meta-xml-doc//office:meta" />
	</xsl:template>
<!---->
<!---->
	<!-- <xsl:template match="office:automatic-styles"> -->
	<!-- <xsl:apply-templates /> -->
	<!-- </xsl:template> -->
<!---->
	<!-- <xsl:template match="style:style"> -->
	<!-- <xsl:choose> -->
	<!-- <xsl:when test="@style:family='table'"> -->
	<!-- <xsl:call-template name="process-table-style" /> -->
	<!-- </xsl:when> -->
	<!-- <xsl:when test="@style:family='table-column'"> -->
	<!-- <xsl:call-template name="process-table-column-style" /> -->
	<!-- </xsl:when> -->
	<!-- <xsl:when test="@style:family='table-cell'"> -->
	<!-- <xsl:call-template name="process-table-cell-style" /> -->
	<!-- </xsl:when> -->
	<!-- <xsl:when test="@style:family='paragraph'"> -->
	<!-- <xsl:call-template name="process-paragraph-style" /> -->
	<!-- </xsl:when> -->
	<!-- <xsl:when test="@style:family='text'"> -->
	<!-- <xsl:call-template name="process-text-style" /> -->
	<!-- </xsl:when> -->
	<!-- </xsl:choose> -->
	<!-- </xsl:template> -->

	<xsl:template name="process-paragraph-style">
		<xsl:if test="style:paragraph-properties">
			<xsl:value-of select="$lineBreak" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="translate(@style:name,'.','_')" />
			<xsl:text>{</xsl:text>
			<xsl:value-of select="$lineBreak" />
			<xsl:call-template name="handle-style-properties">
				<xsl:with-param name="nodeSet" select="style:paragraph-properties" />
			</xsl:call-template>
			<xsl:text>}</xsl:text>
			<xsl:value-of select="$lineBreak" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="process-text-style">
		<xsl:if test="style:text-properties">
			<xsl:value-of select="$lineBreak" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="translate(@style:name,'.','_')" />
			<xsl:text>{</xsl:text>
			<xsl:value-of select="$lineBreak" />
			<xsl:call-template name="handle-style-properties">
				<xsl:with-param name="nodeSet" select="style:text-properties" />
			</xsl:call-template>
			<xsl:text>}</xsl:text>
			<xsl:value-of select="$lineBreak" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="handle-style-properties">
		<xsl:param name="nodeSet" />
		<xsl:for-each select="$nodeSet/@*">
			<xsl:variable name="this" select="." />

			<xsl:if test="true()">
				<xsl:variable name="action" select="'pass-through'" />
				<xsl:choose>
					<xsl:when test="$action='pass-through'">
						<xsl:call-template name="pass-through" />
					</xsl:when>
					<xsl:when test="$action='check-align'">
						<xsl:call-template name="check-align" />
					</xsl:when>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="pass-through">
		<xsl:value-of select="local-name()" />
		<xsl:text>: </xsl:text>
		<xsl:value-of select="." />
		<xsl:text>;</xsl:text>
		<xsl:value-of select="$lineBreak" />
	</xsl:template>

	<xsl:template name="check-align">
		<xsl:value-of select="local-name()" />
		<xsl:text>: </xsl:text>
		<xsl:choose>
			<xsl:when test=".='start'">
				<xsl:text>left</xsl:text>
			</xsl:when>
			<xsl:when test=".='end'">
				<xsl:text>right</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="." />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>;</xsl:text>
		<xsl:value-of select="$lineBreak" />
	</xsl:template>

</xsl:stylesheet>                
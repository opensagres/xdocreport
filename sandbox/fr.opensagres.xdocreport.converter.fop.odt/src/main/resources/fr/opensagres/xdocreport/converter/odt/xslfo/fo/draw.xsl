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
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:fop="http://www.w3.org/1999/XSL/Format" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
	xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	exclude-result-prefixes="office style fo svg draw text xlink" version="2.0">

	<xsl:template match="draw:frame">
		<fop:block-container>
			<xsl:choose>
				<xsl:when test="./@text:anchor-type='char'">
					<xsl:attribute name="position">absolute</xsl:attribute>
					<xsl:if test="./@svg:x or ./@svg:y">
						<xsl:attribute name="margin-left"><xsl:value-of
							select="number(substring-before(./@svg:x,'cm'))-2" />cm</xsl:attribute>
						<xsl:attribute name="margin-right">0cm</xsl:attribute>
						<!--<xsl:attribute name="margin-top"><xsl:value-of select="./@svg:y"/></xsl:attribute> -->
					</xsl:if>
					<xsl:attribute name="width"><xsl:value-of
						select="./@svg:width" /></xsl:attribute>
				</xsl:when>
			</xsl:choose>

			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name" select="./@draw:style-name" />
			</xsl:call-template>

			<fop:block>
				<xsl:choose>
					<xsl:when test="./@text:anchor-type='char'">
						<xsl:attribute name="margin-left">-<xsl:value-of
							select="number(substring-before(./@svg:x,'cm'))-2" />cm</xsl:attribute>
					</xsl:when>
				</xsl:choose>

				<xsl:for-each select="./child::*">
					<xsl:apply-templates select="." />
				</xsl:for-each>
			</fop:block>
		</fop:block-container>
	</xsl:template>

	<xsl:template match="draw:image">
		<fop:block>
			<fop:external-graphic src="url({translate(./@xlink:href, '#', '')})">
				<xsl:if test="../@svg:width">
					<xsl:attribute name="content-width"><xsl:value-of
						select="../@svg:width" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="../@svg:height">
					<xsl:attribute name="content-height"><xsl:value-of
						select="../@svg:height" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="../@svg:x">
					<xsl:attribute name="left"><xsl:value-of
						select="../@svg:x" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="../@svg:y">
					<xsl:attribute name="top"><xsl:value-of
						select="../@svg:y" /></xsl:attribute>
				</xsl:if>
			</fop:external-graphic>
		</fop:block>
	</xsl:template>

</xsl:stylesheet>
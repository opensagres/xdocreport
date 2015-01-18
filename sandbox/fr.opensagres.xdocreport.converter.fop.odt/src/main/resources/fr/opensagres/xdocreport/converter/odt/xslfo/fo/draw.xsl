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
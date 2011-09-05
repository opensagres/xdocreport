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
	xmlns:fop="http://www.w3.org/1999/XSL/Format" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	exclude-result-prefixes="office style fo text table" version="2.0">

	<xsl:template match="table:table">

		<fop:table>
			<!-- Generate FO Table attributes -->
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name"
					select="ancestor-or-self::table:table[@table:style-name][1]/@table:style-name" />
			</xsl:call-template>

			<!-- Generate FO Table Columns -->
			<xsl:call-template name="GenerateFOTableColumns" />

			<!-- Table header rows -->
			<xsl:apply-templates select="table:table-header-rows" />

			<!-- Table body -->
			<fop:table-body>
				<xsl:apply-templates select="table:table-row" />
			</fop:table-body>

		</fop:table>
	</xsl:template>

	<xsl:template name="GenerateFOTableColumns">
		<xsl:for-each select="table:table-column">
			<xsl:variable name="column-style-name" select="@table:style-name" />
			<xsl:call-template name="GenerateFOTableColumn">
				<xsl:with-param name="columnStyle"
					select="//office:automatic-styles/style:style[@style:name=$column-style-name]" />
				<xsl:with-param name="columns-repeated" select="@table:number-columns-repeated" />
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="GenerateFOTableColumn">
		<xsl:param name="columnStyle" />
		<xsl:param name="columns-repeated" />
		<fop:table-column
			column-width="{$columnStyle/style:table-column-properties/@style:column-width}" />
		<xsl:if test="$columns-repeated>1">
			<xsl:call-template name="GenerateFOTableColumn">
				<xsl:with-param name="columnStyle" select="$columnStyle" />
				<xsl:with-param name="columns-repeated" select="$columns-repeated - 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- Table Headers -->
	<xsl:template match="table:table-header-rows">
		<fop:table-header>
			<xsl:apply-templates />
		</fop:table-header>
	</xsl:template>

	<!-- Table Rows -->
	<xsl:template match="table:table-row">

		<xsl:variable name="row-style-name" select="ancestor-or-self::table:table-row[1][@table:style-name]/@table:style-name" />
		<xsl:variable name="row-style"
			select="//style:style[@style:name=$row-style-name]/style:properties" />

		<fop:table-row>
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name"
					select="$row-style-name" />
			</xsl:call-template>

			<!-- apply default values -->
			<!-- <xsl:for-each select="$default-style-table-row/style:properties/@fop:*" -->
			<!-- xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"> -->
			<!-- <xsl:attribute name="{local-name()}"><xsl:value-of select="." /></xsl:attribute> -->
			<!-- </xsl:for-each> -->

			<xsl:if test="$row-style/@style:row-height">
				<xsl:attribute name="height"><xsl:value-of
					select="$row-style/@style:row-height" /></xsl:attribute>
			</xsl:if>

			<xsl:choose>
				<xsl:when test="$row-style/@style:row-height">
					<xsl:apply-templates select="table:table-cell">
						<xsl:with-param name="cell-height" select="$row-style/@style:row-height" />
					</xsl:apply-templates>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="table:table-cell" />
				</xsl:otherwise>
			</xsl:choose>

		</fop:table-row>
	</xsl:template>

	<!-- Table Cell -->
	<xsl:template match="table:table-cell">

		<fop:table-cell>
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name"
					select="ancestor-or-self::table:table-cell[1][@table:style-name]/@table:style-name" />
			</xsl:call-template>

			<xsl:apply-templates />
		</fop:table-cell>

	</xsl:template>
</xsl:stylesheet>	
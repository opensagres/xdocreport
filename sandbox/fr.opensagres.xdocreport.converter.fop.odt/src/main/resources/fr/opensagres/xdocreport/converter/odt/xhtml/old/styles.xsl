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
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0" version="2.0">

	<xsl:template name="GenerateHTMLCSSStyles">
		<style type="text/css">
		/* office:document-styles begin */
		html
		{
			font-family: Verdana, SunSans-Regular, Sans-Serif;
			font-size: <xsl:value-of select="$scale * 14" />pt;
		}
		@media print
		{
			html
			{
			}
		}
			@media screen
		{
			html
			{
				background-color: <xsl:value-of select="$style.background-color" />;
				margin: 1.5em;
				position: absolute;
			}
			body
			{
				position: absolute;
			}
		}
		table
		{
			border: thin solid gray;
			border-collapse: collapse;
			empty-cells: show;
			font-size: 10pt;
			table-layout: fixed;
		}
		td
		{
			border: thin solid gray;
			vertical-align: bottom;
		}
		.cell_string
		{
			text-align: left;
		}
		.cell_time
		{
			text-align: right;
		}
		p
		{
			margin-top: 0;
			margin-bottom: 0;
		}
		.page-break
		{
			margin: 1em;
		}
		
		.page_table {border: 0;}
		.page_table tr {border: 0;}
		.page_table td {border: 0; padding-right:3em; vertical-align:top;}
		
		.page
		{
			background-color: white;
			border-left: 1px solid black;
			border-right: 2px solid black;
			border-top: 1px solid black;
			border-bottom: 2px solid black;
		}

			<xsl:apply-templates select="$styles-xml-doc//office:document-styles/office:master-styles" />
			/* office:document-styles end */

			/* office:automatic-styles begin */
			<xsl:apply-templates
				select="//office:document-content/office:automatic-styles/*" />
			/* office:automatic-styles end */
		</style>
	</xsl:template>

	<xsl:template match="office:styles">
		<xsl:text>/* office:styles begin */</xsl:text>
		<xsl:value-of select="$linebreak" />
		<xsl:apply-templates />
		<xsl:text>/* office:styles end */</xsl:text>
	</xsl:template>


	<xsl:template match="office:automatic-styles">
		<xsl:text>/* office:automatic-styles begin */</xsl:text>
		<xsl:apply-templates />
		<xsl:text>/* office:automatic-styles end */</xsl:text>
	</xsl:template>


	<xsl:template match="office:master-styles">
		<xsl:text>/* office:master-styles begin */</xsl:text>
		<xsl:apply-templates />
		<xsl:text>/* office:master-styles end */</xsl:text>
	</xsl:template>


	<xsl:template match="style:style">

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* style:style @style:family='</xsl:text>
			<xsl:value-of select="@style:family" />
			<xsl:text>' begin */</xsl:text>
		</xsl:if>

		<!-- classic style -->

		<xsl:value-of select="$linebreak" />
		<xsl:text>.</xsl:text>
		<xsl:value-of select="@style:family" />
		<xsl:text>_</xsl:text>
		<xsl:value-of select="translate(@style:name,'.','_')" />
		<xsl:value-of select="$linebreak" />
		<xsl:text>{</xsl:text>

		<xsl:if test="$CSS.debug=1">
			<xsl:value-of select="$linebreak" />
		</xsl:if>

		<xsl:call-template name="style_standard_default" />
		<xsl:apply-templates select="@*" mode="CSS-attr" />
		<xsl:apply-templates />

		<xsl:text>}</xsl:text>
		<xsl:value-of select="$linebreak" />

		<!-- special style for master elements -->
		<!-- elements in master-page is affected only with styles defined in styles.xml, 
			not with styles in content.xml -->
		<xsl:if
			test="ancestor::office:document-styles and @style:family='presentation'">

			<xsl:text>.</xsl:text>
			<xsl:value-of select="@style:family" />
			<xsl:text>_</xsl:text>
			<xsl:value-of select="translate(@style:name,'.','_')" />
			<xsl:text>_master</xsl:text>
			<xsl:value-of select="$linebreak" />
			<xsl:text>{</xsl:text>

			<xsl:if test="$CSS.debug=1">
				<xsl:value-of select="$linebreak" />
			</xsl:if>

			<xsl:call-template name="style_standard_default" />
			<xsl:apply-templates select="@*" mode="CSS-attr" />
			<xsl:apply-templates />

			<xsl:text>}</xsl:text>
			<xsl:value-of select="$linebreak" />

		</xsl:if>


		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* style:style @style:family='</xsl:text>
			<xsl:value-of select="@style:family" />
			<xsl:text>' end */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

	</xsl:template>


	<xsl:template name="style_standard_default" mode="CSS-attr">

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* standard_default begin */</xsl:text>
		</xsl:if>

		<xsl:choose>
			<xsl:when test="name()='style:page-layout'">

				<xsl:text>border-left: 1px solid gray;</xsl:text>
				<xsl:text>border-right: 1px solid gray;</xsl:text>
				<xsl:text>border-top: 1px solid gray;</xsl:text>
				<xsl:text>border-bottom: 1px solid gray;</xsl:text>

				<xsl:if test="//office:text|//office:spreadsheet">
					<xsl:text>background-color: white;</xsl:text>
				</xsl:if>
			</xsl:when>
			<xsl:when test="@style:family='table'">
				<!--<xsl:text>border: 0pt solid black;</xsl:text> -->
				<xsl:text>padding: 0pt;</xsl:text>
				<xsl:text>margin: 0pt;</xsl:text>
			</xsl:when>
			<xsl:when test="@style:family='paragraph'">
				<!--<xsl:text>text-align: left;</xsl:text> -->
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* standard_default end */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

	</xsl:template>


	<xsl:template match="style:style" mode="CSS-attr">

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* style:style CSS-attr @style:family='</xsl:text>
			<xsl:value-of select="@style:family" />
			<xsl:text>' begin */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

		<xsl:apply-templates select="@*" mode="CSS-attr" />
		<xsl:apply-templates />

		<xsl:if test="$CSS.debug=1">
			<xsl:value-of select="$linebreak" />
			<xsl:text>/* style:style CSS-attr @style:family='</xsl:text>
			<xsl:value-of select="@style:family" />
			<xsl:text>' end */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

	</xsl:template>

	<!-- properties to inherit -->


	<xsl:template
		match="
		style:drawing-page-properties|
		style:page-layout-properties|
		style:paragraph-properties|
		style:text-properties|
		style:graphic-properties|
		style:table-properties|
		style:table-column-properties|
		style:table-cell-properties|
		style:table-row-properties">

		<xsl:if test="$CSS.debug=1">
			<xsl:value-of select="$linebreak" />
			<xsl:text>/* </xsl:text>
			<xsl:value-of select="name()" />
			<xsl:text> begin */</xsl:text>
		</xsl:if>

		<xsl:apply-templates mode="CSS-attr" select="@*" />
		<xsl:apply-templates mode="CSS-attr" select="*" />

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* </xsl:text>
			<xsl:value-of select="name()" />
			<xsl:text> end */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

	</xsl:template>


	<xsl:template match="@style:parent-style-name" mode="CSS-attr">
		<xsl:variable name="style-name" select="." />

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* @style:parent-style-name '</xsl:text>
			<xsl:value-of select="$style-name" />
			<xsl:text>' begin */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

		<xsl:apply-templates select="//style:style[@style:name=$style-name]"
			mode="CSS-attr" />

		<xsl:if test="$CSS.debug=1">
			<xsl:text>/* @style:parent-style-name '</xsl:text>
			<xsl:value-of select="$style-name" />
			<xsl:text>' end */</xsl:text>
			<xsl:value-of select="$linebreak" />
		</xsl:if>

	</xsl:template>

	<xsl:template name="SetAttributeClass">
		<xsl:param name="prepend_style" select="''" />
		<xsl:variable name="class">

			<xsl:if test="$prepend_style != ''">
				<xsl:value-of select="$prepend_style" />
				<xsl:text> </xsl:text>
			</xsl:if>

			<!-- order by priority -->

			<xsl:if test="@draw:master-page-name">
				<xsl:text>masterpage_</xsl:text>
				<xsl:value-of select="@draw:master-page-name" />
				<xsl:text> </xsl:text>
			</xsl:if>

			<xsl:if test="name()='presentation:notes' and ../@draw:master-page-name">
				<xsl:text>masterpage_</xsl:text>
				<xsl:value-of select="../@draw:master-page-name" />
				<xsl:text>_notes </xsl:text>
			</xsl:if>

			<!-- default of family -->
			<xsl:choose>
				<xsl:when test="name()='draw:frame'">
					<xsl:text>_graphic </xsl:text>
				</xsl:when>
				<xsl:when
					test="name()='table:table-cell' and @office:value-type='string'">
					<xsl:text>cell_string </xsl:text>
				</xsl:when>
				<xsl:when test="name()='table:table-cell' and @office:value-type='time'">
					<xsl:text>cell_time </xsl:text>
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>

			<xsl:choose>
				<xsl:when test="name()='text:span'">
					<xsl:if test="@text:style-name">
						<xsl:text>text_</xsl:text>
						<xsl:value-of select="@text:style-name" />
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="@text:style-name">
						<xsl:text>paragraph_</xsl:text>
						<xsl:value-of select="@text:style-name" />
						<xsl:text> </xsl:text>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>

			<xsl:if test="@table:style-name">
				<xsl:choose>
					<xsl:when test="name()='table:table-column'">
						<xsl:text>table-column_</xsl:text>
					</xsl:when>
					<xsl:when test="name()='table:table-row'">
						<xsl:text>table-row_</xsl:text>
					</xsl:when>
					<xsl:when test="name()='table:table-cell'">
						<xsl:text>table-cell_</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>table_</xsl:text>
						<!-- void42: possible bug here -->
						<!-- due to different style naming with "class" template above -->
					</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of select="@table:style-name" />
				<xsl:text> </xsl:text>
			</xsl:if>

			<xsl:if test="@presentation:style-name">
				<xsl:text>presentation_</xsl:text>
				<xsl:value-of select="@presentation:style-name" />
				<xsl:if test="ancestor::style:master-page">
					<xsl:text>_master</xsl:text>
				</xsl:if>
				<xsl:text> </xsl:text>
			</xsl:if>

			<!-- The draw:text-style-name attribute specifies a style for the drawing 
				shape that is used to format the text that can be added to this shape. The 
				value of this attribute is the name of a <style:style> element with a family 
				value of paragraph. -->
			<xsl:if test="@draw:text-style-name">
				<xsl:text>paragraph_</xsl:text>
				<xsl:value-of select="@draw:text-style-name" />
				<xsl:text> </xsl:text>
			</xsl:if>

			<xsl:if test="@draw:style-name">
				<xsl:choose>
					<xsl:when test="local-name()='page'">
						<xsl:text>drawing-page_</xsl:text>
					</xsl:when>
					<xsl:when test="local-name()='notes'">
						<xsl:text>drawing-page_</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>graphic_</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of select="@draw:style-name" />
				<xsl:text> </xsl:text>
			</xsl:if>

		</xsl:variable>

		<xsl:if test="$class != ''">
			<xsl:attribute name="class">
				<xsl:value-of select="translate($class,'.','_')" />
			</xsl:attribute>
		</xsl:if>

	</xsl:template>


</xsl:stylesheet>
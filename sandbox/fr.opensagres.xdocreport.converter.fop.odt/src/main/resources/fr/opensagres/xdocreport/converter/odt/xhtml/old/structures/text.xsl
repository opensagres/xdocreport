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
<xsl:stylesheet xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	exclude-result-prefixes="office text style" version="2.0">

	<xsl:template match="office:text">
		<xsl:comment>
			page begin
		</xsl:comment>
		<div class="page"> <!-- default page background color -->
			<div>
				<xsl:attribute name="class">
					<xsl:text>masterpage_</xsl:text>
					<xsl:value-of select="$styles-xml-doc//style:master-page[1]/@style:name" />
				</xsl:attribute>
				<xsl:apply-templates />
			</div>
		</div>
		<xsl:comment>
			page end
		</xsl:comment>
	</xsl:template>

	<xsl:template match="text:tab">
		<xsl:text xml:space="preserve"> </xsl:text>
	</xsl:template>


	<xsl:template match="text:line-break">
		<br />
	</xsl:template>

	<xsl:template match="text:p">
		<p>
			<xsl:call-template name="SetAttributeClass" />
			<xsl:apply-templates />
			<xsl:if test="count(node())=0">
				<br />
			</xsl:if>
		</p>
	</xsl:template>

	<xsl:template match="text:span">
		<span>
			<xsl:call-template name="SetAttributeClass" />
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<xsl:template match="text:h">
		<!-- Heading levels go only to 6 in XHTML -->
		<xsl:variable name="level">
			<xsl:choose>
				<xsl:when test="@text:outline-level &gt; 6">
					6
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@text:outline-level" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="{concat('h', $level)}">
			<xsl:attribute name="class">
			<xsl:value-of select="translate(@text:style-name,'.','_')" />
		</xsl:attribute>
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>

	<!-- When processing a list, you have to look at the parent style *and* 
		level of nesting -->
	<xsl:template match="text:list">
		<xsl:variable name="level" select="count(ancestor::text:list)+1" />

		<!-- the list class is the @text:style-name of the outermost <text:list> 
			element -->
		<xsl:variable name="listClass">
			<xsl:choose>
				<xsl:when test="$level=1">
					<xsl:value-of select="@text:style-name" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="
					ancestor::text:list[last()]/@text:style-name" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- Now select the <text:list-level-style-foo> element at this level of 
			nesting for this list -->
		<xsl:variable name="node"
			select="key('listTypes',
		$listClass)/*[@text:level='$level']" />

		<!-- emit appropriate list type -->
		<xsl:choose>
			<xsl:when test="local-name($node)='list-level-style-number'">
				<ol class="{concat($listClass,'_',$level)}">
					<xsl:apply-templates />
				</ol>
			</xsl:when>
			<xsl:otherwise>
				<ul class="{concat($listClass,'_',$level)}">
					<xsl:apply-templates />
				</ul>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="text:list-item">
		<li>
			<xsl:apply-templates />
		</li>
	</xsl:template>


</xsl:stylesheet>
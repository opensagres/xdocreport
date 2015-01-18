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
	xmlns:fop="http://www.w3.org/1999/XSL/Format" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xalan="http://xml.apache.org/xalan"
	exclude-result-prefixes="office style fo text xalan xlink" version="2.0">

	<!-- ================== -->
	<!-- Template Paragraph -->
	<!-- ================== -->
	<xsl:template match="text:p|text:h|text:section">
		<fop:block>
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name" select="@text:style-name"></xsl:with-param>
			</xsl:call-template>
			<xsl:if test="name(.) = 'text:h'">
				<xsl:attribute name="id"><xsl:value-of select="text()" /></xsl:attribute>
			</xsl:if>

			<xsl:variable name="end-level" select="@text:outline-level" />
			<xsl:if test="$end-level">

				<!-- Get nodes list of text:outline-level-style -->
				<!-- <xsl:variable name="outline-level-style-nodes" -->
				<!-- select="$styles-xml-doc//office:styles/text:outline-style/text:outline-level-style" 
					/> -->
				<!-- <xsl:if -->
				<!-- test="$outline-level-style-nodes[@text:level=$end-level] and ($outline-level-style-nodes[@text:level=$end-level]/@style:num-format!='' 
					or $outline-level-style-nodes[@text:level=$end-level]/@text:bullet-char)"> -->
<!---->
				<!-- <xsl:choose> -->
				<!-- <xsl:when -->
				<!-- test="$outline-level-style-nodes[@text:level=$end-level]/@text:bullet-char"> -->
				<!-- <xsl:call-template name="calcOutlineStyle"> -->
				<!-- <xsl:with-param name="outline-level-style-nodes" -->
				<!-- select="$outline-level-style-nodes" /> -->
				<!-- <xsl:with-param name="end-level" select="$end-level" /> -->
				<!-- <xsl:with-param name="start-level" select="$end-level" /> -->
				<!-- </xsl:call-template> -->
				<!-- </xsl:when> -->
				<!-- <xsl:otherwise> -->
				<!-- <xsl:call-template name="calcOutlineStyle"> -->
				<!-- <xsl:with-param name="outline-level-style-nodes" -->
				<!-- select="$outline-level-style-nodes" /> -->
				<!-- <xsl:with-param name="end-level" select="$end-level" /> -->
				<!-- <xsl:with-param name="start-level" select="1" /> -->
				<!-- </xsl:call-template> -->
				<!-- </xsl:otherwise> -->
				<!-- </xsl:choose> -->
<!---->
				<!-- </xsl:if> -->
			</xsl:if>


			<xsl:if test="string-length(current()) = 0">
				<xsl:text> &#160;</xsl:text>
			</xsl:if>
			<xsl:apply-templates />

		</fop:block>
	</xsl:template>


	<xsl:template name="calcOutlineStyle">
		<xsl:param name="outline-level-style-nodes" />
		<xsl:param name="end-level" />
		<xsl:param name="start-level" />

		<xsl:choose>
			<xsl:when test="$end-level >= $start-level">
				<xsl:choose>
					<xsl:when
						test="$outline-level-style-nodes[@text:level=$start-level]/@style:num-format!='' and ($outline-level-style-nodes[@text:level=$start-level]/@text:display-levels!='' or $start-level=1)">
						<xsl:choose>
							<xsl:when test="$start-level > 1 ">
								.
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of
									select="$outline-level-style-nodes[@text:level=$end-level]/@style:num-prefix" />
							</xsl:otherwise>
						</xsl:choose>

						<xsl:number
							format="{$outline-level-style-nodes[@text:level=$start-level]/@style:num-format}"
							level="any" count="text:h[@text:outline-level=$start-level]" />
					</xsl:when>
					<xsl:when
						test="$outline-level-style-nodes[@text:level=$start-level]/@text:bullet-char">
						<xsl:value-of select="$outline-level-style-nodes/@text:bullet-char" />
					</xsl:when>
				</xsl:choose>

				<xsl:call-template name="calcOutlineStyle">
					<xsl:with-param name="outline-level-style-nodes"
						select="$outline-level-style-nodes" />
					<xsl:with-param name="end-level" select="$end-level" />
					<xsl:with-param name="start-level" select="$start-level + 1" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="$outline-level-style-nodes[@text:level=$end-level]/@style:num-suffix" />
				<xsl:if test="name(.)!='text:list-item'">
					<fop:leader leader-pattern="space">
						<xsl:choose>
							<xsl:when
								test="$outline-level-style-nodes[@text:level=$end-level]/style:list-level-properties/@text:min-label-distance">
								<xsl:attribute name="leader-length"><xsl:value-of
									select="$outline-level-style-nodes[@text:level=$end-level]/style:list-level-properties/@text:min-label-distance" /></xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="leader-length">0.3cm</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</fop:leader>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ================== -->
	<!-- Template Span -->
	<!-- ================== -->
	<xsl:template match="text:span">
		<fop:inline>
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name" select="@text:style-name" />
			</xsl:call-template>
			<xsl:apply-templates />
		</fop:inline>
	</xsl:template>

	<!-- ================== -->
	<!-- Template Tabulation -->
	<!-- ================== -->
	<xsl:template match="text:tab">

		<xsl:variable name="style-name">
			<xsl:call-template name="GetParentOrSelfTextStyleName">
				<xsl:with-param name="node-set" select="." />
			</xsl:call-template>
		</xsl:variable>

		<fop:leader>
			<xsl:call-template name="ApplyStyle">
				<xsl:with-param name="style-name" select="$style-name" />
				<xsl:with-param name="style-type" select="'text:tab'" />
				<xsl:with-param name="style-position"
					select="count(preceding-sibling::text:tab)" />
			</xsl:call-template>
		</fop:leader>
		<!-- <xsl:variable name="outline-level-style-nodes" -->
		<!-- select="$styles-xml-doc//office:styles/style:style" /> -->
<!---->
		<!-- <xsl:variable name="tabStyle"> -->
		<!-- <text:tab> -->
		<!-- <xsl:call-template name="ApplyStyle"> -->
		<!-- <xsl:with-param name="style-name" -->
		<!-- select="ancestor-or-self::*[@text:style-name]/@text:style-name" /> -->
		<!-- <xsl:with-param name="styleType" select="'text:tab'" /> -->
		<!-- <xsl:with-param name="stylePosition" -->
		<!-- select="count(preceding-sibling::text:tab)" /> -->
		<!-- </xsl:call-template> -->
		<!-- </text:tab> -->
		<!-- </xsl:variable> -->
<!---->
		<!-- <xsl:choose> -->
		<!-- <xsl:when test="function-available('xalan:nodeset')"> -->
		<!-- <xsl:call-template name="getTabStyleValues"> -->
		<!-- <xsl:with-param name="styleNode" select="xalan:nodeset($tabStyle)" 
			/> -->
		<!-- <xsl:with-param name="aktNode" select="." /> -->
		<!-- </xsl:call-template> -->
		<!-- </xsl:when> -->
		<!-- <xsl:otherwise> -->
		<!-- <xsl:message terminate="yes"> -->
		<!-- ERROR: Function not found: nodeset -->
		<!-- </xsl:message> -->
		<!-- </xsl:otherwise> -->
		<!-- </xsl:choose> -->

	</xsl:template>

	<!-- <xsl:template name="getTabStyleValues"> -->
	<!-- <xsl:param name="styleNode" /> -->
	<!-- <xsl:param name="aktNode" /> -->
<!---->
	<!-- <xsl:for-each select="$styleNode/child::*"> -->
	<!-- <xsl:choose> -->
	<!-- <xsl:when -->
	<!-- test="./@type and normalize-space($aktNode/preceding-sibling::text())=''"> -->
	<!-- <xsl:attribute name="text-align"><xsl:value-of -->
	<!-- select="./@type" /></xsl:attribute> -->
	<!-- </xsl:when> -->
	<!-- <xsl:when test="./@type and ./@leader-style"> -->
	<!-- <fop:leader> -->
	<!-- <xsl:choose> -->
	<!-- <xsl:when test="./@leader-style='dotted'"> -->
	<!-- <xsl:attribute name="leader-pattern">dots</xsl:attribute> -->
	<!-- </xsl:when> -->
	<!-- <xsl:otherwise> -->
	<!-- <xsl:attribute name="leader-pattern">space</xsl:attribute> -->
	<!-- </xsl:otherwise> -->
	<!-- </xsl:choose> -->
	<!-- </fop:leader> -->
	<!-- </xsl:when> -->
	<!-- <xsl:otherwise> -->
	<!-- <fop:leader leader-pattern="space"> -->
	<!-- <xsl:attribute name="leader-length"><xsl:value-of -->
	<!-- select="./@tab-stop-distance" /></xsl:attribute> -->
	<!-- </fop:leader> -->
	<!-- </xsl:otherwise> -->
	<!-- </xsl:choose> -->
	<!-- </xsl:for-each> -->
	<!-- </xsl:template> -->

	<xsl:template match="text:table-of-content|text:user-index">
		<xsl:apply-templates select="./text:index-body/text:index-title" />

		<xsl:for-each select="./text:index-body/text:p">
			<fop:block>
				<xsl:call-template name="ApplyStyle">
					<xsl:with-param name="style-name" select="./@text:style-name" />
				</xsl:call-template>
				<xsl:apply-templates />
			</fop:block>
		</xsl:for-each>

	</xsl:template>


	<!-- <xsl:template match="text:index-title"> -->
	<!-- <xsl:call-template name="ApplyStyle"> -->
	<!-- <xsl:with-param name="style-name" select="./@text:style-name" /> -->
	<!-- </xsl:call-template> -->
	<!-- <xsl:apply-templates /> -->
	<!-- </xsl:template> -->

	<!-- Hyperlink -->
	<xsl:template match="text:a">

			<!-- Is internal link -->
			<xsl:variable name="is-internal-link" select="starts-with(@xlink:href, '#')" />
			<!-- Compute destination link -->
			<xsl:variable name="destination">
				<xsl:choose>
					<xsl:when test="$is-internal-link">
						<xsl:value-of
							select="substring-before(substring(@xlink:href, string-length(../text()) + 2, string-length(@xlink:href)), '|outline')" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat('url(', @xlink:href, ')')" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xsl:choose>
				<xsl:when test="$destination != ''">
					<!-- destination filled, generate link -->
					<fop:basic-link>
						<!-- Generate destination link -->
						<xsl:choose>
							<xsl:when test="$is-internal-link">
								<xsl:attribute name="internal-destination"><xsl:value-of
									select="$destination" /></xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="external-destination"><xsl:value-of
									select="$destination" /></xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:attribute name="text-decoration">underline</xsl:attribute>
						<!-- Apply styles -->
						<xsl:call-template name="ApplyStyle">
							<xsl:with-param name="style-name" select="./@text:style-name" />
						</xsl:call-template>
						<xsl:value-of select="." />
					</fop:basic-link>
				</xsl:when>
				<xsl:otherwise>
					<!-- Destination is empty, generate text -->
					<fop:inline>
						<xsl:call-template name="ApplyStyle">
							<xsl:with-param name="style-name" select="./@text:style-name" />
						</xsl:call-template>
						<xsl:attribute name="text-decoration">underline</xsl:attribute>
						<xsl:value-of select="." />
					</fop:inline>
				</xsl:otherwise>
			</xsl:choose>

	</xsl:template>

	<xsl:template match="text:page-number">
		<fop:page-number />
	</xsl:template>

	<xsl:template match="text:page-count">
		<fop:page-number-citation ref-id="last-page" />
	</xsl:template>

</xsl:stylesheet>	
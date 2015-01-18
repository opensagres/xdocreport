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
<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
	version="2.0">

	<!-- ============================================================ -->
	<!-- Elements that can occur anywhere where we start a new block, -->
	<!-- such inside the body, comments, headers, textboxes, etc. -->
	<!-- ============================================================ -->
	<!-- ============================================= -->
	<xsl:template name="GenerateSection">
		<xsl:param name="context" />
		<fo:block>
			<!-- Set default properties. -->
			<xsl:call-template name="SetDefaultAttributes" />
			<fo:marker marker-class-name="first-page-header">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'header'" />
					<xsl:with-param name="type" select="'first'" />
				</xsl:call-template>
			</fo:marker>
			<fo:marker marker-class-name="first-page-footer">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'footer'" />
					<xsl:with-param name="type" select="'first'" />
				</xsl:call-template>
			</fo:marker>
			<fo:marker marker-class-name="odd-page-header">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'header'" />
					<xsl:with-param name="type" select="'odd'" />
				</xsl:call-template>
			</fo:marker>
			<fo:marker marker-class-name="odd-page-footer">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'footer'" />
					<xsl:with-param name="type" select="'odd'" />
				</xsl:call-template>
			</fo:marker>
			<fo:marker marker-class-name="even-page-header">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'header'" />
					<xsl:with-param name="type" select="'even'" />
				</xsl:call-template>
			</fo:marker>
			<fo:marker marker-class-name="even-page-footer">
				<xsl:call-template name="DisplayStaticContent">
					<xsl:with-param name="section" select="$context" />
					<xsl:with-param name="hdr-or-ftr" select="'footer'" />
					<xsl:with-param name="type" select="'even'" />
				</xsl:call-template>
			</fo:marker>
			<xsl:variable name="body-content">
				<xsl:call-template name="AnalyzeHighlevelContent">
					<xsl:with-param name="current-node" select="$context" />
				</xsl:call-template>
			</xsl:variable>
			<xsl:copy-of select="$body-content" />
		</fo:block>
	</xsl:template>

	<xsl:template match="w:t">
		<fo:leader leader-length="0pt" />
		<xsl:value-of select="." />
	</xsl:template>

	<!-- This template analyzes "high-level" content (sections and subsections) -->
	<xsl:template name="AnalyzeHighlevelContent">
		<!-- current-node is wx:sect or wx:sub-section -->
		<xsl:param name="current-node" />
		<xsl:variable name="two.table.in.one.row">
			<xsl:for-each select="$current-node/self::*">
				<xsl:if test="name(.) = 'w:tbl' ">
					<xsl:variable name="pos" select="position()" />
					<xsl:for-each select="$current-node/self::*">
						<xsl:if test="position() = $pos +1 and name(.) = 'w:tbl' ">
							<xsl:text>true</xsl:text>
						</xsl:if>
					</xsl:for-each>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:choose>
			<!-- 06-27-2006: Get into Linearize template only if condition for two 
				tables in row is true -->
			<xsl:when test="$two.table.in.one.row = 'true'">
				<xsl:call-template name="Linearize">
					<xsl:with-param name="current-node" select="$current-node" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates
					select="$current-node/self::*[ not(self::w:sectPr) ]" />
				<!-- <xsl:for-each select="$current-node/self::wx:sub-section">
					<xsl:variable name="pos" select="position()" />
					<fo:block id="{generate-id()}">
						<xsl:call-template name="AnalyzeHighlevelContent">
							<xsl:with-param name="current-node" select="node()" />
						</xsl:call-template>
					</fo:block>
				</xsl:for-each> -->
				<xsl:apply-templates select="$current-node/self::w:sectPr" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="Linearize">
		<xsl:param name="current-node" />
		<xsl:choose>
			<xsl:when test="$current-node/self::w:p">
				<xsl:for-each select="$current-node/self::w:p">
					<xsl:variable name="preceding-siblings" select="preceding-sibling::*" />
					<xsl:variable name="current-portion"
						select="$preceding-siblings[count(following-sibling::w:p[1] | current()) = 1 and not(self::w:p) and not(self::w:proofErr)]" />
					<xsl:call-template name="BreakContentIntoOneRowTable">
						<xsl:with-param name="content" select="$current-portion" />
					</xsl:call-template>
					<xsl:apply-templates select="current()" />
					<xsl:if test="position() = last()">
						<xsl:call-template name="BreakContentIntoOneRowTable">
							<xsl:with-param name="content"
								select="following-sibling::*[not(self::w:sectPr | self::w:proofErr)]" />
						</xsl:call-template>
						<xsl:apply-templates select="following-sibling::w:sectPr" />
					</xsl:if>
				</xsl:for-each>
				<xsl:if test="$current-node/self::w:p[last()]/w:pPr/w:sectPr">
					<fo:block break-after="page" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="BreakContentIntoOneRowTable">
					<xsl:with-param name="content"
						select="$current-node/self::*[not(self::w:sectPr | self::w:proofErr)]" />
				</xsl:call-template>
				<xsl:apply-templates select="$current-node/self::w:sectPr" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="BreakContentIntoOneRowTable">
		<!-- content is a node-set -->
		<xsl:param name="content" />
		<xsl:if test="$content">
			<fo:block>
				<fo:table padding-before="5pt" padding-after="5pt">
					<fo:table-body>
						<fo:table-row>
							<xsl:for-each select="$content">
								<fo:table-cell padding-start="10pt" padding-end="10pt">
									<fo:block>
										<xsl:apply-templates select="current()" />
									</fo:block>
								</fo:table-cell>
							</xsl:for-each>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
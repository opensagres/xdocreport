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

	<xsl:template name="GenerateFOPageSequences">
		<xsl:param name="context" select="w:body/*" />
		<xsl:param name="number" select="1" />

		<xsl:call-template name="GenerateFOPageSequencesBody">
			<xsl:with-param name="sect"
				select="$context[(count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($number - 1) and ($number = 1 or not(./descendant-or-self ::w:sectPr/w:type/@w:val='continuous'))) or (count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($number) and not(./descendant-or-self::w:sectPr[not(w:type/@w:val='continuous')]) and (./descendant-or-self::w:sectPr/w:type/@w:val='continuous' or ./following::w:sectPr[1]/w:type/@w:val='continuous'))]" />
			<xsl:with-param name="page-index" select="$number" />
		</xsl:call-template>

		<xsl:variable name="remainder"
			select="$context[(count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])>$number) or (count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=$number and (./descendant-or-self::w:sectPr[not(w:type/@w:val='continuous')] or (not(./descendant-or-self::w:sectPr/w:type/@w:val='continuous') and not(./following::w:sectPr[1]/w:type/@w:val='continuous')))) ]" />
		<xsl:if test="count($remainder) > 0">
			<xsl:call-template name="GenerateFOPageSequences">
				<xsl:with-param name="context" select="$remainder" />
				<xsl:with-param name="number" select="$number + 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- ======================== -->
	<!-- Generate page sequences. -->
	<!-- ======================== -->
	<xsl:template name="GenerateFOPageSequencesBody">
		<xsl:param name="sect" />
		<xsl:param name="page-index" />

		<!-- ==================================================== -->
		<!-- Apply page sequences for all non-continuous sections -->
		<!-- ==================================================== -->

		<xsl:variable name="page-sequence-master-name"
			select="concat('section',$page-index,'-page-sequence-master')" />
		<fo:page-sequence master-reference="{$page-sequence-master-name}"
			id="{generate-id()}">
			<!-- Set format attribute -->
			<xsl:variable name="instruction"
				select="normalize-space($sect//w:fldSimple[1]/@w:instr[contains(., 'PAGE')])" />
			<xsl:attribute name="format">
          <xsl:call-template name="PageNumberFormat">
            <xsl:with-param name="format"
				select="substring-before(substring-after($instruction, '\* '), ' ')" />
          </xsl:call-template>
        </xsl:attribute>

			<!-- =================== -->
			<!-- Headers and Footers -->
			<!-- =================== -->
			<fo:static-content flow-name="first-page-header">
				<fo:retrieve-marker retrieve-class-name="first-page-header"
					retrieve-position="first-including-carryover" retrieve-boundary="page" />
			</fo:static-content>
			<fo:static-content flow-name="first-page-footer">
				<fo:retrieve-marker retrieve-class-name="first-page-footer"
					retrieve-position="first-including-carryover" retrieve-boundary="page" />
			</fo:static-content>
			<fo:static-content flow-name="odd-page-header">
				<fo:retrieve-marker retrieve-class-name="odd-page-header"
					retrieve-position="first-including-carryover" retrieve-boundary="page" />
			</fo:static-content>
			<fo:static-content flow-name="odd-page-footer">
				<fo:retrieve-marker retrieve-class-name="odd-page-footer"
					retrieve-position="first-including-carryover" retrieve-boundary="page" />
			</fo:static-content>
			<fo:static-content flow-name="even-page-header">
				<xsl:choose>
					<xsl:when
						test="/w:document/w:docPr/w:evenAndOddHeaders[not(@w:val) or @w:val!='off' ]"> <!-- element w:evenAndOddHeaders appears only if different headers for odd/even 
							pages specified -->
						<fo:retrieve-marker retrieve-class-name="even-page-header"
							retrieve-position="first-including-carryover" retrieve-boundary="page" />
					</xsl:when>
					<xsl:otherwise>
						<fo:retrieve-marker retrieve-class-name="odd-page-header"
							retrieve-position="first-including-carryover" retrieve-boundary="page" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:static-content>
			<fo:static-content flow-name="even-page-footer">
				<xsl:choose>
					<xsl:when
						test="/w:document/w:docPr/w:evenAndOddHeaders[not(@w:val) or @w:val!='off' ]"> <!-- element w:evenAndOddHeaders appears only if different footers for odd/even 
							pages specified -->
						<fo:retrieve-marker retrieve-class-name="even-page-footer"
							retrieve-position="first-including-carryover" retrieve-boundary="page" />
					</xsl:when>
					<xsl:otherwise>
						<fo:retrieve-marker retrieve-class-name="odd-page-footer"
							retrieve-position="first-including-carryover" retrieve-boundary="page" />
					</xsl:otherwise>
				</xsl:choose>
			</fo:static-content>
			<!-- ========= -->
			<!-- Footnotes -->
			<!-- ========= -->
			<fo:static-content flow-name="xsl-footnote-separator">
				<fo:block>
					<fo:leader leader-pattern="rule" leader-length="144pt"
						rule-thickness="0.5pt" rule-style="solid" color="gray" />
				</fo:block>
			</fo:static-content>
			<!-- =================================================================== -->
			<!-- Text flow is produced by applying templates to the wx:sect element: -->
			<!-- see templates under "DOCUMENT'S CONTENT" -->
			<!-- =================================================================== -->
			<fo:flow flow-name="xsl-region-body">
				<!-- Display the document's content -->
				<!--<xsl:apply-templates select=".|following-sibling::wx:sect[position() 
					&lt; $number-of-continuous-sections]" /> -->

				<xsl:call-template name="ProcessContinuousSections">
					<xsl:with-param name="sect" select="$sect" />
				</xsl:call-template>

				<xsl:if test="position()=last()">
					<!-- Generate an empty block at the end of the FO document. -->
					<fo:block id="{generate-id(/)}" />
				</xsl:if>
			</fo:flow>

		</fo:page-sequence>
		<!-- </xsl:for-each> -->
	</xsl:template>

	<xsl:template name="ProcessContinuousSections">
		<xsl:param name="sect" />
		<xsl:param name="number" select="count($sect[1]/preceding::w:sectPr)" />

		<!-- $context[(count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($number 
			- 1) and not(./descendant-or-self ::w:sectPr/w:type/@w:val='continuous')) 
			or (count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($number) 
			and (./descendant-or-self::w:sectPr/w:type/@w:val='continuous' or ./following-sibling::w:sectPr[1]/w:type/@w:val='continuous'))] -->

		<xsl:variable name="part"
			select="$sect[ count(./preceding::w:sectPr)=$number ] " />
		<xsl:variable name="remainder"
			select="$sect[ count(./preceding::w:sectPr)>$number ]" />

		<xsl:call-template name="GenerateSection">
			<xsl:with-param name="context" select="$part" />
		</xsl:call-template>

		<xsl:if test="count($remainder)>0">
			<xsl:call-template name="ProcessContinuousSections">
				<xsl:with-param name="sect" select="$remainder" />
				<xsl:with-param name="number" select="$number + 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>


	<!-- ====================== -->
	<!-- Display static content -->
	<!-- ====================== -->
	<xsl:template name="DisplayStaticContent">
		<xsl:param name="section" />
		<xsl:param name="hdr-or-ftr" />
		<xsl:param name="type" />
		<!-- $sect/descendant-or-self::w:sectPr -->
		<!-- $section/self::w:p -->
		<xsl:choose>
			<xsl:when
				test="$hdr-or-ftr='header' and $section/descendant-or-self::w:sectPr/w:hdr[@w:type=$type]">
				<xsl:if test="$section/self::w:p/w:pPr/w:sectPr/w:hdr[@w:type=$type]">
					<xsl:apply-templates
						select="$section/self::w:p/w:pPr/w:sectPr/w:hdr[@w:type=$type]" />
				</xsl:if>
				<xsl:if
					test="not($section/self::w:p/w:pPr/w:sectPr/w:hdr[@w:type=$type])">
					<xsl:if
						test="count($section/descendant-or-self::w:sectPr/w:hdr[@w:type=$type]) > 1">
						<xsl:apply-templates
							select="$section/descendant-or-self::w:sectPr/w:hdr[1][@w:type=$type]" />
					</xsl:if>
					<xsl:if
						test="count($section/descendant-or-self::w:sectPr/w:hdr[@w:type=$type]) = 1">
						<xsl:apply-templates
							select="$section/descendant-or-self::w:sectPr/w:hdr[@w:type=$type]" />
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:when
				test="$hdr-or-ftr='footer' and $section/descendant-or-self::w:sectPr/w:ftr[@w:type=$type]">
				<xsl:if test="$section/self::w:p/w:pPr/w:sectPr/w:ftr[@w:type=$type]">
					<xsl:apply-templates
						select="$section/self::w:p/w:pPr/w:sectPr/w:ftr[@w:type=$type]" />
				</xsl:if>
				<xsl:if
					test="not($section/self::w:p/w:pPr/w:sectPr/w:ftr[@w:type=$type])">
					<xsl:if
						test="count($section/descendant-or-self::w:sectPr/w:ftr[@w:type=$type]) > 1">
						<xsl:apply-templates
							select="$section/descendant-or-self::w:sectPr/w:ftr[1][@w:type=$type]" />
					</xsl:if>
					<xsl:if
						test="count($section/descendant-or-self::w:sectPr/w:ftr[@w:type=$type]) = 1">
						<xsl:apply-templates
							select="$section/descendant-or-self::w:sectPr/w:ftr[@w:type=$type]" />
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:when
				test="$section and (($section/descendant-or-self::w:sectPr)[1] != (w:body//w:sectPr)[1])"> <!-- $section and ($section != w:body/wx:sect[1]) -->
				<xsl:call-template name="DisplayStaticContent">
					<!--<xsl:with-param name="section" select="$section[1]/ancestor::w:body/wx:sect[1]"/> --> <!-- $section[1]/../../wx:sect[1] --> <!-- $section/../preceding-sibling::wx:sect[1] -->
					<xsl:with-param name="section"
						select="($section[1]/ancestor::w:body//w:sectPr)[1]/../.." />
					<xsl:with-param name="hdr-or-ftr" select="$hdr-or-ftr" />
					<xsl:with-param name="type" select="$type" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
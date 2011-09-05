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

<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
	version="2.0">

	<!-- ========================================================= -->
	<!-- GENERATE fo:layout-master-set -->
	<!-- ========================================================= -->
	<xsl:template name="GenerateFOLayoutMasterSet">
		<fo:layout-master-set>
			<xsl:call-template name="GenerateFOLayoutMasterSetBody" />
		</fo:layout-master-set>
	</xsl:template>

	<!-- ========================================================= -->
	<!-- GENERATE BODY of fo:layout-master-set -->
	<!-- ========================================================= -->
	<xsl:template name="GenerateFOLayoutMasterSetBody">
		<!-- By default current-DOMNodes is nodes list included to w:body -->
		<xsl:param name="current-DOMNodes" select="w:body/*" />
		<xsl:param name="page-index" select="1" />

		<!-- ==================================================================================== -->
		<!-- 1) Generate fo:simple-page-master, fo:page-sequence-master for current 
			DOM node list which contains w:sectPr -->
		<!-- ==================================================================================== -->
		<xsl:call-template name="GenerateFOPageMasters">
			<xsl:with-param name="sect"
				select="$current-DOMNodes[(count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($page-index - 1) and ($page-index = 1 or not(./descendant-or-self ::w:sectPr/w:type/@w:val='continuous'))) or (count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=($page-index) and not(./descendant-or-self::w:sectPr[not(w:type/@w:val='continuous')]) and (./descendant-or-self::w:sectPr/w:type/@w:val='continuous' or ./following::w:sectPr[1]/w:type/@w:val='continuous'))]" />
			<xsl:with-param name="page-index" select="$page-index" />
		</xsl:call-template>

		<!-- ==================================================================================== -->
		<!-- 2) Generate fo:simple-page-master, fo:page-sequence-master for next 
			DOM node list by incrementing page-index -->
		<!-- ==================================================================================== -->
		<xsl:variable name="remainder"
			select="$current-DOMNodes[(count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])>$page-index) or (count(./preceding::w:sectPr[not(w:type/@w:val='continuous')])=$page-index and (./descendant-or-self::w:sectPr[not(w:type/@w:val='continuous')] or (not(./descendant-or-self::w:sectPr/w:type/@w:val='continuous') and not(./following::w:sectPr[1]/w:type/@w:val='continuous')))) ]" />
		<xsl:if test="count($remainder) > 0">
			<xsl:call-template name="GenerateFOLayoutMasterSetBody">
				<xsl:with-param name="current-DOMNodes" select="$remainder" />
				<xsl:with-param name="page-index" select="$page-index + 1" />
			</xsl:call-template>
		</xsl:if>

	</xsl:template>

	<!-- ========================================================= -->
	<!-- GENERATE fo:simple-page-master and fo:page-sequence-master -->
	<!-- ========================================================= -->
	<xsl:template name="GenerateFOPageMasters">
		<xsl:param name="sect" />
		<xsl:param name="page-index" />

		<!-- ========================================================= -->
		<!-- Generate page masters and page sequences for each section -->
		<!-- ========================================================= -->
		<xsl:variable name="first-page-name"
			select="concat('section',$page-index,'-first-page')" />
		<xsl:variable name="odd-page-name"
			select="concat('section',$page-index,'-odd-page')" />
		<xsl:variable name="even-page-name"
			select="concat('section',$page-index,'-even-page')" />
		<xsl:variable name="page-sequence-master-name"
			select="concat('section',$page-index,'-page-sequence-master')" />

		<!-- ====================================== -->
		<!-- Masters for first, odd, and even pages -->
		<!-- ====================================== -->
		<fo:simple-page-master master-name="{$first-page-name}">
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgSz" />
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
				<xsl:with-param name="area" select="'page-master'" />
			</xsl:apply-templates>

			<fo:region-body>
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'region-body'" />
				</xsl:apply-templates>
				<xsl:apply-templates
					select="$sect/descendant-or-self::w:sectPr/w:pgBorders">
					<xsl:with-param name="page" select="first" />
				</xsl:apply-templates>
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:cols" />
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:vAlign" />
			</fo:region-body>
			<fo:region-before region-name="first-page-header"
				extent="{$default-header-extent}">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-before>
			<fo:region-after region-name="first-page-footer"
				extent="{$default-footer-extent}" display-align="after">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-after>
		</fo:simple-page-master>

		<fo:simple-page-master master-name="{$odd-page-name}">
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgSz" />
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
				<xsl:with-param name="area" select="'page-master'" />
			</xsl:apply-templates>

			<fo:region-body>
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'region-body'" />
				</xsl:apply-templates>
				<xsl:apply-templates
					select="$sect/descendant-or-self::w:sectPr/w:pgBorders" />
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:cols" />
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:vAlign" />
			</fo:region-body>
			<fo:region-before region-name="odd-page-header"
				extent="{$default-header-extent}">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-before>
			<fo:region-after region-name="odd-page-footer"
				extent="{$default-footer-extent}" display-align="after">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-after>
		</fo:simple-page-master>

		<fo:simple-page-master master-name="{$even-page-name}">
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgSz" />
			<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
				<xsl:with-param name="area" select="'page-master'" />
			</xsl:apply-templates>

			<fo:region-body>
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'region-body'" />
				</xsl:apply-templates>
				<xsl:apply-templates
					select="$sect/descendant-or-self::w:sectPr/w:pgBorders" />
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:cols" />
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:vAlign" />
			</fo:region-body>
			<fo:region-before region-name="even-page-header"
				extent="{$default-header-extent}">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-before>
			<fo:region-after region-name="even-page-footer"
				extent="{$default-footer-extent}" display-align="after">
				<xsl:apply-templates select="$sect/descendant-or-self::w:sectPr/w:pgMar">
					<xsl:with-param name="area" select="'static-content'" />
				</xsl:apply-templates>
			</fo:region-after>
		</fo:simple-page-master>

		<!-- create a page-sequence master -->
		<fo:page-sequence-master master-name="{$page-sequence-master-name}">
			<xsl:if
				test="$sect/descendant-or-self::w:sectPr/w:titlePg[not(@w:val='off')]">
				<fo:single-page-master-reference
					master-reference="{$first-page-name}" />
			</xsl:if>
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference
					odd-or-even="odd" master-reference="{$odd-page-name}" />
				<fo:conditional-page-master-reference
					odd-or-even="even" master-reference="{$even-page-name}" />
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>

	</xsl:template>
</xsl:stylesheet>
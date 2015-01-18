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
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
	xmlns:fop="http://www.w3.org/1999/XSL/Format" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
	exclude-result-prefixes="office style text table fo svg" version="2.0">

	<!-- InterpretStyle template implementation to generate FO attributes by 
		using style-node -->
	<xsl:template name="InterpretStyle">
		<xsl:param name="style-node" />
		<xsl:param name="style-type" />
		<xsl:param name="style-position" />

		<xsl:choose>

			<!-- Interpret Tabulation style -->
			<xsl:when test="$style-type='text:tab'">

				<xsl:variable name="paragraph-properties"
					select="$style-node/style:paragraph-properties" />
				<xsl:if test="$paragraph-properties">

					<xsl:if test="$paragraph-properties/@style:tab-stop-distance">
						<xsl:attribute name="leader-length"><xsl:value-of
							select="$paragraph-properties/@style:tab-stop-distance" /></xsl:attribute>
					</xsl:if>

					<xsl:variable name="tab-stop"
						select="$paragraph-properties/style:tab-stops/style:tab-stop[$style-position+1]" />

					<xsl:if test="$tab-stop">

						<!-- <xsl:if test="$tab-stop/@style:type"> -->
						<!-- <xsl:attribute name="text-indent"><xsl:value-of -->
						<!-- select="$tab-stop/@style:type" /></xsl:attribute> -->
						<!-- </xsl:if> -->
						<xsl:if test="$tab-stop/@style:leader-style">
							<xsl:choose>
								<xsl:when test="$tab-stop/@style:leader-style='dotted'">
									<xsl:attribute name="leader-pattern">dots</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="leader-pattern">space</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>

					</xsl:if>
				</xsl:if>
			</xsl:when>

			<!-- Interpret Header footer style -->
			<xsl:when test="$style-type='header:footer'">
				<xsl:if test="$style-node/style:header-footer-properties/@fo:min-height">
					<xsl:attribute name="min-height"><xsl:value-of
						select="$style-node/style:header-footer-properties/@fo:min-height" /></xsl:attribute>
				</xsl:if>


				<xsl:if test="$style-node/style:header-footer-properties">
					<xsl:call-template name="interpetBoxStyle">
						<xsl:with-param name="style-node"
							select="$style-node/style:header-footer-properties" />
					</xsl:call-template>
				</xsl:if>
				<xsl:choose>
					<xsl:when
						test="$style-node/style:header-footer-properties/@svg:height and $style-node/style:header-footer-properties/@fo:margin-bottom">
						<xsl:attribute name="extent"><xsl:value-of
							select="$style-node/style:header-footer-properties/@svg:height" /> - <xsl:value-of
							select="$style-node/style:header-footer-properties/@fo:margin-bottom" /></xsl:attribute>
					</xsl:when>
					<xsl:when
						test="$style-node/style:header-footer-properties/@svg:height and $style-node/style:header-footer-properties/@fo:margin-top">
						<xsl:attribute name="extent"><xsl:value-of
							select="$style-node/style:header-footer-properties/@svg:height" /> - <xsl:value-of
							select="$style-node/style:header-footer-properties/@fo:margin-top" /></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="extent">0.55cm</xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<!-- Interpret Table style -->
			<xsl:when test="name(.)='table:table'">
				<xsl:if test="$style-node/style:table-properties">
					<xsl:call-template name="interpetBoxStyle">
						<xsl:with-param name="style-node"
							select="$style-node/style:table-properties" />
					</xsl:call-template>
				</xsl:if>
			</xsl:when>

			<!-- Interpret Table Row style -->
			<xsl:when test="name(.)='table:table-row'">
				<xsl:if test="$style-node/style:table-row-properties">
					<xsl:call-template name="interpetBoxStyle">
						<xsl:with-param name="style-node"
							select="$style-node/style:table-row-properties" />
					</xsl:call-template>
				</xsl:if>
			</xsl:when>

			<!-- Interpret Table Cell style -->
			<xsl:when test="name(.)='table:table-cell'">
				<xsl:if test="$style-node/style:table-cell-properties">
					<xsl:call-template name="interpetBoxStyle">
						<xsl:with-param name="style-node"
							select="$style-node/style:table-cell-properties" />
					</xsl:call-template>
				</xsl:if>
			</xsl:when>

			<!-- Interpret Other properties -->
			<xsl:otherwise>
				<xsl:if test="$style-node/style:paragraph-properties">
					<xsl:call-template name="interpetBoxStyle">
						<xsl:with-param name="style-node"
							select="$style-node/style:paragraph-properties" />
					</xsl:call-template>

					<xsl:if test="$style-node/style:paragraph-properties/@fo:text-indent">
						<xsl:attribute name="text-indent"><xsl:value-of
							select="$style-node/style:paragraph-properties/@fo:text-indent" /></xsl:attribute>
					</xsl:if>

					<xsl:if test="$style-node/style:paragraph-properties/@fo:keep-with-next">
						<xsl:attribute name="keep-with-next"><xsl:value-of
							select="$style-node/style:paragraph-properties/@fo:keep-with-next" /></xsl:attribute>
					</xsl:if>

					<xsl:if test="$style-node/style:paragraph-properties/@fo:text-align">
						<xsl:attribute name="text-align"><xsl:value-of
							select="$style-node/style:paragraph-properties/@fo:text-align" /></xsl:attribute>
					</xsl:if>

					<xsl:choose>
						<xsl:when test="$style-node/style:paragraph-properties/@fo:line-height">
							<xsl:attribute name="line-height"><xsl:value-of
								select="$style-node/style:paragraph-properties/@fo:line-height" /></xsl:attribute>
						</xsl:when>
						<xsl:when
							test="$style-node/style:paragraph-properties/@style:line-height-at-least">
							<xsl:attribute name="line-height"><xsl:value-of
								select="$style-node/style:paragraph-properties/@style:line-height-at-least" /></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="line-height">110%</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>



					<xsl:if test="$style-node/style:paragraph-properties/@fo:break-before">
						<xsl:attribute name="break-before"><xsl:value-of
							select="$style-node/style:paragraph-properties/@fo:break-before" /></xsl:attribute>
					</xsl:if>

					<xsl:if
						test="$style-node/style:paragraph-properties/@fo:hyphenation-ladder-count">
						<xsl:attribute name="hyphenation-ladder-count"><xsl:value-of
							select="$style-node/style:paragraph-properties/@fo:hyphenation-ladder-count" /></xsl:attribute>
					</xsl:if>
				</xsl:if>

				<xsl:variable name="text-properties" select="$style-node/style:text-properties" />
				<xsl:if test="$text-properties">
					<xsl:if test="$text-properties/@fo:hyphenate">
						<xsl:attribute name="hyphenate"><xsl:value-of
							select="$text-properties/@fo:hyphenate" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:hyphenation-remain-char-count">
						<!--<xsl:attribute name="hyphenation-remain-char-count"><xsl:value-of 
							select="$text-properties/@fo:hyphenation-remain-char-count"/></xsl:attribute> -->
					</xsl:if>
					<xsl:if test="$text-properties/@fo:hyphenation-push-char-count">
						<!--<xsl:attribute name="hyphenation-push-char-count"><xsl:value-of 
							select="$text-properties/@fo:hyphenation-push-char-count"/></xsl:attribute> -->
					</xsl:if>

					<xsl:if test="$text-properties/@fo:language">
						<xsl:attribute name="language"><xsl:value-of
							select="$text-properties/@fo:language" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:country">
						<xsl:attribute name="country"><xsl:value-of
							select="$text-properties/@fo:country" /></xsl:attribute>
					</xsl:if>

					<xsl:if test="$text-properties/@fo:color">
						<xsl:attribute name="color"><xsl:value-of
							select="$text-properties/@fo:color" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:background-color">
						<xsl:attribute name="background-color"><xsl:value-of
							select="$text-properties/@fo:background-color" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:font-size">
						<xsl:attribute name="font-size"><xsl:value-of
							select="$text-properties/@fo:font-size" /></xsl:attribute>
					</xsl:if>

					<xsl:if test="$text-properties/@fo:font-style">
						<xsl:attribute name="font-style"><xsl:value-of
							select="$text-properties/@fo:font-style" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:font-weight">
						<xsl:attribute name="font-weight"><xsl:value-of
							select="$text-properties/@fo:font-weight" /></xsl:attribute>
					</xsl:if>
					<xsl:if test="$text-properties/@fo:text-align">
						<xsl:attribute name="text-align"><xsl:value-of
							select="$text-properties/@fo:text-align" /></xsl:attribute>
					</xsl:if>

					<xsl:variable name="font-name" select="$text-properties/@style:font-name" />
					<xsl:if test="$font-name">

						<xsl:variable name="font-face"
							select="key('findStyleFromFontFaceDeclsByName', $font-name)" />

						<xsl:if test="$font-face">
						
							<xsl:variable name="font-family"
								select="$font-face/@svg:font-family" />
							<xsl:variable name="font-family-generic"
								select="$font-face/@style:font-family-generic" />

							<xsl:choose>
								<xsl:when test="$font-family-generic = 'roman'">
									<xsl:attribute name="font-family"><xsl:value-of
										select="concat($font-family,',serif')" /></xsl:attribute>
								</xsl:when>
								<xsl:when
									test="$font-family-generic = 'sans-serif' or $font-family-generic = 'serif' or $font-family-generic = 'monospace' or $font-family-generic = 'cursive' or $font-family-generic = 'fantasy'">
									<xsl:attribute name="font-family"><xsl:value-of
										select="concat($font-family,',',$font-family-generic)" /></xsl:attribute>
								</xsl:when>
								<xsl:when
									test="contains($font-family,'Times') or contains($font-family,'Minion') or contains($font-family,'Garamond')">
									<xsl:attribute name="font-family"><xsl:value-of
										select="concat($font-family,',serif')" /></xsl:attribute>
								</xsl:when>
								<xsl:when test="contains($font-family,'Courier')">
									<xsl:attribute name="font-family"><xsl:value-of
										select="concat($font-family,',monospace')" /></xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="font-family"><xsl:value-of
										select="concat($font-family,',sans-serif')" /></xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</xsl:if>
					
					<xsl:if test="$text-properties/@style:text-underline='single'">
						<xsl:attribute name="text-decoration">underline</xsl:attribute>
					</xsl:if>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="interpetBoxStyle">
		<xsl:param name="style-node" />

		<xsl:if test="$style-node/@fo:margin">
			<xsl:attribute name="margin"><xsl:value-of
				select="$style-node/@fo:margin" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:margin-top">
			<!--<xsl:attribute name="margin-top"><xsl:value-of select="$style-node/@fo:margin-top"/></xsl:attribute> -->
			<xsl:attribute name="space-before"><xsl:value-of
				select="$style-node/@fo:margin-top" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:margin-bottom">
			<xsl:attribute name="space-after"><xsl:value-of
				select="$style-node/@fo:margin-bottom" /></xsl:attribute>
			<!--<xsl:attribute name="margin-bottom"><xsl:value-of select="$style-node/@fo:margin-bottom"/></xsl:attribute> -->
		</xsl:if>
		<xsl:if test="$style-node/@fo:margin-left">
			<xsl:attribute name="margin-left"><xsl:value-of
				select="$style-node/@fo:margin-left" /></xsl:attribute>
			<!--<xsl:attribute name="margin-left"><xsl:value-of select="$style-node/@fo:margin-left"/></xsl:attribute> -->
		</xsl:if>
		<xsl:if test="$style-node/@fo:margin-right">
			<xsl:attribute name="margin-right"><xsl:value-of
				select="$style-node/@fo:margin-right" /></xsl:attribute>
			<!--<xsl:attribute name="margin-right"><xsl:value-of select="$style-node/style:paragraph-properties/@fo:margin-right"/></xsl:attribute> -->
		</xsl:if>

		<xsl:if test="$style-node/@fo:padding">
			<xsl:attribute name="padding"><xsl:value-of
				select="$style-node/@fo:padding" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:padding-left">
			<xsl:attribute name="padding-left"><xsl:value-of
				select="$style-node/@fo:padding-left" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:padding-right">
			<xsl:attribute name="padding-right"><xsl:value-of
				select="$style-node/@fo:padding-right" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:padding-top">
			<xsl:attribute name="padding-top"><xsl:value-of
				select="$style-node/@fo:padding-top" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:padding-bottom">
			<xsl:attribute name="padding-bottom"><xsl:value-of
				select="$style-node/@fo:padding-bottom" /></xsl:attribute>
		</xsl:if>

		<xsl:if test="$style-node/@fo:border">
			<xsl:attribute name="border"><xsl:value-of
				select="$style-node/@fo:border" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:border-left">
			<xsl:attribute name="border-left"><xsl:value-of
				select="$style-node/@fo:border-left" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:border-right">
			<xsl:attribute name="border-right"><xsl:value-of
				select="$style-node/@fo:border-right" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:border-top">
			<xsl:attribute name="border-top"><xsl:value-of
				select="$style-node/@fo:border-top" /></xsl:attribute>
		</xsl:if>
		<xsl:if test="$style-node/@fo:border-bottom">
			<xsl:attribute name="border-bottom"><xsl:value-of
				select="$style-node/@fo:border-bottom" /></xsl:attribute>
		</xsl:if>

		<xsl:if test="$style-node/@fo:background-color">
			<xsl:attribute name="background-color"><xsl:value-of
				select="$style-node/@fo:background-color" /></xsl:attribute>
		</xsl:if>

		<xsl:if test="$style-node/@style:vertical-align">
			<xsl:attribute name="display-align">end</xsl:attribute>
		</xsl:if>

		<xsl:if test="$style-node/@style:width">
			<xsl:attribute name="width"><xsl:value-of
				select="$style-node/@style:width" /></xsl:attribute>
		</xsl:if>

	</xsl:template>
</xsl:stylesheet>
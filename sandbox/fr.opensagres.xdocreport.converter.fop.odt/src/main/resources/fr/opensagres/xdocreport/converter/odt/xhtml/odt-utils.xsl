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
<!--  are made available under the terms of the Eclipse Public License v1.0            -->
<!--  which accompanies this distribution, and is available at                         -->
<!--  http://www.eclipse.org/legal/epl-v10.html                                        -->
<!--                                                                                   -->
<!--  Contributors:                                                                    -->
<!--      Angelo Zerr <angelo.zerr@gmail.com> - Initial API and implementation         -->
<!--      Pascal Leclercq <pascal.leclercq@gmail.com> - Initial API and implementation -->
<!-- ================================================================================= -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
	xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
	xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
	xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
	xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0" xmlns:math="http://www.w3.org/1998/Math/MathML"
	xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
	xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
	xmlns:ooo="http://openoffice.org/2004/office" xmlns:ooow="http://openoffice.org/2004/writer"
	xmlns:oooc="http://openoffice.org/2004/calc" xmlns:dom="http://www.w3.org/2001/xml-events"
	xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rpt="http://openoffice.org/2005/report"
	xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2" xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:grddl="http://www.w3.org/2003/g/data-view#" xmlns:tableooo="http://openoffice.org/2009/table"
	xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
	xmlns:formx="urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0"
	xmlns:css3t="http://www.w3.org/TR/css3-text/" office:version="1.2"
	grddl:transformation="http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl"
	exclude-result-prefixes="" version="2.0">

	<!-- ====================================== -->
	<!-- XML DOM Document styles.xml + meta.xml -->
	<!-- ====================================== -->
	<xsl:variable name="styles-xml-doc" select="document('styles.xml')" />
	<xsl:variable name="meta-xml-doc" select="document('meta.xml')" />

	<!-- ================================================== -->
	<!-- KEYS: to improve performance while style searching -->
	<!-- ================================================== -->
	<xsl:key name="findStylePageLayoutByName" match="//office:automatic-styles/style:page-layout"
		use="@style:name" />
	<xsl:key name="findStyleFromAutomaticStylesByName" match="//office:automatic-styles/style:style"
		use="@style:name" />
	<xsl:key name="findStyleFromOfficeStylesByName" match="//office:styles/style:style"
		use="@style:name" />
	<xsl:key name="findDefaultStyleFromOfficeStylesByFamily" match="//office:styles/style:default-style"
		use="@style:family" />
	<xsl:key name="findStyleFromFontFaceDeclsByName" match="//office:font-face-decls/style:font-face"
		use="@style:name" />

	<!-- Generic Template Apply style search style-node declared in the content.xml 
		or styles.xml and call InternalInterpretStyle template which is a template 
		which must be implemented to generate content (FO, XHTML...) -->
	<xsl:template name="ApplyStyle">
		<xsl:param name="style-node" />
		<xsl:param name="style-name" />
		<xsl:param name="style-type" />
		<xsl:param name="style-position" />

		<xsl:choose>

			<!-- Case 1) style-node is filled -->
			<xsl:when test="$style-node">
				<!-- Style node found, interpret style -->
				<xsl:call-template name="InternalInterpretStyle">
					<xsl:with-param name="style-node" select="$style-node" />
					<xsl:with-param name="style-type" select="$style-type" />
					<xsl:with-param name="style-position" select="$style-position" />
				</xsl:call-template>
			</xsl:when>

			<!-- Case 2) style-name is filled -->
			<xsl:when test="$style-name">
				<!-- Search the style node by name declared in the content.xml//office:automatic-styles 
					OR styles.xml//office:styles -->

				<!-- 1) Search styles from content.xml//office:automatic-styles -->
				<xsl:variable name="style-node-from-contentXML"
					select="key('findStyleFromAutomaticStylesByName', $style-name)" />

				<xsl:choose>
					<xsl:when test="$style-node-from-contentXML">
						<!-- Style node found, interpret style -->
						<xsl:call-template name="InternalInterpretStyle">
							<xsl:with-param name="style-node" select="$style-node-from-contentXML" />
							<xsl:with-param name="style-type" select="$style-type" />
							<xsl:with-param name="style-position" select="$style-position" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>

						<!-- 2) Search styles from styles.xml//office:styles -->
						<xsl:for-each select="$styles-xml-doc">
							<xsl:call-template name="InternalInterpretStyle">
								<xsl:with-param name="style-node"
									select="key('findStyleFromOfficeStylesByName', $style-name)" />
								<xsl:with-param name="style-type" select="$style-type" />
								<xsl:with-param name="style-position" select="$style-position" />
							</xsl:call-template>
						</xsl:for-each>

					</xsl:otherwise>
				</xsl:choose>

			</xsl:when>

		</xsl:choose>

	</xsl:template>

	<xsl:template name="InternalInterpretStyle">
		<xsl:param name="style-node" />
		<xsl:param name="style-type" />
		<xsl:param name="style-position" />

		<xsl:if test="$style-node/@style:name">

			<!-- 1) Interpret Next Styles -->
			<xsl:variable name="next-style-name" select="$style-node/@style:next-style-name" />
			<xsl:if test="$next-style-name">
				<xsl:for-each select="$styles-xml-doc">
					<xsl:call-template name="InternalInterpretStyle">
						<xsl:with-param name="style-node"
							select="key('findStyleFromOfficeStylesByName', $next-style-name)" />
						<xsl:with-param name="style-type" select="$style-type" />
						<xsl:with-param name="style-position" select="$style-position" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>

			<!-- 2) Interpret Default Styles -->
			<xsl:variable name="family" select="$style-node/@style:family" />
			<xsl:if test="$family">
				<xsl:for-each select="$styles-xml-doc">
					<xsl:call-template name="InternalInterpretStyle">
						<xsl:with-param name="style-node"
							select="key('findDefaultStyleFromOfficeStylesByFamily', $family)" />
						<xsl:with-param name="style-type" select="$style-type" />
						<xsl:with-param name="style-position" select="$style-position" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>

			<!-- 3) Interpret Parent Styles -->
			<xsl:variable name="parent-style-name"
				select="$style-node/@style:parent-style-name" />
			<xsl:if test="$parent-style-name">
				<xsl:for-each select="$styles-xml-doc">
					<xsl:call-template name="InternalInterpretStyle">
						<xsl:with-param name="style-node"
							select="key('findStyleFromOfficeStylesByName', $parent-style-name)" />
						<xsl:with-param name="style-type" select="$style-type" />
						<xsl:with-param name="style-position" select="$style-position" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
		</xsl:if>

		<!-- Call InterpretStyle template to implement switch FO, XHTML.. generation -->
		<xsl:call-template name="InterpretStyle">
			<xsl:with-param name="style-node" select="$style-node" />
			<xsl:with-param name="style-type" select="$style-type" />
			<xsl:with-param name="style-position" select="$style-position" />
		</xsl:call-template>

	</xsl:template>

	<!-- Returns text style name of the given node-set. If not founded, it search 
		to the parent node -->
	<xsl:template name="GetParentOrSelfTextStyleName">
		<xsl:param name="node-set" />
		<xsl:choose>
			<xsl:when test="$node-set/@text:style-name">
				<xsl:value-of select="$node-set/@text:style-name" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="GetParentOrSelfTextStyleName">
					<xsl:with-param name="node-set" select="parent::*" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>	
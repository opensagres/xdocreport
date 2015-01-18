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

	<xsl:output method="xml" omit-xml-declaration="yes" version="1.0"
		indent="no" encoding="UTF-8" />

	<xsl:variable name="styles-doc" select="document('word/styles.xml')"/>

	<!-- =================== -->
	<!-- INCLUDES XSL -->
	<!-- =================== -->	
	<xsl:include href="GenerateElementProperties.xsl" />
	<xsl:include href="GenerateElementStructures.xsl" />
	<xsl:include href="DocxUtils.xsl" />
	<xsl:include href="DefaultVariables.xsl" />
	<xsl:include href="GenerateFOLayoutMasterSet.xsl" />
	<xsl:include href="GenerateFOPageSequences.xsl" />

	<!-- =================== -->
	<!-- MAIN ROOT TEMPLATE -->
	<!-- =================== -->
	<xsl:template match="/w:document">
		<fo:root>

<xsl:value-of select="$styles-doc" />

			<!-- Generate <fo:layout-master-set -->
			<xsl:call-template name="GenerateFOLayoutMasterSet" />
			<!-- Generate <fo:page-sequence -->
			<xsl:call-template name="GenerateFOPageSequences" />

		</fo:root>
	</xsl:template>

</xsl:stylesheet>                
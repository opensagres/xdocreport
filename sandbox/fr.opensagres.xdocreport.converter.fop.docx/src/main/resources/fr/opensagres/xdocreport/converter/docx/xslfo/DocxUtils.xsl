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
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- ======================= -->
	<!-- Set page number format. -->
	<!-- ======================= -->
	<xsl:template name="PageNumberFormat">
		<xsl:param name="format" />

		<xsl:choose>
			<xsl:when test="$format='Arabic'">
				<xsl:text>1</xsl:text>
			</xsl:when>
			<xsl:when test="$format='ArabicDash'">
				<xsl:text>- 1 -</xsl:text>
			</xsl:when>
			<xsl:when test="$format='alphabetic'">
				<xsl:text>a</xsl:text>
			</xsl:when>
			<xsl:when test="$format='ALPHABETIC'">
				<xsl:text>A</xsl:text>
			</xsl:when>
			<xsl:when test="$format='roman'">
				<xsl:text>i</xsl:text>
			</xsl:when>
			<xsl:when test="$format='ROMAN'">
				<xsl:text>I</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>1</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
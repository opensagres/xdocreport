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
	xmlns:fop="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="office style fo"
	version="2.0">

	<!-- ==================================== -->
	<!-- Generate FO page-sequence -->
	<!-- ==================================== -->
	<xsl:template name="GenerateFOPageSequences">
		<!-- FIXME : how generate page-sequence by using list of style:master-page??? -->
		<xsl:variable name="master-page-node"
			select="$styles-xml-doc//office:master-styles/style:master-page[1]" />
		<xsl:if test="$master-page-node">
			<xsl:variable name="master-name" select="$master-page-node/@style:name" />
			<fop:page-sequence master-reference="{$master-name}">

				<!-- Generate header -->
				<xsl:if test="$master-page-node/style:header">
					<fop:static-content flow-name="xsl-region-before">
						<xsl:apply-templates select="$master-page-node/style:header/child::*" />
					</fop:static-content>
				</xsl:if>

				<!-- Generate footer -->
				<xsl:if test="$master-page-node/style:footer">
					<fop:static-content flow-name="xsl-region-after">
						<xsl:apply-templates select="$master-page-node/style:footer/child::*" />
					</fop:static-content>
				</xsl:if>

				<!-- <fop:static-content flow-name="xsl-footnote-separator"> -->
				<!-- <fop:block> -->
				<!-- <fop:leader leader-pattern="rule" leader-length="100%" -->
				<!-- rule-style="solid" rule-thickness="0.5pt" /> -->
				<!-- </fop:block> -->
				<!-- </fop:static-content> -->

				<!-- Generate body -->
				<fop:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="//office:body/office:text/*" />
					<fop:block id="last-page" />
				</fop:flow>

			</fop:page-sequence>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>	
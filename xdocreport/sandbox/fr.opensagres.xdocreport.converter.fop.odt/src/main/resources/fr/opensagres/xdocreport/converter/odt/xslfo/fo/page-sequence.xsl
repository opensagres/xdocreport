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
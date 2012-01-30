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
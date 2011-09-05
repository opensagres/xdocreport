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
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
	xmlns:fop="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="office style fo text table"
	version="2.0">

	<xsl:output method="xml" omit-xml-declaration="yes" version="1.0"
		indent="no" encoding="UTF-8" />

	<!-- =================== -->
	<!-- INCLUDES XSL -->
	<!-- =================== -->
	<xsl:include href="odt-utils.xsl" />
	<xsl:include href="fo/styles.xsl" />
	<xsl:include href="fo/layout-master-set.xsl" />
	<xsl:include href="fo/page-sequence.xsl" />
	<xsl:include href="fo/draw.xsl" />
	<xsl:include href="fo/text.xsl" />
	<xsl:include href="fo/list.xsl" />
	<xsl:include href="fo/table.xsl" />

	<!-- =================== -->
	<!-- MAIN ROOT TEMPLATE -->
	<!-- =================== -->
	<xsl:template match="/office:document-content">
		<fop:root>
			<!-- Generate <fop:layout-master-set -->
			<xsl:call-template name="GenerateFOLayoutMasterSet" />
			<!-- Generate <fop:page-sequence -->
			<xsl:call-template name="GenerateFOPageSequences" />
		</fop:root>
	</xsl:template>

</xsl:stylesheet>                

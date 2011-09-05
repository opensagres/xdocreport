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

	<!-- =================== -->
	<!-- MAIN ROOT TEMPLATE -->
	<!-- =================== -->
	<xsl:template match="/w:document">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<title>TODO</title>
				<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
				<!--  <xsl:apply-templates select="office:automatic-styles" />-->
			</head>
			<body>
				<xsl:apply-templates select="w:body/*" />
			</body>
		</html>

	</xsl:template>

</xsl:stylesheet>                
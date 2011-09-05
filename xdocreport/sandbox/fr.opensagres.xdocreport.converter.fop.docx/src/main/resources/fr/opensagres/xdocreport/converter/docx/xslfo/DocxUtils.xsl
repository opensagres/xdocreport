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
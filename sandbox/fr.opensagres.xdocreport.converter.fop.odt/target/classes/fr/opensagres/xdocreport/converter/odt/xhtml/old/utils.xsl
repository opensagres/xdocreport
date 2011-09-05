<?xml version="1.0" encoding="utf-8"?>
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
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	xmlns:presentation="urn:oasis:names:tc:opendocument:xmlns:presentation:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	exclude-result-prefixes="office text table draw presentation style"
	version="2.0">

	<!-- This template is part of the XSL DocBook Stylesheet distribution. See 
		http://nwalsh.com/docbook/xsl/ for copyright and other information. -->

	<xsl:template name="length-magnitude">
		<xsl:param name="length" select="'0pt'" />
		<xsl:choose>
			<xsl:when test="string-length($length) = 0" />
			<xsl:when
				test="
					substring($length,1,1) = '-' or
					substring($length,1,1) = '0' or
					substring($length,1,1) = '1' or
					substring($length,1,1) = '2' or
					substring($length,1,1) = '3' or
					substring($length,1,1) = '4' or
					substring($length,1,1) = '5' or
					substring($length,1,1) = '6' or
					substring($length,1,1) = '7' or
					substring($length,1,1) = '8' or
					substring($length,1,1) = '9' or
					substring($length,1,1) = '.'">
				<xsl:value-of select="substring($length,1,1)" />
				<xsl:call-template name="length-magnitude">
					<xsl:with-param name="length" select="substring($length,2)" />
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- This template is a rewrited part of the XSL DocBook Stylesheet distribution. 
		See http://nwalsh.com/docbook/xsl/ for copyright and other information. -->

	<xsl:template name="length-normalize">
		<xsl:param name="length" select="'0pt'" />
		<xsl:param name="pixels.per.inch" select="72" />
		<xsl:param name="unit" select="'pt'" />

		<xsl:variable name="magnitude">
			<xsl:call-template name="length-magnitude">
				<xsl:with-param name="length" select="$length" />
			</xsl:call-template>
		</xsl:variable>

		<xsl:variable name="units">
			<xsl:value-of select="substring($length, string-length($magnitude)+1)" />
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$units = '' and $magnitude=''">
				<xsl:value-of select="0" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = ''">
				<xsl:value-of select="$magnitude" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = 'em'">
				<xsl:value-of select="$magnitude * 12 * $scale" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = '%'">
				<xsl:value-of select="$magnitude * $scale" />
				<xsl:text>%</xsl:text>
			</xsl:when>
			<xsl:when test="$units = 'px'">
				<xsl:value-of select="$magnitude div $pixels.per.inch * 72.0 * $scale" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = 'pt'">
				<xsl:value-of select="$magnitude * $scale" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = 'cm'">
				<xsl:value-of select="$magnitude * 28.45 * $scale" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<xsl:when test="$units = 'mm'">
				<xsl:value-of select="$magnitude * 28.45 * $scale * 10" />
				<xsl:value-of select="$unit" />
			</xsl:when>
			<!-- <xsl:when test="$units = 'mm'"> <xsl:value-of select="$magnitude 
				div 25.4 * 72.0"/> </xsl:when> <xsl:when test="$units = 'in'"> <xsl:value-of 
				select="$magnitude * 72.0"/> </xsl:when> <xsl:when test="$units = 'pc'"> 
				<xsl:value-of select="$magnitude * 12.0"/> </xsl:when> <xsl:when test="$units 
				= 'px'"> <xsl:value-of select="$magnitude div $pixels.per.inch * 72.0"/> 
				</xsl:when> -->
			<xsl:otherwise>
				<xsl:message>
					<xsl:text>Unrecognized unit of measure: </xsl:text>
					<xsl:value-of select="$units" />
					<xsl:text>.</xsl:text>
				</xsl:message>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

</xsl:stylesheet>
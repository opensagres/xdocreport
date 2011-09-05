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
	xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
	version="1.0">

	<xsl:variable name="default-header-extent" select="'11in'" />
	<xsl:variable name="default-footer-extent" select="'11in'" />
	<xsl:variable name="default-widows" select="2" />
	<xsl:variable name="default-orphans" select="2" />
	<xsl:variable name="default-font-size" select="10" />
	<xsl:variable name="default-line-height" select="1.147" />
	<xsl:variable name="white-space-collapse" select="'false'" />

</xsl:stylesheet>
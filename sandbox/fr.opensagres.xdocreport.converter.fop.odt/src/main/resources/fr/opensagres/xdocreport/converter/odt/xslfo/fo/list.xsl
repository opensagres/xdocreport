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
	xmlns:fop="http://www.w3.org/1999/XSL/Format" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:xalan="http://xml.apache.org/xalan" exclude-result-prefixes="office style fo text xalan"
	version="2.0">

	<!-- ================== -->
	<!-- Template List -->
	<!-- ================== -->
	<xsl:template match="text:list">

		<xsl:variable name="list-style-name"
			select="ancestor-or-self::text:list[@text:style-name][1]/@text:style-name" />
		<xsl:variable name="end-level" select="count(ancestor-or-self::text:list)" />

		<!-- Get nodes list of /text:list-level-style-number OR /text:list-level-style-bullet 
			OR /text:list-level-style-image -->

		<xsl:variable name="list-style-node"
			select="//office:automatic-styles/text:list-style[@style:name=$list-style-name]|
			$styles-xml-doc//office:styles/text:list-style[@style:name=$list-style-name]" />

		<xsl:variable name="last-level-node"
			select="$list-style-node/text:list-level-style-number[@text:level=$end-level]|
					$list-style-node/text:list-level-style-bullet[@text:level=$end-level]|
					$list-style-node/text:list-level-style-image[@text:level=$end-level]" />

		<fop:list-block>
			<!-- Generate @start-indent attribute by using ODT @text:space-before -->
			<xsl:variable name="space-before"
				select="$last-level-node[@text:level=$end-level]//style:list-level-label-alignment/@text:list-tab-stop-position" />
			<xsl:choose>
				<xsl:when test="$space-before">
					<xsl:attribute name="provisional-distance-between-starts"><xsl:value-of
						select="$space-before" /></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="start-indent">0cm</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>

			<!-- Generate <fop:list-item -->
			<xsl:apply-templates select="./text:list-item">
				<xsl:with-param name="list-style-node" select="$list-style-node" />
				<xsl:with-param name="end-level" select="$end-level" />
			</xsl:apply-templates>

		</fop:list-block>

	</xsl:template>

	<!-- ================== -->
	<!-- Template List Item -->
	<!-- ================== -->
	<xsl:template match="text:list-item">
		<xsl:param name="list-style-node" />
		<xsl:param name="end-level" />

		<xsl:variable name="last-level-node"
			select="$list-style-node/text:list-level-style-number[@text:level=$end-level]|
					$list-style-node/text:list-level-style-bullet[@text:level=$end-level]|
					$list-style-node/text:list-level-style-image[@text:level=$end-level]" />

		<fop:list-item>

			<fop:list-item-label end-indent="label-end()">
				<xsl:variable name="list-level-label-alignment-node"
					select="$last-level-node/style:list-level-properties" />
				<fop:block>
					<xsl:apply-templates
						select="xalan:nodeset($list-level-label-alignment-node)/*" mode="apply-styles" />

					<!-- Generate label for text:list-level-style-number|text:list-level-style-bullet|text:list-level-style-image -->
					<xsl:choose>
						<xsl:when
							test="name($last-level-node) = 'text:list-level-style-bullet'">
							<!-- Display bullet char -->
							<xsl:value-of select="$last-level-node/@text:bullet-char" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when
									test="name($last-level-node) = 'text:list-level-style-number'">
									<!-- Display number -->
									<xsl:value-of select="$last-level-node/@style:num-prefix" />
									<xsl:choose>
										<xsl:when test="$last-level-node/@text:display-levels!=''">
											<xsl:number format="{$last-level-node/@style:num-format}"
												level="multiple" count="text:list-item" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:number format="{$last-level-node/@style:num-format}"
												level="single" count="text:list-item" />
										</xsl:otherwise>
									</xsl:choose>
									<xsl:value-of select="$last-level-node/@style:num-suffix" />
								</xsl:when>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>

				</fop:block>

			</fop:list-item-label>

			<fop:list-item-body>
				<!-- Generate @start-indent -->
<!--				<xsl:choose>-->
<!--					<xsl:when test="$last-level-node/@text:display-levels!=''">-->
<!--						<xsl:attribute name="start-indent">body-start()-0.4cm+<xsl:value-of-->
<!--							select="$end-level*0.2" />cm</xsl:attribute>-->
<!--					</xsl:when>-->
<!--					<xsl:otherwise>-->
						<xsl:attribute name="start-indent">body-start()</xsl:attribute>
<!--					</xsl:otherwise>-->
<!--				</xsl:choose>-->

				<xsl:apply-templates />

			</fop:list-item-body>

		</fop:list-item>

	</xsl:template>

</xsl:stylesheet>	
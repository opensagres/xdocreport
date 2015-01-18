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
<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
	version="1.0">

	<!-- ================================================ -->
	<!-- Specifies the size and orientation of this page. -->
	<!-- Parent elements: w:sectPr -->
	<!-- ================================================ -->
	<xsl:template match="w:pgSz">
		<xsl:attribute name="page-width">
          <xsl:value-of select="@w:w div 1440" /><xsl:text>in</xsl:text>
        </xsl:attribute>
		<xsl:attribute name="page-height">
          <xsl:value-of select="@w:h div 1440" /><xsl:text>in</xsl:text>
        </xsl:attribute>
	</xsl:template>

	<!-- ================== -->
	<!-- Section properties -->
	<!-- ================== -->
	<!-- =========================== -->
	<!-- Specifies the page margins. -->
	<!-- Parent elements: w:sectPr -->
	<!-- =========================== -->
	<xsl:template match="w:pgMar">
		<xsl:param name="area" select="'region-body'" />

		<xsl:variable name="border-right" select="../w:pgBorders/w:right" />
		<xsl:variable name="border-left" select="../w:pgBorders/w:left" />
		<xsl:variable name="border-top" select="../w:pgBorders/w:top" />
		<xsl:variable name="border-bottom" select="../w:pgBorders/w:bottom" />
		<xsl:choose>
			<!-- set margins on simple-page-master -->
			<xsl:when test="$area='page-master'">
				<!-- @w:right, @w:left, @w:header, @w:bottom define page master margins -->
				<xsl:if test="@w:header">
					<xsl:attribute name="margin-top">
                <xsl:value-of select="@w:header div 20" /><xsl:text>pt</xsl:text>
              </xsl:attribute>
				</xsl:if>
				<xsl:if test="@w:footer">
					<xsl:attribute name="margin-bottom">
                <xsl:value-of select="@w:footer div 20" /><xsl:text>pt</xsl:text>
              </xsl:attribute>
				</xsl:if>
				<xsl:if test="@w:right">
					<xsl:attribute name="margin-right">
                <xsl:choose>
                  <!-- if page has borders - the right margin is the distance -->
                  <!-- between page edge and the right border.                -->
                  <xsl:when test="$border-right">
                    <xsl:choose>
                      <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                        <xsl:value-of select="$border-right/@w:space" />
                        <xsl:text>pt</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of
						select="(@w:right div 20) - ($border-right/@w:sz div 8) - ($border-right/@w:space)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="@w:right div 20" /><xsl:text>pt</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
				</xsl:if>
				<xsl:if test="@w:left">
					<xsl:attribute name="margin-left">
                <xsl:choose>
                  <!-- if page has borders - the left margin is the distance -->
                  <!-- between page edge and the left border.                -->
                  <xsl:when test="$border-left">
                    <xsl:choose>
                      <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                        <xsl:value-of select="$border-left/@w:space" />
                        <xsl:text>pt</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of
						select="(@w:left div 20) - ($border-left/@w:sz div 8) - ($border-left/@w:space)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="@w:left div 20" /><xsl:text>pt</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
				</xsl:if>
			</xsl:when>
			<!-- set margins and paddings on region-body -->
			<xsl:when test="$area='region-body'">
				<xsl:choose>
					<!-- if page has borders - the body would have paddings -->
					<xsl:when test="../w:pgBorders">
						<xsl:if test="$border-top">
							<xsl:choose>
								<xsl:when test="../w:pgBorders/@w:offset-from='page'">
									<xsl:attribute name="margin-top">
                        <xsl:value-of
										select="$border-top/@w:space - (@w:header div 20)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
									<xsl:attribute name="padding-top">
                        <xsl:value-of
										select="(@w:top div 20) - $border-top/@w:space - ($border-top/@w:sz div 8)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="margin-top">
                        <xsl:value-of
										select="(@w:top - @w:header) div 20 - $border-top/@w:space - ($border-top/@w:sz div 8)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
									<xsl:attribute name="padding-top">
                        <xsl:value-of select="$border-top/@w:space" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<xsl:if test="$border-bottom">
							<xsl:choose>
								<xsl:when test="../w:pgBorders/@w:offset-from='page'">
									<xsl:attribute name="margin-bottom">
                        <xsl:value-of
										select="$border-bottom/@w:space - (@w:footer div 20)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
									<xsl:attribute name="padding-bottom">
                        <xsl:value-of
										select="(@w:bottom div 20) - $border-bottom/@w:space - ($border-bottom/@w:sz div 8)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="margin-bottom">
                        <xsl:value-of
										select="(@w:bottom - @w:footer) div 20 - $border-bottom/@w:space - ($border-bottom/@w:sz div 8)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
									<xsl:attribute name="padding-bottom">
                        <xsl:value-of select="$border-bottom/@w:space" />
                        <xsl:text>pt</xsl:text>
                      </xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<xsl:if test="$border-left">
							<xsl:attribute name="padding-left">
                    <xsl:choose>
                      <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                        <xsl:value-of
								select="(@w:left div 20) - ($border-left/@w:sz div 8) - ($border-left/@w:space)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="$border-left/@w:space" /><xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
						</xsl:if>
						<xsl:if test="$border-right">
							<xsl:attribute name="padding-right">
                    <xsl:choose>
                      <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                        <xsl:value-of
								select="(@w:right div 20) - ($border-right/@w:sz div 8) - ($border-right/@w:space)" />
                        <xsl:text>pt</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="$border-right/@w:space" /><xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
						</xsl:if>
					</xsl:when>
					<!-- @w:top and @w:bottom defines body margins -->
					<xsl:otherwise>
						<xsl:if test="@w:top">
							<xsl:attribute name="margin-top">
                    <xsl:choose>
                      <xsl:when test="@w:header">
                        <xsl:if test="count(../w:hdr/w:p) > 2">
                          <xsl:if test="@w:top > @w:header">
                            <xsl:variable name="diff"
								select="@w:top - @w:header" />
                            <xsl:value-of
								select="(@w:top + ($diff div 2)) div 20" /><xsl:text>pt</xsl:text>
                            <!-- xsl:value-of select="@w:top div 20"/><xsl:text>pt</xsl:text -->
                          </xsl:if>
                          <xsl:if test="@w:header > @w:top">
                            <xsl:variable name="diff"
								select="@w:header - @w:top" />
                            <xsl:value-of
								select="(@w:header + ($diff div 2)) div 20" /><xsl:text>pt</xsl:text>
                            <!-- xsl:value-of select="@w:header div 20"/><xsl:text>pt</xsl:text -->
                          </xsl:if>
                        </xsl:if>
                        <xsl:if test="not(count(../w:hdr/w:p) > 2)">
                          <xsl:value-of select="(@w:top - @w:header) div 20" /><xsl:text>pt</xsl:text>
                        </xsl:if>
                        <!-- xsl:value-of select="(@w:top - @w:header) div 20"/><xsl:text>pt</xsl:text -->
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="@w:top div 20" /><xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
						</xsl:if>
						<xsl:if test="@w:bottom">
							<xsl:attribute name="margin-bottom">
                    <xsl:choose>
                      <xsl:when test="@w:footer">
                        <xsl:value-of select="(@w:bottom - @w:footer) div 20" /><xsl:text>pt</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:value-of select="@w:bottom div 20" /><xsl:text>pt</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- set paddings on region-before/region-after -->
			<xsl:when test="$area='static-content'">
				<xsl:if test="../w:pgBorders">
					<xsl:if test="$border-left">
						<xsl:attribute name="padding-left">
                  <xsl:choose>
                    <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                      <xsl:value-of
							select="(@w:left div 20) - ($border-left/@w:sz div 8) - ($border-left/@w:space)" />
                      <xsl:text>pt</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="$border-left/@w:space" /><xsl:text>pt</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
					</xsl:if>
					<xsl:if test="$border-right">
						<xsl:attribute name="padding-right">
                  <xsl:choose>
                    <xsl:when test="../w:pgBorders/@w:offset-from='page'">
                      <xsl:value-of
							select="(@w:right div 20) - ($border-right/@w:sz div 8) - ($border-right/@w:space)" />
                      <xsl:text>pt</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="$border-right/@w:space" /><xsl:text>pt</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>

	<!-- ======================================= -->
	<!-- These attributes have different default -->
	<!-- values in XSL-FO and MS Word 2003. -->
	<!-- ======================================= -->
	<xsl:template name="SetDefaultAttributes">
		<xsl:attribute name="widows"><xsl:value-of select="$default-widows" /></xsl:attribute>
		<xsl:attribute name="orphans"><xsl:value-of select="$default-orphans" /></xsl:attribute>
		<xsl:attribute name="font-size">
          <xsl:value-of select="$default-font-size" />
          <xsl:text>pt</xsl:text>
        </xsl:attribute>
		<xsl:attribute name="line-height"><xsl:value-of select="$default-line-height" /></xsl:attribute>
		<xsl:attribute name="white-space-collapse"><xsl:value-of
			select="$white-space-collapse" /></xsl:attribute>
	</xsl:template>

</xsl:stylesheet>

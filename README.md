XDocReport
==========


[![Maven Central](https://img.shields.io/maven-central/v/fr.opensagres.xdocreport/xdocreport.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22fr.opensagres.xdocreport%22%20AND%20a%3A%22xdocreport%22)
[![Build Status](https://secure.travis-ci.org/opensagres/xdocreport.png)](http://travis-ci.org/opensagres/xdocreport)

XDocReport means XML Document reporting. It's Java API to merge XML document created with MS Office (docx) or OpenOffice (odt), LibreOffice (odt) with a Java model to generate report and convert it if you need to another format (PDF, XHTML...). 

Please read [Getting Started](https://github.com/opensagres/xdocreport/wiki/GettingStarted)

# Samples & Demo

You can test our online demo : 

 * http://mytomcatapp-opensagres.rhcloud.com/demo-webapp/ a Web Application which uses XDocReport to generate report.
 * http://mytomcatapp-opensagres.rhcloud.com/rest-service-reporting/ a Web Application which uses REST Service to generate report.
 * http://mytomcatapp-opensagres.rhcloud.com/rest-service-converter/ a Web Application which uses REST Service to convert docx, odt to pdf, xhtml.
 
You can find source code of thoses demo at https://github.com/opensagres/xdocreport.samples

# License

XDocReport code is license under *MIT license* but the samples are licensed under *LGPL license*, this is why.

## XDocReport Core

Because we love to share our code, we prefer a very weak license. That's why we choose *MIT License* for our core modules.

MIT License is called a weak license which allow redistribution and modification (Although we appreciate contributions).

## Issue with iText based PDF converter

iText is a very powerful Java library for creating PDF. Our iText based converter is by far the best PDF converter that we provide. iText 2.1.7 comes with a *LGPL license* which is not compliant with our *MIT License*.

Thus, people that would use and redistribute xdocreport with iText based converter will have to comply with the *LGPL license*. This is exactly what we do with our [XDocReport Samples](https://github.com/opensagres/xdocreport.samples).

Which are licensed under *LGPL license* and not the *MIT License*.

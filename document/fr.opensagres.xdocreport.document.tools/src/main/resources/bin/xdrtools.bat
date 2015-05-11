@REM
@REM Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
@REM
@REM All rights reserved.
@REM
@REM Permission is hereby granted, free  of charge, to any person obtaining
@REM a  copy  of this  software  and  associated  documentation files  (the
@REM "Software"), to  deal in  the Software without  restriction, including
@REM without limitation  the rights to  use, copy, modify,  merge, publish,
@REM distribute,  sublicense, and/or sell  copies of  the Software,  and to
@REM permit persons to whom the Software  is furnished to do so, subject to
@REM the following conditions:
@REM
@REM The  above  copyright  notice  and  this permission  notice  shall  be
@REM included in all copies or substantial portions of the Software.
@REM
@REM THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
@REM EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
@REM MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
@REM NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
@REM LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
@REM OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
@REM WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
@REM

@echo on

set IN=%1
if %IN% == "" goto error
set OUT=%2
if %OUT% == "" goto error
set METADATA_FILE=%3
if %METADATA_FILE% == "" goto error
set DATA_DIR=%4
if %DATA_DIR% == "" goto error
set ERR=%5
if %ERR% == "" goto error

set BASE_DIR=%~dp0/../lib
set XDOCREPORT_VERSION=${project.version}
set CLASSPATH=%BASE_DIR%/fr.opensagres.xdocreport.core-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.converter-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template.freemarker-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/freemarker-2.3.20.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template.velocity-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/velocity-1.7.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/commons-collections-3.2.1.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/commons-lang-2.4.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/oro-2.0.8.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document.docx-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document.odt-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document.tools-%XDOCREPORT_VERSION%.jar

java -classpath "%CLASSPATH%" fr.opensagres.xdocreport.document.tools.Main -in %IN% -out %OUT% -dataDir %DATA_DIR% -metadataFile %METADATA_FILE% -err %ERR%

goto end

:error
echo "Impossible to launch XDocReport Tools."
:end

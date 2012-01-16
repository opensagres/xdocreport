@echo on

set IN=%1
if %IN% == "" goto error
set OUT=%2
if %OUT% == "" goto error
set ENGINE=%3
if %ENGINE% == "" goto error
set METADATA_FILE=%4
if %METADATA_FILE% == "" goto error
set JSON_FILE=%5
if %JSON_FILE% == "" goto error

set BASE_DIR=%~dp0/lib
set XDOCREPORT_VERSION=${project.version}
set CLASSPATH=%BASE_DIR%/fr.opensagres.xdocreport.core-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.converter-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template.freemarker-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/freemarker-2.3.18.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.template.velocity-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document.docx-%XDOCREPORT_VERSION%.jar
set CLASSPATH=%CLASSPATH%;%BASE_DIR%/fr.opensagres.xdocreport.document.tools-%XDOCREPORT_VERSION%.jar

java -classpath "%CLASSPATH%" fr.opensagres.xdocreport.document.tools.Main -in %IN% -out %OUT% -engine %ENGINE% -jsonFile %JSON_FILE% -metadataFile %METADATA_FILE% -autoGenData true

goto end

:error
echo "Impossible to launch XDocReport Tools."
:end
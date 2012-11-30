@REM
@REM Copyright (C) 2011-2012 The XDocReport Team <xdocreport@googlegroups.com>
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

set BASE_ADDRESS=%1
set USER=%2
set PASSWORD=%3
set SERVICE_TYPE=%4
set SERVICE_NAME=%5
set OUT=%6
set ERR=%7
set RESOURCES=%8
set CHUNK=%9
set TIMEOUT=%10

setlocal EnableDelayedExpansion
set CLASSPATH="
for /R %~dp0/../lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

java -classpath "%CLASSPATH%" fr.opensagres.xdocreport.document.tools.remoting.resources.Main -baseAddress %BASE_ADDRESS% -user %USER% -password %PASSWORD% -serviceType %SERVICE_TYPE% -serviceName %SERVICE_NAME% -out %OUT% -err %ERR% -resources %RESOURCES% -chunk %CHUNK% -timeout %TIMEOUT%

goto end

:error
echo "Impossible to launch XDocReport-Repository Tools."
:end
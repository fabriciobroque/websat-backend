@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@echo off
@setlocal

set ERROR_CODE=0

if "%JAVA_HOME%"=="" (
  echo Error: JAVA_HOME is not set.
  set ERROR_CODE=1
  goto :error
)

if not exist "%JAVA_HOME%\bin\java.exe" (
  echo Error: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
  set ERROR_CODE=1
  goto :error
)

set MAVEN_PROJECTBASEDIR=%~dp0
cd /d "%MAVEN_PROJECTBASEDIR%"

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

if not exist %WRAPPER_JAR% (
  echo Downloading Maven wrapper...
  powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile %WRAPPER_JAR% -UseBasicParsing}"
  if errorlevel 1 (
    echo Failed to download Maven wrapper.
    set ERROR_CODE=1
    goto :error
  )
)

"%JAVA_HOME%\bin\java.exe" -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
set ERROR_CODE=%ERRORLEVEL%

:error
@endlocal & exit /b %ERROR_CODE%

@echo off
setlocal
set "MVN_VERSION=3.10.0"
set "MVN_HOME=%LOCALAPPDATA%\Maven\apache-maven-%MVN_VERSION%"
set "MVN_ZIP=%TEMP%\apache-maven-%MVN_VERSION%-bin.zip"
if not exist "%MVN_HOME%\bin\mvn.cmd" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $base='https://archive.apache.org/dist/maven/maven-3/3.10.0/binaries/apache-maven-3.10.0-bin.zip'; $zip=$env:TEMP+'\apache-maven-3.10.0-bin.zip'; Invoke-WebRequest $base -OutFile $zip; $sha=[System.Security.Cryptography.SHA256]::Create(); try { $actual=[System.BitConverter]::ToString($sha.ComputeHash([System.IO.File]::ReadAllBytes($zip))).Replace('-','').ToLowerInvariant() } finally { $sha.Dispose() }; $expected='1f6d9909266510f039f59aa0e13dcd2c66da85f043e56276e41f2918f8bddaff'; if ($actual -ne $expected) { throw 'Maven distribution checksum mismatch' }; New-Item -ItemType Directory -Force -Path $env:LOCALAPPDATA\Maven | Out-Null; Expand-Archive -Force $zip $env:LOCALAPPDATA\Maven"
  if errorlevel 1 exit /b 1
)
call "%MVN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%

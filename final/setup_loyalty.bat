@echo off
setlocal

echo ------------------------------------
echo -- Starting Loyalty installation  --
echo ------------------------------------
echo

if not "%ATGDIR%" == "" (
 set DYNAMO_HOME=%ATGDIR%\home
) else if not "%DYNAMO_HOME%" == "" (
 set ATGDIR=%DYNAMO_HOME%\..
) 

if "%DYNAMO_HOME%" == "" (
  echo. 
  echo. You must set the DYNAMO_HOME variable to point to the
  echo. 'home' directory in your ATG installation 
  echo. ^(for instance, set ATGDIR=C:\ATG\ATG2007.1^)
  echo.
  goto :end
)

echo -- Using ATGDIR= %ATGDIR%
echo ------------------------------------
echo -- Installing Loyalty directory --
echo ------------------------------------
echo

rem If the Loyalty directory already exists, save it.
if exist %ATGDIR%\Loyalty (
  echo Loyalty already exists, copying to Loyalty-save
  xcopy %ATGDIR%\Loyalty %ATGDIR%\Loyalty-save /s /e /k /q /y /i
  rmdir %ATGDIR%\Loyalty /s /q
)

mkdir %ATGDIR%\Loyalty
xcopy Loyalty %ATGDIR%\Loyalty /E /V /F /Q /-Y /i

echo ------------------------------------
echo -- Loyalty directory installed    --
echo ------------------------------------
echo

set SOLIDDIR=%ATGDIR%\DAS\solid

echo ------------------------------------
echo -- Starting Loyalty Solid database now.  Please press any key when Solid startup completes...
echo ------------------------------------
echo

start %SOLIDDIR%\i486-unknown-win32\solfe.exe -c %SOLIDDIR%\dev2db
pause

echo ------------------------------------
echo -- Setup Loyalty tables           --
echo ------------------------------------

%SOLIDDIR%\i486-unknown-win32\solsql -f %ATGDIR%\Loyalty\sql\loyalty.sql "tcp localhost 1313" admin admin

echo ------------------------------------
echo -- Loyalty tables setup completed --
echo ------------------------------------
echo
echo ------------------------------------
echo -- Loyalty installation completed --
echo ------------------------------------

:end
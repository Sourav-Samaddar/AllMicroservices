@echo off
setlocal enabledelayedexpansion

REM ============================
REM Build Script for All Services
REM Usage:
REM   build.bat             -> Runs builds in parallel (default)
REM   build.bat sequential  -> Runs builds sequentially
REM ============================

REM File containing services to skip
set "SKIP_FILE=skip-services.txt"

REM Load skip list if file exists
set "SKIP_SERVICES="
if exist "%SKIP_FILE%" (
    echo Loading skip list from %SKIP_FILE%...
    for /f "usebackq delims=" %%S in ("%SKIP_FILE%") do (
        set "SKIP_SERVICES=!SKIP_SERVICES! %%S"
    )
    echo Skip list loaded: !SKIP_SERVICES!
) else (
    echo No skip-services.txt found. Nothing will be skipped.
)

set "MODE=parallel"
if /I "%1"=="sequential" (
    set "MODE=sequential"
)

if "%MODE%"=="sequential" (
    echo Running SEQUENTIAL build...
    echo ============================
    for /d %%D in (*) do (
        REM Skip IntelliJ/Eclipse metadata folders
        if /I not "%%~nxD"==".idea" if /I not "%%~nxD"==".metadata" (
            if exist "%%D\pom.xml" (
				echo %SKIP_SERVICES% | findstr /i "\<%%~nxD\>" >nul && (
				echo Skipping %%~nxD found in skip-services list
				) || (
					echo ===========================
					echo Building %%D
					echo ===========================
					
					pushd "%%D"
					mvn clean install -DskipTests
					popd
				)
            ) else (
                echo Skipping %%D (no pom.xml found)
            )
        )
    )
) else (
    echo Running PARALLEL build...
    echo ============================
    for /d %%D in (*) do (
        if /I not "%%~nxD"==".idea" if /I not "%%~nxD"==".metadata" (
            if exist "%%D\pom.xml" (
				echo %SKIP_SERVICES% | findstr /i "\<%%~nxD\>" >nul && (
				echo Skipping %%~nxD found in skip-services list
				) || (
					echo Starting build for %%D ...
					REM Run each build in background, redirect logs
					start /b cmd /c "(cd /d %%D && mvn clean install -DskipTests) > %%D\build.log 2>&1 && echo Build for %%D finished. || echo Build for %%D FAILED."
				)
                
            ) else (
                echo Skipping %%D (no pom.xml found)
            )
        )
    )
)

endlocal

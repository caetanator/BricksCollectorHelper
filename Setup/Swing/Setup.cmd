@Echo Off
Set jdk=%ProgramFiles%\Java\jdk1.6.0_45
Set jre=%ProgramFiles%\Java\jre1.6
Set _Dir=%CD%
Echo %_Dir%
Rem Java Developers Kit
If Exist "%jdk%" (
	Rem Java Swing API Look & Feel configuration.
	Rem Edit the file to change the default Swing Look & Feel for all applications
	Copy /Y ".\swing.properties" "%jdk%\jre\lib\" 
)
Rem Java Run-time Environment
If Exist "%jre%" (
	Rem Java Swing API Look & Feel configuration.
	Rem Edit the file to change the default Swing Look & Feel for all applications
	Copy /Y ".\swing.properties" "%jre%\lib\"
)
pause

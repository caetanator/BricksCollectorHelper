@ECHO OFF

REM Set the Java RE VM directory
REM SET cs_JRE=%ProgramFiles%\Java\jre6
REM SET cs_JRE=%ProgramFiles%\Java\jre-1.8
SET cs_JRE=%ProgramFiles%\Java\jdk-18.0.2

REM Set the application path
SET cs_Path=%PATH%
SET PATH=%cs_JRE%\bin;%PATH%

REM Set the Java Swing default look and feel
REM SET cs_LaF=
SET cs_LaF=-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=com.sun.java.swing.plaf.mac.MacLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=com.sun.java.swing.plaf.motif.MotifLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel
REM SET cs_LaF=-Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel

REM Set the Java international language and region
SET cs_LangReg=
REM SET cs_LangReg=-Duser.language=pt -Duser.region=PT
REM SET cs_LangReg=-Duser.language=en -Duser.region=US

REM Set the default Java Class Path to the application local directory
SET cs_ClassPath=%CLASSPATH%
SET CLASSPATH=.

REM Run the application
java %cs_LaF% %cs_LangReg% -jar CaetanoSoft.JavaDemoApp.jar
REM START javaw %cs_LaF% %LANG% -jar CaetanoSoft.JavaDemoApp.jar

REM Clean up system variables
SET PATH=%cs_Path%
SET CLASSPATH=%cs_ClassPath%
SET cs_Path=
SET cs_ClassPath=
SET cs_JRE=
SET cs_LangReg=
SET cs_LaF=
REM PAUSE

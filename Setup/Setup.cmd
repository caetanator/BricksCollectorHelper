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

REM Set the JavaPOS XML configuration file
SET cs_JPOS=
REM Set cs_JPOS=-Djpos.config.populatorFile=".\jpos.xml"

REM Set the default Java Class Path to the application local directory
SET cs_ClassPath=%CLASSPATH%
SET CLASSPATH=.

REM Set the Sun JavaBeans Activation Framework (not for Java RE 1.6 or Java EE 5.0, or later )
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\activation.jar"

REM Set the Sun JavaMail
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\mail.jar"

REM Set the Sun JavaComm
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\common.jar"

REM Set the EPSON JavaPOS
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\jpos111.jar";"%cs_JRE%\lib\ext\pos.jar";"%cs_JRE%\lib\ext\epsonupos.jar";"%cs_JRE%\lib\ext\uposcommon.jar";"%cs_JRE%\lib\ext\linuxUsbDriver.jar";"%cs_JRE%\lib\ext\xercesImpl.jar";"%cs_JRE%\lib\ext\xml-apis.jar"

REM Set the MS SQL Server JDBC
REM SQL Server 2000 (Java RE 1.3)
REM SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\msbase.jar";"%cs_JRE%\lib\ext\msutil.jar";"%cs_JRE%\lib\ext\mssqlserver.jar"
REM SQL Server 2000/2005 (Java RE 1.5)
REM SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\sqljdbc.jar"
REM SQL Server 2005/2008 (Java RE 1.6)
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\sqljdbc4.jar"

REM Set the Apache Log4Java
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\log4j.jar"

REM Set the iText PDF/XLS/HTML/RTF Converter
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\iText-2.1.3.jar";"%cs_JRE%\lib\ext\iText-rtf-2.1.3.jar";"%cs_JRE%\lib\ext\iText-rups-2.1.3.jar"

REM Set the nachoCalendar
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\nachocalendar-0.23.jar"

REM Set the Sun Enterprise JavaBeans
SET CLASSPATH=%CLASSPATH%;"%cs_JRE%\lib\ext\ejb.zip"

REM Run the application
java %cs_LaF% %cs_LangReg% %cs_JPOS% -jar application.jar
REM START javaw %cs_LaF% %cs_LangReg% %cs_JPOS% -jar application.jar

REM Clean up system variables
SET PATH=%cs_Path%
SET CLASSPATH=%cs_ClassPath%
SET cs_Path=
SET cs_ClassPath=
SET cs_JRE=
SET cs_LangReg=
SET cs_LaF=
SET cs_JPOS=
REM PAUSE

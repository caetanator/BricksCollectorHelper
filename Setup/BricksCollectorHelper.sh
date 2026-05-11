#!/bin/sh

# Set the Java RE VM directory
if [ "$JAVA_HOME" != "" ]; then
  cs_JRE=${JAVA_HOME}
  cs_EmuVer="-Djava.version=21"
elif [ "$JRE_HOME" != "" ]; then
  cs_JRE=${JRE_HOME}
  cs_EmuVer="-Djava.version=21"
else
  if [ -d "/usr/lib/jvm/java-25-openjdk-amd64" ]; then
	  echo "Java RE v25 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-25-openjdk-amd64
	  cs_EmuVer="-Djava.version=21"
  elif [ -d "/usr/lib/jvm/java-21-openjdk-amd64" ]; then
	  echo "Java RE v21 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-21-openjdk-amd64
	  cs_EmuVer=""
  elif [ -d "/usr/lib/jvm/java-11-openjdk-amd64" ]; then
	  echo "Java RE v11 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-11-openjdk-amd64
	  cs_EmuVer="-Djava.version=11"
  elif [ -d "/usr/lib/jvm/java-1.8.0-openjdk-amd64" ]; then
	  echo "Java RE v1.8 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-1.8.0-openjdk-amd64
	  cs_EmuVer="-Djava.version=1.8"
  elif [ -d "/usr/lib/jvm/java-7-openjdk" ]; then
	  echo "Java RE v1.7 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-7-openjdk
	  cs_EmuVer="-Djava.version=1.7"  
  elif [ -d "/usr/lib/jvm/java-6-sun-1.6.0.45" ]; then
    echo "Java RE v1.6 x64 detected!"
	  cs_JRE=/usr/lib/jvm/java-6-sun-1.6.0.45
	  cs_EmuVer="-Djava.version=1.6"
  else
	  echo "ERROR: Java RE VM v21 not found!"
    echo "Java VMs Installed:"
	  # list directories in the form "/usr/lib/jvm/"
	  for dir in /usr/lib/jvm/*/; do
	    # remove the trailing "/"
	    dir=${dir%*/}
	    # print everything after the final "/"
	    echo "${dir##*/}"
	  done
	  exit -2
  fi
fi

# Set the application path
#unset PATH
if [ "$PATH" != "" ]; then
  oldPATH=${PATH}
	PATH=${cs_JRE}/bin:${PATH}
else
	PATH=${cs_JRE}/bin
fi
export PATH

# Set the default Java Class Path to the application local directory
#unset CLASSPATH
if [ "$CLASSPATH" != "" ]; then
	cs_ClaPat=.:${CLASSPATH}
else
	cs_ClaPat=.
fi

# Set the application path
#unset PATH
if [ "$PATH" != "" ]; then
  oldPATH=${PATH}
	PATH=${cs_JRE}/bin:${PATH}
else
	PATH=${cs_JRE}/bin
fi
export PATH

# Set the default Java Class Path to the application local directory
#unset CLASSPATH
if [ "$CLASSPATH" != "" ]; then
	cs_ClaPat=.:${CLASSPATH}
else
	cs_ClaPat=.
fi

# Set the Java Swing default look and feel
#cs_LaF=""
#* Microsoft Windows Look & Feel (Only on Windows XP OS or later, since JRE v1.2)
#cs_LaF="-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
#* Microsoft Windows Classic Look & Feel (Only on Windows OS, since JRE v1.1)
#cs_LaF="-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"
#* Apple OS X Look & Feel (Only on macOS X, since JRE v1.6)
#cs_LaF="-Dswing.defaultlaf=com.apple.laf.AquaLookAndFeel"
#* Apple Classic Look & Feel (Only on Mac OS 9 or earlier, since JRE v1.1)
#cs_LaF="-Dswing.defaultlaf=com.sun.java.swing.plaf.mac.MacLookAndFeel"
#cs_LaF_Extra="-Dapple.laf.useScreenMenuBar=\"true\""
#cs_LaF_Extra="${cs_LaF_Extra} -Dapple.awt.brushMetalLook=\"true\""
#cs_LaF_Extra="${cs_LaF_Extra} -Dapple.awt.fileDialogForDirectories=\"true\""
#cs_LaF_Extra="${cs_LaF_Extra} -Dapple.awt.fakefullscreen=\"true\""
#* GTK+ Look & Feel (Only on Linux/UNIX OS with GTK 2.2 GUI installed, since JRE v1.4.2)
#cs_LaF="-Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
#cs_LaF_Extra="-Dswing.gtkthemefile=PATH_2_PROPERTIES_FILE/gtkrc"
#* CDE/Motif Look & Feel (All Java platforms, since JRE v1.1)
#cs_LaF="-Dswing.defaultlaf=com.sun.java.swing.plaf.motif.MotifLookAndFeel"
#* Metal Look & Feel (All Java platforms, since JRE v1.1)
#cs_LaF="-Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel"
#* Nimbus Look & Feel (All Java platforms, since JRE v1.6)
cs_LaF="-Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel"

# Set the Java international language and region
#* OS default
#cs_LangCou=""
#* Portuguese, Portugal
cs_LangCou="-Duser.language=pt -Duser.country=PT"
#* English, Great Britan
#cs_LangCou="-Duser.language=en -Duser.country=GB"
#* English, United States of America
#cs_LangCou="-Duser.language=en -Duser.country=US"

# Text files default Code Page
#* OS default
cs_CP=""
#* ISO 8859-1 (Latin-1/International)
#cs_CP=-Dfile.encoding=ISO8859_1
#* ISO 8859-15 (Latin-9) [+ Euro character support]
#cs_CP=-Dfile.encoding=ISO8859_15
#* MS-DOS/OEM Latin-1/International
#cs_CP=-Dfile.encoding=Cp850
#* MS-DOS/OEM Portugese
#cs_CP=-Dfile.encoding=Cp860
#* Windows Latin-1/International
#cs_CP=-Dfile.encoding=Cp1252
#* Unicode UTF-8
#cs_CP=-Dfile.encoding=UTF-8
#* Unicode UTF-16LE+BOM: Unicode 1.1/2.0, UCS-2, DBCS, Little Endian (used by Microsoft Windows NT/2000/XP/Vista/7/10)
#cs_CP=-Dfile.encoding=UnicodeLittle
#* Unicode UTF-16BE+BOM: Unicode 1.1/2.0, UCS-2, DBCS, Big Endian (used by Microsoft Windows NT for MIPS/Alpha/PowerPC)
#cs_CP=-Dfile.encoding=UnicodeBig
#* Unicode UTF-16BE: Unicode 1.1, UCS-2, DBCS, Big Endian (used by IBM AS/400 primarily in the IFS)
#cs_CP=-Dfile.encoding=61952
#* EBCDIC Latin-1/International (MNCS)
#cs_CP=-Dfile.encoding=Cp500
#* EBCDIC Latin-1/International (MNCS) [+ Euro character support]
#cs_CP=-Dfile.encoding=Cp1148
#* EBCDIC United States, Canada, Netherlands, Portugal, Brazil, Australia, New Zealand, and South Africa
#cs_CP=-Dfile.encoding=Cp037
#* EBCDIC United States, Canada, Netherlands, Portugal, Brazil, Australia, New Zealand, and South Africa [+ Euro character support]
#cs_CP=-Dfile.encoding=Cp1140

# Creates the application logs directory
if ! [ -d "./logs/" ]; then
  echo "Creating logs directory!"
  mkdir "./logs/"
fi

# Run the application
${cs_JRE}/bin/java ${cs_EmuVer} ${cs_LaF} ${cs_LaF_Extra} ${cs_LangCou} ${cs_CP} -cp "${cs_ClaPat}" -jar BricksCollectorHelper-1.0-SNAPSHOT.jar $@
#${cs_JRE}/bin/javaw ${cs_EmuVer} ${cs_LaF} ${cs_LaF_Extra} ${cs_LangCou} ${cs_CP} -cp "${cs_ClaPat}" -jar BricksCollectorHelper-1.0-SNAPSHOT.jar $@ &

# Print the exit code of the application
echo Exit Code: "$?"

# Clean up system variables
cs_JRE=
cs_ClaPat=
cs_EmuVer=
cs_LangCou=
cs_LaF=
cs_LaF_Extra=
cs_CP=
export PATH=${oldPATH}
oldPATH=
#read -p "Press [Enter] key"

How to start FixCracker using zip distributive

Prerequisites:
You need to configure JAVA_HOME variable on your machine. You can check this by following commands:
Unix:
echo $JAVA_HOME
./$JAVA_HOME/bin/java -version

Windows:
echo %JAVA_HOME%
%JAVA_HOME%\bin\java -version


on Unix system:
1. Extract content of archive into separate folder:
    unzip FixCracker-1.0-SNAPSHOT.zip
2. Make sh script executable:
    chmod u+x launch.sh
3. Start application:
    ./launch.sh



on Windows system:
1. Extract content of archive into separate folder using some archive manager
    Using 7-Zip archiver:
    7z.exe x FixCracker-1.0-SNAPSHOT.zip
2. Start application:
    launch.cmd

FIX Cracker is a lightweight UI tool built on QuickFIXJ base  

Main features:
* Configurable list of sessions for future use
* Composing of custom FIX messages to send
* Convenient view of sent and received FIX messages

![screenshot](screenshot_mainWindow.png)

To make distributive you need Git & Maven installed on your computer:
1) clone repo
    git clone https://github.com/fix-cracker/fix-cracker.git
2) make distributive with Maven
    cd fix-cracker    
    mvn clean install -Dmaven.test.skip=true

this will make archive fix-cracker/distro-zip/target/FixCracker-<version>.zip    
    
    
       




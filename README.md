# jnake
A terminal game of Snake written in Java

## Note
Recommended to be run in a terminal window as it is very laggy in the bare Linux tty

## How to compile (outputs to <srcdir>/lib/build/libs/jnake.jar)
* Linux:
```
./gradlew clean build
```
* Windows (unsupported and heavily broken):  
CMD (cmd is weird so this runs the batch file like it's supposed to):  
`gradlew clean build`  
Powershell:  
`.\gradlew.bat clean build`  
 
When compiled on Windows:
* The block character is broken (Appears fine when compiled on Linux and run on Windows)
* Screen doesn't clear as default cmd doesn't support ASCII escape codes
* Keyboard input doesn't work
* On terminals where ASCII escape codes are supported clearing the screen is laggy

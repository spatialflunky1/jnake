# jnake
Terminal snake written in Java

## Note
Recomended to be ran in a terminal window as it is very laggy in the bare linux terminal

## How to compile (outputs to <srcdir>/lib/build/libs/jnake.jar)
* Linux:
```
./gradlew clean build
```
* Windows:  
CMD (cmd is stupid so this runs the batch file like its supposed to):  
`gradlew clean build`  
Powershell:  
`.\gradlew.bat clean build`  
 
When compiled on windows:
* The block charecter is broken (Appears fine when compiled on linux and ran on windows)
* Screen doesn't clear as default cmd doesn't support ascii escape codes
* Keyboard input doesn't work
* On terminals where ascii escape codes are supported clearing the screen is laggy
  

  
## Planned features
* Windows support (hellish torture to try, might have to rewrite a lot)

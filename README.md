# jnake
Terminal snake written in Java

## Note
Recomended to be ran in a terminal window as it is very laggy in the bare linux terminal

## Debug Note
Space bar is used to increase the score and length of the snake

## How to compile (outputs to <srcdir>/lib/build/libs/jnake.jar)
* Linux:
```
./gradlew build
```
* Windows:  
CMD (cmd is stupid so this runs the batch file like its supposed to):  
`gradlew build`  
Powershell:  
`.\gradlew.bat build`  
  
Currently it will compile but not run because it depends on the linux `stty` command  
  
## Planned features
* Windows support

# ImpendingHTTP

Kompilera programmet genom att först göra Server och Plugin till var sin modul.  

Moduler måste läggas till manuellt i IntelliJ. Server läggs till som maven modul och Plugin från source files. 
Plugin har dependency mot Server och Maven: com.googlecode.json-simple:json-simple:1.1.1
Servers depencencys finns i pom-filen och ska laddas ner automatiskt. (Server är maven projekt men inte Plugin).

För att skapa jar filer gör man artifacts på båda modulerna som läggs i Server mappen.
jar filerna har följande innehåll:

Plugin.jar: Extracted json-simple-1.1.1.jar
Extracted mongo-java-driver.3.8.2.jar
Plugin compile output och server compile output.

Server.jar: Extracted json-simple-1.1.1.jar
Extracted mongo-java-driver.3.8.2.jar
Extracted okhttp-3.13.1.jar
Extracted okio1.17.2.jar
Server compile output.  

För att köra programmet står man i mappen "Server" och kör:

java -cp Plugin.jar org.ia.Main

Om man kör direkt från IntelliJ:

Main class: org.ia.Main
Working directory: ~/IdeaProjects/ImpendingHTTP/Server
Use classpath of module: Plugin 
Include dependencies with provided scope.

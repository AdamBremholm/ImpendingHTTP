# ImpendingHTTP

Kompilera programmet genom att först göra Server och Plugin till var sin modul.  
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

För att köra programmet står man i Servermappen och kör:

java -cp Plugin.jar org.ia.Main
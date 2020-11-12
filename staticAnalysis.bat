@ECHO OFF
:: Section : Google Java Format
ECHO ============================
ECHO FORMATTER CHECKING
ECHO ============================
java -jar lib/google-java-format-1.9-all-deps.jar --replace src/osd/*.java
ECHO Done
PAUSE
:: Section : SpotBugs
ECHO ============================
ECHO SpotBugs CHECKING
ECHO ============================
java -jar lib\spotbugs-4.1.4\lib\spotbugs.jar -textui src/osd/*.class
ECHO Done
PAUSE
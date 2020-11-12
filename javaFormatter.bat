@ECHO OFF
:: Section : Google Java Format
ECHO ============================
ECHO FORMATTER CHECKING
ECHO ============================
java -jar lib/google-java-format-1.9-all-deps.jar --replace src/osd/*.java
ECHO Done
PAUSE
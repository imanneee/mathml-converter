To build this tool simply run `mvn clean package` from pom.xml location.
In folder `target` will be a jar file whose name contains `with-dependencies`.
To run this tool type this text from `target` folder: 
`java -jar mathml-converter-1.3-SNAPSHOT-jar-with-dependencies.jar [-l en] <input XML file location>`,
where `-l en` is optional language parameter with supported values `en, sk, cs`.
Input XML file location is mandatory.

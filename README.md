dbmsau
======

Relational DBMS for DB course at SPbAU

Чтобы заработало надо сделать:

git clone https://github.com/vbmacher/cup-maven-plugin.git куда-нибудь
cd куда-нибудь/cup-maven-plugin && mvn install

Потом вернутся в директорию проекта и сделать:

mvn assembly:assembly
java -cp target/dbmsau-1.0-SNAPSHOT-jar-with-dependencies.jar ru.spbau.mit.dbmsau.App

или для дебага mvn package && mvn exec:exec
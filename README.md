
Database setting:
1. Download postgresql if don't have it on your machine
2. Create Postgres database "taskmanagertasks" on your localhost with 8080 port (url should be: postgresql://localhost/taskmanagertasks) // CREATE DATABASE taskmanagertasks;
3. Use command "Create user someuser with password somepassword"
4. Use command "Grant all privileges for taskmanagertasks to someuser"

Start working:

1. Open terminal in project root folder(where this README lies) and type "mvn package"
2. If build successful, go to "target" subfolder, open another terminal,where type "java -jar taskmanager-1.0-SNAPSHOT.jar"
3. After you see in that terminal "Started Main in ...sec", open browser and use API:
 
https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/3.0.0

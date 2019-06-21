
Database setting:
1. Download postgresql if don't have it on your machine
2. Open console and type "sudo -u postgres psql" to get into Postgres admin console.
3. Use command "CREATE DATABASE taskmanagertasks;" to create Postgres database "taskmanagertasks" on your machine
4. Use command "Create user someuser with password 'somepassword';" to create user "someuser" 
5. Use command "Grant all privileges on database taskmanagertasks to someuser;" 
   to get user someuser full access to database taskmanagertasks
6. Use command "\q" to exit admin console.

If you need to interact with database in console, 
use command "psql -h localhost taskmanagertasks someuser" to connect to it on your machine.

Start working:

1. Open terminal in project root folder(where this README lies) and type "mvn package"
2. If build successful, go to "target" subfolder, open another terminal,where type "java -jar taskmanager-1.0-SNAPSHOT.jar"
3. After you see in that terminal "Started Main in ...sec", open browser and use API:
 
https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/3.0.0

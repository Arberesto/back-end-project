frontend:

add your React App's "build" folder in /var/www/taskmanager/frontend

Nginx:

sudo apt-get install nginx
// nginx have user 'www-data' to interact witn system, so we give him access to frontend
sudo chown -R www-data:www-data /var/www/taskmanager/frontend

//create config file(taskmanager.dev) in /etc/nginx/sites-available
server {
	listen 80;
	server_name taskmanager.dev;
	root /var/www/taskmanager/frontend;
	index index.html;

	access_log /var/logs/nginx/taskmanager.dev/access.log;
	error_log /var/logs/nginx/taskmanager.dev/error.log;

	location ~* ^/api/(.*) {
	 proxy_pass http://127.0.0.1:8080/$1$is_args$args;
	}
}


//create link for config file
sudo ln -s /etc/nginx/sites-available/taskmanager.dev /etc/nginx/sites-enabled/taskmanager.dev


//check nginx

sudo service nginx status
//restart service
sudo service nginx reload

//to work correctly in local network, write 127.0.0.1 and server_name in etc/hosts


Database setting:
1. Download postgresql if don't have it on your machine (use postgres 9.6+)

sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
wget -q https://www.postgresql.org/media/keys/ACCC4CF8.asc -O - | sudo apt-key add -
sudo apt-get update
sudo apt-get install postgresql-9.6


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
(https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/5.0.0)

Taskmanager project(server part):

Requires: postgres 9.6+, Java 1.8+


--------------------
I. Frontend:

Uses build of Taskmanager project(web part): (TODO: link) 

1.Download 'front-end project' repository and build React project
2.Add your React App's "build" folder in /var/www/taskmanager/frontend

--------------------
II. Nginx:

sudo apt-get install nginx
// nginx have user 'www-data' to interact witn system, so we give him access to frontend
sudo chown -R www-data:www-data /var/www/taskmanager/frontend

//create config file(taskmanager.io) in /etc/nginx/sites-available
server {
	listen 80;
	server_name taskmanager.io;
	root /var/www/taskmanager/frontend;
	index index.html;

	access_log /var/logs/nginx/taskmanager.io/access.log;
	error_log /var/logs/nginx/taskmanager.io/error.log;

	 location /todo {
    		try_files $uri $uri/ /index.html;
  	}

 	location /done {
    		try_files $uri $uri/ /index.html;
  	}

	location ~* ^/api/(.*) {
		if ($request_method = 'OPTIONS') {
        		add_header 'Access-Control-Allow-Origin' '*';
        		add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, DELETE, OPTIONS';
        #
        # Custom headers and headers various browsers *should* be OK with but aren't
        #
        		add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        #
        # Tell client that this pre-flight info is valid for 20 days
        #
        		add_header 'Access-Control-Max-Age' 86400;
        		add_header 'Content-Type' 'text/plain; charset=utf-8';
        		add_header 'Content-Length' 0;
        		return 204;
     		}
 		if ($request_method = 'POST') {
        		add_header 'Access-Control-Allow-Origin' '*';
        		add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, DELETE, OPTIONS';
        		add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        		add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range';
     		}
     		if ($request_method = 'GET') {
        		add_header 'Access-Control-Allow-Origin' '*';
        		add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, DELETE, OPTIONS';
        		add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        		add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range';
    		 }
		if ($request_method = 'PATCH') {
        		add_header 'Access-Control-Allow-Origin' '*';
        		add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, DELETE, OPTIONS';
        		add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        		add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range';
    		 }
		if ($request_method = 'DELETE') {
        		add_header 'Access-Control-Allow-Origin' '*';
        		add_header 'Access-Control-Allow-Methods' 'GET, POST, PATCH, DELETE, OPTIONS';
        		add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
        		add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range';
    		 }
	 proxy_pass http://127.0.0.1:8080/$1$is_args$args;
	}
}

//create link for server config file
sudo ln -s /etc/nginx/sites-available/taskmanager.io /etc/nginx/sites-enabled/taskmanager.io


//check nginx

sudo service nginx status
//restart service
sudo service nginx reload

//to work correctly in local network, write 127.0.0.1 and server_name in etc/hosts


--------------------
III. Database:
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

--------------------
IV. Users:

By default sql migrations, you get two users:

user - with password "hooray" and with role USER
admin - with password "51thsong" and with role ADMINs

You can easily change default password hashes and users themselfs in migrations.

--------------------
V: Start working:

1. Open terminal in project root folder(where this README lies) and type "mvn package"
2. If build successful, go to "target" subfolder, open another terminal,where type "java -jar taskmanager-1.0-SNAPSHOT.jar"
3. After you see in that terminal "Started Main in ...sec", open browser and use API:
 
https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/5.0.0

--------------------
VI. API:
If not authorised - return 403

-------
Used Schemas in this API:

User-scheme :

User{
id*	string($uuid)
example: 0884831b-42f9-4f81-af88-3e777be69c8f

id of the user
username*	string
example: admin

name of the user
authorities*	[...]
}
-------

A) Tasks

1)GET /tasks 
(Authorization Role: USER)


2)POST /tasks 
(Authorization Role: USER)

3)GET /tasks/{id} 
(Authorization Role: USER)

4)PATCH /tasks/{id} 
(Authorization Role: USER)

5)DELETE /tasks/{id}
(Authorization Role: USER)

B)Users

1)POST /signin 
(Authorization Role: ANONYMOUS)

2)POST /signup 
(Authorization Role: ANONYMOUS)

3)GET /users 
(Authorization Role: ADMIN)

4)GET /users/{id} 
(Params: 
id -> path{string} - id of user to return
)
(Return: User-scheme)
(Authorization Role: ADMIN)

5)GET /whoami 
(Authorization Role: USER)
(Params:no params)
(Status: 200)
(Media-type: application/json)
(Return: User-scheme)
 

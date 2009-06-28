Lego mindstorm Control api.
Current state:
Will answere a message with true or false base on message.
follow command will result in true:
cmd motor ***** on
cmd motor ***** off
cmd ger sensor value *****

written in Visual C# Framework 2.0

For testing I used 
database:
type: mysql
Server: localhost
user: root
password: 
database: rover
   A dumb of the database can be found here:
web:
apache
server: localhost
folder: /rover/

How to use

Run:
start wamp server
........legomindstorm\C api\Lego MindStorm Control Api\Lego MindStorm Control Api\bin\Release\Lego MindStorm Control Api.exe
go to http://localhost/rover/control.php
set nick name
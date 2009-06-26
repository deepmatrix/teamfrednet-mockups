Lego mindstorm Control api.
Current state:
Will answere a message with true or false base on message.
follow command will result in true:
cmd motor ***** on
cmd motor ***** off

written in Visual C# Framework 2.0

For testing I just bewareircd-win32(version 1.5.7)(http://ircd.bircd.org/ -> download) as IRC server and mIRC as cleint.
Server: localhost
channel: #control
nickname of C api: Cpai

How to use

Run:
start IRC server
use a cleint to connect to #control
start
........legomindstorm\C api\Lego MindStorm Control Api\Lego MindStorm Control Api\bin\Release\Lego MindStorm Control Api.exe
type something in the cleint; this will result in "Result false, Not a command".
type:
cmd motor AvBvC aan
result should be: "Result true"
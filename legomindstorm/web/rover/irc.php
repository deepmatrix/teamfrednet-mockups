<?php
$start = date("U");
//irc.php
include("config.php");
function str2hex($str){
$out = "";
for($i = 0;$i <strlen($str);$i++){
$out .= dechex(ord($str{$i}));
}
return $out;
}
//set up contetion
if(!$socket = socket_create(AF_INET,SOCK_STREAM,SOL_TCP)) { 
    die('Error: socket_create'); 
   
} 
 
if(!socket_bind($socket,$config['hostname'])) { 
    die('Error: socket_bind'); 
} 
 
if(!socket_connect($socket,$config['server'],$config['port'])) { 
    die('Error: socket_connect'); 
}  
//functions
// Schrijf data naar de socket 
function write($data) { 
    global $socket; 
	echo $data . "\n";
    socket_write($socket,$data."\r\n"); 
}


//ident
function ident(){
global $config;
write("NICK {$config['nickname']}");
write("USER {$config['user']}");
write("JOIN {$config['channel']}");
}
ident();
//read db and send to Capi
//read db
while(true) {  
$data = @socket_read($socket,65000,PHP_NORMAL_READ);
if(strlen($data)<2){
echo "!!wait(".($start-date("U")).")!!";
//alive
sleep(2);
}
$eData = explode(' ',$data); 
    if($eData[0] == 'PING') { 
        write('PONG '.$eData[1]); 
    }else{
if(count(split("Register first",$data,2))==2){
ident();
}else{
if(count(split("{$config['channel']} :",$data,2))==2&&count(split(":Result ",$data,2))==2){
$res = split("{$config['channel']} :",$data,2);
$msg_recieved = $res[1];
$msg_recieved = split(" ",substr($msg_recieved,0,strlen($msg_recieved)-1));

$sql = "UPDATE `log_current_session` SET `status`='{$msg_recieved[1]}' WHERE `ID`='$last_send_command'";
mysql_query($sql) or die(mysql_error() . "sql: $sql");
}
if($last_time != date("U")){
$sql = "SELECT * FROM `log_current_session` WHERE `type`='cmd' AND `status`='' ORDER BY `log_current_session`.`ID` DESC LIMIT 0,1";
$result = mysql_query($sql);
$row = mysql_fetch_array( $result );
$last_time = date("U");
if(count($row)>1){
$msg = ":{$config['nickname']}! PRIVMSG {$config['channel']} :" . $row['message'];
$last_send_command = $row['ID'];
write($msg);
}else{
usleep(1000);
//echo "wait!";
}
}
}
}

	
}
//close
?>
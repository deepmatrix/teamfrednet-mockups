<?php
//config.php
//mysql
$mysql['database'] = "rover";
$mysql['user'] = "root";
$mysql['password'] = "";
$mysql['server'] = "localhost";
if(!isset($not_connect)){
//contect to database
mysql_connect($mysql['server'], $mysql['user'], $mysql['password']) or die(mysql_error());
mysql_select_db($mysql['database']) or die(mysql_error());
}
//session start
session_start();
if(!isset($_SESSION['last_time_check'])){
$_SESSION['last_time_check'] = (date("U") + microtime()) - 60;
}
//timeout
$user_offline_time = 300;
$stream_url = "mms://192.168.50.103:81/";//the stream url
?>
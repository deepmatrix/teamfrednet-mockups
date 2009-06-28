<?php
//config.php
//mysql
$mysql['database'] = "rover";
$mysql['user'] = "root";
$mysql['password'] = "";
$mysql['server'] = "localhost";
//contect to database
mysql_connect($mysql['server'], $mysql['user'], $mysql['password']) or die(mysql_error());
mysql_select_db($mysql['database']) or die(mysql_error());
//session start
session_start();
if(!isset($_SESSION['last_time_check'])){
$_SESSION['last_time_check'] = (date("U") + microtime());
}
//timeout
$user_offline_time = 60;
//irc
$config = array(); 
$config['nickname'] = 'php'; 
$config['realname'] = 'php bot'; 
$config['hostname'] = 0; 
$config['server']   = 'localhost'; 
$config['port']    = 6667; 
$config['channel']   = "#control";
$config['user'] = "PHP 127.0.0.1 {$config['server']} :PHP";
?>
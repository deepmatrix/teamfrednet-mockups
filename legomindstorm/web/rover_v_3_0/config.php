<?php
//config.php
//mysql
/**
 * Mysql database 
 */
$mysql['database'] = "rover";
/**
 * Mysql user 
 */
$mysql['user'] = "root";
/**
 * Mysql password 
 */
$mysql['password'] = "";
/**
 * Mysql server 
 */
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
/**
 * Time before user will be set to Offline 
 */
$user_offline_time = 300;
/**
 * Array from stream url's
 * Max 3 streams
 * 
 */
$stream_url = array("mms://192.168.50.103:81/");
/**
 * Template name
 * 
 */
$template_name = "default";
//wiki setting
/**
 * The base url with / at the end
 * Example: http://wiki.xprize.frednet.org/
 * 
 */
$wiki_server = "http://wiki.xprize.frednet.org/";
$wiki_prefixed = "Lego_mindstorm_webpages:";
/**
 * Wiki chache time in hours
 */
$wiki_cache_time = 0.5;
include("login.php");
?>
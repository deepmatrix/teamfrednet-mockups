<?php
$not_connect = true;
include("config.php");
//control for database
mysql_connect($mysql['server'], $mysql['user'], $mysql['password']) or die(mysql_error());
if(mysql_select_db($mysql['database'])){
//go on
$temp = split("/",$_SERVER['REQUEST_URI']);
$url = "";
for($i = 0;$i+1 < count($temp);$i++){
$url .=  $temp[$i] . "/";
}
header('Location: http://'.$_SERVER['SERVER_NAME']  .$url.'control.php');
}else{
//database doesn't exits
$sql_s = split(';',implode("",file("database_dump.txt")));
foreach($sql_s as $sql){
mysql_query($sql) or die(mysql_error() . "SQL: $sql");
echo "$sql ... done!<br />\n";
}
unlink("database_dump.txt") or die("Could not delete database_dump.txt");
echo "database_dump.txt deleted<br />\n";
echo "config Done!";
}
?>
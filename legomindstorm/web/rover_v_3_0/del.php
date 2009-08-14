<?php
//del
session_start();
if(substr($_GET['id'],0,7)=="trigger"){
	$data = $_SESSION['data'];
	unset($data['trigger'][substr($_GET['id'],7)]);
	$_SESSION['data'] = $data;
}
$temp = split("/",$_SERVER['REQUEST_URI']);
$url = "";
for($i = 0;$i+1 < count($temp);$i++){
	$url .=  $temp[$i] . "/";
}
header('Location: http://'.$_SERVER['SERVER_NAME']  .$url.'rover_program.php?build');

?>
<?php
//test.php
//testing of session
include("config.php");
$_SESSION['user_ID'] = 123;
$_SESSION['user_nick'] = "marc1990";
$_SESSION['last_id'] = "4e";
print_r($_SESSION);
?>
<?php
//login demo
function send($URI, $Host, $vars){
	$ReqBody = "";
	foreach($vars as $key => $waarde){
		$ReqBody .= "$key=$waarde&";
	}
	$ContentLength = strlen($ReqBody);
	// Generate the request header
	$ReqHeader =
"POST $URI HTTP/1.1\n".
"Host: $Host\n".
"Content-Type: application/x-www-form-urlencoded\n".
"Content-Length: $ContentLength\n\n".
"$ReqBody\n";

	// Open the connection to the host
	$socket = fsockopen($Host, 80, $errno, $errstr);
	if (!$socket){

		$Result["errno"] = $errno;
		$Result["errstr"] = $errstr;
		return implode($Result);
	}
	$idx = 0;
	fputs($socket, $ReqHeader);
	while (!feof($socket)){
		$Result[$idx++] = fgets($socket, 128);
	}
	//-------------------------------------------
	return implode($Result);
}

//login
function login($user,$pass){
	global $user_info;
	$data =  explode("\r\n\r\n",send("/wiki/api.php","localhost",array("action"=>"login","lgname"=>$user,"lgpassword"=>$pass,"format"=>"php")));
	$data = unserialize($data[1]);
	if($data['login']['result'] == "Success"){
		$user_info['name'] = $user;
		return true;
	}else{
		return false;
	}
}
?>
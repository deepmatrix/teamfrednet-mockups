<?php
//make script

$data = $_SESSION['data'];

if(isset($_GET['build'])){
	if($_GET['build']=='trigger'){
		$data['trigger'][$_POST['line']]['com'] = $_POST['trigger'];
		$data['trigger'][$_POST['line']]['X'] = $_POST['X'];
		$data['trigger'][$_POST['line']]['Y'] = $_POST['Y'];
		$data['trigger'][$_POST['line']]['do'] = $_POST['do'];
	}
	if($_GET['build']=='sub'){
		$data['sub'][$_POST['ID']][$_POST['line']]['what'] = $_POST['what'];
		$data['sub'][$_POST['ID']][$_POST['line']]['X'] = $_POST['X'];
		$data['sub'][$_POST['ID']][$_POST['line']]['Y'] = $_POST['Y'];
		$data['sub'][$_POST['ID']][$_POST['line']]['do'] = $_POST['do'];
	}
	if(!isset($data['var']['speedA'])){
		$data['var']['speedA']['init'] = 80;
		$data['var']['speedB']['init'] = 80;
		$data['var']['speedC']['init'] = 80;
	}
	print_r($data);
	echo "<br />";
	//build script
	$out = "";
	foreach($data['var'] as $name => $waarde){
		$out .= "int $name;\n";
	}
	foreach($data['sub'] as $ID => $waarde){
		$out .= "//function number: $ID\n";
		$out .= "void subProgram$ID(){\n";
		foreach($waarde as $line => $waarde2){
			if($waarde2['what'] == "motor_on"){
				for($i = 4;$i < strlen($waarde2['X']);$i++){
					$out .= " RotateMotor(OUT_".$waarde2['X']{$i}.",speed".$waarde2['X']{$i}.",1000);//Note limit 1000 grades\n";
				}
			}
			if($waarde2['what'] == "set_speed"){
				for($i = 4;$i < strlen($waarde2['X']);$i++){
					$out .= " speed".$waarde2['X']{$i}." = {$waarde2['Y']};\n";
				}
			}
			if($waarde2['what'] == "motor_off"){
				$out .= " Off({$waarde2['X']});\n";
			}
			if($waarde2['what'] == "wait"){
				$out .= " wait({$waarde2['Y']});\n";
			}
			if($waarde2['what'] == "set_var"){
				$out .= " {$waarde2['X']} = {$waarde2['Y']};\n";
			}
			if(substr($waarde2['what'],0,2) == "Y_"){
				$out .= " X = {$waarde2['X']};\n Y = {$waarde2['Y']};\n";
				$out .= " if(" . str_replace(array("_","g","s","ne","e"),array(" ",">","<","!=","="),$waarde2['what']) . "){\n  ".str_replace("sub","subProgram",$waarde2['do'])."();\n }\n";
			}
		}
		$out .= "}\n";
	}
	$out .= "task main(){\n";
	$out .= " int X,Y,Z;\n";
	foreach($data['var'] as $name => $waarde){
		$out .= " $name = {$waarde['init']};\n";
	}
	//ksort($data['trigger']);
	$out .= " while(true){\n";
	foreach($data['trigger'] as $line => $waarde){
		// TODO add time
		$out .= "  //Line number: $line\n";
		$out .= "  X = {$waarde['X']};\n  Y = {$waarde['Y']};\n";
		$out .= "  if(" . str_replace(array("_","g","s","ne","e"),array(" ",">","<","!=","="),$waarde['com']) . "){\n   ".str_replace("sub","subProgram",$waarde['do'])."();\n  }\n";
	}
	$out .= " }\n";
	$out .= "}\n";
	echo str_replace("<br />","<br />\n",highlight_string($out,true));
}
$_SESSION['data'] = $data;
?>
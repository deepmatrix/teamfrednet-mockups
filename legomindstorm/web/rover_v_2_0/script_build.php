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
$out .= "RotateMotor({$waarde2['X']},speed,1000);\n";
}
}
$out .= "}\n";
}
$out .= "task main(){\n";
foreach($data['var'] as $name => $waarde){
$out .= " $name = {$waarde['init']};\n";
}
//ksort($data['trigger']);
foreach($data['trigger'] as $line => $waarde){
// TODO add time
$out .= " //Line number: $line\n";
$out .= " X = {$waarde['X']};\n Y = {$waarde['Y']};\n";
$out .= " if(" . str_replace(array("_","g","s","ne","e"),array(" ",">","<","!=","="),$waarde['com']) . "){\n  ".str_replace("sub","subProgram",$waarde['do'])."();\n }\n";
}
$out .= "}\n";
echo str_replace("<br />","<br />\n",highlight_string($out,true));
}
$_SESSION['data'] = $data;
?>
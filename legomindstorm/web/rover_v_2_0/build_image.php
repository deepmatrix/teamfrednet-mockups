<?php
//build img
if(!isset($_SESSION['id_img'])){
$_SESSION['id_img'] = md5(rand()."_".rand());
}
$controlmap = "<map name=\"controlmap\">\n";
$filename = "img/render_{$_SESSION['id_img']}.jpg";
$h = 400;
$w = 1000;
function drawblock(&$img,$raster_x,$raster_y,$title,$content,$id=""){
global $controlmap;
$w = 200;
$h = 160;
$x = $raster_x*$w-$w;
$y = $raster_y*$h-$h;
//load blok
$source = imagecreatefrompng("program_blok.png");
imagecopy($img,$source,$x,$y,0,0,160,130);
//set title
$text_color = imagecolorallocate($img, 0, 0, 0);
imagestring($img, 3, $x+20, $y+23,  $title, $text_color);
$content = explode("\n",$content);
foreach($content as $key=>$line){
imagestring($img, 2, $x+18, $y+45+$key*20,  $line, $text_color);
}
$controlmap .= '<area shape="rect" coords="'.$x.','.$y.','.($x+$w).','.($y+$h).'" alt="'.$title.'" title="'.$title.'" onclick="popup_chose(\''.$id.'\');" />'."\n";
}
$img = imagecreate($w,$h);
//draw first blok
$temp = "";
foreach($data['var'] as $key => $waarde){
$temp .= "$key = {$waarde['init']}\n";
}
drawblock($img,1,1,"Start",$temp,"start");

//draw interupts
foreach($data['trigger'] as $key => $waarde){
$title = str_replace(array("_","g","s","ne","e","X","Y"),array(" ",">","<","!=","=",$waarde['X'],$waarde['Y']),$waarde['com']);
$content = "";
foreach($data['sub'][substr($waarde['do'],3)] as $waarde2){
$content .= str_replace("_"," ",$waarde2['what']) . " " 
.str_replace(array("OUT_A","OUT_B","OUT_C","OUT_AB","OUT_AC","OUT_BC","OUT_ABC"),array("A","B","C","AvB","AvC","BvC","AvBvC"),$waarde2['X']) . "\n";
}
drawblock($img,1+$key,1,$title,$content,"trigger".$key);
//draw next program

}
//make img
imagejpeg($img,$filename,90);
imagedestroy($img);
$controlmap .= "</map>\n";
?>
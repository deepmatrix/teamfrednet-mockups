<?php
include("config.php");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>rover control</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<script src="ajaxHandle.js"></script>
<script>
function set_nickname(){
ajax_donwload_add("ajax.php?nickname="+document.getElementById("nickname").value,"logbook");
}
function who_is_online(){
ajax_donwload("ajax.php?online","who_online");
setTimeout("who_is_online()",10345);
}
function new_pre_program(){
ajax_donwload("ajax.php?pre_program=new","pre_program");
}
function pre_program(){
ajax_donwload("ajax.php?pre_program","pre_program");
}
function pre_program_id(){
ajax_donwload("ajax.php?pre_program="+document.getElementById("pre_program_id").value,"pre_program");
}
</script>
</head>

<body onload="who_is_online();pre_program();">
<img src="http://forum.xprize.frednet.org/styles/prosilver/imageset/site_logo.png" /> Lego mindstorm missions
<table width="868" border="0" cellpadding="0" cellspacing="0" bgcolor="#DDDDDD" class="compleet_border">
  <tr>
    <td width="864"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="border_buttom">
      <tr>
        <td width="200">Home</td>
        <td width="200"><a href="control.php">Mission control</a></td>
        <td width="200" bgcolor="#999999"><a href="pre_program.php">pre-program</a></td>
        <td width="200">logbook</td>
        <td width="*">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" >
      <tr>
        <td width="240" rowspan="1" valign="top" class="border_right">
          <table>
          <tr>
           <td>
           <div id="who_online">
            
            </div>
            Set Your nick Name:<br />
            <input type="text" id="nickname" value="<?php
            if(isset($_SESSION['nickname'])){echo $_SESSION['nickname']; } ?>" />
            <input type="button" value="Set" onclick="set_nickname();" />
           </td>
        </tr>
        </table>
        </td>
        
        <td>
        <input type="button" onclick="new_pre_program();" value="Create new pre Program" /><br />
        Enter ID: <input id="pre_program_id" /><input type="button" value="Load" onclick="pre_program_id();" /><br />
		<div id="pre_program">
        
    </div>
    <iframe id="hiddenframe" name="hiddenframe" src="about:blank" height="200" width="200" frameborder="0"></iframe></td>
  </tr>
  
</table>
</body>
</html>

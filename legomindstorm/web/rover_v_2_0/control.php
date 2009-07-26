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
function post_chat(){
ajax_donwload_add("ajax.php?message="+document.getElementById("input_chat").value,"logbook");
}
function post_chat_delay(){
ajax_donwload_add("ajax.php?message="+document.getElementById("input_chat").value+"&delay=30","logbook");
}
function chat_update(){
ajax_donwload_add("ajax.php?update","logbook");
setTimeout("chat_update();",1000);
}
function set_nickname(){
ajax_donwload_add("ajax.php?nickname="+document.getElementById("nickname").value,"logbook");
}
function who_is_online(){
//online
ajax_donwload("ajax.php?online","who_online");
//get sensors

setTimeout("who_is_online()",10345);
}
function sensors(){
ajax_donwload("ajax.php?sensor","sensors");
setTimeout("sensors()",4900);
}
</script>
</head>

<body onload="chat_update();who_is_online();sensors();">
<img src="http://forum.xprize.frednet.org/styles/prosilver/imageset/site_logo.png" /> Lego mindstorm missions
<table width="868" border="0" cellpadding="0" cellspacing="0" bgcolor="#DDDDDD" class="compleet_border">
  <tr>
    <td width="864"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="border_buttom">
      <tr>
        <td width="200">Home</td>
        <td width="200" bgcolor="#999999"><a href="control.php">Mission control</a></td>
        <td width="200"><a href="pre_program.php">pre-program</a></td>
        <td width="200">logbook</td>
        <td width="*">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="240" rowspan="2" valign="top" class="border_right">
          <table>
          <tr>
           <td>
           <div id="who_online">            </div>
            Set Your nick Name:<br />
            <input type="text" id="nickname" value="<?php
            if(isset($_SESSION['nickname'])){echo $_SESSION['nickname']; } ?>" />
            <input type="button" value="Set" onclick="set_nickname();" />           </td>
        </tr>
        </table>        </td>
        <td width="*"><table width="100%">
          <tr><td width="*"><div align="center"><img src="http://wiki.xprize.frednet.org/images/Lego_Picorover_Mockup_v1.1.JPG" /></div></td><td width="200" valign="top"><p><strong>Sensor data</strong></p>
                <div id="sensors"><p>Light sensor: 50%<br />
                disteans sensor: 0.4m</p>
                <p>Interface mode: command</p></div></td>
        </tr></table></td>
      </tr>
      <tr>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td bgcolor="#666666">Chat log</td>
          </tr>
          <tr>
            <td bgcolor="#CCCCCC" style="border:thin;">
            <div style="overflow:scroll; height:200px;" id="logbook">            </div>              </td>
          </tr>
          <tr>
            <td bgcolor="#999999"><label>
              <input name="input_chat" type="text" id="input_chat" size="50" maxlength="240" />
              <input type="button" name="button" id="button" value="post"onclick="post_chat();" />
              <a href="http://wiki.xprize.frednet.org/index.php/Lego_Mindstorms_command" target="_blank">How to enter commands</a></label></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>

<?php
include("config.php");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>rover control</title>
<style type="text/css">
<!--
body {
	background-color: #00CCCC;
}
.style1 {color: #FF0000}
.style2 {color: #00FF00}
.style4 {color: #996600}
-->
</style>
<script src="ajaxHandle.js"></script>
<script>
function post_chat(){
ajax_donwload_add("http://localhost/rover/ajax.php?message="+document.getElementById("input_chat").value,"logbook");
}
function post_chat_delay(){
ajax_donwload_add("http://localhost/rover/ajax.php?message="+document.getElementById("input_chat").value+"&delay=30","logbook");
}
function chat_update(){
ajax_donwload_add("http://localhost/rover/ajax.php?update","logbook");
setTimeout("chat_update();",1000);
}
function set_nickname(){
ajax_donwload_add("http://localhost/rover/ajax.php?nickname="+document.getElementById("nickname").value,"logbook");
}
function who_is_online(){
ajax_donwload("http://localhost/rover/ajax.php?online","who_online");
setTimeout("who_is_online()",10345);
}
</script>
</head>

<body onload="chat_update();who_is_online();">
<img src="http://forum.xprize.frednet.org/styles/prosilver/imageset/site_logo.png" /> Lego mindstorm missions
<table width="868" border="1" cellpadding="0" cellspacing="0" bgcolor="#DDDDDD">
  <tr>
    <td width="864"><table width="100%" border="1" cellspacing="0" cellpadding="0">
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
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td width="240" rowspan="2" valign="top">
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
        <form>
        <table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="49"><a title="in seconde" alt="in seconde">Delay</a></td>
            <td width="351">Command</td>
          </tr>
          <tr>
            <td><label>
              <input name="textfield" type="text" id="textfield" value="0" size="4" maxlength="4" />
            </label></td>
            <td><label>
              <input name="textfield10" type="text" id="textfield10" value="cmd motor A on" size="50" maxlength="250" />
            </label></td>
          </tr>
          <tr>
            <td><input name="textfield2" type="text" id="textfield2" value="2" size="4" maxlength="4" /></td>
            <td><input name="textfield11" type="text" id="textfield11" value="cmd motor AvC on" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield3" type="text" id="textfield3" value="5" size="4" maxlength="4" /></td>
            <td><input name="textfield12" type="text" id="textfield12" value="cmd motor AvC off" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield4" type="text" id="textfield4" value="10" size="4" maxlength="4" /></td>
            <td><input name="textfield13" type="text" id="textfield13" value="cmd motor AvC on" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield5" type="text" id="textfield5" value="10" size="4" maxlength="4" /></td>
            <td><input name="textfield14" type="text" id="textfield14" value="cmd program run 1" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield6" type="text" id="textfield6" value="15" size="4" maxlength="4" /></td>
            <td><input name="textfield15" type="text" id="textfield15" value="cmd motor AvC off" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield7" type="text" id="textfield7" size="4" maxlength="4" /></td>
            <td><input name="textfield16" type="text" id="textfield16" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield8" type="text" id="textfield8" size="4" maxlength="4" /></td>
            <td><input name="textfield17" type="text" id="textfield17" size="50" maxlength="250" /></td>
          </tr>
          <tr>
            <td><input name="textfield9" type="text" id="textfield9" size="4" maxlength="4" /></td>
            <td><input name="textfield18" type="text" id="textfield18" size="50" maxlength="250" /></td>
          </tr>
        </table> 
        <p>
          <label>
          <input type="submit" name="button" id="button" value="save" />
          </label>
          <label>
          <input type="submit" name="run" id="run" value="run" />
          </label>
        </p>
        </form>       </td>
      </tr>
      
    </table></td>
  </tr>
  
</table>
</body>
</html>

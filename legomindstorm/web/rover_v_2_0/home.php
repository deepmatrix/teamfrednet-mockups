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
<script src="script.js"></script>
</head>

<body onload="chat_update();who_is_online();sensors();">
<img src="http://forum.xprize.frednet.org/styles/prosilver/imageset/site_logo.png" /> Lego mindstorm missions
<table width="868" border="0" cellpadding="0" cellspacing="0" bgcolor="#DDDDDD" class="compleet_border">
  <tr>
    <td width="864"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="border_buttom">
      <tr>
        <td width="200" bgcolor="#999999"><a href="home.php">Home</a></td>
        <td width="200"><a href="control.php">Mission control</a></td>
        <td width="200"><a href="pre_program.php">pre-program</a></td>
        <td width="200"><a href="rover_program.php">Rover Program(not working)</a></td>
        <td width="*">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="240" rowspan="1" valign="top" class="border_right">
          <table>
          <tr>
           <td>
           <div id="who_online" class="who_is_online">            </div>
            Set Your nick Name:<br />
            <input type="text" id="nickname" value="<?php
            if(isset($_SESSION['nickname'])){echo $_SESSION['nickname']; } ?>"   />
            <input type="button" value="Set" id="nickname_button" onclick="set_nickname();" />           </td>
        </tr>
        </table>        </td>
        <td width="*">
		<table>
		<tr>
		<td>
		<h2>Lego Mindstorms remote,<br /><small>The Lunar mission present by Frednet</small></h2>
		</td>
		<td>
		<img src="teamfred_simple_logo.png" />
		</td>
		</tr>
		<tr>
		<td colspan="2">
		<p>We're in the process of putting a new proto-typing experiment together to leverage interest in educational outreach.  A wiki page is forming to describe the project - but it concerns creating '<a href="http://mindstorms.lego.com/" class="external text" title="http://mindstorms.lego.com/" rel="nofollow">mindstorms</a>' models of our three rover concepts and flowing down to the community the operations software for remote control. We are working on the description of tasks for this so it will have a strong resemblance to what we'll need for the actual system. The intent is to start proto-typing the technologies to demonstrate open source hardware development and team interactions.<br>
What I think we should do is something which demonstrates:<br>
</p>
<ol><li> A successful open source development model - teams working together do develop working designs that are distributed to the community
</li><li> A demonstration of remote operations - participants can use our rover software to control vehicles either locally or remotely
</li><li> Promotion of the team - we need more to establish our open source brand
</li><li> Leverage google efforts - we should stay ahead of Google opportunities
</li></ol>

<p>--<a href="http://wiki.xprize.frednet.org/index.php/User:Sean_Casey" title="User:Sean Casey">Sean Casey</a></p>
<p>
Some more infomation can be found on <a href="http://wiki.xprize.frednet.org/index.php/Portal:Lego_Mindstorms">the wiki of frednet</a></p>

</p>
</td>
</tr>
</table>
		</td>
      </tr>
    </table></td>
  </tr>
</table>
<center>
&copy; 2009 Frednet Group
</center>
</body>
</html>

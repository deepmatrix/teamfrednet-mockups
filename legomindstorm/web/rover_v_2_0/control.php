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
        <td width="200"><a href="home.php">Home</a></td>
        <td width="200" bgcolor="#999999"><a href="control.php">Mission control</a></td>
        <td width="200"><a href="pre_program.php">pre-program</a></td>
        <td width="200"><a href="rover_program.php">Rover Program(not working)</a></td>
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
           <div id="who_online" class="who_is_online">            </div>
            Set Your nick Name:<br />
            <input type="text" id="nickname" value="<?php
            if(isset($_SESSION['nickname'])){echo $_SESSION['nickname']; } ?>"   />
            <input type="button" value="Set" id="nickname_button" onclick="set_nickname();" />           </td>
        </tr>
        </table>        </td>
        <td width="*"><table width="100%">
          <tr><td width="*"><div align="center">
		  <OBJECT id='mediaPlayer' width="320" height="285" 
      classid='CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95' 
      codebase='http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701'
      standby='Loading Microsoft Windows Media Player components...' type='application/x-oleobject'>
      <param name='fileName' value="<?php echo $stream_url; ?>">
      <param name='animationatStart' value='true'>
      <param name='transparentatStart' value='true'>
      <param name='autoStart' value="true">
      <param name='showControls' value="true">
      <param name='loop' value="true">
      <EMBED type='application/x-mplayer2'
        pluginspage='http://microsoft.com/windows/mediaplayer/en/download/'
        id='mediaPlayer' name='mediaPlayer' displaysize='4' autosize='-1' 
        bgcolor='darkblue' showcontrols="true" showtracker='-1' 
        showdisplay='0' showstatusbar='-1' videoborder3d='-1' width="320" height="285"
        src="<?php echo $stream_url; ?>" autostart="true" designtimesp='5311' loop="true">
      </EMBED>
      </OBJECT>

		  </div></td><td width="200" valign="top"><p><strong>Sensor data</strong></p>
                <div id="sensors">Please wait...</div></td>
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
              <input name="input_chat" type="text" id="input_chat" size="50" maxlength="240" onKeyDown="if(event.keyCode==13){post_chat();}" />
              <input type="button" name="button" id="button" value="post" onclick="post_chat();" />
              <a href="http://wiki.xprize.frednet.org/index.php/Lego_Mindstorms_command" target="_blank">How to enter commands</a></label></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<center>
&copy; 2009 Frednet Group
</center>
</body>
</html>

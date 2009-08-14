<?php
include("menu.php");
include("content.php");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>TeamFREDNET MindStorm Lunar Rover Template</title>
<link rel="stylesheet" type="text/css"
	href="template/default/style/main.css" />
<script type="text/javascript" language="javascript"
	src="template/default/script/menu.js"></script>
<script type="text/javascript" language="javascript"
	src="template/default/script/ajax.js"></script>
<script type="text/javascript" language="javascript" src="script.js"></script>
</head>
<body onload="initButtons();watch_page();check_url();who_is_online();">

<div id="heading"><a href="http://www.teamfrednet.org/"> <img
	alt="teamFREDNET logo" src="img/teamfred_simple_logo.png"
	class="head_logo" /> </a> teamFREDNET MindStorm Lunar Rover Portal</div>

<div id="left_col"><?php
build_menu();
?></div>

<div id="center_col"><?php
build_content();
?></div>
<div id="logbook"><!-- testing --></div>
<div id="right_col">
<div class="rankBox">
<h4 style="padding: 0; margin: 0;">My Ranking</h4>
<ul
	style="list-style-type: none; background-color: #BC99FF; padding: 0; font-size: 10pt">
	<li>Worldwide: 134 (17%)</li>
	<li>Regional: 20 (23%)</li>
</ul>
</div>
<div class="rankBox">
<h4 style="padding: 0; margin: 0;">Regional Rankings</h4>
<img alt="World Map" title="World Map" id="RegionMap"
	style="width: 100%;" src="img/regional_map.gif" /></div>
<div class="rankBox" id="login">Set Your nick Name:<br />
nickname:<input type="text" id="nickname"
	value="<?php
            if(isset($_SESSION['nickname'])){echo $_SESSION['nickname']; } ?>" /><br />

<input type="button" value="Login" id="nickname_button"
	onclick="set_nickname();" /></div>
<div class="rankBox">
<div id="who_online" class="who_is_online"></div>
</div>
</div>

<div id="footer">
<table class="footTabs">
	<tr>
		<td class="footer"><a class="hilite2"
			onclick="update_url('static_about');">About</a></td>
		<td class="footer"><a class="hilite2"
			onclick="update_url('static_faq');">FAQ</a></td>
		<td class="footer"><a class="hilite2"
			onclick="update_url('static_press_inquiries');">Press Inquiries</a></td>
		<td class="footer"><a class="hilite2"
			onclick="update_url('static_contact');">Contact</a></td>
		<td class="footer"><a href="http://validator.w3.org/check?uri=referer">
		<img src="http://www.w3.org/Icons/valid-xhtml10-blue"
			alt="Valid XHTML 1.0 Transitional" style="border: 0; height: 20px" />
		</a></td>
		<td class="footer"><a
			href="http://jigsaw.w3.org/css-validator/check/referer"> <img
			style="border: 0; height: 20px"
			src="http://jigsaw.w3.org/css-validator/images/vcss-blue"
			alt="Valid CSS!" /> </a></td>
	</tr>
</table>
</div>
&copy; 2009, this software is relaesed under GNU GPLv3.
</body>
</html>

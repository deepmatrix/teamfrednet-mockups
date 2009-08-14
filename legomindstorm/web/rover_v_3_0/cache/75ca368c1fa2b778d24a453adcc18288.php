<?php
$temp='
			<p>The command that are use to control rover are text based. All commands "should" work in version 0.1.99.3 defined on this page.
</p><p>All commands start with cmd
</p><p><br />
</p>
<table id="toc" style="display: none;" class="toc" summary="Contents"><tr><td><div id="toctitle"><h2>Contents</h2></div>
<ul>
<li class="toclevel-1"><a href="#Motor"><span class="tocnumber">1</span> <span class="toctext">Motor</span></a></li>
<li class="toclevel-1"><a href="#Sensor"><span class="tocnumber">2</span> <span class="toctext">Sensor</span></a>
<ul>
<li class="toclevel-2"><a href="#Result"><span class="tocnumber">2.1</span> <span class="toctext">Result</span></a></li>
</ul>
</li>
<li class="toclevel-1"><a href="#Program"><span class="tocnumber">3</span> <span class="toctext">Program</span></a>
<ul>
<li class="toclevel-2"><a href="#Rover_Program_list"><span class="tocnumber">3.1</span> <span class="toctext">Rover Program list</span></a></li>
<li class="toclevel-2"><a href="#Make_of_pre-program"><span class="tocnumber">3.2</span> <span class="toctext">Make of pre-program</span></a></li>
</ul>
</li>
<li class="toclevel-1"><a href="#Example"><span class="tocnumber">4</span> <span class="toctext">Example</span></a></li>
<li class="toclevel-1"><a href="#command_test_result"><span class="tocnumber">5</span> <span class="toctext">command test result</span></a></li>
</ul>
</td></tr></table><script type="text/javascript"> if (window.showTocToggle) { var tocShowText = "show"; var tocHideText = "hide"; showTocToggle(); } </script>
<a name="Motor" id="Motor"></a><h2> <span class="mw-headline"> Motor </span></h2>
<p>_motor_id_ port where the motor is connect to(A,B,C,All) or multi-id(AvC,BvC,CvA)<br />
_speed_ value in the range -100 to 100 (degree/s)<br />
_degrees_ value in the range -100 to 100 (degrees)
</p>
<pre>
cmd motor _motor_id_ on
cmd motor _motor_id_ off
cmd motor speed _motor_id_ _speed_
cmd motor degrees _motor_id_ _degrees_
</pre>
<a name="Sensor" id="Sensor"></a><h2> <span class="mw-headline"> Sensor </span></h2>
<p>_sensor_id_ port where the sensor is connect to(1,2,3 and 4)
</p>
<pre>
cmd sensor value _sensor_id_
</pre>
<a name="Result" id="Result"></a><h3> <span class="mw-headline"> Result </span></h3>
<table class="wikitable" border="1" cellspacing="1" cellpadding="7" style="border-collapse:collapse;">

<tr>
<td>Type sensor
</td><td>Result
</td></tr>
<tr>
<td>Light Sensor
</td><td>0-100%
</td></tr>
<tr>
<td>Touch Sensor
</td><td>Four states: Pressed, Released, bumped and none
</td></tr>
<tr>
<td>Sound Sensor
</td><td>0-100%
</td></tr>
<tr>
<td>Ultrasonic Sensor
</td><td>0-255 cm
</td></tr>
<tr>
<td>Color Sensor MS1038
</td><td>
</td></tr>
<tr>
<td>Compass Sensor MS1034
</td><td>0-360 degrees
</td></tr>
<tr>
<td>Accelerometer Sensor MS1040
</td><td>
</td></tr>
<tr>
<td>Gyroscopic Sensor MS1044
</td><td> -360 - 360 degrees/s
</td></tr>
<tr>
<td>Infrared Sensor MS1042
</td><td> -180 - 180 degrees
</td></tr></table>
<p>Default refresh rate is 5 seconden.
</p>
<a name="Program" id="Program"></a><h2> <span class="mw-headline"> Program </span></h2>
<p>_pre_program_id_ Program id(1 - 2,147,483,647)<br />
_rover_program_id_ Program name with out extension(max length 15 chars, no space allowed)
</p>
<pre>
cmd run program _pre_program_id_
cmd run rover program _rover_program_id_
cmd stop rover program
</pre>
<a name="Rover_Program_list" id="Rover_Program_list"></a><h3> <span class="mw-headline"> Rover Program list </span></h3>
<table class="wikitable" border="1" cellspacing="1" cellpadding="7" style="border-collapse:collapse;">

<tr>
<td><b>Program ID</b>
</td><td><b>function</b>
</td></tr>
<tr>
<td>run_20_cm
</td><td>Will set motor AvBvC off when Ultrasonic Sensor distantes is less then 20 cm
</td></tr>
<tr>
<td>run_bump
</td><td>Will set motor AvBvC off when Touch Sensor is Pressed or bumped
</td></tr>
<tr>
<td>run_3_s
</td><td>Will set motor AvBvC off after 3 secondens
</td></tr></table>
<p><b>-&gt; Please add other simple program\'s</b> <br />
There can be add extra program\'s depending on the course.
</p>
<a name="Make_of_pre-program" id="Make_of_pre-program"></a><h3> <span class="mw-headline"> Make of pre-program </span></h3>
<ol><li> Go to the webinterface
</li><li> Hit pre-program
</li><li> Hit "Create new pre Program"
</li><li> You will see "Your Program ID: X", remember this id.
</li><li> Enter a delay for first command to send in seconds and enter the command after it.
</li><li> Enter a delay for second command to send in seconds and enter the command after it.
</li><li> ....
</li><li> hit save
</li><li> go to "Mission control"
</li><li> when your are in control enter following command:
</li></ol>
<pre>
cmd run program X
</pre>
<p>replace X with your ID.
</p><p><a href="/wiki/index.php/File:Mindstorm_draft_web_pre_program_0_2.JPG" class="image" title="Image:Mindstorm_draft_web_pre_program_0_2.JPG"><img alt="Image:Mindstorm_draft_web_pre_program_0_2.JPG" src="/wiki/images/f/f4/Mindstorm_draft_web_pre_program_0_2.JPG" width="896" height="460" border="0" /></a>
</p>
<a name="Example" id="Example"></a><h2> <span class="mw-headline"> Example </span></h2>
<pre>
cmd motor speed A 15
cmd motor speed B 20
cmd motor AvB on
cmd motor AvB off
cmd sensor value 1
   50 cm
cmd motor speed A 20
cmd motor AvB on
cmd run rover program 3
</pre>
<a name="command_test_result" id="command_test_result"></a><h2> <span class="mw-headline"> command test result </span></h2>
<table class="wikitable" border="1" cellspacing="1" cellpadding="7" style="border-collapse:collapse;">
<tr>
<td>command
</td><td>result
</td><td>pass
</td></tr>
<tr>
<td>cmd motor AvBvC on
</td><td>?
</td><td>
</td></tr>
<tr>
<td>cmd sensor value 2
</td><td>?
</td><td>
</td></tr></table>

<!-- 
NewPP limit report
Preprocessor node count: 28/1000000
Post-expand include size: 0/2097152 bytes
Template argument size: 0/2097152 bytes
Expensive parser function count: 0/100
-->

<!-- Saved in parser cache with key rover-wiki_:pcache:idhash:10-0!1!0!!en!2 and timestamp 20090813201229 -->
';
$time=1250194349;
?>
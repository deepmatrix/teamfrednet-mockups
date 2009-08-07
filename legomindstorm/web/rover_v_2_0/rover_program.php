<?php
session_start();
// Make rover programs
include("script_build.php");
//make img
include("build_image.php");
/*
*/
?>
<html>
<head>
<script>
function popup_chose(id){
var result = prompt("What do you want to do:\nA) Change trigger point\nB) Edit reaction\nC) Add a IF block\nD) Remove the trigger","");
if(result == "D"){
//del
window.location.href = 'del.php?id='+id;
}
}
</script>
</head>
<body>
just dev:
<table>
<tr>
<td>
<form action="?build=trigger" method="POST" />
line<input name="line" /><br />
trigger: <select name="trigger">
<option value="Y_g_X">When Y greater than X
<option value="Y_s_X">When Y smaller than X
<option value="Y_e_X">When Y same as X
<option value="Y_ne_X">When Y not same as X
</select>
<br />
X: <input name="X" /><br />
Y: 
<select name="Y">
<option value="SENSOR_1">Sensor 1: Ultrasonic distances in cm
<option value="SENSOR_2">Sensor 2: touch 0 or 1
<option value="SENSOR_3">Sensor 3: Sound in dB(A)
<option value="SENSOR_4">Sensor 4: Light in %
<option value="time">runtime in seconds
</select>
<br />
Then run:
<select name="do">
<option value="sub1">subProgram 1
<option value="sub2">subProgram 2
<option value="sub3">subProgram 3
</select>
<br />
<input type="submit" value="Add to rover program" />
</form>
</td>
<td>
<form action="?build=sub" method="POST" />
subProgram ID<input name="ID" /><br />
line<input name="line" /><br />
what: <select name="what">
<option value="motor_on">set motor X on
<option value="motor_off">set motor X off
<option>set motor X speed Y
<option>set var X is Y
<option>When Y greater than X then Z
<option>When Y smaller than X then Z
<option>When Y same as X then Z
<option>When Y not same as X then Z
</select><br />
X: <select name="X">
<option value="OUT_A">motor A
<option value="OUT_B">motor B
<option value="OUT_C">motor C
<option value="OUT_AB">motor AvB
<option value="OUT_AC">motor AvC
<option value="OUT_BC">motor BvC
<option value="OUT_ABC">motor AvBvC
</select><br />
Y: <input name="Y" /><br />
Z: <select name="do">
<option value="sub1">subProgram 1
<option value="sub2">subProgram 2
<option value="sub3">subProgram 3
</select><br />
<input type="submit" value="add" />
</form>
</td>
<td>

</td>
</tr>
</table>
The real
<img src="<?php echo $filename . "?random=".rand() . microtime(); ?>" usemap="#controlmap" />
<?php echo $controlmap; ?>
</body>
</html>
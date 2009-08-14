<?php
function create_player_small($id,$clip_id){
	?>
<object width="123" height="100" id="smPlayer_<?php echo $id; ?>">
	<param name="movie"
		value="http://www.youtube.com/v/<?php echo $clip_id; ?>&hl=en&fs=1&rel=0"></param>
	<param name="allowFullScreen" value="true"></param>
	<param name="allowscriptaccess" value="always"></param>
	<embed
		src="http://www.youtube.com/v/<?php echo $clip_id; ?>&hl=en&fs=1&rel=0"
		type="application/x-shockwave-flash" allowscriptaccess="always"
		allowfullscreen="true" width="123" height="100"></embed> </object>
<div id="vidSel2" class="vidButton"
	onclick="clipToMain('<?php echo $clip_id; ?>');">Send to Main</div>
	<?php
}
?>
<table style="width: 85%">
	<tr>
		<td><?php create_player_small(1,"hgstIKqqWbQ"); ?></td>

		<td><?php create_player_small(2,"uHLZ-HQKS1k"); ?></td>
		<td><?php create_player_small(2,"PjxRfWpzXQk"); ?></td>

	</tr>

	<tr>
		<td colspan="2">
		<div id="mainPlayer"><object width="425" height="344" id="mainPlayer"
			style="margin-left: 1em;">
			<param name="movie"
				value="http://www.youtube.com/v/uHLZ-HQKS1k&hl=en&fs=1&"></param>
			<param name="allowFullScreen" value="true"></param>
			<param name="allowscriptaccess" value="always"></param>
			<param name="autoplay" value="true"></param>
			<embed name="mainPlayer"
				src="http://www.youtube.com/v/uHLZ-HQKS1k&hl=en&fs=1&"
				type="application/x-shockwave-flash" allowscriptaccess="always"
				allowfullscreen="true" width="425" height="344"></embed> </object></div>
		</td>
		<td><strong>sensor data:</strong>
		<div id="sensors">Please wait</div>
		</td>
	</tr>
	<tr>
		<td colspan="3" bgcolor="#CCCCCC" style="border: thin;">
		<div style="overflow: auto; height: 200px; color: #FFFFFF;"
			id="logbook"></div>
		</td>
	</tr>
	<td colspan="3" bgcolor="#AAAAAA" style="border: thin;"><input
		name="input_chat" type="text" id="input_chat" size="50"
		maxlength="240" onKeyDown="if(event.keyCode==13){post_chat();}" /> <input
		type="button" name="button" id="button" value="post"
		onclick="post_chat();" /> <a href="#static_command" target="_blank">How
	to enter commands</a></td>
	</tr>
</table>

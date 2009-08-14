<?php
/**
 *
 */
/**
 * This function will build the menu
 * @return No
 */
function build_menu(){
	//some demo data
	$menu_data[1]['name'] = "Frednet";
	$menu_data[1]['submenu'][1]['name'] = "Home";
	$menu_data[1]['submenu'][1]['page_namespace'] = "";
	$menu_data[1]['submenu'][2]['name'] = "Who is Frednet?";
	$menu_data[1]['submenu'][2]['page_namespace'] = "static_who_is_frednet";
	$menu_data[1]['submenu'][3]['name'] = "The moon";
	$menu_data[1]['submenu'][3]['page_namespace'] = "static_moon";
	$menu_data[2]['name'] = "Rover control";
	$menu_data[2]['submenu'][1]['name'] = "Mission Control";
	$menu_data[2]['submenu'][1]['page_namespace'] = "file_mission_control";
	$menu_data[2]['submenu'][2]['name'] = "Pre Program";
	$menu_data[2]['submenu'][2]['page_namespace'] = "file_pre_program";
	$menu_data[2]['submenu'][3]['name'] = "Rover Program";
	$menu_data[2]['submenu'][3]['page_namespace'] = "file_rover_program";
	$menu_data[2]['submenu'][4]['name'] = "FAQ";
	$menu_data[2]['submenu'][4]['page_namespace'] = "faq";
	$upper = 1;
	while(isset($menu_data[$upper])){
		//open menu
		$name_upper = $menu_data[$upper]['name'];
		?>
<div class="navButton" id="button_<?php echo $upper; ?>"
	style="top: 80px;" onclick="revealMenu(<?php echo $upper; ?>);"><?php echo $name_upper; ?>
<div class="navSubMenu" id="menu_<?php echo $upper; ?>"
	style="visibility: hidden;">
<ul class="Menu">
<?php
$lower = 1;
while(isset($menu_data[$upper]['submenu'][$lower])){
	$name_lower = $menu_data[$upper]['submenu'][$lower]['name'];
	$namespace_lower = $menu_data[$upper]['submenu'][$lower]['page_namespace'];
	?>
	<li><a class="hilite"
		onclick="update_url('<?php echo $namespace_lower; ?>');"><?php echo $name_lower; ?></a>
	</li>
	<?php
	$lower++;
}
?>
</ul>

</div>
</div>
<?php
$upper++;
	}
}
?>

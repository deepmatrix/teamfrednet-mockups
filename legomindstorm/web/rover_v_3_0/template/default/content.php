<?php
/**
 * This function will build the page content
 * @return content string
 */
function build_content(){
	global $template_name,$wiki_server,$wiki_prefixed,$wiki_cache_time;
	if(!isset($_GET['namespace'])){
		//please wait
		echo("<img src=\"template/" . $template_name . "/img/ajax-loader.gif\" alt=\"Loading\" />");
		return 0;
	}
	//undefined
	if($_GET['namespace'] == "undefined" || $_GET['namespace'] == ""){
		//load main
		echo wiki_get($wiki_server."index.php/{$wiki_prefixed}web_main");
		return 0;
	}
	//get namespace
	$namespace = $_GET['namespace'];
	//get the page
	if(substr($namespace,0,7) == "static_"){
		//get it from db(wiki)
		echo wiki_get($wiki_server."index.php/{$wiki_prefixed}" . substr($namespace,7));

	}
	if(substr($namespace,0,5) == "file_"){
		// check if allowed
		$allowed['mission_control'] = true;
		$allowed['rover_program'] = true;
		$allowed['pre_program'] = true;
		if(isset($allowed[substr($namespace,5)])){
			include(substr($namespace,5) . ".php");
		}else{
			echo "ERROR!!!!<br />Not allowed!";
		}
	}
}
function wiki_get($url){
	global $wiki_cache_time, $wiki_server;
	$wiki_server_no_slash = substr($wiki_server,0,strlen($wiki_server)-1);
	//check for cache
	if(file_exists("cache/" . md5($url) . ".php")){
		include("cache/" . md5($url) . ".php");
		//check time
		if(($time + 3600*$wiki_cache_time) > date("U")){
			return $temp;
		}
	}
	$temp = explode("<!-- start content -->",implode("",file($url)));
	$temp = explode("<div class=\"printfooter\">",$temp[1]);
	//remove edit button
	$temp = preg_replace("/\<span class=\"editsection\"\>(.*?)\<\/span\>/is","",$temp[0]);
	//remove edit button
	$temp = str_replace('<table id="toc" class="toc" summary="Contents">','<table id="toc" style="display: none;" class="toc" summary="Contents">',$temp);
	//update inner wiki links
	//$temp = "<a href=\"/wiki/index.php/test\" title=\"lol\">lol</a>";
	$temp = preg_replace("/<a href=\"\/wiki\/index.php\/([a-zA-Z0-9_]*)\" title=\"(.*?)\">(.*?)\<\/a\>/i", "<a href=\"#static_\\1\">\\2</a>", $temp);
	// update url img
	$temp = str_replace("href=\"/index.php/Image:", "href=\"$wiki_server_no_slash/index.php/Image:", $temp);
	// update img
	$temp = str_replace("\"/images/", "\"$wiki_server_no_slash/images/", $temp);
	//save to cache
	$data = "<?php\n\$temp=".var_export($temp,true).";\n\$time=".date("U").";\n?>";
	$fh = fopen("cache/".md5($url).".php", 'w') or die("can't open file");

	fwrite($fh, $data);

	fclose($fh);
	return $temp;
}
?>
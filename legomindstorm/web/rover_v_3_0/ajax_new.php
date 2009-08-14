<?php
$start = date("U") + microtime();
include("config.php");
include("template/" . $template_name . "/content.php");
build_content();
$end = date("U") + microtime();
echo "<br />\n" . round((($end-$start)*1000),1) . " ms build time.\n";
?>
<?php
include("config.php");
//ajax.php
if(isset($_GET['message'])){
//check type message
$message = $_GET['message'];
if(substr($message,0,3)=="cmd"){
$type = "cmd";
}else{
$type = "chat";
}
//set time 1234567890.1232
$time = date("U") + microtime();
if(isset($_GET['delay'])){
$time += $_GET['delay'];
}
//who
if(isset($_SESSION['user_ID'])){
$who_ID = $_SESSION['user_ID'];
}else{
$who_ID  = 0 ;
$_SESSION['user_ID'] = $who_ID;
}
$sql = "SELECT * FROM `users` WHERE `ID`=$who_ID";
			 $result = mysql_query($sql);
$row = mysql_fetch_array( $result );
if(!($row['start_control'] < date("U") && $row['end_control'] > date("U")) && $type == "cmd")
{
echo "Your aren't allow to summit commands.<br />\n";
die();
}
//make sql
$sql = "INSERT `log_current_session` SET `type`='$type', `when`='$time', `message`='".substr($message,0,245)."', `who_ID`='$who_ID'";
mysql_query($sql) or die(mysql_error() . "SQL: $sql");
//update last online time
$sql = "UPDATE `users` SET `last`=" . date("U")." WHERE `ID`='{$_SESSION['user_ID']}'";
$result = mysql_query($sql);
}
if(isset($_GET['update'])){
//check if someone has acces
$sql = "SELECT * FROM `users` WHERE `last`>".(time()-$user_offline_time) . " AND `start_control` < ".time()." AND `end_control` > ".time()."";
			 $result = mysql_query($sql);
			 if(mysql_num_rows($result)==0){
			 //select new controler(
			 //to do: add check for online
			 $sql = "SELECT * FROM `users` WHERE `last`>".(time()-$user_offline_time) . " ORDER BY `end_control` ASC LIMIT 0,15";
			 $result = mysql_query($sql);
			 $row = mysql_fetch_array( $result );
			 $wait = mysql_num_rows($result);
			 if(count($row)>1){
			 //to do: based on the number waiting set control time
			 $sql = "UPDATE `users` SET `start_control`=".time().", `end_control`=".(time()+60*5)." WHERE `ID`={$row['ID']}";
			 mysql_query($sql);
echo date("H:i:s ") . "system: {$row['nickname']} is now in control.<br />\n";
			 }
			 }
			 //update
$last_id = $_SESSION['last_time_check'];
$now = (date("U") + microtime());
$sql = "SELECT * FROM `log_current_session`,`users` WHERE `log_current_session`.`who_ID`=`users`.`ID` AND `log_current_session`.`when`>=$last_id AND `log_current_session`.`when` < $now AND ((`log_current_session`.`status`!='' && `log_current_session`.`type`='cmd')||(`log_current_session`.`type`='chat')) ORDER BY `log_current_session`.`when` ASC";
$result = mysql_query($sql);
while($row = mysql_fetch_array( $result )){
//print_r($row);
echo date("H:i:s ") . $row['nickname'] . ": " . $row['message'];
if($row['type'] == 'cmd'){
echo  " <strong>Result: " . $row['status']."</strong>";
}
echo "<br />\n";
}
$_SESSION['last_time_check'] = $now;

}
if(isset($_GET['online'])){


?>
<table>
             <tr>
              <td colspan="2">
                <strong>Who is online?</strong></td>
             </tr>
             <?php
			 $sql = "SELECT * FROM `users` WHERE `last`>".(time()-$user_offline_time);
			 $result = mysql_query($sql);
while($row = mysql_fetch_array( $result )){
			 ?>
             <tr>
              <td>
               <img src="http://wiki.xprize.frednet.org/images/LegoMindstorms_pic160416BB-15E0-4353-904D-8E210258A3F4_v1.1.JPG" width="50" height="50" />
              </td>
              <td>
        <span class="style1"><?php echo $row['nickname']; ?></span><br />
<?php
if($row['start_control'] < date("U") && $row['end_control'] > date("U"))
{
echo "This user is in control.";
}
if((date("U") - $row['end_control']) < 1000 && (date("U") - $row['end_control']) > 0)
{
echo "This user was in control.";
}
if($row['start_control'] > date("U"))
{
echo "This user will be in control in ".(($row['start_control']-date("U"))/60)." minute.";
}
?>
              
              </td>
             </tr>
             <?php
			 }
			 ?>
             
            </table>
<?php
}
if(isset($_GET['nickname'])){
$sql = "INSERT `users` SET `nickname`='{$_GET['nickname']}', `last`=".time()."";
mysql_query($sql);
$_SESSION['user_ID'] = mysql_insert_id();
}
if(isset($_GET['sensor'])){
$sql = "SELECT * FROM `sensors` WHERE 1 ORDER BY `when` DESC LIMIT 0,1";
			 $result = mysql_query($sql);
$row = mysql_fetch_array( $result );
echo nl2br($row['result']);
}
?>
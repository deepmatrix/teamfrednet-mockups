nickname_update = false;
function post_chat(){
ajax_donwload_add("ajax.php?message="+escape(document.getElementById("input_chat").value),"logbook");
document.getElementById("input_chat").value = '';
}
function post_chat_delay(){
ajax_donwload_add("ajax.php?message="+escape(document.getElementById("input_chat").value)+"&delay=30","logbook");
}
var chat_update_enable = false;
function chat_update(){
if(chat_update_enable == true){
ajax_donwload_add("ajax.php?update","logbook");
setTimeout("chat_update();",1000);//500 ms
}
}
function set_nickname(){
ajax_donwload("ajax.php?nickname="+document.getElementById("nickname").value,"login");
document.getElementById('login').innerHTML = "<img src=\"template/default/img/ajax-loader.gif\" alt=\"Loading\" />";
setTimeout("who_is_online()",800);//800ms
}
function clipToMain(id){
ajax_donwload("ajax.php?clip="+id,"mainPlayer");
}
function who_is_online(){
//online
ajax_donwload("ajax.php?online","who_online");
if(document.getElementById("nickname_button").value != 'Wait..'){
setTimeout("who_is_online()",5045);//5 secondes
}
document.getElementById("nickname_button").value = 'Set';
}
var sensor_enable = false;
function sensors(){
if(sensor_enable == true){
ajax_donwload("ajax.php?sensor","sensors");
setTimeout("sensors()",1950);//2 seconds
}
}
function new_pre_program(){
ajax_donwload("ajax.php?pre_program=new","pre_program");
}
var pre_program_enable = false;
function pre_program(){
if(pre_program_enable == true){
ajax_donwload("ajax.php?pre_program","pre_program");
}
}
function pre_program_id(){
ajax_donwload("ajax.php?pre_program="+document.getElementById("pre_program_id").value,"pre_program");
}
function watch_page(){
//sensor
if(namespace == "file_mission_control" && sensor_enable != true){
sensor_enable = true;
sensors();
}
if(namespace != "file_mission_control" && sensor_enable == true){
sensor_enable = false;
}
//chat_update();
if(namespace == "file_mission_control" && chat_update_enable != true){
chat_update_enable = true;
chat_update();
}
if(namespace != "file_mission_control" && chat_update_enable == true){
chat_update_enable = false;
}
//pre program
if(namespace == "file_pre_program" && pre_program_enable != true){
pre_program_enable = true;
pre_program();
}
if(namespace != "file_pre_program" && pre_program_enable == true){
pre_program_enable = false;
}
setTimeout("watch_page();",1850);//2 seconds
}
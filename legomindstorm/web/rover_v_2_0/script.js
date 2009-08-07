nickname_update = false;
function post_chat(){
ajax_donwload_add("ajax.php?message="+escape(document.getElementById("input_chat").value),"logbook");
document.getElementById("input_chat").value = '';
}
function post_chat_delay(){
ajax_donwload_add("ajax.php?message="+escape(document.getElementById("input_chat").value)+"&delay=30","logbook");
}
function chat_update(){
ajax_donwload_add("ajax.php?update","logbook");
setTimeout("chat_update();",500);//500 ms
}
function set_nickname(){
ajax_donwload_add("ajax.php?nickname="+document.getElementById("nickname").value,"logbook");
document.getElementById("nickname_button").value = 'Wait..';
setTimeout("who_is_online()",800);//800ms
}
function who_is_online(){
//online
ajax_donwload("ajax.php?online","who_online");
if(document.getElementById("nickname_button").value != 'Wait..'){
setTimeout("who_is_online()",5045);//5 secondes
}
document.getElementById("nickname_button").value = 'Set';
}
function sensors(){
ajax_donwload("ajax.php?sensor","sensors");
setTimeout("sensors()",1950);//2 seconds
}
function new_pre_program(){
ajax_donwload("ajax.php?pre_program=new","pre_program");
}
function pre_program(){
ajax_donwload("ajax.php?pre_program","pre_program");
}
function pre_program_id(){
ajax_donwload("ajax.php?pre_program="+document.getElementById("pre_program_id").value,"pre_program");
}
function sensors(){
ajax_donwload("ajax.php?sensor","sensors");
setTimeout("sensors()",1950);//2 seconds
}
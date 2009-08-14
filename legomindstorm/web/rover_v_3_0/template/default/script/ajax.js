var namespace = "";
function getXMLHttp()
{
	var xmlHttp;

	//Kijken welke browser word gebruikt
	try
	  {
	  //Firefox, Opera 8.0+, Safari
	  xmlHttp=new XMLHttpRequest();
	  }
	catch (e)
	  {
	  //Internet Explorer
	  try
		{
			xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		}
	 catch (e)
		{
		try
		  {
			xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
	 catch (e)
		{
		  alert("De browser ondersteund geen ajax!");
		  return false;
		  }
		}
	  }	  
	  return xmlHttp;
}
xmlHttp = getXMLHttp();

function ajax_donwload(url, divId)
{
	
		
//xmlHttp = getXMLHttp();

if(xmlHttp.readyState == 0 || xmlHttp.readyState == 4){

  xmlHttp.onreadystatechange=function()
    {
    switch(xmlHttp.readyState)
	{
		
		case 1:		
			//alert(divId);
			//document.getElementById(divId).innerHTML = "";
			//document.getElementById(divId).style.filter="alpha(opacity=50)";			
			//document.getElementById(divId).style.opacity=".50";
			break;
		
		case 2:	
			//alert("state 2");
			break;	
		case 3:			
			//alert("state 3");	
			break;
		case 4:	
		//alert(xmlHttp.status);
		if(xmlHttp.status==200){
			Output = xmlHttp.responseText;
			document.getElementById(divId).innerHTML = Output;
		}else{
			document.getElementById(divId).innerHTML = 'Error: File not found.<br />\n';
		}
			//alert("state 4");
			break;		
		default:
			alert("Error!");
			break;
	}
    }
	
  xmlHttp.open("GET",url,true);
xmlHttp.send(null);

}else{
		setTimeout("ajax_donwload('"+url+"', '"+divId+"')",100);
		//alert("ajax_donwload('"+url+"', '"+divId+"')");
}
  }
  function ajax_donwload_add(url, divId)
{
	


if(xmlHttp.readyState == 0  || xmlHttp.readyState == 4){

  xmlHttp.onreadystatechange=function()
    {
    switch(xmlHttp.readyState)
	{
		
		case 1:		
			//alert(divId);
			//document.getElementById(divId).innerHTML = "";
			//document.getElementById(divId).style.filter="alpha(opacity=50)";			
			//document.getElementById(divId).style.opacity=".50";
			break;
		
		case 2:	
			//alert("state 2");
			break;	
		case 3:			
			//alert("state 3");	
			break;
		case 4:	
		//alert(xmlHttp.status);
		if(xmlHttp.status==200){
			Output = xmlHttp.responseText;
			if(Output.length > 0){
			document.getElementById(divId).innerHTML = Output + document.getElementById(divId).innerHTML;
			}
		}else{
			document.getElementById(divId).innerHTML += 'Error: File not found.<br />\n';
		}
			//alert("state 4");
			break;		
		default:
			alert("Error!");
			break;
	}
    }
	
  xmlHttp.open("GET",url,true);
xmlHttp.send(null);

  	
}else{
	setTimeout("ajax_donwload_add('"+url+"', '"+divId+"')",200);
			//alert("ajax_donwload('"+url+"', '"+divId+"')");
}
}
function update_main_frame(namespace){
	window.location.href = "#" + namespace;
	ajax_donwload('ajax_new.php?namespace=' + namespace,'center_col');
}
function update_url(namespace){
	window.location.href = "#" + namespace;
	
}
function loadmain(){
//get url
list = window.location.href.split('#');
namespace = list[1];
//update status
//document.getElementById(divId).innerHTML = '<img src="template/default/img/ajax-loader.gif" />';
update_main_frame(namespace);

}
var url;
function check_url(){
if(url != window.location.href){
loadmain();
url = window.location.href;
}
setTimeout("check_url();",50);
}
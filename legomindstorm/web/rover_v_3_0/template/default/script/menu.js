var buttonLoc = new Array();
var numberOfMenuItems = 2;

function initButtons() {
	buttonLoc[1] = 80;
	buttonLoc[2] = 86;
	buttonLoc[3] = 92;
	buttonLoc[4] = 98;
	for (j=1;j<=numberOfMenuItems;j++) {
		document.getElementById("button_"+j).style.top = buttonLoc[j]+"px";
	}
}

function revealMenu(menuNumber) {
	shift = document.getElementById("menu_"+menuNumber).offsetHeight;
	if (document.getElementById("menu_"+menuNumber).style.visibility != "visible") {
		for (i=1;i<=numberOfMenuItems;i++) {
			if (i != menuNumber) {
				hideMenu(i);
			}
		}
		initButtons();
		for (i=(menuNumber+1);i<=numberOfMenuItems;i++) {		
			
			document.getElementById("button_"+i).style.top = (buttonLoc[i]+shift)+"px";
			
		}
		document.getElementById("menu_"+menuNumber).style.visibility="visible";
	}
}

function hideMenu(menuNumber) {

	if (document.getElementById("menu_"+menuNumber).style.visibility == "visible") {
		document.getElementById("menu_"+menuNumber).style.visibility="hidden";
	}
}
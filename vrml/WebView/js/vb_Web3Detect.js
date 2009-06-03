// start other plugin detection for non-Win IE, can be run standalone

var ok    = 1;
var nplug = 0;
var plug  = new Array(200);
var dp    = new Array(10);

// detect plugins where the navigator.plugins array is supported (Firefox, Opera, Netscape, Safari, SeaMonkey, Mozilla, Konqueror)

if (navigator.plugins && navigator.plugins.length > 0) {
  var numPlugins = navigator.plugins.length;

// loop over all plugins
  for (i = 0; i < numPlugins; i++) {
    var plugin = navigator.plugins[i];
    var plugnam = plugin.name;

// rename some of the plugins
    if (plugnam == 'Adobe Acrobat')             {plugnam = 'Adobe Reader';}
    if (plugnam == 'Shockwave Flash')           {plugnam = 'Flash Player';}
    if (plugnam == 'Shockwave for Director')    {plugnam = 'Shockwave Player';}
    if (plugnam == 'MetaStream 3 Plugin')       {plugnam = 'Viewpoint Media Player';}

    if (plugnam.indexOf('RealPlayer(tm)') == 0)            {plugnam = 'RealPlayer';}
    if (plugnam.indexOf('Adobe SVG Viewer') == 0)          {plugnam = 'Adobe SVG Viewer';}
    if (plugnam.indexOf('Windows Media Player') == 0)      {plugnam = 'Windows Media Player';}
    if (plugnam.indexOf('Windows Genuine Advantage') == 0) {plugnam = 'Windows Genuine Advantage';}
    if (plugnam.indexOf('Mozilla ActiveX control') == 0)   {plugnam = 'Mozilla ActiveX Plugin';}
    if (plugnam.indexOf('Helix DNA Plugin') == 0)          {plugnam = 'Helix DNA Plugin';}
    if (plugnam.indexOf('Java(TM) Plug-in') == 0)          {plugnam = 'Java(TM) Plugin';}
    if (plugnam.indexOf('Cosmo Player 2.1.1') == 0)        {plugnam = 'Cosmo Player 2.1.1';}
    if (plugnam.indexOf('Cortona VRML Client') == 0)       {plugnam = 'Cortona3D';}
    if (plugnam.indexOf('DivX') == 0)                      {plugnam = 'DivX Web Player';}
    if (plugnam.indexOf('Google Update') == 0)             {plugnam = 'Google Updater';}
    if (plugnam.indexOf('O3D Plugin') == 0)                {plugnam = 'Google O3D Plugin';}

// determine the plugin version (plugver) from a possible number in the plugin description
    var plugver = '';
				var pf = 1000;
				desc = plugin.description;
				dp[0] = desc.indexOf('0');
				dp[1] = desc.indexOf('1');
				dp[2] = desc.indexOf('2');
				dp[3] = desc.indexOf('3');
				dp[4] = desc.indexOf('4');
				dp[5] = desc.indexOf('5');
				dp[6] = desc.indexOf('6');
				dp[7] = desc.indexOf('7');
				dp[8] = desc.indexOf('8');
				dp[9] = desc.indexOf('9');
				for (l = 0; l < 10; l++) {if (dp[l] != -1 && dp[l] < pf) {pf = dp[l];}}

				if (pf < 1000) {
						var s1 = desc.substring(pf,desc.length);
						if (plugnam.indexOf('Cortona') != -1) {
								s1 = s1.replace(/[,]/g, ".");
								s1 = s1.replace(/[ ]/g, "");
								s1 = s1.replace(/[)]/g, "");
						}
						var p2 = s1.indexOf(' ');
						var p3 = desc.indexOf('<');
						if (p3 == -1) {
								if (p2 == -1) {
										p2 = 20;
								} else if ((s1.length - p2) < 7) {
										p2 = s1.length;
								}
								num = s1.substring(0,p2);
						} else {
								num = parseFloat(desc.substring(pf));
						}
						if (plugin.name.indexOf(num) == -1) {plugver = ' ' + num;}
				}
				if (plugver.indexOf('.') != -1 && plugnam.indexOf('Java') == -1 && plugnam.indexOf('Google Gadget') == -1 && plugnam.indexOf('Windows Genuine Advantage') == -1) {
				  plugnam = plugnam + plugver;
				}

// look for other plugins by checking name
				ok = 1;
				for (k = 0; k < nplug; k++) {if (plugnam == plug[k]) {ok = 0;}}

// do not count the following as different plugins
    if (plugnam.indexOf('RealJukebox') != -1)               {ok = 0;}
				if (plugnam.indexOf('RealPlayer Version Plugin') != -1) {ok = 0;}
				if (plugnam.indexOf('RealNetworks Rhapsody') != -1)     {ok = 0;}
				if (plugnam.indexOf('BS Version') != -1)                {ok = 0;}
				if (plugnam.indexOf('DivX') != -1 && plugnam.length <= 15)  {ok = 0;}

				if (ok) {
						plug[nplug] = plugnam;
						nplug = nplug + 1;
				}
  }
}

var aflash  = 0;
var areader = 0;
var java    = 0;

plug = plug.sort();

if (navigator.plugins && navigator.plugins.length > 0) {
  document.write('<FONT SIZE=-1 FACE="Arial,Helvetica">');
		document.write('<P><A HREF="./plugins.html">All plugins</A>');
		if (nplug > 0) {
				document.write(' (' + nplug + '):');
				for (k = 0; k < nplug; k++) {

// associate URL with plugin
						url1 = "";
						url2 = "";
						if (plug[k].indexOf('AXEL')       != -1) {url1 = '<A HREF="http://www.mindavenue.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Alambik')     == 0) {url1 = '<A HREF="http://www.alambik.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Blender')    != -1) {url1 = '<A HREF="http://www.blender3d.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('CrossOver')   == 0) {url1 = '<A HREF="http://www.codeweavers.com/products/crossover/">'; url2 = '</A>';}
						if (plug[k].indexOf('Emma')        == 0) {url1 = '<A HREF="http://www.emma3d.org/">'; url2 = '</A>';}
						if (plug[k].indexOf('EonX')        == 0) {url1 = '<A HREF="http://www.eonreality.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Flatland')    == 0) {url1 = '<A HREF="http://www.flatland.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Haptek')      == 0) {url1 = '<A HREF="http://www.haptek.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Helix DNA')  != -1) {url1 = '<A HREF="http://helixcommunity.org/">'; url2 = '</A>';}
						if (plug[k].indexOf('Java')        == 0) {url1 = '<A HREF="http://java.sun.com/javase/downloads/index.jsp">'; url2 = '</A>'; java = 1;}
						if (plug[k].indexOf('MetaStream')  == 0) {url1 = '<A HREF="http://www.viewpoint.com/pub/products/vmp.html">'; url2 = '</A>';}
						if (plug[k].indexOf('O2C')        != -1) {url1 = '<A HREF="http://www.o2c.de/">'; url2 = '</A>';}
						if (plug[k].indexOf('Oracle')      == 0) {url1 = '<A HREF="http://www.oracle.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Plugger')    != -1) {url1 = '<A HREF="http://mozplugger.mozdev.org/">'; url2 = '</A>';}
						if (plug[k].indexOf('QuickTime')   == 0) {url1 = '<A HREF="http://www.apple.com/quicktime/">'; url2 = '</A>';}
						if (plug[k].indexOf('RealPlayer')  == 0) {url1 = '<A HREF="http://www.real.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('RealOne')     == 0) {url1 = '<A HREF="http://www.real.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('SpinFire')   != -1) {url1 = '<A HREF="http://www.actify.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Tcl Plugin')  == 0) {url1 = '<A HREF="http://www.tcl.tk/software/plugin/">'; url2 = '</A>';}
						if (plug[k].indexOf('TurnTool')    == 0) {url1 = '<A HREF="http://www.turntool.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Viewpoint')   == 0) {url1 = '<A HREF="http://www.viewpoint.com/pub/products/vmp.html">'; url2 = '</A>';}
						if (plug[k].indexOf('Virtools')    == 0) {url1 = '<A HREF="http://www.virtools.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('XVL')         == 0) {url1 = '<A HREF="http://www.lattice3d.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('DevalVR')     == 0) {url1 = '<A HREF="http://www.devalvr.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('MYRIAD')      == 0) {url1 = '<A HREF="http://www.myriadviewer.com/myriadreader.htm">'; url2 = '</A>';}
						if (plug[k].indexOf('VLC Multimedia') == 0) {url1 = '<A HREF="http://www.videolan.org/">'; url2 = '</A>';}
						if (plug[k].indexOf('iPIX')       != -1) {url1 = '<A HREF="http://www.ipix.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('DivX')       != -1) {url1 = '<A HREF="http://www.divx.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Photosynth') != -1) {url1 = '<A HREF="http://photosynth.net/">'; url2 = '</A>';}
						if (plug[k].indexOf('iTunes')     != -1) {url1 = '<A HREF="http://www.apple.com/itunes/">'; url2 = '</A>';}
						if (plug[k].indexOf('Silverlight') != -1) {url1 = '<A HREF="http://www.microsoft.com/silverlight/">'; url2 = '</A>';}
						if (plug[k].indexOf('3DMLW')      != -1) {url1 = '<A HREF="http://www.3dmlw.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Unity')      != -1) {url1 = '<A HREF="http://unity3d.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Move Media') != -1) {url1 = '<A HREF="http://www.movenetworks.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('MoveNetwork') != -1) {url1 = '<A HREF="http://www.movenetworks.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Google Gears') != -1) {url1 = '<A HREF="http://gears.google.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Google Earth') != -1) {url1 = '<A HREF="http://code.google.com/apis/earth/">'; url2 = '</A>';}
						if (plug[k].indexOf('Google Updater') != -1) {url1 = '<A HREF="http://www.google.com/support/pack/bin/answer.py?hl=en&answer=30252">'; url2 = '</A>';}
						if (plug[k].indexOf('Google Gadget') != -1) {url1 = '<A HREF="http://desktop.google.com/plugins/">'; url2 = '</A>';}
						if (plug[k].indexOf('ExitReality') != -1) {url1 = '<A HREF="http://www.exitreality.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Foxit') == 0) {url1 = '<A HREF="http://www.foxitsoftware.com/">'; url2 = '</A>';}

						if (plug[k].indexOf('Adobe')      != -1) {url1 = '<A HREF="http://www.adobe.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Shockwave')  != -1) {url1 = '<A HREF="http://www.adobe.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Macromedia') != -1) {url1 = '<A HREF="http://www.adobe.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Authorware') != -1) {url1 = '<A HREF="http://www.adobe.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Adobe Acrobat')          != -1) {url1 = '<A HREF="http://www.adobe.com/products/reader/">'; url2 = '</A>'; areader = 1;}
						if (plug[k].indexOf('Adobe Reader')           != -1) {url1 = '<A HREF="http://www.adobe.com/products/reader/">'; url2 = '</A>'; areader = 1;}
						if (plug[k].indexOf('Shockwave Flash')        != -1) {url1 = '<A HREF="http://www.adobe.com/products/flashplayer/">'; url2 = '</A>'; aflash = 1;}
						if (plug[k].indexOf('Flash Player')           != -1) {url1 = '<A HREF="http://www.adobe.com/products/flashplayer/">'; url2 = '</A>'; aflash = 1;}
						if (plug[k].indexOf('Shockwave for Director') != -1) {url1 = '<A HREF="http://www.adobe.com/products/shockwaveplayer/">'; url2 = '</A>';}
						if (plug[k].indexOf('Shockwave Player')       != -1) {url1 = '<A HREF="http://www.adobe.com/products/shockwaveplayer/">'; url2 = '</A>';}
						if (plug[k].indexOf('Adobe SVG')              != -1) {url1 = '<A HREF="http://www.adobe.com/svg/">'; url2 = '</A>';}

  				if (plug[k].indexOf('DepthCharge')     == 0)      {url1 = '<A HREF="http://www.vrex.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Mozilla ActiveX') == 0)      {url1 = '<A HREF="http://www.iol.ie/~locka/mozilla/plugin.htm">'; url2 = '</A>';}
						if (plug[k].indexOf('Nullsoft Winamp') == 0)      {url1 = '<A HREF="http://www.winamp.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('Windows Media Player') == 0) {url1 = '<A HREF="http://www.microsoft.com/windows/windowsmedia/">'; url2 = '</A>';}
						if (plug[k].indexOf('Windows Media Player Firefox Plugin') != -1) {url1 = '<A HREF="http://kb.mozillazine.org/Windows_Media_Player">'; url2 = '</A>';}
						if (plug[k].indexOf('windows media') == 0) {url1 = '<A HREF="http://www.microsoft.com/windows/windowsmedia/">'; url2 = '</A>';}
						if (plug[k].indexOf('getPlus') != -1)    {url1 = '<A HREF="http://www.nosltd.com/get.html">'; url2 = '</A>';}
						if (plug[k].indexOf('Office Live') != -1)    {url1 = '<A HREF="http://www.officelive.com/">'; url2 = '</A>';}
						if (plug[k].indexOf('O3D Plugin') != -1)    {url1 = '<A HREF="http://code.google.com/apis/o3d/">'; url2 = '</A>';}
						if (plug[k].indexOf('Apple Java') != -1)    {url1 = '<A HREF="http://www.apple.com/java/">'; url2 = '</A>';}
						if (plug[k].indexOf('ActiveTouch General Plugin') != -1)    {url1 = '<A HREF="http://wiki.answers.com/Q/What_is_the_ActiveTouch_General_Plugin_Container">'; url2 = '</A>';}
						if (plug[k].indexOf('IE Tab Plug-in') != -1)    {url1 = '<A HREF="https://addons.mozilla.org/firefox/addon/1419">'; url2 = '</A>';}
						if (plug[k].indexOf('Deep View') != -1)    {url1 = '<A HREF="http://www.righthemisphere.com/products/deeppub/DeepPub_View/index.html">'; url2 = '</A>';}

						document.write(' &nbsp;[' + url1 + plug[k] + url2 + ']');
				}
		} else {
				document.write(': &nbsp;none');
		}

// check if certain plugins are installed
  if (navigator.userAgent.indexOf('Windows') == -1) {java = 1;}
  if (navigator.userAgent.indexOf('Opera')  != -1 || navigator.appVersion.indexOf('Opera')  != -1 || navigator.appName.indexOf('Opera') != -1) {java = 1;}
  if (navigator.userAgent.indexOf('Safari') != -1 || navigator.appVersion.indexOf('Safari') != -1) {java = 1;}

		if (aflash == 0 || areader == 0 || java == 0) {
				document.write('<BR>Download other plugins:');
				if (areader == 0) {document.write(' &nbsp;<A HREF="http://www.adobe.com/products/reader/">Adobe Reader</A>');}
				if (aflash  == 0) {document.write(' &nbsp;<A HREF="http://www.adobe.com/products/flashplayer/">Flash Player</A>');}
				if (java    == 0) {document.write(' &nbsp;<A HREF="http://java.sun.com/javase/downloads/index.jsp">Java Runtime Environment</A>');}
		}
		document.write('</FONT>');
}

document.write('<P>');
//if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.indexOf('Win') != -1 && navigator.userAgent.indexOf('Opera') == -1) {
//		document.write('<A HREF="./pluginsIE.html">More Plugins</A>&nbsp;&nbsp;');
//}

document.write('<FONT SIZE=-1 FACE="Arial,Helvetica">');
document.write('<A HREF="http://www.nist.gov/public_affairs/disclaim.htm">Disclaimer</A>');
document.write('&nbsp;&nbsp;(* - Purchasing a license removes the product logo,  <SUP>1</SUP> - Not a plugin)');
if (document.URL.indexOf('nist.gov') == -1 && document.URL.indexOf('lipman') == -1) {
  document.write('<BR>This detector was copied from <A HREF="http://cic.nist.gov/vrml/vbdetect.html">http://cic.nist.gov/vrml/vbdetect.html</A>'); 
}
document.write('</FONT>');

// start VRML and X3D plugin detection

// Author: Robert Lipman, robert.lipman@nist.gov, http://cic.nist.gov/lipman/
// Disclaimer: http://www.nist.gov/public_affairs/disclaim.htm

// This software was developed at the National Institute of Standards and Technology by employees of the
// Federal Government in the course of their official duties. Pursuant to Title 17 Section 105 of the United
// States Code this software is not subject to copyright protection and is in the public domain.  This software
// is an experimental system.  NIST assumes no responsibility whatsoever for its use by other parties, and makes 
// no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.  We would 
// appreciate acknowledgement if the software is used.

// This software can be redistributed and/or modified freely provided that any derivative works bear some notice 
// that they are derived from it, and any modified versions bear some notice that they have been modified. 

var nvrml  = 0;
var vrmlok = 0;
var vrmlPlugin = '';
var vrmlMimetype = '';
var vrml = new Array(20);
var nenabled  = 0;
var nenabledx = 0;
var bold  = 0;
var boldx = 0;

var vrmlMime = new Array(10);
vrmlMime[1] = 'model/vrml';
vrmlMime[2] = 'x-world/x-vrml';
var nvrmlMime = 2;

var nx3d  = 0;
var x3dok = 0;
var x3dPlugin = '';
var x3dMimetype = '';
var x3d = new Array(20);

var x3dMime = new Array(10);
x3dMime[1] = 'model/x3d';
x3dMime[2] = 'model/x3d+xml';
x3dMime[3] = 'model/x3d+vrml';
x3dMime[4] = 'model/x3d+binary';
var nx3dMime = 4;

// detect plugins in Firefox, Netscape, Mozilla, Opera, and other browser that use the navigator.plugins array

if (navigator.plugins && navigator.plugins.length > 0) {
  var numPlugins = navigator.plugins.length;

// loop over all plugins
  for (i = 0; i < numPlugins; i++) {
    var plugin = navigator.plugins[i];
    var numTypes = plugin.length;

// look for VRML plugins by checking mimetype for 'vrml'
    for (j = 0; j < numTypes; j++) {
      mimetype = plugin[j];
      if (mimetype) {
        if (mimetype.type == vrmlMime[1] ||
            mimetype.type == vrmlMime[2] ||
            mimetype.suffixes == 'wrl' ||
            mimetype.suffixes == 'wrz' ||
            mimetype.suffixes == 'vrml') {
          enabledPlugin = mimetype.enabledPlugin;
          if (enabledPlugin && (enabledPlugin.name == plugin.name)) {
            if (plugin.name != vrmlPlugin) {
              vrmlPlugin = plugin.name;
              vrmlMimetype = mimetype.type;
              nenabled = nenabled + 1;
            }
            if (!vrmlok) {vrmlok = 1;}
          }
          ok = 1;
          for (k = 1; k <= nvrml; k++) {if (plugin.name == vrml[k]) {ok = 0;}}
          if (ok) {nvrml = nvrml + 1; vrml[nvrml] = plugin.name;}
          ok = 1;
          for (m = 1; m <= nvrmlMime; m++) {if (mimetype.type == vrmlMime[m]) {ok = 0;}}
          if (ok) {nvrmlMime = nvrmlMime + 1; vrmlMime[nvrmlMime] = mimetype.type;}
        }

// look for X3D plugins by checking mimetype for 'x3d'
        if (mimetype.type.indexOf('x3d') != -1 ||
            mimetype.type.indexOf('X3D') != -1 || 
            mimetype.suffixes.indexOf('x3d') != -1 || 
            mimetype.suffixes.indexOf('x3b') != -1 || 
            mimetype.suffixes.indexOf('b3d') != -1 || 
            mimetype.suffixes.indexOf('b3z') != -1 || 
            mimetype.suffixes.indexOf('X3D') != -1) {
          enabledPlugin = mimetype.enabledPlugin;
          if (enabledPlugin && (enabledPlugin.name == plugin.name)) {
            if (plugin.name != x3dPlugin) {
              x3dPlugin = plugin.name;
              x3dMimetype = mimetype.type;
              nenabledx = nenabledx + 1;
            }
            if (!x3dok) {x3dok = 1;}
          }
          ok = 1;
          for (k = 1; k <= nx3d; k++) {if (plugin.name == x3d[k]) {ok = 0;}}
          if (ok) {nx3d = nx3d + 1; x3d[nx3d] = plugin.name;}
          ok = 1;
          for (m = 1; m <= nx3dMime; m++) {if (mimetype.type == x3dMime[m]) {ok = 0;}}
          if (ok) {nx3dMime = nx3dMime + 1; x3dMime[nx3dMime] = mimetype.type;}
        }
      }
    }
  }
  
  if (nenabled > 1) {
    vrmlPlugin = 'undefined';
    bold = 1;
  }
  if (nenabledx > 1) {
    x3dPlugin = 'undefined';
    boldx = 1;
  }

// *****************************************************************************
// detect VRML and X3D plugins in Microsoft Internet Explorer, only with Windows

} else if (navigator.appName == "Microsoft Internet Explorer") {
  if (navigator.appVersion.indexOf('Win') != -1) {

// IEDetectObject is used to detect ActiveX Controls
    document.writeln('<SCRIPT LANGUAGE="VBScript">');
    document.writeln('function IEDetectObject(activeXControlName)');
    document.writeln('  On Error Resume Next');
    document.writeln('  IEDetectObject = IsObject(CreateObject(activeXControlName))');
    document.writeln('End function');
    document.writeln('</SCR' + 'IPT>');

// detect VRML and X3D ActiveX controls
    if (IEDetectObject('bsContact.bsContact.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "BS&nbsp;Contact";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "BS&nbsp;Contact";
    } else if (IEDetectObject('blaxxunCC3D.blaxxunCC3D.1'))   {
      nvrml = nvrml + 1; vrml[nvrml] = "blaxxun&nbsp;Contact";
    }
    if (IEDetectObject('Cortona.Control.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Cortona3D&nbsp;Viewer";
    }
    if (IEDetectObject('SGI.CosmoPlayer.2')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Cosmo&nbsp;Player";
    } else if (IEDetectObject('SGI.CosmoPlayer.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Cosmo&nbsp;Player";
    }
    if (IEDetectObject('ANIMA.AnimaCtrl.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Flux&nbsp;Player";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "Flux&nbsp;Player";
    }
    if (IEDetectObject('vivaty.VivatyCtrl.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Vivaty&nbsp;Player";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "Vivaty&nbsp;Player";
    }
    if (IEDetectObject('OCTAGAX.OctagaXCtrl.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Octaga&nbsp;Player";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "Octaga&nbsp;Player";
    } else if (IEDetectObject('OctagaProX.OctagaProX.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "Octaga&nbsp;Pro";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "Octaga&nbsp;Pro";
    }
    if (IEDetectObject('SWIRLBPI.SwirlBpiCtrl.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "SwirlX3D";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "SwirlX3D";
    }
    if (IEDetectObject('SpaceTime.SpaceTime.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "SpaceTime";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "SpaceTime";
    }
    if (IEDetectObject('ExitReality 3D Player.ExitReality 3D Player.1')) {
      nvrml = nvrml + 1; vrml[nvrml] = "ExitReality";
      nx3d  = nx3d  + 1; x3d[nx3d]   = "ExitReality";
    }

    if (nvrml > 0) {
      vrmlok = 1;
      if (nvrml == 1) {
        vrmlPlugin = vrml[1];
      } else {
        vrmlPlugin = 'undefined';
      }
    }
    if (nx3d > 0) {
      x3dok = 1;
      if (nx3d == 1) {
        x3dPlugin = x3d[1];
      } else {
        x3dPlugin = 'undefined';
      }
    }
  }
  bold  = 1;
  boldx = 1;
}

// ----------------------------------------------------------------------------
// The rest of the code below is optional.  It reports which VRML and X3D plugins
// were found and makes some recommendations if none was found.  The variables
// 'nvrml' and 'nx3d' indicate how many plugins were found.  The arrays 'vrml'
// and 'x3d' contain the names of the plugins that were found.

// OS

var win   = 0;
var mac   = 0;
var unix  = 0;
var linux = 0;
if (navigator.appVersion.indexOf('Win')  != -1) {win   = 1;}
if (navigator.appVersion.indexOf('Mac')  != -1) {mac   = 1;}
if (navigator.appVersion.indexOf('X11')  != -1) {unix  = 1;}
if (navigator.userAgent.indexOf('Linux') != -1) {unix  = 1; linux = 1;}

var opera = 0;
if (navigator.appName == 'Opera' || navigator.userAgent.indexOf('Opera') != -1) {opera = 1;}

// win     = 0;
// mac     = 1;
// unix    = 1;  linux   = 1;
 
// VRML plugin URLs, update as necessary

var httpBlaxxun = '<A HREF="http://www.blaxxun.com/">';
var httpContact = '<A HREF="http://www.bitmanagement.com/products/bs_contact_vrml.en.html">';
var httpCortona = '<A HREF="http://www.cortona3d.com/cortona">';
var httpCosmo   = '<A HREF="http://cic.nist.gov/vrml/cosmoplayer.html">';
var httpExit    = '<A HREF="http://www.exitreality.com/">'; 
var httpFreeWRL = '<A HREF="http://freewrl.sourceforge.net/">';
var httpInstant = '<A HREF="http://www.instantreality.org/">';
var httpLive3D  = '<A HREF="http://wp.netscape.com/eng/live3d/live3d_overview.html">';
var httpOctaga  = '<A HREF="http://www.octaga.com/">';
var httpOpenWRL = '<A HREF="http://www.openworlds.com/">';
var httpOpnVRML = '<A HREF="http://www.openvrml.org/">';
var httpOrbisnp = '<A HREF="http://www.orbisnap.com/">';
var httpRealTim = '<A HREF="http://www.simcreator.com/">';
var httpSpace   = '<A HREF="http://www.spacetime.com/">';
var httpSwirl   = '<A HREF="http://www.pinecoast.com/plugindownload.htm">';
var httpVivaty  = '<A HREF="http://www.vivaty.com/">';
var httpXj3D    = '<A HREF="http://www.web3d.org/x3d/xj3d/">';

if (linux) {httpContact = '<A HREF="http://www.bitmanagement.com/developer/index.html">';}

var aov = "<BR><FONT SIZE=-1>Download ";
var aox = "<BR><FONT SIZE=-1>Download ";
if (nvrml > 0) {aov = "<BR><FONT SIZE=-1>Other ";}
if (nx3d  > 0) {aox = "<BR><FONT SIZE=-1>Other ";}

// report VRML plugins -----------------------------------------------------------------------
// Variables are set that indicate which type of VRML plugin is installed based on the 'vrml' array 

var blaxxun = 0;
var contact = 0;
var cortona = 0;
var cosmo   = 0;
var flux    = 0;
var freewrl = 0;
var live3d  = 0;
var octaga  = 0;
var openwrl = 0;
var opnvrml = 0;
var pivoron = 0;
var swirl   = 0;
var venues  = 0;
var vivaty  = 0;
var worldvw = 0;

var ip = vrmlPlugin.indexOf(' 2.1');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' Plugin for Mozilla');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' VRML Version');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' VRML Client');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip) + '3D';}
ip = vrmlPlugin.indexOf(' Netscape - Mozilla Plugin');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' X3D/VRML');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' VRML/X3D');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' 3D Plug-in');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip);}
ip = vrmlPlugin.indexOf(' Viewer Plugin');
if (ip != -1) {vrmlPlugin = vrmlPlugin.substring(0,ip) + ' Viewer';}

if (vrmlPlugin == 'Octaga') {vrmlPlugin = "Octaga Player";}
var urlHelp = "";

document.write('<FONT FACE="Arial,Helvetica">');
document.write('<P></P><P>');
if (nvrml > 0) {
  document.write('VRML plugin');
  if (nvrml > 1) {document.write('s');}
  document.write(': &nbsp;');
  
  for (k = 1; k <= nvrml; k++) {
    ip = vrml[k].indexOf(' 2.1');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' Plugin for Mozilla');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' VRML Version');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' VRML Client');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip) + '3D';}
    ip = vrml[k].indexOf(' Netscape - Mozilla Plugin');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' X3D/VRML');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' VRML/X3D');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' 3D Plug-in');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip);}
    ip = vrml[k].indexOf(' Viewer Plugin');
    if (ip != -1) {vrml[k] = vrml[k].substring(0,ip) + ' Viewer';}
        
    if (vrml[k] == 'Octaga') {vrml[k] = "Octaga Player";}
    urlHelp = "";
    if (bold || vrmlPlugin.indexOf(vrml[k]) != -1) {document.write('<B>');}

    if (vrml[k].indexOf('blaxxun') != -1) {
      document.write(httpBlaxxun + vrml[k] + '</A>');
      blaxxun = 1;

    } else if (vrml[k].indexOf('BS') != -1 && vrml[k].indexOf('Contact') != -1) {
      document.write(httpContact + vrml[k] + '</A>');
      if (linux) {document.write('<A HREF="http://www.bitmanagement.com/products/licensing.en.html">*</A>');}
      contact = 1;
      urlHelp = '<A HREF="http://www.bitmanagement.com/developer/index.html">'; 

    } else if (vrml[k].indexOf('Cortona') != -1) {
      document.write(httpCortona + vrml[k] + '</A>');
      if (win) {document.write('<A HREF="http://www.cortona3d.com/stuff/contentmgr/files/3dd8030a9eb4a4386bec79ee32895a34/doc/cortona_licensing_options_1_.pdf">*</A>');}
      cortona = 1;
      urlHelp = '<A HREF="http://www.cortona3d.com/stuff/contentmgr/files/418a435b50d5ab844bcc893ea80e2794/doc/cortona_guide.pdf">';

    } else if (vrml[k].indexOf('Cosmo') != -1) {
      document.write(httpCosmo + vrml[k] + '</A>');
      cosmo = 1;
      urlHelp = '<A HREF="http://cic.nist.gov/vrml/cosmo/Doc/frames-help.html">';

    } else if (vrml[k].indexOf('Vivaty') != -1) {
      document.write(httpVivaty + vrml[k] + '</A>');
      vivaty = 1;

    } else if (vrml[k].indexOf('Octaga') != -1) {
      document.write(httpOctaga + vrml[k] + '</A><A HREF="http://www.octaga.com/joomla/index.php?page=shop.product_details&flypage=shop.flypage&product_id=4&category_id=1&manufacturer_id=0&option=com_virtuemart&Itemid=66">*</A>');
      urlHelp = '<A HREF="http://www.octaga.com/freedownloads/OctagaPlayer/2.3/userManual.pdf">';
      octaga = 1;

    } else if (vrml[k].indexOf('FreeWRL') != -1) {
      document.write(httpFreeWRL + vrml[k] + '</A>');
      freewrl = 1;
      urlHelp = '<A HREF="http://freewrl.sourceforge.net/freewrl.html">'; 

    } else if (vrml[k].indexOf('OpenVRML') != -1) {
      document.write(httpOpnVRML + vrml[k] + '</A>');
      opnvrml = 1;

    } else if (vrml[k].indexOf('Swirl') != -1) {
      document.write(httpSwirl + vrml[k] + '</A>');
      swirl = 1;
      urlHelp = '<A HREF="http://www.pinecoast.com/navbasics.htm">';

    } else if (vrml[k].indexOf('Live3D') != -1) {
      document.write(httpLive3D + vrml[k] + '</A>');
      live3d = 1;

    } else if (vrml[k].indexOf('Realtime Technologies') == 0) {
      document.write(httpRealTim + vrml[k] + '</A>');

    } else if (vrml[k].indexOf('OpenWorlds') == 0) {
      document.write(httpOpenWRL + vrml[k] + '</A>');
      openwrl = 1;

    } else if (vrml[k].indexOf('ExitReality') == 0) {
      document.write(httpExit + vrml[k] + '</A>');
      urlHelp = '<A HREF="http://kb.exitreality.com/">';

    } else if (vrml[k].indexOf('SpaceTime') == 0) {
      document.write(httpSpace + vrml[k] + '</A>');

    } else {
      document.write(vrml[k]);
      if (vrml[k].indexOf('Flux')      != -1) {flux    = 1;}
      if (vrml[k].indexOf('Pivoron')   != -1) {pivoron = 1;}
      if (vrml[k].indexOf('Venues')    != -1) {venues  = 1;}
      if (vrml[k].indexOf('WorldView') != -1) {worldvw = 1;}
    }

    if (bold || vrmlPlugin.indexOf(vrml[k]) != -1) {document.write('</B>');}
    if (urlHelp != "") {document.write('&nbsp;(' + urlHelp + 'Help</A>)');}
    if (nvrml > 1) {
      if (k < nvrml) {document.write('&nbsp; ');}
    }
  }

// no VRML plugins

} else {
  document.write('VRML plugin: &nbsp;<B>None</B>');
}

// cosmo   = 0;
// cortona = 0;

// VRML plugin recommendations ----------------------------------------------------------------
// This section can be modified to take different actions depending on the VRML plugin

var urlContact = httpContact + 'BS&nbsp;Contact</A><A HREF="http://www.bitmanagement.com/products/licensing.en.html">*</A>';
var urlCortona = httpCortona + 'Cortona3D&nbsp;Viewer</A><A HREF="http://www.cortona3d.com/stuff/contentmgr/files/3dd8030a9eb4a4386bec79ee32895a34/doc/cortona_licensing_options_1_.pdf">*</A>';
var urlCosmo   = httpCosmo   + 'Cosmo&nbsp;Player</A>';
var urlFreeWRL = httpFreeWRL + 'FreeWRL</A>';
var urlInstant = httpInstant + 'InstantPlayer</A><SUP>1</SUP>';
var urlOctaga  = httpOctaga  + 'Octaga&nbsp;Player</A><A HREF="http://www.octaga.com/joomla/index.php?page=shop.product_details&flypage=shop.flypage&product_id=4&category_id=1&manufacturer_id=0&option=com_virtuemart&Itemid=66">*</A>';
var urlOpnVRML = httpOpnVRML + 'OpenVRML</A>';
var urlOrbisnp = httpOrbisnp + 'Orbisnap</A><SUP>1</SUP>';
var urlSwirl   = httpSwirl   + 'SwirlX3D</A>';
var urlXj3D    = httpXj3D    + 'Xj3D</A><SUP>1</SUP>';

if (linux) {urlContact =  httpContact + 'BS&nbsp;Contact</A>';}

// Cosmo Player and other old plugins might not work in IE anymore

if (navigator.appName == "Microsoft Internet Explorer") {
		if (nvrml == 1 && (blaxxun || venues || openwrl || pivoron || worldvw)) {
				document.write('<BR><FONT COLOR="#aa0000"><FONT SIZE=-1>Older VRML plugins might not work in Internet Explorer because of a Windows security update.</FONT></FONT>');
		}
}

var wbie = 0;
var wbff = 0;
if (navigator.appName == "Microsoft Internet Explorer") {wbie = 1;}
if (navigator.userAgent.indexOf('Firefox') != -1)       {wbff = 1;}

if (win) {
		document.write(aov + 'VRML plugins:');
		if (!cosmo)                   {document.write('&nbsp; ' + urlCosmo);}
		if (!octaga)                  {document.write('&nbsp; ' + urlOctaga);}
		if (!cortona)                 {document.write('&nbsp; ' + urlCortona);}
		if (!contact)                 {document.write('&nbsp; ' + urlContact);}
	 document.write('&nbsp; ' + urlInstant);
		if (!swirl && (wbie || wbff)) {document.write('&nbsp; ' + urlSwirl);}

} else if (unix) {
		if (linux || navigator.userAgent.indexOf('SunOS') != -1) {
		  document.write(aov + 'VRML plugins:');
		}
		if (linux) {
		  if (!octaga)  {document.write('&nbsp; ' + urlOctaga);}
		  if (!contact) {document.write('&nbsp; ' + urlContact);}
		  if (!freewrl) {document.write('&nbsp; ' + urlFreeWRL);}
		  if (!opnvrml) {document.write('&nbsp; ' + urlOpnVRML);}
	  	document.write('&nbsp; ' + urlInstant);
		}
		if (linux || navigator.userAgent.indexOf('SunOS') != -1) {
		  document.write('&nbsp; ' + urlXj3D);
	  	document.write('&nbsp; ' + urlOrbisnp);
		}

} else if (mac) {
		document.write(aov + 'VRML plugins:');
		if (!octaga)  {document.write('&nbsp; ' + urlOctaga);}
		if (!freewrl) {document.write('&nbsp; ' + urlFreeWRL);}
		if (!opnvrml) {document.write('&nbsp; ' + urlOpnVRML);}
	 document.write('&nbsp; ' + urlInstant);
		document.write('&nbsp; ' + urlXj3D);
		document.write('&nbsp; ' + urlOrbisnp);
}

document.write('</FONT></P>');

// report X3D plugins -----------------------------------------------------------------------
// Variables are set that indicate which type of X3D plugin is installed based on the 'x3d' array

var xcontact = 0;
var xflux    = 0;
var xfreewrl = 0;
var xoctaga  = 0;
var xopnvrml = 0;
var xswirl   = 0;
var xvenues  = 0;
var xvivaty  = 0;

ip = x3dPlugin.indexOf(' Plugin for Mozilla');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' VRML Version');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' Netscape - Mozilla Plugin');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' X3D/VRML');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' VRML/X3D');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' 3D Plug-in');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip);}
ip = x3dPlugin.indexOf(' Viewer Plugin');
if (ip != -1) {x3dPlugin = x3dPlugin.substring(0,ip) + ' Viewer';}
if (x3dPlugin == 'Octaga') {x3dPlugin = "Octaga Player";}

document.write('<P></P><P>');
if (nx3d > 0) {
		document.write('X3D plugin');
		if (nx3d > 1) {document.write('s');}
		document.write(': &nbsp;');

		for (k = 1; k <= nx3d; k++) {
				ip = x3d[k].indexOf(' Plugin for Mozilla');
				if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
				ip = x3d[k].indexOf(' VRML Version');
				if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
				ip = x3d[k].indexOf(' Netscape - Mozilla Plugin');
				if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
				ip = x3d[k].indexOf(' X3D/VRML');
				if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
				ip = x3d[k].indexOf(' VRML/X3D');
				if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
    ip = x3d[k].indexOf(' 3D Plug-in');
    if (ip != -1) {x3d[k] = x3d[k].substring(0,ip);}
    ip = x3d[k].indexOf(' Viewer Plugin');
    if (ip != -1) {x3d[k] = x3d[k].substring(0,ip) + ' Viewer';}

    if (x3d[k] == 'Octaga') {x3d[k] = "Octaga Player";}
				urlHelp = "";
				if (boldx || x3dPlugin.indexOf(x3d[k]) != -1) {document.write('<B>');}

				if (x3d[k].indexOf('BS') != -1 && x3d[k].indexOf('Contact') != -1) {
      document.write(httpContact + x3d[k] + '</A>');
      if (linux) {document.write('<A HREF="http://www.bitmanagement.com/products/licensing.en.html">*</A>');}
						xcontact = 1;
						urlHelp = '<A HREF="http://www.bitmanagement.com/developer/index.html">'; 

				} else if (x3d[k].indexOf('Vivaty') != -1) {
						document.write(httpVivaty + x3d[k] + '</A>');
						xvivaty = 1;

				} else if (x3d[k].indexOf('Octaga') != -1) {
						document.write(httpOctaga + x3d[k] + '</A>');
      document.write('<A HREF="http://www.octaga.com/joomla/index.php?page=shop.product_details&flypage=shop.flypage&product_id=4&category_id=1&manufacturer_id=0&option=com_virtuemart&Itemid=66">*</A>');
      urlHelp = '<A HREF="http://www.octaga.com/freedownloads/OctagaPlayer/2.3/userManual.pdf">';
						xoctaga = 1;

				} else if (x3d[k].indexOf('FreeWRL') != -1) {
						document.write(httpFreeWRL + x3d[k] + '</A>');
						xfreewrl = 1;
						urlHelp = '<A HREF="http://freewrl.sourceforge.net/freewrl.html">'; 

    } else if (x3d[k].indexOf('Swirl') != -1) {
      document.write(httpSwirl + x3d[k] + '</A>');
      xswirl = 1;
      urlHelp = '<A HREF="http://www.pinecoast.com/navbasics.htm">';

				} else if (x3d[k].indexOf('OpenVRML') != -1) {
						document.write(httpOpnVRML + x3d[k] + '</A>');
						xopnvrml = 1;

    } else if (x3d[k].indexOf('ExitReality') == 0) {
      document.write(httpExit + x3d[k] + '</A>');
      urlHelp = '<A HREF="http://kb.exitreality.com/">';

    } else if (x3d[k].indexOf('Realtime Technologies') == 0) {
      document.write(httpRealTim + x3d[k] + '</A>');

    } else if (x3d[k].indexOf('SpaceTime') == 0) {
      document.write(httpSpace + x3d[k] + '</A>');

				} else {
						document.write(x3d[k]);
      if (x3d[k].indexOf('Venues') != -1) {xvenues = 1;}
      if (x3d[k].indexOf('Flux')   != -1) {xflux   = 1;}
				}

				if (boldx || x3dPlugin.indexOf(x3d[k]) != -1) {document.write('</B>');}
				if (urlHelp != "") {document.write('&nbsp;(' + urlHelp + 'Help</A>)');}
				if (nx3d > 1) {
						if (k < nx3d) {document.write('&nbsp; ');}
				}
		}

// no X3D plugins

} else {
		document.write('X3D plugin: &nbsp;<B>None</B>');
}

// X3D plugin recommendations -----------------------------------------------------------------
// This section can be modified to take different actions depending on the X3D plugin

if (win) {
		document.write(aox + 'X3D plugins:');
		if (!xoctaga)                             {document.write('&nbsp; ' + urlOctaga);}
		if (!xcontact)                            {document.write('&nbsp; ' + urlContact);}
	 document.write('&nbsp; ' + urlInstant);
		if (!xswirl && (wbie || wbff))            {document.write('&nbsp; ' + urlSwirl);}

} else if (linux || mac || navigator.userAgent.indexOf('SunOS') != -1) {
		document.write(aox + 'X3D plugins:');
		if (linux || mac) {
		  if (!xoctaga)  {document.write('&nbsp; ' + urlOctaga);}
		}
		if (linux) {
		  if (!xcontact)  {document.write('&nbsp; ' + urlContact);}
		}
		if (linux || mac) {
		  if (!xfreewrl) {document.write('&nbsp; ' + urlFreeWRL);}
	  	if (!xopnvrml) {document.write('&nbsp; ' + urlOpnVRML);}
	   document.write('&nbsp; ' + urlInstant);
		}
		document.write('&nbsp; ' + urlXj3D);
}
document.write('</FONT></FONT>');
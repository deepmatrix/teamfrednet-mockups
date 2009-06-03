// initialize

var appname = navigator.appName;
var useragent = navigator.userAgent;
if (useragent.indexOf('Opera') != -1) {appname = 'Opera';}
var appversion = navigator.appVersion;

var browString = '';
var browVersion = 0;

var p1 = 0;
var p2 = 0;
var p3 = 0;
var p4 = 0;
var p5 = 0;
var p6 = 0;
var p7 = 0;
var s1 = '';

// look for Internet Explorer
if (appname.indexOf('Internet Explorer') != -1) {
  p1 = appversion.indexOf('MSIE') + 5;
  s1 = appversion.substring(p1,p1+8);
  p2 = s1.indexOf(';');
  if (p2 > 0) {browString = ' ' + s1.substring(0,p2);}
  if (appname == 'Microsoft Internet Explorer') {appname = 'Internet Explorer';}

// look for Netscape
} else if (appname == 'Netscape') {
  p1 = appversion.indexOf('[') - 1;
  if (p1 < 0) {p1 = appversion.indexOf('(') - 1;}
  if (p1 > 0) {browString = ' ' + appversion.substring(0,p1);}

// look for Netscape 6 and greater
  p2 = useragent.indexOf('Netscape');
  if (p2 != -1) {
    s1 = useragent.substring(p2,p2+15);
    p1 = s1.indexOf('/');
    p3 = s1.indexOf(' ');
    if (p3 > 0) {
      browString = ' ' + s1.substring(p1+1,p3);
    } else if (p1 > 0) {
      browString = ' ' + s1.substring(p1+1,p1+6);
    }
  } else {
    p2 = useragent.indexOf('Navigator');
    if (p2 != -1) {
      browString = ' ' + useragent.substring(p2,p2+18);
    }
  }

// look for Opera
} else if (appname == 'Opera') {
  p1 = useragent.indexOf('Opera') + 6;
  if (p1 != 6) {
				p2 = useragent.indexOf('[') + 1;
				if (p2 == 0) {p2 = p1+8;}
				s1 = useragent.substring(p1,p2);
				browString = ' ' + s1;
				p2 = s1.indexOf('[');
				if (p2 > 0) {browString = ' ' + s1.substring(0,p2-2);}
				p2 = browString.indexOf('\(');
				if (p2 > 0) {browString = browString.substring(0,p2-1);}
  } else {
    p2 = useragent.indexOf('(');
    browString = ' ' + useragent.substring(6,p2-1);
  }
}
if (browString == '') {browString = ' ' + appversion;}

browVersion = parseFloat(browString.substring(1,browString.length));

// look for Mozilla, Firefox, and variants (Netscape 5)
if (appname == 'Netscape' && browVersion >= 5) {
  appname = 'Mozilla';
		pg = useragent.indexOf('Gecko/');
		p4 = -1;
  p6 = useragent.indexOf('Firefox');
  if (p6 == -1) {p6 = useragent.indexOf('SeaMonkey');}
  if (p6 != -1) {
    appname = useragent.substring(p6,p6+20);
    p4 = 0;
    p5 = 100;
				p7 = appname.indexOf(' ');
				if (p7 > 10) {appname = appname.substring(0,p7);}
		} else if (useragent.indexOf('Fedora') == -1) {
				if (pg != -1) {
						if (useragent.charCodeAt(pg+15) > 32) {
								appname = useragent.substring(pg+15,pg+35);
								p4 = pg+15;
								p5 = 100;
								p7 = appname.indexOf(' ');
								if (p7 > 10) {appname = appname.substring(0,p7);}
						}
				}
  }
  
// Mozilla revision number (rv:)
  p1 = useragent.indexOf('rv:');
  if (p1 != -1 && appname.indexOf('Firefox') == -1) {
    p1 = p1 + 3;
    p2 = useragent.indexOf(')');

    if (p4 != -1) {
      browString = ' ' + useragent.substring(p5,p5+10);
    } else if (browVersion < 6) {
      if (p2 > p1) {
        browString = ' ' + useragent.substring(p1,p2);
								p3 = browString.indexOf(';');
								if (p3 != -1) {browString = browString.substring(0,p3);}
      } else {
        browString = '';
      }
    }
  } else {
    browString = '';
  }
}

// look for Safari or Google Chrome
p1 = useragent.indexOf('Safari');
if (p1 != -1) {
  p4 = useragent.indexOf('Chrome');
  if (p4 == -1) {
				appname = 'Safari';
				p2 = useragent.indexOf('Version');
				if (p2 != -1) {
						p3 = useragent.substring(p2,p2+20).indexOf(' ');
						browString = ' ' + useragent.substring(p2+8,p2+p3);
				}
  } else {
				appname = 'Google Chrome';
				p3 = useragent.substring(p4,p4+20).indexOf(' ');
				browString = ' ' + useragent.substring(p4+7,p4+p3);
  }
}

// look for Lunascape
p1 = useragent.indexOf('Lunascape');
if (p1 != -1) {
  appname = 'Lunascape';
  p2 = useragent.substring(p1,p1+30).indexOf(' ');
  if (p2 != -1) {
    browString = ' ' + useragent.substring(p1+8,p1+p2);
  }
}

// look for Konqueror (Linux)
p1 = useragent.indexOf('Konqueror');
if (p1 != -1) {appname = 'Konqueror';}

// look for Galeon (Linux)
p1 = useragent.indexOf('Galeon');
if (p1 != -1) {appname = 'Galeon';}

// report web browser version

var mozhelp = 0;
document.write('<FONT FACE="Arial,Helvetica">Web browser: &nbsp;<B>');
if (appname.indexOf('Internet Explorer') != -1) {
		var url_IE = '<A HREF="http://www.microsoft.com/windows/internet-explorer/">';
  document.write(url_IE + appname + browString + '</A>');

} else if (appname.indexOf('Firefox') != -1) {
  if (useragent.indexOf('HP-UX') != -1) {
    document.write('<A HREF="http://www.hp.com/go/mozilla">' + appname + browString + '</A>');
		} else if (useragent.indexOf('SunOS') != -1) {
				document.write('<A HREF="http://www.sunfreeware.com/mozilla.html">' + appname + browString + '</A>');
		} else if (useragent.indexOf('AIX') != -1) {
    document.write('<A HREF="http://www-03.ibm.com/systems/power/software/aix/browsers/index.html">' + appname + browString + '</A>');
  } else {
    document.write('<A HREF="http://www.mozilla.com/">' + appname + browString + '</A>');
    mozhelp = 1;
  }

} else if (appname.indexOf('SeaMonkey') != -1) {
  document.write('<A HREF="http://www.seamonkey-project.org/">' + appname + '</A>');
  mozhelp = 1;

} else if (appname.indexOf('Opera') != -1) {
  document.write('<A HREF="http://www.opera.com/">' + appname + browString + '</A>');

} else if (appname.indexOf('Safari') != -1) {
  document.write('<A HREF="http://www.apple.com/safari/">' + appname + browString + '</A>');

} else if (appname.indexOf('Chrome') != -1) {
  document.write('<A HREF="http://www.google.com/chrome">' + appname + browString + '</A>');

} else if (appname.indexOf('Camino') != -1) {
  document.write('<A HREF="http://caminobrowser.org/">' + appname + browString + '</A>');
  mozhelp = 1;

} else if (appname.indexOf('Konqueror') != -1) {
  document.write('<A HREF="http://www.konqueror.org/">' + appname + browString + '</A>');

} else if (appname.indexOf('Galeon') != -1) {
  document.write('<A HREF="http://galeon.sourceforge.net/">' + appname + browString + '</A>');

} else if (appname.indexOf('K-Meleon') != -1) {
  document.write('<A HREF="http://kmeleon.sourceforge.net/">' + appname + browString + '</A>');

} else if (appname.indexOf('Lunascape') != -1) {
  document.write('<A HREF="http://www.lunascape.tv/">' + appname + browString + '</A>');

} else if (appname.indexOf('Epiphany') != -1) {
  document.write('<A HREF="http://projects.gnome.org/epiphany/">' + appname + '</A>');

} else if (appname.indexOf('Netscape') != -1) {
		var url_NS = '<A HREF="http://browser.netscape.com/">';
  document.write(url_NS + appname + browString + '</A>');

} else if (appname.indexOf('Mozilla') != -1) {
		if (useragent.indexOf('SunOS') != -1) {
				var url_MZ = '<A HREF="http://www.sunfreeware.com/mozilla.html">';
		} else if (useragent.indexOf('HP-UX') != -1) {
				var url_MZ = '<A HREF="http://www.hp.com/go/mozilla">';
		} else if (useragent.indexOf('AIX') != -1) {
				var url_MZ = '<A HREF="http://www-03.ibm.com/systems/power/software/aix/browsers/index.html">';
		} else {
	  	var url_MZ = '<A HREF="http://www.seamonkey-project.org/">';
    mozhelp = 1;
		}
  document.write(url_MZ + appname + browString + '</A>');

} else {
  document.write(appname);
}
document.write('</B>');

// Plugin help
if (mozhelp == 1) {
  document.write('&nbsp;(<A HREF="https://addons.mozilla.org/firefox/plugins/">Plugins</A>)');
} else if (appname == 'Opera') {
  document.write('&nbsp;(<A HREF="http://www.opera.com/docs/plugins/">Plugins</A>)');
}

if (appname == 'Netscape' || appname == 'Mozilla') {document.write('</P>');}
document.write('</FONT>');

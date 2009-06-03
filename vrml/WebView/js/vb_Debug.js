// debug messages, requires vb_VRMLDetect.js to be run first

var dbmsg = '';

dbmsg += 'Please report any unexpected results to:  robert.lipman@nist.gov\n';

if (navigator.appName == "Microsoft Internet Explorer") {
  dbmsg += '\nSome VRML and X3D plugins are not detected in Internet Explorer.';
  dbmsg += '\nOlder VRML plugins might not work in Internet Explorer.';
}
dbmsg += '\nPlugins that have been uninstalled might still be detected.';
dbmsg += '\nStandalone VRML viewers, Java applets, and Java programs are not detected.';

dbmsg += '\n\nnavigator.appName = '     + navigator.appName     + '\n';
dbmsg += 'navigator.appCodeName = ' + navigator.appCodeName + '\n';
dbmsg += 'navigator.appVersion = '  + navigator.appVersion  + '\n';
dbmsg += 'navigator.userAgent = '   + navigator.userAgent   + '\n';

if (navigator.plugins) {
  if (navigator.plugins.length > 0) {
    dbmsg += ' \nnavigator.plugins.length = ' + navigator.plugins.length + '\n';
  }
  if (navigator.mimeTypes) {
				if (navigator.mimeTypes.length > 0) {
						dbmsg += 'navigator.mimeTypes.length = ' + navigator.mimeTypes.length + '\n';
						for (i = 1; i <= nvrmlMime; i++) {
								dbmsg += 'navigator.mimeTypes[' + vrmlMime[i] + '] = ' + navigator.mimeTypes[vrmlMime[i]] + '\n';
						}
						for (j = 1; j <= nx3dMime;  j++) {
								dbmsg += 'navigator.mimeTypes[' + x3dMime[j]  + '] = ' + navigator.mimeTypes[x3dMime[j]]  + '\n';
						}
				}
  }
}

// display the details button for debug messages

document.write('<P><FORM><FONT SIZE=-1>');
document.write('<INPUT TYPE=BUTTON VALUE="Details" onClick="alert(dbmsg)">');
document.write('</FONT></FORM>');

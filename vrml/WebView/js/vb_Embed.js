// display VRML and/or X3D model, requires vb_VRMLDetect.js to be run first so the variables are set

var x3dshow = x3dok;
if (vrmlPlugin == x3dPlugin) {x3dshow = 0;}

var wid = 400;
var hgt = 300;
if (vrmlok && x3dshow) {
  wid = 300;
  hgt = 225;
}

var wrlfile = "nistlogo.wrl";
if (live3d && vrmlPlugin == 'Live3D Plugin DLL') {wrlfile  = "nistlogo_v1.wrl";}

var x3dfile = "nistlogo.x3d";
if (vrmlok) {
  document.write('<EMBED SRC="./' + wrlfile + '" WIDTH=' + wid + ' HEIGHT=' + hgt + '>');
}

if (vrmlok && x3dshow) {
  if (document.body.clientWidth > 1100) {
    document.write('&nbsp;');
  } else {
    document.write('<P>');
  }
}

if (x3dshow) {
  document.write('<EMBED SRC="./' + x3dfile + '" WIDTH=' + wid + ' HEIGHT=' + hgt + '>');
}

if (vrmlok || x3dshow) {
  document.write('<P><FONT SIZE=-1 FACE="Arial,Helvetica">Use the mouse, arrow keys, or Page Up/Down to rotate the model.<BR>View the ');
}
if (vrmlok && !x3dshow) {
  document.write('<A HREF="./' + wrlfile + '">VRML model</A> or an equivalent <A HREF="./' + x3dfile + '">X3D model</A>');
} else if (x3dshow && !vrmlok) {
  document.write('<A HREF="./' + x3dfile + '">X3D model</A> or an equivalent <A HREF="./' + wrlfile + '">VRML model</A>');
} else if (vrmlok && x3dshow) {
  document.write('<A HREF="./' + wrlfile + '">VRML model</A> or <A HREF="./' + x3dfile + '">X3D model</A>');
}
if (vrmlok || x3dshow) {
  document.write(' in a new window.</FONT>');
}

if (!vrmlok && !x3dshow) {
  document.write('<FONT SIZE=-1 FACE="Arial,Helvetica">');
  if (navigator.appName == "Microsoft Internet Explorer") {
    document.write('Some VRML and X3D plugins are not detected in Internet Explorer.<P>');
  }
  document.write('If a VRML or X3D plugin is installed,<BR>then try displaying the ');
  document.write('<A HREF="./' + wrlfile + '">VRML model</A> or <A HREF="./' + x3dfile + '">X3D model</A>.');
  document.write('</FONT>');
}


var hexchars = "012345678abcdef";

function dehex8(x){

    var bytes = new Array();
    for (var cc = 0, count = x.length(); cc < count; ){
        var c0 = x.charAt(cc++);
        if (cc < count){
            var c1 = x.charAt(cc++);
            var b = (hexchars.indexOf(c0)<<4)+(hexchars.indexOf(c0));
            bytes.push(b);
        }
        else {
            var b = hexchars.indexOf(c0);
            bytes.push(b);
        }
    }
    return bytes;
}
function dehex32(x){
    var words = new Array();
    var bytes = dehex8(x);
    for (var cc = 0, count = (bytes.length * 8); cc < count; cc += 8){
        words[cc>>5] |= (bytes[cc / 8] & 0xff) << (24-(cc%32));
    }
    return words;
}

var line;
while (line = readline()){
    var vector = line.split(':');
    var vectorMessage = vector[0];
    var vectorDigest = vector[1];
    var result = hex_sha1(vectorMessage);
    if (result == vectorDigest)
        print('ok '+vectorMessage);
    else {
        print('err '+vectorMessage);
        print('\t'+vectorDigest);
        print('\t'+result);
        quit();
    }
}

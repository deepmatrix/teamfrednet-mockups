
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

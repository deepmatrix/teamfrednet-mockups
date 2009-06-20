/*  -*-java-*-
 *
 */
function bpad(){
    var a = Math.floor(Math.random()*0xffffffff);
    var b = Math.floor(Math.random()*0xffffffff);
    var words = new Array(a,b);
    this.hex = binb2hex(words);
    this.update = function(s){
        this.string = this.hex + s;
    };
    this.digest = function(){
        this.dig = hex_sha1(this.string);
        return this.dig;
    };
}
function authenticate(){
    var form = document.forms.udb;
    var usr = form.usr.value;
    var pas = form.pas.value;
    if (usr && pas){
        var pad = new bpad();
        form.pad.value = pad.hex;
        pad.update(pas);
        var md = pad.digest();
        form.sig.value = md;
        form.pas.value = 'xxxxxxxxxxxxxxxxxxxxxxxxx';
        message(pad.hex+' '+md);
    }
}

function version(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function(){
        if (4 == this.readyState){
            message(this.responseText);
        }
    };
    xhr.open("GET","/version.txt",true);
    xhr.send(null);
}
function message(msg, type){
    if (msg){
        var mel = document.getElementById('msg');
        if (mel){
            if (type)
                mel.innerHTML = '<span class="'+type+'">'+msg+'</span>';
            else
                mel.innerHTML = '<span>'+msg+'</span>';
        }
    }
}
function messenger(){
    var map = new Object();

    window.location.search.replace(new RegExp( "([^?=&]+)(=([^&]*))?", "g" ),
                                   function( $0, $1, $2, $3 ){ map[ $1 ] = $3.replace('+',' ','g'); } );

    var msg = map['msg'];
    if (msg)
        message(msg, map['type']);
    else
        version();
}
function initfocus(){
}
function init(){
    initfocus();
    messenger();
}

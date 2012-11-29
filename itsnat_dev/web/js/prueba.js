
var script = document.createElement("script");
script.src = "js/prueba2.js?timeout=" + new Date().getTime();
var body = document.getElementsByTagName("body")[0];
body.appendChild(script);

/*
function addNode()
{
    document.createTextNode();
}

addNode();
window.setInterval(addNode, delay, param1, param2)

*/


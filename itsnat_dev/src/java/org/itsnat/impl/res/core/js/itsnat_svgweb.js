/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function itsnat_fix_svgweb(win,msieOld,metaPos)
{
    if (!win.svgweb) return;

    // Para insertar nuevos nodos root SVG para SVGWeb existe svgweb.appendChild pero no svgweb.insertBefore (svgweb-2009-11-23-Gelatinous-Cube).
    // Dentro del svgweb.appendChild se hace un parent.appendChild() normal,
    // redefinimos temporalmente este parent.appendChild para hacer un parent.insertBefore, asi nos vale el codigo del svgweb.appendChild
    // para hacer un insertBefore indirectamente
    win.svgweb.insertBefore = function (node,nodeRef,parentNode)
    {
        var oldFunc = parentNode.appendChild;
        parentNode.appendChild = function(node)
        {
            if (nodeRef._handler) nodeRef = nodeRef._handler.flash;
            this.insertBefore(node,nodeRef);
        };
        this.appendChild(node,parentNode);
        parentNode.appendChild = oldFunc;
    }

    // SVGWeb en los eventos load y SVGLoad captura las excepciones lanzadas y el mensaje lo pasa a console.log(msg) pero la funcion por defecto
    // no hace nada por lo que no vemos el error.
    // No podemos provocar una excepcion ni un alert pues SVGWeb utiliza el log() a veces
    // con mensajes informativos (no error), por ejemplo 'Forcing Flash SVG viewer for this browser'.
    // Acumulamos los mensajes en un atributo especial que se puede ver con un alert de window.console.messages

    win.console.log = function(msg) { if (!this.messages || this.messages.length > 50000) this.messages = ""; this.messages += msg + "\n"; };

    if (msieOld)
    {
        // Movemos los dos elementos auxiliares que necesita el SVGWeb en MSIE (no en otros navegadores W3C incluso usando flash).
        // Uno de ellos, el que tiene id "__ie__svg__onload" se crea bajo el <head>
        // al ejecutar el script del SVGWeb (en carga por tanto).
        // El otro con id "__htc_container" se crea al final del <body> cuando se procesa el primer elemento SVG.
        // Movemos estos elementos bajo el <meta name="svg.render.forceflash" ...>
        // como el <meta> no admite nodos hijo, lo cambiamos por un nuevo elemento <div>
        // de esta manera en el servidor seguira el <meta> aunque en el cliente sea otra cosa.
        // No lo hacemos con el <script> que incluye el SVGWeb porque no tenemos claro
        // si el svg.js es el de SVGWeb (ademas la version descomprimida tiene nombre diferente).

        var meta = win.document.getElementsByTagName('meta')[metaPos];

        var elem = win.document.createElement("div");
        // Hacemos que el <div> se parezca lo mas posible al <meta> que reemplazamos
        for(var i = 0; i < meta.attributes.length; i++)
        {
            var attr = meta.attributes[i];
            if (attr.specified) elem.setAttribute(attr.name,attr.value); // El setAttribute y el attr.value con style es erroneo (null) pero es rarisimo en un <meta>
        }
        meta.parentNode.replaceChild(elem,meta);

        var ieOnload = win.document.getElementById("__ie__svg__onload");
        elem.appendChild(ieOnload); // Esta en el <head>

        // El elemento especial "__htc_container" se crea en cuanto
        // se procesa el primer elemento SVG, si lo creamos
        // nosotros desde el principio el SVGWeb usara el nuestro.
        // La documentacion interna de SVGWeb dice que bajo <head> no
        // funciona bien el HTC pero por ahora no he encontrado ningun problema (eventos de cambio o algo asi no se emiten)
        var htcCont = win.document.getElementById("__htc_container");
        if (htcCont == null)  // Siempre sera null pues los elementos SVG se procesan en el onload pero por si acaso
        {
          htcCont = win.document.createElement("div");
          htcCont.id = "__htc_container";
        }
        elem.appendChild(htcCont);
    } // if (msieOld)
}


function pkg_itsnat_svgweb(pkg)
{
    pkg.SVGWebHTMLDocument = SVGWebHTMLDocument;

function SVGWebHTMLDocument()  // Usar como extension no como herencia
{
    // Nota: se evita usar tagName porque no esta definido en nodos fake.

    this.SVGWebHTMLDocument_super_createTextNode = this.createTextNode;
    this.createTextNode = createTextNode;
    function createTextNode(parentNode,value)
    {
        if (this.isSVGWebNode(parentNode)) return this.doc.createTextNode(value,true);
        else return this.SVGWebHTMLDocument_super_createTextNode(parentNode,value);
    }

    this.SVGWebHTMLDocument_super_getValidNode = this.getValidNode;
    this.getValidNode = getValidNode;
    function getValidNode(node) // Redefine
    {
        if (node == null) return null;
        if (this.isSVGWebNode(node)) return node;
        if (node.nodeType == 1)
        {
            if ((node.className == "embedssvg")|| // <object> (MSIE) o <embed> (W3C)
               ((node.nodeName.toLowerCase() == "script")&&(node.type == "image/svg+xml")))
            {
                if (node.itsNatSVGWebWrapperRemoved) return this.getValidNode(node.nextSibling);

                // Cuidado al usar un alert aqui depurando, pues mientras se muestra, el SVGLoad puede cambiar el estado del node asincronamente
                if (node.documentElement)
                {
                    var svgRootElem = node.documentElement;
                    if (!svgRootElem.itsNatSVGWebParentNode) svgRootElem.itsNatSVGWebParentNode = node.parentNode;
                    if (!svgRootElem.itsNatSVGWebWrapper) svgRootElem.itsNatSVGWebWrapper = node;
                    return svgRootElem; // object/embed ya renderizado
                }
                else if (node.itsNatOldSVGWebRoot) return node.itsNatOldSVGWebRoot; // Insercion dinamica no renderizado
                else return node;
            }
        }
        return this.SVGWebHTMLDocument_super_getValidNode(node);
    }

    this.SVGWebHTMLDocument_super_getParentNode = this.getParentNode;
    this.getParentNode = getParentNode;
    function getParentNode(node) // Redefine
    {
        if (node.itsNatSVGWebParentNode) return node.itsNatSVGWebParentNode;
        else return this.SVGWebHTMLDocument_super_getParentNode(node);
    }

    // La funcion importNodeSVGWeb es llamada por setInnerXML (MSIE y W3C)
    // para insercion de codigo SVG gestionado por SVGWeb.
    this.importNodeSVGWeb = importNodeSVGWeb;
    function importNodeSVGWeb(node,deep)
    {
        var msieOld = this.browser.isMSIEOld();
        switch(node.nodeType)
        {
            case 1:
                var newNode = this.doc.createElementNS(svgns,node.tagName);
                for (var i = 0; i < node.attributes.length; i++)
                {
                    var attr = node.attributes[i];
                    if (attr.name.indexOf("xmlns") == 0) continue; // Problemas en W3C
                    if (msieOld && !attr.specified) continue;
                    this.setAttribute(newNode,attr.name,this.getAttribute(node,attr.name));
                }
                if (deep)
                    for (var i = 0; i < node.childNodes.length; i++)
                    {
                        var child = this.importNodeSVGWeb(node.childNodes[i],true);
                        if (child != null) newNode.appendChild(child);
                    }
                    return newNode;
            case 3: // Node.TEXT_NODE
            case 4: // Node.CDATA_SECTION_NODE
                return this.doc.createTextNode(node.data,true);
            case 8: // Node.COMMENT_NODE
                return null; // No soportados (y filtrados)
            default:
                return null;
        }
    }

    this.isSVGWebRoot = isSVGWebRoot;
    function isSVGWebRoot(node)
    {
        // Si node fuera un <script> or object/embed de SVGWeb es que no ha sido renderizado y puede eliminarse de forma normal
        if (!this.isSVGWebNode(node)) return false; // No es nodo SVGWeb
        return (node.nodeName.toLowerCase() == 'svg'); // Es fake SVGWeb y nombre propio de root
    }

    this.getSVGWebWrapper = getSVGWebWrapper;
    function getSVGWebWrapper(node)
    {
        if (node._handler) return node._handler.flash; // Renderizado
        else return node.itsNatSVGWebWrapper; // No renderizado, insercion dinamica
    }

    this.setUpNewSVGWebRoot = setUpNewSVGWebRoot;
    function setUpNewSVGWebRoot(newChild,wrapper) // wrapper puede ser el <script> temporal o el embed/object
    {
        wrapper.itsNatOldSVGWebRoot = newChild;
        newChild.itsNatSVGWebParentNode = wrapper.parentNode; // Necesario para un posible posterior registro de SVGLoad via ItsNat
        newChild.itsNatSVGWebWrapper = wrapper;
        var func = function()
        {
            var currRoot = this;
            var wrapper = arguments.callee.wrapper;
            var oldRoot = wrapper.itsNatOldSVGWebRoot;
            wrapper.itsNatOldSVGWebRoot = null; // Indica ya renderizado
            if (oldRoot.itsNatCacheId) // Pudo ser cacheado al registrar un SVGLoad del usuario
            {
                currRoot.itsNatCacheId = oldRoot.itsNatCacheId;
                arguments.callee.itsNatDoc.nodeCacheById.put(currRoot.itsNatCacheId,currRoot);
            }
        };
        func.itsNatDoc = this;
        func.wrapper = wrapper;
        newChild.addEventListener("SVGLoad",func,false);
    }

    this.SVGWebHTMLDocument_super_removeChild = this.removeChild;
    this.removeChild = removeChild;
    function removeChild(node)
    {
        if (node == null) return; // Nodo de texto filtrado
        if (this.isSVGWebRoot(node))
        {
            var parentNode = this.getParentNode(node);
            this.win.svgweb.removeChild(node,parentNode);
            if (this.browser.isMSIEOld() && node.itsNatSVGWebWrapper)
                node.itsNatSVGWebWrapper.itsNatSVGWebWrapperRemoved = true; // La eliminación del <object> del DOM se hace asíncronamente
        }
        else this.SVGWebHTMLDocument_super_removeChild(node);
    }

    // Al insertar un SVG root, en su lugar se inserta realmente un EMBED en W3C y un SCRIPT en MSIE (este sera substituido despues por un OBJECT)
    this.SVGWebHTMLDocument_super_appendChild = this.appendChild;
    this.appendChild = appendChild;
    function appendChild(parentNode,newChild)
    {
        if (this.isSVGWebRoot(newChild))
        {
            this.win.svgweb.appendChild(newChild,parentNode);
            this.setUpNewSVGWebRoot(newChild,parentNode.lastChild);
        }
        else this.SVGWebHTMLDocument_super_appendChild(parentNode,newChild);
    }


    this.SVGWebHTMLDocument_super_insertBefore = this.insertBefore;
    this.insertBefore = insertBefore;
    function insertBefore(parentNode,newChild,childRef)
    {
        if (childRef == null) { this.appendChild(parentNode,newChild); return; }
        if (this.isSVGWebRoot(newChild))
        {
            if (this.isSVGWebNode(childRef)) childRef = this.getSVGWebWrapper(childRef); // Es un nodo root SVG adjacente al nuevo nodo root SVG, debemos usar el verdadero nodo <object>/<embed>
            this.win.svgweb.insertBefore(newChild,childRef,parentNode);  // svgweb.insertBefore() no esta en SVGWeb, es añadido por ItsNat en otro lugar.
            this.setUpNewSVGWebRoot(newChild,childRef.previousSibling);
        }
        else
        {
            if (!this.isSVGWebNode(newChild) && this.isSVGWebNode(childRef)) childRef = this.getSVGWebWrapper(childRef); // Es un nodo root SVG adjacente al nuevo nodo normal, debemos usar el verdadero nodo <object>/<embed>
            this.SVGWebHTMLDocument_super_insertBefore(parentNode,newChild,childRef);
        }
    }

    this.fixSVGWebNodeListeners = fixSVGWebNodeListeners;
    function fixSVGWebNodeListeners(node)
    {   // NO, un delete NO sirve para eliminar un elemento de un array
        if (node._detachedListeners) this.fixSVGWebNodeListeners2(node._detachedListeners);
        if (node._keyboardListeners) this.fixSVGWebNodeListeners2(node._keyboardListeners);
    }

    this.fixSVGWebNodeListeners2 = fixSVGWebNodeListeners2;
    function fixSVGWebNodeListeners2(array)
    {
        for(var i = 0; i < array.length; i++)
            if (typeof array[i] == "undefined") { array.splice(i,1); i--; }
    }
}

} // pkg_itsnat_svgweb

function pkg_itsnat_svgweb_msie_old(pkg)
{
    pkg.SVGWebMSIEOldHTMLDocument = SVGWebMSIEOldHTMLDocument;

function SVGWebMSIEOldHTMLDocument()  // Usar como extension no como herencia
{
    this.isSVGWebNode = isSVGWebNode; // Redefine
    function isSVGWebNode(node) { return node.fake || node._fakeNode; } // SVGWeb en IE 8 está mal y fake no está definido (lo está en _fakeNode pero no se recubierto bien node) cosa de usar Object.defineProperty en vez de HTC

    // SVGWeb captura nuestros listeners onload redefiniendo window.addEventListener
    // y window.attachEvent y capturando el window.onload y el atributo onload="",
    // tal que son ejecutados DESPUES de la carga completa de SVGWeb, pero lo hace regular pues no crea un evento.
    // El caso SVGLoad suele ser el de renderizacion en insercion dinamica

    this.SVGWebMSIEOldHTMLDocument_super_attachEvent = this.attachEvent;
    this.attachEvent = attachEvent;
    function attachEvent(node,type,func)
    {
      if (this.isSVGWebNode(node) || ((node == this.win) && ((type == "load")||(type == "SVGLoad"))) )
      {
          var win = this.win;
          var svgwebWrap = function(evt)
          {
              if ((type == "load")||(type == "SVGLoad")) // SVGWeb en este caso no crea un evento, simulamos un evento W3C
              {
                  evt = { type:type,target:null,currentTarget:null,bubbles:false,cancelable:false,timeStamp:0,eventPhase:2};
                  if (node == win) { evt.target = win.document; evt.currentTarget = win; }
                  else { evt.target = this; evt.currentTarget = this; } // "node" no vale pues es el falso nodo antes de renderizar
              }
              // SVGWeb genera eventos W3C pero ItsNat espera eventos MSIE
              var fakeEvent = { type:type,srcElement:evt.target,cancelBubble:false,returnValue:undefined,fromElement:null,toElement:null,screenX:evt.screenX,screenY:evt.screenY,clientX:evt.clientY,clientY:evt.clientY,ctrlKey:evt.ctrlKey,shiftKey:evt.shiftKey,altKey:evt.altKey,button:1,keyCode:0};
              arguments.callee.func(fakeEvent);
          };
          svgwebWrap.func = func;
          func.svgwebWrap = svgwebWrap;
          if (node == this.win) node.attachEvent("on" + type,svgwebWrap,false); // Sera type "load"
          else node.addEventListener(type,svgwebWrap,false);
      }
      else this.SVGWebMSIEOldHTMLDocument_super_attachEvent(node,type,func);
    }

    this.SVGWebMSIEOldHTMLDocument_super_detachEvent = this.detachEvent;
    this.detachEvent = detachEvent;
    function detachEvent(node,type,func)
    {
        if (this.isSVGWebNode(node) || ((node == this.win) && ((type == "load")||(type == "SVGLoad"))) )
        {
          if (node == this.win) node.detachEvent("on" + type,func.svgwebWrap,false); // Sera type "load"
          else
          {
              node.removeEventListener(type,func.svgwebWrap,false);
              this.fixSVGWebNodeListeners(node);
          }
        }
        else this.SVGWebMSIEOldHTMLDocument_super_detachEvent(node,type,func);
    }

    this.SVGWebMSIEOldHTMLDocument_super_setAttribute = this.setAttribute;
    this.setAttribute = setAttribute;
    function setAttribute(elem,name,value)
    {
        if (this.isSVGWebNode(elem)) elem.setAttribute(name,value); // style.cssText no funciona en SVGWeb
        else this.SVGWebMSIEOldHTMLDocument_super_setAttribute(elem,name,value);
    }

    this.SVGWebMSIEOldHTMLDocument_super_getAttribute = this.getAttribute;
    this.getAttribute = getAttribute;
    function getAttribute(elem,name)
    {
        if (this.isSVGWebNode(elem)) return elem.getAttribute(name); // style.cssText no funciona en SVGWeb
        else return this.SVGWebMSIEOldHTMLDocument_super_getAttribute(elem,name);
    }

    this.SVGWebMSIEOldHTMLDocument_super_setTextData = this.setTextData;
    this.setTextData = setTextData;
    function setTextData(parentNode,textNode,value)
    {
        if (this.isSVGWebNode(textNode))
        {
            try{ this.SVGWebMSIEOldHTMLDocument_super_setTextData(parentNode,textNode,value); }
            catch(ex) // El text.data = "..." no funciona bien en el SVGLoad tras insercion dinamica.
            {
                var sibling = textNode.nextSibling;
                parentNode.removeChild(textNode);
                textNode = this.doc.createTextNode(value,true);
                if (sibling == null) parentNode.appendChild(textNode);
                else parentNode.insertBefore(textNode,sibling);
            }
        }
        else this.SVGWebMSIEOldHTMLDocument_super_setTextData(parentNode,textNode,value);
    }
}

} // pkg_itsnat_svgweb_msie_old

function pkg_itsnat_svgweb_w3c(pkg)
{
    pkg.SVGWebW3CHTMLDocument = SVGWebW3CHTMLDocument;

function SVGWebW3CHTMLDocument()  // Usar como extension no como herencia
{
    this.isSVGWebNode = isSVGWebNode; // Redefine
    function isSVGWebNode(node) { return node.fake;  }

    this.SVGWebW3CHTMLDocument_super_addEventListener = this.addEventListener;
    this.addEventListener = addEventListener;
    function addEventListener(node,type,func,useCapture)
    {
        if ( (this.isSVGWebNode(node) || (node == this.win)) && ((type == "load")||(type == "SVGLoad")) )
        {
            // SVGWeb en "load" o "SVGLoad" no crea un evento, simulamos un evento W3C
            var win = this.win;
            var svgwebWrap = function(evt)
            {
                evt = { type:type,target:null,currentTarget:null,bubbles:false,cancelable:false,timeStamp:0,eventPhase:2};
                evt.preventDefault = function(){};
                evt.stopPropagation = function(){};
                if (node == win) { evt.target = win.document; evt.currentTarget = win; }
                else { evt.target = this; evt.currentTarget = this; } // "node" no vale pues es el falso nodo antes de renderizar

                arguments.callee.func(evt);
            };
            svgwebWrap.func = func;
            func.svgwebWrap = svgwebWrap;
            node.addEventListener(type,svgwebWrap,useCapture);
        }
        else
        {
            this.SVGWebW3CHTMLDocument_super_addEventListener(node,type,func,useCapture);
            if (!this.isSVGWebNode(node) && (node != this.win) && (type == "SVGLoad")) // Es modo nativo (aunque este SVGWeb), simulamos el SVGLoad de SVGWeb
            {
                var evt = this.doc.createEvent("Event");
                evt.initEvent("SVGLoad",false,false);
                func(evt); // node.dispatchEvent no funciona (no hace nada)
            }
        }
    }

    this.SVGWebW3CHTMLDocument_super_removeEventListener = this.removeEventListener;
    this.removeEventListener = removeEventListener;
    function removeEventListener(node,type,func,useCapture)
    {
        if ( (this.isSVGWebNode(node) || (node == this.win)) && ((type == "load")||(type == "SVGLoad")) )
        {
            node.removeEventListener(type,func.svgwebWrap,useCapture);
            this.fixSVGWebNodeListeners(node);
        }
        else this.SVGWebW3CHTMLDocument_super_removeEventListener(node,type,func,useCapture);
    }

    this.SVGWebW3CHTMLDocument_super_getChildNode = this.getChildNode;
    this.getChildNode = getChildNode;
    function getChildNode(i,parentNode,validNode)
    {
        if (this.isSVGWebNode(parentNode) && this.browser.isOperaOld()) // Soluciona algo pero en Opera funciona bastante mal
        {
            var node = parentNode.firstChild;
            for(var j = 1; j < i + 1; j++) node = node.nextSibling;
            return node;
        }
        return this.SVGWebW3CHTMLDocument_super_getChildNode(i,parentNode,validNode);
    }
}

} // pkg_itsnat_svgweb_w3c

function itsnat_init_svgweb(win,itsnat,msieOld)
{
    if (!win.svgweb) return;
    pkg_itsnat_svgweb(itsnat);
    if (msieOld) pkg_itsnat_svgweb_msie_old(itsnat);
    else pkg_itsnat_svgweb_w3c(itsnat);
}

function itsnat_extend_doc_svgweb(itsNatDoc)
{
    if (!itsNatDoc.win.svgweb) return; // No soporta SVGWeb o JS no bien incluido
    itsNatDoc.SVGWebHTMLDocument = itsnat.SVGWebHTMLDocument;
    itsNatDoc.SVGWebHTMLDocument();
    if (itsNatDoc.browser.isMSIEOld())
    {
        itsNatDoc.SVGWebMSIEOldHTMLDocument = itsnat.SVGWebMSIEOldHTMLDocument;
        itsNatDoc.SVGWebMSIEOldHTMLDocument();
    }
    else
    {
        itsNatDoc.SVGWebW3CHTMLDocument = itsnat.SVGWebW3CHTMLDocument;
        itsNatDoc.SVGWebW3CHTMLDocument();
    }
}


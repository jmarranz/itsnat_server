/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_w3c(pkg)
{
    pkg.DOMPathResolverW3C = DOMPathResolverW3C;
    pkg.DOMPathResolverHTMLDocW3C = DOMPathResolverHTMLDocW3C;
    pkg.DOMPathResolverOtherNSDocW3C = DOMPathResolverOtherNSDocW3C;
    pkg.W3CDOMEvent = W3CDOMEvent;
    pkg.getW3CEventUtil = getW3CEventUtil;
    pkg.W3COtherNSDocument = W3COtherNSDocument;
    pkg.W3CHTMLDocument = W3CHTMLDocument;
    pkg.W3CEventUtil_SINGLETON = new W3CEventUtil();
    pkg.W3CUIEventUtil_SINGLETON = new W3CUIEventUtil();
    pkg.W3CMouseEventUtil_SINGLETON = new W3CMouseEventUtil();
    pkg.W3CMutationEventUtil_SINGLETON = new W3CMutationEventUtil();
    pkg.W3CKeyEventUtil_SINGLETON = new W3CKeyEventUtil();

function DOMPathResolverW3C(itsNatDoc) // Es un "mixin"
{
    this.isFiltered = isFiltered;

    function isFiltered(node) // incluir aqui futuros nodos intrusivos de add-ons
    {
        if (this.itsNatDoc.browser.isGecko()) // Por si acaso aunque via cacheado ya se aporta tolerancia a su presencia
        {
            if (node.id == "_firebugConsole") return true; // Hasta la v1.5 existe
            if (node.sheet && (node.sheet.cssRules.length > 0) &&
               (node.sheet.cssRules[0].selectorText == ".firebugCanvas")) return true;
        }
        return false;
    }
}

function W3CDOMEvent(evt,listener)
{
    this.DOMStdEvent = itsnat.DOMStdEvent;
    this.DOMStdEvent(evt,listener);
    if (this.itsNatDoc.browser.isWebKit() && SafariRejectDOMNodeRemoved(evt))
        this.sendEvent = function () { }; // solo afecta a este evento (el objeto no se puede reutilizar)

    this.getTarget = function () { return this.getNativeEvent().target; };
}

function getW3CEventUtil(typeCode)
{
    switch(typeCode)
    {
        case 0: return itsnat.W3CEventUtil_SINGLETON; // DOMStdEventTypeInfo.UNKNOWN_EVENT
        case 1: return itsnat.W3CUIEventUtil_SINGLETON; // UI_EVENT
        case 2: return itsnat.W3CMouseEventUtil_SINGLETON; // MOUSE_EVENT
        case 3: return itsnat.W3CEventUtil_SINGLETON; // HTML_EVENT
        case 4: return itsnat.W3CMutationEventUtil_SINGLETON; // MUTATION_EVENT
        case 5: return itsnat.W3CKeyEventUtil_SINGLETON; // KEY_EVENT
    }
    return null; // ?
}

function W3CEventUtil()
{
    this.genParamURL = genParamURL;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_target=" + itsNatDoc.getStringPathFromNode(evt.target);
        url += "&itsnat_evt_eventPhase=" + evt.eventPhase;
        url += "&itsnat_evt_bubbles=" + evt.bubbles;
        url += "&itsnat_evt_cancelable=" + evt.cancelable;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        evtB.type = evtN.type; evtB.target = evtN.target; evtB.eventPhase = evtN.eventPhase;
        evtB.bubbles = evtN.bubbles; evtB.cancelable = evtN.cancelable;
    }
}

function W3CUIEventUtil()
{
    this.W3CEventUtil = W3CEventUtil;
    this.W3CEventUtil();

    this.W3CUIEventUtil_super_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.W3CUIEventUtil_super_backupEvent = this.backupEvent;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.W3CUIEventUtil_super_genParamURL(evt,itsNatDoc);
        url += "&itsnat_evt_detail=" + evt.detail;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CUIEventUtil_super_backupEvent(evtN,evtB);
        evtB.detail = evtN.detail;
    }
}

function W3CMouseEventUtil()
{
    this.W3CUIEventUtil = W3CUIEventUtil;
    this.W3CUIEventUtil();

    this.MouseEventUtil = itsnat.MouseEventUtil;
    this.MouseEventUtil();

    this.W3CMouseEventUtil_super_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.W3CMouseEventUtil_super_backupEvent = this.backupEvent;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.W3CMouseEventUtil_super_genParamURL(evt,itsNatDoc);
        url += this.genMouseEventURL(evt,itsNatDoc);
        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_relatedTarget=" + itsNatDoc.getStringPathFromNode(evt.relatedTarget);
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CMouseEventUtil_super_backupEvent(evtN,evtB);
        this.backupMouseEvent(evtN,evtB);

        evtB.metaKey = evtN.metaKey;
        evtB.relatedTarget = evtN.relatedTarget;
    }
}

function W3CMutationEventUtil()
{
    this.W3CEventUtil = W3CEventUtil;
    this.W3CEventUtil();

    this.W3CMutationEventUtil_super_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.W3CMutationEventUtil_super_backupEvent = this.backupEvent;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.W3CMutationEventUtil_super_genParamURL(evt,itsNatDoc);

        var relatedNodePath;
        var newValue = evt.newValue;
        var type = evt.type;
        if (type == "DOMNodeInserted")
        {
            var newNode = evt.target;
            if (itsNatDoc.browser.isASVRenesis())
                newValue = itsNatDoc.win.printNode(newNode);
            else
            {
                var serializer = new XMLSerializer();
                newValue = serializer.serializeToString(newNode); // Alternativa (solo HTML), simular outerHTML usando innerHTML y un padre auxiliar: http://www.webdeveloper.com/forum/showthread.php?t=67841
            }
            relatedNodePath = itsNatDoc.getStringPathFromNode(evt.relatedNode); // El nodo padre

            // El path del nodo de referencia en el servidor es el que ocupa el nodo recien insertado (el target)
            // El nodo de referencia puede ser un nodo texto y en el path del nuevo
            // nodo no esta el sufijo [t], se lo indicamos al servidor si es necesario
            var refChildPath;
            var refChild = evt.target.nextSibling;
            if (refChild != null)
            {
                refChildPath = itsNatDoc.getStringPathFromNode(evt.target);
                if (refChild.nodeType == 3) // Node.TEXT_NODE
                    refChildPath += "[t]";
            }
            else refChildPath = null; // ha sido un appendChild

            url += "&itsnat_evt_refChild=" + refChildPath;
        }
        else if (type == "DOMAttrModified")
        {
            relatedNodePath = null; // Se calcula en el servidor
            var attrChange = evt.attrChange;
            if (!attrChange) // Caso ASV v6 (pues nunca es cero si es correcto)
            {
                if (evt.target.hasAttribute(evt.attrName)) attrChange = 1; // MODIFICATION  Se corregira en el servidor el caso ADDITION
                else attrChange = 3; // REMOVAL
            }
            url += "&itsnat_evt_attrName=" + evt.attrName;
            url += "&itsnat_evt_attrChange=" + attrChange;
        }
        else relatedNodePath = itsNatDoc.getStringPathFromNode(evt.relatedNode);

        // En el evento "DOMNodeRemoved" se procesa antes de que se haya quitado
        // el nodo, por lo que al servidor le llegara el path del nodo a quitar (en modo asincrono funciona parece que envia antes)
        url += "&itsnat_evt_relatedNode=" + relatedNodePath;
        url += "&itsnat_evt_prevValue=" + encodeURIComponent(evt.prevValue);
        url += "&itsnat_evt_newValue=" + encodeURIComponent(newValue);

        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CMutationEventUtil_super_backupEvent(evtN,evtB);

        evtB.relatedNode = evtN.relatedNode; evtB.prevValue = evtN.prevValue;
        evtB.attrName = evtN.attrName; evtB.attrChange = evtN.attrChange;
    }
}

function W3CKeyEventUtil()
{
    this.W3CUIEventUtil = W3CUIEventUtil;
    this.W3CUIEventUtil();

    this.W3CKeyEventUtil_super_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.W3CKeyEventUtil_super_backupEvent = this.backupEvent;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.W3CKeyEventUtil_super_genParamURL(evt,itsNatDoc);

        var charCode = 0;
        if (evt.type == "keypress")
        {
            charCode = evt.charCode;
            if (typeof charCode == "undefined") charCode = 0; // Opera y BlackBerryOld no tienen
            if ((charCode == 0) && evt.itsnat_charCode) charCode = evt.itsnat_charCode; // WebKit y BlackBerryOld
        }
        url += "&itsnat_evt_charCode=" + charCode;

        if (itsNatDoc.browser.isWebKit()||itsNatDoc.browser.isBlackBerryOld())
        {
            url += "&itsnat_evt_keyIdentifier=" + encodeURIComponent(evt.keyIdentifier);
            url += "&itsnat_evt_keyLocation=" + evt.keyLocation;
        }
        url += "&itsnat_evt_keyCode=" + evt.keyCode;
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_metaKey=" + evt.metaKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.W3CKeyEventUtil_super_backupEvent(evtN,evtB);

        evtB.keyCode = evtN.keyCode; evtB.charCode = evtN.charCode;
        evtB.altKey = evtN.altKey; evtB.ctrlKey = evtN.ctrlKey;
        evtB.metaKey = evtN.metaKey; evtB.shiftKey = evtN.shiftKey;

        evtB.itsnat_charCode = evtN.itsnat_charCode;
        evtB.keyIdentifier = evtN.keyIdentifier;
        evtB.keyLocation = evtN.keyLocation;
    }
}

function SafariRejectDOMNodeRemoved(evt)
{
    // Safari 3.0 y 3.1 emite dos eventos seguidos DOMNodeRemoved, evitamos el segundo
    // http://lists.webkit.org/pipermail/webkit-dev/2007-July/002067.html
    if (evt.type != "DOMNodeRemoved") return false;

    var removed = evt.target;
    if (!removed.itsnat_safari_removed)
    {
        removed.itsnat_safari_removed = true;
        return false; // primero
    }
    else
    {
        delete removed.itsnat_safari_removed;
        return true; // segundo, rechazado!
    }
}

function W3CDocument()
{
    this.setInnerXML = setInnerXML;
    this.setInnerXMLMSIE9 = setInnerXMLMSIE9;
    this.importNodeMSIE9 = importNodeMSIE9;
    this.addDOMEventListener2 = addDOMEventListener2;
    this.removeDOMEventListener2 = removeDOMEventListener2;
    this.addEventListener = addEventListener;
    this.removeEventListener = removeEventListener;
    this.addAttachUnloadListener2 = addAttachUnloadListener2;
    this.dispatchEvent = dispatchEvent;

    function setInnerXML(elem,value)
    {
        if (this.browser.isMSIE9()) { this.setInnerXMLMSIE9(elem,value); return; } // DOMParser no soportado

        var svgweb = elem.fake;
        var rootElem;
        if (this.browser.isASVRenesis()||this.browser.isBatik())
        {
            var docFrag = this.win.parseXML(value,this.doc);
            rootElem = docFrag.firstChild; // ya importados
        }
        else if (svgweb)
        {
            value = value.replace(/\>\s+\</gm, '><'); // Los nodos de texto inutiles dan problemas
            rootElem = new DOMParser().parseFromString(value,"text/xml").documentElement;
            rootElem = this.importNodeSVGWeb(rootElem,true);
        }
        else
        {
            rootElem = new DOMParser().parseFromString(value,"text/xml").documentElement;
            rootElem = this.doc.importNode(rootElem,true); // Nota: importNode no funciona en Safari con DocumentFragment
        }

        var child = rootElem.firstChild;
        while (child != null)
        {
            if (svgweb) rootElem.removeChild(child);  // Necesario!!
            elem.appendChild(child);
            child = rootElem.firstChild;
        }
    }

    function setInnerXMLMSIE9(elem,value)
    {
        // Estudiar para SVGWeb cuando soporte IE 9
        var xmlDoc = new ActiveXObject("Msxml2.DOMDocument.3.0");
        xmlDoc.async = false;
        xmlDoc.loadXML(value);
        var rootElem = xmlDoc.documentElement;
        var child = rootElem.firstChild;
        while (child != null)
        {
            var newChild = this.importNodeMSIE9(child,true);
            if (newChild != null) elem.appendChild(newChild);
            child = child.nextSibling;
        }
    }

    function importNodeMSIE9(node,deep)
    {
        switch(node.nodeType)
        {
          case 1: // Node.ELEMENT_NODE
            var newNode = this.doc.createElementNS(node.namespaceURI,node.tagName); // Si tiene prefijo/namespace ItsNat lo ha declarado previamente
            for (var i = 0; i < node.attributes.length; i++)
            {
                var attr = node.attributes[i];
                if (!attr.specified) continue; // creo que ahora es siempre true
                if (attr.namespaceURI) this.setAttributeNS(newNode,attr.namespaceURI,attr.name,this.getAttribute(node,attr.name));
                else this.setAttribute(newNode,attr.name,this.getAttribute(node,attr.name));
            }
            if (deep)
              for (var i = 0; i < node.childNodes.length; i++)
              {
                var child = this.importNodeMSIE9(node.childNodes[i],true);
                if (child != null) newNode.appendChild(child);
              }
            return newNode;
          case 3: // Node.TEXT_NODE:
            return this.doc.createTextNode(node.data);
          case 4: // Node.CDATA_SECTION_NODE
            return this.doc.createCDATASection(node.data);
          case 8: // Node.COMMENT_NODE:
            return this.doc.createComment(node.data);
          default:
            return null;
        }
    }

    function addDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var w3cHandler = function(evt) { arguments.callee.listenerWrapper.dispatchEvent(evt); };
        w3cHandler.listenerWrapper = listenerWrapper;
        w3cHandler.toString = function() { return "listener id:" + this.listenerWrapper.getId(); }; // SVGWeb dracolisk (absurdamente) usa toString() como identidad del listener
        listenerWrapper.w3cHander = w3cHandler;
        this.addEventListener(node,type,w3cHandler,useCapture);
    }

    function removeDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        this.removeEventListener(node,type,listenerWrapper.w3cHander,useCapture);
    }

    function addEventListener(node,type,func,useCapture) { node.addEventListener(type,func,useCapture); }

    function removeEventListener(node,type,func,useCapture) { node.removeEventListener(type,func,useCapture); }

    function addAttachUnloadListener2(node,type,listener) { this.addEventListener(node,type,listener,false); }

    function dispatchEvent(node,type,evt) { return node.dispatchEvent(evt); }
}

function DOMPathResolverOtherNSDocW3C(itsNatDoc)
{
    this.DOMPathResolver = itsnat.DOMPathResolver;
    this.DOMPathResolver(itsNatDoc);

    this.DOMPathResolverW3C = itsnat.DOMPathResolverW3C;
    this.DOMPathResolverW3C(itsNatDoc); // Mixin

    this.DOMPathResolverW3C_isFiltered = this.isFiltered;
    this.isFiltered = isFiltered;

    function isFiltered(node)
    {
        if (this.DOMPathResolverW3C_isFiltered(node)) return true;
        if ((typeof node.id != "undefined")&&(node.id != null)&&(node.id.indexOf("itsnat_script_evt") == 0)) return true; // por si acaso pues se insertan al final del elem root (node.id puede ser null en Batik)
        return false;
    }
}


function W3COtherNSDocument()
{
    this.Document = itsnat.Document;
    this.Document();

    this.W3CDocument = W3CDocument;
    this.W3CDocument();

    this.createDOMPathResolver = createDOMPathResolver; // se redefine
    this.getVisualRootElement = getVisualRootElement;
    this.createScriptElement = createScriptElement;

    function createDOMPathResolver() { return new DOMPathResolverOtherNSDocW3C(this); }

    function getVisualRootElement() { return this.doc.documentElement; }

    function createScriptElement(url)
    {
        var ns = this.doc.documentElement.namespaceURI;
        var script;
        if ((ns == "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul") ||  // El <script> de XUL no ejecuta (FireFox 3.6.3)
            this.browser.isWebKit()) // En el <script> de SVG en WebKit el onload se dispara al instante
        {
            script = this.doc.createElementNS("http://www.w3.org/1999/xhtml","script");
            script.src = url;
        }
        else // SVG resto de casos (Opera, Gecko etc)
        {
            script = this.doc.createElementNS(ns,"script");
            script.setAttribute("type","application/ecmascript"); // por si acaso
            script.setAttributeNS("http://www.w3.org/1999/xlink","href",url);
        }
        return script;
    }
}

function DOMPathResolverHTMLDocW3C(itsNatDoc)
{
    this.DOMPathResolverHTMLDoc = itsnat.DOMPathResolverHTMLDoc;
    this.DOMPathResolverHTMLDoc(itsNatDoc);

    this.DOMPathResolverW3C = itsnat.DOMPathResolverW3C;
    this.DOMPathResolverW3C(itsNatDoc); // Mixin

}

function W3CHTMLDocument()
{
    this.HTMLDocument = itsnat.HTMLDocument;
    this.HTMLDocument();

    this.W3CDocument = W3CDocument;
    this.W3CDocument();

    this.createDOMPathResolver = createDOMPathResolver;
    this.getHTMLBody = getHTMLBody; // Redefinimos
    this.W3CHTMLDocument_super_setAttribute = this.setAttribute;
    this.setAttribute = setAttribute;
    this.W3CHTMLDocument_super_removeAttribute = this.removeAttribute;
    this.removeAttribute = removeAttribute;

    function createDOMPathResolver() { return new DOMPathResolverHTMLDocW3C(this); }

    function getHTMLBody()
    {
        if (this.doc.body) return this.doc.body;
        // En WebKits antiguos y XHTML, document NO es HTMLDocument (los elementos contenidos si son HTML DOM)
        return this.getChildNodeWithTagName(this.doc.documentElement,"body");
    }

    function setAttribute(elem,name,value)
    {
        this.W3CHTMLDocument_super_setAttribute(elem,name,value);
        if (this.browser.isBlackBerryOld()) // Para evitar que las dos primeras veces se ignoren visualmente (curioso)
        {
          var style = elem.style;
          var oldDisp = style.display;
          style.display = "none";
          style.display = oldDisp; // "Refresca" el elemento.
        }
        else if (this.browser.isMSIE9() && (name == "style") && (value == ""))
            elem.style.cssText = "";
    }

    function removeAttribute(elem,name)
    {
        if (this.browser.isBlackBerryOld())
        {
          if (name == "style") elem.style.cssText = "";
          else
          {
              elem.removeAttribute(name);
              var style = elem.style;
              var oldDisp = style.display;
              style.display = "none";
              style.display = oldDisp;
          }
        }
        else this.W3CHTMLDocument_super_removeAttribute(elem,name);
    }

}


} // pkg_itsnat_w3c

function itsnat_init_w3c(win)
{
    var itsnat = itsnat_init(win);
    pkg_itsnat_w3c(itsnat);
    return itsnat;
}


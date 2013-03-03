/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_msie_old(pkg)
{
    pkg.DOMPathResolverHTMLDocMSIEOld = DOMPathResolverHTMLDocMSIEOld;
    pkg.MSIEOldDOMEvent = MSIEOldDOMEvent;
    pkg.getMSIEOldEventUtil = getMSIEOldEventUtil;
    pkg.MSIEOldHTMLDocument = MSIEOldHTMLDocument;
    pkg.MSIEOldDOMListenerList = MSIEOldDOMListenerList;
    pkg.MSIEOldEventUtil_SINGLETON = new MSIEOldEventUtil();
    pkg.MSIEOldUIEventUtil_SINGLETON = new MSIEOldUIEventUtil();
    pkg.MSIEOldMouseEventUtil_SINGLETON = new MSIEOldMouseEventUtil();
    pkg.MSIEOldKeyEventUtil_SINGLETON = new MSIEOldKeyEventUtil();

function DOMPathResolverHTMLDocMSIEOld(itsNatDoc)
{
    this.DOMPathResolverHTMLDoc = itsnat.DOMPathResolverHTMLDoc;
    this.DOMPathResolverHTMLDoc(itsNatDoc);

    this.isFiltered = isFiltered;

    function isFiltered(node) { return false; }
}

function MSIEOldDOMEvent(evt,listener)
{
    this.DOMStdEvent = itsnat.DOMStdEvent;
    this.DOMStdEvent(evt,listener);

    this.getTarget = function () { return this.getNativeEvent().srcElement; };
}

function getMSIEOldEventUtil(typeCode)
{
    switch(typeCode)
    {
        case 0: // DOMStdEventTypeInfo.UNKNOWN_EVENT
            return itsnat.MSIEOldEventUtil_SINGLETON;
        case 1: // UI_EVENT
            return itsnat.MSIEOldUIEventUtil_SINGLETON;
        case 2: // MOUSE_EVENT
            return itsnat.MSIEOldMouseEventUtil_SINGLETON;
        case 3: // HTML_EVENT
            return itsnat.MSIEOldEventUtil_SINGLETON;
        case 4: // MUTATION_EVENT
            return null; // Not supported
        case 5: // KEY_EVENT
            return itsnat.MSIEOldKeyEventUtil_SINGLETON;
    }
    return null; // ?
}

function MSIEOldEventUtil()
{
    this.genParamURL = genParamURL;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_srcElement=" + itsNatDoc.getStringPathFromNode(evt.srcElement);
        url += "&itsnat_evt_cancelBubble=" + evt.cancelBubble;
        url += "&itsnat_evt_returnValue=" + evt.returnValue;
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        evtB.type = evtN.type;
        evtB.srcElement = evtN.srcElement;
        evtB.cancelBubble = evtN.cancelBubble;
        evtB.returnValue = evtN.returnValue;
        evtB.keyCode = evtN.keyCode;
    }
}

function MSIEOldUIEventUtil()
{
    this.MSIEOldEventUtil = MSIEOldEventUtil;
    this.MSIEOldEventUtil();
}

function MSIEOldMouseEventUtil()
{
    this.MSIEOldUIEventUtil = MSIEOldUIEventUtil;
    this.MSIEOldUIEventUtil();
    this.MSIEOldUIEventUtil_genParamURL = this.genParamURL;
    this.MSIEOldUIEventUtil_backupEvent = this.backupEvent;

    this.MouseEventUtil = itsnat.MouseEventUtil;
    this.MouseEventUtil();

    this.genParamURL = genParamURL;
    this.backupEvent = backupEvent;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.MSIEOldUIEventUtil_genParamURL(evt,itsNatDoc);
        url += this.genMouseEventURL(evt,itsNatDoc);
        url += "&itsnat_evt_fromElement=" + itsNatDoc.getStringPathFromNode(evt.fromElement);
        url += "&itsnat_evt_toElement=" + itsNatDoc.getStringPathFromNode(evt.toElement);
        return url;
    }

    function backupEvent(evtN,evtB)
    {
        this.MSIEOldUIEventUtil_backupEvent(evtN,evtB);
        this.backupMouseEvent(evtN,evtB);

        evtB.fromElement = evtN.fromElement;
        evtB.toElement = evtN.toElement;
    }
}

function MSIEOldKeyEventUtil()
{
    this.MSIEOldUIEventUtil = MSIEOldUIEventUtil;
    this.MSIEOldUIEventUtil();

    this.MSIEOldUIEventUtil_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;

    function genParamURL(evt,itsNatDoc)
    {
        var url = this.MSIEOldUIEventUtil_genParamURL(evt,itsNatDoc);
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_keyCode=" + evt.keyCode;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        return url;
    }
}

function MSIEOldDOMListenerList(type,currentTarget,itsNatDoc)
{
    this.List = itsnat.List;
    this.List();

    this.dispatchEvent = dispatchEvent;
    this.dispatchEventLocal = dispatchEventLocal;

    // Attrs
    this.type = type;
    this.currentTarget = currentTarget;
    this.itsNatDoc = itsNatDoc;

    function dispatchEventLocal(evt,capture)
    {
        // El handler inline ya lo ejecuta el navegador
        if (this.isEmpty()) return;
        var listeners = this.getArrayCopy(); // Asi permitimos cambios concurrentes
        for(var i = 0; i < listeners.length; i++) // Del primero al ultimo igual que en W3C
        {
            var listenerWrapper = listeners[i];
            if (capture != listenerWrapper.isUseCapture()) continue;
            listenerWrapper.dispatchEvent(evt);
        }
    }

    function dispatchEvent(evt)
    {
        // returnValue es la unica propiedad que se mantiene entre llamadas a listeners
        // Por defecto returnValue es undefined, cualquier otro valor suponemos "ya capturado"
        // El valor true es el valor por defecto deseado aunque hace que beforeunload (caso window) nos "pregunte" si queremos salir,
        // por eso no capturamos con target window y porque no tiene sentido
        if (evt.itsnat_stopPropagation) return;
        if (evt.srcElement && (typeof evt.returnValue == "undefined")) // Si srcElement no definido el target original es window
        {
            evt.returnValue = true;
            this.itsNatDoc.dispatchEventCapture(evt);
        }

        if (evt.itsnat_stopPropagation) return;
        this.dispatchEventLocal(evt,false);
        // No retornar "return evt.returnValue" pues se superpone al return del posible handler inline (pues se ejecuta antes)
        // pues este metodo fue registrado via attachEvent y el returnValue cuenta pero con menor prioridad que si lo retornamos (return) a modo de inline handler
        // y queremos que mande el inline por ejemplo para hacer onclick="return false" en los falsos links AJAX "Google friendly"
        // Yo creo que esta mal: http://msdn.microsoft.com/en-us/library/ms534372(VS.85).aspx
    }
}

function MSIEOldHTMLDocument()
{
    this.HTMLDocument = itsnat.HTMLDocument;
    this.HTMLDocument();

    this.createDOMPathResolver = createDOMPathResolver;
    this.MSIEOldHTMLDocument_getAttribute = this.getAttribute;
    this.getAttribute = getAttribute;
    this.MSIEOldHTMLDocument_setAttribute = this.setAttribute;
    this.setAttribute = setAttribute;
    this.setCSSStyle = setCSSStyle;
    this.importNode = importNode;
    this.setInnerXML = setInnerXML;
    this.attachEvent = attachEvent;
    this.detachEvent = detachEvent;
    this.createMSIEOldDOMListenerList = createMSIEOldDOMListenerList;
    this.createEventListenerRegistry = createEventListenerRegistry;
    this.getEventListenerRegistry = getEventListenerRegistry;
    this.cleanEventListenerRegistry = cleanEventListenerRegistry;
    this.addDOMEventListener2 = addDOMEventListener2;
    this.removeDOMEventListener2 = removeDOMEventListener2;
    this.addAttachUnloadListener2 = addAttachUnloadListener2;
    this.dispatchEvent = dispatchEvent;
    this.dispatchEventCapture = dispatchEventCapture;
    this.dispatchEventTree = dispatchEventTree;

    function createDOMPathResolver() { return new DOMPathResolverHTMLDocMSIEOld(this); }

    function getAttribute(elem,name)
    {
        if ((name == "style")&& elem.style) return elem.style.cssText; // No style si es SVG por ejemplo
        else return this.MSIEOldHTMLDocument_getAttribute(elem,name);
    }

    function setAttribute(elem,name,value)
    {
        if ((name == "style")&& elem.style) elem.style.cssText = value;
        else this.MSIEOldHTMLDocument_setAttribute(elem,name,value);
    }

    function setCSSStyle(idObj,value)
    {
        var elem = this.getNode(idObj);
        elem.style.cssText = value;
    }

    function importNode(node,deep)
    {
        switch(node.nodeType)
        {
          case 1: // Node.ELEMENT_NODE
            var newNode = this.doc.createElement(node.tagName); // Si tiene prefijo/namespace ItsNat lo ha declarado previamente
            for (var i = 0; i < node.attributes.length; i++)
            {
                var attr = node.attributes[i];
                if (!attr.specified) continue;
                this.setAttribute(newNode,attr.name,this.getAttribute(node,attr.name));
            }
            if (deep)
              for (var i = 0; i < node.childNodes.length; i++)
              {
                var child = this.importNode(node.childNodes[i],true);
                if (child != null) newNode.appendChild(child);
              }
            return newNode;
          case 3: // Node.TEXT_NODE:
          case 4: // Node.CDATA_SECTION_NODE
            return this.doc.createTextNode(node.data);
          case 8: // Node.COMMENT_NODE:
            return this.doc.createComment(node.data);
          default:
            return null;
        }
    }

    function setInnerXML(elem,value)
    {
        var xmlDoc = new ActiveXObject("Msxml2.DOMDocument.3.0");
        xmlDoc.async = false;
        xmlDoc.loadXML(value);

        var svgweb = this.isSVGWebNode(elem);
        var rootElem = xmlDoc.documentElement;
        var child = rootElem.firstChild;
        while (child != null)
        {
            var newChild;
            if (svgweb) newChild = this.importNodeSVGWeb(child,true);
            else newChild = this.importNode(child,true);
            if (newChild != null) elem.appendChild(newChild);
            child = child.nextSibling;
        }
    }

    function attachEvent(node,type,func) { node.attachEvent("on" + type,func); }

    function detachEvent(node,type,func) { node.detachEvent("on" + type,func); }

    function createMSIEOldDOMListenerList(node,type) { return new MSIEOldDOMListenerList(type,node,this); }

    function createEventListenerRegistry(node,type)
    {
        if (!node.itsNatListeners) node.itsNatListeners = new Object();
        var func = function (evt) { return arguments.callee.listeners.dispatchEvent(evt); };
        func.listeners = this.createMSIEOldDOMListenerList(node,type);
        node.itsNatListeners[type] = func;
        return func;
    }

    function getEventListenerRegistry(node,type,create)
    {
        var func;
        if (node.itsNatListeners)
        {
            func = node.itsNatListeners[type];
            if (func) return func;
        }
        if (!create) return null;

        func = this.createEventListenerRegistry(node,type);
        this.attachEvent(node,type,func);
        return func;
    }

    function cleanEventListenerRegistry(type,node,func)
    {
        delete node.itsNatListeners[type];
        this.detachEvent(node,type,func);
    }

    function addDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.add(listenerWrapper);
    }

    function removeDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.remove(listenerWrapper);
        if (func.listeners.isEmpty()) this.cleanEventListenerRegistry(type,node,func);
    }

    function addAttachUnloadListener2(node,type,listener)
    {
        // Aseguramos asi que se ejecuta el ultimo (si se registro el ultimo) y con setMustBeSent evitamos que se envie como un evento normal
        var listener2 = function (event) { listener(); event.setMustBeSent(false); };
        this.addDOMEventListener([null,null,"window"],type,"rem_ctrl_unload",listener2,false,3,-1,3);
    }

    function dispatchEvent(node,type,evt) { return node.fireEvent("on" + type,evt); }

    function dispatchEventCapture(evt)
    {
        // Se espera que se despache primero el listener del elemento mas bajo
        var target = evt.srcElement;

        var parentList = new itsnat.List();
        var parentNode = this.getParentNode(target);
        while (parentNode != null)
        {
            parentList.add(parentNode);
            parentNode = this.getParentNode(parentNode);
        }
        this.dispatchEventTree(true,evt,parentList);
        return parentList;
    }

    function dispatchEventTree(capture,evt,parentList)
    {
        var size = parentList.size();
        var first = capture ? size-1 : 0;
        var limit = capture ? -1 : size;
        var step =  capture ? -1 : 1;
        for (var i = first; i != limit; i=i + step)
        {
            var current = parentList.get(i);
            var func = this.getEventListenerRegistry(current,evt.type,false);
            if (!func) continue;
            func.listeners.dispatchEventLocal(evt,capture);
            if (evt.itsnat_stopPropagation) break;
        }
    }

}

} // pkg_itsnat_msie_old

function itsnat_init_msie_old(win)
{
    var itsnat = itsnat_init(win);
    pkg_itsnat_msie_old(itsnat);
    return itsnat;
}

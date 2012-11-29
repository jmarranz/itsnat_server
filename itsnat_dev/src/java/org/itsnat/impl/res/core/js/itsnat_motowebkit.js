/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_motowebkit(pkg)
{
    pkg.MotoWebKitHTMLDocument = MotoWebKitHTMLDocument;

function MotoWebKitListenerList(type,currentTarget,itsNatDoc)
{
    this.List = itsnat.List;
    this.List();

    this.dispatchEventLocal = dispatchEventLocal;
    this.dispatchEvent = dispatchEvent;
    this.setInlineHandler = setInlineHandler;

    // Attrs
    this.type = type;
    this.currentTarget = currentTarget;
    this.itsNatDoc = itsNatDoc;
    this.inlineHandler = null;

    // Construction
    if (currentTarget.getAttribute) // evitar document y window
    {
        var inlineHandler = itsNatDoc.getAttribute(currentTarget,"on" + type);
        this.setInlineHandler(inlineHandler);
    }

    function dispatchEventLocal(evt,capture)
    {
        if (!capture && this.inlineHandler != null)
        {
            var res = this.inlineHandler.call(this.currentTarget);
            if ((typeof res == "boolean") && !res) evt.preventDefault();
        }
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
        // El bubbling lo hace ya el navegador bien
        if (evt.itsnat_stopPropagation) return;
        if ((this.currentTarget != this.itsNatDoc.win) && !evt.itsNatCapturing) // para evitar el capturing en fase bubbling
        {
            evt.itsNatCapturing = true;
            this.itsNatDoc.dispatchEventCapture(evt);
        }
        if (evt.itsnat_stopPropagation) return;
        this.dispatchEventLocal(evt,false);
    }

    function setInlineHandler(code)
    {
        if (code != null) this.inlineHandler = new Function(code); // code es string
        else this.inlineHandler = null;
    }
}

function MotoWebKitHTMLDocument()
{
    this.W3CHTMLDocument = itsnat.W3CHTMLDocument;
    this.W3CHTMLDocument();

    this.createEventListenerRegistry = createEventListenerRegistry;
    this.getEventListenerRegistry = getEventListenerRegistry;
    this.cleanEventListenerRegistry = cleanEventListenerRegistry;
    this.addDOMEventListener2 = addDOMEventListener2; // redef
    this.removeDOMEventListener2 = removeDOMEventListener2; // redef
    this.dispatchEvent = dispatchEvent;
    this.dispatchEventCapture = dispatchEventCapture;
    this.dispatchEventBubble = dispatchEventBubble;
    this.dispatchEventTree = dispatchEventTree;
    this.setAttribute = setAttribute; // redef
    this.removeAttribute = removeAttribute; // redef
    this.procAttrAsHandler = procAttrAsHandler;

    this.evtNotBubble = {"load":true,"unload":true,"focus":true,"blur":true,"DOMNodeInsertedIntoDocument":true,"DOMNodeRemovedFromDocument":true,"beforeunload":true,"DOMContentLoaded":true };

    function createEventListenerRegistry(node,type)
    {
        if (!node.itsNatListeners) node.itsNatListeners = new Object();
        var func = function (evt) { return arguments.callee.listeners.dispatchEvent(evt); };
        func.listeners = new MotoWebKitListenerList(type,node,this);
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

        if (!create)
        {
            var inlineHandler = this.getAttribute(node,"on" + type);
            if ((inlineHandler == null)||(inlineHandler == "")) return null; // No hay handler
            // Hay un atributo handler onXXX, estamos obligados a crear el registro, da igual "create", pues hay que ejecutarlo en el dispatch manual
        }

        func = this.createEventListenerRegistry(node,type);
        node["on" + type] = func; // no cambia el atributo (al reves si)
        return func;
    }

    // No hacer nada, hay handler inline por medio que cuesta trabajo iniciar
    function cleanEventListenerRegistry(type,node,func) { }

    function addDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        // Algunos elementos (de forms) y window soportan addEventListener pero otros no (ej. document,A,BUTTON), en ellos tenemos que simular tambien el capture manualmente
        // lo mejor es suponer que el addEventListener no funciona en ninguno.
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.add(listenerWrapper);
    }

    function removeDOMEventListener2(listenerWrapper,node,type,useCapture)
    {
        var func = this.getEventListenerRegistry(node,type,true);
        func.listeners.remove(listenerWrapper);
        if (func.listeners.isEmpty()) this.cleanEventListenerRegistry(type,node,func);
    }

    function dispatchEvent(node,type,evt)
    {
        // node.dispatchEvent no funciona para todos los tipos de eventos (ej click) pero en algunos si (ej change)
        // Necesitamos definir el "target" por ello hacemos el truco:
        evt.initEvent("fake",evt.bubbles,evt.cancelable);
        node.dispatchEvent(evt); // No hace nada y define la prop. "target" (valdria un tipo bueno y un stopPropagation() antes)
        evt.initEvent(type,evt.bubbles,evt.cancelable);

        if (evt.itsnat_stopPropagation) return evt.returnValue;
        var parentList = null;
        if (node != this.win)
            parentList = this.dispatchEventCapture(evt);

        if (evt.itsnat_stopPropagation) return evt.returnValue;
        var func = this.getEventListenerRegistry(node,type,false);
        if (func) func.listeners.dispatchEventLocal(evt,false);

        if (evt.itsnat_stopPropagation) return evt.returnValue;
        if (node != this.win)
            this.dispatchEventBubble(evt,parentList);

        return evt.returnValue;
    }

    function dispatchEventCapture(evt)
    {
        // Se espera que se despache primero el listener del elemento mas bajo
        var target = evt.target;

        var parentList = new itsnat.List();
        var parentNode = this.getParentNode(target);
        while (parentNode != null)
        {
            parentList.add(parentNode);
            parentNode = this.getParentNode(parentNode);
        }

        this.dispatchEventTree(true,evt,parentList);
        return parentList; // util para el bubbling
    }

    function dispatchEventBubble(evt,parentList)
    {
        if (this.evtNotBubble[evt.type]) return;
        this.dispatchEventTree(false,evt,parentList);
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

    function setAttribute(elem,name,value)
    {
        elem.setAttribute(name,value);
        this.procAttrAsHandler(elem,name,value,false);
    }

    function removeAttribute(elem,name)
    {
        elem.removeAttribute(name);
        this.procAttrAsHandler(elem,name,"",false);
    }

    function procAttrAsHandler(elem,name,value,remove)
    {
        if (name.indexOf("on") != 0) return; // onXXX
        var type = name.substring(2);
        var func = this.getEventListenerRegistry(elem,type,false);
        if (!func) return;
        elem["on" + type] = func; // Restauramos, esto no afecta al atributo
        if (remove) value = null;
        func.listeners.setInlineHandler(value);  // Aunque no este soportado por IE valdria para server-sent events
    }
}

} // pkg_itsnat_motowebkit

function itsnat_init_motowebkit(win)
{
    var itsnat = itsnat_init_w3c(win);
    pkg_itsnat_motowebkit(itsnat);
    return itsnat;
}

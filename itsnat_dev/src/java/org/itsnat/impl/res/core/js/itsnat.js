/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat(pkg)
{
    pkg.AJAX = AJAX;
    pkg.List = List;
    pkg.Map = Map;
    pkg.MouseEventUtil = MouseEventUtil;
    pkg.DOMPathResolver = DOMPathResolver;
    pkg.EventMgr = EventMgr;
    pkg.Event = Event;
    pkg.NormalEvent = NormalEvent;
    pkg.TransportUtil = new TransportUtil(); // SINGLETON
    pkg.DOMEvent = DOMEvent;
    pkg.DOMStdEvent = DOMStdEvent;
    pkg.UserEvent = UserEvent;
    pkg.AttachTimerRefreshEvent = AttachTimerRefreshEvent;
    pkg.AttachCometTaskRefreshEvent = AttachCometTaskRefreshEvent;
    pkg.AttachUnloadEvent = AttachUnloadEvent;
    pkg.DOMStdEventListener = DOMStdEventListener;
    pkg.TimerEventListener = TimerEventListener;
    pkg.UserEventListener = UserEventListener;
    pkg.ContinueEventListener = ContinueEventListener;
    pkg.AsyncTaskEventListener = AsyncTaskEventListener;
    pkg.CometTaskEventListener = CometTaskEventListener;
    pkg.Document = Document;
    pkg.DOMPathResolverHTMLDoc = DOMPathResolverHTMLDoc;
    pkg.HTMLDocument = HTMLDocument;

function AJAX(itsNatDoc,win)
{
    // Crear un objeto por cada llamada, no reutilizar. http://developer.mozilla.org/en/docs/AJAX:Getting_Started
    this.request = request;
    this.requestAsyncText = requestAsyncText;
    this.requestSyncText = requestSyncText;
    this.processResult = processResult;
    this.abort = abort;

    this.itsNatDoc = itsNatDoc;
    this.xhr = null;
    this.timerHnd = null;
    this.aborted = false;

    if (typeof win.XMLHttpRequest != "undefined") this.xhr = new win.XMLHttpRequest();
    else if (typeof win.ActiveXObject != "undefined") // MSIE,ASV
    {
        try { this.xhr = new win.ActiveXObject("Msxml2.XMLHTTP"); }
        catch(ex) { this.xhr = new win.ActiveXObject("Microsoft.XMLHTTP"); }
    }

    function request(method,url,async,content)
    {  
        if (method == "GET") { url += "?" + content; content = null; }
        this.xhr.open(method, url, async);
        if (method == "POST") this.xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded"); 
        this.xhr.setRequestHeader("If-Modified-Since","Wed, 15 Nov 1995 00:00:00 GMT"); // Para WebKit viejos http://lists.apple.com/archives/dashboard-dev/2005/May/msg00196.html
        this.xhr.setRequestHeader("Cache-Control","no-cache");      
        this.xhr.send(content);
    }

    function requestAsyncText(method,url,content,listener,timeout)
    {
        var func = function() { var thisFunc = arguments.callee; thisFunc.thisObj.processResult(thisFunc.listener,true); };
        func.thisObj = this;
        func.listener = listener;
        this.xhr.onreadystatechange = func;

        this.request(method,url,true,content);

        if (timeout >= 0) // en modo sincrono no hay abort
        {
            var func2 = function() { var thisFunc = arguments.callee; thisFunc.thisObj.abort(thisFunc.listener); };
            func2.thisObj = this;
            func2.listener = listener;
            this.timerHnd = this.itsNatDoc.setTimeout(func2,timeout);
        }
    }

    function requestSyncText(method,url,content)
    {
        this.request(method,url,false,content);
        return this.xhr.responseText;
    }

    function processResult(listener,async)
    {
        if (!this.itsNatDoc) return; // En proceso de destruccion evitamos procesar respuestas asincronas para evitar errores (FireFox)

        if ((this.xhr.readyState == 4) && (this.timerHnd != null)) // 4 = "complete"
            this.itsNatDoc.clearTimeout(this.timerHnd); // lo primero por si el proceso normal tarda mucho (o lanza un alert)

        if (this.aborted) return; // El XHR abort() de MSIE llama al listener de todas formas con readyState a 4 y status != 200, el proceso del abort no debe ir por aqui

        if (this.xhr.readyState != 4) return; // 4 = "complete"
        var status; try { status = this.xhr.status; } catch(e) { } // Caso de fallo de red o unload en FireFox (esta destruyendose) https://bugzilla.mozilla.org/show_bug.cgi?id=238559#c0
        // http://www.monsur.com/httpstatuscodes/test.html
        // El status 0 o undefined es o bien un error no reconocido por el navegador o un falso error (WebKits antiguos 4xx), lo "filtramos" sin mostrar error

        listener.processRespBegin();

        if (typeof status == "number") // Es undefined ocasionalmente en S60WebKit por ejemplo al enviar el unload 
        {
            if (status == 200) listener.processRespValid(this.xhr.responseText); // "OK"
            else if (status != 0)
            {
                // Normalmente: status == 500 => Error interno del servidor, el servidor ha lanzado una excepcion
                // "responseText" contiene el texto de la excepcion del servidor (en Opera esta vacio), xhr.statusText nos da apenas la frase "Error Interno del Servidor"
                var errMsg = "status: " + status + "\n" + this.xhr.statusText + "\n\n" + this.xhr.responseText;
                listener.processRespError(errMsg);
            }
        }

        listener.processRespEnd(async);
    }

    function abort(listener)
    {
        if ((this.xhr.readyState >= 1) || (this.xhr.readyState <= 3)) // http://msdn2.microsoft.com/en-us/library/ms534361.aspx
        {
            this.aborted = true;
            this.xhr.abort();
            listener.processRespTimeout();
        }
    }
}

function List() // mimics java.util.List and LinkedList
{
    this.add = add;
    this.remove = remove;
    this.removeByIndex = removeByIndex;
    this.removeFirst = removeFirst;
    this.addLast = addLast;
    this.get = get;
    this.isEmpty = isEmpty;
    this.size = size;
    this.getArray = getArray;
    this.getArrayCopy = getArrayCopy;

    // attributes
    this.array = new Array();

    function add(obj) { this.array[this.array.length] = obj; }  // length aumenta automaticamente
    function get(index) { return this.array[index]; }
    function isEmpty() { return (this.array.length == 0); }
    function size() { return this.array.length; }
    function removeFirst() { return this.array.shift(); }
    function addLast(obj) { return this.array.push(obj); }
    function removeByIndex(index) { this.array.splice(index,1); }
    function getArray() { return this.array; }
    function getArrayCopy() { return this.array.slice(0); }

    function remove(obj)
    {
        var len = this.array.length;
        for(var i = 0; i < len; i++)
        {
            if (this.array[i] != obj) continue;
            this.removeByIndex(i);
            return true;
        }
        return false;
    }
}

function Map(useDelete) // mimics java.util.Map
{
    this.get = get;
    this.put = put;
    this.remove = remove;
    this.doForAll = doForAll;

    // Attribs
    this.useDelete = useDelete;
    this.map = new Object();

    function get(key)
    {
        var value = this.map[key];
        if (!value) return null; // Para evitar devolver "undefined"
        return value;
    }

    function put(key,value) { this.map[key] = value; }

    function remove(key)
    {
        var value = this.get(key);
        if (this.useDelete) delete this.map[key];
        else this.map[key] = undefined; // Inevitable memory leak
        return value;
    }

    function doForAll(func)
    {
        for(var key in this.map)
        {
            var value = this.map[key];
            if (!this.useDelete && (typeof value == "undefined")) continue;
            func(key,value);
        }
    }
}

function Browser(type,subType)
{
    this.isMSIEOld = isMSIEOld;
    this.isW3C = isW3C;
    this.isGecko = isGecko;
    this.isWebKit = isWebKit;
    this.isOpera = isOpera;
    this.isOperaMini = isOperaMini;
    this.isBlackBerryOld = isBlackBerryOld;
    this.isBlackBerryOld5 = isBlackBerryOld5;
    this.isAdobeSVG = isAdobeSVG;
    this.isBatik = isBatik;
    this.isMSIE9 = isMSIE9;

    /* UNKNOWN=0,MSIE_OLD=1,GECKO=2,WEBKIT=3,OPERA=4,BLACKBERRY_OLD=5,ADOBE_SVG=6,BATIK=7,MSIE_9=8 */
    this.type = type;
    this.subType = subType;

    function isMSIEOld() { return this.type == 1; }
    function isW3C() { return !this.isMSIEOld(); }
    function isGecko() { return this.type == 2; }
    function isWebKit() { return this.type == 3; }
    function isOpera() { return this.type == 4; }
    function isOperaMini() { return this.isOpera() && (this.subType == 2); }
    function isBlackBerryOld() { return this.type == 5; }
    function isBlackBerryOld5() { return this.isBlackBerryOld() && (this.subType >= 5); }
    function isAdobeSVG() { return this.type == 6; }
    function isBatik() { return this.type == 7; }
    function isMSIE9() { return this.type == 8; }
}

function DOMPathResolver(itsNatDoc)
{
    this.isFiltered = null; // implementar
    this.getNodeDeep = getNodeDeep;
    this.getNodePath = getNodePath;
    this.getSuffix = getSuffix;
    this.getStringPathFromNode = getStringPathFromNode;
    this.getNodeFromArrayPath = getNodeFromArrayPath;
    this.getNodeFromPath = getNodeFromPath;
    this.getStringPathFromArray = getStringPathFromArray;
    this.getArrayPathFromString = getArrayPathFromString;
    this.getChildNodeFromPos = getChildNodeFromPos;
    this.getChildNodeFromStrPos = getChildNodeFromStrPos;
    this.getNodeChildPosition = getNodeChildPosition;

    this.itsNatDoc = itsNatDoc;

    function getArrayPathFromString(pathStr)
    {
        if (pathStr == null) return null;
        return pathStr.split(",");
    }

    function getChildNodeFromPos(parentNode,pos,isTextNode)
    {
        var currPos = 0;
        var currNode = parentNode.firstChild;
        while(currNode != null)
        {
            var currNodeValid = this.itsNatDoc.getValidNode(currNode); // puede que cambie
            if (!this.isFiltered(currNodeValid))
            {
                var type = currNodeValid.nodeType;
                if (currPos == pos)
                {
                    if (isTextNode || (type != 3)) return currNodeValid;
                }
                else if (type != 3) currPos++;
            }
            currNode = currNode.nextSibling;
        }
        return null;
    }

    function getChildNodeFromStrPos(parentNode,posStr)
    {
        if (posStr == "de") return this.itsNatDoc.doc.documentElement;

        var posBracket = posStr.indexOf('[');
        if (posBracket == -1)
        {
            var pos = parseInt(posStr);
            return this.getChildNodeFromPos(parentNode,pos,false);
        }
        else
        {
            var pos = parseInt(posStr.substring(0,posBracket));
            if (posStr.charAt(posBracket + 1) == '@') // Atributo
            {
                var attrName = posStr.substring(posBracket + 2,posStr.length - 1);
                var child = this.getChildNodeFromPos(parentNode,pos,false);
                return child.getAttributeNode(attrName);
            }
            else return this.getChildNodeFromPos(parentNode,pos,true);
        }
    }

    function getNodeFromArrayPath(arrayPath,topParent)
    {
        var doc = this.itsNatDoc.doc;
        if (arrayPath.length == 1)
        {
            var firstPos = arrayPath[0];
            if (firstPos == "window") return this.itsNatDoc.win;
            else if (firstPos == "document") return doc;
            else if (firstPos == "doctype") return doc.doctype;
        }

        if (topParent == null) topParent = doc;
        var node = topParent;

        var len = arrayPath.length;
        for(var i = 0; i < len; i++)
        {
            var posStr = arrayPath[i];
            node = this.getChildNodeFromStrPos(node,posStr);
        }

        return node;
    }

    function getNodeFromPath(pathStr,topParent)
    {
        var path = this.getArrayPathFromString(pathStr);
        if (path == null) return null;
        return this.getNodeFromArrayPath(path,topParent);
    }

    function getNodeChildPosition(node)
    {
        if (node == this.itsNatDoc.doc.documentElement) return "de";
        var parentNode = this.itsNatDoc.getParentNode(node);
        if (parentNode == null) throw "Unexpected error";

        var pos = 0;
        var currNode = parentNode.firstChild;
        while(currNode != null)
        {
            var currNodeValid = this.itsNatDoc.getValidNode(currNode);
            if (currNodeValid == node) return pos;
            if (currNodeValid.nodeType != 3) pos++; // 3 = Node.TEXT_NODE

            currNode = currNode.nextSibling;
        }
        return "-1";
    }

    function getStringPathFromArray(path)
    {
        var code = "";
        var len = path.length;
        for(var i = 0; i < len; i++)
        {
            if (i != 0) code += ",";
            code += path[i];
        }
        return code;
    }

    function getNodeDeep(node,topParent)
    {
        var i = 0;
        while(node != topParent)
        {
            i++;
            node = this.itsNatDoc.getParentNode(node);
            if (node == null) return -1; // el nodo no esta bajo topParent
        }
        return i;
    }

    function getNodePath(nodeLeaf,topParent)
    {
        if (nodeLeaf == null) return null;
        if (topParent == null) topParent = this.itsNatDoc.doc;

        if (nodeLeaf == this.itsNatDoc.win) return ["window"];
        var nodeType = nodeLeaf.nodeType;
        if (nodeType == 9) return ["document"]; // Node.DOCUMENT_NODE
        else if (nodeType == 10) return ["doctype"]; // Node.DOCUMENT_TYPE_NODE

        var node = nodeLeaf;
        if (nodeType == 2) node = node.ownerElement; // Node.ATTRIBUTE_NODE
        var len = this.getNodeDeep(node,topParent);
        if (len < 0) return null;
        var path = new Array(len);
        for(var i = len - 1; i >= 0; i--)
        {
            var pos = this.getNodeChildPosition(node);
            path[i] = pos;
            node = this.itsNatDoc.getParentNode(node);
        }
        path[len - 1] += this.getSuffix(nodeLeaf);
        return path;
    }

    function getSuffix(nodeLeaf)
    {
        var type = nodeLeaf.nodeType;
        if (type == 3) return "[t]"; // Node.TEXT_NODE
        else if (type == 2) return "[@" + nodeLeaf.name + "]"; // Node.ATTRIBUTE_NODE
        else return "";
    }

    function getStringPathFromNode(node,topParent)
    {
        if (node == null) return null;
        var path = this.getNodePath(node,topParent);
        if (path == null) return null;
        return this.getStringPathFromArray(path);
    }
}


function TransportUtil()
{
    this.transpAllAttrs = transpAllAttrs;
    this.transpAttr = transpAttr;
    this.transpNodeInner = transpNodeInner;
    this.transpNodeComplete = transpNodeComplete;

    function transpAllAttrs(evt)
    {
        var itsNatDoc = evt.itsNatDoc;
        var msieOld = itsNatDoc.browser.isMSIEOld();
        var target = evt.getCurrentTarget();
        var attribs = target.attributes;
        var num = 0,len = attribs.length;
        for(var i = 0; i < len; i++)
        {
            var attr = attribs[i];
            if (msieOld && !attr.specified) continue; // Pues la col. attribs contiene todos los posibles

            evt.setExtraParam("itsnat_attr_" + num,attr.name);
            evt.setExtraParam(attr.name,itsNatDoc.getAttribute(target,attr.name)); // En MSIE attr.value es erroneo el attr "style"
            num++;
        }
        evt.setExtraParam("itsnat_attr_num",num);
    }

    function transpAttr(evt,name)
    {
        var value = evt.itsNatDoc.getAttribute(evt.getCurrentTarget(),name);
        if (value != null) evt.setExtraParam(name,value);
    }

    function transpNodeInner(evt,name)
    {
        var value = evt.getCurrentTarget().innerHTML;
        if (value != null) evt.setExtraParam(name,value);
    }

    function transpNodeComplete(evt,nameInner)
    {
        this.transpAllAttrs(evt);
        this.transpNodeInner(evt,nameInner);
    }
}

function EventMgr(itsNatDoc)
{
    this.returnedEvent = returnedEvent;
    this.processEvents = processEvents;
    this.sendEvent = sendEvent;
    this.sendEventEffective = sendEventEffective;
    if (itsNatDoc.browser.isMSIEOld()) this.getEventUtil = itsnat.getMSIEOldEventUtil;
    else this.getEventUtil = itsnat.getW3CEventUtil;

    this.itsNatDoc = itsNatDoc;
    this.queue = new List();
    this.holdEvt = null;

    function returnedEvent(evt)
    {
        if (this.holdEvt == evt) this.processEvents(false);
    }

    function processEvents(notHold)
    {
        this.holdEvt = null;
        while(!this.queue.isEmpty())
        {
            var evt = this.queue.removeFirst();
            this.sendEventEffective(evt);
            if (notHold) continue;
            if (this.holdEvt != null) break; // El evento enviado ordena bloquear
        }
        if (notHold) this.holdEvt = null;
    }

    function sendEvent(evt)
    {
        if (this.itsNatDoc.disabledEvents) return;
        if (evt.ignoreHold)
        {
            this.processEvents(true); // liberamos la cola, recordar que es monohilo
            this.sendEventEffective(evt);
        }
        else if (this.holdEvt != null)
        {
            if (evt.saveEvent != null) evt.saveEvent();
            this.queue.addLast(evt);
        }
        else this.sendEventEffective(evt);
    }

    function sendEventEffective(evt)
    {
        if (this.itsNatDoc.disabledEvents) return; // pudo ser definido desde el servidor en el anterior evento

        var globalListeners = this.itsNatDoc.globalEventListeners;
        if (!globalListeners.isEmpty())
        {
            var array = globalListeners.getArrayCopy(); // asi permitimos que se añadan mientras se procesan
            var len = array.length;
            for(var i = 0; i < len; i++)
            {
                var listener = array[i];
                var res = listener(evt);
                if (!res && (typeof res == "boolean")) return; // no enviar
            }
        }

        this.itsNatDoc.fireEventMonitors(true,false,evt);
        var win = this.itsNatDoc.win;
        if (this.itsNatDoc.browser.isAdobeSVG()) win = window; // En ASV itsNatDoc.win es _window_impl que no tiene el ActiveXObject
        var method = this.itsNatDoc.usePost ? "POST" : "GET";
        var servletPath = this.itsNatDoc.getServletPath();
        var commMode = evt.getListener().getCommMode();
        var paramURL = evt.genParamURL();

        if ((commMode == 1) && !this.itsNatDoc.xhrSyncSup) // XHR SYNC
        {
            // Simulamos "el bloqueo" en lo posible, no se da el caso de navegadores con SVG/XUL (solo HTML). 
            var body = this.itsNatDoc.getHTMLBody();
            var layer = this.itsNatDoc.doc.createElement("div");
            layer.setAttribute("style","position:absolute; z-index:999999; left:0; top:0; width:" + body.scrollWidth + "px; height:" + body.scrollHeight + "px; ");
            body.appendChild(layer);
            this.itsNatDoc.syncLayerTmp = layer;
            commMode = 3; // XHR_ASYNC_HOLD
        }

        if (commMode == 1) // XHR_SYNC
        {
            var ajax = new itsnat.AJAX(this.itsNatDoc,win);
            ajax.requestSyncText(method,servletPath,paramURL);
            ajax.processResult(evt,false);
        }
        else
        {
            if ((commMode == 3)||(commMode == 5)) this.holdEvt = evt; // XHR_ASYNC_HOLD y SCRIPT_HOLD

            var timeout = evt.getListener().getTimeout();
            if ((commMode == 2)||(commMode == 3)) // XHR_ASYNC y XHR_ASYNC_HOLD
            {
                var ajax = new itsnat.AJAX(this.itsNatDoc,win);
                ajax.requestAsyncText(method,servletPath,paramURL,evt,timeout);
            }
            else // SCRIPT (4) y SCRIPT_HOLD (5)
            {
                this.itsNatDoc.sendEventByScript(servletPath,paramURL,evt,timeout);
            }
        }
    }
}

function Event(listener)
{
    this.getListener = getListener;
    this.setMustBeSent = setMustBeSent;
    this.sendEvent = sendEvent;
    this.genParamURL = genParamURL;
    this.processRespBegin = processRespBegin;
    this.processRespEnd = processRespEnd;
    this.processRespTimeout = processRespTimeout;
    this.processRespError = processRespError;
    this.processRespValid = processRespValid;
    this.getCurrentTarget = null; // implementar
    this.saveEvent = null; // se implementa si es necesario

    // attribs
    this.listener = listener;
    this.itsNatDoc = listener.itsNatDoc;
    this.ignoreHold = false;
    this.mustBeSent = true;

    function getListener() { return this.listener; }
    function setMustBeSent(value) { this.mustBeSent = value; }
    function sendEvent() { if (this.mustBeSent) this.itsNatDoc.evtMgr.sendEvent(this); }

    function genParamURL()
    {
        var url = "";
        url += this.itsNatDoc.genParamURL();
        url += this.listener.genParamURL(this);
        return url;
    }

    function processRespBegin()
    {
        if (this.itsNatDoc.syncLayerTmp)
            { var layer = this.itsNatDoc.syncLayerTmp; layer.parentNode.removeChild(layer); this.itsNatDoc.syncLayerTmp = null; }
    }

    function processRespEnd(async) { if (async) this.itsNatDoc.evtMgr.returnedEvent(this); }

    function processRespTimeout()
    {
        this.processRespBegin();
        this.itsNatDoc.fireEventMonitors(false,true,this);
        this.processRespEnd(true);
    }

    function processRespError(errMsg)
    {
        this.itsNatDoc.fireEventMonitors(false,false,this);
        var errorMode = this.itsNatDoc.getErrorMode();
        if (errorMode == 0) alert(ERROR); // 0 == ClientErrorMode.NOT_CATCH_ERRORS . Provocamos un error para que los debuggers se paren
        else this.itsNatDoc.showErrorMessage(true,null,errMsg);
    }

    function processRespValid(response)
    {
        this.itsNatDoc.fireEventMonitors(false,false,this);
        var func;
        if (typeof response == "function") func = response; // Casos SCRIPT y SCRIPT_HOLD
        else // string
        {
            if (response.length == 0) return;
            func = new Function("event","itsNatDoc",response);
        }
        var errorMode = this.itsNatDoc.getErrorMode();
        if (errorMode == 0) func.call(this.getCurrentTarget(),this,this.itsNatDoc); // 0 = ClientErrorMode.NOT_CATCH_ERRORS. En MSIE activar el mostrar mensaje de error y con un depurador podremos depurar. Si hay error no continua y en modo ASYNC_HOLD la aplicacion se para (pues no se quita de la cola el evento)
        else
        {
            try { func.call(this.getCurrentTarget(),this,this.itsNatDoc); }
            catch(e) { this.itsNatDoc.showErrorMessage(false,e,response); }
        }
    }
}

function NormalEvent(listener)
{
    this.Event = Event;
    this.Event(listener);

    this.getExtraParam = getExtraParam;
    this.setExtraParam = setExtraParam;
    this.getCurrentTarget = getCurrentTarget;
    this.getUtil = getUtil;

    // attribs
    this.extraParams = null;

    function getCurrentTarget() { return this.listener.getCurrentTarget(); }

    function getExtraParam(name)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        return this.extraParams[name];
    }

    function setExtraParam(name,value)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        this.extraParams[name] = value;
    }

    function getUtil() { return itsnat.TransportUtil; }
}

function DOMEvent(listener)
{
    this.NormalEvent = NormalEvent;
    this.NormalEvent(listener);

    this.timeStamp = new Date().getTime();

    this.NormalEvent_super_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    function genParamURL()
    {
        var url = this.NormalEvent_super_genParamURL();
        url += "&itsnat_evt_timeStamp=" + this.timeStamp; // En vez del problematico Event.timeStamp
        return url;
    }
}

function DOMStdEvent(evt,listener)
{
    this.DOMEvent = DOMEvent;
    this.DOMEvent(listener);

    this.getNativeEvent = getNativeEvent;
    this.saveEvent = saveEvent;
    this.getTarget = null; // En derivadas

    // attribs
    this.evt = evt;
    this.ignoreHold = (evt.type == "unload")||(evt.type == "beforeunload")||(evt.type == "SVGUnload");

    function getNativeEvent() { return this.evt; }

    function saveEvent()
    {
        // Para evitar el problema de acceder en modo ASYNC_HOLD al evento original tras haberse encolado y terminado el proceso del evento por el navegador (da error en MSIE y otros)
        var evtUtil = this.getListener().getEventUtil();
        var evtN = this.evt;
        this.evt = new Object();
        evtUtil.backupEvent(evtN,this.evt);
    }
}

function UserEvent(evt,listener)
{
    this.DOMEvent = DOMEvent;
    this.DOMEvent(listener);

    if ((evt != null) && (evt.extraParams != null)) // evt es un UserEventPublic
        for(var name in evt.extraParams)
            this.setExtraParam(name,evt.extraParams[name]);
}

function UserEventPublic(name)
{
    this.getExtraParam = getExtraParam;
    this.setExtraParam = setExtraParam;
    this.getName = getName;

    this.extraParams = null;
    this.name = name;

    function getExtraParam(name)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        return this.extraParams[name];
    }

    function setExtraParam(name,value)
    {
        if (this.extraParams == null) this.extraParams = new Object();
        this.extraParams[name] = value;
    }

    function getName() { return this.name; }
}

function AttachEvent(commMode,timeout,itsNatDoc)
{
    this.Event = Event;
    this.Event(new GenericEventListener(itsNatDoc,itsNatDoc.attachType,commMode,timeout));

    this.getCurrentTarget = function() { return this.itsNatDoc.doc; } // por poner algo
}

function AttachUnloadEvent(commMode,timeout,itsNatDoc)
{
    this.AttachEvent = AttachEvent;
    this.AttachEvent(commMode,timeout,itsNatDoc);

    this.AttachEvent_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;

    this.ignoreHold = true;

    function genParamURL()
    {
        var url = this.AttachEvent_genParamURL();
        url += "&itsnat_unload=true";
        return url;
    }
}

function AttachTimerRefreshEvent(callback,interval,commMode,timeout,itsNatDoc)
{
    this.AttachEvent = AttachEvent;
    this.AttachEvent(commMode,timeout,itsNatDoc);

    this.AttachEvent_processRespValid = this.processRespValid;
    this.processRespValid = processRespValid;

    // attribs
    this.callback = callback;
    this.interval = interval;

    function processRespValid(response)
    {
        this.AttachEvent_processRespValid(response);

        this.attachTimerHandle = this.itsNatDoc.setTimeout(this.callback,this.interval);
    }
}

function AttachCometTaskRefreshEvent(listenerId,commMode,timeout,itsNatDoc)
{
    this.AttachEvent = AttachEvent;
    this.AttachEvent(commMode,timeout,itsNatDoc);

    this.AttachEvent_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;

    this.listenerId = listenerId;

    function genParamURL()
    {
        var url = this.AttachEvent_genParamURL();
        url += "&itsnat_listener_id=" + this.listenerId;
        return url;
    }
}

function MouseEventUtil()
{
    this.genMouseEventURL = genMouseEventURL;
    this.backupMouseEvent = backupMouseEvent;

    function genMouseEventURL(evt,itsNatDoc)
    {
        var url = "";
        url += "&itsnat_evt_screenX=" + evt.screenX;
        url += "&itsnat_evt_screenY=" + evt.screenY;
        url += "&itsnat_evt_clientX=" + evt.clientX;
        url += "&itsnat_evt_clientY=" + evt.clientY;
        url += "&itsnat_evt_ctrlKey=" + evt.ctrlKey;
        url += "&itsnat_evt_shiftKey=" + evt.shiftKey;
        url += "&itsnat_evt_altKey=" + evt.altKey;
        url += "&itsnat_evt_button=" + evt.button;
        return url;
    }

    function backupMouseEvent(evtN,evtB)
    {
        evtB.screenX = evtN.screenX; evtB.screenY = evtN.screenY;
        evtB.clientX = evtN.clientX; evtB.clientY = evtN.clientY;
        evtB.ctrlKey = evtN.ctrlKey; evtB.shiftKey = evtN.shiftKey;
        evtB.altKey = evtN.altKey; evtB.button = evtN.button;
    }
}

function GenericEventListener(itsNatDoc,eventType,commMode,timeout)
{
    this.genParamURL = genParamURL;

    this.itsNatDoc = itsNatDoc;
    this.eventType = eventType;
    this.commMode = commMode;
    this.timeout = timeout;

    this.getEventType = getEventType;
    this.getCommMode = getCommMode;
    this.getTimeout = getTimeout;

    function getEventType() { return this.eventType; }
    function getCommMode() { return this.commMode; }
    function getTimeout() { return this.timeout; }

    function genParamURL(evt)
    {
        return "&itsnat_action=event&itsnat_eventType=" + this.getEventType();
    }
}

function NormalEventListener(itsNatDoc,eventType,currentTarget,listener,id,commMode,timeout)
{
    this.GenericEventListener = GenericEventListener;
    this.GenericEventListener(itsNatDoc,eventType,commMode,timeout);

    this.getCurrentTarget = getCurrentTarget;
    this.getListener = getListener;
    this.getId = getId;

    this.GenericEventListener_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.createNormalEvent = null; // redefinir
    this.dispatchEvent = dispatchEvent;

    // attribs

    this.currentTarget = currentTarget;
    this.listener = listener;
    this.id = id;

    function getCurrentTarget() { return this.currentTarget; }
    function getListener() { return this.listener; }
    function getId() { return this.id; }

    function genParamURL(evt)
    {
        var url = this.GenericEventListener_genParamURL(evt);
        url += "&itsnat_listener_id=" + this.getId();
        var params = evt.extraParams;
        if (params != null)
            for(var name in params)
                url += "&" + name + "=" + encodeURIComponent(params[name]);
        return url;
    }

    function dispatchEvent(evt)
    {
        var evtWrapper = this.createNormalEvent(evt);
        var listener = this.getListener();
        if (listener != null) listener(evtWrapper);
        evtWrapper.sendEvent();
    }
}

function DOMStdEventListener(itsNatDoc,currentTarget,type,listener,id,useCapture,commMode,timeout,typeCode)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"domstd",currentTarget,listener,id,commMode,timeout);

    this.getType = getType;
    this.isUseCapture = isUseCapture;
    this.getTypeCode = getTypeCode;
    this.getEventUtil = getEventUtil;
    this.NormalEventListener_genParamURL = this.genParamURL;
    this.genParamURL = genParamURL;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.type = type;
    this.useCapture = useCapture;
    this.typeCode = typeCode;
    this.evtUtil = this.itsNatDoc.evtMgr.getEventUtil(typeCode);

    function getType() { return this.type; }
    function isUseCapture() { return this.useCapture; }
    function getTypeCode() { return this.typeCode; }
    function getEventUtil() { return this.evtUtil; }

    function genParamURL(evt)
    {
        var url = this.NormalEventListener_genParamURL(evt);
        url += "&itsnat_evt_type=" + this.type;
        url += this.evtUtil.genParamURL(evt.getNativeEvent(),this.itsNatDoc);
        return url;
    }

    function createNormalEvent(evt)
    {
        if (this.itsNatDoc.browser.isMSIEOld()) return new itsnat.MSIEOldDOMEvent(evt,this);
        else return new itsnat.W3CDOMEvent(evt,this);
    }
}

function UserEventListener(itsNatDoc,currentTarget,name,listener,id,commMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"user",currentTarget,listener,id,commMode,timeout);

    this.getName = getName;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.name = name;

    function getName() { return this.name; }
    function createNormalEvent(evt) { return new UserEvent(evt,this); }
}

function TimerEventListener(itsNatDoc,currentTarget,listener,id,commMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"timer",currentTarget,listener,id,commMode,timeout);

    this.getHandle = getHandle;
    this.setHandle = setHandle;
    this.createNormalEvent = createNormalEvent;

    // attribs
    this.handle = 0;

    function getHandle() { return this.handle; }
    function setHandle(handle) { this.handle = handle; }
    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function ContinueEventListener(itsNatDoc,currentTarget,listener,id,commMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"continue",currentTarget,listener,id,commMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function AsyncTaskEventListener(itsNatDoc,currentTarget,listener,id,commMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"asyncret",currentTarget,listener,id,commMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function CometTaskEventListener(itsNatDoc,id,commMode,timeout)
{
    this.NormalEventListener = NormalEventListener;
    this.NormalEventListener(itsNatDoc,"cometret",null,null,id,commMode,timeout);

    this.createNormalEvent = createNormalEvent;

    function createNormalEvent(evt) { return new DOMEvent(this); }
}

function Document()
{
    this.init = init;
    this.getVisualRootElement = null;
    this.createDOMPathResolver = null;
    this.createScriptElement = null;
    this.sendEventByScript = sendEventByScript;
    this.getValidNode = getValidNode;
    this.getParentNode = getParentNode;
    this.getLenChildNodes = getLenChildNodes;
    this.getChildNode = getChildNode;
    this.setTimeout = setTimeout;
    this.clearTimeout = clearTimeout;
    this.showErrorMessage = showErrorMessage;
    this.getErrorMode = getErrorMode;
    this.genParamURL = genParamURL;
    this.getServletPath = getServletPath;
    this.getPropInNative = getPropInNative;
    this.setPropInNative = setPropInNative;
    this.removePropInNative = removePropInNative;
    this.addNodeCache = addNodeCache;
    this.addNodeCache2 = addNodeCache2;
    this.getNode = getNode;
    this.getNode2 = getNode2;
    this.getNodeCached = getNodeCached;
    this.removeNodeCache = removeNodeCache;
    this.getNodeCacheId = getNodeCacheId;
    this.getStringPathFromNode = getStringPathFromNode;
    this.addDOMEventListener = addDOMEventListener;
    this.removeDOMEventListener = removeDOMEventListener;
    this.addTimerEventListener = addTimerEventListener;
    this.removeTimerEventListener = removeTimerEventListener;
    this.updateTimerEventListener = updateTimerEventListener;
    this.sendAsyncTaskEvent = sendAsyncTaskEvent;
    this.sendCometTaskEvent = sendCometTaskEvent;
    this.sendContinueEvent = sendContinueEvent;
    this.addUserEventListener = addUserEventListener;
    this.removeUserEventListener = removeUserEventListener;
    this.createUserEvent = createUserEvent;
    this.dispatchUserEvent = dispatchUserEvent;
    this.dispatchUserEvent2 = dispatchUserEvent2;
    this.fireUserEvent = fireUserEvent;
    this.sendAttachTimerRefresh = sendAttachTimerRefresh;
    this.stopAttachTimerRefresh = stopAttachTimerRefresh;
    this.sendAttachCometTaskRefresh = sendAttachCometTaskRefresh;
    this.addAttachUnloadListener = addAttachUnloadListener;
    this.setCharacterData = setCharacterData;
    this.createTextNode = createTextNode;
    this.setTextData = setTextData;
    this.setTextData2 = setTextData2;
    this.getAttribute = getAttribute;
    this.setAttribute = setAttribute;
    this.setAttribute2 = setAttribute2;
    this.setAttributeNS = setAttributeNS;
    this.setAttributeNS2 = setAttributeNS2;
    this.removeAttribute = removeAttribute;
    this.removeAttribute2 = removeAttribute2;
    this.removeAttributeNS = removeAttributeNS;
    this.removeAttributeNS2 = removeAttributeNS2;
    this.appendChild = appendChild;
    this.appendChild2 = appendChild2;
    this.appendChild3 = appendChild3;
    this.insertBefore = insertBefore;
    this.insertBefore2 = insertBefore2;
    this.insertBefore3 = insertBefore3;
    this.removeChild = removeChild;
    this.removeChild2 = removeChild2;
    this.removeChild3 = removeChild3;
    this.removeAllChild = removeAllChild;
    this.removeAllChild2 = removeAllChild2;
    this.setInnerHTML = setInnerHTML;
    this.setInnerHTML2 = setInnerHTML2;
    this.setInnerXML = null; // implementar
    this.setInnerXML2 = setInnerXML2;
    this.addEventMonitor = addEventMonitor;
    this.removeEventMonitor = removeEventMonitor;
    this.fireEventMonitors = fireEventMonitors;
    this.setEnableEventMonitors = setEnableEventMonitors;
    this.dispatchEvent2 = dispatchEvent2;
    this.addGlobalEventListener = addGlobalEventListener;
    this.removeGlobalEventListener = removeGlobalEventListener;


    function init(doc,win,browserType,browserSubType,sessionToken,sessionId,clientId,servletPath,usePost,attachType,errorMode,xhrSyncSup,numScripts)
    {
        // Mas attribs
        this.browser = new Browser(browserType,browserSubType);
        this.doc = doc;
        if (!this.browser.isBatik())
        {
            var func = function () { return this.itsNatDoc; };
            doc.getItsNatDoc = func;
        }
        doc.itsNatDoc = this;
        this.win = win;
        this.sessionToken = sessionToken;
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.servletPath = servletPath;
        this.usePost = usePost;
        this.attachType = attachType;
        this.errorMode = errorMode;
        this.xhrSyncSup = xhrSyncSup;
        this.evtMgr = new EventMgr(this);

        this.disabledEvents = false;
        this.pathResolver = this.createDOMPathResolver();
        var useDelete = !this.browser.isBlackBerryOld5(); // En BB JDE v5.0 el delete borra otros elementos erroneamente
        this.nodeCacheById = new Map(useDelete);
        this.domListeners = new Map(useDelete);
        this.timerListeners = new Map(useDelete);
        this.userListenersById = new Map(useDelete);
        this.userListenersByName = new Map(useDelete); // listeners sin nodo asociado
        this.evtMonitors = new List();
        this.enableEvtMonitors = true;
        this.globalEventListeners = new List();

        if (numScripts > 0)
        {
            var scriptParent = this.getVisualRootElement();
            var current = scriptParent.lastChild;
            do
            {
                if ((current.nodeType == 1) && //  1 = Node.ELEMENT_NODE
                    (current.getAttribute("id") == ("itsnat_load_script_" + numScripts)) )
                {
                    var prev = current.previousSibling;
                    scriptParent.removeChild(current);
                    current = prev;
                    numScripts--;
                }
                else current = current.previousSibling;
            }
            while((numScripts > 0)&&(current != null));
        }
    }

    function sendEventByScript(servletPath,paramURL,evt,timeout)
    {
        if (typeof this.scriptForEvtCount == "undefined") this.scriptForEvtCount = 0;

        var func;
        var scriptId = "itsnat_script_evt_" + new Date().getTime() + "_" + this.scriptForEvtCount; // sirve como id unico y como timestamp para evitar cacheos
        var url = servletPath + "?" + paramURL + "&itsnat_script_evt_id=" + scriptId;
        var script = this.createScriptElement(url);
        script.itsNatDoc = this;
        script.id = scriptId; // Sirve para eliminarse posteriormente
        script.evt = evt;
        script.timeoutHnd = null;

        func = function(evt)
        {
            var script = arguments.callee.script;
            if (script.itsNatDoc.browser.isMSIEOld())
            {   // http://www.nczonline.net/blog/2009/06/23/loading-javascript-without-blocking/
                if (!script.executed) return; // Mejor tecnica que el lio de readyState "loaded" y "complete"
                if ((script.readyState != "loaded")&&(script.readyState != "complete")) return; // por si acaso
                script.onreadystatechange = null; // para evitar reentradas y memory leak (MSIE)
            }

            script.parentNode.removeChild(script);
            if (script.timeoutHnd != null) script.itsNatDoc.clearTimeout(script.timeoutHnd);
            var evt = script.evt;
            evt.processRespBegin();
            if (typeof script.error != "undefined") evt.processRespError(script.code);
            else evt.processRespValid(script.code);
            evt.processRespEnd(true);
        };
        func.script = script;
        if (this.browser.isMSIEOld()) script.onreadystatechange = func;
        else script.onload = func;

        if (timeout != -1)
        {
            func = function()
            {
                var script = arguments.callee.script;
                script.parentNode.removeChild(script);
                script.evt.processRespTimeout();
            };
            func.script = script;
            script.timeoutHnd = this.setTimeout(func,timeout);
        }

        this.doc.documentElement.appendChild(script); // En caso de X/HTML esta mal formado pero funciona (tecnica no intrusiva), en SVG/XUL es intrusivo
        this.scriptForEvtCount++;
    }

    function getValidNode(node) { return node; } // Se extiende para SVGWeb

    function getParentNode(node) { return node.parentNode; }

    function getLenChildNodes(node) { return node.childNodes.length; }

    function getChildNode(i,parentNode)
    {
        var childNodes = parentNode.childNodes;
        if (typeof childNodes.item != "undefined") return childNodes.item(i);
        else return childNodes[i];
    }

    function setTimeout(func,delay) { return this.win.setTimeout(func,delay); }

    function clearTimeout(handle) { return this.win.clearTimeout(handle); }

    function showErrorMessage(serverErr,e,msg)
    {
        var errorMode = this.getErrorMode();
        if (errorMode == 1) return; // ClientErrorMode.NOT_SHOW_ERRORS

        if (serverErr) // Pagina HTML con la excepcion del servidor
        {
            if ((errorMode == 2) || (errorMode == 4)) // 2 = ClientErrorMode.SHOW_SERVER_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
                    alert("SERVER ERROR: " + msg);
        }
        else
        {
             if ((errorMode == 3) || (errorMode == 4)) // 3 = ClientErrorMode.SHOW_CLIENT_ERRORS, 4 = ClientErrorMode.SHOW_SERVER_AND_CLIENT_ERRORS
             {
                // Ha sido un error JavaScript
                if (e != null) alert(e + "\n" + msg); // En FireFox la info de la excepcion es util (en MSIE no)
                else alert(msg);
             }
        }
    }

    function getErrorMode() { return this.errorMode; }

    function genParamURL()
    {
        var url = "";
        url += "itsnat_client_id=" + this.clientId;
        url += "&itsnat_session_token=" + this.sessionToken;
        url += "&itsnat_session_id=" + this.sessionId;
        return url;
    }

    function getServletPath() { return this.servletPath; }

    function getPropInNative(obj,name)
    {
        if (this.browser.isBatik()) return obj.getUserData(name);
        var value = obj[name];
        if (typeof value == "undefined") return null;
        return value;
    }

    function setPropInNative(obj,name,value)
    {
        if (typeof value == "undefined") value = null;
        if (this.browser.isBatik()) obj.setUserData(name,value,null);
        else obj[name] = value;
    }

    function removePropInNative(obj,name)
    {
        this.setPropInNative(obj,name,null);
        try{ delete obj[name]; }catch(e) {} // En MSIE no se puede usar delete en nodos DOM nativos, al menos queda como null
    }

    function getNodeCached(id)
    {
        if (id == null) return null;
        return this.nodeCacheById.get(id);
    }

    function removeNodeCache(idList)
    {
        var len = idList.length;
        for(var i = 0; i < len; i++)
        {
            var id = idList[i];
            var node = this.nodeCacheById.remove(id);
            this.removePropInNative(node,"itsNatCacheId");
        }
    }

    function getNodeCacheId(node)
    {
        if (node == null) return null;
        return this.getPropInNative(node,"itsNatCacheId");
    }

    function addNodeCache(idObj) { return this.getNode(idObj); }

    function addNodeCache2(id,node)
    {
        if (id == null) return; // si id es null cache desactivado
        this.nodeCacheById.put(id,node);
        this.setPropInNative(node,"itsNatCacheId",id);
    }

    function getNode(idObj)
    {
        if (idObj == null) return null;
        var cachedParentId = null,id = null,path = null,newCachedParentIds = null;
        if (typeof idObj == "string") id = idObj;
        else // array
        {
            var len = idObj.length;
            if (len == 2)
            {
              id = idObj[0];
              newCachedParentIds = idObj[1];
            }
            else if (len >= 3)
            {
              cachedParentId = idObj[0];
              id = idObj[1];
              path = idObj[2];
              if (len == 4) newCachedParentIds = idObj[3];
            }
        }

        var cachedParent = null;
        if (cachedParentId != null)
        {
            cachedParent = this.getNodeCached(cachedParentId);
            if (cachedParent == null) throw "Unexpected error";
        }

        var node = this.getNode2(cachedParent,[id,path]);
        if (newCachedParentIds != null)
        {
            var parentNode = this.getParentNode(node);
            var len = newCachedParentIds.length;
            for(var i = 0; i < len; i++)
            {
                this.addNodeCache2(newCachedParentIds[i],parentNode);
                parentNode = this.getParentNode(parentNode);
            }
        }
        return node;
    }

    function getNode2(parentNode,idObj)
    {
        if (idObj == null) return null;
        var id = null;
        var path = null;
        if (typeof idObj == "string") id = idObj;
        else // array
        {
            id = idObj[0];
            if (idObj.length == 2) path = idObj[1];
        }
        if ((id == null) && (path == null)) return null; // raro
        if (path == null) return this.getNodeCached(id); // Debe estar en la cache
        else
        {
            // si parentNode es null caso de path absoluto, si no, path relativo
            var node = this.pathResolver.getNodeFromPath(path,parentNode);
            if (id != null) this.addNodeCache2(id,node);
            return node;
        }
    }

    function getStringPathFromNode(node)
    {
        if (node == null) return null;

        var nodeId = this.getNodeCacheId(node);
        if (nodeId != null) return "id:" + nodeId; // es undefined si no esta cacheado (o null si se quito)
        else
        {
            var parentId = null;
            var parentNode = node;
            do
            {
                parentNode = this.getParentNode(parentNode);
                parentId = this.getNodeCacheId(parentNode); // si parentNode es null devuelve null
            }
            while((parentId == null)&&(parentNode != null));

            var path = this.pathResolver.getStringPathFromNode(node,parentNode); // Si parentNode es null (parentId es null) devuelve un path absoluto
            if (parentNode != null) return "pid:" + parentId + ":" + path;
            else return path; // absoluto
        }
    }

    function addDOMEventListener(idObj,type,listenerId,listener,useCapture,commMode,timeout,typeCode)
    {
        var node = this.getNode(idObj);
        var listenerWrapper = new DOMStdEventListener(this,node,type,listener,listenerId,useCapture,commMode,timeout,typeCode);
        this.domListeners.put(listenerId,listenerWrapper);
        this.addDOMEventListener2(listenerWrapper,node,type,useCapture);
        return node;
    }

    function removeDOMEventListener(listenerId)
    {
        var listenerWrapper = this.domListeners.remove(listenerId);
        var node = listenerWrapper.getCurrentTarget();
        var type = listenerWrapper.getType();
        var useCapture = listenerWrapper.isUseCapture();
        this.removeDOMEventListener2(listenerWrapper,node,type,useCapture);
        return node;
    }

    function addTimerEventListener(idObj,listenerId,listener,commMode,timeout,delay)
    {
        var node = this.getNode(idObj); // puede ser nulo
        var listenerWrapper = new TimerEventListener(this,node,listener,listenerId,commMode,timeout);
        var timerFunction = function() { arguments.callee.listenerWrapper.dispatchEvent(null); };
        timerFunction.listenerWrapper = listenerWrapper;
        listenerWrapper.timerFunction = timerFunction;
        this.timerListeners.put(listenerId,listenerWrapper);
        var handle = this.setTimeout(timerFunction,delay);
        listenerWrapper.setHandle(handle);
    }

    function removeTimerEventListener(listenerId)
    {
        var listenerWrapper = this.timerListeners.remove(listenerId);
        if (!listenerWrapper) return;
        this.clearTimeout(listenerWrapper.getHandle());
    }

    function updateTimerEventListener(listenerId,delay)
    {
        var listenerWrapper = this.timerListeners.get(listenerId);
        if (!listenerWrapper) return;
        var handle = this.setTimeout(listenerWrapper.timerFunction,delay);
        listenerWrapper.setHandle(handle);
    }

    function sendAsyncTaskEvent(idObj,listenerId,listener,commMode,timeout)
    {
        var currTarget = this.getNode(idObj);
        var listenerWrapper = new AsyncTaskEventListener(this,currTarget,listener,listenerId,commMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function sendCometTaskEvent(listenerId,commMode,timeout)
    {
        var listenerWrapper = new CometTaskEventListener(this,listenerId,commMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function sendContinueEvent(idObj,listenerId,listener,commMode,timeout)
    {
        var currTarget = this.getNode(idObj);
        var listenerWrapper = new ContinueEventListener(this,currTarget,listener,listenerId,commMode,timeout);
        listenerWrapper.dispatchEvent(null);
    }

    function addUserEventListener(idObj,name,listenerId,listener,commMode,timeout)
    {
        var currTarget = this.getNode(idObj);
        var listenerWrapper = new UserEventListener(this,currTarget,name,listener,listenerId,commMode,timeout);
        this.userListenersById.put(listenerId,listenerWrapper);
        var listenersByName;
        if (currTarget == null) listenersByName = this.userListenersByName;
        else
        {
            listenersByName = this.getPropInNative(currTarget,"itsNatUserListenersByName");
            if (!listenersByName)
            {
                listenersByName = new Map(this.userListenersByName.useDelete); // La regla de useDelete es la misma
                this.setPropInNative(currTarget,"itsNatUserListenersByName",listenersByName);
            }
        }
        var listeners = listenersByName.get(name);
        if (!listeners)
        {
            listeners = new Map(listenersByName.useDelete); // La regla de useDelete es la misma
            listenersByName.put(name,listeners);
        }
        listeners.put(listenerId,listenerWrapper);
    }

    function removeUserEventListener(listenerId)
    {
        var listenerWrapper = this.userListenersById.remove(listenerId);
        if (!listenerWrapper) return;

        var listenersByName;
        var currTarget = listenerWrapper.getCurrentTarget();
        if (currTarget == null) listenersByName = this.userListenersByName;
        else listenersByName = this.getPropInNative(currTarget,"itsNatUserListenersByName");

        var name = listenerWrapper.getName();
        var listeners = listenersByName.get(name);
        listeners.remove(listenerId);  // No podemos saber cuando queda vacio
    }

    function createUserEvent(name) { return new UserEventPublic(name); }

    function dispatchUserEvent(currTarget,evt)
    {
        var listenersByName;
        if (currTarget == null) listenersByName = this.userListenersByName;
        else listenersByName = this.getPropInNative(currTarget,"itsNatUserListenersByName");
        if (!listenersByName) return true;

        var listeners = listenersByName.get(evt.getName());
        if (!listeners) return true;
        listeners.doForAll(function (id,listenerWrapper)
            { listenerWrapper.dispatchEvent(evt);  }
        );
        return true; // no cancelable
    }

    function dispatchUserEvent2(idObj,name,evt)
    {
        var elem = this.getNode(idObj);
        return this.dispatchUserEvent(elem,name,evt);
    }

    function fireUserEvent(currTarget,name)
    {
        var evt = this.createUserEvent(name);
        this.dispatchUserEvent(currTarget,evt);
    }

    function sendAttachTimerRefresh(callback,interval,commMode,timeout)
    {
        if (!callback) return; // Se esta destruyendo el documento (FireFox)
        if (this.attachTimerHandle == null) return;
        var evt = new AttachTimerRefreshEvent(callback,interval,commMode,timeout,this);
        evt.sendEvent();
    }

    function stopAttachTimerRefresh()
    {
        this.clearTimeout(this.attachTimerHandle);
        this.attachTimerHandle = null;
    }

    function sendAttachCometTaskRefresh(listenerId,commMode,timeout)
    {
        var evt = new AttachCometTaskRefreshEvent(listenerId,commMode,timeout,this);
        evt.sendEvent();
    }

    function addAttachUnloadListener(node,type,commMode)
    {
        var itsNatDoc = this;
        var listener = function ()
        {
            var evt = new AttachUnloadEvent(commMode,-1,itsNatDoc);
            evt.sendEvent();
        };
        this.addAttachUnloadListener2(node,type,listener);
    }

    function setCharacterData(idObj,value)
    {
        var charNode = this.getNode(idObj);
        charNode.data = value;
    }

    function createTextNode(parentNode,value) { return this.doc.createTextNode(value); }

    function setTextData(parentNode,textNode,value) { textNode.data = value; }

    function setTextData2(parentIdObj,nextSibIdObj,value)
    {
        var parentNode = this.getNode(parentIdObj);
        var nextSibling = this.getNode2(parentNode,nextSibIdObj);
        var textNode = null;
        if (nextSibling != null) textNode = nextSibling.previousSibling;
        else textNode = parentNode.lastChild;
        if (textNode != null)
        {
            if (textNode.nodeType != 3) textNode = null;  // 3 = Node.TEXT_NODE No es de texto, fue filtrado
            else if (this.browser.isOperaMini())
            {
                // Text.data es solo lectura => reinsertamos con el nuevo valor
                parentNode.removeChild(textNode);
                textNode = null;
            }
        }

        if (textNode == null) // Hay nodo de texto en el servidor pero en el browser se filtro por tener solo espacios o similares
        {
            textNode = this.createTextNode(parentNode,value);
            if (nextSibling != null) parentNode.insertBefore(textNode,nextSibling);
            else parentNode.appendChild(textNode);
        }
        else this.setTextData(parentNode,textNode,value);
    }

    function getAttribute(elem,name)
    {
        // Distinguimos entre atributo con posible valor "" y atributo que no existe (null)
        var hasAttr;
        if (elem.hasAttribute) hasAttr = elem.hasAttribute(name); // MSIE por ej. no tiene hasAttribute
        else hasAttr = (elem.getAttributeNode(name) != null);
        if (!hasAttr) return null;
        return elem.getAttribute(name);
    }

    function setAttribute(elem,name,value) { elem.setAttribute(name,value); }

    function setAttribute2(idObj,name,value)
    {
        var elem = this.getNode(idObj);
        this.setAttribute(elem,name,value);
    }

    function setAttributeNS(elem,namespaceURI,name,value) { elem.setAttributeNS(namespaceURI,name,value); }

    function setAttributeNS2(idObj,namespaceURI,name,value)
    {
        var elem = this.getNode(idObj);
        this.setAttributeNS(elem,namespaceURI,name,value);
    }

    function removeAttribute(elem,name) { elem.removeAttribute(name); }

    function removeAttribute2(idObj,name)
    {
        var elem = this.getNode(idObj);
        this.removeAttribute(elem,name);
    }

    function removeAttributeNS(elem,namespaceURI,name) { elem.removeAttributeNS(namespaceURI,name); }

    function removeAttributeNS2(idObj,namespaceURI,name)
    {
        var elem = this.getNode(idObj);
        this.removeAttributeNS(elem,namespaceURI,name);
    }

    function appendChild(parentNode,newChild)
    {
        if (typeof newChild == "string") newChild = this.createTextNode(parentNode,newChild);
        parentNode.appendChild(newChild);
    }

    function appendChild2(parentNode,newChild,newId)
    {
        this.appendChild(parentNode,newChild);
        if (newId != null) this.addNodeCache2(newId,newChild);
    }

    function appendChild3(idObj,newChild,newId)
    {
        var parentNode = this.getNode(idObj);
        this.appendChild2(parentNode,newChild,newId);
    }

    function insertBefore(parentNode,newChild,childRef)
    {
        if (childRef == null) { this.appendChild(parentNode,newChild); return; }
        if (typeof newChild == "string") newChild = this.createTextNode(parentNode,newChild);
        parentNode.insertBefore(newChild,childRef);
    }

    function insertBefore2(parentNode,newChild,childRef,newId)
    {
        this.insertBefore(parentNode,newChild,childRef);
        if (newId != null) this.addNodeCache2(newId,newChild);
    }

    function insertBefore3(parentIdObj,newChild,childRefIdObj,newId)
    {
        var parentNode = this.getNode(parentIdObj);
        var childRef = this.getNode2(parentNode,childRefIdObj);
        this.insertBefore2(parentNode,newChild,childRef,newId);
    }

    function removeChild(child)
    {
        if (child == null) return; // Si es un nodo de texto que fue filtrado
        this.getParentNode(child).removeChild(child);
    }

    function removeChild2(id,isText)
    {
        var child = this.getNode(id);
        if (isText && (child != null) && (child.nodeType != 3)) return; // 3 = Node.TEXT_NODE, no encontrado, parece que fue filtrado
        this.removeChild(child);
    }

    function removeChild3(parentIdObj,childRelPath,isText)
    {
        var parentNode = this.getNode(parentIdObj);
        var child = this.getNode2(parentNode,[null,childRelPath]);
        if (isText && (child != null) && (child.nodeType != 3)) return; // 3 = Node.TEXT_NODE, no encontrado, parece que fue filtrado
        this.removeChild(child);
    }

    function removeAllChild(parentNode)
    {
        while(this.getLenChildNodes(parentNode) > 0)
        {
            var child = this.getChildNode(0,parentNode); // Los hijos verdaderos
            this.removeChild(child);
        }
    }

    function removeAllChild2(parentIdObj)
    {
        var parentNode = this.getNode(parentIdObj);
        this.removeAllChild(parentNode);
    }

    function setInnerHTML(parentNode,value) { parentNode.innerHTML = value; }

    function setInnerHTML2(idObj,value)
    {
        var parentNode = this.getNode(idObj);
        this.setInnerHTML(parentNode,value);
    }

    function setInnerXML2(idObj,value)
    {
        var parentNode = this.getNode(idObj);
        this.setInnerXML(parentNode,value);
    }

    function addEventMonitor(monitor) { this.evtMonitors.addLast(monitor); }

    function removeEventMonitor(monitor)
    {
        var index = -1;
        for(var i = 0; i < this.evtMonitors.size(); i++)
        {
            var curr = this.evtMonitors.get(i);
            if (curr == monitor)
                index = i;
        }
        if (index == -1) return;
        this.evtMonitors.array.splice(index,1);
    }

    function fireEventMonitors(before,timeout,evt)
    {
        if (!this.enableEvtMonitors) return;

        for(var i = 0; i < this.evtMonitors.size(); i++)
        {
            var curr = this.evtMonitors.get(i);
            if (before) curr.before(evt);
            else curr.after(evt,timeout);
        }
    }

    function setEnableEventMonitors(value) { this.enableEvtMonitors = value; }

    function dispatchEvent2(idObj,type,evt)
    {
        var node = this.getNode(idObj);
        return this.dispatchEvent(node,type,evt);
    }

    function addGlobalEventListener(listener) { this.globalEventListeners.add(listener); }

    function removeGlobalEventListener(listener) { this.globalEventListeners.remove(listener); }

} // Document

function DOMPathResolverHTMLDoc(itsNatDoc)
{
    this.DOMPathResolver = itsnat.DOMPathResolver;
    this.DOMPathResolver(itsNatDoc);

    this.DOMPathResolver_getChildNodeFromStrPos = this.getChildNodeFromStrPos;
    this.getChildNodeFromStrPos = getChildNodeFromStrPos;
    function getChildNodeFromStrPos(parentNode,posStr)
    {
        if (parentNode == this.itsNatDoc.doc.documentElement) // <html>
        {
            if (posStr == "bo")
                return this.itsNatDoc.getHTMLBody();
            else if (posStr == "he")
                return this.itsNatDoc.getHTMLHead();
        }
        return this.DOMPathResolver_getChildNodeFromStrPos(parentNode,posStr);
    }

    this.DOMPathResolver_getNodeChildPosition = this.getNodeChildPosition;
    this.getNodeChildPosition = getNodeChildPosition;
    function getNodeChildPosition(node)
    {
        if (this.itsNatDoc.getParentNode(node) == this.itsNatDoc.doc.documentElement) // <html>
        {
            if (node == this.itsNatDoc.getHTMLBody())
                return "bo";
            else if (node == this.itsNatDoc.getHTMLHead())
                return "he";
        }
        return this.DOMPathResolver_getNodeChildPosition(node);
    }
}


function HTMLDocument()
{
    this.Document = Document;
    this.Document();

    this.getVisualRootElement = getVisualRootElement;
    this.getHTMLBody = getHTMLBody;
    this.getHTMLHead = getHTMLHead;
    this.getSelectedHTMLSelect = getSelectedHTMLSelect;
    this.createScriptElement = createScriptElement;
    this.getChildNodeWithTagName = getChildNodeWithTagName;

    function getVisualRootElement() { return this.getHTMLBody(); }

    function getHTMLBody() { return this.doc.body; }

    function getHTMLHead() { return this.getChildNodeWithTagName(this.doc.documentElement,"head"); }

    function getChildNodeWithTagName(parentNode,tagName)
    {
        var child = parentNode.firstChild;
        while(child != null)
        {
            if ((child.nodeType == 1) && (child.tagName.toLowerCase() == tagName))
                return child;
            child = child.nextSibling;
        }
        return null;
    }

    function getSelectedHTMLSelect(elem) // Es llamada desde el servidor
    {
        var res = "",nselec = 0,options = elem.options,len = options.length;
        for(var i = 0; i < len; i++)
        {
            if (!options[i].selected) continue;
            if (nselec > 0) res += ",";
            res += i; nselec++;
        }
        return res;
    }

    function createScriptElement(url)
    {
        var script = this.doc.createElement("script");
        script.src = url;
        return script;
    }
}

} // pkg_itsnat

function itsnat_init(win)
{
    win.itsnat = new Object();
    pkg_itsnat(win.itsnat);
    return win.itsnat;
}


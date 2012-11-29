/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_msie_pocket(pkg)
{
    pkg.DOMPathResolverHTMLDocMSIEPocket = DOMPathResolverHTMLDocMSIEPocket;
    pkg.MSIEPocketHTMLDocument = MSIEPocketHTMLDocument;
    pkg.MSIEPocketDOMListenerList = MSIEPocketDOMListenerList;

function DOMPathResolverHTMLDocMSIEPocket(itsNatDoc)
{
    this.DOMPathResolverHTMLDocMSIEOld = itsnat.DOMPathResolverHTMLDocMSIEOld;
    this.DOMPathResolverHTMLDocMSIEOld(itsNatDoc);

    this.getChildNodeFromPos = getChildNodeFromPos;  // Redefinimos
    this.getNodeChildPosition = getNodeChildPosition; // Redefinimos

    function getChildNodeFromPos(parentNode,pos,isTextNode)
    {
        // Reescribir el algoritmo, el precio a pagar por no tener firstChild, nextSibling, nodeType etc
        var currPos = 0;
        var len = this.itsNatDoc.getLenChildNodes(parentNode);
        for(var i = 0; i < len; i++)
        {
            var currNode = this.itsNatDoc.getChildNode(i,parentNode);
            currNode = this.itsNatDoc.getValidNode(currNode); // Por si acaso (no necesario, no SVGWeb)
            if (!this.isFiltered(currNode)) // se puede quitar esta llamada
            {
                if (currPos == pos) return currNode;
                else currPos++;
            }
        }
        return null;
    }

    function getNodeChildPosition(node) // Idem razones que getChildNodeFromPos
    {
        if (node == this.itsNatDoc.doc.documentElement) return "de";
        var parentNode = this.itsNatDoc.getParentNode(node);
        if (parentNode == null) throw "Unexpected error";
        var pos = 0;
        var len = this.itsNatDoc.getLenChildNodes(parentNode);
        for(var i = 0; i < len; i++)
        {
            var currNode = this.itsNatDoc.getChildNode(i,parentNode);
            currNode = this.itsNatDoc.getValidNode(currNode); // Por si acaso (no necesario, no SVGWeb)
            if (currNode == node) return pos;
            pos++;
        }
        return "-1";
    }
}


function MSIEPocketDOMListenerList(type,currentTarget,itsNatDoc)
{
    this.MSIEOldDOMListenerList = itsnat.MSIEOldDOMListenerList;
    this.MSIEOldDOMListenerList(type,currentTarget,itsNatDoc);

    this.MSIEPocketDOMListenerList_super_dispatchEventLocal = this.dispatchEventLocal;

    this.dispatchEventLocal = dispatchEventLocal; // redefine
    this.setInlineHandler = setInlineHandler;

    // Attrs
    this.inlineHandler = null;

    // Construction
    var inlineHandler = itsNatDoc.getEventListenerInline(currentTarget,type);
    this.setInlineHandler(inlineHandler);

    function dispatchEventLocal(evt,capture)
    {
        if (!capture && this.inlineHandler != null)
        {
            this.itsNatDoc.win.event = evt; // para simular lo mas posible un MSIE desktop
            var res = this.inlineHandler.call(this.currentTarget);
            if (typeof res != "undefined") evt.returnValue = res;
        }
        this.MSIEPocketDOMListenerList_super_dispatchEventLocal(evt,capture);
    }

    function setInlineHandler(code)
    {
        if (code != null) this.inlineHandler = new Function(code); // code es string
        else this.inlineHandler = null;
    }
}

function FakeMSIEEvent(type,target)
{
    // Se simula un evento nativo MSIE
    this.type = type;
    this.srcElement = target;
    this.cancelBubble = false;
    this.returnValue = undefined;
    this.fromElement = null; // da igual, no hay mouseover/mouseto
    this.toElement = null;  // da igual, no hay mouseover/mouseto
    this.screenX = 0; this.screenY = 0; this.clientX = 0; this.clientY = 0;
    this.ctrlKey = false; this.shiftKey = false; this.altKey = false;
    this.button = 1; // boton izquierdo
    this.keyCode = 0; // No hay eventos de teclado
}

function MSIEPocketHTMLDocument()
{
    this.MSIEOldHTMLDocument = itsnat.MSIEOldHTMLDocument;
    this.MSIEOldHTMLDocument();

    this.MSIEPocketHTMLDocument_super_init = this.init;
    this.init = init;
    this.getChildNodeWithTagName = getChildNodeWithTagName; // Redefinimos
    this.createDOMPathResolver = createDOMPathResolver;
    this.createMSIEOldDOMListenerList = createMSIEOldDOMListenerList;
    this.appendNeededAuxTree = appendNeededAuxTree;
    this.getParentNode = getParentNode;
    this.extractChildNodes = extractChildNodes;
    this.restoreChildNodes = restoreChildNodes;
    this.setInnerHTMLInCopy = setInnerHTMLInCopy;
    this.isInnerHTMLSupported = isInnerHTMLSupported;
    this.setInnerHTML = setInnerHTML; // NO redefine
    this.getOuterHTMLUnBound = getOuterHTMLUnBound;
    this.getOuterHTMLBound = getOuterHTMLBound;
    this.getParentInnerHTMLPos = getParentInnerHTMLPos;
    this.getParentTable = getParentTable;
    this.hideTable = hideTable;
    this.showTable = showTable;
    this.appendChild = appendChild; // Redefinimos
    this.insertBefore = insertBefore; // Redefinimos
    this.removeChild = removeChild; // Redefinimos
    this.replaceChild = replaceChild;
    this.insertAfter = insertAfter;
    this.appendFilteredNode = appendFilteredNode;
    this.appendFilteredNode2 = appendFilteredNode2;
    this.insertBeforeFilteredNode = insertBeforeFilteredNode;
    this.setFilteredNode = setFilteredNode; // Redefinimos
    this.parseElement = parseElement;
    this.addAttributes = addAttributes;
    this.procAttrAsHandler = procAttrAsHandler;
    this.setAttribute = setAttribute; // Redefinimos
    this.removeAttribute = removeAttribute; // Redef
    this.manageAttribute = manageAttribute; // Interna
    this.getAttribute = getAttribute; // Redefine
    this.updateReferences = updateReferences;
    this.getEventListenerInline = getEventListenerInline;
    this.getEventListenerRegistry = getEventListenerRegistry; // Redef.
    this.cleanEventListenerRegistry = cleanEventListenerRegistry; // Redef.
    this.dispatchEvent = dispatchEvent; // Redefinimos
    this.dispatchEventBubble = dispatchEventBubble;


    this.parseElemsInfo = {
      "TBODY": ["TABLE"], "TFOOT": ["TABLE"], "THEAD": ["TABLE"],
      "CAPTION": ["TABLE"], "COLGROUP": ["TABLE"],
      "DT": ["DL"], "DD": ["DL"],
      "LEGEND": ["FIELDSET"], "OPTION": ["SELECT"], "OPTGROUP": ["SELECT"],
      "LI": ["UL"], "AREA": ["MAP"], "COL": ["TABLE","COLGROUP"],
      "TR": ["TABLE","TBODY"], "TD": ["TABLE","TBODY","TR"], "TH": ["TABLE","TBODY","TR"]
    };

    // Elementos que no admiten elem.innerHTML = ... Casi todos no admiten
    // nodos de texto utiles como hijos o solo tienen un nodo de texto hijo.
    this.innerHTMLNotValid = {
      "TABLE":true, "TBODY":true, "TFOOT":true, "THEAD":true, "TR":true,
      "COL":true, "COLGROUP":true, "FRAMESET":true, "HTML":true, "STYLE":true,
      "TITLE":true, "SCRIPT":true, "SELECT":true, "PRE":true, "TEXTAREA":true,
      "IFRAME":true, "IMG":true, "INPUT":true, "BUTTON":true
    };

    this.evtNotBubble = {"load":true,"unload":true,"focus":true,"blur":true,"DOMNodeInsertedIntoDocument":true,"DOMNodeRemovedFromDocument":true,"beforeunload":true,"DOMContentLoaded":true };

    function init(doc,win,browserType,browserSubType,sessionToken,sessionId,clientId,servletPath,usePost,attachType,errorMode,xhrSyncSup,numScripts)
    {
        if (numScripts > 0)
        {
            // IE Pocket no tiene lastChild ni previousSibling ni nodeType
            var nodeList = doc.body.childNodes;
            for(var i = nodeList.length - 1; i >= 0; i--)
            {
                var node = nodeList.item(i);
                if ((node.tagName == "SCRIPT") && (node.id == "itsnat_load_script_" + numScripts))
                {
                    doc.body.removeChild(node);
                    numScripts--;
                    if (numScripts == 0) break;
                }
            }
            numScripts = 0; // Que quede claro,asi evitamos la eliminacion normal
        }

        this.MSIEPocketHTMLDocument_super_init(doc,win,browserType,browserSubType,sessionToken,sessionId,clientId,servletPath,usePost,attachType,errorMode,xhrSyncSup,numScripts);
    }

    function getChildNodeWithTagName(parentNode,tagName) // Redefinimos porque no disponemos de nodeType ni firstChild/nextSibling
    {
        var len = this.getLenChildNodes(parentNode);
        for(var i = 0; i < len; i++)
        {
            var child = this.getChildNode(i,parentNode);
            if (child.tagName == tagName.toLowerCase()) return child;
        }
        return null;
    }

    function createDOMPathResolver() { return new DOMPathResolverHTMLDocMSIEPocket(this); }

    function createMSIEOldDOMListenerList(node,type) { return new MSIEPocketDOMListenerList(type,node,this); }

    function appendNeededAuxTree(parentNode,childTagName,htmlCode)
    {
        var levels = 0,prefix = "",suffix = "";
        var parentList = this.parseElemsInfo[childTagName];
        if (parentList)
        {
            levels = parentList.length;
            for(var i = 0; i < levels; i++)
            {
                prefix += "<" + parentList[i] + ">";
                suffix += "</" + parentList[levels - i - 1] + ">";
            }
        }
        parentNode.innerHTML = prefix + htmlCode + suffix;
        return levels;
    }

    function getParentNode(elem)
    {
        try { return elem.parentNode; } catch(e) { return null; }
    }

    function extractChildNodes(elem,remove)
    {
        var len = elem.childNodes.length;
        if (len == 0) return null;
        var childNodes = new Array(len);
        for(var i = 0; i < len; i++)
            childNodes[i] = elem.childNodes.item(i);

        if (remove)
            while(elem.childNodes.length > 0) // Del ultimo (mas abajo) al primero (mas arriba) para evitar el movimiento/recalculo visual
                this.removeChild( elem.childNodes.item(elem.childNodes.length - 1) );

        return childNodes;
    }

    function restoreChildNodes(oldChildNodes,elem,indexNew)
    {
        if (oldChildNodes == null) return;
        if (elem.childNodes.length == 0)
        {
            for(var i = 0; i < oldChildNodes.length; i++)
                this.appendChild(elem,oldChildNodes[i]); // Volvemos a insertar
        }
        else // Reemplazamos
        {
            var i1 = 0, len2 = elem.childNodes.length;
            for(var i2 = 0; i2 < len2; i2++)
            {
                if (i2 == indexNew) continue;
                var currNode = elem.childNodes.item(i2);
                this.replaceChild(elem,oldChildNodes[i1],currNode);
                i1++;
            }
        }
    }

    function updateReferences(elem,newElem,deep)
    {
        function updateListeners(map,elem,newElem)
        {
            map.doForAll(function (id,listener) {
                if (listener.currentTarget == elem)
                    listener.currentTarget = newElem;
                }
            );
        }

        function setProp(elem,newElem,name)
        {
            try{ if (typeof elem[name] != "undefined") newElem[name] = elem[name]; } catch(e) {}
        }

        setProp(elem,newElem,"className");
        setProp(elem,newElem,"selected");
        setProp(elem,newElem,"checked");
        setProp(elem,newElem,"value");

        updateListeners(this.domListeners,elem,newElem);
        updateListeners(this.timerListeners,elem,newElem);
        updateListeners(this.userListenersById,elem,newElem);
        var evt = this.win.event;
        if (evt && (evt.srcElement == elem)) evt.srcElement = newElem;
        if (elem.itsNatCacheId) // esta cacheado
        {
            newElem.itsNatCacheId = elem.itsNatCacheId;
            this.nodeCacheById.put(newElem.itsNatCacheId,newElem);
        }
        var listeners = elem.itsNatListeners;
        if (listeners)
        {
            newElem.itsNatListeners = listeners;
            for(var type in listeners)
            {
                var func = listeners[type];
                if (func) func.listeners.currentTarget = newElem; // el if es por si acaso
            }
        }

        if (deep) // Suponemos que ambos elementos tienen un arbol DOM simetrico
        {
          for(var i = 0; i < elem.childNodes.length; i++)
          {
              var child = elem.childNodes.item(i);
              var newChild = newElem.childNodes.item(i);
              this.updateReferences(child,newChild,true);
          }
        }
    }

    function setInnerHTMLInCopy(elem,value,bound)
    {
        var outerHTML = bound ? this.getOuterHTMLBound(elem) : this.getOuterHTMLUnBound(elem);
        var tagName = elem.tagName.toLowerCase(); // innerHTML lo muestra en minusculas
        var suffix = "</" + tagName + ">"; // si el elemento puede tener hijos tiene finalizador aunque este vacio
        var end = "";
        if (outerHTML.indexOf(suffix) != -1) end = suffix; // Tiene hijos
        var pos = outerHTML.indexOf(elem.innerHTML + end);
        var begin = outerHTML.substring(0,pos);
        return this.parseElement(elem.tagName,begin + value + end);
    }

    function isInnerHTMLSupported(elem)
    {
        return !(elem.tagName in this.innerHTMLNotValid);
    }

    function setInnerHTML(elem,value,indexNew)
    {
        // El nodo "elem" esta en el arbol
        // Si indexNew < 0 el arbol DOM no cambia (cambio en nodo texto).
        var oldDisp = elem.style.display;
        elem.style.display = "none";
        if (indexNew > 0) throw "Internal Error"; // 0 o -1
        var childNodes = this.extractChildNodes(elem,true);
        if (this.isInnerHTMLSupported(elem))
        {
            //Al hacer innerHTML inevitablemente se emite el "load" en los <img>, se ejecutan los <script> etc.
            elem.innerHTML = value;
            this.restoreChildNodes(childNodes,elem,indexNew);
        }
        else
        {
            var newElem = this.setInnerHTMLInCopy(elem,value,true);
            this.replaceChild(elem.parentNode,newElem,elem);
            this.updateReferences(elem,newElem,false);
            elem = newElem;
            this.restoreChildNodes(childNodes,elem,indexNew);
        }
        elem.style.display = oldDisp;
        return elem;
    }

    function getOuterHTMLUnBound(elem)
    {
        // Se supone que "elem" NO esta en el arbol (pues se quitaria aqui)
        // Como no esta en el documento se permite insertar <table> <tr> etc bajo <span>
        var span = this.doc.createElement("span");
        span.appendChild(elem);
        var html = span.innerHTML;
        span.removeChild(elem);
        return html;
    }

    function getOuterHTMLBound(elem)
    {
        // elem DEBE ESTAR en el arbol.
        var parentNode = elem.parentNode;
        var before = this.getParentInnerHTMLPos(elem,false);
        var end = this.getParentInnerHTMLPos(elem,true);
        var innerHTML = parentNode.innerHTML;
        return innerHTML.substring(before,end);
    }

    function getParentInnerHTMLPos(childNode,end)
    {
        // Este metodo modifica ligeramente el innerHTML del padre (por tanto acceder al mismo *despues* de llamar a este metodo)
        var idOld = childNode.id; // siempre es string
        if (idOld == "") childNode.id = ""; // Impone el atributo id="" para unificar los casos de "no definido" e id=""
        childNode.id = "itsNatPosCalc"; // Se reflejara como atributo
        // <tagName ... id="itsNatPosCalc"...">innerHTML[</tagName>] Al normalizar los atributos estaran entre comillas y al menos hay uno (el id) y pegado al ">"
        var parentNode = childNode.parentNode; // childNode debe estar en el arbol
        var parentInnerHTML = parentNode.innerHTML;
        var tagName = childNode.tagName.toLowerCase();
        var posId = parentInnerHTML.indexOf("id=\"itsNatPosCalc\"");
        var posRes;
        if (end)
        {
            var pattern; var childInnerHTML = childNode.innerHTML;
            if ((childInnerHTML.length > 0) || (parentInnerHTML.indexOf("</" + tagName + ">") != -1)) // Si esta el finalizador es que es un nodo con cierre (aunque no tenga hijos)
                 pattern = "\">" + childInnerHTML + "</" + tagName + ">";
            else pattern = "\">"; // No hay cierre y no hay hijos
            posRes = parentInnerHTML.indexOf(pattern,posId);
            if (posRes == -1) throw "Internal Error";
            var correction = "itsNatPosCalc".length - idOld.length;
            posRes += pattern.length - correction;
        }
        else
        {
            var pattern = "<" + tagName + " ";
            var posAuxPrev = -1,posAuxCurr = -1;
            do
            {
                posAuxPrev = posAuxCurr;
                posAuxCurr = parentInnerHTML.indexOf(pattern,posAuxCurr + 1);
            }
            while((posAuxCurr < posId)&&(posAuxCurr != -1));
            posRes = posAuxPrev;
        }
        childNode.id = idOld; // Si id no estaba definido quedara el atributo id="" , es un mal menor
        return posRes;
    }

    function getParentTable(parentNode)
    {
        // Ver notas en ClientMutationEventListenerHTMLMSIEPocketImpl
        var tagName = parentNode.tagName;
        if ((tagName != "TABLE") && (tagName != "TR") &&
            (tagName != "THEAD") && (tagName != "TBODY") && (tagName != "TFOOT"))
           return null;
        var table = parentNode;
        while(table.tagName != "TABLE") { table = table.parentNode; }
        return table;
    }

    function hideTable(table)
    {
        var dispOld = table.style.display;
        table.style.display = "none"; // Cualquier valor soportado menos "table" (el actual)
        return dispOld;
    }

    function showTable(table,dispOld)
    {
        table.style.display = dispOld;
    }

    function appendChild(parentNode,newChild)
    {
        if (parentNode.tagName == "SELECT")
            parentNode.add(newChild);
        else
        {
            var dispOldTable;
            var table = this.getParentTable(parentNode);
            if (table) dispOldTable = this.hideTable(table);
            parentNode.appendChild(newChild);
            if (table) this.showTable(table,dispOldTable);
        }
    }

    function insertBefore(parentNode,newChild,childRef)
    {
        if (childRef == null) this.appendChild(parentNode,newChild);

        if (parentNode.tagName == "SELECT")
        {
            // No usamos parentNode.options porque inexplicablemente se corrompe y childNodes se "recrea" cuando cambia el select
            var index = childRef.index;
            parentNode.add(newChild,index); // NO DOCUMENTADA y NO W3C
        }
        else
        {
            var dispOldTable; var table = this.getParentTable(parentNode);
            if (table) dispOldTable = this.hideTable(table);
            parentNode.insertBefore(newChild,childRef);
            if (table) this.showTable(table,dispOldTable);
        }
    }

    function removeChild(child)
    {
        // Nota: los nodos hijo perdidos tras un innerHTML solo pueden volver a insertarse
        // si fueron antes eliminados con Node.removeChild (IE bug)
        if (child == null) return;
        var parentNode = child.parentNode;
        if (parentNode.tagName == "SELECT")
        {
            var index = child.index;
            parentNode.remove(index);
        }
        else
        {
            var dispOldTable; var table = this.getParentTable(parentNode);
            if (table) dispOldTable = this.hideTable(table);
            var dispOld = child.style.display;
            child.style.display = "none"; // Esto es NECESARIO si el nodo contiene controles form (input etc), la alternativa es eliminar estos nodos antes. Recordar que introduce el atributo style (si no estaba)
            parentNode.removeChild(child);
            child.style.display = dispOld;
            if (table) this.showTable(table,dispOldTable);
        }
    }

    function replaceChild(parentNode,newChild,oldChild)
    {
        if (parentNode.tagName == "SELECT")
        {
            this.insertBefore(parentNode,newChild,oldChild);
            this.removeChild(oldChild);
        }
        else
        {
            var dispOldTable; var table = this.getParentTable(parentNode);
            if (table) dispOldTable = this.hideTable(table);
            var dispOld = oldChild.style.display;
            oldChild.style.display = "none"; // Ver notas en removeChild
            parentNode.replaceChild(newChild,oldChild);
            oldChild.style.display = dispOld;
            if (table) this.showTable(table,dispOldTable);
        }
    }

    function insertAfter(idObj,newChild,childRelIdObj)
    {
        var parentNode = this.getNode(idObj);
        var childRef = this.getNode2(parentNode,childRelIdObj);
        if (childRef != null)
        {
            this.replaceChild(parentNode,newChild,childRef);
            this.insertBefore(parentNode,childRef,newChild);
            return newChild;
        }
        else // insertar al ppio, se supone que puede haber un nodo de texto al ppio (si es comentario da igual)
        {
            var newMarkup = this.getOuterHTMLUnBound(newChild);
            parentNode = this.setInnerHTML(parentNode,newMarkup + parentNode.innerHTML,0);
            newChild = parentNode.childNodes.item(0);
            return newChild;
        }
    }

    function appendFilteredNode(parentNode,text)
    {
        if (this.getParentNode(parentNode) == null) // Caso de insercion de un <script> con contenido
            return this.setInnerHTMLInCopy(parentNode,text,false);
        else
            return this.setInnerHTML(parentNode,parentNode.innerHTML + text,-1);
    }

    function appendFilteredNode2(idObj,text)
    {
        var parentNode = this.getNode(idObj);
        return this.appendFilteredNode(parentNode,text);
    }

    function insertBeforeFilteredNode(idObj,childRefIdObj,text)
    {
        var parentNode = this.getNode(idObj);
        var childRef = this.getNode2(parentNode,childRefIdObj); // childRef NO deberia ser null (usar appendFilteredNode)
        var pos = this.getParentInnerHTMLPos(childRef,false);
        var code = parentNode.innerHTML;
        code = code.substring(0,pos) + text + code.substring(pos);
        return this.setInnerHTML(parentNode,code,-1);
    }

    function setFilteredNode(parentIdObj,prevSibIdObj,nextSibIdObj,text)
    {
        var parentNode = this.getNode(parentIdObj);

        var pos1;
        var prevChild = this.getNode2(parentNode,prevSibIdObj); // puede ser null
        if (prevChild) pos1 = this.getParentInnerHTMLPos(prevChild,true);
        else pos1 = 0;

        var pos2;
        var nextChild = this.getNode2(parentNode,nextSibIdObj); // puede ser null
        if (nextChild) pos2 = this.getParentInnerHTMLPos(nextChild,false);
        else pos2 = -1;

        var code = parentNode.innerHTML;
        if (pos2 == -1) pos2 = code.length;
        code = code.substring(0,pos1) + text + code.substring(pos2);
        return this.setInnerHTML(parentNode,code,-1);
    }

    function parseElement(tagName,htmlCode)
    {
        var auxElem = this.doc.createElement("span");
        this.doc.body.appendChild(auxElem);
        auxElem.style.display = "none";
        var levels = 1;
        levels += this.appendNeededAuxTree(auxElem,tagName,htmlCode);
        var elem = auxElem;
        for(var i = 1; i <= levels; i++)
            elem = elem.childNodes.item(0);
        //this.removeChild(elem); // Si no se hace el elemento no es correcto para insertar (falla aleatoriamente)
        this.removeChild(auxElem);
        return elem;
    }

    function addAttributes(elem,attribs)
    {
        // Se supone "elem" NO insertado en el arbol, sin atributos y sin hijos
        var attrCode = "";
        for(var name in attribs)
        {
            var value = attribs[name];
            attrCode += " " + name + "=\"" + value + "\"";
        }
        var html = this.getOuterHTMLUnBound(elem);
        var pattern = "<" + elem.tagName;
        var newHTML = html.substring(0,pattern.length) + attrCode + html.substring(pattern.length);
        elem = this.parseElement(elem.tagName,newHTML);
        return elem;
    }

    function procAttrAsHandler(elem,name,value)
    {
        var type = name.substring(2); // onXXX , quitamos el "on"
        var func = this.getEventListenerRegistry(elem,type,true);
        func.listeners.setInlineHandler(value);  // Aunque no este soportado por IE valdria para server-sent events
        return elem;
    }

    function setAttribute(elem,name,value)
    {
        return this.manageAttribute(elem,name,value,false);
    }

    function removeAttribute(elem,name)
    {
        return this.manageAttribute(elem,name,"",true);
    }

    function manageAttribute(elem,name,value,remove)
    {
         // Se supone elem insertado en el documento
        if ((elem == this.doc.documentElement) || (elem == this.doc.body) ||
                 (elem.tagName == "HEAD") ) // Esto es lo maximo que podemos hacer
        {
            if (remove) value = "";
            elem[name] = value; // En remove no hacemos "delete" porque no produce efecto visual
            return elem;
        }
        else
        {
            name = name.toLowerCase(); // el innerHTML por ejemplo muestra en minusculas los attrib siempre
            var tagName = elem.tagName.toLowerCase(); // En innerHTML estan en minusculas
            var oldValue = this.getAttribute(elem,name);
            var parentNode = this.getParentNode(elem);
            var innerHTML = elem.innerHTML; // Antes de manipular
            var childNodes = this.extractChildNodes(elem,true); // para acelerar y poder reinsertar los originales, quedaran textos y comentarios
            var outerHTML = this.getOuterHTMLBound(elem);
            // Quitamos el contenido y el tag finalizador
            var posEndTag = outerHTML.lastIndexOf("</" + tagName + ">");
            var hasEndTag = (posEndTag != -1);
            if (hasEndTag) outerHTML = outerHTML.replace(elem.innerHTML + "</" + tagName + ">","");
            else if (elem.innerHTML.length > 0)
            {
                var pos = outerHTML.lastIndexOf(elem.innerHTML);
                outerHTML = outerHTML.substring(0,pos);
            }
            if (oldValue == "")
            {
                // No hay garantia de que no exista, buscamos
                var attr = name + "=\"\"";
                var pos = outerHTML.indexOf(attr);
                if (pos != -1)
                {
                    // Nos aseguramos que no es de un elemento hijo
                    var posEndElem = outerHTML.indexOf('>');
                    if (pos > posEndElem) oldValue = null; // Era en un elem. hijo
                }
                else oldValue = null; // No esta
            }
            var left,right;
            if (oldValue != null) // existe =>
            {
                var attr = name + "=\"" + oldValue + "\"";
                var pos = outerHTML.indexOf(attr);
                left = outerHTML.substring(0,pos);
                right = outerHTML.substring(pos + attr.length);
            }
            else // no existe
            {
                var prefix = "<" + elem.tagName;
                left = prefix;
                left += " "; // para separar por si no hay atributos
                right = outerHTML.substring(prefix.length);
            }
            var attr = "";
            if (!remove) attr = name + "=\"" + value + "\""; // Nuevo valor
            var isInnerHTMLSup = this.isInnerHTMLSupported(elem);
            var htmlCode = left + attr + right;
            if (!isInnerHTMLSup) htmlCode += innerHTML;
            if (hasEndTag) htmlCode += "</" + tagName + ">";
            var newElem = this.parseElement(elem.tagName,htmlCode);

            this.replaceChild(parentNode,newElem,elem);
            if (isInnerHTMLSup) newElem.innerHTML = innerHTML;
            this.updateReferences(elem,newElem,false);
            this.restoreChildNodes(childNodes,newElem,-1); // Restauramos los hijos originales
            return newElem;
        }
    }

    function getAttribute(elem,name)
    {
        if (name == "style") return elem.style.cssText;
        // En teoria hemos de devolver null cuando no exista (distinguiendo de "")
        // Si getAttribute funciona bien no hay garantia de no existencia si value == ""
        try { return elem.getAttribute(name); } catch(e) { } // Si no existe da error
        return null;
    }

    function getEventListenerInline(elem,type)
    {
        var inlineHandler = this.getAttribute(elem,"on" + type); // En IE Pocket el atributo es una string siempre
        if (inlineHandler == null) return null;
        var pattern = "return document.getItsNatDoc().dispatchEvent(this,'" + type + "',null);";
        if (inlineHandler.length < pattern.length) return null;
        if ((inlineHandler.indexOf(pattern) == 0) && (inlineHandler.length > pattern.length))
            inlineHandler = inlineHandler.substring(pattern.length); // Hay un handler del usuario
        else
            inlineHandler = null; // No hay handler del usuario o bien no hay patron, en este caso es que el IE no hace nada con dicho handler (no soportado por IE)
        return inlineHandler;
    }

    function getEventListenerRegistry(node,type,create)
    {
        var func;
        if (node == this.win) node = this.doc.body;
        if (node.itsNatListeners)
        {
            func = node.itsNatListeners[type];
            if (func) return func;
        }

        if (!create)
        {
            var inlineHandler = this.getEventListenerInline(node,type);
            if (inlineHandler == null) return null; // No hay handler
            // Hay un codigo de usuario en el onXXX, estamos obligados a crear el registro, da igual "create", pues hay que ejecutarlo en el dispatch manual
        }

        return this.createEventListenerRegistry(node,type);
    }

    function cleanEventListenerRegistry(type,node,func)
    {
        // No destruimos el contenedor pues los inline handler para cada evento lo crearian de nuevo
        // aunque no haya listeners al llamar a getEventListenerRegistry
    }

    function dispatchEvent(node,type,evt)
    {
        // node es el target. En el caso de atributo handler en <body> el "this" puede ser el window
        if (evt == null) evt = new FakeMSIEEvent(type,node);
        else
        {
            evt.type = type;
            evt.srcElement = node;
        }

        var currFocus = this.currElemWithFocus;
        if (currFocus && (currFocus != node)) // afortunadamente los eventos timer no pasan por aqui (no deberian hacer blur)
        {
            this.currElemWithFocus = null;
            var blurEvt = new FakeMSIEEvent("blur",currFocus);
            this.dispatchEvent(currFocus,"blur",blurEvt);
        }

        if (evt.itsnat_stopPropagation) return evt.returnValue;
        var parentList = null;
        if (node != this.win)
            parentList = this.dispatchEventCapture(evt);

        var func = this.getEventListenerRegistry(node,type,false);
        if (func) func.listeners.dispatchEventLocal(evt,false);

        if (evt.itsnat_stopPropagation) return evt.returnValue;
        if (node != this.win)
        {
            this.dispatchEventBubble(evt,parentList);
            if (evt.itsnat_stopPropagation) return evt.returnValue;
        }

        var currFocus = this.currElemWithFocus;
        if ((currFocus == node)&&(type == "change"))
        {
            this.currElemWithFocus = null;
            var blurEvt = new FakeMSIEEvent("blur",currFocus);
            this.dispatchEvent(currFocus,"blur",blurEvt);
        }

        var tagName = node.tagName;
        if ( (type == "focus") && (((tagName == "INPUT")&&( (node.type == "text")||(node.type == "password")||(node.type == "file") ))||(tagName == "TEXTAREA")) )
            this.currElemWithFocus = node;

        return evt.returnValue; // Pues es el retorno del handler inline realmente
    }

    function dispatchEventBubble(evt,parentList)
    {
        if (this.evtNotBubble[evt.type]) return;
        this.dispatchEventTree(false,evt,parentList);
    }
}

} // pkg_itsnat_msie_pocket

function itsnat_init_msie_pocket(win)
{
    var itsnat = itsnat_init_msie_old(win);
    pkg_itsnat_msie_pocket(itsnat);
    return itsnat;
}


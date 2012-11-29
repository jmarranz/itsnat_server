/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

window.ItsNatFixOtherNSInHTML = function ()
{
    // El localName en HTML incluye tambien, erroneamente el prefijo y el ":" por lo que tenemos que extraerlo manualmente.
    // Ademas en HTML el nombre del elemento esta suele estar erroneamente en mayúsculas o minusculas si es atributo

    this.getPrefix = function (name)
    {
        var pos = name.indexOf(":");
        if (pos == -1) return null;
        else return name.substring(0,pos);
    };

    this.getLocalName = function (name)
    {
        var pos = name.indexOf(":");
        if (pos == -1) return name;
        else return name.substring(pos + 1);
    };

    this.getValidName = function (node,clean)
    {
        var name = node.nodeName.toLowerCase();
        var prefix = this.getPrefix(name);
        var localName = this.getLocalName(name);
        var attrName = (prefix != null) ? prefix + "_" + localName : localName;
        var elem = (node.nodeType == 1) ? node : node.ownerElement; // 1 = Node.ELEMENT_NODE
        var validName = elem.getAttribute("itsnatns_" + attrName);
        if (!validName) return name;
        if (clean) elem.removeAttribute("itsnatns_" + attrName); // Eliminamos el auxiliar
        return validName;
    };

    this.getDefNS = function (elem,defNS)
    {
        if (elem.hasAttribute('xmlns')) return elem.getAttribute('xmlns');
        else return defNS;
    };

    this.getPrefixes = function (elem,prefixes)
    {
        var attribs = elem.attributes;
        var newPrefixes = null;
        for(var i = 0; i < attribs.length; i++)
        {
            var attr = attribs[i];
            var attrName = this.getValidName(attr,false);
            var prefix = this.getPrefix(attrName);
            if (prefix != "xmlns") continue;
            if (newPrefixes == null)
            {
                newPrefixes = new Object();
                for(var name in prefixes) newPrefixes[name] = prefixes[name];
            }
            var localName = this.getLocalName(attrName);
            newPrefixes[localName] = attr.value; // No es necesario usar itsNatDoc.getAttribute(node,attrName) los navegadores W3C no suelen tener problemas con el attr.value
        }
        if (newPrefixes == null) newPrefixes = prefixes;
        return newPrefixes;
    };

    this.getNamespace = function (nodeName,defNS,prefixes)
    {
        var prefix = this.getPrefix(nodeName);
        var namespace;
        if (prefix != null) namespace = (prefix == "xmlns") ? "http://www.w3.org/2000/xmlns/" : prefixes[prefix];
        else namespace = (nodeName == "xmlns") ? "http://www.w3.org/2000/xmlns/" : defNS;
        if (!namespace) return null; // Incluye el caso "undefined" cuando el prefijo no esta en prefixes (por ejemplo itsnat:nocache=... pues el namespace itsnat no lo procesamos
        if (namespace == "http://www.w3.org/1999/xhtml") return null; // nada que hacer con el
        return namespace;
    };

    this.fixAttr = function (attr,elem,elemTarget,prefixes)
    {
        if (attr.name.indexOf("itsnatns_") == 0) return;
        var cloning = (elem != elemTarget);
        var attrName = this.getValidName(attr,!cloning);
        var attrNS = this.getNamespace(attrName,null,prefixes); // En el caso de no tener prefijo el namespace NO es el de por defecto, es nulo (salvo en el caso de "xmlns")
        if (!cloning && (attr.namespaceURI == attrNS) && (attr.name == attrName)) return;
        if (!cloning) elem.removeAttributeNode(attr);
        if (attrNS == null) elemTarget.setAttribute(attrName,attr.value); // El no utilizar setAttributeNS(null,...) es porque en FireFox curiosamente no admite: setAttributeNS(null,"xmlns:itsnat",...)  lo cual ocurre al ignorar los namespaces itsnat
        else elemTarget.setAttributeNS(attrNS,attrName,attr.value);
    };

    this.fixAttributes = function (elem,prefixes)
    {
        var attribs = []; // Necesaria porque se quitan y ponen atributos
        for(var i = 0; i < elem.attributes.length; i++) attribs.push(elem.attributes[i]);
        for(var i = 0; i < attribs.length; i++)
        {
            var attr = attribs[i];
            this.fixAttr(attr,elem,elem,prefixes);
        }
    };

    this.processElement = function (elem,defNS,prefixes)
    {
        var ignoreNS = elem.getAttribute("itsnat:ignorens"); // SOLO PERMITIMOS el prefijo "itsnat" pues el namespace itsnat es ignorado en HTML
        if ("true" == ignoreNS) return;
        defNS = this.getDefNS(elem,defNS);
        prefixes = this.getPrefixes(elem,prefixes);
        var tagName = this.getValidName(elem,true);
        var elemNS = this.getNamespace(tagName,defNS,prefixes);
        if (elemNS)
        {
            var elemClone = document.createElementNS(elemNS,tagName);
            for(var i = 0; i < elem.attributes.length; i++)
            {
                var attr = elem.attributes[i];
                this.fixAttr(attr,elem,elemClone,prefixes);
            }
            while(elem.childNodes.length > 0) elemClone.appendChild(elem.childNodes[0]);
            elem.parentNode.replaceChild(elemClone,elem);
            elem = elemClone;
        }
        else this.fixAttributes(elem,prefixes);
        for(var i = 0; i < elem.childNodes.length; i++)
        {
            var node = elem.childNodes[i];
            if (node.nodeType != 1) continue; // 1 == Node.ELEMENT_NODE
            this.processElement(node,defNS,prefixes);
        }
    };

    this.fixTreeOtherNSInHTML = function (tagNameList)
    {
        var elemList = []; // Necesaria pues al procesar elem eliminamos nodos e influyendo en getElementsByTagName
        for(var i = 0; i < tagNameList.length; i++)
        {
            var elemsByTag = document.getElementsByTagName(tagNameList[i]);
            for(var j = 0; j < elemsByTag.length; j++)
                elemList.push(elemsByTag[j]);
        }

        for(var i = 0; i < elemList.length; i++)
        {
            var elem = elemList[i];
            if (!elem.hasAttribute("itsnatnsroot")) continue;
            elem.removeAttribute("itsnatnsroot");
            this.processElement(elem,null,new Object());
        }

        delete window.ItsNatFixOtherNSInHTML;
    };
}


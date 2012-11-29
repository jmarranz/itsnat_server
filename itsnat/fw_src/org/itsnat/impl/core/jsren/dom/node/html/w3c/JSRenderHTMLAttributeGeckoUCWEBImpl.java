/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.core.jsren.dom.node.html.w3c;

import org.itsnat.impl.core.browser.BrowserGeckoUCWEB;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.DelegateClientDocumentStfulUCWEBImpl;
import org.itsnat.impl.core.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.path.NodeJSRefImpl;
import org.itsnat.impl.core.path.NodeLocationImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLAttributeGeckoUCWEBImpl extends JSRenderHTMLAttributeW3CImpl
{
    public final static JSRenderHTMLAttributeGeckoUCWEBImpl SINGLETON = new JSRenderHTMLAttributeGeckoUCWEBImpl();

    /** Creates a new instance of JSRenderHTMLAttributeW3CDefaultImpl */
    public JSRenderHTMLAttributeGeckoUCWEBImpl()
    {
    }

    public boolean isRenderAttributeAlongsideProperty(String attrName,Element elem)
    {
        // UCWEB es un Gecko y distingue entre propiedades y atributos
        return true;
    }

    public static String getFixOnlyOnClickExecProcessMethodName()
    {
        return "ucwebFixOnlyOnClickExecProcess";
    }

    public static JSRenderNodeRegistryByClientImpl getFixOnlyOnClickExecRegistry(ClientDocumentStfulImpl clientDoc)
    {
        DelegateClientDocumentStfulUCWEBImpl clientDeleg = (DelegateClientDocumentStfulUCWEBImpl)clientDoc.getDelegateClientDocumentStful();
        return clientDeleg.getFixOnClickNodeRegistry();
    }

    public static String getFixOnlyOnClickExecMethodCall(String id)
    {
        return "document.getItsNatDoc()." + getFixOnlyOnClickExecProcessMethodName() + "('"  + id + "');";
    }

    public static JSRenderNodeRegistryByClientImpl getHTMLAnchorHRefRegistry(ClientDocumentStfulImpl clientDoc)
    {
        DelegateClientDocumentStfulUCWEBImpl clientDeleg = (DelegateClientDocumentStfulUCWEBImpl)clientDoc.getDelegateClientDocumentStful();
        return clientDeleg.getFixAnchorNodeRegistry();
    }

    public static String getHTMLAnchorHRefCallMethodName()
    {
        return "ucwebFixHTMLAnchorHRefProcess";
    }

    public static String getHTMLAnchorHRefMethodCall(String id)
    {
        return "document.getItsNatDoc()." + getHTMLAnchorHRefCallMethodName() + "('"  + id + "');";
    }

    private static String extractValue(String jsValue)
    {
        // Formato esperado: "valor" o null
        if (jsValue == null) return "";
        return jsValue.substring(1,jsValue.length()-1);
    }

    private boolean mustFixAnchorHRef(Element elem,String attrName,boolean newElem,ClientDocumentStfulImpl clientDoc)
    {
        if (!newElem && isAnchorHRef(elem,attrName))
        {
            // En el caso de newElem = true NO tratamos el proceso del atributo,
            // (al cliente se enviará el href original, no modificado)
            // pues la adición del formato especial se hace luego en otro sitio (en carga
            // y en el mutation event tras inserción/renderizado del elemento) al detectar
            // que un <a> ha sido insertado.
            // Esto es debido a que existe la posibilidad de que el anchor
            // sea renderizado e insertado via innerHTML por lo que no pasaría
            // por aquí, por el renderizado de cada atributo, por eso delegamos la adaptación del href
            // al post proceso de la inserción de los elementos.
            BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();
            return browser.isHTMLAnchorOnClickNotExec();
        }
        return false;
    }

    private boolean isAnchorHRef(Element elem,String attrName)
    {
        return (elem instanceof HTMLAnchorElement &&
                 attrName.toLowerCase().equals("href"));
    }

    public static String transformHTMLAnchorHRef(String href,String id)
    {
        String processCall = getHTMLAnchorHRefMethodCall(id);

        String hrefLow = href.toLowerCase();
        if (hrefLow.equals("")) // Por ejemplo si no existe el atributo
        {
            href = "javascript:" + processCall;
        }
        else if (hrefLow.startsWith("javascript:"))
        {
            href = "javascript:" + processCall +
                    href.substring("javascript:".length());
        }
        else // Es un URL
        {
            href = "javascript:" + processCall + "window.location = '" + href + "';";
        }
        return href;
    }

    private String transformHTMLAnchorHRef(String jsValue,Element elem,ClientDocumentStfulImpl clientDoc)
    {
        String href = extractValue(jsValue);

        JSRenderNodeRegistryByClientImpl registry = getHTMLAnchorHRefRegistry(clientDoc);
        String id = registry.getIdByNode(elem);

        href = transformHTMLAnchorHRef(href,id);

        return "\"" + href + "\"";
    }

    private boolean mustFixOnlyOnClickExec(Element elem,String attrName,boolean newElem,ClientDocumentStfulImpl clientDoc)
    {
        if (!newElem && attrName.equals("onclick"))
        {
            // En el caso de newElem = true NO tratamos el proceso del atributo,
            // (al cliente se enviará el href original, no modificado)
            // pues la adición del formato especial se hace luego en otro sitio (en carga
            // y en el mutation event tras inserción/renderizado del elemento) al detectar
            // que el elemento ha sido insertado.

            // Esto es debido a que existe la posibilidad de que el atributo
            // sea renderizado e insertado via innerHTML por lo que no pasaría
            // por aquí, por el renderizado de cada atributo, por eso delegamos la adaptación del atributo
            // al post proceso de la inserción de los elementos.
            BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();
            return browser.isOnlyOnClickExecuted(elem);
        }
        return false;
    }

    private String transformOnClick(Element elem,String elemVarName,ClientDocumentStfulImpl clientDoc)
    {
        JSRenderNodeRegistryByClientImpl registry = getFixOnlyOnClickExecRegistry(clientDoc);
        String id = registry.getIdByNode(elem);
        if (id == null)
        {
            return JSRenderHTMLElementGeckoUCWEBImpl.fixOnlyOnClickExecutedElementInsert(elem,elemVarName,clientDoc);
        }
        else
        {
            // Actualizamos la propiedad pues el atributo tiene que tener el formato de llamada para lanzar el evento manualmente
            String onclickHandler = elem.getAttribute("onclick");
            return elemVarName + ".onclick = function (event) { " + onclickHandler + " };\n";
        }
    }

    public String setAttributeOnlyCode(Attr attr,String attrName,String jsValue,NodeJSRefImpl nodeRef,boolean newElem)
    {
        ClientDocumentStfulImpl clientDoc = nodeRef.getClientDocumentStful();
        Element elem = attr.getOwnerElement();
        if (mustFixAnchorHRef(elem,attrName,newElem,clientDoc))
        {
            jsValue = transformHTMLAnchorHRef(jsValue,elem,clientDoc);
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem);
        }
        else if (mustFixOnlyOnClickExec(elem,attrName,newElem,clientDoc))
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                StringBuffer code = new StringBuffer();
                code.append("var elem = " + JSRenderNodeImpl.getNodeReference(nodeLoc,true) + ";\n");
                code.append( transformOnClick(elem,"elem",clientDoc) );
                return code.toString();
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return transformOnClick(elem,elemVarName,clientDoc);
            }
        }
        else
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,newElem);
    }

    protected String removeAttributeOnlyCode(Attr attr,String attrName,Element elem,NodeJSRefImpl nodeRef)
    {
        ClientDocumentStfulImpl clientDoc = nodeRef.getClientDocumentStful();
        if (mustFixAnchorHRef(elem,attrName,false,clientDoc)) // Es raro este caso pero posible
        {
            String jsValue = transformHTMLAnchorHRef(null,elem,clientDoc);
            return super.setAttributeOnlyCode(attr,attrName,jsValue,nodeRef,false);
        }
        else if (mustFixOnlyOnClickExec(elem,attrName,false,clientDoc))
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                StringBuffer code = new StringBuffer();
                code.append("var elem = " + JSRenderNodeImpl.getNodeReference(nodeLoc,true) + ";\n");
                code.append( transformOnClick(elem,"elem",clientDoc) );
                return code.toString();
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return transformOnClick(elem,elemVarName,clientDoc);
            }
        }
        else
            return super.removeAttributeOnlyCode(attr,attrName,elem,nodeRef);
    }
}

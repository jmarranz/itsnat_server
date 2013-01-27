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

package org.itsnat.impl.core.jsren.dom.node.html.msie;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserMSIEOld;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.PropertyImpl;
import org.itsnat.impl.core.jsren.dom.node.html.JSRenderHTMLPropertyImpl;
import org.itsnat.impl.core.path.NodeJSRefImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class JSRenderHTMLPropertyMSIEOldImpl extends JSRenderHTMLPropertyImpl
{
    public static final JSRenderHTMLPropertyMSIEOldImpl SINGLETON = new JSRenderHTMLPropertyMSIEOldImpl();
    
    // No es necesario sincronizar esta colección va a ser sólo leída
    public final Map propertiesByAttrNameMSIE = new HashMap();  // Propiedades/Atributos comunes a todos los elementos

    /** Creates a new instance of JSRenderHTMLPropertyMSIEOldImpl */
    public JSRenderHTMLPropertyMSIEOldImpl()
    {
        // Atributos problemáticos del MSIE que necesitan definirse
        // como propiedad en vez de como atributo (en el caso de "class" definimos
        // también el atributo aunque no sirve para nada).

        // El caso de "class" y "name" es debido a un problema de MSIE
        // A propósito de "name" se habla de "class", el caso de "class"
        // se puede arreglar con className pero con "name" es más complicado
        // y se pasa name en el createElement
        // http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/properties/name_2.asp
        // http://www.thunderguy.com/semicolon/2005/05/23/setting-the-name-attribute-in-internet-explorer/

        addGlobalProperty("className","class",PropertyImpl.STRING);



        // Sobre handlers de eventos: http://www.thescripts.com/forum/thread90407.html
        // W3C http://www.w3.org/TR/html4/interact/scripts.html#h-18.2.3
        // Podrían definirse también via setAttribute,
        // ej. elem.setAttribute("onclick",funcHandler);
        // En donde funcHandler es una función, lo que no vale es que sea una cadena.

        String[] eventHandlers = new String[]
        {
            "onload","onunload","onclick","ondblclick","onmousedown",
            "onmouseup","onmouseover","onmousemove","onmouseout","onfocus",
            "onblur","onkeypress","onkeydown","onkeyup","onsubmit",
            "onreset","onselect","onchange",

            /* Handlers de eventos no W3C pero válidos en MSIE y FireFox
            http://www.irt.org/articles/js058/
            http://www.quirksmode.org/js/events_compinfo.html  */
            "onabort","onerror","ondragdrop","onmove","onresize",
            "oncontextmenu","onscroll",

            /* Handlers de eventos MSIE sólo:
             http://www.quirksmode.org/js/events_compinfo.html  */
            "onmouseenter","onmouseleave"
        };
        // Los handlers desconocidos onXXX se detectan también
        // http://msdn.microsoft.com/en-us/library/ms533051%28VS.85%29.aspx

        for(int i = 0; i < eventHandlers.length; i++)
            addGlobalFunction(eventHandlers[i]);
    }

    public static JSRenderHTMLPropertyMSIEOldImpl getJSRenderHTMLPropertyMSIEOld(BrowserMSIEOld browser)
    {
        return JSRenderHTMLPropertyMSIEOldImpl.SINGLETON;
    }

    protected void addGlobalFunction(String propName)
    {
        addGlobalProperty(propName,propName,PropertyImpl.FUNCTION,null);
    }

    protected void addGlobalProperty(String propName,int type)
    {
        addGlobalProperty(propName,propName,type,null);
    }
    
    protected void addGlobalProperty(String propName,String attrName,int type)
    {
        addGlobalProperty(propName,attrName,type,null);
    }

    protected void addGlobalProperty(String propName,String attrName,int type,String nullValue)
    {
        PropertyImpl property = new PropertyImpl(this,propName,attrName,type,nullValue);
        propertiesByAttrNameMSIE.put(attrName.toLowerCase(),property);
    }

    protected PropertyImpl getGlobalProperty(Element elem,String attrName)
    {
        // Lo de poner en minúsculas es para que el nombre del atributo (normalmente en minúsculas)
        // y la propiedad (podría tener alguna mayúscula) coincidan.
        String attrNameLower = attrName.toLowerCase();
        PropertyImpl prop = (PropertyImpl)propertiesByAttrNameMSIE.get(attrNameLower);
        if (prop != null) return prop;
        
        // Es posible que sea un inline handler nuevo del IE que desconocemos 
        // de esta manera no nos tenemos que preocupar de soportar de handlers no listados (que son muchos)
        if (attrNameLower.startsWith("on"))
            return new PropertyImpl(this,attrNameLower,attrNameLower,PropertyImpl.FUNCTION,null);

        return null;
    }
    
    public String attrValueJSToPropValueJS(PropertyImpl prop,boolean setValue,String attrValueJS,String value)
    {
        if (setValue && (prop.getType() == PropertyImpl.FUNCTION))
        {
            // Caso de handlers en MSIE (onclick etc)
            // ignoramos en este caso attrValueJS
            String trimed = value.trim();
            if (trimed.startsWith("function ")) // Ya tiene el formato deseado
                return value;
            else
                return "function (event) {" +  value + "}";
        }
        else
            return super.attrValueJSToPropValueJS(prop,setValue,attrValueJS,value);
    }

    protected String renderProperty(PropertyImpl prop,Element elem,String elemVarName,String attrValueJS,String value,boolean setValue,ClientDocumentStfulImpl clientDoc)
    {
        String propName = prop.getPropertyName();

        if ((elem instanceof HTMLOptionElement) &&
            propName.equals("selected") ) // (type == BOOLEAN)
        {
            Browser browser = clientDoc.getBrowser();
            if ((browser instanceof BrowserMSIEOld)&&
                ((BrowserMSIEOld)browser).getVersion() < 8 )
            {
                // Es un error estúpido de MSIE, detectado en el "selected" de los OPTION de un SELECT en el script de carga del documento
                // la causa es que la propiedad es posible que esté bloqueada por otro hilo porque el MSIE hace cosas en background
                // http://ianso.blogspot.com/2005/10/another-stupid-ie-bug-selectedindex-on.html
                // Como dice el link, poniendo un alert antes de hacer la operación funciona, pero hay alternativas
                // mejores como hacer: elem.setAttribute('selected',true);
                // Otra alternativa que se me ocurrio a mi es usar un while con un try catch hasta que se defina
                //     if (elem.selected != value)  // imprescindible este if
                //         while(true) try { elem.selected = value; break; } catch(e) {}
                // funciona pero es más aparatosa.
                // Es posible que para testear se necesite poner la carga del documento con un select
                // en fastLoad = false para que el select se rellene via JavaScript
                // La propiedad readyState no lo soluciona (readyState puede estar a "complete" y seguir bloqueada la propiedad).
                // En IE 8 ya se distingue entre propiedades y atributos como en el W3C DOM por lo que esto no vale.
                String propValueJS = attrValueJSToPropValueJS(prop,setValue,attrValueJS,value);

                JSRenderHTMLAttributeMSIEOldImpl renderer = JSRenderHTMLAttributeMSIEOldImpl.getJSRenderHTMLAttributeMSIEOld((BrowserMSIEOld)browser);
                return renderer.setAttributeOnlyCode(null,"selected",propValueJS,new NodeJSRefImpl(elemVarName,clientDoc),false); // newElem = false porque por una parte da igual en este caso
            }
            else
            {
                // IE Mobile no parece tener los problemas del IE desktop y usar setAttribute es costosímo en IE Mobile
                return super.renderProperty(prop,elem,elemVarName,attrValueJS,value,setValue,clientDoc);
            }
        }
        else
            return super.renderProperty(prop,elem,elemVarName,attrValueJS,value,setValue,clientDoc);
    }
}

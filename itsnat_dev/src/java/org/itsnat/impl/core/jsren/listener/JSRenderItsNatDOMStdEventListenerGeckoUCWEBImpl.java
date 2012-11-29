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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.core.NameValue;
import org.itsnat.impl.core.browser.BrowserGeckoUCWEB;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLAttributeGeckoUCWEBImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementGeckoUCWEBImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderNodeRegistryByClientImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerGeckoUCWEBImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerGeckoUCWEBImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerGeckoUCWEBImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerGeckoUCWEBImpl()
    {
    }

    protected boolean needsAddListenerReturnElement()
    {
        return true;
    }

    protected boolean needsRemoveListenerReturnElement()
    {
        return true;
    }

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget target = itsNatListener.getCurrentTarget();
        if (target instanceof Element)
        {
            StringBuffer code = new StringBuffer();

            code.append( super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc) );

            Element elem = (Element)target;
            BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();
            String type = itsNatListener.getType();
            if ("click".equals(type) && browser.isOnlyOnClickExecuted(elem))
            {
                // Necesitamos usar el handler "onclick" para lanzar eventos
                JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
                NameValue idCont = registry.getNameValueByNode(elem);
                if (idCont == null)
                {
                    code.append( JSRenderHTMLElementGeckoUCWEBImpl.fixOnlyOnClickExecutedElementInsert(elem,"elem",clientDoc) );
                    idCont = registry.getNameValueByNode(elem); // Ahora NO puede ser nulo
                }
                // Contamos el número de listeners click
                Integer num = (Integer)idCont.getValue();
                if (num == null) num = new Integer(0);
                num = new Integer(num.intValue() + 1);
                idCont.setValue(num);
            }

            return code.toString();
        }
        else
            return super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc);
    }

    protected String removeItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        EventTarget target = itsNatListener.getCurrentTarget();
        if (target instanceof Element)
        {
            StringBuffer code = new StringBuffer();

            code.append( super.removeItsNatDOMStdEventListenerCode(itsNatListener,clientDoc) );

            Element elem = (Element)target;
            BrowserGeckoUCWEB browser = (BrowserGeckoUCWEB)clientDoc.getBrowser();
            String type = itsNatListener.getType();
            if ("click".equals(type) && browser.isOnlyOnClickExecuted(elem))
            {
                JSRenderNodeRegistryByClientImpl registry = JSRenderHTMLAttributeGeckoUCWEBImpl.getFixOnlyOnClickExecRegistry(clientDoc);
                NameValue idCont = registry.getNameValueByNode(elem);
                if (idCont != null) // Será MUY RARO que sea nulo, pero por si acaso
                {
                    // Decrementamos el número de listeners click
                    Integer num = (Integer)idCont.getValue(); // NO puede ser nulo pues antes de eliminar el listener se añadió
                    num = new Integer(num.intValue() - 1);
                    idCont.setValue(num);

                    if ((num.intValue() == 0) && !elem.hasAttribute("onclick"))
                    {
                        // Esto lo hacemos porque si no hay listeners click en el servidor
                        // y el elemento no tiene handler onclick inline (en el cliente está en la propiedad onclick)
                        // entonces no necesitamos el handler especial onclick y podemos eliminarlo
                        // y desregistrar el elemento en el cliente.
                        // Evitamos con esto, por ejemplo, que el onclick especial de un elemento
                        // sin listeners "oculte" el onclick de elementos anidados. Esto ocurre
                        // por ejemplo en componentes en modo joystick, si el nodo contenedor tiene
                        // handler especial onclick, puede ocultar los onclick de los elementos hijo.
                        String id = idCont.getName();
                        code.append( JSRenderHTMLElementGeckoUCWEBImpl.fixOnlyOnClickExecutedElementRemove(elem,id,clientDoc) );
                        code.append("elem.removeAttribute(\"onclick\");\n");
                    }
                }
            }

            return code.toString();
        }
        else
            return super.removeItsNatDOMStdEventListenerCode(itsNatListener,clientDoc);
    }
}

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

package org.itsnat.impl.core.jsren.dom.node.html;

import java.util.Iterator;
import java.util.LinkedList;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.html.msie.JSRenderHTMLElementMSIE6Impl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementASVRenesisImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementBatikImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementBlackBerryOldImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementGeckoDefaultImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementGeckoSkyFireImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementGeckoUCWEBImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementMSIE9Impl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementNetFrontImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementOpera8MobileImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementOpera9Impl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementWebKitAndroidImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementWebKitDefaultImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementWebKitMotoImpl;
import org.itsnat.impl.core.jsren.dom.node.html.w3c.JSRenderHTMLElementWebKitS60Impl;
import org.itsnat.impl.core.jsren.dom.node.otherns.JSRenderOtherNSElementW3CImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * El propósito de esta clase es definir una especie de navegador HTML común denominador
 * de todos los demás con el fin de detectar qué elementos no admiten
 * ser insertados en una sentencia innerHTML para cualquier navegador.
 * Importante en el caso del cacheado/serialización de nodos para salvar memoria a nivel de template.
 *
 * ESTA CLASE NO SE UTILIZA PARA GENERAR JAVASCRIPT
 *
 * @author jmarranz
 */
public class JSRenderHTMLElementAllBrowsersImpl extends JSRenderHTMLElementImpl
{
    public static final JSRenderHTMLElementAllBrowsersImpl SINGLETON = new JSRenderHTMLElementAllBrowsersImpl();

    protected LinkedList browsers = new LinkedList();

    /** Creates a new instance of JSMSIEHTMLElementRenderImpl */
    public JSRenderHTMLElementAllBrowsersImpl()
    {
        // MSIE Old
        addBrowser(JSRenderHTMLElementMSIE6Impl.SINGLETON);

        // W3C
        addBrowser(JSRenderHTMLElementASVRenesisImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementBatikImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementBlackBerryOldImpl.SINGLETON);

        addBrowser(JSRenderHTMLElementGeckoUCWEBImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementGeckoSkyFireImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementGeckoDefaultImpl.SINGLETON);

        addBrowser(JSRenderHTMLElementNetFrontImpl.SINGLETON);

        addBrowser(JSRenderHTMLElementOpera8MobileImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementOpera9Impl.SINGLETON);

        addBrowser(JSRenderHTMLElementWebKitAndroidImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementWebKitDefaultImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementWebKitMotoImpl.SINGLETON);
        addBrowser(JSRenderHTMLElementWebKitS60Impl.SINGLETON);
        
        addBrowser(JSRenderHTMLElementMSIE9Impl.SINGLETON);
    }

    private void addBrowser(JSRenderHTMLElementImpl renderer)
    {
        browsers.add(renderer);
        // Sólo se insertarán una sola vez pues no admite duplicados
        tagNamesWithoutInnerHTML.addAll(renderer.getTagNamesWithoutInnerHTML());
        tagNamesNotValidInsideInnerHTML.addAll(renderer.getTagNamesNotValidInsideInnerHTML());
    }

    protected boolean isChildNotValidInsideInnerHTMLHTMLElement(Element elem,MarkupTemplateVersionImpl template)
    {
        // Mismo chequeo que JSRenderHTMLElementW3CImpl.isChildNotValidInsideInnerHTMLHTMLElement(Node,Object)
        // pues añade alguna restricción más
        if (super.isChildNotValidInsideInnerHTMLHTMLElement(elem,template))
            return true; // No se puede meter dentro de un innerHTML, nada más que decir

        if (JSRenderOtherNSElementW3CImpl.isElementWithSomethingOtherNSInMIMEHTML(elem,template))
            return true; 

        return false;
    }

    protected boolean isChildNotValidInsideInnerHTMLElementNotHTML(Element elem,MarkupTemplateVersionImpl template)
    {
        return true; // Sabemos que los navegadores W3C no procesan correctamente elementos no-HTML dentro de innerHTML y MIME text/html y en un caso en MSIE , no hace falta por tanto iterar por los navegadores
    }

    protected boolean isChildNotValidInsideInnerHTMLNotElementOrText(Node node)
    {
        // Apenas se llega aquí para chequear cuando el nodo es un comentario
        if (node.getNodeType() == Node.COMMENT_NODE)
            return true; // Algunos navegadores no lo admiten dentro de innerHML (ej MSIE), así nos ahorramos iterar.

        // Aquí es posible que no lleguemos nunca y mejor pues nos ahorramos la iteración.
        // Iteramos por si algún día se añade alguna condición más complicada
        // y nos olvidamos de ponerla aquí

        for(Iterator it = browsers.iterator(); it.hasNext(); )
        {
            JSRenderHTMLElementImpl renderer = (JSRenderHTMLElementImpl)it.next();
            if (renderer.isChildNotValidInsideInnerHTMLNotElementOrText(node))
                return true;
        }

        return false;
    }

    public String getCurrentStyleObject(String itsNatDocVar,String elemName,ClientDocumentStfulImpl clientDoc)
    {
        throw new ItsNatException("INTERNAL ERROR");
    }
}

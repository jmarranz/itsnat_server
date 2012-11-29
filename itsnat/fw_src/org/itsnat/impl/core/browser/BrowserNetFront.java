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

package org.itsnat.impl.core.browser;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.html.HTMLElement;

/**
 * Soportado desde la versión 3.4
 *
 * http://www.mylolabs.com/ReferenceDocs/ReferenceDocs.asp
 *
 *   Notas sobre NetFront v3.3

  NetFront v3.3 es no usable pues no permite hacer un removeChild:

  http://objectmix.com/javascript/37965-changing-text.html

  Aquí: http://www.howtocreate.co.uk/tutorials/javascript/domintroduction
  se dice que el soporte de DOM antes de la versión 3.4 es muy malo.

 * Aunque el navegador de la PS3 y de la PSP está basado en el NetFront, la
 * versión es muy antigua (NetFront 2.81) por lo que nos olvidamos del mundo PlayStation *
 *
 * Algunos teléfonos SonyEricsson soportan NetFront 3.4 (y alguno de Samsung)
 *
   http://developer.sonyericsson.com/getDocument.do?docId=65048

 * http://www.design215.com/read.php?title=playstation%203%20browser%20specs
 * http://en.wikipedia.org/wiki/PlayStation_3
 *
 * Peculiaridades del NetFront 3.5 (Build 635) que influyen en ItsNat
   pero no hay un lugar concreto en donde se pueda documentar:

    => JavaScript: El NodeList del childNodes de un elemento FORM NO soporta el método item
      La forma de acceder a los nodos hijo es a través de []
      Detectado en el build 635.
      Por prudencia usar siempre los []


 * @author jmarranz
 */
public class BrowserNetFront extends BrowserW3C
{
    private static final Map tagNamesIgnoreZIndex = new HashMap();
    static
    {
        tagNamesIgnoreZIndex.put("select",null);
        tagNamesIgnoreZIndex.put("input",null);
        tagNamesIgnoreZIndex.put("textarea",null);
    }


    /**
     * Creates a new instance of BrowserNetFront
     */
    public BrowserNetFront(String userAgent)
    {
        super(userAgent);

        this.browserType = NETFRONT;
    }

    public static boolean isNetFront(String userAgent)
    {
        return (userAgent.indexOf("NetFront") != -1);
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return false;
    }

    public boolean isReferrerReferenceStrong()
    {
        // El back/forward está cacheado en el cliente.
        return true;
    }

    public boolean isCachedBackForward()
    {
        return true;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        return true;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return false;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // El focus() da muchos problemas en el sentido de que impide cambiar
        // la propiedad "value" después (ignora el cambio).
        return true;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return tagNamesIgnoreZIndex;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

    public boolean isSetTimeoutSupported()
    {
        return true;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // No soporta SVG por ejemplo.
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        // En teoría se ejecuta pero curiosamente después de que
        // termine el código que inserta el <script>.
        return false;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no hay SVG
    }
    
    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no hay SVG
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
    }
}

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
 * Desde la versión 6 (incluido en Windows Mobile 6 y 6.1)
 * Es un navegador muerto pues desde 6.1.4 ha cambiado totalmente
 *
 *  Formato User Agent: Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows CE; IEMobile m.n)
 *  Ejemplo (WM 6): Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows CE; IEMobile 6.12)
 *  http://blogs.msdn.com/iemobile/archive/2006/08/03/Detecting_IE_Mobile.aspx
 *
 * Enlaces:
 *
  http://blogs.msdn.com/iemobile/archive/2007/05/15/ie-mobile-standards-support.aspx
  http://msdn2.microsoft.com/en-us/library/bb158633.aspx "Developing AJAX Applications for Windows Mobile"
  http://msdn2.microsoft.com/en-us/library/bb415428.aspx "Internet Explorer Mobile Reference"
  http://msdn2.microsoft.com/en-us/library/bb159684.aspx "Identifying Internet Explorer Mobile to a Web Server"
  http://msdn2.microsoft.com/en-us/library/bb415489.aspx  HTML Elements Cada elemento especifica las propiedades métodos etc que soporta
  http://blogs.msdn.com/iemobile/archive/2006/08/03/Detecting_IE_Mobile.aspx
  http://en.wikipedia.org/wiki/Internet_Explorer_Mobile
  http://blogs.msdn.com/iemobile/archive/2005/11/15/493200.aspx
  http://www.amset.info/pocketpc/emulator.asp
  http://www.pocketpcfaq.com/faqs/5.0/aku.htm
  http://modernnomads.info/wiki/index.php?page=OS+Versions
  http://discussion.treocentral.com/showthread.php?p=1081606
  http://channel9.msdn.com/wiki/default.aspx/MobileDeveloper.ProgrammingWithInternetExplorerMobileAndAjax
  http://blogs.msdn.com/iemobile/archive/2006/05/03/589322.aspx
 *
 * @author jmarranz
 */
public class BrowserMSIEPocket extends BrowserMSIEOld
{
    public static final Set EVENTS = new HashSet();
    public static final Map EVENTS_BYTAG = new HashMap();

    static
    {
        addEvent("a","blur"); addEvent("a","click"); addEvent("a","focus");
        addEvent("body","load"); addEvent("body","unload");
        addEvent("button","blur"); addEvent("button","click"); addEvent("button","focus");
        addEvent("form","submit"); addEvent("form","reset");
        addEvent("frameset","load"); addEvent("frameset","unload");
        addEvent("img","load");
        addEvent("input","blur"); addEvent("input","change");
          addEvent("input","click"); addEvent("input","focus");
          addEvent("input","load"); // Nota: cada tipo de INPUT (atributo type) tiene varios tipos de eventos pero no todos
        addEvent("select","blur"); addEvent("select","change"); addEvent("select","focus");
        addEvent("textarea","blur"); addEvent("textarea","change"); addEvent("textarea","focus");
        addEvent("window","load"); addEvent("window","unload");
    }

    public static void addEvent(String localName,String type)
    {
        EVENTS.add(localName + "_" + type);
        LinkedList types = (LinkedList)EVENTS_BYTAG.get(localName);
        if (types == null)
        {
            types = new LinkedList();
            EVENTS_BYTAG.put(localName, types);
        }
        types.add(type);
    }

    public static boolean hasEvent(String localName,String type)
    {
        return EVENTS.contains(localName + "_" + type);
    }

    public static Map getEventTypesByTagName()
    {
        return EVENTS_BYTAG;
    }

    public static String getHandlerValue(String type)
    {
        // Tiene que ser exactemente este valor, en JavaScript se usa también
        // Si se cambia cambiar también en el código JavaScript
        return "return document.getItsNatDoc().dispatchEvent(this,'" + type + "',null);";
    }

    public static boolean needAttributeHandler(String localName)
    {
        //if (localName.equals("window") || localName.equals("body"))
        //    return false; // No necesitan esto, window por que no es un elemento, y BODY no necesita el atributo como forma de conectar con el sistema de listeners de ItsNat y por tanto sus listeners no necesitan la emulación de setAttribute
        return EVENTS_BYTAG.containsKey(localName);
    }

    /** Creates a new instance of BrowserMSIEPocket */
    public BrowserMSIEPocket(String userAgent)
    {
        super(userAgent);

        this.browserSubType = MSIE_POCKET;
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
        // No se carga la página desde el servidor en back/forward pero el evento
        // load NO se ejecuta.
        return false;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // El evento blur no se emite en los <input> de texto (text,password,file) y <textarea>
        // ni llamando a blur() ni saliendo "a mano" del control. Por tanto ante una llamada a blur()
        // emitimos el evento nosotros para asegurarnos.
        return (methodName.equals("blur") &&
                DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem));
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        // No hay posicionamiento absoluto en Pocket IE por lo que no nos preocupa esto
        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

    public boolean isEventTimeoutSupported()
    {
        // Es un misterio pero NO SE DEBE LLAMAR a window.setTimeout
        // tras una llamada al send() del XMLHttpRequest, aunque window.setTimeout
        // esté soportado.
        // Es como si en el scheduler de tareas pendientes pusiera
        // antes del envío del evento AJAX la ejecución de la función del setTimeout.
        return false;
    }

    public boolean isFunctionEnclosingByBracketSupported()
    {
        // Es curioso porque en teoría sí lo soporta pero cuando el código
        // que hay dentro es complicado "se estropea" como es el caso
        // del código de inicio de la página
        // El problema yo creo que NO es la sintaxis sino que no admite demasiadas
        // funciones dentro de funciones, pues tampoco vale en la práctica la técnica
        // var func = function() { ... }; func();  si lo de dentro tiene más niveles.
        return false;
    }
}

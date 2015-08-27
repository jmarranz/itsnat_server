/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.browser.web;

import java.util.Map;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOld;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.doc.web.ItsNatSVGDocumentImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLAttributeImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLElementImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLTextImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public abstract class BrowserWeb extends Browser
{
    public static final int UNKNOWN  = 0;  // Robots, nuevos navegadores etc
    public static final int MSIE_OLD = 1;
    public static final int GECKO  = 2;
    public static final int WEBKIT = 3;  // Navegadores basados en WebKit
    public static final int OPERA_OLD  = 4;
    public static final int BLACKBERRY_OLD = 5; // NO SE USA YA
    public static final int ADOBE_SVG = 6;
    public static final int BATIK = 7;
    public static final int MSIE_9 = 8;
   
    
    protected int browserType;
    protected int browserSubType = -1; // Hay alguno que no tiene subtipos    
    
    protected transient JSRenderHTMLElementImpl jsRenderHtmlElement;
    protected transient JSRenderHTMLTextImpl jsRenderHtmlText;
    protected transient JSRenderHTMLAttributeImpl jsRenderHtmlAttr;
    
    public BrowserWeb(String userAgent)
    {
        super(userAgent);
    }
    
    public static BrowserWeb createBrowserWeb(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        // Enorme lista de user-agents:
        // http://www.botsvsbrowsers.com/category/

        // http://en.wikipedia.org/wiki/User_agent
        // http://www.useragentstring.com/pages/useragentstring.php
        // http://web.archive.org/web/20061217053523/http://en.wikipedia.org/wiki/User_agent
        // http://en.wikipedia.org/wiki/List_of_user_agents_for_mobile_phones
        // http://mobileopera.com/reference/ua
        // http://www.zytrax.com/tech/web/mobile_ids.html
        // http://www.smartphonemag.com/cms/blogs/3/an_updated_list_of_mobile_user_agents
        // http://forums.thoughtsmedia.com/f323/updated-list-mobile-user-agents-89045.html (más claro)

        if (isMSIE(userAgent,itsNatRequest))  //  MSIE 11 incluye "like Gecko" por lo que tenemos que evaluar ANTES de Gecko
        {
            int version = getMSIEVersion(userAgent);
            if (version < 9)
                return BrowserMSIEOld.createBrowserMSIEOld(userAgent,itsNatRequest,version);
            else
                return new BrowserMSIE9(userAgent,version);
        }
        else if (BrowserGecko.isGecko(userAgent,itsNatRequest))
            return BrowserGecko.createBrowserGecko(userAgent,itsNatRequest);
        else if (BrowserWebKit.isWebKit(userAgent))
            return BrowserWebKit.createBrowserWebKit(userAgent);
        else if (BrowserOperaOld.isOperaOld(userAgent,itsNatRequest)) // Llamar DESPUES de WebKit pues también soportamos Opera basado en WebKit cuyo user agent contiene la palabra Opera
            return BrowserOperaOld.createBrowserOperaOld(userAgent);
        else if (BrowserBatik.isBatik(userAgent))
            return new BrowserBatik(userAgent); 
        else // Desconocido (suponemos que es un robot)
            return new BrowserUnknown(userAgent);
    }    
    
    public static boolean isMSIE(String userAgent,ItsNatServletRequestImpl itsNatRequest)
    {
        // Opera en algunas versiones (algún Opera 9.x por ejemplo) incluye la palabra "MSIE", excluimos esos casos
        // IE 11 tiene algún user agent SIN MSIE pero con Trident
        // http://www.useragentstring.com/pages/Internet%20Explorer/
        return (userAgent.contains("MSIE") || userAgent.contains("Trident")) &&
                !BrowserOperaOld.isOperaOld(userAgent,itsNatRequest);
    }
    
    public static int getMSIEVersion(String userAgent)
    {
        if (userAgent.contains("MSIE "))
        {
            try
            {
                // Se espera MSIE M.m
                // nos interesa sólo M
                int start = userAgent.indexOf("MSIE ") + "MSIE ".length();
                int end = userAgent.indexOf('.',start);
                return Integer.parseInt(userAgent.substring(start, end));
            }
            catch(Exception ex)
            {
                // Por si cambia Microsoft el patrón
                return 8; // La versión mínima soportada
            }
        }
        else if (userAgent.contains("Trident")) // Caso de IE 11 con este user agent: Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko
        {
            try
            {
                // Se espera rv:M.n
                // nos interesa sólo M
                int start = userAgent.indexOf("rv:") + "rv:".length();
                int end = userAgent.indexOf('.',start);
                return Integer.parseInt(userAgent.substring(start, end));
            }
            catch(Exception ex)
            {
                // Por si cambia Microsoft el patrón
                return 8; // La versión mínima soportada
            }            
        }
        else
        {
            return 8; // La versión mínima soportada
        }
    }
    
    
    public int getTypeCode()
    {
        return browserType;
    }

    public int getSubTypeCode()
    {
        return browserSubType;
    }    
    
    public abstract boolean isMobile();   
    
    public JSRenderHTMLElementImpl getJSRenderHTMLElement()
    {
        return jsRenderHtmlElement;
    }

    public void setJSRenderHTMLElement(JSRenderHTMLElementImpl jsRenderHtmlElement)
    {
        this.jsRenderHtmlElement = jsRenderHtmlElement;
    }    
    
    public JSRenderHTMLTextImpl getJSRenderHTMLText()
    {
        return jsRenderHtmlText;
    }

    public void setJSRenderHTMLText(JSRenderHTMLTextImpl jsRenderHtmlText)
    {
        this.jsRenderHtmlText = jsRenderHtmlText;
    }    
    
    public JSRenderHTMLAttributeImpl getJSRenderHTMLAttribute()
    {
        return jsRenderHtmlAttr;
    }

    public void setJSRenderHTMLAttribute(JSRenderHTMLAttributeImpl jsRenderHtmlAttr)
    {
        this.jsRenderHtmlAttr = jsRenderHtmlAttr;
    }    
    
    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // El evento beforeunload fue introducido por MSIE, no es W3C, por tanto en SVG (cuando es soportado) es ignorado        
        // En SVG no existe conceptualmente, es más propio de HTML aunque en XUL está también soportado
        // En Opera y BlackBerryOld se redefine porque no se soporta nunca
        return ! (itsNatDoc instanceof ItsNatSVGDocumentImpl);
    }      
     
    
    public abstract boolean isDOMContentLoadedSupported();    
    
    /* El back y el forward no recargan la página desde el servidor
     * pero se ejecutan los elementos <script> dentro del <body> son ejecutados
     * y el evento load (y DOMContentLoaded antes si es soportado).
     * En este caso la página a la que se vuelve tiene el estado de antes
     * de cargar los scripts (no el que queda al dejar la página).
     */
    public abstract boolean isCachedBackForwardExecutedScripts();    
    
    /* El back y el forward no recargan la página desde el servidor
     * ¿Ya no se usa este método? parece que no.
     * De todas formas aunque no se use está bien para caracterizar bien al navegador
     */
    public abstract boolean isCachedBackForward();
    
    /*
     * Algunos navegadores (ej. FireFox 2.0 y S60WebKit) lanzan el blur antes del change
     * cuando abandonamos un text/password box o textarea que ha sido cambiado.
     */
    public abstract boolean isBlurBeforeChangeEvent(HTMLElement formElem);    
    

    /* Si el método focus() y/o blur() no debe ser llamado (y enviar eventos de forma alternativa)
     * o bien porque es ignorado o bien porque es problemático. El contexto de test es el edit inplace
     * ejecutando el focus() como una respuesta AJAX asíncrono.
     * Ocurre al menos en elementos de formulario de tipo: HTMLTextArea, HTMLInputElement o HTMLSelectElement.
     * formElem es no nulo y es un elemento HTML con método focus(), es decir HTMLSelectElement, HTMLTextAreaElement o HTMLInputElement
     */
    public abstract boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem);    
    
    /**
     * Si hay elementos que ignoran el zIndex y recibe eventos.
     * @return 
     */
    public abstract Map<String,String[]> getHTMLFormControlsIgnoreZIndex();   
    
    /* Si soporta opacidad aunque no sea a través de CSS opacity (caso de MSIE_OLD 6+)
     * Sólo tiene sentido en documentos X/HTML.
     */
    public abstract boolean hasHTMLCSSOpacity();
    
    /* Si es capaz de renderizar nativamente markup con namespace no X/HTML, por ejemplo SVG, MathML
     * Si la respuesta es true equivale a preguntar si soporta SVG pues al menos es siempre SVG
       el primer namespace no X/HTML (Gecko soporta también XUL y MathML).
     */
    public abstract boolean canNativelyRenderOtherNSInXHTMLDoc();
    
    
    /* Un elemento <script> con código no es ejecutado cuando es insertado como tal
     * (incluído el código) ya sea el código insertado antes o después
     * de insertar el elemento <script>. Esto no ocurre en HTML por eso sólo consideramos SVG
     */
    public abstract boolean isInsertedSVGScriptNotExecuted(); 
    
    /* Idem para SVG que isTextAddedToInsertedHTMLScriptNotExecuted()
       En el caso de Opera 9 es necesario por ejemplo */
    public abstract boolean isTextAddedToInsertedSVGScriptNotExecuted();    
}

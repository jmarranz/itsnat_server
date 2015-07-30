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

import org.itsnat.impl.core.browser.droid.BrowserDroid;
import org.itsnat.impl.core.browser.web.BrowserGecko;
import org.itsnat.impl.core.browser.web.BrowserMSIEOld;
import org.itsnat.impl.core.browser.web.BrowserBatik;
import org.itsnat.impl.core.browser.web.BrowserUnknown;
import org.itsnat.impl.core.browser.web.BrowserMSIE9;
import java.io.Serializable;
import org.itsnat.impl.core.browser.web.opera.BrowserOperaOld;
import org.itsnat.impl.core.browser.web.webkit.BrowserWebKit;
import org.itsnat.impl.core.doc.web.ItsNatSVGDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;

/**
 *
 * @author jmarranz
 */
public abstract class Browser implements Serializable
{
    public static final int UNKNOWN  = 0;  // Robots, nuevos navegadores etc
    public static final int MSIE_OLD = 1;
    public static final int GECKO  = 2;
    public static final int WEBKIT = 3;  // Navegadores basados en WebKit
    public static final int OPERA_OLD  = 4;
    public static final int BLACKBERRY_OLD = 5;
    public static final int ADOBE_SVG = 6;
    public static final int BATIK = 7;
    public static final int MSIE_9 = 8;
    public static final int DROID = 9;
    
    protected String userAgent;
    protected int browserType;
    protected int browserSubType = -1; // Hay alguno que no tiene subtipos




    /** Creates a new instance of Browser */
    public Browser(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public static Browser createBrowser(ItsNatServletRequestImpl itsNatRequest)
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

        String userAgent = itsNatRequest.getHeader("User-Agent");

        if (BrowserDroid.isBrowserDroid(userAgent))
            return new BrowserDroid(userAgent);
        else if (isMSIE(userAgent,itsNatRequest))  //  MSIE 11 incluye "like Gecko" por lo que tenemos que evaluar ANTES de Gecko
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



    public String getUserAgent()
    {
        return userAgent;
    }

    public int getTypeCode()
    {
        return browserType;
    }

    public int getSubTypeCode()
    {
        return browserSubType;
    }


    /* Si usamos una referencia strong para almacenar el referrer.
     * Aplicar cuando no hay garantía de que el nuevo documento se cargue antes del unload del anterior.
     * Es aconsejable cuando el back/forward es cacheado en el cliente por ejemplo
     * porque al volver a una página cacheada en el cliente puede perderse el documento
     * anterior via unload y no cargarse antes el nuevo documento.
     */
    public abstract boolean isReferrerReferenceStrong();


    /* Especifica si window tiene addEventListener o attachEvent 
     * A día de hoy sólo algunos plugins SVG no admiten eventos en window
     */
    public abstract boolean isClientWindowEventTarget();

    /* Si soporta la sintaxis: (function(){ ... })(); útil por ejemplo
     * para evitar memory leaks en MSIE_OLD y evitar variables globales si estamos dentro de un <script>
     */
    public boolean isFunctionEnclosingByBracketSupported()
    {
        return true;
    }

    /*
     * Si necesita URL absolutos para requests AJAX o en carga de scripts con <script>
     */
    public boolean isNeededAbsoluteURL()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        // El evento beforeunload fue introducido por MSIE, no es W3C, por tanto en SVG (cuando es soportado) es ignorado        
        // En SVG no existe conceptualmente, es más propio de HTML aunque en XUL está también soportado
        // En Opera y BlackBerryOld se redefine porque no se soporta nunca
        return ! (itsNatDoc instanceof ItsNatSVGDocumentImpl);
    }    
}

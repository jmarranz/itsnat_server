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

package org.itsnat.impl.core.browser.opera;

import java.util.Map;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
 * A partir de Opera Mobile 9.5 beta (Win CE y Symbian UIQ)
 * No existe 9.5 "release" ni v9.6, se pasó directamente a la 9.7 beta,
 * la última versión 9.5 fue la 9.51 beta.
 *
 * @author jmarranz
 */
public class BrowserOpera9Mobile extends BrowserOpera9
{
    protected int subVersion;

    /**
     * Creates a new instance of BrowserOpera9Mobile
     */
    public BrowserOpera9Mobile(String userAgent)
    {
        super(userAgent);

        this.browserSubType = OPERA_MOBILE_9;

        // Subversión:
        // 9.xx => xx:
        // 9.x => x y multiplicamos por 10 => x0

        // El Opera Mobile 10 sigue conteniendo la cadena "Opera/9."
        // para evitar problemas en webs por cambio de valor a dos dígitos (9=>10)
        // concretamente es: Opera/9.80
        // Teniendo en cuenta que no existe Opera Mobile 9.8 podemos
        // considerar Opera Mobile 10 como la v9.8 (subVersion será 80)

        try
        {
            int start = userAgent.indexOf("Opera/9.");
            start += "Opera/9.".length();
            int end = start;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == ' ')
                    break;
                end++;
            }
            String strVer = userAgent.substring(start,end);
            this.subVersion = Integer.parseInt(strVer);
            if (subVersion < 10) subVersion = subVersion * 10; // Ejemplo 9.7 => 7 => 70
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.subVersion = 0;
        }
    }

    public static boolean isOperaMobile9(String userAgent)
    {
        // User agents 9.5:
        //   Bajado de Opera.com: "Opera/9.51 Beta (Microsoft Windows; PPC; Opera Mobi/1718; U; en)"
        //   UIQ 3.3 beta simul.:  "Phone1 Model1/ID Mozilla/4.0 (compatible; MSIE_OLD 6.0; Symbian OS; en-GB; 10000346) Opera 9.5"
        //   SAMSUNG-SGH-i900:     "SAMSUNG-SGH-i900/1.0 (compatible; MSIE_OLD 6.0; Windows CE; PPC) Opera 9.5"
        //       "               "SAMSUNG-SGH-i900/1.0 Opera 9.5"
        //   (hay más variantes)        //
        // 9.7 beta: "Opera/9.7 (Windows Mobile; PPC; Opera Mobi/35166; U; en) Presto/2.2.1"
        // 10 beta 2: "Opera/9.80 (Windows Mobile; WCE; Opera Mobi/WMD-50286; U; en) Presto/2.4.13 Version/10.00"
        return (userAgent.indexOf("Opera Mobi") != -1) ||
                ((userAgent.indexOf("Opera 9") != -1) &&
                 ((userAgent.indexOf("Symbian") != -1) ||
                  (userAgent.indexOf("Windows CE") != -1) ||
                  (userAgent.indexOf("SAMSUNG-SGH-i900") != -1)) );
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean isCookiesNotSaved()
    {
        // La primera beta de Opera Mobile 9.7 tiene el problema de que
        // en modo no Turbo (el modo por defecto) las cookies enviadas por el
        // servidor NO son salvadas en el cliente (y hacerlo via document.cookie no hace nada)
        // aunque el servidor cree que sí que admite cookies
        // por ello tenemos que forzar el URL rewrite añadiendo
        // el session ID en la URL.
        // Ya veremos cuando se arregla
        // pero aunque se arregle seguirá funcionando bien, de hecho en modo
        // Turbo (modo que todavía es muy beta pues por ejemplo el select multiple
        // funciona mal) no es necesario, pero no interfiere (el id se enviá como cookie
        // y como URL).
        // http://dev.opera.com/forums/topic/283963

        // Opera Mobile 10 beta 2 (la siguiente versión a la 9.7) ya no tiene este problema.

        return subVersion < 80;
    }

    public boolean isDOMNotValidOnLoading()
    {
        // Hasta que no se emite el DOMContentLoaded el DOM puede no ser correcto
        // La v9.7 ya lo soluciona.
        return subVersion < 70;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        return true;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // La llamada a focus() es ignorada en los input type=text, suponemos lo mismo en "password" y "file"
        // Si el focus() es ignorado un posterior blur() lo sería
        return DOMUtilHTML.isHTMLInputTextBased(formElem);
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        // En teoría todos los elementos no ignoran el z-index, sin embargo
        // aunque Opera Mobile 9.x está diseñado para pantallas táctiles,
        // hasta cierto punto soporta navegación por cursores,
        // y he descubierto que a través de los cursores podemos llegar y "pulsar"
        // todos los elementos con listeners de layers por debajo, tanto en Win CE como en Symbian.

        // El problema es que cualquier elemento no form con
        // un listener "click" es susceptible también de llegarse a él usando
        // los cursores, desafortunadamente "el cuadro de foco" se muestra
        // al pasar por el mismo.

        // No consideramos ese caso pues su pulsación es premeditada o accidental
        // y en ese caso lo que hacemos es lanzar una excepción.

        return null;
    }

    public boolean hasHTMLCSSOpacity()
    {
        // Parece que la versión 9.7 beta ha sido una regresión en este tema
        // no es que no esté soportada la opacidad, es que es un bug, en donde
        // si se pone un valor menor que 1 oculta el nodo, como si internamente
        // aplicara la opacidad 0 (el valor el la propiedad CSS es correcto),
        // por lo que hay que evitar aplicar el opacity o hacerlo con el valor 1

        // En la v10 (9.8 en nuestra numeración) ya no hay este problema

        return subVersion != 70;
    }

}

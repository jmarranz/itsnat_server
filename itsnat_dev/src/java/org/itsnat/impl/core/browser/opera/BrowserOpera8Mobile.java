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

import java.util.HashMap;
import java.util.Map;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
 * Desde Opera Mobile 8.6 para Windows Mobile y UIQ 3.0
 *
 * Notas sobre Opera Mobile 8.6 (WinCE al menos):
 * El JavaScript de Opera Mobile 8.6 falla *aleatoriamente*, no parece que se solucione
 * ni con el modo AJAX síncrono ni sin usar GZIP en las comunicaciones.
 * Lo que sí creo que mejora el comportamiento es no ejecutar el script de iniciación
 * hasta que la página se ha cargado del todo (evento load) aunque en teoría no es necesario.
 * Cuando empieza a fallar inexplicablemente lo mejor es volver a cargar el navegador.
 *
 * User agents de los navegadores estudiados:
 * v8.65 WinCE   : Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows CE; PPC; 240x320) Opera 8.65 [en]
 * v8.60 WinCE   : Mozilla/4.0 (compatible; MSIE_OLD 6.0; Windows CE; PPC; 480x480) Opera 8.60 [en]
 * v8.65 UIQ 3.1 : Phone1 Model1/ID Mozilla/4.0 (compatible; MSIE_OLD 6.0; Symbian OS; 319) Opera 8.65 [en]
 * v8.60 UIQ 3.0 : Mozilla/4.0 (compatible; MSIE_OLD 6.0; Symbian OS; 225) Opera 8.60 [en-US]Phone1 Model1/ID
 *
 * @author jmarranz
 */
public class BrowserOpera8Mobile extends BrowserOpera
{
    private static final Map tagNamesIgnoreZIndex = new HashMap();
    static
    {
        // En teoría sólo <button> ignora el z-index y permite ser pulsado
        // usando el ratón en el emulador (tanto WinCE como UIQ),
        // sin embargo el Opera Mobile 8.x está diseñado para poder utilizarse
        // también exclusivamente con los cursores y he descubierto que a través
        // de los cursores podemos llegar y "pulsar" todos los elementos con listeners
        // de layers por debajo. El problema es que cualquier elemento no form con
        // un listener "click" es susceptible también de llegarse a él usando
        // los cursores aunque afortunadamente "el cuadro de foco" no se muestra
        // al pasar por el mismo lo cual hace que si se pulsan es por accidente.
        // A los elementos no form con listeners mouseup/down no se puede llegar con los cursores
        // curiosamente (sólo listeners "click").

        // No consideramos ese caso, es muy accidental y Opera Mobile 8.x es demasiado
        // antiguo y sus potenciales usuarios no muchos para merecer tanto esfuerzo,
        // además en dicho caso lanzamos una excepción.

        tagNamesIgnoreZIndex.put("button",null);
    }

    protected boolean windowsCE; // Si es false es que es Symbian u otro (no soportado pero por si acaso)
    protected int subVersion; // Ej. en v8.65 subVersion es 65

    /**
     * Creates a new instance of BrowserOpera8Mobile
     */
    public BrowserOpera8Mobile(String userAgent)
    {
        super(userAgent);

        this.browserSubType = OPERA_MOBILE_8;
        this.windowsCE = (userAgent.indexOf("Windows CE") != -1);

        // Subversión 8.xx => xx:
        try
        {
            int start = userAgent.indexOf("Opera 8.");
            start += "Opera 8.".length();
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
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.subVersion = 0;
        }
    }

    public static boolean isOpera8Mobile(String userAgent)
    {
        // http://www.botsvsbrowsers.com/category/8/index.html
        // http://mobileopera.com/reference/ua#presto
        return ( ((userAgent.indexOf("Windows CE") != -1) ||
                  (userAgent.indexOf("Symbian") != -1) ||
                  (userAgent.indexOf("Motorola") != -1) ||
                  (userAgent.indexOf("MOT-") != -1) ) &&
                 (userAgent.indexOf("Opera 8") != -1) );
    }

    public boolean isMobile()
    {
        return true;
    }
/*
    public boolean isWindowsCE()
    {
        return windowsCE;
    }

    public int getSubVersion()
    {
        return subVersion;
    }
*/
    public boolean isCachedBackForwardExecutedScripts()
    {
        // Por ejemplo usando window.history.back() no se ejecutan onload y <script>
        // pero con el botón sí. Como en algún caso se envía el evento load
        // devolvemos true, en los casos en donde no se emita el evento load
        // lo hará al procesar cualquier evento al no existir ya el documento.
        // De todas formas NO he conseguido que funcione bien (ver los ejemplos del Feature Showcase)
        return true;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // La llamada a focus() es ignorada
        // Si el focus() es ignorado un posterior blur() lo sería
        return true;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return tagNamesIgnoreZIndex;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        // En Opera Mobile 8.6x tanto en WinCE como UIQ se da el caso
        // curioso de emitirse el "blur" antes que el change en el
        // caso de <select> combo box (no "size" ni "multiple") cuando
        // dicho <select> se utiliza usando exclusivamente los cursores.
        // Por ello decimos que blur se emite antes que change (aunque no
        // ocurra con pantalla táctil) pues lo normal es hacer que el blur
        // sea un change lo cual es compatible con ambos casos.
        // Este problema se puede estudiar en el edit in place con <select>
        return DOMUtilHTML.isHTMLSelectComboBox(formElem);
    }

    public boolean isInitialScriptElementNotInDOM()
    {
        // En tiempo de carga el <script> que ejecuta el código inicial
        // no está presente en el DOM. Se ha detectado sólo en
        // Opera Mobile 8.60 UIQ (UIQ 3.0), suponemos igual en versiones anteriores
        // aunque no estén soportadas.
        return !windowsCE && (subVersion <= 60);
    }

    public boolean isLoadEventNotEverFired()
    {
        // Se ha detectado este error en Opera Mobile 8.60 UIQ (UIQ 3.0)
        // cuando se carga la página llegando a ella a través de un link,
        // esto no ocurre cuando es un reload manual.
        // Quizás el problema es debido a que los registros de los eventos
        // load se realizan tras un setTimeout debido al problema de carga este navegador
        // (ver isInitialScriptElementNotInDOM()).
        return !windowsCE && (subVersion <= 60);
    }

    public boolean isInputCheckBoxSurplusFocusBlur()
    {
        // El elemento <input type="checkbox"> tienen un fallo peculiar,
        // cuando se llega al mismo usando los cursores (con el ratón no hay problema)
        // se dispara un focus un blur y un focus, es decir el primer focus/blur
        // es sobrante. Esto no ocurre en WinCE 8.65 pero sí ocurre en los demás,
        // es decir WinCE 8.60 y los UIQs incluido el 8.65
        // Esto es importante en edición in-place
        if (this.windowsCE && (this.subVersion == 65))
            return false;
        return true;
    }

    public boolean isHTMLSelectSelectedPropBuggy()
    {
        return this.subVersion <= 60;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // No soporta SVG por ejemplo.
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no hay SVG
    }
    
    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, no hay SVG
    }
}

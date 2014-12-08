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

package org.itsnat.impl.core.browser.web.webkit;

import java.util.Map;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*

  Soportado desde el Safari del iOS 6.1
 
  User agents:

   Mozilla/5.0 (iPad; CPU OS 6_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B141 Safari/8536.25  
   Mozilla/5.0 (iPhone; CPU iPhone OS 6_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B141 Safari/8536.25 
  
    - User agents antiguos

        La que más se muestra en Google (la primera versión seguramente):
        Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3

        Beta 1 (AppleWebKit/525.7):
          Mozilla/5.0 (Aspen Simulator; U; Aspen 1_2 like Mac OS X; en-us) AppleWebKit/525.7 (KHTML, like Gecko) Version/3.1 Mobile/5A147p Safari/5525.7

        Beta 2 (AppleWebKit/525.15) No la tengo: http://www.intomobile.com/2008/03/30/webkit-gets-perfect-score-on-acid3-web-standards-iphone-safari-and-s60-web-browsers-will-be-even-better.html
          Mozilla/5.0 (Aspen Simulator; U; iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.15 (KHTML, like Gecko) Version/3.1 Mobile/5A225c Safari/5525.7

        Beta 5 (AppleWebKit/525.19):
            Mozilla/5.0 (iPhone Simulator; U; iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.19 (KHTML, like Gecko) Version/3.1 Mobile/5A274d Safari/5525.7

        http://groups.google.com/group/iphonewebdev/browse_thread/thread/10e1b3c08e2c5acf

    - User agents

        v1.1.4 (beta 1): "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420.1 (KHTML, like Gecko) Version/3.0 Mobile/4A102 Safari/419.3"
        Betas desde la 2 ya son iPhone 2.0

        v2.0 Beta X?
                Mozilla/5.0 (iPhone; U; iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.15 (KHTML, like Gecko) Version/3.1 Mobile/5A225c Safari/5525.7
        v2.0 Beta 5:
                Mozilla/5.0 (iPhone Simulator; U; iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.19 (KHTML, like Gecko) Version/3.1 Mobile/5A274d Safari/5525.7
        v2.0:   Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5A345 Safari/525.20
        v2.1:   Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 2_1 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5F135 Safari/525.20
        v2.2:   Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 2_2 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5G77 Safari/525.20
        v2.2.1: Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 2_2_1 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5H11 Safari/525.20
        v3.0:   Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16
                  (Notar que usa el WebKit de la v4 de Safari)

    - iPod Touch

        http://www.botsvsbrowsers.com/listings.asp?search=iPod

        Notar como en los user agents más actuales "iPhone" está dos veces,
        en los iPod el primer "iPhone" es "iPod" compartiendo la parte
        "iPhone OS version". Por tanto soportamos el iPod indirectamente.

        Ej: Mozilla/5.0 (iPod; U; CPU iPhone OS 2_2 like Mac OS X; es-es) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5G77 Safari/525.20

    * Sobre el iPhone real y el simulador

        - SDK Beta 1
            El iPhone real (firmware antiguo) y el simulador (beta 1) tuvieron un error muy grave:
            consiste en que el primer elemento de un <select multiple> se selecciona por defecto sin quererlo
            Una consecuencia es que el evento change no siempre se emite (cuando el primero está seleccionado).
            No tiene solución y fue arreglado en firmwares posteriores.
            http://forums.macrumors.com/showthread.php?t=324849
            http://prayforsalvation.com/cgi-bin/iphonebug.pl
            http://modeleven.blogspot.com/2007/12/iphone-bugs.html

        - SDK Beta 5
            También un fallo en el iPhone real, parece que fue introducido en el firmware 2.0.
            El evento "change" no es lanzado en <select multiple> o con el atributo "size" (>1 supongo)
            http://lists.apple.com/archives/safari-iphone-web-dev/2008/Jul/msg00025.html
            http://groups.google.com/group/iphonewebdev/browse_thread/thread/33288230c30cbd53

  * Algunos análisis:

    - iPhone Simulator (AppleWebKit/525.7 y .19): es más moderno que el firmware del iPhone real más antiguo

        DOMContentLoaded : SI
        beforeunload : NO
        Unload Guarantied : SI
        Cached Back/Forward : NO
        Eventos síncronos: SI

    - iPhone real (AppleWebKit/420+)

        DOMContentLoaded : seguramente NO (fue introducido en Webkit 525)
        beforeunload: parece que NO, el simulador no lo soporta y http://lists.apple.com/archives/Web-dev/2008/Jan/msg00036.html
        Unload Guarantied : suponemos que SI
        Cached Back/Forward : suponemos que NO
        Eventos síncronos: suponemos que SI

 */

public class BrowserWebKitIOS extends BrowserWebKit
{
    protected int iPhoneMainVersion;
    protected int iPhoneSubVersion;

    public BrowserWebKitIOS(String userAgent)
    {
        super(userAgent,IPHONE);

        // Versión:
        // Formato: "iPhone OS V_v " o "iPhone OS V_v_v " (ej "iPhone OS 2_0 ")
        try
        {
            int start = userAgent.indexOf(" OS ");
            start += " OS ".length();
            int end = start;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == ' ')
                    break;
                end++;
            }
            String strVer = userAgent.substring(start,end); // Ej "2_0"
            String[] strVerArr = strVer.split("_");
            this.iPhoneMainVersion = Integer.parseInt(strVerArr[0]);
            this.iPhoneSubVersion =  Integer.parseInt(strVerArr[1]);
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.iPhoneMainVersion = 6;
            this.iPhoneSubVersion =  1;
        }
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        return false;  // Curiosamente iPhone no soporta beforeunload en HTML (menos aun SVG que no lo soporta)
    }


    public boolean ignoreChangeEventSelectMultiple(HTMLElement elem)
    {
        // El iPhone es terrible en cuanto al funcionamiento del select multiple
        // (con y sin el atributo size).

        // En el iPhone 2.0 y 2.1 (las versiones anteriores 1.x eran beta y no las consideramos)
        // el evento change NO se emite, la solución en este caso no es ignorar
        // el evento change, es otra.

        // En el caso del iPhone 2.2 y 2.2.1 el comportamiento
        // parece que se está consolidando y es el siguiente:
        // En el display selector, al cambiar la selección de una opción se lanza el evento
        // change (por fin).
        // Sin embargo NO ES CONVENIENTE PROCESAR ESTE EVENTO por dos razones:
        // 1) La propiedad selected del elemento tocado NO SIEMPRE es correcto sobre todo cuando
        //    pasa de seleccionada a deseleccionada, muestra el valor antes de editar.
        // 2) El cambio de la propiedad selected via JavaScript PROVOCA UN EVENTO CHANGE. Increible pero cierto.
        //    Esto es necesario por ejemplo cuando indirectamente cambia la selección de un componente
        //      select multiple con tramo de selección continuo etc.
        // La solución es IGNORAR el evento change y procesar el evento blur como un change

        // El iPhone 3.0 soluciona el problema 1) pero SIGUE EXISTIENDO el 2)
        // por lo que es aconsejable ignorar el change en este caso también
        
        // En iPhone iOS 6.1 parece que esto ya no es problema
        return false;
//        return DOMUtilHTML.isHTMLSelectMultiple(elem); // Desde 2.2 (antes no se lanzaba el change)
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // Ocurre al menos en elementos de formulario de tipo: HTMLTextArea, HTMLInputElement o HTMLSelectElement.
        // El contexto en el que se ha probado es en edición "inplace" tras una llamada focus() en el elemento editándose.

        // En iPhone el método focus() necesita ser ejecutado en el mismo hilo del evento del usuario (hilo del GUI)
        // por tanto no puede ser ejecutado como una respuesta asíncrona AJAX,
        // pues si es así se genera inmediatamente un evento blur que nos quita el editor inplace

        return true;  // En todos los elementos form
    }

    public Map<String,String[]> getHTMLFormControlsIgnoreZIndex()
    {
        return null;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        // La v2.0 (525.18.1) no soporta SVG
        // La 2.1 ya lo soporta aunque la versión del
        // WebKit no cambia 

        return true;
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // La v2.0 (525.18.1) no soporta SVG
        // La 2.1 ya lo soporta aunque la versión del
        // WebKit no cambia (¿no fue incluído?)
        // Hasta la v3 la ejecución del <script> no se hace

        return false;
    }

}

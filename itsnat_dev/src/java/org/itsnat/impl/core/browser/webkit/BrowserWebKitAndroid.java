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

package org.itsnat.impl.core.browser.webkit;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/*
 * User agents:

    - Android desde Beta r1 (0.6) hasta v1.1 r1
        Beta r1 (0.6): Mozilla/5.0 (Linux; U; Android 0.6; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.0 r2:       Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.1 r1:       Mozilla/5.0 (Linux; U; Android 1.0; en-us; generic) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2
        V1.5 r1:       Mozilla/5.0 (Linux; U; Android 1.5; en-us; sdk Build/CUPCAKE) AppleWebKit/528.5+ (KHTML, like Gecko) Version/3.1.2 Mobile Safari/525.20.1
        v1.6:
        v2.0           Mozilla/5.0 (Linux; U; Android 2.0; en-us; sdk Build/ECLAIR) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17

        No hay versiones intermedias entre 1.1 y 1.5
        El navegador de la 1.1 parece idéntico al de la 1.0 y no hay forma de distinguirlo.
 */

public class BrowserWebKitAndroid extends BrowserWebKit
{
    protected int mainVersion;
    protected int subVersion;

    private static final Map tagNamesIgnoreZIndex = new HashMap();
    static
    {
        // Verificado hasta la v2 incluida:
        tagNamesIgnoreZIndex.put("select",null);
        tagNamesIgnoreZIndex.put("input",new String[]{"text","password","file","checkbox","radio"});
        // El caso de INPUT checkbox y radio es especial, no ignoran el z-index pero podemos llegar a ellos usando el cursor
        // y aunque ignoran los eventos queda feo. Con "button" no ocurre.
        tagNamesIgnoreZIndex.put("textarea",null);
    }

    public BrowserWebKitAndroid(String userAgent)
    {
        super(userAgent,ANDROID);

        // Versión del Android: "Android M.s;"
        try
        {
            int start = userAgent.indexOf("Android ");
            start += "Android ".length();
            int end = start;
            int dot = -1;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == '.') dot = end;
                else if (c == ';') break;
                end++;
            }
            this.mainVersion = Integer.parseInt(userAgent.substring(start,dot));
            this.subVersion =  Integer.parseInt(userAgent.substring(dot + 1,end));
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.mainVersion = 1;
            this.subVersion =  0;
        }
    }

    public boolean isMobile()
    {
        return true;
    }

    public boolean isFilteredCommentsInMarkup()
    {
        return false;
    }

    public boolean hasBeforeUnloadSupportHTML()
    {
        return true;
    }

    public boolean isXHRSyncSupported()
    {
        return true;
    }

    public boolean isXHRPostSupported()
    {
        return true;
    }

    public boolean isSelectMultipleFirstOptionEverSelected()
    {
        // El <select> de la v1.5 es un desastre, aparentemente funciona bien
        // pero deja de mostrar enseguida el diálogo de cambio, es más deja en un estado erróneo
        // el browser, la v1.6 es igual. Quizás los errores de la v1.0 se hayan solucionado
        // pero no podemos ni estudiarlo.
        // En la v2.0 ya no hay este problema

        return (mainVersion < 2);
    }

    public boolean isChangeNotFiredHTMLSelectWithSizeOrMultiple(HTMLSelectElement elem)
    {
        // Nota: elem puede ser null.
        // Hasta la v2 tienen este error (aunque no en el caso de atributo "size")

        if (mainVersion < 2)
        {
            if (elem == null) return true;
            return elem.hasAttribute("multiple"); // Con "size" no pasa de hecho no tiene impacto visual (ni funcional)
        }
        else return false;
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // El contexto en el que se ha probado es en edición "inplace" tras una llamada focus() en el elemento editándose.

        // En Android se vuelve loco y se lanza un evento blur en seguida en cuanto se toca el control
        // y el evento change *no se lanza* aunque se modifique el texto. Sin focus() se comporta normal
        // aunque es preciso "tocarlo" con el cursor para que tenga el focus pues de otra manera una llamada a blur() es ignorada (no se emite el evento blur)
        // El llamar a focus() en el ámbito de una respuesta AJAX síncrona no soluciona este problema.

        // En los TextArea e Input type=text/password el focus() no funciona bien (cajas de texto), en los demás sí
        // el caso "file" no está probado pero lo incluimos. En el resto de controles funciona bien

        // En el caso de <input type=checkbox> el focus es problemático si se usan exclusivamente
        // los cursores, idem en <select> combobox.

        // Conclusión: evitamos el focus normal en todos

        // En la versión 1.5 funciona mejor pero con cursores sigue existiendo el problema
        // La versión 2 no tiene problemas
        return (mainVersion < 2);
    }

    public boolean isAJAXEmptyResponseFails()
    {
        return false;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        return tagNamesIgnoreZIndex;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return true;
    }

    public boolean isTextAddedToInsertedHTMLScriptNotExecuted()
    {
        return false;
    }

    public boolean isOldEventSystem()
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

    public boolean isInsertedSVGScriptNotExecuted()
    {
        // Android no soporta SVG
        return false; // Por poner algo
    }
}

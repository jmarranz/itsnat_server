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
import org.itsnat.impl.core.browser.webkit.BrowserWebKit;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.w3c.dom.html.HTMLElement;

/**
 * Desde el JDE 4.6 (BlackBerry Bold, 9000) pues es la primera versión que soporta AJAX,
 * hasta la JDE 5.x incluida, pues en la 6.0 se cambia el motor y se mete WebKit (MUY diferente)
 *
 * Lanzado oficialmente el 4 noviembre de 2008.
 * Por ahora el JDE 4.7 (BlackBerry Storm, 9530) funciona igual (mismos bugs) que JDE 4.6
 * por lo menos el problema del select multiple/size. Yo creo que el navegador 4.6 y 4.7 son
 * idénticos, de hecho los smartphones (Bold y Storm) salieron a la vez prácticamente
 * (la diferencia es que JDE 4.7 soporta pantallas táctiles).
 *
 * http://na.blackberry.com/eng/deliverables/3850/JavaScript_Reference.pdf
 *
 * User Agents ejemplos:
 * Bold (creo): BlackBerry9000/4.6.0.150 Profile/MIDP-2.0 Configuration/CLDC-1.1 VendorID/-1
   Storm :      BlackBerry9500/4.7.0.41 Profile/MIDP-2.0 Configuration/CLDC-1.1 VendorID/-1
   Storm 2 :    BlackBerry9550/5.0.0.334 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/-1
   Curve 8530:  BlackBerry8530/5.0.0.337 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/-1

 * La BlackBerry (por lo menos la Bold, JDE 4.6) filtra los nodos con espacios
 * y fines de línea en tiempo de carga al igual que los comentarios. Podríamos
 * solucionar "este problema" con la misma técnica que los comentarios pero no
 * es un problema importante pues ItsNat es tolerante a la ausencia de nodos de texto.
 *
 * Versiones previas a JDE 4.6 no merecen la pena, pues probando con JDE 4.5 (8820)
 * aparte de no tener AJAX, resulta que addEventListener no está soportado y métodos tan tontos como createTextNode
 * lanzan una excepción Java (increible). De hecho el soporte de JavaScript parece no actualizado desde la 4.3
 * http://na.blackberry.com/eng/deliverables/1369/BlackBerry_Browser_Version_4.3_Content_Developer_Guide.pdf
 *
 * @author jmarranz
 */
public class BrowserBlackBerryOld extends BrowserW3C
{
    private static final Map tagNamesIgnoreZIndex_v4 = new HashMap();
    private static final Map tagNamesIgnoreZIndex_v5 = new HashMap();
    static
    {
        // En BlackBerry el tema de tagNamesIgnoreZIndex es muy complicado. El único elemento
        // que ignora siempre el z-index es <button>, los demás en principio no,
        // sin embargo he descubierto que si el layer es redimensionado automáticamente (el refresco de las dimensiones),
        // en el caso transparente por lo menos, unas 8 veces, el layer
        // modal deja de capturar los eventos y permite que los elementos
        // por abajo sean pulsados. A lo mejor el número exacto de redimensionamientos
        // sea lo de menos sino el hecho de que algo se degrada.
        // La solución es EVITAR EL REFRESCO DE REDIMENSIONAMIENTO.
        // Testeado esto en JDE 4.6 y 4.7 (Flip,Bold y Storm)

        tagNamesIgnoreZIndex_v4.put("button",null);

        // Sin embargo en JDE 5.0 (Storm 2) parece que ha habido una regresión y todos
        // los elementos son pulsables ya sea el fondo transparente o no.

        tagNamesIgnoreZIndex_v5.put("button",null);
        tagNamesIgnoreZIndex_v5.put("select",null);
        tagNamesIgnoreZIndex_v5.put("input",null);
        tagNamesIgnoreZIndex_v5.put("textarea",null);
        tagNamesIgnoreZIndex_v5.put("a",null);
    }

    protected int mainVersion;
    protected int subVersion; // Por ahora no necesitamos la subversión para ya que estamos la obtenemos
    protected String model;

    /** Creates a new instance of BrowserGecko */
    public BrowserBlackBerryOld(String userAgent)
    {
        super(userAgent);

        this.browserType = BLACKBERRY_OLD;

        // Formato de la versión: BlackBerryXXXX/M.m. ...
        try
        {
            int start = userAgent.indexOf("/"); // La "/" afortunadamente es la primera

            this.model = userAgent.substring("BlackBerry".length(),start);

            start += 1;
            int end = start;
            int dot = -1;
            while(true)
            {
                char c = userAgent.charAt(end);
                if (c == '.')
                {
                   if (dot < 0) dot = end; // Primer punto
                   else break; // Segundo punto (delimitador de la subversión)
                }
                end++;
            }
            this.mainVersion = Integer.parseInt(userAgent.substring(start,dot));
            this.subVersion =  Integer.parseInt(userAgent.substring(dot + 1,end));
        }
        catch(Exception ex) // Caso de user agent de formato desconocido
        {
            this.mainVersion = 4;
            this.subVersion =  0;
            this.model = "";
        }

        this.browserSubType = mainVersion; // Por ahora el mainVersion viene a ser el browser subType
    }

    public static boolean isBlackBerryOld(String userAgent)
    {
        // A partir de JDE 6.0 se usa el motor WebKit que no tiene nada que ver con los navegadores
        // anteriores, el problema es que la versión WebKit contiene la palabra BlackBerry
        return (userAgent.indexOf("BlackBerry") != -1) &&
                !BrowserWebKit.isWebKit(userAgent);
    }

    public boolean isMobile()
    {
        return true;
    }

    @Override
    public boolean hasBeforeUnloadSupport(ItsNatStfulDocumentImpl itsNatDoc)
    {
        return false; // Tampoco en HTML
    }

    public boolean isReferrerReferenceStrong()
    {
        return true;
    }

    public boolean isCachedBackForward()
    {
        // Cuando volvemos a través de escape el retorno es instantáneo.
        // Además el unload no se lanza
        return true;
    }

    public boolean isCachedBackForwardExecutedScripts()
    {
        // Cuando volvemos via link se ejecutan tanto los scripts como el load
        // Cuando volvemos a través de escape el retorno es instantáneo.
        return true;
    }

    public boolean isDOMContentLoadedSupported()
    {
        return true;
    }

    public boolean isBlurBeforeChangeEvent(HTMLElement formElem)
    {
        return DOMUtilHTML.isHTMLTextAreaOrInputTextBox(formElem); // También en v5
    }

    public boolean isFocusOrBlurMethodWrong(String methodName,HTMLElement formElem)
    {
        // El focus() es ignorado y por tanto un posterior blur()
        return true;
    }

    public Map getHTMLFormControlsIgnoreZIndex()
    {
        if (this.mainVersion <= 4)
            return tagNamesIgnoreZIndex_v4;
        else
            return tagNamesIgnoreZIndex_v5;
    }

    public boolean hasHTMLCSSOpacity()
    {
        return false;
    }

    public boolean canNativelyRenderOtherNSInXHTMLDoc()
    {
        return false; // Soporta SVG pero sin script. Embebido en X/HTML yo creo que tampoco
    }

    public boolean isInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, soporta SVG pero sin scripts
    }

    public boolean isTextAddedToInsertedSVGScriptNotExecuted()
    {
        return false; // Por poner algo, soporta SVG pero sin scripts
    }

    public boolean isClientWindowEventTarget()
    {
        return true;
    }

    public boolean isHTMLSelectMultipleOrWithSizeBuggy()
    {
        // La versión 5 aparentemente parece que ha resuelto esto
        // PERO NO ES ASÍ, por ejemplo el simulador del 9550 (Storm 2 JDE 5.0.0.334)
        // funciona bien pero el del Curve 8530 (JDE 5.0.0.337) en teoría
        // posterior, NO funciona bien.
        // Conclusión, sólo sabemos que funciona bien en Storm 2 (9550 JDE 5.0)
        // Hay más modelos con JDE 5.0 pero hay que estudiarlos todos.
        // De todas maneras no hay problema pues si funciona bien la
        // corrección del fallo no lo estropea.
        return !model.equals("9550");
    }

    public boolean isHTMLInputFileValueBuggy()
    {
        // La simple presencia del atributo value enloquece al BlackBerry
        // Solucionado en la v5
        return (mainVersion <= 4);
    }
}

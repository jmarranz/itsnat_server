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

package org.itsnat.impl.core.resp.shared.html;

import org.itsnat.impl.core.browser.opera.BrowserOpera9Mobile;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocOpera9MobileImpl extends ResponseDelegateHTMLLoadDocOpera9Impl
{
    public ResponseDelegateHTMLLoadDocOpera9MobileImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    public boolean isDelayedInit()
    {
        return true;
    }

    public void dispatchRequestListeners()
    {
        fixOnLoad();

        super.dispatchRequestListeners();
    }

    protected void fixOnLoad()
    {
        // Problema del onload, este problema es similar al de Opera Mini, si no se hace esto el addEventListener estándar no funciona,
        // sin embargo sólo es necesario para el evento "load". Lo añadimos
        // antes del código de inicialización que es ejecutado tras procesar
        // el evento DOMContentLoaded porque ha de definirse antes de que se procese el evento DOMContentLoaded
        // También podría ponerse este código como código FixDOM
        String code = "if (!document.body.hasAttribute(\"onload\")) document.body.setAttribute(\"onload\",\"\");\n";
        addFixDOMCodeToSend(code);
    }

    protected String getInitDocumentAndLoadJSCode(final int prevScriptsToRemove)
    {
        // Delayed Init:

        // En Opera Mobile 9.5 (beta) el DOM no es correcto hasta
        // que se carga todo.
        // Tenemos dos opciones: usar un setTimeout con intervalo 0 o
        // delegamos al evento DOMContentLoaded la iniciación.

        // El problema del setTimeout es que no tenemos la garantía de que
        // se ejecute antes del DOMContentLoaded y del "load", de hecho parece que se
        // ejectuta después en el caso de Opera Mobile 9.5 beta, hay que tener en cuenta
        // que el listener ejecuta el código de inicialización que a su vez posiblemente
        // registre listeners para DOMContentLoaded y load. En el caso de usar DOMContentLoaded
        // tendremos el problema de que los listeners registrados después no son nunca ejecutados
        // y el framework necesita este evento.
        // Solución: usar DOMContentLoaded y simular el evento DOMContentLoaded con JavaScript para que llegue al servidor.

        // Curiosamente no he encontrado la necesidad de hacer esto en SVG

        // El Opera Mini hubo una época en el que esto era necesario también,
        // pues no podíamos eliminar del árbol el <script> que contiene el código de eliminación
        // más exactamente: sí se podía, pero era la última operación del script (terminaba repentinamente).
        // Afortunadamente fue temporal y ahora no es necesario.


        StringBuffer code = new StringBuffer();

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        BrowserOpera9Mobile browser = (BrowserOpera9Mobile)clientDoc.getBrowser();
        boolean domInvalidLoading = browser.isDOMNotValidOnLoading();

        if (domInvalidLoading)
        {
        code.append( "\n" );
        code.append( "var initListener = function ()\n" );
        code.append( "{\n" );
        code.append( "  window.removeEventListener(\"DOMContentLoaded\",arguments.callee,false);\n" ); // Para evitar que se llame recursivamente
        }

        code.append( super.getInitDocumentAndLoadJSCode(prevScriptsToRemove) );

        if (domInvalidLoading)
        {
        code.append( "  var evt = document.createEvent(\"Events\");\n" ); 
        code.append( "  evt.initEvent(\"DOMContentLoaded\",false,false);\n" );
        code.append( "  window.dispatchEvent(evt);\n" );
        code.append( "};\n" );
        code.append( "window.addEventListener(\"DOMContentLoaded\",initListener,false);\n" );
        code.append( "initListener = null; \n" );
        }

        return code.toString();
    }
}


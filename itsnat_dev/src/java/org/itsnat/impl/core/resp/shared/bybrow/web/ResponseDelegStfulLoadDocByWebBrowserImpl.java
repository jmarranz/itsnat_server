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
package org.itsnat.impl.core.resp.shared.bybrow.web;

import java.util.LinkedList;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserW3C;
import org.itsnat.impl.core.resp.shared.ResponseDelegateStfulWebLoadDocImpl;


/**
 *
 * @author jmarranz
 */
public abstract class ResponseDelegStfulLoadDocByWebBrowserImpl extends ResponseDelegStfulLoadDocByBrowserImpl
{
    public ResponseDelegStfulLoadDocByWebBrowserImpl(ResponseDelegateStfulWebLoadDocImpl parent)
    {
        super(parent);
    }

    public static ResponseDelegStfulLoadDocByWebBrowserImpl createResponseDelegStfulLoadDocByWebBrowser(ResponseDelegateStfulWebLoadDocImpl parent)
    {
        Browser browser = parent.getClientDocumentStful().getBrowser();
        if (browser instanceof BrowserW3C)
            return ResponseDelegStfulLoadDocByBW3CImpl.createResponseDelegStfulLoadDocByBW3C(parent);
        else
            return new ResponseDelegStfulLoadDocByBMSIEOldImpl(parent);
    }    
    
    public ResponseDelegateStfulWebLoadDocImpl getResponseDelegateStfulWebLoadDoc()
    {
        return (ResponseDelegateStfulWebLoadDocImpl)parent;
    }
    

    public boolean getRevertJSChanges()
    {
        // Revertir cambios hechos a través de JavaScript y que el navegador ha podido memorizar para el autofill, por ejemplo la etiqueta de un botón que cambia
        // Se redefine en varios casos en los que hay auto-form-fill
        // Llamar sólo una vez (por el caso de BrowserOperaDesktop)

        // Caso de revertir las acciones del usuario en
        // navegadores sin "autofill no desactivable": FireFox, MSIE etc.
        // (pues al revertir el autofill solucionamos ya el problema de las acciones del usuario)
        // Esto no quiere decir que no tengan autofill sino que éste no actúa si la página
        // es recargada desde el servidor (pues suponen acertadamente que la página puede haber cambiado).

        // En tiempo de carga de la página, antes de que se ejecute el script de iniciación de la página,
        // el usuario tiene la oportunidad de tocar los formularios cambiando su estado, dicho
        // nuevo estado no se propaga al servidor porque los formularios todavía no
        // tienen listeners asociados porque todavía no se ha ejecutado el script de la página.
        // De esta manera el usuario puede tocar lo que quiera pues por una parte
        // los botones etc no hacen nada pues no hay listeners, y por otro los checkboxes
        // radio buttons y select son reinicializados después.
        // Esto lo hacemos DESPUES del proceso de la página porque en "fast load"
        // los cambios se manifiestan sólo en el árbol DOM y no se envían por JavaScript
        // por lo que el usuario ya tiene el árbol DOM final de la página inicial antes de que se ejecute el código
        // JavaScript de iniciación de la página (que fundamentalmente añade listeners)
        // En caso de no fast load probablemente no sería necesario pues lo normal es que los
        // elementos del formulario estén o vacíos o con el elemento patrón por lo que el usuario no puede tocar mucho
        // y lo que toque será sobreescrito al ejecutarse el JavaScript inicial, pero por si acaso
        // ejecutamos el forzado de los formularios en su estado final, sobre todo también por el caso de los controles markup driven
        // en donde el markup inicial son los datos iniciales.
        // No consideramos los controles que no pueden ser cambiados por el usuario (el value de botones etc).

        // En el caso de Opera 9 el auto-fill existe pero no actúa en tiempo
        // de carga sino tras el onload.

        return false;
    }    
    
    public abstract String getJSMethodInitName(); 
    public abstract void fillFrameworkScriptFileNamesOfBrowser(LinkedList<String> list);    
}

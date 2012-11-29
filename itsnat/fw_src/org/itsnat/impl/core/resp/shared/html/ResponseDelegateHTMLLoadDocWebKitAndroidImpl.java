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

import org.itsnat.impl.core.browser.webkit.BrowserWebKitAndroid;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.domutil.DOMUtilHTML;
import org.itsnat.impl.core.resp.ResponseLoadStfulDocumentValid;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ResponseDelegateHTMLLoadDocWebKitAndroidImpl extends ResponseDelegateHTMLLoadDocWebKitImpl
{
    public ResponseDelegateHTMLLoadDocWebKitAndroidImpl(ResponseLoadStfulDocumentValid response)
    {
        super(response);
    }

    protected void rewriteClientUIControlProperties(Element elem,boolean revertJSChanges,StringBuffer code)
    {
        super.rewriteClientUIControlProperties(elem,revertJSChanges,code);

        ClientDocumentStfulImpl clientDoc = getClientDocumentStful();
        BrowserWebKitAndroid browser = (BrowserWebKitAndroid)clientDoc.getBrowser();
        if (browser.isSelectMultipleFirstOptionEverSelected() &&
            DOMUtilHTML.isHTMLSelectMultiple(elem))
        {
            // El Android v1 r2 (y anteriores) tiene el mismo error que tuvo el iPhone
            // y es que el primer <option> se selecciona por defecto en select multiple (el atributo size no influye),
            // tanto tras cargar la página como al cambiar las option via JavaScript.
            // En el caso de carga no hay problema pues en carga el select ya está renderizado visualmente
            // antes de que se ejecute el código de ItsNat que evita los cambios del usario en el proceso de carga
            // esto indirectamente soluciona este error al poner la propiedad selected al valor apropiado.
            // Aún así hay un error visual, y es que el select visualmente no se entera de que el primer elemento
            // ya no está seleccionado. Usamos la técnica de re-insertar el select que soluciona
            // este problema al obligar a renderizar de nuevo el control.

            HTMLSelectElement select = (HTMLSelectElement)elem;

            String selectRef = clientDoc.getNodeReference(select,true,true);
            code.append( "var elem = " + selectRef + ";\n" );

            // Evitamos usar el nombre "parent" porque es una propiedad de "window" que suele ser el contexto
            code.append( "var parentNode = elem.parentNode;\n" );
            code.append( "var elemClone = elem.cloneNode(true);\n" );
            code.append( "parentNode.replaceChild(elemClone, elem);\n" );
            code.append( "parentNode.replaceChild(elem, elemClone);\n" );
        }
    }
}

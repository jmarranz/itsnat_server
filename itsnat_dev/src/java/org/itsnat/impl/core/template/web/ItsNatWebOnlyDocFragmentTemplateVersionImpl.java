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

package org.itsnat.impl.core.template.web;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.impl.core.scriptren.jsren.node.JSRenderElementImpl;
import org.itsnat.impl.core.scriptren.jsren.node.html.JSRenderHTMLElementAllBrowsersImpl;
import org.itsnat.impl.core.scriptren.jsren.node.otherns.JSRenderOtherNSElementW3CDefaultImpl;
import org.itsnat.impl.core.template.ItsNatDocFragmentTemplateVersionImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLElement;
import org.xml.sax.InputSource;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatWebOnlyDocFragmentTemplateVersionImpl extends ItsNatDocFragmentTemplateVersionImpl
{
    /**
     * Creates a new instance of ItsNatXMLDocFragmentTemplateVersionImpl
     */
    public ItsNatWebOnlyDocFragmentTemplateVersionImpl(ItsNatWebOnlyDocFragmentTemplateImpl docTemplate,InputSource source,long timeStamp,ItsNatServletRequest request,ItsNatServletResponse response)
    {
        super(docTemplate,source,timeStamp,request,response);
    }

    @Override
    protected boolean isElementValidForCaching(Element elem)
    {
        if (!super.isElementValidForCaching(elem))
            return false; // Ya arriba se dice que no.

        // La razón de esto es la siguiente: un fragmento puede insertarse
        // en un documento después de estar cargado por ejemplo durante un evento AJAX
        // por tanto la inserción en el cliente del código serializado de un elemento
        // cacheado ha de hacerse necesariamente via innerHTML o setInnerXML.

        // El fragmento podría ser insertado en el documento en la fase de carga
        // ya sea via <include> estático o via DOM en caso de fast load (el árbol no ha sido serializado todavía)
        // en ese caso no se utiliza innerHTML/setInnerXML pero no lo sabemos a priori, por tanto
        // contemplamos todos los escenarios.
        // Otra opción alternativa a innerHTML/setInnerXML sería reconstruir el DOM del nodo cacheado y convertirlo
        // en instrucciones DOM JavaScript pero esto es mucho
        // más lento y necesita de parseado parcial.
        // Por tanto NO podemos cachear el interior de un elemento que no puede ser enviado su contenido al cliente
        // e insertado por JavaScript usando innerHTML o las alternativas XML (aunque sí sus hijos que a lo mejor permiten innerHTML
        // pero eso ha de decidirse para cada hijo concreto).
        // Tampoco sabemos qué navegador/es usará el template por tanto consideramos una mezcla de todas
        // las restricciones de los navegadores soportados

        // En el caso de que este sea un fragmento XML (namespace cuyos documentos no tienen estado)
        // el documento destino sí puede tener estado (MUY RARO pero posible), es decir
        // puede ser un X/HTML, SVG o XUL. Por tanto lo anterior también se aplica.

        JSRenderElementImpl render;
        if (elem instanceof HTMLElement)
        {
            // Si el nodo es HTML suponemos que el navegador destino es el especial
            // común denominador de todos.
            render = JSRenderHTMLElementAllBrowsersImpl.SINGLETON;
        }
        else
        {
            // Tanto W3C como MSIE admiten inserción de markup usando DOMRender (parseXML en ASV) y loadXML
            // respectivamente. 
            // El criterio tanto en W3C como en MSIE por ahora es el mismo por lo que
            // usamos el SINGLETON de W3C
            render = JSRenderOtherNSElementW3CDefaultImpl.SINGLETON;
        }

        return render.canInsertAllChildrenAsMarkup(elem,this);
    }    
}

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

package org.itsnat.impl.core.domutil;

import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 * En la creación del árbol DOM en tiempo de parseo conseguimos que 
 * se creen elementos HTMLElement no sólo en HTML sino también en XHTML,
 * pero no así en otros namespaces tal y como SVG y XUL. En SVG no
 * hay problema pues los navegadores hasta ahora no admiten elementos
 * HTML embebidos pero en XUL sí.
 *
 * En el futuro quizás consigamos parsear los elementos HTML como HTMLElement
 * entonces el objeto ElementGroupManagerImpl deberá poder gestionar cualquier
 * tipo de namespace soportado independientemente de si el documento
 * es HTML, SVG, XUL, MathML etc, es decir, el código de esta clase subirlo a la genérica.
 * De la manera actual queda más ordenadito y siempre se puede refactorizar
 * fácilmente en el futuro sin cambiar la API externa
 *
 * Por ello es *importante* no hacer pública una interface HTMLElementGroupManager,
 * si es necesario añadir métodos dependientes de elementos HTML se debería hacer en ElementGroupManager,
 * o bien en interfaces HTMLElementGroupManager, SVGElementGroupManager etc pero que
 * el objeto ElementGroupManager pueda hacerse cast para cualquier namespace.
 *
 * @author jmarranz
 */
public class HTMLElementGroupManagerImpl extends ElementGroupManagerImpl
{
    /** Creates a new instance of HTMLElementGroupManagerImpl */
    public HTMLElementGroupManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public ElementListFree createElementListFree(Element parentElement,boolean master)
    {
        if (master)
        {
            return super.createElementListFree(parentElement,master);
        }
        else
        {
            // Usamos en modo no master las HTMLCollection puesto que de acuerdo al estándar,
            // están preparadas para detectar cambios realizados en medio de su uso,
            // no es esperable un rendimiento mejor que la solución "manual" pero por si acaso
            // la implementación DOM fuera capaz de mejorar el rendimiento.
            // Si vemos que el uso de las HTMLCollection falla o lo que sea
            // simplemente llamar a:  return super.createElementListFree(parentElement,false);
            // en todos los casos

            if (parentElement instanceof HTMLSelectElement) 
            {
                HTMLSelectElement selectElem = (HTMLSelectElement)parentElement;
                return new HTMLCollectionAsElementListImpl(selectElem,selectElem.getOptions(),getItsNatDocumentImpl());
            }
            else if (parentElement instanceof HTMLTableRowElement)
            {
                HTMLTableRowElement rowElem = (HTMLTableRowElement)parentElement;
                return new HTMLCollectionAsElementListImpl(rowElem,rowElem.getCells(),getItsNatDocumentImpl());
            }
            else if (parentElement instanceof HTMLTableSectionElement)
            {
                HTMLTableSectionElement sectionElem = (HTMLTableSectionElement)parentElement;
                return new HTMLCollectionAsElementListImpl(sectionElem,sectionElem.getRows(),getItsNatDocumentImpl());
            }
            else
                return super.createElementListFree(parentElement,false);
        }
    }
}

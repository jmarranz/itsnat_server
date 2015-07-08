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

package org.itsnat.impl.core.domimpl;

import org.itsnat.impl.core.doc.ElementDocContainerWrapperImpl;

/**
  Elementos que implementan ElementDocContainer:
 * <object>, <iframe>, <embed> y <applet> por ahora todos HTML
 * sin embargo en el futuro se podría incluir algún elemento contenedor
 * por ejemplo de XUL.
 *
  En el caso de <embed> este tag es antiguo y no estándar
  pero soportado por los principales navegadores, el problema es que
  no disponemos de una interface que devuelva getContentDocument(),
  embed es vital para que funcione completamente bien el Adobe SVG Viewer
  pues via <iframe> no podemos acceder desde el padre al hijo via JavaScript.
  El problema del getContentDocument() lo solucionamos con una interface
  especial de ItsNat.

  Curiosamente <embed> está siendo rescatado en HTML 5

  http://msdn.microsoft.com/en-us/library/dd347080%28VS.85%29.aspx
  http://search.cpan.org/~slanning/Mozilla-DOM-0.21/lib/Mozilla/DOM/HTMLEmbedElement.pod
  http://developer.apple.com/mac/library/documentation/AppleApplications/Reference/WebKitDOMRef/HTMLEmbedElement_idl/Classes/HTMLEmbedElement/index.html#//apple_ref/idl/cl/HTMLEmbedElement
  http://lists.w3.org/Archives/Public/public-html-bugzilla/2009Aug/0207.html
  http://www.whatwg.org/specs/web-apps/current-work/#the-embed-element

  Otro caso es el <iframe> de XUL (https://developer.mozilla.org/en/XUL/elem)
  que tampoco tiene una interface que devuelva getContentDocument(),
  siempre es posible usar en XUL el iframe de XHTML (de hecho la implementación por debajo debe ser casi idéntica)
  y en el caso <embed> puede usarse <object> e <iframe> alternativamente.

  En el caso de <applet> el "URL" se ha de definir en un <param src="" value="...">

 * @author jmarranz
 */
public interface ElementDocContainer
{
    // Nunca será nulo el retorno
    public ElementDocContainerWrapperImpl getElementDocContainerWrapper();
}

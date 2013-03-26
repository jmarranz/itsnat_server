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

package org.itsnat.impl.core.jsren.dom.node;

import java.lang.ref.WeakReference;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class InnerMarkupCodeImpl
{
    protected JSRenderElementImpl jsRender;
    protected WeakReference<Element> parentNodeRef; // No usamos una referencia normal pues supondria sujetar nodos inútilmente, pues cuando es usada todavía forma parte del documento y está sujeta por referencias normales, si se pierde no pasa nada porque devuelva null, no se usa para renderizar sólo para añadir nuevos trozos (implica que sigue referenciado)
    protected String parentNodeJSLocator;
    protected boolean useNodeLocation;
    protected StringBuilder innerMarkup = new StringBuilder();

    public InnerMarkupCodeImpl(JSRenderElementImpl jsRender,Element parentNode,String parentNodeJSLocator,boolean useNodeLocation,String firstInnerMarkup)
    {
        this.jsRender = jsRender;
        this.parentNodeRef = new WeakReference<Element>(parentNode);
        this.parentNodeJSLocator = parentNodeJSLocator;
        this.useNodeLocation = useNodeLocation;        
        innerMarkup.append(firstInnerMarkup);
    }

    public Element getParentNode()
    {
        return parentNodeRef.get();
    }

    public String getParentNodeJSLocator()
    {
        return parentNodeJSLocator;
    }

    public boolean isUseNodeLocation()
    {
        return useNodeLocation;
    }

    public void addInnerMarkup(String newInnerMarkup)
    {
        innerMarkup.append(newInnerMarkup);
    }

    public String getInnerMarkup()
    {
        return innerMarkup.toString();
    }

    @Override
    public String toString()
    {
        throw new ItsNatException("INTERNAL ERROR");
    }
    
    public String render(ClientDocumentImpl clientDoc)
    {
        return jsRender.getAppendChildrenCodeAsMarkupSentence(this,(ClientDocumentStfulImpl)clientDoc);
    }
}

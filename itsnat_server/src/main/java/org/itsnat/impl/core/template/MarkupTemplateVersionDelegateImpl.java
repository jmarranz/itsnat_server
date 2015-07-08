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

package org.itsnat.impl.core.template;


import java.io.Writer;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class MarkupTemplateVersionDelegateImpl 
{
    protected MarkupTemplateVersionImpl parent;
    /**
     * Creates a new instance of MarkupTemplateVersionDelegateImpl
     */
    public MarkupTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        this.parent = parent;
    }

    public MarkupTemplateVersionImpl getMarkupTemplateVersion()
    {
        return parent;
    }

    public void serializeNode(Node node,Writer out,DOMRenderImpl nodeRender)
    {
        // Es derivada en el caso de HTML
        nodeRender.serializeNode(node,out);
    }

    public void normalizeDocument(Document doc)
    {
        // Derivar si hay que hacer más cosas (caso HTML)
    }

    public boolean declaredAsComponent(Element elem)
    {
        return ItsNatDocComponentManagerImpl.declaredWithCompTypeAttribute(elem);
    }
}

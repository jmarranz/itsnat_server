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

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroup;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementGroupImpl extends ItsNatUserDataImpl implements ElementGroup
{
    protected ItsNatDocumentImpl itsNatDoc;

    /**
     * Creates a new instance of ElementGroupImpl
     */
    public ElementGroupImpl(ItsNatDocumentImpl itsNatDoc)
    {
        super(false);

        this.itsNatDoc = itsNatDoc;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return itsNatDoc;
    }

    public static void restorePatternMarkupWhenRendering(Element parent,DocumentFragment pattern)
    {
        if (pattern == null) return; // por si acaso no se ha podido obtener un pattern
        DOMUtilInternal.removeAllChildren(parent);
        parent.appendChild(pattern.cloneNode(true));
    }
}

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

package org.itsnat.impl.comp;

import org.itsnat.comp.ItsNatHTMLInput;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.scriptren.jsren.JSRenderMethodCallImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLInputImpl extends ItsNatHTMLFormCompValueBasedImpl implements ItsNatHTMLInput
{

    /**
     * Creates a new instance of ItsNatHTMLInputImpl
     */
    public ItsNatHTMLInputImpl(HTMLInputElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        DOMUtilInternal.setAttribute(getHTMLInputElement(),"type",getExpectedType()); // para asegurarnos (actualmente sobra, se hace también en el modelo interno)
    }

    public HTMLInputElement getHTMLInputElement()
    {
        return (HTMLInputElement)node;
    }

    public HTMLFormElement getHTMLFormElement()
    {
        return getHTMLInputElement().getForm();
    }

    public abstract String getExpectedType();

    public void blur()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getElement(),"blur",itsNatDoc);
    }

    public void focus()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getElement(),"focus",itsNatDoc);
    }

    public void click()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"click",itsNatDoc);
    }

    public void select()
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)getItsNatDocumentImpl();
        JSRenderMethodCallImpl.addCallMethodHTMLFormControlCode(getHTMLElement(),"select",itsNatDoc);
    }

    public Node createDefaultNode()
    {
        HTMLInputElement elem = (HTMLInputElement)getItsNatDocument().getDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"input");
        DOMUtilInternal.setAttribute(elem,"type",getExpectedType());
        return elem;
    }

    public boolean isEnabled()
    {
        HTMLInputElement element = getHTMLInputElement();
        return !element.getDisabled();
    }

    public void setEnabled(boolean b)
    {
        HTMLInputElement element = getHTMLInputElement();
        element.setDisabled( ! b );
    }
}

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

package org.itsnat.impl.comp.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.itsnat.impl.comp.ItsNatHTMLFormCompMarkupDrivenUtil;
import org.itsnat.impl.comp.ItsNatHTMLFormComponentImpl;
import javax.swing.text.Document;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLFormTextCompMarkupDrivenUtil extends ItsNatHTMLFormCompMarkupDrivenUtil implements EventListener,Serializable  // Yo creo que el Serializable no se necesita porque el EventListener es para mutation events internos
{
    public ItsNatHTMLFormTextCompMarkupDrivenUtil(ItsNatHTMLFormTextComponentInternal comp)
    {
        super((ItsNatHTMLFormComponentImpl)comp);
    }

    public static ItsNatHTMLFormTextCompMarkupDrivenUtil initMarkupDriven(ItsNatHTMLFormTextComponentInternal comp)
    {
        return setMarkupDriven(comp,null,isMarkupDrivenInitial((ItsNatHTMLFormComponentImpl)comp));
    }

    public static ItsNatHTMLFormTextCompMarkupDrivenUtil setMarkupDriven(ItsNatHTMLFormTextComponentInternal comp,ItsNatHTMLFormTextCompMarkupDrivenUtil obj,boolean markupDriven)
    {
        if (markupDriven && (obj == null))
        {
            return new ItsNatHTMLFormTextCompMarkupDrivenUtil(comp);
        }
        else if (!markupDriven && (obj != null))
        {
            obj.dispose();
            return null;
        }
        else
            return obj;
    }

    public ItsNatHTMLFormTextComponentInternal getItsNatHTMLFormTextComponentInternal()
    {
        return (ItsNatHTMLFormTextComponentInternal)comp;
    }

    public void preSetDefaultDataModel(Object dataModel)
    {
        Element elem = comp.getElement();
        String str = elem.getAttribute("value");
        ItsNatHTMLFormTextCompSharedImpl.setText((Document)dataModel, str);
    }

    public void initialSyncUIWithDataModel()
    {
        Element elem = comp.getElement();
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false);
    }

    public void dispose()
    {
        Element elem = comp.getElement();
        ((ItsNatNodeInternal)elem).removeEventListenerInternal("DOMAttrModified",this, false);
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        // Hay que tener en cuenta que los event listeners internos no se serializan
        if (!comp.isDisposed())
        {
            Element elem = comp.getElement();
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false);
        }
    }

    public void handleEvent(Event evt)
    {
        // Ojo, se espera un evento interno (mutation event) no remoto.
        if (comp.isServerUpdatingFromClient())
            return;

        MutationEvent mutEvent = (MutationEvent)evt;
        Attr attr = (Attr)mutEvent.getRelatedNode();
        if (!attr.getName().equals("value")) return;

        Document dataModel = getItsNatHTMLFormTextComponentInternal().getDocument();
        Element elem = comp.getElement();
        String str = elem.getAttribute("value"); // En el caso MutationEvent.REMOVAL devolverá la cadena vacía
        boolean uiEnabled = comp.isUIEnabled();
        comp.setUIEnabled(false);
        try
        {
            ItsNatHTMLFormTextCompSharedImpl.setText(dataModel, str);
        }
        finally
        {
            comp.setUIEnabled(uiEnabled); // Restaura
        }
    }
}

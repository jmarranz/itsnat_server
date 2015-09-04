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

package org.itsnat.impl.comp.button.toggle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.impl.comp.ItsNatHTMLFormCompMarkupDrivenUtil;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.itsnat.impl.core.listener.EventListenerSerializableInternal;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputButtonToggleMarkupDrivenUtil extends ItsNatHTMLFormCompMarkupDrivenUtil implements EventListenerSerializableInternal  // Yo creo que el Serializable no se necesita porque el EventListener es para mutation events internos
{
    public ItsNatHTMLInputButtonToggleMarkupDrivenUtil(ItsNatHTMLInputButtonToggleImpl comp)
    {
        super(comp);
    }

    public static ItsNatHTMLInputButtonToggleMarkupDrivenUtil initMarkupDriven(ItsNatHTMLInputButtonToggleImpl comp)
    {
        return setMarkupDriven(comp,null,isMarkupDrivenInitial(comp));
    }

    public static ItsNatHTMLInputButtonToggleMarkupDrivenUtil setMarkupDriven(ItsNatHTMLInputButtonToggleImpl comp,ItsNatHTMLInputButtonToggleMarkupDrivenUtil obj,boolean markupDriven)
    {
        if (markupDriven && (obj == null))
        {
            return new ItsNatHTMLInputButtonToggleMarkupDrivenUtil(comp);
        }
        else if (!markupDriven && (obj != null))
        {
            obj.dispose();
            return null;
        }
        else
            return obj;
    }

    public ItsNatHTMLInputButtonToggleImpl getItsNatHTMLInputButtonToggle()
    {
        return (ItsNatHTMLInputButtonToggleImpl)comp;
    }

    @Override
    public void preSetDefaultDataModel(Object dataModel)
    {
        HTMLInputElement elem = getItsNatHTMLInputButtonToggle().getHTMLInputElement();
        ToggleButtonModel buttonModel = (ToggleButtonModel)dataModel;

        boolean checked = elem.getChecked();
        buttonModel.setSelected(checked);
    }

    @Override
    public void initialSyncUIWithDataModel()
    {
        Element elem = comp.getElement();
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMAttrModified",this, false);
    }

    @Override
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

    @Override
    public void handleEvent(Event evt)
    {
        if (comp.isServerUpdatingFromClient())
            return;

        MutationEvent mutEvent = (MutationEvent)evt;
        Attr attr = (Attr)mutEvent.getRelatedNode();
        if (!attr.getName().equals("checked")) return;

        boolean checked = false;
        int changeType = mutEvent.getAttrChange();
        switch(changeType)
        {
            case MutationEvent.ADDITION:
            case MutationEvent.MODIFICATION: // Caso raro
                checked = true;
                break;
            case MutationEvent.REMOVAL:
                checked = false;
                break;
        }

        ItsNatHTMLInputButtonToggleImpl comp = getItsNatHTMLInputButtonToggle();
        ToggleButtonModel dataModel = comp.getToggleButtonModel();
        if (dataModel.isSelected() != checked)
        {
            boolean uiEnabled = comp.isUIEnabled();
            comp.setUIEnabled(false);
            try
            {
                dataModel.setSelected(checked);
            }
            finally
            {
                comp.setUIEnabled(uiEnabled); // Restaura
            }
        }
    }
}

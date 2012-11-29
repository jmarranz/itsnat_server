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

package org.itsnat.impl.comp.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.itsnat.impl.comp.ItsNatHTMLFormCompMarkupDrivenUtil;
import javax.swing.ListModel;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.core.domimpl.ItsNatNodeInternal;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLSelectMarkupDrivenUtil extends ItsNatHTMLFormCompMarkupDrivenUtil implements EventListener,Serializable  // Yo creo que el Serializable no se necesita porque el EventListener es para mutation events internos
{
    public ItsNatHTMLSelectMarkupDrivenUtil(ItsNatHTMLSelectImpl comp)
    {
        super(comp);
    }

    public static ItsNatHTMLSelectMarkupDrivenUtil initMarkupDriven(ItsNatHTMLSelectImpl comp)
    {
        return setMarkupDriven(comp,null,isMarkupDrivenInitial(comp));
    }

    public static ItsNatHTMLSelectMarkupDrivenUtil setMarkupDriven(ItsNatHTMLSelectImpl comp,ItsNatHTMLSelectMarkupDrivenUtil obj,boolean markupDriven)
    {
        if (markupDriven && (obj == null))
        {
            if (comp instanceof ItsNatHTMLSelectComboBoxImpl)
                return new ItsNatHTMLSelectComboBoxMarkupDrivenUtil((ItsNatHTMLSelectComboBoxImpl)comp);
            else
                return new ItsNatHTMLSelectMultMarkupDrivenUtil((ItsNatHTMLSelectMultImpl)comp);
        }
        else if (!markupDriven && (obj != null))
        {
            obj.dispose();
            return null;
        }
        else
            return obj;
    }

    public ItsNatHTMLSelectImpl getItsNatHTMLSelect()
    {
        return (ItsNatHTMLSelectImpl)comp;
    }

    public abstract void addDataModelItem(String item,ListModel dataModel);
    public abstract void addDataModelItem(int index,String item,ListModel dataModel);
    public abstract void removeDataModelItem(int index,ListModel dataModel);

    public abstract void selectItem(int index,boolean selected);

    public static String getOptionText(HTMLOptionElement option)
    {
        Text text = (Text)option.getFirstChild();
        String item = null;
        if (text != null) item = text.getData();
        return item;
    }

    public void preSetDefaultDataModel(Object dataModel)
    {
        HTMLSelectElement select = getItsNatHTMLSelect().getHTMLSelectElement();
        // El uso de HTMLSelectElement.getOptions() es terriblemente ineficiente.
        // No toleramos <optgroup>
        HTMLOptionElement option = (HTMLOptionElement)ItsNatTreeWalker.getFirstChildElement(select);
        while(option != null)
        {
            String item = getOptionText(option);
            addDataModelItem(item,(ListModel)dataModel);
            option = (HTMLOptionElement)ItsNatTreeWalker.getNextSiblingElement(option);
        }
    }

    public void initialSyncUIWithDataModel()
    {
        HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMNodeInserted",this, false); // Para nuevos elementos opción
        ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMNodeRemoved",this, false); // Para elementos opción eliminados
    }

    public void dispose()
    {
        HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
        ((ItsNatNodeInternal)elem).removeEventListenerInternal("DOMNodeInserted",this, false);
        ((ItsNatNodeInternal)elem).removeEventListenerInternal("DOMNodeRemoved",this, false);
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
            HTMLSelectElement elem = getItsNatHTMLSelect().getHTMLSelectElement();
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMNodeInserted",this, false); // Para nuevos elementos opción
            ((ItsNatNodeInternal)elem).addEventListenerInternal("DOMNodeRemoved",this, false); // Para elementos opción eliminados
        }
    }

    public void handleEvent(Event evt)
    {
        ItsNatHTMLSelectImpl comp = getItsNatHTMLSelect();
//        if (comp.isServerUpdatingFromClient())
//            return;

        MutationEvent mutEvent = (MutationEvent)evt;
        String type = evt.getType();
        if (type.equals("DOMAttrModified"))
        {
            Attr attr = (Attr)mutEvent.getRelatedNode();
            if (!attr.getName().equals("selected")) return;

            // No hacemos comp.setUIEnabled(false); porque si el cambio
            // de selección via markup no es válido el selection model
            // corregirá, o bien el cambio de un option puede suponer cambios en otros option.
            // No hay problema de cambios cíclicos porque se evita lanzar un mutation event
            // si el nuevo valor del atributo es el mismo ya presente.
            HTMLOptionElement option = (HTMLOptionElement)mutEvent.getTarget();
            int index = option.getIndex();
            int changeType = mutEvent.getAttrChange();
            switch(changeType)
            {
                case MutationEvent.ADDITION:
                case MutationEvent.MODIFICATION: // Caso raro
                    selectItem(index,true);
                    break;
                case MutationEvent.REMOVAL:
                    selectItem(index,false);
                    break;
            }
        }
        else if (type.equals("DOMNodeInserted") || type.equals("DOMNodeRemoved"))
        {
            // No he testeado el caso de insertar/remover datos via ListModel directamente (no a través de markup)
            // yo creo que no funcionaría bien porque no podemos detectar este caso, el resultado
            // es que al añadir/remover el DOM por el funcionamiento normal
            // provocaría un mutation event que pasaría por aquí añadiendo/removiendo
            // de nuevo en el ListModel. No es preocupante, un componente en modo markup driven
            // debe modificarse desde el markup o por acción del usuario (selección).

            //Element parent = (Element)mutEvent.getRelatedNode();
            Node node = (Node)mutEvent.getTarget();
            if (node instanceof HTMLOptionElement)
            {
                ListModel dataModel = comp.getListModel();
                HTMLOptionElement option = (HTMLOptionElement)node;
                int index = option.getIndex();
                boolean uiEnabled = comp.isUIEnabled();
                comp.setUIEnabled(false);
                try
                {
                    if (type.equals("DOMNodeInserted"))
                    {
                        // Hay que tener en cuenta que el nodo YA está insertado en el DOM
                        String item = getOptionText(option);
                        addDataModelItem(index,item,dataModel);
                    }
                    else // if (type.equals("DOMNodeRemoved"))
                    {
                        // El nodo todavía NO ha sido removido del árbol DOM
                        removeDataModelItem(index,dataModel);
                    }
                }
                finally
                {
                    comp.setUIEnabled(uiEnabled); // Restaura
                }
            }
            else if (node instanceof Text)
            {
                if (type.equals("DOMNodeInserted"))
                    throw new ItsNatDOMException("Add the text node to the <option> node before inserting to the tree",node);
                else
                    throw new ItsNatDOMException("The text node of the <option> cannot be removed, remove the <option> or change the value",node);
            }
            else
                throw new ItsNatDOMException("Unexpected node",node);
        }
    }
}

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

import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectComboBoxUIImpl extends ItsNatHTMLSelectUIImpl implements ItsNatComboBoxUIInternal
{

    /**
     * Creates a new instance of ItsNatHTMLSelectComboBoxUIImpl
     */
    public ItsNatHTMLSelectComboBoxUIImpl(ItsNatHTMLSelectComboBoxImpl parentComp)
    {
        super(parentComp);

    }

    public ItsNatHTMLSelectComboBox getItsNatHTMLSelectComboBox()
    {
        return (ItsNatHTMLSelectComboBox)parentComp;
    }

    public ItsNatHTMLSelectComboBoxImpl getItsNatHTMLSelectComboBoxImpl()
    {
        return (ItsNatHTMLSelectComboBoxImpl)parentComp;
    }

/*
    public int getSelectedIndex()
    {
        HTMLSelectElement selectElem = getHTMLSelectElement();
        return selectElem.getSelectedIndex();
    }
*/

    public void setSelectedState(HTMLOptionElement option,boolean newState)
    {
        boolean oldState = option.getSelected();
        if (oldState != newState)
        {
            DOMUtilInternal.setBooleanAttribute(option,"selected",newState);
            // option.setSelected(newState); no está definida en versiones de Xerces antiguas (ej. 2.6.2)
        }
    }

    public void setSelectedIndex(int index)
    {
        // Primero ponemos a false todos los no seleccionados
        int len = getLength();
        for(int i = 0; i < len; i++)
        {
            if (index == i) continue;
            HTMLOptionElement option = getHTMLOptionElementAt(i);
            setSelectedState(option,false);
        }
        // Ahora seleccionamos el nuevo (evitamos así que estén seleccionados dos o más en cualquier momento)
        if (index < 0) return; // Ninguno seleccionado
        HTMLOptionElement option = getHTMLOptionElementAt(index);
        setSelectedState(option,true);

        /* Podríamos usar:
        HTMLSelectElement selectElem = getHTMLSelectElement();
        selectElem.setSelectedIndex(index);

        pero Xerces no usa la propiedad selectedIndex realmente, cambia el selected de los OPTION adecuadamente
        es decir hace algo parecido a lo que hacemos antes, pero nos interesa tener
        un total control y evitar al máximo enviar código inútil.

        Podríamos usar también la propiedad selectedIndex pero tiene el problema de que no tiene
        atributo asociado importante por ejemplo para que el control remoto se entere del estado actual.
         */
    }

    public ItsNatComboBox getItsNatComboBox()
    {
        return (ItsNatComboBox)parentComp;
    }
}

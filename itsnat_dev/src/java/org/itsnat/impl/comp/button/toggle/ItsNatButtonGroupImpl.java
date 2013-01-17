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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.toggle.ItsNatButtonRadio;
import org.itsnat.core.ItsNatException;



/**
 * Clase auxiliar y no pública (por ahora)
 *
 * @author jmarranz
 */
public class ItsNatButtonGroupImpl implements ItsNatButtonGroup,Serializable
{
    protected String name;
    protected ButtonGroup group;
    protected List buttonList = new LinkedList();

    /**
     * Creates a new instance of ItsNatButtonGroupImpl
     */
    public ItsNatButtonGroupImpl(String name,ButtonGroup group)
    {
        if (group == null) throw new ItsNatException("ButtonGroup cannot be null");
        this.name = name;
        this.group = group;
    }

    public static void checkRadioButton(ItsNatComponent button)
    {
        if (!(button instanceof ItsNatButtonRadio))
            throw new ItsNatException("Only " + ItsNatButtonRadio.class.getName() + " components are supported",button);
    }

    public void addButton(ItsNatComponent button)
    {
        checkRadioButton(button);
        addButton((ItsNatButtonRadioInternal)button,true);
    }

    public void addButton(ItsNatButtonRadioInternal button,boolean setInComponent)
    {
        buttonList.remove(button); // Para asegurarnos de que no se añade dos veces
        buttonList.add(button);
        if (setInComponent)
        {
            button.setItsNatButtonGroup(this,false);
        }
    }

    public void removeButton(ItsNatComponent button)
    {
        checkRadioButton(button);
        removeButton((ItsNatButtonRadio)button,true);
    }

    public void removeButton(ItsNatButtonRadio button,boolean setInModel)
    {
        buttonList.remove(button);
        if (setInModel)
        {
            ToggleButtonModel model = (ToggleButtonModel)button.getButtonModel();
            model.setGroup(null);

            button.setItsNatButtonGroup((ItsNatButtonGroup)null); // Pues el setGroup no genera evento hay que sincronizar explícitamente
        }
    }

    public int getButtonCount()
    {
        return buttonList.size();
    }

    public ItsNatComponent getButton(int index)
    {
        return (ItsNatComponent)buttonList.get(index);
    }

    public String getName()
    {
        return name;
    }

    public ButtonGroup getButtonGroup()
    {
        return group;
    }
}

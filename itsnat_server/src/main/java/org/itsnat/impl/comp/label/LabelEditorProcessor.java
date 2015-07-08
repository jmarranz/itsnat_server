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
package org.itsnat.impl.comp.label;

import org.itsnat.impl.comp.inplace.EditorProcessorBaseImpl;
import org.itsnat.impl.comp.*;
import java.beans.PropertyVetoException;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.core.ItsNatException;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class LabelEditorProcessor extends EditorProcessorBaseImpl
{
    public LabelEditorProcessor(ItsNatLabelImpl compParent)
    {
        super(compParent);
    }

    public ItsNatLabelImpl getItsNatLabel()
    {
        return (ItsNatLabelImpl)compParent;
    }

    public ItsNatLabelEditor getItsNatLabelEditor()
    {
        return (ItsNatLabelEditor)cellEditor;
    }

    public void setItsNatLabelEditor(ItsNatLabelEditor cellEditor)
    {
        setCellEditor(cellEditor);
    }

    public void startEdition()
    {
        if (prepareEdition())
        {
            openEditor();
        }
    }

    private void openEditor()
    {
        // setCurrentContext(cellInfo,path);

        ItsNatLabelImpl label = getItsNatLabel();
        Object value = label.getValue();
        Element contentElem = label.getElement();
        beforeShow(contentElem);
        ItsNatLabelEditor cellEditor = getItsNatLabelEditor();
        ItsNatComponent compEditor = cellEditor.getLabelEditorComponent(label,value,contentElem);
        afterShow(compEditor);
    }

    protected void openEditor(Event evt)
    {
        openEditor();
    }

    public void acceptNewValue(Object value)
    {
        try
        {
            getItsNatLabel().setValue(value);
        }
        catch(PropertyVetoException ex)
        {
            throw new ItsNatException(ex,value);
        }
    }

    public void clearCurrentContext()
    {
        // setCurrentContext(null,null);
    }
}

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

import org.itsnat.impl.comp.ItsNatElementComponentUIImpl;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.comp.label.ItsNatLabelUI;
import org.itsnat.impl.comp.label.ItsNatLabelImpl;
import org.itsnat.impl.core.domutil.ElementGroupManagerImpl;
import org.itsnat.impl.core.domutil.ElementLabelImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatLabelUIImpl extends ItsNatElementComponentUIImpl implements ItsNatLabelUI
{
    protected boolean enabled = true;
    protected ElementLabelImpl label;

    /** Creates a new instance of ItsNatLabelUIImpl */
    public ItsNatLabelUIImpl(ItsNatLabelImpl parentComp)
    {
        super(parentComp);

        ElementGroupManagerImpl factory = getItsNatDocumentImpl().getElementGroupManagerImpl();
        this.label = factory.createElementLabelInternal(getElement(),true,null);
    }

    public ItsNatLabel getItsNatLabel()
    {
        return (ItsNatLabel)parentComp;
    }

    public ItsNatLabelRenderer getItsNatLabelRenderer()
    {
        return getItsNatLabel().getItsNatLabelRenderer();
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Adds the original markup content to the label (now supposed empty) and renders
     * the specified value.
     *
     * @param value the value to render.
     * @see #removeLabelMarkup()
     * @see org.itsnat.core.domutil.ElementLabel#setLabelValue(Object)
     */
    public void addLabelMarkup(Object value)
    {
        label.addLabelMarkup();
        setLabelValue(value,true);
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     *
     * Removes the current label content.
     *
     * @see #addLabelMarkup(Object)
     */
    public void removeLabelMarkup()
    {
        ItsNatLabelRenderer renderer = getItsNatLabelRenderer();
        if (renderer != null)
        {
            Element parent = label.getParentElement();
            renderer.unrenderLabel(getItsNatLabel(),parent);
        }

        label.removeLabelMarkup();
    }

    /**
     * ESTE METODO FUE PUBLICO ANTES
     * 
     * Updates the label markup rendering the new value.
     *
     * @param value the new value to render.
     */
    public void setLabelValue(Object value)
    {
        setLabelValue(value,false);
    }

    public void setLabelValue(Object value,boolean isNew)
    {
        label.prepareRendering(isNew);

        ItsNatLabelRenderer renderer = getItsNatLabelRenderer();
        if (renderer != null)
        {
            Element parent = label.getParentElement();
            renderer.renderLabel(getItsNatLabel(),value,parent,isNew);
        }
    }

    public boolean hasLabelMarkup()
    {
        return label.hasLabelMarkup();
    }

    public boolean isUsePatternMarkupToRender()
    {
        return label.isUsePatternMarkupToRender();
    }

    public void setUsePatternMarkupToRender(boolean value)
    {
        label.setUsePatternMarkupToRender(value);
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}

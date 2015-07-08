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

import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.domutil.ElementLabelRenderer;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ElementLabelImpl extends ElementGroupImpl implements ElementLabel
{
    protected Element parentElement;
    protected ElementLabelRenderer renderer;
    protected DocumentFragment contentPatternFragment; // Será recordado como patrón, nunca es null pero puede estar vacío
    protected boolean usePatternMarkupToRender;
    protected boolean hasLabel = false;

    /** Creates a new instance of ElementLabelImpl */
    public ElementLabelImpl(ItsNatDocumentImpl itsNatDoc,Element parentElement,boolean removePattern,ElementLabelRenderer renderer)
    {
        super(itsNatDoc);

        this.parentElement = parentElement;
        this.renderer = renderer;
        this.usePatternMarkupToRender = itsNatDoc.isUsePatternMarkupToRender();

        if (removePattern)
        {
            this.contentPatternFragment = DOMUtilInternal.extractChildrenToDocFragment(parentElement);
            // Hasta que no se defina un value explícitamente el label está vacío
        }
        else
        {
            Element clonedParentElem = (Element)parentElement.cloneNode(true);
            this.contentPatternFragment = DOMUtilInternal.extractChildrenToDocFragment(clonedParentElem);
            this.hasLabel = true; // De esta manera evitamos que se use el patrón para llenar el contenido la primera vez, pues el contenido original ya está ahí
            // El contenido original queda como está, el patrón está clonado del original
            // y se usará si usePatternMarkupToRender es true
        }
    }

    public Element getParentElement()
    {
        return parentElement;
    }

    public ElementLabelRenderer getElementLabelRenderer()
    {
        return renderer;
    }

    public void setElementLabelRenderer(ElementLabelRenderer renderer)
    {
        this.renderer = renderer;
    }

    public DocumentFragment getContentPatternFragment()
    {
        return contentPatternFragment;
    }

    public boolean hasLabelMarkup()
    {
        return hasLabel;
    }

    public void setLabelValue(Object value)
    {
        if (!hasLabel)
        {
            // Definido por vez primera explícitamente, en este caso
            // inicialmente el contenido del label está vacío, lo llenamos con el pattern
            addLabelMarkup(value);
        }
        else
        {
            setElementValue(value,false);
        }
    }

    public void setElementValue(Object value,boolean isNew)
    {
        prepareRendering(isNew);

        Element parent = getParentElement();
        ElementLabelRenderer renderer = getElementLabelRenderer();
        if (renderer != null)
            renderer.renderLabel(this,value,parent,isNew);
    }

    public void addLabelMarkup()
    {
        if (hasLabel) throw new ItsNatException("Label already has markup",this);

        Element parent = getParentElement();
        parent.appendChild(contentPatternFragment.cloneNode(true));
        this.hasLabel = true;
    }

    public void addLabelMarkup(Object value)
    {
        addLabelMarkup();
        setElementValue(value,true);
    }

    public void removeLabelMarkup()
    {
        Element parent = getParentElement();
        ElementLabelRenderer renderer = getElementLabelRenderer();
        if (renderer != null) // If null rendering disabled
            renderer.unrenderLabel(this,parent);

        DOMUtilInternal.removeAllChildren(parent); // Si está ya vacío no hace nada obviamente
        this.hasLabel = false;
    }

    public void prepareRendering(boolean isNew)
    {
        if (!isNew && isUsePatternMarkupToRender())
        {
            // Es una actualización en donde tenemos que usar el markup pattern en vez del contenido actual
            Element parent = getParentElement();
            restorePatternMarkupWhenRendering(parent,getContentPatternFragment());
        }
    }

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }
}

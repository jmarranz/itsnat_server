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

import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ListElementInfo;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.core.domutil.ElementListRenderer;
import org.itsnat.core.domutil.ElementListStructure;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * En contra de lo que se dice en la documentación oficial, los ElementList
 * pueden ser slave por conveniencia para el manejo de los <option> de <select>
 * en modo "markup driven".
 *
 * @author jmarranz
 */
public class ElementListImpl extends ElementListBaseImpl implements ElementList
{
    protected ElementListRenderer renderer;
    protected ElementListStructure structure;
    protected Element childPatternElement; // Será recordado como patrón aunque sea removido de la lista el original pues es una copia de otro (clone) y será inmune a los cambios del original
    protected ElementListFreeImpl elementList;
    protected DocumentFragment childContentPatternFragment; // Será recordado como patrón del contenido
    protected boolean usePatternMarkupToRender;

    /**
     * Creates a new instance of ElementListImpl
     */
    public ElementListImpl(ItsNatDocumentImpl itsNatDoc,boolean master,Element parentElement,Element childPatternElement,boolean clonePattern,
                DocumentFragment childContentPatternFragment,boolean removePattern,ElementListRenderer renderer,ElementListStructure structure)
    {
        super(itsNatDoc);

        this.renderer = renderer;
        this.structure = structure;
        this.usePatternMarkupToRender = itsNatDoc.isUsePatternMarkupToRender();

        this.elementList = (ElementListFreeImpl)itsNatDoc.getElementGroupManagerImpl().createElementListFree(parentElement,master);

        if (childPatternElement == null)
        {
            // Debe existir al menos un elemento, dicho nodo, el primero,
            // será recordado como patrón aunque sea removido de la lista.
            childPatternElement = getElementAt(0);
            if (childPatternElement == null) throw new ItsNatDOMException("The list must have at least one cell (used as pattern)",elementList.getParentElement());
            clonePattern = !removePattern; // Si se va a quitar el elemento hijo no hacemos clone pues no va a quedarse en la lista
        }

        setChildPatternElement(childPatternElement,clonePattern);

        this.childContentPatternFragment = childContentPatternFragment;

        if (removePattern) // Eliminamos el original, queda el clonado memorizado
            removeAllElements();
    }

    public ElementListFreeImpl getInternalElementListFree()
    {
        return elementList;
    }

    public ElementListRenderer getElementListRenderer()
    {
        return renderer;
    }

    public void setElementListRenderer(ElementListRenderer renderer)
    {
        this.renderer = renderer;
    }

    public ElementListStructure getElementListStructure()
    {
        return structure;
    }

    public void setElementListStructure(ElementListStructure structure)
    {
        this.structure = structure;
    }

    public Element getContentElementAt(int index)
    {
        Element elem = getElementAt(index);
        if (elem == null) return null;
        return getContentElementAt(index,elem);
    }

    public Element getContentElementAt(int index,Element elem)
    {
        return getElementListStructure().getContentElement(this,index,elem);
    }

    public boolean isMaster()
    {
        return elementList.isMaster();
    }

    public Element getParentElement()
    {
        return elementList.getParentElement();
    }

    public Element getChildPatternElement()
    {
        return childPatternElement;
    }

    public void setChildPatternElement(Element childPatternElement,boolean clone)
    {
        if (clone)
            this.childPatternElement = (Element)childPatternElement.cloneNode(true);
        else
            this.childPatternElement = childPatternElement;

        if (this.childPatternElement.getAttribute("id").length() > 0)
            this.childPatternElement.removeAttribute("id"); // Para evitar duplicidades del id cuando se inserten clones basados en el pattern
    }

    public DocumentFragment getChildContentPatternFragment()
    {
        if (childContentPatternFragment == null)
        {
            // Se crea cuando se necesita, pues puede no usarse nunca
            ElementListStructure structure = getElementListStructure();

            if (structure == null) throw new ItsNatException("INTERNAL ERROR");

            Element itemContentElem = structure.getContentElement(this,0,getChildPatternElement());
            if (itemContentElem != null) // Si no se puede no se puede
            {
                Element clonedItemContentElem = (Element)itemContentElem.cloneNode(true); // Necesitamos clonar porque al extraer los nodos hijos se vaciará el contenido
                this.childContentPatternFragment = DOMUtilInternal.extractChildrenToDocFragment(clonedItemContentElem);
            }
        }

        return childContentPatternFragment;
    }

    public boolean isEmpty()
    {
        return elementList.isEmpty();
    }

    public int getLength()
    {
        return elementList.getLength();
    }

    public void setLength(int len)
    {
        if (len < 0) throw new ItsNatException("Length can not be negative:" + len,this);
        int currentSize = getLength();
        int diff = len - currentSize;
        if (diff > 0)
            for(int i = 0; i < diff; i++)
                addElement();
        else if (diff < 0)
            for(int i = currentSize - 1; i >= len; i--)
                removeElementAt(i);
    }

    public Element getElementAt(int index)
    {
        return elementList.getElementAt(index);
    }

    public Element createNewElement()
    {
        return (Element)childPatternElement.cloneNode(true);
    }

    public Element addElement()
    {
        Element newNode = createNewElement();
        newNode = elementList.addElement2(newNode);
        return newNode;
    }

    public Element addElement(Object value)
    {
        Element newNode = addElement();
        setElementValueAt(getLength() - 1,newNode,value,true);
        return newNode;
    }

    public Element insertElementAt(int index)
    {
        Element newNode = createNewElement();
        newNode = elementList.insertElementAt2(index,newNode);
        return newNode;
    }

    public Element insertElementAt(int index,Object value)
    {
        Element newNode = insertElementAt(index);
        setElementValueAt(index,newNode,value,true);
        return newNode;
    }

    public void unrenderElementAt(int index)
    {
        ElementListRenderer renderer = getElementListRenderer();
        if (renderer == null) return;

        Element elem = getElementAt(index);
        if (elem == null) return;

        Element contentElem = getContentElementAt(index,elem);
        renderer.unrenderList(this,index,contentElem);
    }

    public Element removeElementAt(int index)
    {
        unrenderElementAt(index);

        return elementList.removeElementAt(index);
    }

    public void removeElementRange(int fromIndex, int toIndex)
    {
        if (getElementListRenderer() != null)
            for(int i = fromIndex; i <= toIndex; i++)
                unrenderElementAt(i);

        elementList.removeElementRange(fromIndex,toIndex);
    }

    public void removeAllElements()
    {
        if (getElementListRenderer() != null)
        {
            int size = getLength();
            for(int i = 0; i < size; i++)
                unrenderElementAt(i);
        }

        elementList.removeAllElements();
    }

    public void removeChildPatternElement()
    {
        Node parent = childPatternElement.getParentNode();
        if (parent != null) // Si es null es que el propio pattern ya fue eliminado del árbol (sigue asociado al documento pero no está en el árbol)
            parent.removeChild(childPatternElement);
    }

    public void setElementValueAt(int index,Object value)
    {
        Element elem = getElementAt(index);
        if (elem == null) throw new ItsNatException("Out of range",this);
        setElementValueAt(index,elem,value,false);
    }

    public void setElementValueAt(int index,Element elem,Object value,boolean isNew)
    {
        Element contentElem = getContentElementAt(index,elem);
        prepareRendering(contentElem,isNew);
        ElementListRenderer renderer = getElementListRenderer();
        if (renderer != null)
            renderer.renderList(this,index,value,contentElem,isNew);
    }

    public void prepareRendering(Element contentElem,boolean isNew)
    {
        if (!isNew && isUsePatternMarkupToRender())  // Si es nuevo el markup es ya el del patrón
        {
            // Es una actualización en donde tenemos que usar el markup pattern en vez del contenido actual
            restorePatternMarkupWhenRendering(contentElem,getChildContentPatternFragment());
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

    public Element getElementFromNode(Node node)
    {
        return elementList.getElementFromNode(node);
    }

    public ListElementInfo getListElementInfoAt(int index)
    {
        return elementList.getListElementInfoAt(index);
    }

    public ListElementInfo getListElementInfoFromNode(Node node)
    {
        return elementList.getListElementInfoFromNode(node);
    }

    public ListElementInfoImpl getListElementInfoFromNode(Node node,Element limitElem)
    {
        // Uso interno
        return elementList.getListElementInfoFromNode(node,limitElem);
    }

    public Element getFirstElement()
    {
        return elementList.getFirstElement();
    }

    public Element getLastElement()
    {
        return elementList.getLastElement();
    }

    public int indexOfElement(Element node)
    {
        return elementList.indexOfElement(node);
    }

    public int lastIndexOfElement(Element node)
    {
        return elementList.lastIndexOfElement(node);
    }

    public Element[] getElements()
    {
        return elementList.getElements();
    }

    public void moveElement(int start,int end,int to)
    {
        elementList.moveElement(start,end,to);
    }

}

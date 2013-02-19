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

import java.util.ArrayList;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTreeNodeListImpl extends ElementGroupImpl implements ElementTreeNodeList
{
    protected ElementTreeNodeStructure structure; // La que usarán los nuevos hijos, puede cambiarse
    protected ElementTreeNodeRenderer renderer; // El que usarán los nuevos hijos
    protected ElementTreeNodeImpl parentTreeNode;
    protected ArrayList<ElementTreeNodeImpl> childTreeNodes;
    protected boolean usePatternMarkupToRender;

    /**
     * Creates a new instance of ElementTreeNodeListImpl
     */
    public ElementTreeNodeListImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeNodeImpl parentTreeNode,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc);

        this.parentTreeNode = parentTreeNode; // Puede ser nulo, caso de la lista contenedora del root o bien rootless tree
        if (structure == null) throw new ItsNatException("No tree node structure was registered",this);
        this.structure = structure;
        this.renderer = renderer;

        if (parentTreeNode != null)
            this.usePatternMarkupToRender = parentTreeNode.isUsePatternMarkupToRender();
        else
            this.usePatternMarkupToRender = itsNatDoc.isUsePatternMarkupToRender(); // Root
    }

    public boolean isUsePatternMarkupToRender()
    {
        return usePatternMarkupToRender;
    }

    public void setUsePatternMarkupToRender(boolean usePatternMarkupToRender)
    {
        this.usePatternMarkupToRender = usePatternMarkupToRender;
    }

    protected ArrayList<ElementTreeNodeImpl> getInternalTreeNodeList()
    {
        // En el caso de TreeTable puede no usarse nunca
        if (childTreeNodes == null)
            this.childTreeNodes = new ArrayList<ElementTreeNodeImpl>();
        return childTreeNodes;
    }

    public ElementTreeNodeImpl getElementTreeNodeParent()
    {
        return parentTreeNode;
    }


    public ElementTreeNodeStructure getElementTreeNodeStructure()
    {
        return structure;
    }

    public void setElementTreeNodeStructure(ElementTreeNodeStructure structure)
    {
        this.structure = structure;
    }

    public ElementTreeNodeRenderer getElementTreeNodeRenderer()
    {
        return renderer;
    }

    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer)
    {
        this.renderer = renderer;
    }

    public abstract ElementTreeNodeImpl createTreeNode(int index,Element childElemElement);

    public boolean isOutOfRange(int index)
    {
        return (index < 0) || (index >= getInternalTreeNodeList().size());
    }

    public ElementTreeNode getTreeNodeAt(int index)
    {
        if (isOutOfRange(index)) return null;

        return getInternalTreeNodeList().get(index);
    }

    public ElementTreeNode getFirstTreeNode()
    {
        return getTreeNodeAt(0);
    }

    public ElementTreeNode getLastTreeNode()
    {
        return getTreeNodeAt(getLength() - 1);
    }

    protected abstract Element addTreeNodeDOMElementAt();


    public ElementTreeNode addTreeNode()
    {
        Element newNode = addTreeNodeDOMElementAt();

        int index = getLength();
        ElementTreeNodeImpl treeNode = createTreeNode(index,newNode);
        getInternalTreeNodeList().add(treeNode);

        // No es necesario recalcular el index porque es el último pero sí el row
        recalcForwardIndices(index + 1, +1);

        return treeNode;
    }

    public ElementTreeNode addTreeNode(Object value)
    {
        ElementTreeNodeImpl childNode = (ElementTreeNodeImpl)addTreeNode();
        childNode.setValue(value,true);
        return childNode;
    }

    protected abstract Element insertTreeNodeDOMElementAt(int index);


    public ElementTreeNode insertTreeNodeAt(int index)
    {
        Element newNode = insertTreeNodeDOMElementAt(index);

        ElementTreeNodeImpl treeNode = createTreeNode(index,newNode);
        getInternalTreeNodeList().add(index,treeNode);

        recalcForwardIndices(index + 1, +1);

        return treeNode;
    }

    public ElementTreeNode insertTreeNodeAt(int index,Object value)
    {
        ElementTreeNodeImpl treeNode = (ElementTreeNodeImpl)insertTreeNodeAt(index);
        treeNode.setValue(value,true);
        return treeNode;
    }

    protected abstract Element removeTreeNodeDOMElementAt(int index);

    public void unrenderTreeNode(int index)
    {
        ElementTreeNodeImpl treeNode = (ElementTreeNodeImpl)getTreeNodeAt(index);
        if (treeNode == null) return;

        ElementTreeNodeRenderer renderer = treeNode.getElementTreeNodeRenderer();
        if (renderer != null)
        {
            Element labelElem = treeNode.getLabelElement();
            renderer.unrenderTreeNode(treeNode,labelElem);
        }

        ElementTreeNodeListImpl childList = treeNode.getElementTreeNodeList();
        if (childList == null) return;

        int len = childList.getLength();
        for(int i = 0; i < len; i++)
            childList.unrenderTreeNode(i);
    }

    public ElementTreeNode removeTreeNodeAt(int index)
    {
        unrenderTreeNode(index);

        Element removedElem = removeTreeNodeDOMElementAt(index);
        if (removedElem == null) return null; // out of bounds.

        ArrayList<ElementTreeNodeImpl> childTreeNodes = getInternalTreeNodeList();
        ElementTreeNodeImpl treeNodeRemoved = childTreeNodes.remove(index);

        recalcForwardIndices(index, -1);

        return treeNodeRemoved;
    }

    public void removeTreeNodeRange(int fromIndex, int toIndex)
    {
        // Hay que actualizar rows etc está la complicación del tree-table
        // demasiadas cosas para intentar hacer el borrado del DOM primero
        // con un posible removeTreeNodeDOMElementRange y de las auxiliares después (daba errores ese enfoque)
        // Lo hacemos uno a uno y así aseguramos y del final al principio
        // para que no ralentice la actualización de la row (hacia adelante) de nodos que luego van a ser eliminados
        for(int i = toIndex; i >= fromIndex; i--)
            removeTreeNodeAt(i);
    }

    public void recalcForwardIndices(int fromChildIndex,int count)
    {
        // Corregimos los índices
        ArrayList<ElementTreeNodeImpl> childTreeNodes = getInternalTreeNodeList();
        int newLen = childTreeNodes.size();
        for(int i = fromChildIndex; i < newLen ; i++)
        {
            ElementTreeNodeImpl nodeAfter = childTreeNodes.get(i);
            nodeAfter.setIndex(nodeAfter.getIndex() + count);
        }

        // El recálculo de las rows es mucho más complicado que los índices de los hijos inmediatos
        // porque el borrado o inserción de un nuevo nodo puede suponer el borrado o adición
        // también de nodos hijo. Por eso lo que hacemos es invalidar la row de los siguientes
        // nodos a los borrados/añadidos y de esa manera cuando se necesite obtenerse se calculará automáticamente
        // "bajo demanda".

        ElementTreeNodeImpl nodeAfter = null;
        if (fromChildIndex < newLen)
            nodeAfter = childTreeNodes.get(fromChildIndex);
        else
        {
            // Es el caso en el que se ha añadido el nuevo nodo como el último
            // hijo, o bien se ha eliminado el último nodo hijo (o todos los hijos) y newLen es cero
            // en dicho caso el siguiente es el siguiente del padre, es decir de este nodo,
            // salvo que sea una lista contenedora del Root (no hay TreeNode)
            // en dicho caso no hay siguiente claro pues el único posible nodo hijo añadido o quitado es el Root
            ElementTreeNodeImpl thisTreeNode = getElementTreeNodeParent();
            if (thisTreeNode != null)
                nodeAfter = (ElementTreeNodeImpl)thisTreeNode.getNextSiblingTreeNode();
        }

        while(nodeAfter != null)
        {
            nodeAfter.setRow(-1);

            nodeAfter = (ElementTreeNodeImpl)nodeAfter.getNextTreeNode();
        }
    }

    public void removeAllTreeNodes()
    {
        int len = getLength();
        if (len == 0) return;

        removeTreeNodeRange(0,len - 1);
    }

    public boolean isEmpty()
    {
        return getInternalTreeNodeList().isEmpty();
    }

    public int getLength()
    {
        return getInternalTreeNodeList().size();
    }

    public void setLength(int len)
    {
        if (len < 0) throw new ItsNatException("Length can not be negative:" + len,this);
        int currentSize = getLength();
        int diff = len - currentSize;
        if (diff > 0)
            for(int i = 0; i < diff; i++)
                addTreeNode();
        else if (diff < 0)
            for(int i = currentSize - 1; i >= len; i--)
                removeTreeNodeAt(i);
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node)
    {
        return getElementTreeNodeFromNode(node,getTreeContainerElement());
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node,Element treeContainerElem)
    {
        Element parentElem = getParentElement();
        if (!DOMUtilInternal.isChildOrSame(node,parentElem,treeContainerElem))
        {
            // Podemos ahorrar mucho tiempo en este caso, evitando iterar por el árbol de los hijos sobre todo si hay muchos
            // En el caso de tree-table salvo la primera vez es redundante (si no es el caso nulo).
            return null;
        }

        ArrayList<ElementTreeNodeImpl> childList = getInternalTreeNodeList();
        int len = childList.size();
        for(int i = 0; i < len; i++)
        {
            ElementTreeNodeImpl childTreeNode = childList.get(i);
            ElementTreeNode result = childTreeNode.getElementTreeNodeFromNode(node,treeContainerElem);
            if (result != null)
                return result;
        }
        return null; // En este caso "node" no pertenece a este TreeNode o bien se ha pulsado un nodo de texto entre los hijos o una zona entre el elemento DOM padre de los hijos y los hijos
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row)
    {
        int[] currRow = ElementTreeNodeImpl.createRowIterator();

        return getElementTreeNodeFromRow(row,currRow);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row,int[] currRow)
    {
        ArrayList<ElementTreeNodeImpl> childList = getInternalTreeNodeList();
        int size = childList.size();
        for(int i = 0; i < size; i++)
        {
            ElementTreeNodeImpl childTreeNode = childList.get(i);
            ElementTreeNodeImpl treeNodeRes = childTreeNode.getElementTreeNodeFromRow(row,currRow);
            if (treeNodeRes != null)
                return treeNodeRes;
        }

        return null;
    }

    public int getRowCount()
    {
        int count = 0;
        ArrayList<ElementTreeNodeImpl> childList = getInternalTreeNodeList();
        int size = childList.size();
        for(int i = 0; i < size; i++)
        {
            ElementTreeNodeImpl childTreeNode = childList.get(i);
            count += childTreeNode.getRowCount();
        }
        return count;
    }

    public abstract Element getTreeContainerElement();

    public ElementTreeNode[] getTreeNodes()
    {
        ArrayList<ElementTreeNodeImpl> childList = getInternalTreeNodeList();
        return childList.toArray(new ElementTreeNode[childList.size()]);
    }

}

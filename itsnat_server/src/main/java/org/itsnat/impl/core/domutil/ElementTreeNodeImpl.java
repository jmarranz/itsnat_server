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
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ElementTreeNodeImpl extends ElementGroupImpl implements ElementTreeNode
{
    protected final ElementTreeNodeStructure structure;  // No se puede cambiar pues se utiliza en tiempo de creación del objeto (en el constructor), por tanto no tiene sentido un método "set"
    protected ElementTreeNodeRenderer renderer;
    protected ElementTreeNodeListImpl parentList;
    protected ElementTreeNodeListImpl childElemList;
    protected Element parentElement;
    protected int index; // -1 cuando parentTreeNode es null
    protected int row = -1; // 0 si es el root, -1 es el valor inicial que indica que no ha sido calculado, se calcula la primera vez que se necesite
    protected Object auxObject; // Usado por el componente ItsNatTree, no usar para otro fin, no hacer público
    protected boolean usePatternMarkupToRender;
    protected DocumentFragment labelContentPatternFragment; // Será recordado como patrón

    /**
     * Creates a new instance of ElementTreeNodeImpl
     */
    public ElementTreeNodeImpl(ItsNatDocumentImpl itsNatDoc,ElementTreeNodeListImpl parentList,int index,Element parentElement,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        super(itsNatDoc);

        this.parentList = parentList;  // Puede ser null en el caso de root no removible, lo cual no es posible en TreeTables (nunca null)
        this.index = index;
        this.parentElement = parentElement;
        if (structure == null) throw new ItsNatException("No tree node structure was registered",this);
        this.structure = structure;
        this.renderer = renderer;

        if (parentList != null)
            this.usePatternMarkupToRender = parentList.isUsePatternMarkupToRender();
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

    public Object getAuxObject()
    {
        return auxObject;
    }

    public void setAuxObject(Object auxObject)
    {
        this.auxObject = auxObject;
    }

    public ElementTreeNodeListImpl getParentList()
    {
        return parentList;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        // Interna, no hacer pública
        this.index = index;
    }

    public Element getParentElement()
    {
        return parentElement;
    }

    public Element getTreeContainerElement()
    {
        // Devuelve el elemento DOM más alto posible que englobe todo
        // el árbol, sea el nodo removible o no
        if (parentList != null)
            return parentList.getTreeContainerElement();
        else
            return getParentElement(); // Es un root no removible (lo más alto)
    }

    public ElementTreeNodeStructure getElementTreeNodeStructure()
    {
        return structure;
    }

    public ElementTreeNode getElementTreeNodeParent()
    {
        // Devuelve null si es el root
        if (parentList == null)
            return null; // Caso de root no removible, cuando el root es no removible no tiene lista contenedora
        return parentList.getElementTreeNodeParent(); // Si devuelve null es el root removible o rootless tree
    }

    public ElementTreeNode getPreviousSiblingTreeNode()
    {
        ElementTreeNodeListImpl parentList = getParentList();
        if (parentList == null) // Caso de root no removible
            return null;
        return parentList.getTreeNodeAt(getIndex() - 1);
    }

    public ElementTreeNode getPreviousTreeNode()
    {
        ElementTreeNodeImpl prevSibling = (ElementTreeNodeImpl)getPreviousSiblingTreeNode();
        if (prevSibling == null)
            return getElementTreeNodeParent(); // puede ser null

        return prevSibling.getDeepMostLastTreeNode();
    }

    public ElementTreeNodeImpl getDeepMostLastTreeNode()
    {
        ElementTreeNode lastChild = getChildTreeNodeList().getLastTreeNode();
        if (lastChild == null)
            return this;  // Sí mismo, no tiene hijos

        ElementTreeNode prevChild;
        do
        {
            prevChild = lastChild;
            lastChild = lastChild.getChildTreeNodeList().getLastTreeNode();
        }
        while (lastChild != null);

        return (ElementTreeNodeImpl)prevChild;
    }

    public ElementTreeNode getNextSiblingTreeNode()
    {
        ElementTreeNodeListImpl parentList = getParentList();
        if (parentList == null) // Caso de root no removible
            return null;
        return parentList.getTreeNodeAt(getIndex() + 1);
    }

    public ElementTreeNode getNextTreeNode()
    {
        ElementTreeNode result = getChildTreeNodeList().getFirstTreeNode();
        if (result != null)
            return result;

        result = getNextSiblingTreeNode();
        if (result != null)
            return result;

        // return parent's 1st sibling.
        ElementTreeNode parent = getElementTreeNodeParent();
        while (parent != null)
        {
            result = parent.getNextSiblingTreeNode();
            if (result != null)
                return result;
            else
                parent = parent.getElementTreeNodeParent();
        }

        // end , return null
        return null;
    }

    public static int[] createRowIterator()
    {
        int[] currRow = new int[1];
        currRow[0] = -1;
        return currRow;
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row)
    {
        int[] currRow = createRowIterator();

        return getElementTreeNodeFromRow(row,currRow);
    }

    public ElementTreeNodeImpl getElementTreeNodeFromRow(int row,int[] currRow)
    {
        currRow[0] += 1;
        if (row == currRow[0])
            return this;
        else
        {
            ElementTreeNodeListImpl childList = (ElementTreeNodeListImpl)getChildTreeNodeList();
            return childList.getElementTreeNodeFromRow(row,currRow);
        }
    }

    public int getRowCount()
    {
        int count = 1; // el propio nodo
        ElementTreeNodeListImpl childList = (ElementTreeNodeListImpl)getChildTreeNodeList();
        count += childList.getRowCount();
        return count;
    }

    public ElementTreeNodeRenderer getElementTreeNodeRenderer()
    {
        return renderer;
    }

    public void setElementTreeNodeRenderer(ElementTreeNodeRenderer renderer)
    {
        this.renderer = renderer;
    }

    public Element getContentElement()
    {
        return getElementTreeNodeStructure().getContentElement(this,getParentElement());
    }

    public Element getHandleElement()
    {
        return getElementTreeNodeStructure().getHandleElement(this,getParentElement());
    }

    public Element getIconElement()
    {
        return getElementTreeNodeStructure().getIconElement(this,getParentElement());
    }

    public Element getLabelElement()
    {
        return getElementTreeNodeStructure().getLabelElement(this,getParentElement());
    }

    public Element getChildListElement()
    {
        return getElementTreeNodeStructure().getChildListElement(this,getParentElement());
    }

    public void setValue(Object value)
    {
        setValue(value,false);
    }

    public void setValue(Object value,boolean isNew)
    {
        Element labelElem = getLabelElement();
        prepareRendering(labelElem,isNew);
        ElementTreeNodeRenderer renderer = getElementTreeNodeRenderer();
        if (renderer != null)
            renderer.renderTreeNode(this,value,labelElem,isNew);
    }

    public void prepareRendering(Element labelElem,boolean isNew)
    {
        if (!isNew && isUsePatternMarkupToRender())  // Si es nuevo el markup es ya el del patrón
        {
            // Es una actualización en donde tenemos que usar el markup pattern en vez del contenido actual
            restorePatternMarkupWhenRendering(labelElem,getLabelContentPatternFragment());
        }
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node)
    {
        return getElementTreeNodeFromNode(node,getTreeContainerElement());
    }

    public ElementTreeNode getElementTreeNodeFromNode(Node node,Element treeContainerElem)
    {
        if (getParentElement() == node)
            return this;

        Element contentElem = getContentElement();
        if (contentElem != null)
        {
            if (DOMUtilInternal.isChildOrSame(node,contentElem,treeContainerElem)) // El treeContainerElem es para acelerar la búsqueda en caso fallido
            {
                // "node" forma parte del "contenido" del nodo
                return this;
            }
        }
        else // Caso raro
        {
            Element handleElem = getHandleElement();
            if ((handleElem != null) && DOMUtilInternal.isChildOrSame(node,handleElem,treeContainerElem)) // El treeContainerElem es para acelerar la búsqueda en caso fallido
                return this; // "node" forma parte del "contenido" del nodo

            Element iconElem = getIconElement();
            if ((iconElem != null) && DOMUtilInternal.isChildOrSame(node,iconElem,treeContainerElem)) // El treeContainerElem es para acelerar la búsqueda en caso fallido
                return this; // "node" forma parte del "contenido" del nodo

            Element labelElem = getIconElement();
            if ((labelElem != null) && DOMUtilInternal.isChildOrSame(node,labelElem,treeContainerElem)) // El treeContainerElem es para acelerar la búsqueda en caso fallido
                return this; // "node" forma parte del "contenido" del nodo
        }

        ElementTreeNodeListImpl childElemList = getElementTreeNodeList();
        if (childElemList != null) // puede no tener hijos
            return childElemList.getElementTreeNodeFromNode(node,treeContainerElem);
        return null;
        // Si null en este caso "node" no pertenece a este TreeNode o bien se ha pulsado un nodo de texto entre los hijos o una zona entre el elemento DOM padre de los hijos y los hijos
    }

    public static String toString(int[] path)
    {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < path.length; i++)
        {
            if (i > 0) res.append( "," );
            res.append( path[i] );
        }
        return res.toString();
    }

    public ElementTreeNode getElementTreeNodeFromPath(int[] path)
    {
        // No se usa, para un posible uso recuperar.
        return getElementTreeNodeFromPath(path,0);
    }

    public ElementTreeNode getElementTreeNodeFromPath(int[] path,int fromIndex)
    {
        // No se usa, para un posible uso recuperar.
        if (fromIndex == path.length)
            return this;
        int childIndex = path[fromIndex];
        if (childIndex == -1) // Puede serlo si viene de llamar a TreeModel.getIndexOfChild
            return null;
        ElementTreeNodeImpl childNode = (ElementTreeNodeImpl)getChildTreeNodeList().getTreeNodeAt(childIndex);
        if (childNode == null)
            throw new ItsNatException("Tree node not found: " + toString(path),this);

        return childNode.getElementTreeNodeFromPath(path,fromIndex + 1);
    }

    public ElementTreeNodeList getChildTreeNodeList()
    {
        return getElementTreeNodeList();
    }

    public abstract ElementTreeNodeListImpl getElementTreeNodeList();


    public int getRow()
    {
        if (this.row != -1)
            return row;

        // Devuelve el índice de la fila visto el árbol como una lista
        // Hay que tener en cuenta el caso "rootless" en donde un nodo sin
        // nodo padre no tiene por qué ser el root, luego eso no vale como criterio "el root"
        // para calcular la fila 0, el no existir nodo previo si vale en todos los casos.
        ElementTreeNode prevTreeNode = getPreviousTreeNode();
        if (prevTreeNode == null)
            this.row = 0; // El root
        else
            this.row = prevTreeNode.getRow() + 1;

        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getDeepLevel()
    {
        ElementTreeNode parentNode = getElementTreeNodeParent();
        if (parentNode == null)
            return 0; // Sea o no el root (pues existe el caso de rootless) no hay más niveles por encima
        return parentNode.getDeepLevel() + 1;
    }

    public boolean isLeaf()
    {
        return getChildTreeNodeList().isEmpty();
    }

    public DocumentFragment getLabelContentPatternFragment()
    {
        if (labelContentPatternFragment == null)
        {
            Element labelElem;
            ElementTreeNodeListImpl parentList = getParentList(); // No puede ser null
            if (parentList == null)
            {
                // En este caso obtenemos el pattern a través del markup original del root
                // antes de que se renderice por vez primera
                labelElem = structure.getLabelElement(this,getParentElement());
            }
            else
            {
                Element treeNodePattern = parentList.getChildPatternElement();
                ElementTreeNodeStructure structure = getElementTreeNodeStructure();
                labelElem = structure.getLabelElement(this,treeNodePattern);
            }

            if (labelElem != null) // Si no se puede no se puede
            {
                Element clonedLabelElem = (Element)labelElem.cloneNode(true); // Necesitamos clonar porque al extraer los nodos hijos se vaciará el contenido
                this.labelContentPatternFragment = DOMUtilInternal.extractChildrenToDocFragment(clonedLabelElem);
            }
        }

        return labelContentPatternFragment;
    }
}

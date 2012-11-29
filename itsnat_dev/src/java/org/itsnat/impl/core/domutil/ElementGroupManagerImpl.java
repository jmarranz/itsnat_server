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

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementLabel;
import org.itsnat.core.domutil.ElementLabelRenderer;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.core.domutil.ElementListRenderer;
import org.itsnat.core.domutil.ElementListStructure;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.core.domutil.ElementTableFree;
import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementGroupManagerImpl implements ElementGroupManager,Serializable
{
    protected ItsNatDocumentImpl itsNatDoc;

    /** Creates a new instance of ElementGroupManagerImpl */
    public ElementGroupManagerImpl(ItsNatDocumentImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return itsNatDoc;
    }

    public ElementListFree createElementListFree(Element parentElement,boolean master)
    {
        if (master)
            return new ElementListFreeMasterImpl(parentElement,getItsNatDocumentImpl());
        else
            return new ElementListFreeSlaveDefaultImpl(parentElement,getItsNatDocumentImpl());
    }

    public ElementTableFree createElementTableFree(Element parentElement,boolean master)
    {
        if (master)
            return new ElementTableFreeMasterImpl(getItsNatDocumentImpl(),parentElement);
        else
            return new ElementTableFreeSlaveImpl(getItsNatDocumentImpl(),parentElement);
    }

    public ElementLabel createElementLabel(Element parentElement,boolean removePattern,ElementLabelRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementLabelRenderer();  // Aunque no se use no hay problema pues es un singleton

        return createElementLabelInternal(parentElement,removePattern,renderer);
    }

    public ElementLabelImpl createElementLabelInternal(Element parentElement,boolean removePattern,ElementLabelRenderer renderer)
    {
        return new ElementLabelImpl(getItsNatDocumentImpl(),parentElement,removePattern,renderer);
    }

    public ElementList createElementList(Element parentElement,boolean removePattern,ElementListStructure structure,ElementListRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementListRenderer();  // Aunque no se use no hay problema pues es un singleton
        if (structure == null) structure = createDefaultElementListStructure();  // Aunque no se use no hay problema pues es un singleton

        return createElementListInternal(true,parentElement,null,true,null,removePattern,structure,renderer);
    }

    public ElementList createElementList(Element parentElement,boolean removePattern)
    {
        return createElementList(parentElement,removePattern,null,null);
    }

    public ElementListImpl createElementListInternal(Element parentElement,boolean removePattern,ElementListStructure structure,ElementListRenderer renderer)
    {
        return createElementListInternal(true,parentElement,null,true,null,removePattern,structure,renderer);
    }

    public ElementListImpl createElementListInternal(boolean master,Element parentElement,Element childPatternElement,boolean clonePattern,DocumentFragment childContentPatternFragment,boolean removePattern,ElementListStructure structure,ElementListRenderer renderer)
    {
        return new ElementListImpl(getItsNatDocumentImpl(),master,parentElement,childPatternElement,clonePattern,childContentPatternFragment,removePattern,renderer,structure);
    }

    public ElementListImpl createElementListNoRenderInternal(Element parentElement,Element childPatternElement,boolean clonePattern,boolean removePattern)
    {
        // Como no renderiza no necesitamos ni la estructura ni el renderer, sirve para gestionar elementos
        // Pasamos childContentPatternFragment como null porque éste está vinculado a la renderización
        return createElementListInternal(true,parentElement,childPatternElement,clonePattern,null,removePattern,null,null);
    }

    public ElementTable createElementTable(Element parentElement,boolean removePattern,ElementTableStructure structure,ElementTableRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementTableRenderer();  // Aunque no se use no hay problema pues es un singleton
        if (structure == null) structure = createDefaultElementTableStructure();  // Aunque no se use no hay problema pues es un singleton

        return createElementTableInternal(parentElement,removePattern,structure,renderer);
    }

    public ElementTable createElementTable(Element parentElement,boolean removePattern)
    {
        return createElementTable(parentElement,removePattern,null,null);
    }

    public ElementTableImpl createElementTableInternal(Element parentElement,boolean removePattern,ElementTableStructure structure,ElementTableRenderer renderer)
    {
        return new ElementTableImpl(getItsNatDocumentImpl(),parentElement,removePattern,structure,renderer);
    }

    public ElementTreeNode createElementTreeNode(Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementTreeNodeRenderer();  // Aunque no se use no hay problema pues es un singleton
        if (structure == null) structure = createDefaultElementTreeNodeStructure();  // Es realmente un singleton

        return createElementTreeNodeInternal(parentElement,removePattern,structure,renderer);
    }

    public ElementTreeNode createElementTreeNode(Element parentElement,boolean removePattern)
    {
        return createElementTreeNode(parentElement,removePattern,null,null);
    }

    public ElementTreeNodeNormalImpl createElementTreeNodeInternal(Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        return new ElementTreeNodeNormalImpl(getItsNatDocumentImpl(),null,-1,parentElement,null,true,removePattern,structure,renderer);
    }

    public ElementTree createElementTree(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementTreeNodeRenderer();
        if (structure == null) structure = createDefaultElementTreeNodeStructure();

        return createElementTreeInternal(treeTable,parentElement,removePattern,structure,renderer);
    }

    public ElementTree createElementTree(boolean treeTable,Element parentElement,boolean removePattern)
    {
        return createElementTree(treeTable,parentElement,removePattern,null,null);
    }

    public ElementTreeImpl createElementTreeInternal(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        return new ElementTreeImpl(getItsNatDocumentImpl(),treeTable,parentElement,removePattern,structure,renderer);
    }

    public ElementTreeNodeList createElementTreeNodeList(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        if (renderer == null) renderer = createDefaultElementTreeNodeRenderer();  // Aunque no se use no hay problema pues es un singleton
        if (structure == null) structure = createDefaultElementTreeNodeStructure();

        return createElementTreeNodeListInternal(treeTable,parentElement,removePattern,structure,renderer);
    }

    public ElementTreeNodeList createElementTreeNodeList(boolean treeTable,Element parentElement,boolean removePattern)
    {
        return createElementTreeNodeList(treeTable,parentElement,removePattern,null,null);
    }

    public ElementTreeNodeListImpl createElementTreeNodeListInternal(boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        if (treeTable)
            return new ElementTreeTableNodeListRootImpl(getItsNatDocumentImpl(),parentElement,removePattern,structure,renderer);
        else
            return new ElementTreeNormalNodeListRootImpl(getItsNatDocumentImpl(),parentElement,removePattern,structure,renderer);
    }

    public ElementTreeVersatileImpl createElementTreeVersatileInternal(boolean rootLess,boolean treeTable,Element parentElement,boolean removePattern,ElementTreeNodeStructure structure,ElementTreeNodeRenderer renderer)
    {
        if (rootLess)
            return new ElementTreeVersatileRootlessImpl(getItsNatDocumentImpl(),treeTable,parentElement,removePattern,structure,renderer);
        else
            return new ElementTreeVersatileWithRootImpl(getItsNatDocumentImpl(),treeTable,parentElement,removePattern,structure,renderer);
    }

    public ElementRenderer createDefaultElementRenderer()
    {
        return ElementRendererDefaultImpl.newElementRendererDefault();
    }

    public ElementLabelRenderer createDefaultElementLabelRenderer()
    {
        return ElementLabelRendererDefaultImpl.newElementLabelRendererDefault();
    }

    public ElementListRenderer createDefaultElementListRenderer()
    {
        return ElementListRendererDefaultImpl.newElementListRendererDefault();
    }

    public ElementTableRenderer createDefaultElementTableRenderer()
    {
        return ElementTableRendererDefaultImpl.newElementTableRendererDefault();
    }

    public ElementTreeNodeRenderer createDefaultElementTreeNodeRenderer()
    {
        return ElementTreeNodeRendererDefaultImpl.newElementTreeNodeRendererDefault();
    }

    public ElementListStructure createDefaultElementListStructure()
    {
        return ElementListStructureDefaultImpl.newElementListStructureDefault();
    }

    public ElementTableStructure createDefaultElementTableStructure()
    {
        return ElementTableStructureDefaultImpl.newElementTableStructureDefault();
    }

    public ElementTreeNodeStructure createDefaultElementTreeNodeStructure()
    {
        return ElementTreeNodeStructureDefaultImpl.newElementTreeNodeStructureDefault();
    }
}

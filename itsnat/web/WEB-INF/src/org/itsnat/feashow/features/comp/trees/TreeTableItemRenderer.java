/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow.features.comp.trees;

import javax.swing.tree.DefaultMutableTreeNode;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;

public class TreeTableItemRenderer implements ItsNatTreeCellRenderer
{
    protected ElementRenderer renderer;

    public TreeTableItemRenderer(ItsNatDocument itsNatDoc)
    {
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        this.renderer = factory.createDefaultElementRenderer();
    }

    public void renderTreeCell(ItsNatTree tree,int row, Object value,
            boolean selected,boolean expanded,boolean leaf,boolean hasFocus,Element labelElem,boolean isNew)
    {
        DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)value;
        TreeTableItem item = (TreeTableItem)dataNode.getUserObject();
        renderer.render(null,item.getPrincipal(),labelElem,isNew);

        Element labelParentElem = (Element)labelElem.getParentNode();

        if (labelParentElem instanceof HTMLTableCellElement)
        {
            /*
            <tr>
                <td><img src="..." /><img src="..." /><span><b>Label</b></span></td>
                <td><b>Other content</b></td>
            </tr>
            */
            HTMLTableCellElement tdSecondElem = (HTMLTableCellElement)ItsNatTreeWalker.getNextSiblingElement(labelParentElem);
            renderer.render(null,item.getSecondary(),tdSecondElem,isNew);
        }
        else
        {
            /*
            <p>
                <img src="..." /><img src="..." /><span><b>Label</b></span>
                <span>Other content</span>
            </p>
             */
            Element secondContentElem = ItsNatTreeWalker.getNextSiblingElement(labelElem);
            renderer.render(null,item.getSecondary(),secondContentElem,isNew);
        }
    }

    public void unrenderTreeCell(ItsNatTree tree,int row,Element treeNodeLabelElem)
    {
    }
}

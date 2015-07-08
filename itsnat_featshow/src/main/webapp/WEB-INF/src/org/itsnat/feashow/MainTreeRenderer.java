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
package org.itsnat.feashow;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;

public class MainTreeRenderer implements ItsNatTreeCellRenderer
{
    protected ItsNatTreeCellRenderer delegate;

    public MainTreeRenderer(ItsNatTreeCellRenderer parent)
    {
        this.delegate = parent;
    }

    public void renderTreeCell(ItsNatTree tree, int row, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, boolean hasFocus, Element treeNodeLabelElem, boolean isNew)
    {
        if (isNew)
        {
            ItsNatDocumentTemplate mainTemplate = tree.getItsNatDocument().getItsNatDocumentTemplate();
            String mainTempName = mainTemplate.getName();

            TreePath path = tree.getTreePathForRow(row);
            DefaultMutableTreeNode dataNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            FeatureTreeNode feature = (FeatureTreeNode)dataNode.getUserObject();
            String featureName = feature.getFeatureName();

            HTMLAnchorElement permalink = (HTMLAnchorElement)ItsNatTreeWalker.getNextSiblingElement(treeNodeLabelElem);
            permalink.setHref("?itsnat_doc_name=" + mainTempName + "&feature=" + featureName + ".default");
        }

        delegate.renderTreeCell(tree,row,value,isSelected,isExpanded,isLeaf,hasFocus,treeNodeLabelElem,isNew);
    }

    public void unrenderTreeCell(ItsNatTree tree,int row,Element treeNodeLabelElem)
    {
    }
}

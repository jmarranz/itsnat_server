/*
 * FirstLevelTreeRenderer.java
 *
 * Created on 22 de agosto de 2007, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core.treecustom;

import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FirstLevelTreeRenderer implements ElementTreeNodeRenderer
{
    public static final FirstLevelTreeRenderer SINGLETON = new FirstLevelTreeRenderer();

    /** Creates a new instance of FirstLevelTreeRenderer */
    public FirstLevelTreeRenderer()
    {
    }

    public void renderTreeNode(ElementTreeNode treeNode, Object value, Element labelElem,boolean isNew)
    {
        String[] name = (String[])value;
        Element firstName = labelElem;
        ItsNatDOMUtil.setTextContent(firstName,name[0]);
        Element lastName = ItsNatTreeWalker.getNextSiblingElement(firstName);
        ItsNatDOMUtil.setTextContent(lastName,name[1]);
    }

    public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem)
    {
    }
}

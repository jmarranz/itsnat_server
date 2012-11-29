/*
 * FirstLevelTreeRenderer.java
 *
 * Created on 22 de agosto de 2007, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core.treecustom;

import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class SecondLevelTreeRenderer implements ElementTreeNodeRenderer
{
    public static final SecondLevelTreeRenderer SINGLETON = new SecondLevelTreeRenderer();

    /** Creates a new instance of FirstLevelTreeRenderer */
    public SecondLevelTreeRenderer()
    {
    }

    public void renderTreeNode(ElementTreeNode treeNode, Object value,Element labelElem,boolean isNew)
    {
        ItsNatDOMUtil.setTextContent(labelElem,(String)value);
    }

    public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem)
    {
    }
}

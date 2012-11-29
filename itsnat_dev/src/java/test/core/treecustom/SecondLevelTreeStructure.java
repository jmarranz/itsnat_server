/*
 * FirstLevelTreeStructure.java
 *
 * Created on 22 de agosto de 2007, 14:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core.treecustom;

import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class SecondLevelTreeStructure implements ElementTreeNodeStructure
{
    public static final SecondLevelTreeStructure SINGLETON = new SecondLevelTreeStructure();

    /** Creates a new instance of FirstLevelTreeStructure */
    public SecondLevelTreeStructure()
    {
    }

    public Element getContentElement(ElementTreeNode treeNode,Element nodeElem)
    {
        return null;
    }

    public Element getHandleElement(ElementTreeNode treeNode,Element nodeElem)
    {
        return null;
    }

    public Element getIconElement(ElementTreeNode treeNode,Element nodeElem)
    {
        return null;
    }

    public Element getLabelElement(ElementTreeNode treeNode,Element nodeElem)
    {
        return nodeElem;
    }

    public Element getChildListElement(ElementTreeNode treeNode,Element nodeElem)
    {
        return null;
    }

}

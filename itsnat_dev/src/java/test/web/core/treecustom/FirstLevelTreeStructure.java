/*
 * FirstLevelTreeStructure.java
 *
 * Created on 22 de agosto de 2007, 14:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core.treecustom;

import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FirstLevelTreeStructure implements ElementTreeNodeStructure
{
    public static final FirstLevelTreeStructure SINGLETON = new FirstLevelTreeStructure();

    /** Creates a new instance of FirstLevelTreeStructure */
    public FirstLevelTreeStructure()
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
        Element firstName = ItsNatTreeWalker.getFirstChildElement(nodeElem);
        return firstName;
    }

    public Element getChildListElement(ElementTreeNode treeNode,Element nodeElem)
    {
        Element firstName = getLabelElement(treeNode,nodeElem);
        Element lastName = ItsNatTreeWalker.getNextSiblingElement(firstName);
        Element childList = ItsNatTreeWalker.getNextSiblingElement(lastName);
        return childList;
    }

}

/*
 * TestTreeBase.java
 *
 * Created on 28 de febrero de 2007, 20:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestTreeNoRootBase
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestTreeBase
     */
    public TestTreeNoRootBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void load(String treeId,boolean treeTable)
    {
        Document doc = itsNatDoc.getDocument();
        Element treeParentElem = doc.getElementById(treeId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNodeList rootList = factory.createElementTreeNodeList(treeTable,treeParentElem,true,null,null);

        createTree(rootList);
    }

    public void createTree(ElementTreeNodeList rootList)
    {
        ElementTreeNode childNode;

        childNode = rootList.addTreeNode("Item 1");
           childNode = childNode.getChildTreeNodeList().addTreeNode("Item 1.1");
               childNode.getChildTreeNodeList().addTreeNode("Item 1.1.1");

        childNode = rootList.addTreeNode("Item 2");
           childNode.getChildTreeNodeList().addTreeNode("Item 2.1");
           childNode.getChildTreeNodeList().addTreeNode("Item 2.2");
    }

}

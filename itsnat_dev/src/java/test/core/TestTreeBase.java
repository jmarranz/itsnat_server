/*
 * TestTreeBase.java
 *
 * Created on 28 de febrero de 2007, 20:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestTreeBase
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestTreeBase
     */
    public TestTreeBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void testRemoveRoot(ElementTree tree)
    {
        tree.removeRootNode(); // Para ver si funciona la recreación
        TestUtil.checkError(!tree.hasTreeNodeRoot());
        tree.addRootNode();
    }

    public void createTree(ElementTreeNode rootNode)
    {
        rootNode.setUsePatternMarkupToRender(true); // para probar

        rootNode.setValue("Root");

        ElementTreeNode childNode;

        childNode = rootNode.getChildTreeNodeList().addTreeNode("Item 1");
           childNode = childNode.getChildTreeNodeList().addTreeNode("Item 1.1");
               childNode.getChildTreeNodeList().addTreeNode("Item 1.1.1");

        childNode = rootNode.getChildTreeNodeList().addTreeNode("Item 2");
           childNode.getChildTreeNodeList().addTreeNode("Item 2.1");
           childNode.getChildTreeNodeList().addTreeNode("Item 2.2");
    }

}

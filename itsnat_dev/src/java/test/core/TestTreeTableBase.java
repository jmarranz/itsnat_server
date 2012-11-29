/*
 * TestTreeTableBase.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestTreeTableBase extends TestTreeBase
{

    /**
     * Creates a new instance of TestTreeTableBase
     */
    public TestTreeTableBase(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);
    }

    public void load(String treeId)
    {
        Document doc = itsNatDoc.getDocument();
        Element treeParentElem = doc.getElementById(treeId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTree tree = factory.createElementTree(true,treeParentElem,true);

        testRemoveRoot(tree);

        ElementTreeNode rootNode = tree.getRootNode();
        createTree(rootNode);
    }

}

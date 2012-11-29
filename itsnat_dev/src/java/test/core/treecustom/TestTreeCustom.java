/*
 * TestTreeCustom.java
 *
 * Created on 22 de agosto de 2007, 14:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core.treecustom;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestTreeCustom
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestTreeCustom
     */
    public TestTreeCustom(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();

        Element parentElement = doc.getElementById("treeCustomId");
        ElementTreeNodeStructure structure = FirstLevelTreeStructure.SINGLETON;
        ElementTreeNodeRenderer renderer = FirstLevelTreeRenderer.SINGLETON;
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNodeList tree = factory.createElementTreeNodeList(false,parentElement,true,structure,renderer);
        String[] name;
        ElementTreeNode treeNode;
        ElementTreeNodeList nodeList;

        name = new String[] { "Jose", "Arranz" };
        treeNode = tree.addTreeNode(name);
            nodeList = treeNode.getChildTreeNodeList();
            nodeList.setElementTreeNodeStructure(SecondLevelTreeStructure.SINGLETON);
            nodeList.setElementTreeNodeRenderer(SecondLevelTreeRenderer.SINGLETON);
            nodeList.addTreeNode("Andrés");
            nodeList.addTreeNode("Pepa");

        name = new String[] { "Antonio", "Pérez" };
        treeNode = tree.addTreeNode(name);
            nodeList = treeNode.getChildTreeNodeList();
            nodeList.setElementTreeNodeStructure(SecondLevelTreeStructure.SINGLETON);
            nodeList.setElementTreeNodeRenderer(SecondLevelTreeRenderer.SINGLETON);
            nodeList.addTreeNode("Sergio");
            nodeList.addTreeNode("Luis");

        // Otra alternativa es detectar el nivel y usar una sola clase estructura y renderer
        // que funcionan de forma diferente segun el nivel.
    }
}

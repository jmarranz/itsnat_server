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

package org.itsnat.feashow.features.core.domutils;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.itsnat.core.domutil.ElementTreeNodeList;
import org.itsnat.core.domutil.ElementTreeNodeRenderer;
import org.itsnat.core.domutil.ElementTreeNodeStructure;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PatternBasedElementTreeTreeNode extends FeatureTreeNode
{
    public PatternBasedElementTreeTreeNode()
    {
    }

    public void startExamplePanel()
    {
        buildTree("treeId",false,null,null);
        buildTree("treeId2",false,null,null);
        buildTree("treeId3",false,null,null);
        buildTree("treeId4",false,null,null);
        buildTreeFixedRoot("treeId5",null,null);
        buildTreeRootless("treeId6",false,null,null);

        ElementTreeNodeRenderer customRenderer = new ElementTreeNodeRenderer()
        {
            public void renderTreeNode(ElementTreeNode treeNode,Object value,Element labelElem,boolean isNew)
            {
                int level = treeNode.getDeepLevel();

                String style;
                if (level == 0)
                    style = "font-size:large;";
                else if (level == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-style:italic;";

                labelElem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(labelElem,value.toString());
            }

            public void unrenderTreeNode(ElementTreeNode treeNode,Element labelElem)
            {
            }
        };

        ElementTreeNodeStructure customStructure = new ElementTreeNodeStructure()
        {
            public Element getContentElement(ElementTreeNode treeNode,Element nodeElem)
            {
                Element child = ItsNatTreeWalker.getFirstChildElement(nodeElem);
                return ItsNatTreeWalker.getFirstChildElement(child);
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
                return getContentElement(treeNode,nodeElem);
            }
            public Element getChildListElement(ElementTreeNode treeNode,Element nodeElem)
            {
                Element contentParentElem = getContentElement(treeNode,nodeElem);
                return ItsNatTreeWalker.getNextSiblingElement(contentParentElem);
            }
        };

        buildTree("treeId7",false,customStructure,customRenderer);

        // TreeTable with <table>
        buildTree("treeId8",true,null,customRenderer);

        // TreeTable without <table>
        buildTree("treeId9",true,null,customRenderer);
    }

    public void buildTree(String parentId,boolean treeTable,ElementTreeNodeStructure customStructure,ElementTreeNodeRenderer customRenderer)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById(parentId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTree elemTree = factory.createElementTree(treeTable,parent,true,customStructure,customRenderer);
        ElementTreeNode rootNode = elemTree.addRootNode("Spain");

        buildTree(rootNode);
    }

    public void buildTreeFixedRoot(String parentId,ElementTreeNodeStructure customStructure,ElementTreeNodeRenderer customRenderer)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById(parentId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNode rootNode = factory.createElementTreeNode(parent,true,customStructure,customRenderer);
        rootNode.setValue("Spain");
        buildTree(rootNode);
    }

    public void buildTreeRootless(String parentId,boolean treeTable,ElementTreeNodeStructure customStructure,ElementTreeNodeRenderer customRenderer)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById(parentId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTreeNodeList rootList = factory.createElementTreeNodeList(treeTable,parent,true,customStructure,customRenderer);

        buildTree(rootList);
    }

    public void buildTree(ElementTreeNode rootNode)
    {
        ElementTreeNodeList rootChildren = rootNode.getChildTreeNodeList();
        buildTree(rootChildren);
    }

    public void buildTree(ElementTreeNodeList rootChildren)
    {
        ElementTreeNode provincesNode = rootChildren.addTreeNode("Autonomous Communities");
        ElementTreeNodeList provinces = provincesNode.getChildTreeNodeList();
        provinces.addTreeNode("Asturias");
        provinces.addTreeNode("Cantabria");
        provinces.addTreeNode("Castilla La Mancha");

        ElementTreeNode ccaaNode = rootChildren.addTreeNode("Cities");
        ElementTreeNodeList ccaas = ccaaNode.getChildTreeNodeList();
        ccaas.addTreeNode("Madrid");
        ccaas.addTreeNode("Barcelona");
        ccaas.addTreeNode("Sevilla");
    }

    public void endExamplePanel()
    {
    }

}

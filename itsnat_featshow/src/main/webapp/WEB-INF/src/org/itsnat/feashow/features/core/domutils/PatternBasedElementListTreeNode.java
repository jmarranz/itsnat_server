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
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ElementListRenderer;
import org.itsnat.core.domutil.ElementListStructure;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class PatternBasedElementListTreeNode extends FeatureTreeNode
{
    public PatternBasedElementListTreeNode()
    {
    }

    public void startExamplePanel()
    {
        buildList("elementListId",null,null);

        buildList("elementListId2",null,null);

        ElementListRenderer customRenderer = new ElementListRenderer()
        {
            public void renderList(ElementList list,int index,Object value,Element elem,boolean isNew)
            {
                String style;
                if (index == 0)
                    style = "font-style:italic;";
                else if (index == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-size:large;";
                elem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(elem,value.toString());
            }

            public void unrenderList(ElementList list,int index,Element contentElem)
            {
            }
        };

        buildList("elementListId3",null,customRenderer);

        ElementListStructure customStructure = new ElementListStructure()
        {
            public Element getContentElement(ElementList list,int index,Element elem)
            {
                /*
                <tr>
                    <td>
                        <table border="1px">
                            <tbody>
                                <tr><td>Element Pattern</td></tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                 */
                HTMLTableRowElement rowElem = (HTMLTableRowElement)elem;
                HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem2 = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                HTMLTableCellElement cellElem2 = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem2);
                return cellElem2;
            }
        };

        buildList("elementListId4",customStructure,customRenderer);
    }

    public void buildList(String listId,ElementListStructure customStructure,ElementListRenderer customRenderer)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById(listId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parent,true,customStructure,customRenderer);
        elemList.addElement("Madrid");
        elemList.addElement("Barcelona");
        elemList.addElement("Sevilla");
    }

    public void endExamplePanel()
    {
    }

}

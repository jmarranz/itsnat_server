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
import org.itsnat.core.domutil.ElementTable;
import org.itsnat.core.domutil.ElementTableRenderer;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class PatternBasedElementTableTreeNode extends FeatureTreeNode
{
    public PatternBasedElementTableTreeNode()
    {
    }

    public void startExamplePanel()
    {

        buildTable("tableId",null,null);

        ElementTableRenderer customRenderer = new ElementTableRenderer()
        {
            public void renderTable(ElementTable table,int row,int col,Object value,Element elem,boolean isNew)
            {
                String style;
                if (row == 0)
                    style = "font-style:italic;";
                else if (row == 1)
                    style = "font-weight:bold;";
                else
                    style = "font-size:large;";
                elem.setAttribute("style",style);
                ItsNatDOMUtil.setTextContent(elem,value.toString());
            }

            public void unrenderTable(ElementTable table,int row,int col,Element cellContentElem)
            {
            }
        };

        buildTable("tableId2",null,customRenderer);


        ElementTableStructure customStructure = new ElementTableStructure()
        {
            /*
            <tbody id="tableId3">
                <tr> <-- row
                    <td>
                        <table>
                            <tbody>
                                <tr> <-- row content
                                    <td> <-- cell
                                        <table>
                                            <tbody>
                                                <tr>
                                                    <td>Cell pattern</td> <-- td: cell content
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                    ...
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
            */
            public Element getRowContentElement(ElementTable table,int row,Element elem)
            {
                HTMLTableRowElement rowElem = (HTMLTableRowElement)elem;
                HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem2 = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                return rowElem2;
            }

            public Element getCellContentElement(ElementTable table,int row,int col,Element elem)
            {
                HTMLTableCellElement cellElem = (HTMLTableCellElement)elem;
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                HTMLTableCellElement cellElem2 = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                return cellElem2;
            }

        };

        buildTable("tableId3",customStructure,customRenderer);

        buildTable("tableId4",null,null);
    }

    public void buildTable(String tableId,ElementTableStructure customStructure,ElementTableRenderer customRenderer)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element parent = doc.getElementById(tableId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable elemTable = factory.createElementTable(parent,true,customStructure,customRenderer);
        elemTable.setColumnCount(3);
        elemTable.addRow(new String[] {"Madrid","Plaza Mayor","Palacio Real"});
        elemTable.addRow(new String[] {"Sevilla","Plaza de España","Giralda"});
        elemTable.addRow(new String[] {"Segovia","Plaza del Azoguejo","Acueducto Romano"});
    }

    public void endExamplePanel()
    {
    }

}

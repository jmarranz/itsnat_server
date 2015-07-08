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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementRenderer;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

public class DOMRendererTreeNode extends FeatureTreeNode
{
    public DOMRendererTreeNode()
    {
    }

    public void startExamplePanel()
    {
        Date value = new Date();

        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementRenderer renderer = factory.createDefaultElementRenderer();
        renderer.render(null,value,doc.getElementById("elementId"),true);

        ElementRenderer customRenderer = new ElementRenderer()
        {
            public void render(Object userObj, Object value, Element elem,boolean isNew)
            {
                /*
                <table id="elementId2">
                    <tbody>
                        <tr><td>Year:</td><td>(year)</td></tr>
                        <tr><td>Month:</td><td>(month)</td></tr>
                        <tr><td>Day:</td><td>(day)</td></tr>
                    </tbody>
                </table>
                 */
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
                // Format: June 8,2007
                String date = format.format(value);
                int pos = date.indexOf(' ');
                String month = date.substring(0,pos);
                int pos2 = date.indexOf(',');
                String day = date.substring(pos + 1,pos2);
                String year = date.substring(pos2 + 1);

                HTMLTableElement table = (HTMLTableElement)elem;
                HTMLTableSectionElement tbody = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(table);

                HTMLTableRowElement yearRow = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbody);
                HTMLTableCellElement yearCell = (HTMLTableCellElement)yearRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(yearCell,year);

                HTMLTableRowElement monthRow = (HTMLTableRowElement)ItsNatTreeWalker.getNextSiblingElement(yearRow);
                HTMLTableCellElement monthCell = (HTMLTableCellElement)monthRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(monthCell,month);

                HTMLTableRowElement dayRow = (HTMLTableRowElement)ItsNatTreeWalker.getNextSiblingElement(monthRow);
                HTMLTableCellElement dayCell = (HTMLTableCellElement)dayRow.getCells().item(1);
                ItsNatDOMUtil.setTextContent(dayCell,day);
            }

            public void unrender(Object userObj,Element elem)
            {
            }
        };

        customRenderer.render(null,value,doc.getElementById("elementId2"),true);
    }

    public void endExamplePanel()
    {
    }

}

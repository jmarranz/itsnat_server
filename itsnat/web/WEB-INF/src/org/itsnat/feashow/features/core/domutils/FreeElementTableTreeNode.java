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

import java.util.Collections;
import java.util.Comparator;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementListFree;
import org.itsnat.core.domutil.ElementTableFree;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class FreeElementTableTreeNode extends FeatureTreeNode implements EventListener
{
    protected int cityColumn;
    protected int squareColumn;
    protected int monumentColumn;
    protected ElementListFree elemHeader;
    protected ElementTableFree elemTable;
    protected Element sortByCityButton;
    protected Element sortBySquareButton;
    protected Element sortByMonumentButton;
    protected Element rotateColumnsButton;
    protected HTMLInputElement masterCheckBox;

    public FreeElementTableTreeNode()
    {
    }

    public void startExamplePanel()
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        this.cityColumn = 0;
        this.squareColumn = 1;
        this.monumentColumn = 2;

        this.sortByCityButton = doc.getElementById("sortByCityId");
        ((EventTarget)sortByCityButton).addEventListener("click",this,false);

        this.sortBySquareButton = doc.getElementById("sortBySquareId");
        ((EventTarget)sortBySquareButton).addEventListener("click",this,false);

        this.sortByMonumentButton = doc.getElementById("sortByMonumentId");
        ((EventTarget)sortByMonumentButton).addEventListener("click",this,false);

        this.rotateColumnsButton = doc.getElementById("rotateColumnsId");
        ((EventTarget)rotateColumnsButton).addEventListener("click",this,false);

        this.masterCheckBox = (HTMLInputElement)doc.getElementById("masterId");
        itsNatDoc.addEventListener((EventTarget)masterCheckBox,"click",this,false,new NodePropertyTransport("checked",boolean.class));

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();

        Element headParent = doc.getElementById("elementTableHeadId");
        this.elemHeader = factory.createElementListFree(headParent,true);

        boolean master = masterCheckBox.getChecked();
        Element parent = doc.getElementById("elementTableBodyId");
        this.elemTable = factory.createElementTableFree(parent,master);

        elemTable.addRow(createRowCitySquare("Madrid","Plaza Mayor"));
        elemTable.addRow(createRowCitySquare("Sevilla","Plaza de España"));
        elemTable.addRow(createRowCitySquare("Segovia","Plaza del Azoguejo"));

        elemTable.addColumn(createColMonuments(new String[] { "Palacio Real","Giralda","Acueducto Romano" } ));
    }

    public Element createRowCitySquare(String city,String square)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element rowElem = doc.createElement("tr");
        rowElem.appendChild(createCell(city));
        rowElem.appendChild(createCell(square));
        return rowElem;
    }

    public Element[] createColMonuments(String[] names)
    {
        Element[] cols = new Element[names.length];
        for(int i = 0; i < cols.length; i++)
            cols[i] = createCell(names[i]);
        return cols;
    }

    public Element createCell(String text)
    {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element cellElem = doc.createElement("td");
        cellElem.appendChild(doc.createTextNode(text));
        return cellElem;
    }

    public void endExamplePanel()
    {
        this.elemHeader = null;
        this.elemTable = null;
        this.sortByCityButton = null;
        this.sortBySquareButton = null;
        this.sortByMonumentButton = null;
        this.rotateColumnsButton = null;
        this.masterCheckBox = null;
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == sortByCityButton)
        {
            Collections.sort(elemTable,new NodeComparator(cityColumn));
        }
        else if (currTarget == sortBySquareButton)
        {
            Collections.sort(elemTable,new NodeComparator(squareColumn));
        }
        else if (currTarget == sortByMonumentButton)
        {
            Collections.sort(elemTable,new NodeComparator(monumentColumn));
        }
        else if (currTarget == rotateColumnsButton)
        {
            cityColumn     = ++cityColumn     > 2 ? 0 : cityColumn;
            squareColumn   = ++squareColumn   > 2 ? 0 : squareColumn;
            monumentColumn = ++monumentColumn > 2 ? 0 : monumentColumn;
            elemHeader.moveElement(2,2,0);
            elemTable.moveColumn(2,0);
        }
        else if (currTarget == masterCheckBox)
        {
            boolean master = masterCheckBox.getChecked();
            if (master != elemTable.isMaster())
            {
                ItsNatDocument itsNatDoc = getItsNatDocument();
                ElementGroupManager factory = itsNatDoc.getElementGroupManager();
                elemTable = factory.createElementTableFree(elemTable.getParentElement(),master);
            }
        }
    }

    public static class NodeComparator implements Comparator
    {
        protected int col;

        public NodeComparator(int col)
        {
            this.col = col;
        }

        public int compare(Object o1, Object o2)
        {
            Element row1 = (Element)o1;
            Text text1 = getCellContent(row1,col);

            Element row2 = (Element)o2;
            Text text2 = getCellContent(row2,col);

            return text1.getData().compareTo(text2.getData());
        }

        public static Text getCellContent(Element row,int col)
        {
            Element cell = ItsNatTreeWalker.getFirstChildElement(row);
            for(int i = 1; i <= col; i++)
                cell = ItsNatTreeWalker.getNextSiblingElement(cell);

            return (Text)cell.getFirstChild();
        }
    }
}

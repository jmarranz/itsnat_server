/*
 * TestComplexHTMLTableStructure.java
 *
 * Created on 31 de marzo de 2007, 17:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import java.io.Serializable;
import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class TestComplexHTMLTableStructure implements ItsNatTableStructure,Serializable
{

    /** Creates a new instance of TestComplexHTMLTableStructure */
    public TestComplexHTMLTableStructure()
    {
    }

    public Element getHeadElement(ItsNatTable table,Element tableElem)
    {
        if (tableElem == null) tableElem = table.getElement();
        HTMLTableElement htmlTableElem = (HTMLTableElement)tableElem;
        return htmlTableElem.getTHead(); // Si no tiene <thead> devolverá null
    }

    public Element getBodyElement(ItsNatTable table,Element tableElem)
    {
        if (tableElem == null) tableElem = table.getElement();
        return (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElementWithTagName(tableElem,"tbody");
    }

    public Element getHeaderColumnContentElement(ItsNatTableHeader tableHeader, int index,Element parentElem)
    {
        if (index > 0)
        {
            parentElem.setAttribute("style","display:none");
        }
        return parentElem;
    }

        /*
        <table border="1px" cellspacing="1px">
            <tbody>
                <tr>
                    <td><!-- Contiene una fila entera -->
                        <table border="1px">
                            <tbody>
                                <tr><!-- Padre de la fila -->
                                    <td> <!-- Columna/celda -->
                                        <table border="1px">
                                            <tbody>
                                                <tr>
                                                    <td><b>Item 1,1</b></td> <!-- el <td> es el padre del contenido de la celda -->
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                    <td>
                                        ... Item 1,2
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
         */
    public Element getRowContentElement(ItsNatTable table, int row,Element rowElem)
    {
        HTMLTableRowElement rowTableElem = (HTMLTableRowElement)rowElem;
        HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowTableElem);
        HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
        HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
        rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
        return rowElem;
    }

    public Element getCellContentElement(ItsNatTable table, int row, int col,Element cellElem)
    {
        HTMLTableCellElement cellTableElem = (HTMLTableCellElement)cellElem;
        HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellTableElem);
        HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
        HTMLTableRowElement rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
        cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
        return cellElem;
    }

}

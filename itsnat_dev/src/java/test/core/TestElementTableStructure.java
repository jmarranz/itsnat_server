/*
 * TestElementTableStructure.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ElementTable;
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
public class TestElementTableStructure extends TestElementTableBase
{
    /**
     * Creates a new instance of TestElementTableStructure
     */
    public TestElementTableStructure(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        /*
        <table border="1px" cellspacing="1px">
            <tbody id="tableStructureId">
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

        ElementTableStructure structure = new ElementTableStructure()
        {
            public Element getRowContentElement(ElementTable table,int row,Element elem)
            {
                HTMLTableRowElement rowElem = (HTMLTableRowElement)elem;
                HTMLTableCellElement cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                return rowElem;
            }

            public Element getCellContentElement(ElementTable table,int row,int col,Element elem)
            {
                HTMLTableCellElement cellElem = (HTMLTableCellElement)elem;
                HTMLTableElement tableElem = (HTMLTableElement)ItsNatTreeWalker.getFirstChildElement(cellElem);
                HTMLTableSectionElement tbodyElem = (HTMLTableSectionElement)ItsNatTreeWalker.getFirstChildElement(tableElem);
                HTMLTableRowElement rowElem = (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElement(tbodyElem);
                cellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
                return cellElem;
            }

        };

        initTable("tableStructureId",structure);
    }

}

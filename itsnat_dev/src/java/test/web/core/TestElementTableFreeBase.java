/*
 * TestElementTableBase.java
 *
 * Created on 29 de diciembre de 2006, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementTableFree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestElementTableFreeBase
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestElementTableBase
     */
    public TestElementTableFreeBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void testShared(ElementTableFree table,Document doc)
    {
        // En las siguientes sentencias no se opera directamente con el DOM
        // pero simplemente para ver si funcionan en modo slave también (y master claro)


        table.addColumn(createColumn(doc,table.getRowCount(),2));

        table.removeColumnAt(1);
        table.insertColumnAt(1,createColumn(doc,table.getRowCount(),1));

        TestUtil.checkError(table.getTableCellElementInfoAt(0,2).getRowIndex() == 0);
        TestUtil.checkError(table.getTableCellElementInfoAt(0,2).getColumnIndex() == 2); // para ver si se ha actualizado bien
        if (table.isMaster())
        {
            // La misma instancia
            TestUtil.checkError(table.getTableCellElementInfoAt(0,2) == table.getTableCellElementInfoAt(0,2));
        }
        else
        {
            // Diferentes instancias
            TestUtil.checkError(table.getTableCellElementInfoAt(0,2) != table.getTableCellElementInfoAt(0,2));
        }

        if (table.isMaster())
        {
            // La misma instancia
            TestUtil.checkError(table.getRowListElementInfoAt(2) == table.getRowListElementInfoAt(2));
        }
        else
        {
            // Diferentes instancias
            TestUtil.checkError(table.getRowListElementInfoAt(2) != table.getRowListElementInfoAt(2));
        }

        Element cellElem0 = table.getCellElementAt(0,0);
        Element cellElem1 = table.getCellElementAt(1,0);
        Element cellElem2 = table.getCellElementAt(2,0);
        table.moveRow(0,1,1); // Desplaza las dos primeras filas a ser las dos finales
        TestUtil.checkError(table.getCellElementAt(0,0) == cellElem2);
        TestUtil.checkError(table.getCellElementAt(1,0) == cellElem0);
        TestUtil.checkError(table.getCellElementAt(2,0) == cellElem1);
        table.moveRow(1,2,0); // Las deja como estaba
        TestUtil.checkError(table.getCellElementAt(0,0) == cellElem0);
        TestUtil.checkError(table.getCellElementAt(1,0) == cellElem1);
        TestUtil.checkError(table.getCellElementAt(2,0) == cellElem2);
        TestUtil.checkError(table.getRowCount() == 3);


        Element colElem = table.getCellElementAt(0,0);
        table.moveColumn(0,2);
        TestUtil.checkError(table.getCellElementAt(0,2) == colElem);
        table.moveColumn(2,0); // La dejamos en su sitio de nuevo
        TestUtil.checkError(table.getCellElementAt(0,0) == colElem);
        TestUtil.checkError(table.getCellElementsOfRow(0).length == 3);
    }

    public static Element createRow(Document doc,int row)
    {
        Element elem = doc.createElement("div");
        elem.appendChild(createCell(doc,row,0));
        elem.appendChild(createCell(doc,row,1));
        return elem;
    }

    public static Element createCell(Document doc,int row,int col)
    {
        Element elem = doc.createElement("span");
        elem.appendChild(doc.createTextNode("Cell " + row + "," + col + " "));
        return elem;
    }

    public static Element[] createColumn(Document doc,int rowCount,int col)
    {
        Element[] elems = new Element[rowCount];
        for(int row = 0; row < rowCount; row++)
        {
            elems[row] = createCell(doc,row,col);
        }
        return elems;
    }
}

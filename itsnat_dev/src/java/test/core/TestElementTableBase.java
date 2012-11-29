/*
 * TestElementTableBase.java
 *
 * Created on 29 de diciembre de 2006, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTableStructure;
import org.itsnat.core.domutil.ElementTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementTableBase
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestElementTableBase
     */
    public TestElementTableBase(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void initTable(String tableId)
    {
        initTable(tableId,null);
    }

    public void initTable(String tableId,ElementTableStructure structure)
    {
        Document doc = itsNatDoc.getDocument();
        Element tableParent = doc.getElementById(tableId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable table = factory.createElementTable(tableParent,true,structure,null);

        table.removeAllColumns();
        // table.removeAllRows();
        table.addColumn(); // Se añade al patrón pues no hay filas
        table.addColumn(); // "
        table.addRow(new String[]{"Item 0,0","Item 0,1"}); // Fila con dos columnas
        table.addRow(new String[]{"Item 1,0","Item 1,1"}); // "
        table.addColumn(                  new String[]{"Item 0,2","Item 1,2"}); // Añade una columna a las dos filas
        table.addRow(new String[]{"Item 2,0","Item 2,1","Item 2,2"}); // Nueva fila con tres columnas
        TestUtil.checkError(table.getColumnCount() == 3);
        TestUtil.checkError(table.getRowCount() == 3);
        TestUtil.checkError(table.getCellElementAt(2,2) != null);

        table.setUsePatternMarkupToRender(true);
        table.setCellValueAt(0,0,"BAD Item 0,0");
        table.setCellValueAt(0,0,"Item 0,0");

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
        TestUtil.checkError(table.getColumnCount() == 3);

        table.setColumnCount(5);
        TestUtil.checkError(table.getColumnCount() == 5);

        table.setColumnCount(3); // restauramos quitando las dos últimas
        TestUtil.checkError(table.getColumnCount() == 3);

        table.setRowCount(5);
        TestUtil.checkError(table.getRowCount() == 5);

        table.setRowCount(3); // restauramos quitando las dos últimas
        TestUtil.checkError(table.getRowCount() == 3);
    }
}

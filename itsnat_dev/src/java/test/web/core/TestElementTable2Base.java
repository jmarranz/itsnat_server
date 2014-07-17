/*
 * TestElementTable2Base.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestElementTable2Base
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestElementTable2Base
     */
    public TestElementTable2Base(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void initTable(String tableId)
    {
        Document doc = itsNatDoc.getDocument();
        Element tableParent = doc.getElementById(tableId);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTable table = factory.createElementTable(tableParent,false);

        TestUtil.checkError(table.getColumnCount() == 2);

        // En este ejemplo nos basamos en las filas y columnas originales y añadimos más,
        // el patrón de las 2 primeras celdas de la primera fila se conservará
        table.addColumn();  // El patrón será el del primer elemento (el 0,0 o 1,1 visualmente)
        table.addRow(new String[]{"Item 2,1","Item 2,2","Item 2,3"});
        table.addRow(new String[]{"Item 3,1","Item 3,2","Item 3,3"});

        TestUtil.checkError(table.getRowCount() == 3);
    }

}

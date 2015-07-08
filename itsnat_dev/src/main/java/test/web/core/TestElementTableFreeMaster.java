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
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementTableFree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementTableFreeMaster extends TestElementTableFreeBase
{

    /**
     * Creates a new instance of TestElementTableBase
     */
    public TestElementTableFreeMaster(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable();
    }

    public void initTable()
    {
        Document doc = itsNatDoc.getDocument();
        Element tableParent = doc.getElementById("tableFreeMasterId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTableFree table = factory.createElementTableFree(tableParent,true);

        table.addRow(createRow(doc,0));
        table.addRow(createRow(doc,1));
        table.addRow(createRow(doc,2));

        TestUtil.checkError(table.getRowCount() == 3);

        table.removeRowAt(1);
        table.insertRowAt(1,createRow(doc,1)); // Insertamos de nuevo

        TestUtil.checkError(table.getRowListElementInfoAt(2).getIndex() == 2); // para ver si se ha actualizado bien

        TestUtil.checkError(table.getRowCount() == 3);

        testShared(table,doc);
    }

}

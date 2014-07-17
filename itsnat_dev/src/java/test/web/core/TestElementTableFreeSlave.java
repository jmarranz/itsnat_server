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
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementTableFreeSlave extends TestElementTableFreeBase
{
    /**
     * Creates a new instance of TestElementTableBase
     */
    public TestElementTableFreeSlave(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable();
    }

    public void initTable()
    {
        Document doc = itsNatDoc.getDocument();
        Element tableParent = doc.getElementById("tableFreeSlaveId");
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementTableFree table = factory.createElementTableFree(tableParent,false);

        tableParent.appendChild(createRow(doc,0));
        tableParent.appendChild(createRow(doc,1));
        tableParent.appendChild(createRow(doc,2));

        TestUtil.checkError(table.getRowCount() == 3);

        tableParent.removeChild(table.getRowElementAt(1));
        tableParent.insertBefore(createRow(doc,1),table.getRowElementAt(1)); // Insertamos de nuevo

        TestUtil.checkError(table.getRowCount() == 3);

        // En las siguientes sentencias no se opera directamente con el DOM
        // pero simplemente para ver si funcionan en modo slave
        testShared(table,doc);
    }

}

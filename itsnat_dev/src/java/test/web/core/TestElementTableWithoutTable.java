/*
 * TestElementTableWithoutTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementTableWithoutTable extends TestElementTableBase
{

    /**
     * Creates a new instance of TestElementTableWithoutTable
     */
    public TestElementTableWithoutTable(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable("tableWithoutTableId");
    }

}

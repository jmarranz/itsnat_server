/*
 * TestElementTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;

/**
 *
 * @author jmarranz
 */
public class TestElementTable extends TestElementTableBase
{
    /**
     * Creates a new instance of TestElementTable
     */
    public TestElementTable(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable("tableId");
    }

}

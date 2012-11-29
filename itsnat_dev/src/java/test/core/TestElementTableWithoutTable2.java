/*
 * TestElementTableWithoutTable2.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;

/**
 *
 * @author jmarranz
 */
public class TestElementTableWithoutTable2 extends TestElementTable2Base
{

    /**
     * Creates a new instance of TestElementTableWithoutTable2
     */
    public TestElementTableWithoutTable2(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable("tableWithoutTableId2");
    }


}

/*
 * TestTreeNoRoot.java
 *
 * Created on 18 de agosto de 2007, 16:23
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
public class TestTreeTableNoRoot extends TestTreeNoRootBase
{

    /** Creates a new instance of TestTreeNoRoot */
    public TestTreeTableNoRoot(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        load("treeTableNoRootId",true);
    }

}

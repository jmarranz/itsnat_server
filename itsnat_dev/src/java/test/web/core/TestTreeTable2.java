/*
 * TestTreeTable2.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementTree;
import org.itsnat.core.domutil.ElementTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestTreeTable2 extends TestTreeTableBase
{

    /**
     * Creates a new instance of TestTreeTable2
     */
    public TestTreeTable2(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        load("treeTableId2");
    }

}

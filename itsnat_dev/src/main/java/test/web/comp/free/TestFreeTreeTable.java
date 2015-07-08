/*
 * TestTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.core.html.ItsNatHTMLDocument;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestFreeTreeTable extends TestFreeTreeBase implements EventListener,TreeModelListener,TreeSelectionListener,TreeExpansionListener,TreeWillExpandListener
{

    /**
     * Creates a new instance of TestTable
     */
    public TestFreeTreeTable(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTree("freeTreeTableId","addNodeFreeTreeTableId","removeNodeFreeTreeTableId","reloadFreeTreeTableId","joystickModeFreeTreeTableId");
    }

}

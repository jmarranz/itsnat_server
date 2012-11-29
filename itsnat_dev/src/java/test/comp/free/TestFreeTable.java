/*
 * TestTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.table.ItsNatFreeTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLElement;
import test.comp.*;

/**
 *
 * @author jmarranz
 */
public class TestFreeTable extends TestTableBase implements EventListener,TableModelListener,ListSelectionListener
{

    /**
     * Creates a new instance of TestTable
     */
    public TestFreeTable(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable();
    }

    public void initTable()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLElement tableElem = (HTMLElement)doc.getElementById("freeTableId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatFreeTable comp = (ItsNatFreeTable)componentMgr.findItsNatComponent(tableElem);
        initTable(comp,"addRowFreeTableId","removeRowFreeTableId","joystickModeFreeTableId");
    }


}

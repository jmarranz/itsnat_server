/*
 * TestHTMLTable.java
 *
 * Created on 6 de diciembre de 2006, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.table.ItsNatHTMLTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLTableElement;
import test.web.comp.TestTableBase;

/**
 *
 * @author jmarranz
 */
public class TestHTMLTable extends TestTableBase implements EventListener,TableModelListener,ListSelectionListener
{
    /**
     * Creates a new instance of TestHTMLTable
     */
    public TestHTMLTable(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initTable();
    }

    public void initTable()
    {
        Document doc = itsNatDoc.getDocument();
        HTMLTableElement tableElem = (HTMLTableElement)doc.getElementById("tableId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatHTMLTable comp = (ItsNatHTMLTable)componentMgr.findItsNatComponent(tableElem);
        initTable(comp,"addRowTableId","removeRowTableId","joystickModeTableId");
    }

}

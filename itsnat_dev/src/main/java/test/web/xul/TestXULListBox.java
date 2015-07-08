/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.xul;

import java.io.Serializable;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.list.ItsNatListUI;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.core.event.NodePropertyTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestXULListBox implements EventListener,ListSelectionListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatFreeButtonNormal buttonAdd;

    public TestXULListBox(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        Document doc = itsNatDoc.getDocument();
        Element listElem = doc.getElementById("listId");
        this.listComp = (ItsNatFreeListMultSel)compMgr.addItsNatComponent(listElem,"freeListMultSel",null);
        listComp.setItsNatListCellRenderer(new ListItemRenderer());
        //listComp.setItsNatListCellEditor(null);
        listComp.getListSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listComp.getListSelectionModel().addListSelectionListener(this);

        itsNatDoc.addEventListener((EventTarget)listElem,"select",this,false, new NodePropertyTransport("selectedIndex",false));

        DefaultListModel model = (DefaultListModel)listComp.getListModel();
        model.addElement("Item 0");
        model.addElement("Item 1");
        listComp.getListSelectionModel().setSelectionInterval(1,1);

        this.buttonAdd = (ItsNatFreeButtonNormal)compMgr.addItsNatComponentById("addItemId","freeButtonNormal",null);
        buttonAdd.addEventListener("click",this);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == buttonAdd.getElement())
        {
            DefaultListModel model = (DefaultListModel)listComp.getListModel();
            int len = model.getSize();
            model.addElement("Item " + len);
        }
        else if (currTarget == listComp.getElement())
        {
            // Para testear que se recibe
            int index = Integer.parseInt((String)((ItsNatEvent)evt).getExtraParam("selectedIndex"));
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();
        syncSelectionInServer(first,last);
    }

    public void syncSelectionInServer(int first,int last)
    {
        ListSelectionModel selModel = listComp.getListSelectionModel();
        ItsNatListUI compUI = listComp.getItsNatListUI();
        for(int i = first; i <= last; i++)
        {
            Element elem = compUI.getElementAt(i);
            boolean selected = selModel.isSelectedIndex(i);
            if (selected)
                elem.setAttribute("selected","true");
            else
                elem.removeAttribute("selected");
        }
    }
}

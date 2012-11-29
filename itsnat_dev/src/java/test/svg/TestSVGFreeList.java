/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.svg;

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
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public class TestSVGFreeList implements EventListener,ListSelectionListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected ItsNatFreeListMultSel listComp;
    protected ItsNatFreeButtonNormal buttonAddRect;

    public TestSVGFreeList(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        Document doc = itsNatDoc.getDocument();
        Element rectListParent = doc.getElementById("rectListId");
        Element rectPattern = ItsNatTreeWalker.getFirstChildElement(rectListParent);
        int x_init = Integer.parseInt(rectPattern.getAttribute("x"));

        this.listComp = (ItsNatFreeListMultSel)compMgr.addItsNatComponent(rectListParent,"freeListMultSel",null);
        listComp.setItsNatListCellRenderer(new RectRenderer());
        //listComp.setItsNatListCellEditor(null);
        listComp.getListSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listComp.getListSelectionModel().addListSelectionListener(this);

        DefaultListModel model = (DefaultListModel)listComp.getListModel();
        model.addElement(new Rect(x_init));

        this.buttonAddRect = (ItsNatFreeButtonNormal)compMgr.addItsNatComponentById("addRectId","freeButtonNormal",null);
        buttonAddRect.addEventListener("click",this);

        decorate(0,listComp.getListModel().getSize() - 1);
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == buttonAddRect.getElement())
        {
            // Al menos hay uno
            DefaultListModel model = (DefaultListModel)listComp.getListModel();
            int len = model.getSize();
            int x = ((Rect)model.getElementAt(len - 1)).getX();
            model.addElement(new Rect(x + 70));
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();
        decorate(first,last);
    }

    public void decorate(int first,int last)
    {
        ListSelectionModel selModel = listComp.getListSelectionModel();
        ItsNatListUI compUI = listComp.getItsNatListUI();
        for(int i = first; i <= last; i++)
        {
            Element elem = compUI.getElementAt(i);
            boolean selected = selModel.isSelectedIndex(i);
            if (selected)
                elem.setAttribute("fill","#ff0000");
            else
                elem.setAttribute("fill","#0000ff");
        }
    }
}

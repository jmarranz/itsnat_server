/*
 * TestMobileFreeListMultiple.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.iepocket;

import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.event.ChangeEvent;
import test.mobile.*;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableCellElement;

/**
 *
 * @author jmarranz
 */
public class TestIEPocketFreeListMultiple extends TestMobileListMultipleBase implements ChangeListener
{
    protected ItsNatFreeListMultSel comp;
    protected ItsNatHTMLButton addButton;
    protected ItsNatHTMLButton removeButton;
    protected ItsNatHTMLInputCheckBox editInplace;

    /**
     * Creates a new instance of TestMobileFreeListMultiple
     */
    public TestIEPocketFreeListMultiple(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        initListMultiple();
    }

    public ItsNatListMultSel getItsNatList()
    {
        return comp;
    }

    public void initListMultiple()
    {
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        HTMLElement parentElem = (HTMLElement)doc.getElementById("freeListMultipleId");
        ItsNatHTMLComponentManager compMgr = itsNatDoc.getItsNatHTMLComponentManager();
        ItsNatListStructure structure = new IEPocketListStructure(compMgr.createDefaultItsNatListStructure());
        this.comp = compMgr.createItsNatFreeListMultSel(parentElem, structure, null);
        //comp.setJoystickMode(true);

        super.initListMultiple();

        comp.setSelectionUsesKeyboard(false);

        comp.addEventListener("click",this);

        comp.setEditorActivatorEvent("click");

        ListSelectionModel selModel = comp.getListSelectionModel();
        int size = comp.getListModel().getSize();
        for(int i = 0; i < size; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }

        this.addButton = (ItsNatHTMLButton)compMgr.createItsNatComponentById("addRowsFreeListId");
        addButton.addEventListener("click",this);

        this.removeButton = (ItsNatHTMLButton)compMgr.createItsNatComponentById("removeRowsFreeListId");
        removeButton.addEventListener("click",this);

        this.editInplace = (ItsNatHTMLInputCheckBox)compMgr.createItsNatComponentById("editInplaceId");
        ToggleButtonModel dataModel = editInplace.getToggleButtonModel();
        dataModel.setSelected(true);
        dataModel.addChangeListener(this);

        comp.setEditingEnabled(dataModel.isSelected());
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == addButton.getElement())
            addNewRows();
        else if (currTarget == removeButton.getElement())
        {
            removeRows();
        }
        else
            super.handleEvent(evt);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        super.valueChanged(e);

        ListSelectionModel selModel = (ListSelectionModel)e.getSource();
        if (selModel.getValueIsAdjusting())
            return;

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        for(int i = first; i <= last; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }
    }

    public void decorateSelection(int index,boolean selected)
    {
        HTMLAnchorElement option = (HTMLAnchorElement)comp.getItsNatListUI().getContentElementAt(index);
        if (option == null) return; // Hay un caso extraño en donde el index no es correcto en un ListSelectionEvent (cuando la lista se vacía y se añade un elemento) yo creo que es un bug de Swing

        HTMLTableCellElement cell = (HTMLTableCellElement)option.getParentNode();
        if (selected)
        {
            cell.setAttribute("style","background:rgb(0,0,255);");
            option.setAttribute("style", "color:white;");
        }
        else
        {
            cell.removeAttribute("style");
            option.setAttribute("style", "color:black;");
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        ToggleButtonModel dataModel = (ToggleButtonModel)e.getSource();
        comp.setEditingEnabled(dataModel.isSelected());
    }
}

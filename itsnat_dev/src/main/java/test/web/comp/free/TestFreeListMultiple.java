/*
 * TestFreeListMultiple.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLElement;
import test.web.comp.TestListMultipleBase;
import test.web.comp.TestSelectionUtil;

/**
 *
 * @author jmarranz
 */
public class TestFreeListMultiple extends TestListMultipleBase
{
    protected ItsNatFreeListMultSel comp;
    protected ItsNatHTMLButton addButton;
    protected ItsNatHTMLButton removeButton;
    protected ItsNatHTMLInputCheckBox joystickModeCheck;

    /**
     * Creates a new instance of TestFreeListMultiple
     */
    public TestFreeListMultiple(ItsNatHTMLDocument itsNatDoc)
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
        Document doc = itsNatDoc.getDocument();
        HTMLElement parentElem = (HTMLElement)doc.getElementById("freeListMultipleId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.comp = (ItsNatFreeListMultSel)componentMgr.findItsNatComponent(parentElem);

        super.initListMultiple();

        comp.addEventListener("click",this);

        // comp.setSelectionModel(null);

        ListSelectionModel selModel = comp.getListSelectionModel();
        int size = comp.getListModel().getSize();
        for(int i = 0; i < size; i++)
        {
            decorateSelection(i,selModel.isSelectedIndex(i));
        }

        this.addButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById("addItemFreeListId");
        addButton.addEventListener("click",this);

        this.removeButton = (ItsNatHTMLButton)componentMgr.findItsNatComponentById("removeItemFreeListId");
        removeButton.addEventListener("click",this);

        this.joystickModeCheck = (ItsNatHTMLInputCheckBox)componentMgr.findItsNatComponentById("joystickModeFreeListId");
        joystickModeCheck.addEventListener("click",this);
        joystickModeCheck.setSelected(itsNatDoc.isJoystickMode());
    }

    public void handleEvent(Event evt)
    {
        if (evt.getCurrentTarget() == addButton.getElement())
            addNewRow();  // Lo hacemos aparte via botón para que no influya en la selección de celdas
        else if (evt.getCurrentTarget() == removeButton.getElement())
            removeLastRow();
        else if (evt.getCurrentTarget() == joystickModeCheck.getElement())
            comp.setJoystickMode(joystickModeCheck.isSelected());
        else
            super.handleEvent(evt); // pulsado el componente
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
        Element option = comp.getItsNatListUI().getContentElementAt(index);
        if (option == null) return; // Hay un caso extraño en donde el index no es correcto en un ListSelectionEvent (cuando la lista se vacía y se añade un elemento) yo creo que es un bug de Swing
        TestSelectionUtil.decorateSelection(option,selected);
    }
}

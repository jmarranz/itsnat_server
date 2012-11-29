/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.mobile;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class TestMobileSelectMultiple extends TestMobileListMultipleBase
{
    protected ItsNatHTMLSelectMult comp;
    protected HTMLElement addRowsElem;
    protected HTMLElement removeRowsElem;
    protected HTMLElement reinsertSelect;
    protected HTMLElement changeOptions;
    protected HTMLElement invertSelection;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestMobileSelectMultiple(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
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
        HTMLSelectElement selectElem = (HTMLSelectElement)doc.getElementById("selectIdMultiple");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.comp = (ItsNatHTMLSelectMult)componentMgr.createItsNatComponent(selectElem);

        super.initListMultiple();

        comp.addEventListener("change",this);

        this.addRowsElem = (HTMLElement)doc.getElementById("addRowsSelectId");
        ((EventTarget)addRowsElem).addEventListener("click",this,false);

        this.removeRowsElem = (HTMLElement)doc.getElementById("removeRowsSelectId");
        ((EventTarget)removeRowsElem).addEventListener("click",this,false);

        this.reinsertSelect = (HTMLElement)doc.getElementById("reinsertSelectId");
        ((EventTarget)reinsertSelect).addEventListener("click",this,false);

        this.changeOptions = (HTMLElement)doc.getElementById("changeOptionsId");
        ((EventTarget)changeOptions).addEventListener("click",this,false);

        this.invertSelection = (HTMLElement)doc.getElementById("invertSelectionId");
        ((EventTarget)invertSelection).addEventListener("click",this,false);

    }


    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == addRowsElem)
            addNewRows();
        else if (currTarget == removeRowsElem)
            removeRows();
        else if (currTarget == reinsertSelect)
            reinsertSelect();
        else if (currTarget == changeOptions)
            changeOptions();
        else if (currTarget == invertSelection)
            invertSelection();
        else
            super.handleEvent(evt);
    }

    public void reinsertSelect()
    {
        // Esto es para testear algunos navegadores tal y como el iPhone, S40WebKit y Android
        // que necesitan "arreglar" los <select> multiple. Para ver si funciona.

        HTMLSelectElement selectElem = comp.getHTMLSelectElement();
        comp.dispose();

        ((EventTarget)addRowsElem).removeEventListener("click",this,false);
        ((EventTarget)removeRowsElem).removeEventListener("click",this,false);
        ((EventTarget)reinsertSelect).removeEventListener("click",this,false);
        ((EventTarget)changeOptions).removeEventListener("click",this,false);

        Node parentNode = selectElem.getParentNode();
        Node nextNode = selectElem.getNextSibling();
        parentNode.removeChild(selectElem);
        parentNode.insertBefore(selectElem, nextNode);

        initListMultiple();
   }

    public void changeOptions()
    {
        // Para ver si entera el control y por ejemplo en Android no se selecciona
        // automáticamente el primer elemento.
        DefaultListModel model = (DefaultListModel)comp.getListModel();
        for(int i = 0; i < model.getSize(); i++)
        {
            String value = model.getElementAt(i).toString();
            model.setElementAt(value + "+", i);
        }
    }

    public void invertSelection()
    {
        ListSelectionModel selModel = comp.getListSelectionModel();
        int min = selModel.getMinSelectionIndex();
        int max = selModel.getMaxSelectionIndex();
        int size = comp.getListModel().getSize();
        if ((min >= 0)&&(max >= 0))
            selModel.removeSelectionInterval(min,max);
        if (min > 0)
            selModel.addSelectionInterval(0,min - 1);
        if ((max >= 0)&&((size - 1) >= (max + 1)))
            selModel.addSelectionInterval(max + 1,size - 1);
        if ((min < 0)&&(max < 0))
            selModel.addSelectionInterval(0,size - 1);
        // Recuerda que el resultado de la selección debe ser un tramo continuo
    }
}

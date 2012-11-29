/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.iepocket;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLSelectElement;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestIEPocketSelectMultiple extends TestIEPocketListMultipleBase
{
    protected ItsNatHTMLSelectMult comp;
    protected HTMLElement addRowsElem;
    protected HTMLElement removeRowsElem;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestIEPocketSelectMultiple(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc);

        initListMultiple(request);
    }

    public ItsNatListMultSel getItsNatList()
    {
        return comp;
    }

    public void initListMultiple(ItsNatServletRequest request)
    {
        Document doc = itsNatDoc.getDocument();
        HTMLSelectElement selectElem = (HTMLSelectElement)doc.getElementById("selectIdMultiple");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        this.comp = (ItsNatHTMLSelectMult)componentMgr.createItsNatComponent(selectElem);

        super.initListMultiple();

        comp.addEventListener("change",this);

        this.addRowsElem = (HTMLElement)doc.getElementById("addRowsId");
        ((EventTarget)addRowsElem).addEventListener("click",this,false);

        this.removeRowsElem = (HTMLElement)doc.getElementById("removeRowsId");
        ((EventTarget)removeRowsElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == addRowsElem)
            addNewRows();
        else if (currTarget == removeRowsElem)
            removeRows();
        else
            super.handleEvent(evt);
    }

}

/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.html;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatListMultSel;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLSelectElement;
import test.web.comp.TestListMultipleBase;

/**
 *
 * @author jmarranz
 */
public class TestHTMLSelectMultiple extends TestListMultipleBase
{
    protected ItsNatHTMLSelectMult comp;

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestHTMLSelectMultiple(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
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
        this.comp = (ItsNatHTMLSelectMult)componentMgr.findItsNatComponent(selectElem);

        super.initListMultiple();

        comp.addEventListener("change",this);
    }

    public void handleEvent(Event evt)
    {
        super.handleEvent(evt);

        addNewRow();
    }

}

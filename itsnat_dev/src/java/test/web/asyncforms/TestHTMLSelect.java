/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.asyncforms;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;
import test.web.shared.TestBaseHTMLDocument;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public abstract class TestHTMLSelect extends TestBaseHTMLDocument implements EventListener
{
    protected HTMLSelectElement selectElem;
    protected Element addItemElem;
    protected Element removeItemElem;
    protected Element selItemElem;

    public TestHTMLSelect(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request,
            String selectId,String addItemSelId,String removeItemSelId,String selItemSelId)
    {
        super(itsNatDoc);

        Document doc = itsNatDoc.getDocument();

        this.selectElem = (HTMLSelectElement)doc.getElementById(selectId);
        ((EventTarget)selectElem).addEventListener("change", this, false);

        this.addItemElem = doc.getElementById(addItemSelId);
        ((EventTarget)addItemElem).addEventListener("click", this, false);

        this.removeItemElem = doc.getElementById(removeItemSelId);
        ((EventTarget)removeItemElem).addEventListener("click", this, false);

        this.selItemElem = doc.getElementById(selItemSelId);
        ((EventTarget)selItemElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        String type = evt.getType();
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == selectElem)
        {
            HTMLCollection col = selectElem.getOptions();
            String str = type + " ";
            for(int i = 0; i < col.getLength(); i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)col.item(i);
                Text item = (Text)option.getFirstChild();
                boolean selected = option.getSelected();
                str += item.getData() + "(" + selected + ") ";
            }
            outText(str);
        }
        else if (currTarget == addItemElem)
        {
            Document doc = itsNatDoc.getDocument();
            HTMLCollection col = selectElem.getOptions();
            HTMLElement option;

            HTMLElement firstOpt = (HTMLElement)col.item(0); // puede ser null
            option = (HTMLElement)doc.createElement("option");
            option.appendChild(doc.createTextNode(Integer.toString(col.getLength())));
            selectElem.add(option, firstOpt);

            option = (HTMLElement)doc.createElement("option");
            option.appendChild(doc.createTextNode(Integer.toString(col.getLength())));
            selectElem.add(option,null);
        }
        else if (currTarget == removeItemElem)
        {
            if (selectElem.getLength() > 0)
                selectElem.remove(0);
            if (selectElem.getLength() > 0)
                selectElem.remove(selectElem.getLength() - 1);
        }
    }
}

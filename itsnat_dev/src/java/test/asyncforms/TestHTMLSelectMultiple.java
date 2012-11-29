/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.asyncforms;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;

/**
 *
 * @author jmarranz
 */
public class TestHTMLSelectMultiple extends TestHTMLSelect
{
    protected Element changeItemElem;

    public TestHTMLSelectMultiple(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc,request,"selMulId","addItemSelMulId","removeItemSelMulId","selItemSelMulId");

        Document doc = itsNatDoc.getDocument();

        this.changeItemElem = doc.getElementById("changeItemSelMulId");
        ((EventTarget)changeItemElem).addEventListener("click", this, false);
    }

    public void handleEvent(Event evt)
    {
        super.handleEvent(evt);

        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == changeItemElem)
        {
            HTMLCollection col = selectElem.getOptions();
            for(int i = 0; i < col.getLength(); i++)
            {
                Element option = (Element)col.item(i);
                Text item = (Text)option.getFirstChild();
                item.setData(item.getData() + "+");
            }
        }
        else if (currTarget == selItemElem)
        {
            // Invertimos la selección
            HTMLCollection col = selectElem.getOptions();
            for(int i = 0; i < col.getLength(); i++)
            {
                Element option = (Element)col.item(i);
                boolean selected = !option.getAttribute("selected").equals("");
                if (selected)
                    option.removeAttribute("selected");
                else
                    option.setAttribute("selected","selected");
            }
        }
    }
}

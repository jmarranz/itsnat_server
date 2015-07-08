/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.web.asyncforms;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLOptionElement;

/**
 *
 * @author jmarranz
 */
public class TestHTMLSelectComboBox extends TestHTMLSelect
{
    public TestHTMLSelectComboBox(ItsNatHTMLDocument itsNatDoc,ItsNatServletRequest request)
    {
        super(itsNatDoc,request,"selComboId","addItemSelComboId","removeItemSelComboId","selItemSelComboId");
    }

    public void handleEvent(Event evt)
    {
        super.handleEvent(evt);

        EventTarget currTarget = evt.getCurrentTarget();

        if (currTarget == selItemElem)
        {
            // Invertimos la selección
            HTMLCollection col = selectElem.getOptions();
            int len = col.getLength();
            for(int i = 0; i < len; i++)
            {
                HTMLOptionElement option = (HTMLOptionElement)col.item(i);
                boolean selected = option.getSelected();
                if (selected)
                {
                    option.removeAttribute("selected");
                    i++;
                    i = i % len;
                    option = (HTMLOptionElement)col.item(i);
                    option.setAttribute("selected","selected");
                    break;
                }
            }
        }
    }
}

/*
 * TestSelectComboBoxListener.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.droid.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.droid.CheckBox;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import test.droid.shared.TestDroidBase;

/**
 *
 * @author jmarranz
 */
public class TestDroidCheckBox extends TestDroidBase implements EventListener
{

    /**
     * Creates a new instance of TestSelectComboBoxListener
     */
    public TestDroidCheckBox(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        initCheckBox();
    }

    public void initCheckBox()
    {
        Document doc = itsNatDoc.getDocument();
        Element checkBoxElem = doc.getElementById("checkBoxId");
        ItsNatComponentManager componentMgr = itsNatDoc.getItsNatComponentManager();
        CheckBox input = (CheckBox)componentMgr.findItsNatComponent(checkBoxElem);

        input.addEventListener("click",this);
    }

    public void handleEvent(Event evt)
    {
        itsNatDoc.addCodeToSend("alert(\"BIEN\");");
        //logToTextView(null, "OK " + evt.getType() + " ");
    }


}

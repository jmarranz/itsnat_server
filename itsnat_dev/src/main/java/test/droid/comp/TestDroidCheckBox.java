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
import org.itsnat.comp.android.widget.CheckBox;
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
    protected CheckBox checkBox;
    
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
        this.checkBox = (CheckBox)componentMgr.findItsNatComponent(checkBoxElem);

        checkBox.addEventListener("click",this);
    }

    @Override
    public void handleEvent(Event evt)
    {
        Element logElem = getDocument().getElementById("checkBox_text_Id");
        
        logToTextView(logElem, "OK " + evt.getType() + " " + checkBox.isChecked() + " ");
    }
}

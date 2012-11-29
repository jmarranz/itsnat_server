/*
 * TestHTMLLabelEditors.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.comp.free;

import java.beans.PropertyChangeListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.w3c.dom.events.EventListener;
import test.comp.TestLabelEditorsBase;

/**
 *
 * @author jmarranz
 */
public class TestFreeLabelEditors extends TestLabelEditorsBase
{
    /**
     * Creates a new instance of TestHTMLLabelEditors
     */
    public TestFreeLabelEditors(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        labels = new ItsNatFreeLabel[6];

        load( new String[]
               {"testItemEditorFreeId1","testItemEditorFreeId2","testItemEditorFreeId3",
                "testItemEditorFreeId4","testItemEditorFreeId5","testItemEditorFreeId6"}
            );
    }


}

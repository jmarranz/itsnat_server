/*
 * TestHTMLLabelEditors.java
 *
 * Created on 26 de octubre de 2006, 16:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.comp.free;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.core.ItsNatServletRequest;
import test.web.comp.TestLabelEditorsBase;

/**
 *
 * @author jmarranz
 */
public class TestFreeLabelEditors extends TestLabelEditorsBase
{
    /**
     * Creates a new instance of TestHTMLLabelEditors
     */
    public TestFreeLabelEditors(ItsNatServletRequest request,ItsNatHTMLDocument itsNatDoc)
    {
        super(request,itsNatDoc);

        labels = new ItsNatFreeLabel[6];

        load( new String[]
               {"testItemEditorFreeId1","testItemEditorFreeId2","testItemEditorFreeId3",
                "testItemEditorFreeId4","testItemEditorFreeId5","testItemEditorFreeId6"}
            );
    }


}

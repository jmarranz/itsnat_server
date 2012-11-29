/*
 * TestClassAttribute.java
 *
 * Created on 9 de marzo de 2007, 13:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TestSetOnClickAttribute
{
    /** Creates a new instance of TestClassAttribute */
    public TestSetOnClickAttribute(ItsNatDocument itsNatDoc)
    {
        load(itsNatDoc);
    }

    public void load(ItsNatDocument itsNatDoc)
    {
        Element elem = itsNatDoc.getDocument().getElementById("testSetOnClickAttrId");
        elem.setAttribute("onclick","alert('OK');");
    }

}

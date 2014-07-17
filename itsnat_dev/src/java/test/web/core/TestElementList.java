/*
 * TestElementList.java
 *
 * Created on 11 de julio de 2007, 11:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestElementList
{

    /**
     * Creates a new instance of TestElementList
     */
    public TestElementList(ItsNatHTMLDocument itsNatDoc)
    {
        test(itsNatDoc);
    }

    public void test(ItsNatHTMLDocument itsNatDoc)
    {
        Document doc = itsNatDoc.getDocument();
        Element parent = doc.getElementById("testElementListId");

        // Modo master
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList elemList = factory.createElementList(parent,true);

        elemList.addElement("Item 0");
        elemList.addElement("Item 2");
        elemList.insertElementAt(1,"Item 1");

        elemList.setUsePatternMarkupToRender(true);
        elemList.addElement("Item 3 BAD");
        elemList.setElementValueAt(3,"Item 3");

        TestUtil.checkError(elemList.getLength() == 4);
    }


}

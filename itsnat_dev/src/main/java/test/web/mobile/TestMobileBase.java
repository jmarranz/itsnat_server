/*
 * TestMobileBase.java
 *
 * Created on 18 de diciembre de 2006, 21:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.mobile;

import java.io.Serializable;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class TestMobileBase implements Serializable
{
    protected ItsNatHTMLDocument itsNatDoc;

    /**
     * Creates a new instance of TestMobileBase
     */
    public TestMobileBase(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void outText(String msg)
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element parent = doc.getElementById("logId");
        parent.appendChild(doc.createTextNode(msg + ".")); // Para que se vea
    }
}

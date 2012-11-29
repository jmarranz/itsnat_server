/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.core;

import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Document;
import test.shared.*;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 *
 * @author jmarranz
 */
public class TestHTMLScriptInsertion extends TestScriptInsertion
{
    public TestHTMLScriptInsertion(ItsNatHTMLDocument itsNatDoc)
    {
        super(itsNatDoc);

        addLoadEventListener();
    }

    public void addLoadEventListener()
    {
        Document doc = itsNatDoc.getDocument();
        AbstractView view = ((DocumentView)doc).getDefaultView();
        ((EventTarget)view).addEventListener("load",this,false);
    }

    public Element getScriptParentElement()
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        return doc.getBody();
    }

    public Element createScriptElement()
    {
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        return doc.createElement("script");
    }

}

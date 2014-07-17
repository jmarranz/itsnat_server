/*
 * TestSetTextNode.java
 *
 * Created on 12 de agosto de 2007, 9:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestSetTextNode implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestSetTextNode
     */
    public TestSetTextNode(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        Document doc = itsNatDoc.getDocument();
        Element link = doc.getElementById("setTextNodeTestId");
        ((EventTarget)link).addEventListener("click",this,false);

        Element img2 = doc.getElementById("setTextNodeTestId2");
        Text text = doc.createTextNode("Text Node");
        img2.getParentNode().insertBefore(text,img2);
        text.setData(""); // En fast load mode no hay mutation events en carga al enviarse el markup resultante el browser lo filtrará al estar vacío
    }

    public void handleEvent(Event evt)
    {
        // Testeamos el método ItsNat JavaScript setTextNode

        Document doc = itsNatDoc.getDocument();
        Element img2 = doc.getElementById("setTextNodeTestId2");
        Node text = img2.getPreviousSibling();
        ((Text)text).setData("Text Node");
    }
}

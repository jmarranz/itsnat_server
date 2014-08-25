/*
 * OnClickAddRowListenerExample.java
 *
 * Created on 2 de octubre de 2006, 21:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.clientmut;

import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.events.EventTarget;
import test.web.shared.Shared;
import test.web.shared.TestSerialization;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestClientMutationDocument implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;

    /**
     * Creates a new instance of TestClientMutationDocument
     */
    public TestClientMutationDocument(ItsNatDocument itsNatDoc,ItsNatServletRequest request, ItsNatServletResponse response)
    {
        this.itsNatDoc = itsNatDoc;

        Document doc = itsNatDoc.getDocument();
        itsNatDoc.addMutationEventListener((EventTarget)doc.getDocumentElement(),this,false);

        Shared.setRemoteControlLink(request,response);
        
        new TestSerialization(request);
    }

    public void handleEvent(Event evt)
    {
        // Comprobamos que se ha hecho bien la sincronización
        MutationEvent mutEvent = (MutationEvent)evt;

        String type = mutEvent.getType();
        if (type.equals("DOMNodeInserted"))
        {
            //Element parent = (Element)mutEvent.getRelatedNode();
            Node newNode = (Node)mutEvent.getTarget();

            TestUtil.checkError(newNode != null);
            if (newNode.getNextSibling() != null)
                System.out.println("OK DOMNodeInserted: " + newNode.getNodeName() + " Sibling:" + newNode.getNextSibling().getNodeName());
            else
                System.out.println("OK DOMNodeInserted: " + newNode.getNodeName());
        }
        else if (type.equals("DOMNodeRemoved"))
        {
            Element parent = (Element)mutEvent.getRelatedNode();
            Node removedNode = (Node)mutEvent.getTarget();

            TestUtil.checkError(removedNode.getParentNode() != null); // Todavía NO se ha eliminado

            System.out.println("OK DOMNodeRemoved: " + removedNode.getNodeName() + " Parent:" + parent.getNodeName());
        }
        else if (type.equals("DOMAttrModified"))
        {
            Attr attr = (Attr)mutEvent.getRelatedNode();
            Element elem = (Element)mutEvent.getTarget();
            String attrName = mutEvent.getAttrName();
            TestUtil.checkError(attr.getName().equals(attrName));
            String attrValue = elem.getAttribute(mutEvent.getAttrName());
            String changeName = null;
            int changeType = mutEvent.getAttrChange();
            switch(changeType)
            {
                case MutationEvent.ADDITION:
                    TestUtil.checkError(attrValue.equals(mutEvent.getNewValue()));
                    changeName = "addition";
                    break;
                case MutationEvent.MODIFICATION:
                    TestUtil.checkError(attrValue.equals(mutEvent.getNewValue()));
                    changeName = "modification";
                    break;
                case MutationEvent.REMOVAL:
                    TestUtil.checkError(attrValue.equals(""));
                    changeName = "removal";
                    break;
            }

            System.out.println("OK DOMAttrModified (" + changeName + ") Name:" + mutEvent.getAttrName() + " Value:" + attrValue + " Element:" + elem.getNodeName());
        }
        else if (type.equals("DOMCharacterDataModified"))
        {
            CharacterData charNode = (CharacterData)mutEvent.getTarget();
            TestUtil.checkError(charNode.getData().equals(mutEvent.getNewValue()));

            System.out.println("OK DOMCharacterDataModified \"" + mutEvent.getNewValue() + "\"");
        }
    }

}

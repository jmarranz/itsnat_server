/*
 * OnClickFireEventFromServerTest.java
 *
 * Created on 6 de agosto de 2007, 15:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.core;


import java.io.Serializable;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.EventListenerSerial;
import test.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestDisconnectNode implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;
    protected Element container;
    protected Element disconnectNode3;
    protected DocumentFragment childNodes = null;
    
    /** Creates a new instance of OnClickFireEventFromServerTest */
    public TestDisconnectNode(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testDisconnectNodeId");
        ((EventTarget)link).addEventListener("click",this,false);

        this.container = doc.getElementById("testDisconnectNodeContainerId");
        this.disconnectNode3 = doc.getElementById("disconnectNode_3_Id");
        setupTestDisconnectNode();

        disconnectChildNodesFromClient(false);
        reconnectChildNodesToClient();
        disconnectChildNodesFromClient(true);
    }

    public boolean isLoadFastMode()
    {
        return itsNatDoc.isLoading() && itsNatDoc.getItsNatDocumentTemplate().isFastLoadMode();
    }

    public void handleEvent(Event evt)
    {
        if (childNodes == null)
        {
            disconnectChildNodesFromClient(true);
        }
        else
        {
            reconnectChildNodesToClient();
        }
    }

    public void disconnectChildNodesFromClient(boolean changeClientWithJS)
    {
        TestUtil.checkError(!itsNatDoc.isDisconnectedChildNodesFromClient(container));

        // Aunque eliminamos los nodos en el servidor, deben seguir estando en el cliente
        // pero ahora sin vinculación con el servidor, los event listeners etc se deregistrarán
        // automáticamente pero es conveniente que lo haga el programador manualmente
        this.childNodes = (DocumentFragment)itsNatDoc.disconnectChildNodesFromClient(container);

        setupTestReconnectNode(changeClientWithJS);
    }

    public void reconnectChildNodesToClient()
    {
        TestUtil.checkError(itsNatDoc.isDisconnectedChildNodesFromClient(container));

        // Probamos a ver si se reconecta implícitamente sólo por el hecho de insertar un nodo
        Element elemTemp = itsNatDoc.getDocument().createElement("input");
        container.appendChild(elemTemp);
        TestUtil.checkError(!itsNatDoc.isDisconnectedChildNodesFromClient(container));

        // Desconectamos de nuevo
        Element elemTemp2 = (Element)itsNatDoc.disconnectChildNodesFromClient(container);
        if (elemTemp != elemTemp2) throw new RuntimeException("Test ERROR");

        // Finalmente reconectamos de nuevo ahora explícitamente
        itsNatDoc.reconnectChildNodesToClient(container);
        container.appendChild(childNodes);
        this.childNodes = null;
        setupTestDisconnectNode();
    }

    public void setupTestDisconnectNode()
    {
        ItsNatDOMUtil.setTextContent(link, "Click to Test Disconnect Node");
        ItsNatDOMUtil.setTextContent(disconnectNode3, "Click Me Before Disconnect");

        if (!isLoadFastMode())
        {
            EventListener listener = new EventListenerSerial()
            {
                public void handleEvent(Event evt)
                {
                    ItsNatDOMUtil.setTextContent(disconnectNode3, "OK");
                }
            };
            ((EventTarget)disconnectNode3).addEventListener("click",listener,false);
        }
    }

    public void setupTestReconnectNode(boolean changeClientWithJS)
    {
        ItsNatDOMUtil.setTextContent(link, "Click To Test Reconnect Node");
        if (changeClientWithJS)
        {
            StringBuilder code = new StringBuilder();
            code.append("var elem = document.getElementById('disconnectNode_3_Id');");
            code.append("elem.innerHTML = \"<span style='border: solid blue 1px'><b>Rewritten By JavaScript: OK</b></span>\"; ");
            itsNatDoc.addCodeToSend(code.toString());
        }
    }
}

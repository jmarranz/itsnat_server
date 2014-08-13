/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.droid.core;

import org.itsnat.core.ItsNatDocument;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import test.web.shared.TestUtil;

/**
 *
 * @author jmarranz
 */
public class TestDroidDisconnectNode extends TestDroidBase implements EventListener
{
    protected Element testLauncher;
    protected Element container;
    protected DocumentFragment childNodes = null;
    
    public TestDroidDisconnectNode(ItsNatDocument itsNatDoc)
    {
        super(itsNatDoc);

        this.testLauncher = getDocument().getElementById("testDisconnectNodeId");        
        ((EventTarget)testLauncher).addEventListener("click", this, false);
        
        this.container = getDocument().getElementById("testDisconnectNodeContainerId");        
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
        // pero ahora sin vinculaci�n con el servidor, los event listeners etc se deregistrar�n
        // autom�ticamente pero es conveniente que lo haga el programador manualmente
        this.childNodes = (DocumentFragment)itsNatDoc.disconnectChildNodesFromClient(container);

        StringBuilder code = new StringBuilder();
        code.append("ViewGroup container = itsNatDoc.getPage().findViewByXMLId(\"testDisconnectNodeContainerId\");");
        code.append("container.removeViewAt(0);");        
        code.append("TextView textView = (TextView)container.getChildAt(0); ");        
        code.append("textView.setText(\"Text 2 DISCONNECTED from Server\");");                
        
        itsNatDoc.addCodeToSend(code);
        
        testLauncher.setAttributeNS(ANDROID_NS, "text", "Click to RECONNECT Node");
        
    }    
    
    public void reconnectChildNodesToClient()
    {
        TestUtil.checkError(itsNatDoc.isDisconnectedChildNodesFromClient(container));

        // Probamos a ver si se reconecta impl�citamente s�lo por el hecho de insertar un nodo
        Element elemTemp = itsNatDoc.getDocument().createElement("Button");
        container.appendChild(elemTemp);
        TestUtil.checkError(!itsNatDoc.isDisconnectedChildNodesFromClient(container));

        // Desconectamos de nuevo
        Element elemTemp2 = (Element)itsNatDoc.disconnectChildNodesFromClient(container);
        if (elemTemp != elemTemp2) throw new RuntimeException("Test ERROR");

        // Finalmente reconectamos de nuevo ahora expl�citamente
        itsNatDoc.reconnectChildNodesToClient(container);
        container.appendChild(childNodes);
        this.childNodes = null;
        
        testLauncher.setAttributeNS(ANDROID_NS, "text", "Test Disconnect Node");        
        
    }    
}
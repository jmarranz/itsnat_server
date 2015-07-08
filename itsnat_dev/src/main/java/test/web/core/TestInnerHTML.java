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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 *
 * @author jmarranz
 */
public class TestInnerHTML implements EventListener,Serializable
{
    protected ItsNatDocument itsNatDoc;
    protected Element link;


    /** Creates a new instance of OnClickFireEventFromServerTest */
    public TestInnerHTML(ItsNatDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        load();
    }

    public void load()
    {
        final Document doc = itsNatDoc.getDocument();
        this.link = doc.getElementById("testInnerHTMLId");
        ((EventTarget)link).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        // Este test prueba el innerHTML y el "cacheado" de nodos ya analizados.
        // Evitamos que se use innerHTML a través del uso de un <script>, pues es un nodo problemático
        // que necesita controlarse como se inserta por ello evitamos por sistema para todos los navegadores
        // meterlo en un innerHTML. Al meter en el ejemplo un <script> profundo conseguimos
        // activar el mecanismo de "cacheado" de nodos que ya sabemos que pueden/no pueden insertarse en innerHTML
        // tras un innerHTML fracasado.
        // Al intentar insertar los tres hijos del div padre de todos via innerHTML, detectaremos
        // que el segundo el del <script> no nos deja, por lo que se insertarán los tres via DOM,
        // pero no se estudiarán de nuevo el primero ni el segundo pues ya se sabe de la primera
        // pasada que el primero es válido para innerHTML por lo que el contenido (<b>One rectangle</b>) se hará via innerHTML
        // y que el segundo no vale en ningun caso por el <script> interno, en el tercero habrá que estudiarlo pues
        // en la primera pasada no llegamos a él y se verá que el contenido sí admite innerHTML (<b>Other rectangle</b>) .

        StringBuilder code = new StringBuilder();
        code.append("<div>");
        code.append(" <div style='border:solid 1px red; margin:2px'><b>One rectangle</b></div>");
        code.append(" <div style='border:solid 1px red; padding:2px;'><div style='border:solid 1px red; padding:2px;'><div style='border:solid 1px red'>Three nested rectangles <script></script></div></div></div>");
        code.append(" <div style='border:solid 1px red; margin:2px'><b>Other rectangle</b></div>");     
        code.append("</div>");
        DocumentFragment docFrag = itsNatDoc.toDOM(code.toString());
        link.getParentNode().insertBefore(docFrag, link);
        
        /*
        HTMLDocument doc = (HTMLDocument)itsNatDoc.getDocument();
        Element inputElem = doc.createElement("input");
        inputElem.setAttribute("type","text");      
        doc.getBody().appendChild(inputElem);
        final ItsNatHTMLInputText comp = (ItsNatHTMLInputText)itsNatDoc.getItsNatComponentManager().createItsNatComponent(inputElem);
        comp.setText("Initial Value");
        comp.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e)
            {
                String text = comp.getText();
                System.out.println("insertUpdate " + text);
            }

            public void removeUpdate(DocumentEvent e)
            {
                String text = comp.getText();
                System.out.println("removeUpdate " + text);
            }

            public void changedUpdate(DocumentEvent e)
            {
                String text = comp.getText();
                System.out.println("changedUpdate " + text);
            }              
        });
        * */
    }
    
  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

/**
 *
 * @author jmarranz
 */
public class NewItemError extends ModalDialog implements EventListener
{
    protected ExtJSExampleDocument extJSDoc;
    protected Element modalWinElem;
    protected Element closeIconElem;
    protected Element okElem;

    public NewItemError(ExtJSExampleDocument extJSDoc)
    {
        this.extJSDoc = extJSDoc;

        ItsNatHTMLDocument itsNatDoc = extJSDoc.getItsNatHTMLDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.modalLayer = compMgr.createItsNatModalLayer(null,false,9010,(float)0.2,"black",null);  // opacity is ignored because ExtJS HTML already includes a semitranparent layer
        unexpectedEventDetection();
        
        DocumentFragment frag = extJSDoc.loadDocumentFragment("extjsexample_error_new_item");
        this.modalWinElem = ItsNatTreeWalker.getFirstChildElement(frag);
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element body = doc.getBody();
        body.appendChild(modalWinElem);

        this.closeIconElem = doc.getElementById("ext-gen1006");
        ((EventTarget)closeIconElem).addEventListener("click",this,false);

        this.okElem = doc.getElementById("ext-gen970");
        ((EventTarget)okElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        close();
    }

    public void close()
    {
        ItsNatHTMLDocument itsNatDoc = extJSDoc.getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        ((EventTarget)closeIconElem).removeEventListener("click",this,false);
        ((EventTarget)okElem).removeEventListener("click",this,false);

        Element body = doc.getBody();
        body.removeChild(modalWinElem);

        modalLayer.dispose();
    }
}

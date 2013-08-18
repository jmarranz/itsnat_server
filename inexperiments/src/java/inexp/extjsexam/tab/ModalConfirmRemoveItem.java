
package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatVariableResolver;
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
public class ModalConfirmRemoveItem extends ModalDialog implements EventListener
{
    protected TabContainingTable parent;
    protected Element modalWinElem;
    protected Element closeIconElem;
    protected Element yesElem;
    protected Element noElem;
    protected int index;
    protected String name;

    public ModalConfirmRemoveItem(TabContainingTable parent,int index,String name)
    {
        this.parent = parent;
        this.index = index;
        this.name = name;

        ExtJSExampleDocument extJSDoc = parent.getExtJSExampleDocument();
        ItsNatHTMLDocument itsNatDoc = extJSDoc.getItsNatHTMLDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.modalLayer = compMgr.createItsNatModalLayer(null,false,9000,(float)0.2,"black",null);  // opacity is ignored because ExtJS HTML already includes a semitranparent layer
        unexpectedEventDetection();

        DocumentFragment frag = extJSDoc.loadDocumentFragment("extjsexample_confirm_remove_item");
        this.modalWinElem = ItsNatTreeWalker.getFirstChildElement(frag);
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element body = doc.getBody();
        body.appendChild(modalWinElem);

        this.closeIconElem = doc.getElementById("ext-gen1006");
        ((EventTarget)closeIconElem).addEventListener("click",this,false);

        this.yesElem = doc.getElementById("ext-gen978");
        ((EventTarget)yesElem).addEventListener("click",this,false);

        this.noElem = doc.getElementById("ext-gen986");
        ((EventTarget)noElem).addEventListener("click",this,false);

        Element msgElem = doc.getElementById("ext-gen1015");
        ItsNatVariableResolver resolver = itsNatDoc.createItsNatVariableResolver();
        resolver.setLocalVariable("name",name);
        resolver.resolve(msgElem);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == closeIconElem)
            close();
        else if (currTarget == yesElem)
            removeItem();
        else if (currTarget == noElem)
            close();
    }

    public void close()
    {
        ItsNatHTMLDocument itsNatDoc = parent.getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        ((EventTarget)closeIconElem).removeEventListener("click",this,false);
        ((EventTarget)yesElem).removeEventListener("click",this,false);
        ((EventTarget)noElem).removeEventListener("click",this,false);

        Element body = doc.getBody();
        body.removeChild(modalWinElem);

        modalLayer.dispose();
    }

    public void removeItem()
    {
        parent.removeItem(index);
        close();
    }
}

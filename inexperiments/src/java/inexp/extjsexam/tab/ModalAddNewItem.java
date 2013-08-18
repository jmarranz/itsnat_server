package inexp.extjsexam.tab;

import inexp.extjsexam.ExtJSExampleDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
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
public class ModalAddNewItem extends ModalDialog implements EventListener
{
    protected TabContainingTable parent;
    protected Element modalWinElem;
    protected Element closeIconElem;
    protected ItsNatHTMLInputText nameComp;
    protected ItsNatHTMLInputText descComp;
    protected Element addNewItemElem;
    protected Element cancelNewItemElem;

    public ModalAddNewItem(TabContainingTable parent)
    {
        this.parent = parent;

        ExtJSExampleDocument extJSDoc = parent.getExtJSExampleDocument();
        ItsNatHTMLDocument itsNatDoc = extJSDoc.getItsNatHTMLDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.modalLayer = compMgr.createItsNatModalLayer(null,false,9000,(float)0.2,"black",null); // opacity is ignored because ExtJS HTML already includes a semitranparent layer
        unexpectedEventDetection();

        DocumentFragment frag = extJSDoc.loadDocumentFragment("extjsexample_add_new_item");
        this.modalWinElem = ItsNatTreeWalker.getFirstChildElement(frag);
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        Element body = doc.getBody();
        body.appendChild(modalWinElem);

        this.closeIconElem = doc.getElementById("ext-gen934");
        ((EventTarget)closeIconElem).addEventListener("click",this,false);

        this.nameComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("newItemNameId");
        this.descComp = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("newItemDescId");

        this.addNewItemElem = doc.getElementById("ext-gen891");
        ((EventTarget)addNewItemElem).addEventListener("click",this,false);

        this.cancelNewItemElem = doc.getElementById("ext-gen899");
        ((EventTarget)cancelNewItemElem).addEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == closeIconElem)
            close();
        else if (currTarget == addNewItemElem)
            addNewItem();
        else if (currTarget == cancelNewItemElem)
            close();
    }

    public void close()
    {
        ItsNatHTMLDocument itsNatDoc = parent.getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        ((EventTarget)closeIconElem).removeEventListener("click",this,false);

        nameComp.dispose();
        descComp.dispose();

        ((EventTarget)addNewItemElem).removeEventListener("click",this,false);
        ((EventTarget)cancelNewItemElem).removeEventListener("click",this,false);

        Element body = doc.getBody();
        body.removeChild(modalWinElem);

        modalLayer.dispose();
    }

    public void addNewItem()
    {
        String name = nameComp.getText();
        String desc = descComp.getText();
        if (name.equals(""))
        {
           new NewItemError(parent.getExtJSExampleDocument());
           return;
        }
        parent.addNewItem(name,desc);
        close();
    }
}

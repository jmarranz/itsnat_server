package org.itsnat.spitut;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateOverviewShowPopup extends SPITutState implements EventListener
{
    protected SPITutStateOverview parent;
    protected Element container;
    protected ItsNatModalLayer layer;

    public SPITutStateOverviewShowPopup(SPITutStateOverview parent)
    {
        super(parent.getSPITutMainDocument());
        this.parent = parent;

        SPITutMainDocument spiTutDoc = parent.getSPITutMainDocument();
        ItsNatHTMLDocument itsNatDoc = parent.getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.layer = compMgr.createItsNatModalLayer(null,false,1,0.5f,"black",null);
        HTMLBodyElement body = (HTMLBodyElement)doc.getBody();

        DocumentFragment frag = spiTutDoc.loadDocumentFragment("overview.popup");
        this.container = ItsNatTreeWalker.getFirstChildElement(frag);
        body.appendChild(container);

        ((EventTarget)container).addEventListener("click", this, false);

        itsNatDoc.addCodeToSend("try{ window.scroll(0,-1000); }catch(ex){}");
        // try/catch is used to prevent some mobile browser does not support it
    }

    @Override
    public String getStateTitle()
    {
        return "Overview Popup";
    }

    @Override
    public String getStateName()
    {
        return "overview.showpopup";
    }
    
    public void handleEvent(Event evt)
    {
        dispose();
    }

    public void dispose()
    {
        ((EventTarget)container).removeEventListener("click",this, false);
        container.getParentNode().removeChild(container);
        layer.dispose();

        parent.onDisposeOverviewPopup();
    }

}

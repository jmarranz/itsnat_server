
package org.itsnat.spitut;

import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateDetail extends SPITutState implements EventListener
{
    protected Element detailMoreElem;
    protected boolean inserted = false;
    
    public SPITutStateDetail(SPITutMainDocument spiTutDoc,String stateSecondaryName)
    {
        super(spiTutDoc);
        
        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        if ("moreDetail".equals(stateSecondaryName))
        {
            
        }
    }

    @Override
    public String getStateTitle()
    {
        return "Detail";
    }

    @Override
    public String getStateName()
    {
        return "detail";
    }

    public void handleEvent(Event evt)
    {
        if (detailMoreElem == null)
        {
            DocumentFragment frag = spiTutDoc.loadDocumentFragment("detail.more");
            this.detailMoreElem = ItsNatTreeWalker.getFirstChildElement(frag);
        }

        if (!inserted)
        {
            Element contentParentElem = spiTutDoc.getContentParentElement();
            contentParentElem.appendChild(detailMoreElem);
            detailMoreLink.setTextContent("Hide");
            this.inserted = true;
        }
        else
        {
            detailMoreElem.getParentNode().removeChild(detailMoreElem);            
            detailMoreLink.setTextContent("More Detail");
            this.inserted = false;
        }
    }
}

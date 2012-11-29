
package org.itsnat.spitut;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateOverview extends SPITutState implements EventListener
{
    protected Element showPopupElem;
    protected SPITutStateOverviewShowPopup popup;

    public SPITutStateOverview(SPITutMainDocument spiTutDoc,boolean showPopup)
    {
        super(spiTutDoc);

        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        this.showPopupElem = doc.getElementById("showPopupId");
        ((EventTarget)showPopupElem).addEventListener("click",this,false);

        if (showPopup) showOverviewPopup();
    }

    public void dispose()
    {
        if (popup != null) popup.dispose();
        ((EventTarget)showPopupElem).removeEventListener("click",this,false);
    }

    public void handleEvent(Event evt)
    {
        showOverviewPopup();
    }

    public void showOverviewPopup()
    {
        ((EventTarget)showPopupElem).removeEventListener("click",this,false); // Avoids two consecutive clicks
        this.popup = new SPITutStateOverviewShowPopup(this);
    }

    public void onDisposeOverviewPopup()
    {
        this.popup = null;
        ((EventTarget)showPopupElem).addEventListener("click",this,false); // Restores
        spiTutDoc.registerState(this);
    }
    
    @Override
    public String getStateTitle()
    {
        return "Overview";
    }

    @Override
    public String getStateName()
    {
        return "overview";
    }
}

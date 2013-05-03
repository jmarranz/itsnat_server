
package org.itsnat.spitut;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTitleElement;

public class SPITutMainDocument implements EventListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected String title;
    protected HTMLTitleElement titleElem;
    protected Map<String,Element> menuElemMap = new HashMap<String,Element>();
    protected Element currentMenuItemElem;
    protected Element contentParentElem;
    protected SPITutState currentState;
    protected Element googleAnalyticsElem;
    protected String googleAnalyticsIFrameURL;

    public SPITutMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response)
    {
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        this.titleElem = (HTMLTitleElement)doc.getElementById("titleId");
        this.title = titleElem.getText(); // Initial value

        menuElemMap.put("overview",doc.getElementById("menuOpOverviewId"));
        menuElemMap.put("detail",doc.getElementById("menuOpDetailId"));
        // More menu options here...

        itsNatDoc.addUserEventListener(null,"setState", this);

        this.contentParentElem = doc.getElementById("contentParentId");
        this.googleAnalyticsElem = doc.getElementById("googleAnalyticsId");
        this.googleAnalyticsIFrameURL = googleAnalyticsElem.getAttribute("src");  // Initial value

        HttpServletRequest servReq = request.getHttpServletRequest();
        String stateName = servReq.getParameter("_escaped_fragment_"); // Google bot, has priority, its value is based on the hash fragment
        if (stateName != null)
        {
            if (stateName.startsWith("st=")) // st means "state"
                stateName = stateName.substring("st=".length(), stateName.length());
            else // Wrong format
                stateName = "overview";
        }
        else
        {
            stateName = servReq.getParameter("st");
            if (stateName == null)
                stateName = "overview";
        }

        changeState(stateName);
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public void setStateTitle(String stateTitle)
    {
        String pageTitle = stateTitle + " - " + title;
        if (itsNatDoc.isLoading())
            titleElem.setText(pageTitle);
        else
            itsNatDoc.addCodeToSend("document.title = \"" + pageTitle + "\";\n");
    }

    public Element getContentParentElement()
    {
        return contentParentElem;
    }

    public ItsNatDocFragmentTemplate getFragmentTemplate(String name)
    {
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        return servlet.getItsNatDocFragmentTemplate(name);
    }

    public DocumentFragment loadDocumentFragment(String name)
    {
        ItsNatDocFragmentTemplate template = getFragmentTemplate(name);
        if (template == null) return null;
        return template.loadDocumentFragment(itsNatDoc);
    }

    public String getFragmentName(String stateName)
    {
        String fragmentName = stateName;
        int pos = stateName.indexOf('.');
        if (pos != -1) fragmentName = stateName.substring(0, pos); // Case "overview.showpopup"
        return fragmentName;
    }

    public void changeState(String stateName)
    {
        String fragmentName = getFragmentName(stateName);

        ItsNatDocFragmentTemplate template = getFragmentTemplate(fragmentName);
        if (template == null)
        {
            changeState("not_found");
            return;
        }

        // Cleaning previous state:
        if (currentState != null)
        {
            currentState.dispose();
            this.currentState = null;
        }

        ItsNatDOMUtil.removeAllChildren(contentParentElem);

        // Setting new state:
        changeActiveMenu(stateName);

        DocumentFragment frag = template.loadDocumentFragment(itsNatDoc);
        contentParentElem.appendChild(frag);

        if (stateName.equals("overview")||stateName.equals("overview.showpopup"))
        {
            boolean popup = stateName.equals("overview.showpopup");
            this.currentState = new SPITutStateOverview(this,popup);
        }
        else if (stateName.equals("detail"))
            this.currentState = new SPITutStateDetail(this);

        itsNatDoc.addCodeToSend("try{ window.scroll(0,-5000); }catch(ex){}");
        // try/catch is used to avoid exceptions when some (mobile) browser does not support window.scroll()
    }

    public void registerState(SPITutState state)
    {
        setStateTitle(state.getStateTitle());
        String stateName = state.getStateName();
        itsNatDoc.addCodeToSend("spiSite.setURLReference(\"" + stateName + "\");");
        googleAnalyticsElem.setAttribute("src",googleAnalyticsIFrameURL + stateName);
    }

    public void handleEvent(Event evt)
    {
        if (evt instanceof ItsNatUserEvent)
        {
            ItsNatUserEvent itsNatEvt = (ItsNatUserEvent)evt;
            String name = (String)itsNatEvt.getExtraParam("name");
            changeState(name);
        }
    }

    public void changeActiveMenu(String stateName)
    {
        String mainMenuItem;
        int pos = stateName.indexOf('.');
        if (pos != -1) mainMenuItem = stateName.substring(0, pos); // Case "overview.showpopup"
        else mainMenuItem = stateName;

        if (currentMenuItemElem != null)
            currentMenuItemElem.removeAttribute("class");

        this.currentMenuItemElem = menuElemMap.get(mainMenuItem);

        if (currentMenuItemElem != null)
            currentMenuItemElem.setAttribute("class","menuOpSelected");
    }
}

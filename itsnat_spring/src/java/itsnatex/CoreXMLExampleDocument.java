
package itsnatex;

import itsnatspring.ItsNatDocumentInitialize;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public class CoreXMLExampleDocument implements ItsNatDocumentInitialize
{
    public CoreXMLExampleDocument()
    {
    }

    @Override
    public void load(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element discsElem = doc.getDocumentElement();

        Element cdListElem = ItsNatTreeWalker.getLastChildElement(discsElem);

        ItsNatServlet servlet = request.getItsNatServlet();
        ItsNatDocFragmentTemplate fragTemplate = servlet.getItsNatDocFragmentTemplate("manual.core.xmlFragExample");
        DocumentFragment docFrag = fragTemplate.loadDocumentFragment(itsNatDoc);
        discsElem.insertBefore(docFrag,cdListElem); // docFrag is empty now

        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList discList = factory.createElementList(cdListElem,true);

        addCD("Help","The Beatles",new String[] {"A Hard Day's Night","Let It Be"},discList);
        addCD("Making Movies","Dire Straits",new String[] {"Tunnel Of Love","Romeo & Juliet"},discList);
    }

    public void addCD(String title,String artist,String[] songs,ElementList discList)
    {
        Element cdElem = discList.addElement();
        Element titleElem = ItsNatTreeWalker.getFirstChildElement(cdElem);
        ItsNatDOMUtil.setTextContent(titleElem,title);
        Element artistElem = ItsNatTreeWalker.getNextSiblingElement(titleElem);
        ItsNatDOMUtil.setTextContent(artistElem,artist);
        Element songsElem = ItsNatTreeWalker.getNextSiblingElement(artistElem);

        ItsNatDocument itsNatDoc = discList.getItsNatDocument();
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList songList = factory.createElementList(songsElem,true);
        for(int i = 0; i < songs.length; i++)
            songList.addElement(songs[i]);
    }
}

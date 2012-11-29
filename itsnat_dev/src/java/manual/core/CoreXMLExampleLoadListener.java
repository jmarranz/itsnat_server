/*
 * CoreXMLExampleLoadListener.java
 *
 * Created on 4 de octubre de 2007, 13:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.domutil.ElementList;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CoreXMLExampleLoadListener implements ItsNatServletRequestListener
{
    public CoreXMLExampleLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request,
 				ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        Element discsElem = doc.getDocumentElement();

        Element cdListElem = ItsNatTreeWalker.getLastChildElement(discsElem);
        ElementGroupManager factory = itsNatDoc.getElementGroupManager();
        ElementList discList = factory.createElementList(cdListElem,true);

        addCD("Help","The Beatles",
		new String[] {"A Hard Day's Night","Let It Be"},discList);
        addCD("Making Movies","Dire Straits",
		new String[] {"Tunnel Of Love","Romeo & Juliet"},discList);
    }

    public void addCD(String title,String artist,String[] songs,
			ElementList discList)
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

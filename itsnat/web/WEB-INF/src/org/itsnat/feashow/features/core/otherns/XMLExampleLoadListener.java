/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */
package org.itsnat.feashow.features.core.otherns;

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

public class XMLExampleLoadListener implements ItsNatServletRequestListener
{
    public XMLExampleLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();

        Element cdListElem = ItsNatTreeWalker.getLastChildElement(doc.getDocumentElement());
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
        {
            songList.addElement(songs[i]);
        }
    }

}

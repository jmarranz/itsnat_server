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

package org.itsnat.feashow.features.comp.xmlcomp;


import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLAndCompLoadListener implements ItsNatServletRequestListener
{
    public XMLAndCompLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();

        Element titleElem = ItsNatTreeWalker.getFirstChildElement(doc.getDocumentElement());
        Element cdListElem = ItsNatTreeWalker.getNextSiblingElement(titleElem);

        ItsNatFreeListMultSel comp = (ItsNatFreeListMultSel)compMgr.createItsNatComponent(cdListElem,"freeListMultSel",null);
        comp.setItsNatListCellRenderer(CDListRenderer.SINGLETON);
        DefaultListModel model = (DefaultListModel)comp.getListModel();

        List cdList = new ArrayList();

        CompactDisc cd;

        cd = new CompactDisc("Help","The Beatles");
        cd.addSong(new Song("A Hard Day's Night"));
        cd.addSong(new Song("Let It Be"));
        cdList.add(cd);

        cd = new CompactDisc("Making Movies","Dire Straits");
        cd.addSong(new Song("Tunnel Of Love"));
        cd.addSong(new Song("Romeo & Juliet"));
        cdList.add(cd);

        for(int i = 0; i < cdList.size(); i++)
            model.addElement(cdList.get(i));
    }

}

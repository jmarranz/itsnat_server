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

import javax.swing.DefaultListModel;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;

public class CDListRenderer implements ItsNatListCellRenderer
{
    public final static CDListRenderer SINGLETON = new CDListRenderer();

    public CDListRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        CompactDisc cd = (CompactDisc)value;

        Element titleElem = ItsNatTreeWalker.getFirstChildElement(cellElem);
        ItsNatDOMUtil.setTextContent(titleElem,cd.getTitle());
        Element artistElem = ItsNatTreeWalker.getNextSiblingElement(titleElem);
        ItsNatDOMUtil.setTextContent(artistElem,cd.getArtist());
        Element songsElem = ItsNatTreeWalker.getNextSiblingElement(artistElem);

        ItsNatComponentManager compMgr = list.getItsNatComponentManager();
        ItsNatFreeListMultSel comp = (ItsNatFreeListMultSel)compMgr.createItsNatComponent(songsElem,"freeListMultSel",null);
        comp.setItsNatListCellRenderer(SongListRenderer.SINGLETON);
        DefaultListModel model = (DefaultListModel)comp.getListModel();
        for(int i = 0; i < cd.getSongCount(); i++)
            model.addElement(cd.getSong(i));
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}

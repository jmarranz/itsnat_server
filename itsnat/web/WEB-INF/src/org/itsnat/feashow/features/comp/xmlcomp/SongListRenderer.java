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


import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.w3c.dom.Element;

public class SongListRenderer implements ItsNatListCellRenderer
{
    public final static SongListRenderer SINGLETON = new SongListRenderer();

    public SongListRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        Song song = (Song)value;
        ItsNatDOMUtil.setTextContent(cellElem,song.getName());
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}

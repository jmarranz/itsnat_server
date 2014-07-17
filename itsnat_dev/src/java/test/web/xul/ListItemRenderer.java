/*
 * RectRenderer.java
 *
 * Created on 18 de abril de 2007, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.xul;

import java.io.Serializable;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ListItemRenderer implements ItsNatListCellRenderer,Serializable
{

    /**
     * Creates a new instance of ListItemRenderer
     */
    public ListItemRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        cellElem.setAttribute("label",value.toString());
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}

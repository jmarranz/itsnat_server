/*
 * RectRenderer.java
 *
 * Created on 18 de abril de 2007, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.svg;

import java.io.Serializable;
import javax.swing.DefaultListModel;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class RectRenderer implements ItsNatListCellRenderer,Serializable
{

    /**
     * Creates a new instance of RectRenderer
     */
    public RectRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        DefaultListModel model = (DefaultListModel)list.getListModel();
        int x = ((Rect)model.getElementAt(index)).getX();

        Element circleElem = list.getItsNatListUI().getElementAt(index);
        circleElem.setAttribute("x",Integer.toString(x));
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}

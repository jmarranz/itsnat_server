/*
 * CircleRenderer.java
 *
 * Created on 18 de abril de 2007, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.web.svgxhtml;

import javax.swing.DefaultListModel;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class CircleRenderer implements ItsNatListCellRenderer
{

    /** Creates a new instance of CircleRenderer */
    public CircleRenderer()
    {
    }

    public void renderListCell(ItsNatList list, int index, Object value, boolean isSelected, boolean cellHasFocus, Element cellElem,boolean isNew)
    {
        DefaultListModel model = (DefaultListModel)list.getListModel();
        int cx = ((Circle)model.getElementAt(index)).getCx();

        Element circleElem = list.getItsNatListUI().getElementAt(index);
        circleElem.setAttribute("cx",Integer.toString(cx));
    }

    public void unrenderListCell(ItsNatList list,int index,Element cellContentElem)
    {
    }
}

/*
 * CityListCustomStructure.java
 *
 * Created on 22 de junio de 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package manual.comp;

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

/**
 *
 * @author jmarranz
 */
public class CityListCustomStructure implements ItsNatListStructure
{
    public CityListCustomStructure()
    {
    }

    public Element getContentElement(ItsNatList list, int index, Element parentElem)
    {
        HTMLTableRowElement rowElem = (HTMLTableRowElement)parentElem;
        HTMLTableCellElement firstCellElem = (HTMLTableCellElement)ItsNatTreeWalker.getFirstChildElement(rowElem);
        HTMLTableCellElement secondCellElem = (HTMLTableCellElement)ItsNatTreeWalker.getNextSiblingElement(firstCellElem);
        return secondCellElem;
    }
}

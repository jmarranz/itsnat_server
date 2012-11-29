/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.iepocket;

import java.io.Serializable;
import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTableCellElement;

/**
 *
 * @author jmarranz
 */
public class IEPocketListStructure implements ItsNatListStructure,Serializable
{
    protected ItsNatListStructure delegate;

    public IEPocketListStructure(ItsNatListStructure delegate)
    {
        this.delegate = delegate;
    }

    public Element getContentElement(ItsNatList list, int index, Element itemElem)
    {
        HTMLTableCellElement cell = (HTMLTableCellElement)delegate.getContentElement(list, index, itemElem);
        return (HTMLAnchorElement)cell.getFirstChild();
    }

}

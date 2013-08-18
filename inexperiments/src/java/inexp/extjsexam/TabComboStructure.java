package inexp.extjsexam;

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class TabComboStructure implements ItsNatListStructure
{
    public static final ItsNatListStructure SINGLETON = new TabComboStructure();

    public Element getContentElement(ItsNatList list, int index, Element cellElem)
    {
        Element firstA = ItsNatTreeWalker.getFirstChildElement(cellElem);
        Element secondA = ItsNatTreeWalker.getNextSiblingElement(firstA);
        return ItsNatTreeWalker.getFirstDeepestChildElement(secondA);        
    }
}

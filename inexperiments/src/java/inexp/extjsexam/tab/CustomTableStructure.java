package inexp.extjsexam.tab;

import org.itsnat.comp.table.ItsNatTable;
import org.itsnat.comp.table.ItsNatTableHeader;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class CustomTableStructure implements ItsNatTableStructure
{
    public static final CustomTableStructure SINGLETON = new CustomTableStructure();

    public Element getHeadElement(ItsNatTable tableComp, Element tableElem)
    {
        return null; // No header
    }

    public Element getBodyElement(ItsNatTable tableComp, Element tableElem)
    {
        return tableElem;
    }

    public Element getRowContentElement(ItsNatTable tableComp, int row, Element rowElem)
    {
        // div/table/tbody/tr
        Element table = ItsNatTreeWalker.getFirstChildElement(rowElem);
        Element tbody = ItsNatTreeWalker.getFirstChildElement(table);
        Element tr = ItsNatTreeWalker.getFirstChildElement(tbody);
        return tr;
    }

    public Element getCellContentElement(ItsNatTable tableComp, int row, int col, Element cellElem)
    {
        // td/div
        return ItsNatTreeWalker.getFirstChildElement(cellElem);
    }

    public Element getHeaderColumnContentElement(ItsNatTableHeader tableHead, int column, Element itemElem)
    {
        // Never called.
        return null;
    }

}

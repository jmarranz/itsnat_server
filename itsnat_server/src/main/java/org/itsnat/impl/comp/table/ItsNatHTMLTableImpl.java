/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
*/

package org.itsnat.impl.comp.table;

import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatHTMLTableHeader;
import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.itsnat.comp.table.ItsNatHTMLTableUI;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTableImpl extends ItsNatTableImpl implements ItsNatHTMLTable
{
    /** Creates a new instance of ItsNatHTMLTableImpl */
    public ItsNatHTMLTableImpl(HTMLTableElement element,ItsNatTableStructure structure,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,structure,artifacts,componentMgr);

        init();
    }

    public HTMLTableElement getHTMLTableElement()
    {
        return (HTMLTableElement)node;
    }

    public ItsNatHTMLTableUI getItsNatHTMLTableUI()
    {
        return (ItsNatHTMLTableUI)compUI;
    }

    public ItsNatHTMLTableHeader getItsNatHTMLTableHeader()
    {
        return (ItsNatHTMLTableHeader)header;
    }

    public ItsNatHTMLTableUI createDefaultItsNatHTMLTableUI()
    {
        return new ItsNatHTMLTableUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatHTMLTableUI();
    }

    public ItsNatTableHeaderImpl createItsNatTableHeader(Element headerElem)
    {
        return new ItsNatHTMLTableHeaderImpl(this,(HTMLTableSectionElement)headerElem);
    }

    public Node createDefaultNode()
    {
        Document doc = getItsNatDocument().getDocument();
        HTMLTableElement table = (HTMLTableElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"table");
        HTMLTableSectionElement tbody = (HTMLTableSectionElement)doc.createElementNS(NamespaceUtil.XHTML_NAMESPACE,"tbody");
        table.appendChild(tbody);
        return table;
    }

    public ItsNatStfulWebDocComponentManagerImpl getItsNatStfulWebDocComponentManager()
    {
        return (ItsNatStfulWebDocComponentManagerImpl)componentMgr;
    }

    public ItsNatHTMLComponentManager getItsNatHTMLComponentManager()
    {
        return getItsNatStfulWebDocComponentManager();
    }

    public ItsNatHTMLElementComponentUI getItsNatHTMLElementComponentUI()
    {
        return (ItsNatHTMLElementComponentUI)compUI;
    }

    public HTMLElement getHTMLElement()
    {
        return (HTMLElement)node;
    }
}

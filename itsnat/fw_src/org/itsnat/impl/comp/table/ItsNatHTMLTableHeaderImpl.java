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

import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.table.ItsNatHTMLTableHeader;
import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.itsnat.comp.table.ItsNatHTMLTableHeaderUI;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.comp.mgr.web.ItsNatStfulWebDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableRowElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLTableHeaderImpl extends ItsNatTableHeaderImpl implements ItsNatHTMLTableHeader
{
    /** Creates a new instance of ItsNatHTMLTableHeaderImpl */
    public ItsNatHTMLTableHeaderImpl(ItsNatHTMLTableImpl tableComp,HTMLTableSectionElement headerElem)
    {
        super(tableComp,headerElem);
    }

    public HTMLTableSectionElement getHTMLTableSectionElement()
    {
        return (HTMLTableSectionElement)node;
    }

    public HTMLTableRowElement getHTMLTableRowElement()
    {
        HTMLTableSectionElement tHeadElement = getHTMLTableSectionElement();
        return (HTMLTableRowElement)ItsNatTreeWalker.getFirstChildElementWithTagNameNS(tHeadElement,NamespaceUtil.XHTML_NAMESPACE,"tr");
    }

    public ItsNatHTMLTable getItsNatHTMLTable()
    {
        return (ItsNatHTMLTable)tableComp;
    }

    public ItsNatHTMLTableHeaderUI getItsNatHTMLTableHeaderUI()
    {
        return (ItsNatHTMLTableHeaderUI)compUI;
    }

    public ItsNatHTMLTableHeaderUI createDefaultItsNatHTMLTableHeaderUI()
    {
        return new ItsNatHTMLTableHeaderUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatHTMLTableHeaderUI();
    }

    public Node createDefaultNode()
    {
        // Este componente es esclavo del <table> y si el <table> no tiene
        // <thead> no lo imponemos aquí.
        return null;
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

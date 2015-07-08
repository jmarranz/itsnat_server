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

package org.itsnat.impl.comp.inc;

import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.NameValue;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocDefaultImpl;
import org.itsnat.impl.comp.listener.ItsNatCompNormalEventListenersByDocImpl;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatFreeIncludeImpl extends ItsNatFreeElementComponentImpl implements ItsNatFreeInclude
{
    protected boolean enabled = true;
    protected boolean included;
    protected boolean buildComp;
    protected String fragmentName;

    /**
     * Creates a new instance of ItsNatFreeIncludeImpl
     */
    public ItsNatFreeIncludeImpl(Element element,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        init();
    }

    public ItsNatCompNormalEventListenersByDocImpl createItsNatCompNormalEventListenersByDoc()
    {
        return new ItsNatCompNormalEventListenersByDocDefaultImpl(this);
    }

    public ItsNatCompNormalEventListenersByClientImpl createItsNatCompNormalEventListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompNormalEventListenersByClientDefaultImpl(this,clientDoc);
    }

    public Object createDefaultStructure()
    {
        return null;
    }

    public boolean isIncluded()
    {
        return included;
    }

    public void bindDataModel()
    {
        // No hay modelo
    }

    public void unbindDataModel()
    {
        // No hay modelo
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return new ItsNatFreeElementComponentUIImpl(this);
    }

    public void initialSyncUIWithDataModel()
    {
    }

    public Object createDefaultModelInternal()
    {
        return null; // No hay modelo
    }

    public String getIncludedFragmentName()
    {
        return fragmentName;
    }

    public void includeFragment(String name,boolean buildComp)
    {
        removeFragment();

        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplateImpl().getItsNatServlet();
        ItsNatDocFragmentTemplate docFragDesc = servlet.getItsNatDocFragmentTemplate(name);
        if (docFragDesc == null) throw new ItsNatException("Document fragment not found: " + name,this);
        DocumentFragment docFrag = docFragDesc.loadDocumentFragment(itsNatDoc);

        Element parent = getElement();
        DOMUtilInternal.removeAllChildren(parent); // Por si acaso no está vacío
        parent.appendChild(docFrag); // El DocumentFragment queda vacío (creo)

        if (buildComp)
        {
            ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();
            Node child = parent.getFirstChild();
            while(child != null)
            {
                compMgr.buildItsNatComponents(child);
                child = child.getNextSibling();
            }
        }

        this.fragmentName = name;
        this.included = true;
        this.buildComp = buildComp;
    }

    public void includeFragment(String name)
    {
        includeFragment(name,false);
    }

    public void removeFragment()
    {
        if (!included) return;

        Element parent = getElement();

        if (buildComp)
        {
            ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();

            Node child = parent.getFirstChild();
            while(child != null)
            {
                compMgr.removeItsNatComponents(child,true,null);
                child = child.getNextSibling();
            }
        }

        DOMUtilInternal.removeAllChildren(parent);

        this.included = false;
        this.fragmentName = null;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }
}

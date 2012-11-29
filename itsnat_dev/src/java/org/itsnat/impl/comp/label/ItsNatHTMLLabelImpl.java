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

package org.itsnat.impl.comp.label;

import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.comp.ItsNatHTMLElementComponentUI;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLabelElement;

/**
 * Nota por si se aplican futuros usos: El accesskey se accede via ALT-letra
 * en MSIE y Firefox <2, en el FireFox >= 2 ha cambiado a SHIFT-ALT-letra
 * para que no interfiera con la funcionalidad normal de los menues del navegador.
 * Cuando se usa un accesskey FireFox (v2 al menos) envía un evento click al label.
 *
 * http://juicystudio.com/article/numeric-accesskeys-fixed-firefox.php
 *
 * @author jmarranz
 */
public class ItsNatHTMLLabelImpl extends ItsNatLabelImpl implements ItsNatHTMLLabel
{
    /**
     * Creates a new instance of ItsNatHTMLLabelImpl
     */
    public ItsNatHTMLLabelImpl(HTMLLabelElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        setItsNatLabelEditor(null);  // No hace falta, pero para que quede claro que no son editables por defecto porque suelen estar asociados a controles de formulario que son los que se "editan"

        init();
    }

    public HTMLElement getHTMLElement()
    {
        return (HTMLElement)node;
    }

    public HTMLLabelElement getHTMLLabelElement()
    {
        return (HTMLLabelElement)node;
    }

    public ItsNatHTMLFormComponent getForComponent()
    {
        HTMLLabelElement element = getHTMLLabelElement();
        String compId = element.getHtmlFor();

        ItsNatStfulDocComponentManagerImpl compMgr = getItsNatStfulDocComponentManager();
        return (ItsNatHTMLFormComponent)compMgr.findItsNatComponentById(compId);
    }

    public ItsNatHTMLElementComponentUI getItsNatHTMLElementComponentUI()
    {
        return (ItsNatHTMLElementComponentUI)compUI;
    }

    public ItsNatHTMLComponentManager getItsNatHTMLComponentManager()
    {
        return getItsNatStfulDocComponentManager();
    }

    public ItsNatStfulDocComponentManagerImpl getItsNatStfulDocComponentManager()
    {
        return (ItsNatStfulDocComponentManagerImpl)componentMgr;
    }

    public Node createDefaultNode()
    {
        return (HTMLLabelElement)getItsNatDocument().getDocument().createElementNS(NamespaceUtil.XHTML_NAMESPACE,"label");
    }
}

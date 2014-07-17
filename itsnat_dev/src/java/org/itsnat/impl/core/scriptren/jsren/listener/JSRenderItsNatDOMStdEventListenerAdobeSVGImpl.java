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

package org.itsnat.impl.core.scriptren.jsren.listener;

import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerAdobeSVGImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerAdobeSVGImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerAdobeSVGImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerAdobeSVGImpl()
    {
    }

    public boolean needsAddListenerReturnElement()
    {
        return false;
    }

    public boolean needsRemoveListenerReturnElement()
    {
        return false;
    }

    @Override    
    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        // Adobe SVG Viewer no soporta add/removeEventListener en window
        // (normalmente usado para los eventos "load" y "unload" en SVG en otros navegadores)
        EventTarget nodeTarget = itsNatListener.getCurrentTarget();
        if (!isEventTargetValid(nodeTarget))
            return "";
        else
            return super.addItsNatDOMStdEventListenerCode(itsNatListener, clientDoc);
    }

    @Override    
    protected String removeItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        EventTarget nodeTarget = itsNatListener.getCurrentTarget();
        if (!isEventTargetValid(nodeTarget))
            return "";
        else
            return super.removeItsNatDOMStdEventListenerCode(itsNatListener, clientDoc);
    }

    protected boolean isEventTargetValid(EventTarget nodeTarget)
    {
        // Estudiamos si el target tiene métodos add/removeEventListener
        // pues de otra manera da error
        if (nodeTarget instanceof AbstractView)
        {
            // Adobe SVG Viewer no soporta add/removeEventListener en window
            // (normalmente usado para los eventos "load" y "unload" en SVG en otros navegadores)
            return false;
        }
        else if (nodeTarget instanceof Node) // Por si acaso aunque yo creo que no hace falta
        {
            // Sólo los elementos SVG tienen add/removeEventListener, los elementos
            // bajo <foreignObject> y por tanto con namespace XHTML son "extraños"
            // para ASV
            if (!NamespaceUtil.isSVGNode((Node)nodeTarget))
                return false;
        }
        return true;
    }
}

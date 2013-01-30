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

package org.itsnat.impl.core.jsren.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatDOMStdEventListenerOperaMiniImpl extends JSRenderItsNatDOMStdEventListenerImpl
{
    public static final JSRenderItsNatDOMStdEventListenerOperaMiniImpl SINGLETON = new JSRenderItsNatDOMStdEventListenerOperaMiniImpl();

    /** Creates a new instance of JSRenderItsNatDOMStdEventListenerImpl */
    public JSRenderItsNatDOMStdEventListenerOperaMiniImpl()
    {
    }

    protected boolean needsAddListenerReturnElement()
    {
        return true;
    }

    protected boolean needsRemoveListenerReturnElement()
    {
        return false;
    }

    protected String addItsNatDOMStdEventListenerCode(ItsNatDOMStdEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        // Es necesaria la presencia de un handler inline onXXX para
        // que funcione el addEventListener en ciertos casos, de otra manera es ignorado.
        // Se ha detectado al menos en el evento "change" en <select>
        // y alguno más en los <input> (no me acuerdo)
        // pero lo hacemos por sistema por si acaso, el impacto es mínimo.
        if (itsNatListener.getCurrentTarget() instanceof Element)
        {
            String attrName = "\"on" + itsNatListener.getType() + "\"";
            StringBuffer code = new StringBuffer();
            code.append( super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc) );
            code.append( "if (!elem.hasAttribute(" + attrName + ")) elem.setAttribute(" + attrName + ",\"\");\n" );
            return code.toString();
        }
        else return super.addItsNatDOMStdEventListenerCode(itsNatListener,clientDoc);
    }
}

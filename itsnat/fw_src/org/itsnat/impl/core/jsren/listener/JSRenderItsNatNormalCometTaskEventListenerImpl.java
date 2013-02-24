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
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatNormalCometEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatNormalCometTaskEventListenerImpl extends JSRenderItsNatGenericTaskEventListenerImpl
{
    public static final JSRenderItsNatNormalCometTaskEventListenerImpl SINGLETON = new JSRenderItsNatNormalCometTaskEventListenerImpl();

    /**
     * Creates a new instance of JSRenderItsNatNormalCometTaskEventListenerImpl
     */
    private JSRenderItsNatNormalCometTaskEventListenerImpl()
    {
    }

    private String addNormalCometTaskEventListenerCode(ItsNatNormalCometEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        String listenerId = itsNatListener.getId();
        int sync = itsNatListener.getCommModeDeclared();
        long eventTimeout = getEventTimeout(itsNatListener,clientDoc);

        StringBuilder code = new StringBuilder();

        // A día de hoy no hay código del usuario en los eventos comet, podría añadirse fácilmente

        code.append( "\n" );
        code.append( "itsNatDoc.sendCometTaskEvent(\"" + listenerId + "\"," + sync + "," + eventTimeout + ");\n" );

        return code.toString();
    }

    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return addNormalCometTaskEventListenerCode((ItsNatNormalCometEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}

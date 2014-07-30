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
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatAttachedClientTimerEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public class JSRenderItsNatAttachedClientTimerEventListenerImpl extends JSRenderItsNatAttachedClientEventListenerImpl
{
    private static final JSRenderItsNatAttachedClientTimerEventListenerImpl SINGLETON = new JSRenderItsNatAttachedClientTimerEventListenerImpl();

    /**
     * Creates a new instance of JSRenderItsNatAttachedClientTimerEventListenerImpl
     */
    public JSRenderItsNatAttachedClientTimerEventListenerImpl()
    {
    }

    public static JSRenderItsNatAttachedClientTimerEventListenerImpl getJSRenderItsNatAttachedClientTimerEventListener()
    {
        return SINGLETON;
    }
    
    private static String addItsNatAttachedClientTimerEventListenerCode(ItsNatAttachedClientTimerEventListenerWrapperImpl listener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();

        int commMode = listener.getCommModeDeclared(); // Llama a ClientDocumentAttachedClientTimerImpl.getCommMode()
        int interval = listener.getRefreshInterval(); // Llama a ClientDocumentAttachedClientTimerImpl.getRefreshInterval()

        code.append( "\n" );
        code.append( "var listener = function ()\n" );
        code.append( "{\n" );
        code.append( "  var itsNatDoc = arguments.callee.itsNatDoc;\n" );
        code.append( "  itsNatDoc.sendAttachTimerRefresh(arguments.callee," + interval + "," + commMode + ");\n" );
        code.append( "};\n" );
        code.append( "listener.itsNatDoc = itsNatDoc;\n" );
        code.append( "itsNatDoc.attachTimerHandle = itsNatDoc.setTimeout(listener," + interval + ");\n" );

        return code.toString();
    }

    @Override    
    protected String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return addItsNatAttachedClientTimerEventListenerCode((ItsNatAttachedClientTimerEventListenerWrapperImpl)itsNatListener,clientDoc);
    }

    @Override    
    protected String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return null; // Nada que hacer
    }
}

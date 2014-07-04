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
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderItsNatDOMEventListenerImpl extends JSRenderItsNatNormalEventListenerImpl
{
    /** Creates a new instance of JSRenderItsNatDOMEventListenerImpl */
    public JSRenderItsNatDOMEventListenerImpl()
    {
    }

    public static JSRenderItsNatDOMEventListenerImpl getJSRenderItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatDOMStdEventListenerWrapperImpl)
            return JSRenderItsNatDOMStdEventListenerImpl.getJSRenderItsNatDOMStdEventListener((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatDOMExtEventListenerWrapperImpl)
            return JSRenderItsNatDOMExtEventListenerImpl.getJSRenderItsNatDOMExtEventListener((ItsNatDOMExtEventListenerWrapperImpl)itsNatListener);
        return null;
    }

    public String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatDOMEventListenerWrapperImpl normalListener = (ItsNatDOMEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatDOMEventListenerWrapperImpl normalListener = (ItsNatDOMEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    protected static String getUserCode(ItsNatDOMEventListenerWrapperImpl itsNatListener)
    {
        StringBuilder code = new StringBuilder();

        String extraParams = itsNatListener.getCodeToSendParamTransports();
        String preSendCode = itsNatListener.getPreSendCode();

        if (extraParams != null)
            code.append( extraParams );
        if (preSendCode != null)
            code.append( preSendCode );

        return code.toString();
    }

    public static String addCustomFunctionCode(ItsNatDOMEventListenerWrapperImpl itsNatListener,StringBuilder code)
    {
        String userCode = getUserCode(itsNatListener);
        if ((userCode != null) && !userCode.equals(""))
        {
            code.append( "\n" );
            code.append( "var func = function(event)\n" );
            code.append( "{\n" );
            code.append(    userCode );
            code.append( "};\n" );

            String bindToCustomFunc = itsNatListener.getBindToCustomFunc();
            if ((bindToCustomFunc != null) && !bindToCustomFunc.equals(""))
                code.append( "func." + bindToCustomFunc + ";\n" );

            return "func";
        }
        else
        {
            return "null";
        }
    }
}

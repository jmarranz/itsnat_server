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
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatNormalEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatNormalEventListener;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderItsNatNormalEventListenerImpl extends JSRenderItsNatEventListenerImpl implements RenderItsNatNormalEventListener
{

    /**
     * Creates a new instance of JSRenderItsNatNormalEventListenerImpl
     */
    public JSRenderItsNatNormalEventListenerImpl()
    {
    }

    public static JSRenderItsNatNormalEventListenerImpl getJSRenderItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatDOMStdEventListenerWrapperImpl)
            return JSRenderItsNatDOMStdEventListenerImpl.getJSRenderItsNatDOMStdEventListener((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatDOMExtEventListenerWrapperImpl)
            return JSRenderItsNatDOMExtEventListenerImpl.getJSRenderItsNatDOMExtEventListener((ItsNatDOMExtEventListenerWrapperImpl)itsNatListener);
        return null;
    }

    @Override    
    public String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatNormalEventListenerWrapperImpl normalListener = (ItsNatNormalEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }
    
    @Override
    public String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        ItsNatNormalEventListenerWrapperImpl normalListener = (ItsNatNormalEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public String addCustomFunctionCode(ItsNatNormalEventListenerWrapperImpl itsNatListener,StringBuilder code)
    {
        String userCode = JSAndBSRenderItsNatNormalEventListenerImpl.getUserCodeInsideCustomFunc(itsNatListener);
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
    
    protected abstract String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);    
    protected abstract String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateWebImpl clientDoc);    
    
}

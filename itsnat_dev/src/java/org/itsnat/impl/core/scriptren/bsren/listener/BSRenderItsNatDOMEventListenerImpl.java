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

package org.itsnat.impl.core.scriptren.bsren.listener;

import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.ItsNatDOMEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domstd.ItsNatDOMStdEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatDOMEventListenerImpl;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatDOMEventListenerImpl extends BSRenderItsNatNormalEventListenerImpl
{
    /** Creates a new instance of BSRenderItsNatDOMEventListenerImpl */
    public BSRenderItsNatDOMEventListenerImpl()
    {
    }

    public static BSRenderItsNatDOMEventListenerImpl getBSRenderItsNatDOMEventListener(ItsNatDOMEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatDOMStdEventListenerWrapperImpl)
            return BSRenderItsNatDOMStdEventListenerImpl.getBSRenderItsNatDOMStdEventListener((ItsNatDOMStdEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatDOMExtEventListenerWrapperImpl)
            return BSRenderItsNatDOMExtEventListenerImpl.getBSRenderItsNatDOMExtEventListener((ItsNatDOMExtEventListenerWrapperImpl)itsNatListener);
        return null;
    }

    public String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatDOMEventListenerWrapperImpl normalListener = (ItsNatDOMEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatDOMEventListenerWrapperImpl normalListener = (ItsNatDOMEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public static String addCustomFunctionCode(ItsNatDOMEventListenerWrapperImpl itsNatListener,StringBuilder code)
    {
        String userCode = JSAndBSRenderItsNatDOMEventListenerImpl.getUserCodeInsideCustomFunc(itsNatListener);
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
    
    protected abstract String addItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc);    
    protected abstract String removeItsNatEventListenerCodeInherit(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc);    
}

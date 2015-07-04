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

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.listener.ItsNatEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.dom.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.droid.ItsNatDroidEventListenerWrapperImpl;
import org.itsnat.impl.core.scriptren.shared.listener.JSAndBSRenderItsNatNormalEventListenerImpl;
import org.itsnat.impl.core.scriptren.shared.listener.RenderItsNatNormalEventListener;

/**
 *
 * @author jmarranz
 */
public abstract class BSRenderItsNatNormalEventListenerImpl extends BSRenderItsNatEventListenerImpl implements RenderItsNatNormalEventListener
{

    /**
     * Creates a new instance of BSRenderItsNatNormalEventListenerImpl
     */
    public BSRenderItsNatNormalEventListenerImpl()
    {
    }

    public static BSRenderItsNatNormalEventListenerImpl getBSRenderItsNatNormalEventListener(ItsNatNormalEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (itsNatListener instanceof ItsNatDroidEventListenerWrapperImpl)
            return BSRenderItsNatDroidEventListenerImpl.getBSRenderItsNatDroidEventListener((ItsNatDroidEventListenerWrapperImpl)itsNatListener,clientDoc);
        else if (itsNatListener instanceof ItsNatDOMExtEventListenerWrapperImpl)
            return BSRenderItsNatDOMExtEventListenerImpl.getBSRenderItsNatDOMExtEventListener((ItsNatDOMExtEventListenerWrapperImpl)itsNatListener);
        return null;
    }

    public String addItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatNormalEventListenerWrapperImpl normalListener = (ItsNatNormalEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return addItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public String removeItsNatEventListenerCodeClient(ItsNatEventListenerWrapperImpl itsNatListener,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        ItsNatNormalEventListenerWrapperImpl normalListener = (ItsNatNormalEventListenerWrapperImpl)itsNatListener;
        if (!clientDoc.getClientDocumentStful().canReceiveNormalEvents(normalListener))
            return null; // Si es un visor remoto sólo lectura lo ignoramos

        return removeItsNatEventListenerCodeInherit(itsNatListener,clientDoc);
    }

    public String addCustomFunctionCode(ItsNatNormalEventListenerWrapperImpl itsNatListener,StringBuilder code,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String userCode = JSAndBSRenderItsNatNormalEventListenerImpl.getUserCodeInsideCustomFunc(itsNatListener,clientDoc);
        if ((userCode != null) && !userCode.equals(""))
        {
            code.append( "\n" );
            code.append( "var func = new CustomFunction()\n" ); // El package está importado previamente por parte del cliente Android
            code.append( "{\n" );
            code.append( "  public void exec(NormalEvent event)\n" );            
            code.append( "  {\n" );            
            code.append(    userCode );
            code.append( "  }\n" );            
            code.append( "};\n" );

            String bindToCustomFunc = itsNatListener.getBindToCustomFunc();
            if ((bindToCustomFunc != null) && !bindToCustomFunc.equals(""))
                code.append( "func." + bindToCustomFunc + ";\n" );  // La verdad es que en Droid no se si tiene alguna utilidad o sentido

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

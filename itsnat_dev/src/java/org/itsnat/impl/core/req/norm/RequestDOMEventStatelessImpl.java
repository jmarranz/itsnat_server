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

package org.itsnat.impl.core.req.norm;

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.listener.domext.ItsNatDOMExtEventListenerWrapperImpl;
import org.itsnat.impl.core.listener.domext.ItsNatEventStatelessListenerWrapperImpl;
import org.itsnat.impl.core.resp.norm.ResponseDOMEventImpl;
import org.itsnat.impl.core.resp.norm.ResponseNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class RequestDOMEventStatelessImpl extends RequestDOMExtEventImpl
{
    public RequestDOMEventStatelessImpl(int evtType,ItsNatServletRequestImpl itsNatRequest)
    {
        super(evtType,itsNatRequest);
    }
    
    @Override
    public String getEventListenerId()
    {
        return null; // No disponemos de este id y no es necesario
    }    
    
    public ResponseNormalEventImpl createResponseNormalEvent(String listenerId,ClientDocumentStfulImpl clientDoc)
    {
        // Nos interesa procesar la request con los listeners globales definidos en documento, en clientDoc etc, pero para ello necesitamos un listener falso (no definido por el usuario)
        // sino no se procesan estos listener globales pues el comportamiento normal es ignorar las "requests perdidas" desde el punto de vista de los listeners
        int commMode = Integer.parseInt(getItsNatServletRequest().getAttrOrParamExist("itsnat_commMode"));
        long eventTimeout = Integer.parseInt(getItsNatServletRequest().getAttrOrParamExist("itsnat_eventTimeout"));  
        ItsNatDOMExtEventListenerWrapperImpl listener = new ItsNatEventStatelessListenerWrapperImpl(clientDoc.getItsNatStfulDocument(),clientDoc,commMode,eventTimeout); 

        // listener puede ser null pero puede haber código pendiente a enviar
        return new ResponseDOMEventImpl(this,listener);
    }
}

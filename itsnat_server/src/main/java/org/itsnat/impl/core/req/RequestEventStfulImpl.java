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

package org.itsnat.impl.core.req;

import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.req.norm.RequestNormalEventImpl;
import org.itsnat.impl.core.req.attachcli.RequestAttachedClientEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestEventStfulImpl extends RequestAlreadyLoadedDocImpl
{
    public static final int EVENT_TYPE_DOMSTD = 1;
    public static final int EVENT_TYPE_TIMER = 2;
    public static final int EVENT_TYPE_ASYNC_RET = 3;
    public static final int EVENT_TYPE_COMET_RET = 4;
    public static final int EVENT_TYPE_CONTINUE = 5;
    public static final int EVENT_TYPE_USER = 6;
    public static final int EVENT_TYPE_ATTACH_TIMER = 7;
    public static final int EVENT_TYPE_ATTACH_COMET = 8;
    public static final int EVENT_TYPE_ATTACH_NOT_REFRESH = 9;
    public static final int EVENT_TYPE_STATELESS = 10;
    public static final int EVENT_TYPE_DROID = 11;
    
    public RequestEventStfulImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public static RequestEventStfulImpl createRequestEventStful(ItsNatServletRequestImpl itsNatRequest)
    {
        String eventTypeStr = itsNatRequest.getAttrOrParamExist("itsnat_eventType");
        int evtType = getEventTypeCode(eventTypeStr);
        switch(evtType)
        {
            case EVENT_TYPE_DOMSTD:
            case EVENT_TYPE_DROID:
            case EVENT_TYPE_TIMER:
            case EVENT_TYPE_CONTINUE:
            case EVENT_TYPE_USER:
            case EVENT_TYPE_ASYNC_RET:
            case EVENT_TYPE_COMET_RET:
            case EVENT_TYPE_STATELESS:                
                return RequestNormalEventImpl.createRequestNormalEvent(evtType,itsNatRequest);

            case EVENT_TYPE_ATTACH_TIMER:
            case EVENT_TYPE_ATTACH_COMET:
            case EVENT_TYPE_ATTACH_NOT_REFRESH:
                return RequestAttachedClientEventImpl.createRequestAttachedClientEvent(evtType,itsNatRequest);

            default:
                throw new ItsNatException("Malformed URL/request, bad itsnat_eventType: " + evtType);
        }
    }

    public static int getEventTypeCode(String value)
    {
        if ("domstd".equals(value))
            return EVENT_TYPE_DOMSTD;
        else if ("droid".equals(value))
            return EVENT_TYPE_DROID;        
        else if ("timer".equals(value))
            return EVENT_TYPE_TIMER;
        else if ("asyncret".equals(value))
            return EVENT_TYPE_ASYNC_RET;
        else if ("cometret".equals(value))
            return EVENT_TYPE_COMET_RET;
        else if ("continue".equals(value))
            return EVENT_TYPE_CONTINUE;
        else if ("user".equals(value))
            return EVENT_TYPE_USER;
        else if ("attach_timer".equals(value))
            return EVENT_TYPE_ATTACH_TIMER;
        else if ("attach_comet".equals(value))
            return EVENT_TYPE_ATTACH_COMET;
        else if ("attach_none".equals(value))
            return EVENT_TYPE_ATTACH_NOT_REFRESH;
        else if ("stateless".equals(value))
            return EVENT_TYPE_STATELESS;        
        else
            throw new ItsNatException("Unknown itsnat_eventType value:\"" + value + "\"");
    }


    public abstract boolean isUnloadEvent();
    public abstract void processLostSessionError(String sessionId,String sessionToken);
    public abstract void processClientDocumentNotFoundError(String clientId);
    public abstract void processLostSessionOrClientUnloading();

    public void processClientDocumentNotFound(String clientId)
    {
        if (isUnloadEvent())
        {
            // Evitamos enviar a toda costa una excepción cuando *se está cerrando una ventana*
            // pues MSIE no cierra la conexión de dicho requestParent "fallido" y total
            // no nos vamos a enterar del error pues la ventana se cierra y ni siquiera
            // se procesa el XMLHttpRequest "fallido".
            processLostSessionOrClientUnloading();
        }
        else processClientDocumentNotFoundError(clientId);
    }

    public void processLostSession(String sessionId,String sessionToken)
    {
        // En el caso de control remoto puede darse el caso de un timer con un gran lapso
        // y mientras tanto se ha reiniciado el servidor o ha caducado la sesión (este último
        // caso será raro en mi opinión).

        if (isUnloadEvent())
        {
            // No damos error, el usuario ha cerrado la página que sabe que está "perdida".
            processLostSessionOrClientUnloading();
        }
        else
        {
            processLostSessionError(sessionId,sessionToken);
        }
    }
}

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

import org.itsnat.impl.core.servlet.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.servlet.ItsNatSessionImpl;

/**
 *
 * @author jmarranz
 */
public abstract class RequestAlreadyLoadedDocImpl extends RequestImpl implements RequestStfulDocument
{
    public RequestAlreadyLoadedDocImpl(ItsNatServletRequestImpl itsNatRequest)
    {
        super(itsNatRequest);
    }

    public abstract void processClientDocument(ClientDocumentStfulImpl clientDoc);
    public abstract void processClientDocumentNotFound(String clientId);
    public abstract void processLostSession(String sessionId,String sessionToken);
    public abstract ClientDocumentStfulImpl getClientDocumentStfulById(String id);

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)getItsNatDocument();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocumentReferrer()
    {
        // El referrer sólo tiene sentido en tiempo de carga de una página para que tenga acceso a la anterior
        return null;
    }

    protected boolean isMustNotifyEndOfRequestToSession()
    {
        return true;
    }

    @Override
    public void processRequest(ClientDocumentStfulImpl clientDocStateless)
    {
        if (!checkValidClientSession())
            return;

        ClientDocumentStfulImpl clientDoc;
        if (clientDocStateless != null)
        {
            clientDoc = clientDocStateless;
        }
        else
        {
            // Buscamos el cliente en la sesión, sólo puede estar ahí registrado
            // se evita toda la posibilidad de intentar
            // acertar con el id del observador.

            String clientId = itsNatRequest.getAttrOrParamExist("itsnat_client_id");

            clientDoc = getClientDocumentStfulById(clientId);
            
            if (clientDoc == null) // Cliente no encontrado
            {
                processClientDocumentNotFound(clientId);
                return;
            }
        }
        
        processClientDocument(clientDoc);
    }

    public String getRequestedSessionId()
    {
        ItsNatServletRequestImpl itsNatReq = getItsNatServletRequest();
        return itsNatReq.getAttrOrParamExist("itsnat_session_id");
    }

    public String getRequestedSessionToken()
    {
        ItsNatServletRequestImpl itsNatReq = getItsNatServletRequest();
        return itsNatReq.getAttrOrParamExist("itsnat_session_token");
    }

    private boolean checkValidClientSession()
    {
        String sessionId = getRequestedSessionId();
        String sessionToken = getRequestedSessionToken();
        if (!isValidClientSession(sessionId,sessionToken))
        {
            processLostSession(sessionId,sessionToken);
            return false; // No válida
        }

        return true; // Válida
    }

    public boolean isStateless()    
    {
        return false; // Se redefine en evento stateless
    }    
    
    public boolean isValidClientSession(String sessionId,String sessionToken)
    {
        if (isStateless())
        {
            // En stateless debemos soportar que la sesión haya caducado o el servidor haya sido reiniciado, para eso es stateless        
            return true;
        }
        
        // Esta es la única razón de enviar el id de la sesión al cliente
        // y usar un token (número aleatorio), para detectar que la sesión ha caducado
        // por tanto para evitar el error de buscar un cliente
        // que perteneció a otra sesión y que no está en la nueva sesión en el servidor
        // (creada a propósito de la requestParent).
        // NO SON PARA EVITAR un problema de seguridad pues NI EL TOKEN NI EL ID
        // ENVIADOS SON USADOS PARA OBTENER LA SESIÓN, pues la sesión es
        // obtenida a través de la cookie del contenedor de servlets,
        // Ambos valores nos sirven PARA SABER QUE HA PASADO, SON INFORMATIVOS,
        // para dar "un buen error" si es necesario.
        // El token nos sirve para evitar el "caso casual" de que el número
        // de sesión del cliente coincida con la sesión recién creada por la requestParent
        // por ejemplo en el caso de recarga de la aplicación (reinicio de números Id),
        // pues en una aplicación dada el servidor asegura la unicidad de los sessionId y que no son rehusados
        // pero no en diferentes ciclos de vida, antes de introducir token dábamos un
        // error del tipo "cliente con id no existe" o bien si había otra página abierta
        // en la nueva sesión con el mismo número de cliente, daría error en algún momento
        // al no corresponderse el estado DOM del servidor con el del cliente, el problema
        // de seguridad no es diferente a modificar el estado de la página cliente de una página legalmente obtenida,
        // es lo bueno que tiene la programación "centrica en el servidor". Ahora con el token
        // sabemos claramente qué la sesión que creó la página expiró.
        // Incluso en el caso de falsear "y acertar" el token (extremadamente difícil) y la sesión (caso mucho más fácil)
        // con los de la nueva sesión, no hay problema de seguridad pues la nueva sesión no tiene
        // documentos asociados, dará error al buscar el ClientDocument owner (el que no existe en la nueva sesión).
        // Por eso no nos preocupamos en comprobar si el número generado ya fue
        // generado antes por casualidad (la probabilidad es pequeñíiiiisima).
        // De cualquier forma el token *nos aporta más seguridad* aunque ese no fuera el objetivo inicial.

        ItsNatServletRequestImpl itsNatReq = getItsNatServletRequest();
        if (!itsNatReq.isValidClientStandardSessionId())
            return false;

        ItsNatSessionImpl itsNatSession = itsNatReq.getItsNatSessionImpl();
        return (itsNatSession.getId().equals(sessionId) && itsNatSession.getToken().equals(sessionToken));
    }
}

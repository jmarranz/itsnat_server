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

package org.itsnat.impl.core.clientdoc;

import org.itsnat.impl.core.servlet.ItsNatSessionImpl;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.event.ItsNatAttachedClientEvent;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.itsnat.impl.core.listener.WaitForEventListenerImpl;
import org.w3c.dom.events.EventListener;

/**
 *
 * @author jmarranz
 */
public abstract class ClientDocumentAttachedClientImpl extends ClientDocumentStfulImpl
{
    protected boolean readOnly;
    protected int commMode;
    protected long eventTimeout;
    protected long waitDocTimeout;
    protected int phase = ItsNatAttachedClientEvent.REQUEST;
    protected boolean accepted = false; // Inicialmente NO se acepta (hay que aceptar explícitamente).

    /** Creates a new instance of ClientDocumentAttachedClientImpl */
    public ClientDocumentAttachedClientImpl(boolean readOnly,int commMode,long eventTimeout,long waitDocTimeout,Browser browser,ItsNatSessionImpl itsNatSession,ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc,browser,itsNatSession);

        this.readOnly = readOnly;
        this.commMode = commMode;
        this.eventTimeout = eventTimeout;
        this.waitDocTimeout = waitDocTimeout;
    }

    public void registerInSession()
    {
        getItsNatSessionImpl().registerClientDocumentAttachedClient(this);
    }

    public boolean isScriptingEnabled()
    {
        return true; // De otra manera no podríamos hacer control remoto
    }

    public boolean isEventsEnabled()
    {
        return true; // De otra manera no podríamos hacer control remoto
    }

    public boolean canReceiveALLNormalEvents()
    {
        return !isReadOnly();
    }

    public boolean canReceiveSOMENormalEvents()
    {
        if (canReceiveALLNormalEvents()) return true;

        // Aunque sea read only los eventos para WaitForEventListenerImpl
        // pueden ser recibidos.
        // A día de hoy sólo son necesarios con SVGWeb
        return SVGWebInfoImpl.isSVGWebEnabled(this);
    }

    public boolean canReceiveNormalEvents(EventListener listener)
    {
        if (canReceiveALLNormalEvents()) return true; // Como puede recibir todos los eventos el listener del parámetro está incluido sea cual sea

        // Es read only
        // A lo mejor puede recibir eventos via el listener WaitForEventListenerImpl
        // relacionado con SVGWeb
        if (!SVGWebInfoImpl.isSVGWebEnabled(this)) return false; // SVGWebInfo desactivado

        // Aunque sea read only hacemos una excepción con los WaitForEventListenerImpl
        // porque vienen a ser eventos "de servicio"
        // A día de hoy sólo son necesarios con SVGWeb
        return (listener instanceof WaitForEventListenerImpl);
    }

    public boolean isReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    public int getCommModeDeclared()
    {
        return commMode;
    }

    @Override
    public long getEventTimeout()
    {
        return eventTimeout;
    }

    public long getWaitDocTimeout()
    {
        return waitDocTimeout; 
    }

    public abstract void startAttachedClient();

    public int getPhase()
    {
        return phase;
    }

    public void setPhase(int phase)
    {
        // El UNLOAD prevalece sobre los demás estados pues este estado es que libera el ClientDocument
        // El UNLOAD puede haberse definido o bien porque el usuario ha cerrado la página observadora
        // o bien porque no ha sido aceptado un evento.
        // Evitamos así también "volver atrás" en el caso de REFRESH.
        if ((this.phase == ItsNatAttachedClientEvent.UNLOAD) &&
            (this.phase != phase))
                throw new ItsNatException("INTERNARL ERROR"); // return;

        this.phase = phase;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setAccepted(boolean accepted)
    {
        if (!accepted)
        {
            setInvalid(); // Envía el JavaScript de parar el timer por ejemplo
        }

        this.accepted = accepted;
    }

    public void attachedClientEventReceived()
    {
        this.lastEventTime = System.currentTimeMillis();
    }

    protected void setInvalidInternal()
    {
        super.setInvalidInternal();

//        ItsNatDocumentImpl itsNatDoc = getItsNatDocumentImpl();
//        if (itsNatDoc.isInvalid()) // El setInvalid del cliente ha podido ser llamado por el documento que está siendo invalidado
//            setPhase(ItsNatAttachedClientEvent.OBSERVED_INVALID); // Recuerda que en caso de UNLOAD este estado prevalece

        // NO DESREGISTRAMOS, pues el setInvalidInternal ha podido ser llamado
        // por un ClientDocumentOwner destruyéndose, el cliente observador
        // viene dado por la existencia de la página en el navegador del usuario
        // y conviene que se entere al procesar el siguiente evento y terminar de forma
        // elegante p.ej. avisando con un alert.

        //ItsNatSessionImpl itsNatSession = getItsNatSessionOwner();
        //itsNatSession.unregisterClientDocumentAttachedClient(this);
    }

    public void invalidateAndUnregister()
    {
        // El motivo de este método es asegurar que el método unregisterClientDocumentAttachedClient
        // se llame en la sesión correcta pues los ClientDocumentAttachedClientImpl
        // están registrados en su sesión pero también en el documento al que están asociados
        // que puede pertenecer a otra sesión
        getItsNatSessionImpl().unregisterClientDocumentAttachedClient(this);
    }

    public abstract String getRefreshMethod();


}

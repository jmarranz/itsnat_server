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

package org.itsnat.impl.core.comet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.CometNotifier;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class CometNotifierImpl extends ItsNatUserDataImpl implements CometNotifier
{
    protected boolean started = true;
    protected transient Object monitor = new Object();
    protected boolean pendingNotification = false;
    protected long expirationDelay = 1 * 60 * 60 * 1000; // Una hora, para que no esté indefinidamente parado el hilo
    protected ClientDocumentStfulImpl clientDoc;

    /**
     * Creates a new instance of CometNotifierImpl
     */
    public CometNotifierImpl(boolean userDataSync,ClientDocumentStfulImpl clientDoc)
    {
        super(userDataSync);
        this.clientDoc = clientDoc;
    }

    protected void finalize()
    {
        stop(); // Para asegurarnos que el hilo termine, como started es false el ItsNatDocument no se usa (no hay problema de estado "incorrecto" del documento)
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.monitor = new Object(); // Object no es serialiable

        in.defaultReadObject();
    }

    public ClientDocument getClientDocument()
    {
        return clientDoc;
    }

    public abstract void addCometTask();

    public Object getMonitor()
    {
        return monitor;
    }

    public void notifyClient()
    {
        // if (!started) throw new ItsNatException("Notifier was disposed");

        if (isStopped()) return; // Si ya está parado no hace nada, antes de llamar a notifyClient debería preguntarse si está parado

        wakeup();  // Despertar al hilo en espera para que termine y el hilo del evento pendiente de él despierte y haga su trabajo
    }

    public void stop()
    {
        if (isStopped()) return; // Ya parado

        stopInternal();
    }

    public void stopInternal()
    {
        this.started = false;
        wakeup(); // Despertar el hilo en espera para que se entere que ha de terminar
        this.pendingNotification = false;
    }

    public boolean isStopped()
    {
        return !started;
    }

    public void wakeup()
    {
        synchronized(monitor)
        {
            this.pendingNotification = true;
            monitor.notifyAll();
        }
    }

    public boolean hasPendingNotification()
    {
        return pendingNotification;
    }

    public void setPendingNotification(boolean pendingNotification)
    {
        this.pendingNotification = pendingNotification;
    }

    public ClientDocumentStfulImpl getClientDocumentStful()
    {
        return clientDoc;
    }

    public ItsNatDocument getItsNatDocument()
    {
        return getItsNatStfulDocument();
    }

    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return clientDoc.getItsNatStfulDocument();
    }

    public long getExpirationDelay()
    {
        return expirationDelay;
    }

    public void setExpirationDelay(long expirationDelay)
    {
        this.expirationDelay = expirationDelay;
    }

}

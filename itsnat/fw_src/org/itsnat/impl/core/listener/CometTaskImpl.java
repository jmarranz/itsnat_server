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

package org.itsnat.impl.core.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.itsnat.impl.core.comet.CometNotifierImpl;

/**
 *
 * @author jmarranz
 */
public class CometTaskImpl extends GenericTaskImpl
{
    protected CometNotifierImpl notifier;
    protected transient Object monitor; // apunta al mismo objeto que el CometNotifierImpl

    /**
     * Creates a new instance of CometTaskImpl
     */
    public CometTaskImpl(CometNotifierImpl notifier)
    {
        this.notifier = notifier;
        this.monitor = notifier.getMonitor();
    }

    public void dispose()
    {
        // Este método DEBE llamarse con ItsNatDocument sincronizado
        // pues addCometTask realiza un registro en el documento o cliente.
        this.monitor = null;
        if (!notifier.isStopped())
        {
            notifier.setPendingNotification(false); // Este CometTaskImpl va a ser procesado por lo que se enviará el código JavaScript pendiente al cliente

            notifier.addCometTask(); // Se añade una nueva
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        this.monitor = notifier.getMonitor(); // No se si esto funcionará
    }

    public boolean isDisposed()
    {
        return monitor == null;
    }

    public CometNotifierImpl getCometNotifier()
    {
        return notifier;
    }

    public long getEventTimeout()
    {
        return notifier.getEventTimeout();
    }

    public boolean mustWait()
    {
        return  !isDisposed() &&
                !notifier.isStopped() &&
                !notifier.hasPendingNotification();
    }

    public void waitToFinish()
    {
        // Este método es llamado SIN sincronizar el documento.
        // Para que espere tranquilito (parado) dando oportunidad a otros hilos
        Object monitor = this.monitor;
        if (monitor == null) return; // disposed

        try
        {
            long before = System.currentTimeMillis();
            long delay = notifier.getExpirationDelay();
            synchronized(monitor)
            {
                if (mustWait())
                    monitor.wait(delay);
            }
            long after = System.currentTimeMillis();
            if (after - before > delay) // Se ha despertado no porque le hayan notificado sino porque ha terminado el tiempo de espera
                notifier.stop(); // Terminamos el CometNotifier porque parece que el agente notificador ha muerto, así evitamos un proceso Comet indefinido
        }
        catch(InterruptedException ex) { }

        this.monitor = null; // No equivale exactamente a llamar a dispose() pero así marcamos que no debe esperar más
    }
}

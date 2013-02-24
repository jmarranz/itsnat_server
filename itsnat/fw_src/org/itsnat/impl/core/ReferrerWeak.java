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

package org.itsnat.impl.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ReferrerWeak extends Referrer
{
    protected transient WeakReference<ItsNatStfulDocumentImpl> referrer;

    /** Creates a new instance of ReferrerWeak */
    public ReferrerWeak()
    {
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        ItsNatStfulDocumentImpl itsNatDoc = null;
        if (referrer != null)
            itsNatDoc = referrer.get();

        out.writeObject(itsNatDoc);

        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        ItsNatStfulDocumentImpl itsNatDoc = (ItsNatStfulDocumentImpl)in.readObject();
        if (itsNatDoc != null)
            this.referrer = new WeakReference<ItsNatStfulDocumentImpl>(itsNatDoc);

        in.defaultReadObject();
    }

    public synchronized ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        if (referrer == null) return null;
        return referrer.get();
    }

    public synchronized void pushItsNatStfulDocument(ItsNatStfulDocumentImpl itsNatDoc)
    {
        /* Nota: se sabe que cuando se pulsa un link o se envía el formulario
         * que substituiría al documento actual, la nueva página
         * se accede sin que se ejecute el evento unload de la lanzadora.
         * Por tanto se ha de usar onbeforeunload para hacer el push para
         * que se ejecute antes de la carga de la nueva página.
         * No hay problema con la WeakReference pues durante la carga
         * de la nueva página, el documento referrer todavía está vivo
         * con referencias normales pues todavía no se ha ejecutado el unload.
         * La WeakReference es por si el link o formulario no lleva a una página
         * ItsNat que recoja el referrer y por si el unload fallara por alguna razón.
         * Sólo funciona el referrer cuando la página se destruye, es decir
         * los links y forms usando target (ej. target="_blank") no tienen referrer (null)
         * En esos casos con window.opener tienen acceso al cliente y con el idObj del documento
         * tienen acceso al padre en el servidor.
         * En Internet Explorer sin embargo se ejecuta el unload antes
         * de acceder a la nueva página cuando no se usa un link o form
         * como método de transición (caso de navegación directa con un URL
           en el navegador o pulsando reload), por tanto el referrer se pierde en estos
         * casos, esto no ocurre en FireFox. El comportamiento del Explorer
         * ciertamente está más de acorde con el concepto de "referrer".
         **/

        this.referrer = new WeakReference<ItsNatStfulDocumentImpl>(itsNatDoc);
    }

    public synchronized ItsNatStfulDocumentImpl popItsNatStfulDocument()
    {
        if (referrer == null) return null;
        ItsNatStfulDocumentImpl itsNatDoc = referrer.get();
        this.referrer = null;
        return itsNatDoc;
    }

    public void cleanItsNatStfulDocument()
    {
        popItsNatStfulDocument();
    }
}

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

package org.itsnat.impl.core.doc;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDocSynchronizerImpl
{
    public ItsNatDocSynchronizerImpl()
    {
    }

    protected abstract void syncMethod();

    public void exec(final ItsNatDocumentImpl itsNatDoc)
    {
        // Sincronizamos también los padres, así podremos acceder al documento padre
        // desde un hilo que accede al hijo sin tener que sincronizar y por otra parte un hilo
        // del padre podrá acceder al hijo sincronizando el documento pero sin problemas
        // dead locks respecto a accesos del hijo al padre.

        ItsNatStfulDocumentImpl parentItsNatDoc = itsNatDoc.getParentItsNatStfulDocument();
        if (parentItsNatDoc != null)
        {
            final ItsNatDocSynchronizerImpl childExecutor = this;

            ItsNatDocSynchronizerImpl executor = new ItsNatDocSynchronizerImpl()
            {
                protected void syncMethod()
                {
                    synchronized(itsNatDoc) // El documento del "childExecutor"
                    {
                        childExecutor.syncMethod();
                    }
                }
            };
            executor.exec(parentItsNatDoc);
        }
        else
        {
            synchronized(itsNatDoc)
            {
                syncMethod();
            }
        }
    }
}

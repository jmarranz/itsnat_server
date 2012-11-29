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

package org.itsnat.impl.core.servlet;

import java.security.MessageDigest;
import javax.servlet.http.HttpSession;
import org.itsnat.core.ItsNatException;
import org.itsnat.impl.core.util.*;

/**
 *
 * @author jmarranz
 */
public class ItsNatServletContextUniqueIdGenImpl implements UniqueIdGenerator
{
    protected MessageDigest md;
    protected ItsNatServletContextImpl context;
    protected UniqueIdGenIntList genIntList;
    protected UniqueIdGenMessageDigest genMesDigest;
    protected boolean frozen = false;

    /** Creates a new instance of UniqueIdGenerator */
    public ItsNatServletContextUniqueIdGenImpl(ItsNatServletContextImpl context)
    {
        this.context = context;
        initIdGenerators();
    }

    public void initIdGenerators()
    {
        if (context.isSessionReplicationCapable())
        {
            this.genIntList = null;
            this.genMesDigest = new UniqueIdGenMessageDigest();
        }
        else
        {
            this.genIntList = new UniqueIdGenIntList(true);
            this.genMesDigest = null;
        }
    }

    public void notifySessionReplicationCapableChanged()
    {
        if (frozen) throw new ItsNatException("Too late some servlet has already received events");
        initIdGenerators();
    }

    public UniqueId generateUniqueId(HttpSession session,String prefix)
    {
        this.frozen = true;

        String id;
        if (context.isSessionReplicationCapable())
        {
            // Este método genera un id único si el idRef es único usando SHA-1
            // Es útil (y es su único uso) en la generación del id de sesión
            // de ItsNat cuando se usa replicación de sesiones por ejemplo en GAE
            // pues el ItsNatServletContextImpl no puede compartirse entre nodos
            // (se podría usando una caché compartida y algún método atómico)
            // por tanto el enfoque de generación de ids únicos usando enteros
            // no nos vale pues deben ser únicos para todos los nodos.
            // El generar un id SHA diferente al id de la sesión original y no reversible es importante
            // porque en control remoto este id es público para poder acceder a una sesión
            // de un usuario diferente, este id de ItsNat sólo será útil en ItsNat
            // y sus acciones posibles estarán controladas por ItsNat (autorización etc)
            // si publicáramos el id nativo de la sesión estaríamos dando control
            // ABSOLUTO al usuario controlador más allá de ItsNat.

            id = genMesDigest.generateId(session.getId(),prefix);
        }
        else
        {
            id = genIntList.generateId(prefix);
        }

        return new UniqueId(id,this);
    }

}

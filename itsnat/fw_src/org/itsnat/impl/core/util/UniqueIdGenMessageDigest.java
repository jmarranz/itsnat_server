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

package org.itsnat.impl.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.itsnat.core.ItsNatException;

/**
 *
 * @author jmarranz
 */
public class UniqueIdGenMessageDigest implements UniqueIdGenerator
{
    protected MessageDigest md;

    /** Creates a new instance of UniqueIdGenerator */
    public UniqueIdGenMessageDigest()
    {
        try
        {
            md = MessageDigest.getInstance("SHA"); // SHA-1 (18 bytes) es menos vulnerable que MD5 (16)
        }
        catch(NoSuchAlgorithmException ex) { throw new ItsNatException(ex); }
    }

    public String generateId(String idRef,String prefix)
    {
        // Este método genera un id único si el idRef es único usando SHA-1

         MessageDigest newMd;
        try
        {
            newMd = (MessageDigest)md.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            throw new ItsNatException(ex);
        }

        // Al ser una nueva instancia el MessageDigest podemos hacerlo multihilo sin sincronizar

        byte[] idBytes = newMd.digest(idRef.getBytes());

        StringBuilder code = new StringBuilder();
        code.append(prefix + "_");
        for(int i = 0; i < idBytes.length; i++)
        {
            int n = idBytes[i];
            n = n & 0x000000FF;
            code.append(Integer.toHexString(n));
        }

        return code.toString();
    }
}

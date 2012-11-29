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

/**
 * Clase de utilidad en el ámbito de resolver el problema
 * del "double-checked locking" (DCL)
   http://www.javaworld.com/javaworld/jw-05-2001/jw-0525-double.html
   http://www.oreillynet.com/onjava/blog/2007/01/singletons_and_lazy_loading.html
   http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
   http://en.wikipedia.org/wiki/Double-checked_locking
 *
 * En la JVM 1.5 se resuelve fácilmente con "volatile" pero en 1.4 e inferiores
 * puede resolverse con un ThreadLocal aunque sólo en 1.4 es suficientemente rápido
 * y no evita la sincronización la primera vez que se chequea
 * http://www.javaworld.com/javaworld/jw-11-2001/jw-1116-dcl.html?page=3
 *
 * @author jmarranz
 */
public class ThreadLocalDCL
{
    protected static final boolean is1_4orLower; // toma un primer valor y no cambia

    static
    {
        String verStr = System.getProperty("java.vm.version");
        // Ej. java.vm.version=1.5.0_02-b09
        int pos = verStr.indexOf('.');
        int firstVer = Integer.parseInt(verStr.substring(0,pos));
        if (firstVer == 1)
        {
            // Versión 1.x
            verStr = verStr.substring(pos + 1);
            pos = verStr.indexOf('.');
            if (pos != -1)
                verStr = verStr.substring(0,pos);
            int secondVer = Integer.parseInt(verStr);

            is1_4orLower = (secondVer <= 4);
        }
        else
            is1_4orLower = false; // Es v2 o superior

        //int first = verStr.
    }

    public static ThreadLocal newThreadLocal()
    {
        if (is1_4orLower)
            return new ThreadLocal();
        else
            return null; // No es necesario, usar volatile resuelve el problema
    }
}

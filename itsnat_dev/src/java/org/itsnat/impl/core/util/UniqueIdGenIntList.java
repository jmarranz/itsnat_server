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

import java.io.Serializable;

/**
 * El objetivo es generar ids con la seguridad de que no volverán a repetirse
 * por lo que pueden perderse ids sin problema, además que el número posible
 * de ids generable sea infinito, no vale un simple contador.
 *
 * No tenemos el requisito de que sean persistentes (guardables en base de datos etc),
 * usar con fines de volatilidad.
 *
 * Sólo hay garantía de unicidad dentro del mismo generador (otros generadores pueden generar los mismos ids).
 *
 * Hay dos técnicas posibles:
 *
 * 1) Lista de números: cuando el primer número se agota (alcanza el máximo) se añade otro.
 * 2) System.currentTimeMillis() + un contador. Si el contador se agota (máximo)
 *    se pone a cero y obtiene un nuevo instante, si el instante no ha cambiado
 *    nos esperamos hasta que pase el milisegundo (este caso sólo ocurre con un computador de velocidad brutal y miles de millones de ids).
 *    Sobre el rendimiento de System.currentTimeMillis():
      http://www.devx.com/Java/Article/28685
 *
 * Elegimos por simplicidad, rapidez y no demoras la técnica 1)
 *
 * @author jmarranz
 */
public class UniqueIdGenIntList implements UniqueIdGenerator,Serializable
{
    protected final boolean sync;
    protected long[] counters;

    /** Creates a new instance of UniqueIdGenerator */
    public UniqueIdGenIntList(boolean sync)
    {
        this.sync = sync;
        this.counters = new long[1];
    }

    public UniqueId generateUniqueId(String prefix)
    {
        return new UniqueId(generateId(prefix),this);
    }

    public String generateId(String prefix)
    {
        long[] id;
        if (sync)
        {
            synchronized(this)
            {
                id = generateUniqueIdInternal(true);
            }
        }
        else id = generateUniqueIdInternal(false);

        // Esto se puede ejecutar en multihilo porque el id si es necesario fue clonado
        // ahora bien NO SACAR el objeto id fuera pues cuando no se clona es el propio objeto counters.
        StringBuffer code = new StringBuffer();
        code.append(prefix + "_");
        for(int i = 0; i < id.length; i++)
        {
            long curr = id[i];
            if (i > 0) code.append("_");
            code.append(Long.toString(curr));
        }

        return code.toString();
    }

    protected long[] generateUniqueIdInternal(boolean clone)
    {
        long last = counters[counters.length - 1];
        if (last == Long.MAX_VALUE) // Cambia Long.MAX_VALUE a un valor pequeño para test
        {
            // Seguramente jamás pasará por aquí, pero así evitamos el "efecto año 3000" usando supercomputadores
            long[] oldCounters = counters;
            this.counters = new long[oldCounters.length + 1];
            System.arraycopy(oldCounters,0,counters,0,oldCounters.length);
            last = counters[counters.length - 1];
        }

        last++;  // Empieza desde el uno
        counters[counters.length - 1] = last;

        if (clone)
        {
            // Clonamos para que en multihilo se pueda modificar el counters
            // sin problema por otros hilos al generar otros ids
            long[] id = new long[counters.length];
            System.arraycopy(counters,0,id,0,counters.length);
            return id;
        }
        else return counters;
    }

/*
    public static void main(String[] args)
    {
        // Cambiar antes Long.MAX_VALUE por un valor pequeño menor que 100
        UniqueIdGenerator gen = new UniqueIdGenerator(true);
        for(int i = 0; i < 100; i++)
        {
            String id = gen.generateId("n");
            System.out.println(id);
        }
    }
*/

}

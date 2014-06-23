package org.itsnat.droid.impl.util;

/**
 * Created by jmarranz on 9/06/14.
 */
public class UniqueIdGenerator
{
    protected long[] counters;

    /** Creates a new instance of UniqueIdGenerator */
    public UniqueIdGenerator()
    {
        this.counters = new long[1];
    }

    public String generateId(String prefix)
    {
        long[] id = generateUniqueIdInternal();

        // Esto se puede ejecutar en multihilo porque el id si es necesario fue clonado
        // ahora bien NO SACAR el objeto id fuera pues cuando no se clona es el propio objeto counters.
        StringBuilder code = new StringBuilder();
        code.append(prefix + "_");
        for(int i = 0; i < id.length; i++)
        {
            long curr = id[i];
            if (i > 0) code.append("_");
            code.append(Long.toString(curr));
        }

        return code.toString();
    }

    protected long[] generateUniqueIdInternal()
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

        return counters;
    }

/*
    public static void main(String[] args)
    {
        // Cambiar antes Long.MAX_VALUE por un valor pequeño menor que 100
        UniqueIdGenerator gen = new UniqueIdGenerator();
        for(int i = 0; i < 100; i++)
        {
            String id = gen.generateId("n");
            System.out.println(id);
        }
    }
*/

}

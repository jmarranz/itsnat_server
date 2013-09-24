package inexp.jreloadex;

/**
 *
 * @author jmarranz
 */
public class JReloadExampleAux {

    public static void log()
    {
        System.out.println("JReloadExampleAux: 48 " + JReloadExampleAux.class.getClassLoader().hashCode());
    }
}

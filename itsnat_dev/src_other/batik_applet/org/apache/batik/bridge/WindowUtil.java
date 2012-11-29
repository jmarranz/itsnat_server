
package org.apache.batik.bridge;

import org.apache.batik.script.Interpreter;
import org.apache.batik.script.Window;

/**
 *
 * @author jmarranz
 */
public class WindowUtil
{
    public static void changeInterpreter(Window window,Interpreter inter)
    {
        // El acceso a la propiedad "interpreter" es lo único que justifica
        // el que WindowUtil esté en org.apache.batik.bridge
        ((ScriptingEnvironment.Window)window).interpreter = inter;
    }
}

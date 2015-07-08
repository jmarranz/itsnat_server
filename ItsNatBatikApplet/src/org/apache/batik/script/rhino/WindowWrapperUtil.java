package org.apache.batik.script.rhino;

import org.apache.batik.script.Window;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author jmarranz
 */
public class WindowWrapperUtil
{
    public static Window getWindow(WindowWrapper winWrapper)
    {
        // El acceso a la propiedad "protected window" es lo que nos obliga
        // a poner esta clase en el mismo package que WindowWrapper
        // es decir "org.apache.batik.script.rhino"
        return winWrapper.window;
    }

    public static WindowWrapper getWindowWrapper(Scriptable scope)
    {
        Scriptable global = ScriptableObject.getTopLevelScope(scope);
        // Realmente no se necesita porque "global" es ya WindowWrapper pero para
        // asegurar que lo que queremos es el objeto dado por la referencia "window"
        return (WindowWrapper)global.get("window", global);
    }

    public static Window getWindow(Scriptable scope)
    {
        WindowWrapper winWrapper = getWindowWrapper(scope);
        return getWindow(winWrapper);
    }
}

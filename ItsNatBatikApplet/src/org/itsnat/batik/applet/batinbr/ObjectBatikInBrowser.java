
package org.itsnat.batik.applet.batinbr;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.script.rhino.RhinoInterpreterFixed;
import org.apache.batik.util.RunnableQueue;
import org.itsnat.batik.applet.ItsNatSVGOMDocumentBatik;
import org.itsnat.batik.applet.JSVGCanvasApplet;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author jmarranz
 */
public abstract class ObjectBatikInBrowser
{
    public abstract ItsNatSVGOMDocBatikInBrowser getItsNatSVGOMDocBatikInBrowser();
    public abstract Scriptable getScriptable();

    public Object callJSMethodFromBrowser(final String name,final Object[] params)
    {
        final Object[] res = new Object[1];

        // En este caso estamos en un hilo creado por el motor JavaScript del browser
        // por lo que el JavaScript que ejecutemos y que actualice el DOM
        // no se manifestará visualmente si no se ejecuta en el hilo
        // del update manager del JSVGCanvas
        // http://xmlgraphics.apache.org/batik/faq.html#display-does-not-update
        // http://xmlgraphics.apache.org/batik/faq.html#must-mouseover-to-change

        final Scriptable thisObj = getScriptable();

        final ItsNatSVGOMDocumentBatik batikDoc = getItsNatSVGOMDocBatikInBrowser().getItsNatSVGOMDocumentBatik();
        JSVGCanvasApplet canvas = batikDoc.getJSVGCanvasApplet();
        UpdateManager um = canvas.getUpdateManager();
        RunnableQueue queue = um.getUpdateRunnableQueue();
        try
        {
            queue.invokeAndWait(
                new Runnable()
                {
                    public void run()
                    {
                        Object[] jsParams;
                        if (params != null)
                        {
                            if (params.length == 0) jsParams = params;
                            else
                            {
                                jsParams = new Object[params.length];
                                for(int i = 0; i < params.length; i++)
                                {
                                    Object param = params[i];
                                    if (param != null)
                                    {
                                        // Este es un intento de hacer lo más interoperable posible Batik y el browser
                                        if (param instanceof ObjectBatikInBrowser)
                                            jsParams[i] = ((ObjectBatikInBrowser)param).getScriptable();
                                        else
                                            jsParams[i] = param;
                                    }
                                }
                            }
                        }
                        else jsParams = new Object[0];

                        RhinoInterpreterFixed interpreter = batikDoc.getRhinoInterpreterFixed();
                        Function func = (Function)ScriptableObject.getProperty(thisObj,name);
                        res[0] = interpreter.callJSMethod(func, thisObj,params);
                    }
                }
            );
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return res[0];
    }
}

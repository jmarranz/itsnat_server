package org.itsnat.batik.applet;

import org.apache.batik.script.rhino.WindowWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author jmarranz
 */
public class LocationBatik extends ScriptableObject
{
    protected WindowWrapper winWrapper;
    protected JSVGCanvasApplet canvas;

    public LocationBatik()
    {
        // Necesario para el defineClass
    }

    public void init(WindowWrapper winWrapper,JSVGCanvasApplet canvas)
    {
        this.winWrapper = winWrapper;
        this.canvas = canvas;
    }
    
    public static Scriptable createScriptable(WindowWrapper winWrapper,JSVGCanvasApplet canvas)
    {
        // Si creamos el objeto ScriptableObject con new, las propiedades
        // via jsGet_ etc no funcionan, el objeto necesita ser construido/inicializado por Rhino.
        Context ctx = Context.getCurrentContext();
        LocationBatik loc = (LocationBatik)ctx.newObject(winWrapper,getClassNameStatic());
        loc.init(winWrapper, canvas);
        return loc;
    }

    public static void registerClass(Scriptable scope)
    {
        try
        {
            ScriptableObject.defineClass(scope,LocationBatik.class);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    public void jsConstructor()
    {
    }

    @Override
    public String getClassName()
    {
        return getClassNameStatic();
    }

    public static String getClassNameStatic()
    {
        return "Location";
    }

    @Override
    public Object getDefaultValue(Class typeHint)
    {
        // Por si hacemos por ejemplo: alert(window.location);
        return jsGet_href();
    }

    // Las demás propiedades hash, port etc no merece la pena implementarse
    public String jsGet_href()
    {
        return canvas.getSVGOMDocument().getURL();
    }

    public void jsSet_href(String href)
    {
        canvas.loadDocumentFromURL(href);
    }

    public String jsFunction_toString()
    {
        return jsGet_href();
    }

    public void jsFunction_assign(String url)
    {
        jsSet_href(url);
    }

    public void jsFunction_reload(boolean forceget)
    {
        // forceget no es relevante (siempre true)
        jsSet_href(jsGet_href());
    }
    
    public void jsFunction_reload()
    {
        jsFunction_reload(false);
    }
    
    public void jsFunction_replace(String url)
    {
        // Como no hay historial es igual que assign
        jsSet_href(url);
    }
}

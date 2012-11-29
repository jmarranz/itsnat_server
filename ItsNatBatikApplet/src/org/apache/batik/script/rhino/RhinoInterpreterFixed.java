package org.apache.batik.script.rhino;

import java.net.URL;
import java.security.AccessControlContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author jmarranz
 */
public class RhinoInterpreterFixed extends RhinoInterpreter
{
    protected RhinoInterpreterFixed(URL documentURL)
    {
        super(documentURL);
    }

    public static RhinoInterpreterFixed createRhinoInterpreterFixed(URL documentURL)
    {
        return new RhinoInterpreterFixed(documentURL);
    }

    public static void avoidGlobalImports()
    {
        // Evitamos que se registren packages por defecto (con importPackage(...))
        // Rhino tiende a resolver cualquier indentificador global con estos imports
        // para ver si es una clase Java y así un simple alert(Node.ELEMENT_NODE)
        // busca entre los packages importados por defecto y resuelve que Node existe como org.w3c.dom.Node
        // (pues org.w3c.dom es un package por defecto).
        // El problema es que el ámbito de un applet esta búsqueda se realiza
        // a través de un URLClassLoader el cual tiende a pedir al servidor todas
        // las posibles combinaciones de resolución. Esto he comprado que tiene
        // un impacto BRUTAL en el rendimiento del código JavaScript sobre todo si
        // el applet se ejecuta remótamente.
        // Este problema es conocido y su causa (el importado por defecto)
        // está en el constructor de RhinoInterpreter que registra los packages
        // de la lista estática global: TO_BE_IMPORTED
        // Sobre este asunto:
        // http://old.nabble.com/Cache-entry-not-found---slows-drawing-display-td21562669.html
        // http://www.nabble.com/FW%3A-Strange-applet-delay-revisited-to21494010.html
        // http://old.nabble.com/attachment/21502224/0/disable-rhino-class-loader.patch

        // Este problema se puede comprobar de la forma siguiente:
        // Dejar el TO_BE_IMPORTED original o bien hacer un TO_BE_IMPORTED = new String[] { "foo.faa" };
        // ejecutar el applet y en la consola de Java poner el nivel de rastreo 2 (red)
        // y recargar el applet. Verás intentos de carga http://..../foo/NombreGlobal.class
        // Es más el propio importPackage("foo.faa") provoca dos llamadas
        // http://..../foo.class  y http://..../foo/faa.class

        // Al evitar los import por defecto como consecuencia, YA NO PODEMOS SUPONER LOS IMPORT POR DEFECTO
        // por ejemplo un simple alert(Node.ELEMENT_NODE) ya no funciona
        // ha de ser:  alert(Packages.org.dom.w3c.Node.ELEMENT_NODE)

        // Nota: los paquetes que se auto-importaban eran:
        /*
        "java.lang",
        "org.w3c.dom",
        "org.w3c.dom.css",
        "org.w3c.dom.events",
        "org.w3c.dom.smil",
        "org.w3c.dom.stylesheets",
        "org.w3c.dom.svg",
        "org.w3c.dom.views",
        "org.w3c.dom.xpath"
        */

        // Por la naturaleza del código de RhinoInterpreter
        // tenemos que poner al menos uno pues de otra manera falla
        // No elegimos "org.w3c.dom" pues Rhino de todas formas intenta resolver
        // org.class, w3.class y dom.class, en el caso de java.lang es un paquete
        // que Rhino conoce y no hace eso.

        TO_BE_IMPORTED = new String[] { "java.lang" };
    }

    public AccessControlContext getAccessControlContext()
    {
        if (rhinoClassLoader == null) return null;
        return super.getAccessControlContext();
    }

    public ContextFactory getContextFactory()
    {
        // Ya que redefinimos la clase hacemos público el ContextFactory de RhinoInterpreter que necesitamos
        // así no obligamos a las clases que lo necesiten a que estén en este
        // package (contextFactory es protected).

        // Este ContextFactory es FUNDAMENTAL para crear/obtener Context
        // en vez de usar la estándar, Context cx = Context.enter();
        // si queremos que el contexto nuevo asociado al nuevo hilo (en el caso
        // AJAX asíncrono) tenga las mismas características de seguridad
        // que los demás contextos, pues el contextFactory apunta a un
        // ContextFactory especial que redefine el método makeContext()
        // el cual es llamado cuando el hilo no tiene un contexto asociado
        // En especial la llamada crítica que se hace es makeContext() es:
        // cx.setSecurityController(securityController);
        // es decir el nuevo Context tiene el SecurityController por defecto.
        // Definir este SecurityController es NECESARIO para que podamos
        // compilar bajo demanda código nuevo por ejemplo via eval() o Function
        // lo cual es IMPRESCINDIBLE en ItsNat pues retornamos JavaScript nuevo.
        // De otra manera se produce un NullPointerException
        // en BatikSecurityController.callWithDomain, podríamos evitarlo
        // redefiniendo BatikSecurityController.callWithDomain en una nueva
        // clase y cambiando RhinoInterpreter.securityController pero sería una chapuza.

        return contextFactory;
    }

    public Object callJSMethod(Function func,Scriptable thisObj,Object[] params)
    {
        boolean newThread;
        Scriptable globalScope;

        Context cx = Context.getCurrentContext();
        if (cx == null)
        {
            newThread = true;
            // ¡¡¡NO!!!: Context cx = Context.enter();
            cx = getContextFactory().enter();
            //globalScope = cx.initStandardObjects(); Al parecer es un método costoso en tiempo y memoria
            globalScope = ScriptableObject.getTopLevelScope(thisObj);
        }
        else
        {
            newThread = false; // Es un hilo ya usado para ejecutar scripts
            globalScope = ScriptableObject.getTopLevelScope(thisObj);
        }

        try
        {
            try
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
                                if (param instanceof Scriptable)
                                    jsParams[i] = param;
                                else
                                    jsParams[i] = Context.javaToJS(params[i],globalScope);
                            }
                        }
                    }
                }
                else jsParams = new Object[0];

                return func.call(cx, globalScope, thisObj,jsParams);
            }
            catch (org.mozilla.javascript.JavaScriptException e)
            {
                e.printStackTrace();
                throw new org.mozilla.javascript.WrappedException(e);
            }
        }
        finally
        {
            if (newThread) cx.exit(); // Liberamos el thread
        }
    }

    public Object evaluateString(Scriptable scope,String code)
    {
        boolean newThread;

        Context cx = Context.getCurrentContext();
        if (cx == null)
        {
            newThread = true;
            // ¡¡¡NO!!!: Context cx = Context.enter();
            cx = getContextFactory().enter();
            cx.initStandardObjects(); // No estoy seguro de si es necesario
        }
        else
        {
            newThread = false; // Es un hilo ya usado para ejecutar scripts
        }

        try
        {
            try
            {
                return cx.evaluateString(scope, code,"anonymous",0,null);
            }
            catch (org.mozilla.javascript.JavaScriptException e)
            {
                e.printStackTrace();
                throw new org.mozilla.javascript.WrappedException(e);
            }
        }
        finally
        {
            if (newThread) cx.exit(); // Liberamos el thread
        }
    }
}

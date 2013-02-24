package org.itsnat.batik.applet;

import java.io.IOException;
import java.io.InputStream;
import org.itsnat.batik.applet.batinbr.ItsNatSVGOMDocBatikInBrowser;
import org.itsnat.batik.applet.brinbat.WindowBrowserInBatik;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JApplet;
import netscape.javascript.JSObject;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.script.Window;
import org.apache.batik.script.rhino.RhinoInterpreterFixed;
import org.apache.batik.script.rhino.WindowWrapper;
import org.apache.batik.script.rhino.WindowWrapperUtil;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.RunnableQueue;
import org.apache.batik.util.XMLResourceDescriptor;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author jmarranz
 */
public class JSVGCanvasApplet extends JSVGCanvas
{
    static
    {
        // Lo primero de todo hay que hacer esto para evitar este problema:
        // http://old.nabble.com/FW%3A-Strange-applet-delay-revisited-to21494010.html
        // http://old.nabble.com/attachment/21502224/0/disable-rhino-class-loader.patch
        // Este problema se puede ver en la consola de los applets poniendo un nivel de rastreo 2 ("red")
        // (obviamente sin aplicar la solución).
        
        // Hay que tener en cuenta que ahora hemos quitado las declaraciones
        // globales de algunos paquetes por lo que algunas cosas que se documentan
        // en Batik con JavaScript PUEDEN NO FUNCIONAR (cuando se dice que new String("hola")
        // puede llamarse directamente etc).
        
        RhinoInterpreterFixed.avoidGlobalImports();
    }

    protected JApplet applet;
    protected WindowWrapper winWrapper;
    protected WindowBrowserInBatik browserWin;
    protected ItsNatSVGOMDocumentBatik docBatik;
    protected ItsNatSVGOMDocBatikInBrowser docBrowser;
    protected String cookie;
    
    public JSVGCanvasApplet(JApplet applet)
    {
        this.applet = applet;
    }

    public JApplet getJApplet()
    {
        return applet;
    }

    public String getCookie()
    {
        return cookie;
    }

    public WindowWrapper getWindowWrapper()
    {
        return winWrapper;
    }

    public void setWindowWrapper(WindowWrapper winWrapper)
    {
        this.winWrapper = winWrapper;
        this.browserWin = new WindowBrowserInBatik(winWrapper,applet);

        // WindowWrapper es un objeto Scriptable
        LocationBatik.registerClass(winWrapper);
        Scriptable locScrip = LocationBatik.createScriptable(winWrapper, this);
        winWrapper.put("location",winWrapper,locScrip);
    }


    public void start()
    {
        try
        {
            // Obtenemos la cookie del browser sobre todo con la intención
            // de obtener la sesión Java, esto únicamente es necesario para Chrome
            // pues al (re)insertar por ejemplo el applet via JavaScript al parecer
            // el nuevo applet no envía la cookie del browser
            JSObject window = (JSObject) JSObject.getWindow(applet);
            JSObject document = (JSObject) window.getMember("document");
            this.cookie = (String)document.getMember("cookie");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public void stop()
    {
        try
        {
            setDocument(null); // Removes the document.
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // No relanzamos el error pues inútilmente hace recargar la JVM
            // throw new RuntimeException(ex);
        }
    }

    public void destroy()
    {
        try
        {
            dispose();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public WindowBrowserInBatik getWindowBrowserInBatik()
    {
        return browserWin;
    }

    public SVGOMDocument getSVGOMDocument()
    {
        return (SVGOMDocument)super.getSVGDocument();
    }

    public void loadSVGDocument(String urlStr)
    {
        // Este método lo redefinimos porque es el llamado cuando se pulsa un link,
        // el comportamiento por defecto tiene dos problemas:
        // 1) La carga de un nuevo documento por defecto no es sufiente para ItsNat
        //    como se puede ver en el código de loadDocumentFromURL.
        // 2) Batik no soporta "javascript:..." en links

        // Sabemos que este método no se llama si el URL coincide con el ya cargado
        // es decir no podemos usar el link para varios reload.

        urlStr = urlStr.trim();
        if (urlStr.startsWith("javascript:"))
        {
            executeJSCode(urlStr.substring("javascript:".length()));
        }
        else
        {
            // El URL será ya absoluto, por tanto puede no corresponderse con el href original.
            loadDocumentFromURL(urlStr);
        }
    }

    public void executeJSCode(String code)
    {
        // El código a ejecutar es que está en un link
        Window win = WindowWrapperUtil.getWindow(winWrapper);
        RhinoInterpreterFixed interpreter = (RhinoInterpreterFixed)win.getInterpreter();
        interpreter.evaluateString(winWrapper,code);
    }

    public synchronized void loadDocumentFromURL(String urlStr)
    {
        // Este método lo redefinimos porque es el llamado cuando se pulsa un link,
        // el comportamiento por defecto tiene dos problemas:
        // 1) La carga de un nuevo documento por defecto no es sufiente para ItsNat
        //    como se puede ver en el codigo de este método.
        // 2) Batik no soporta "javascript:..." en links
        // El synchronized es por si acaso.

        urlStr = urlStr.trim();

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        URL baseURL = applet.getDocumentBase();
        URL url = buildURL(baseURL,urlStr);

        System.out.println("Loading:" + url.toString()); // Para que el programador detecte errores tontos en la formación de la URL

        SVGOMDocument doc;

        try
        {
            URLConnection urlCon = url.openConnection();

            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("User-Agent", ParsedURL.getGlobalUserAgent()); // "Batik/development.build"
            urlCon.setRequestProperty("Cache-Control","no-store,max-age=0,no-cache");
            urlCon.setRequestProperty("Expires", "0");
            urlCon.setRequestProperty("Pragma", "no-cache");

            if ((cookie != null) && cookie.length() > 0)
                urlCon.setRequestProperty("Cookie",cookie);

            InputStream stream = urlCon.getInputStream();
            doc = (SVGOMDocument)f.createDocument(url.toString(),stream);
            stream.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        System.out.println("Finished load:" + url.toString());

        registerInSVGDocument(doc); // Lo antes posible por si acaso, antes de asociar el documento al canvas

        // Display the document.
        setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        setDocument(doc);

        System.out.println("Ready to display:" + url.toString());
    }

    public static URL buildURL(URL baseURL,String relPath)
    {
        relPath = relPath.trim();

        try
        {
            return new URL(relPath); // If sucesses it already was absolute
        }
        catch(MalformedURLException ex)
        {
            StringBuilder newURL = new StringBuilder();
            newURL.append(baseURL.getProtocol());
            newURL.append(":");
            newURL.append("//");
            String authority = baseURL.getAuthority();
            newURL.append(authority);
            String basePath = baseURL.getPath();
            if (basePath != null)
            {
                if (relPath.startsWith("?"))
                {
                    newURL.append(basePath);
                }
                else if (relPath.startsWith("/")) // Absolute basePath without protocol,host...
                {
                    // Nothing to do
                }
                else // Examples: "servlet?..."  or "someRelPath/servlet?..."
                {
                    if (basePath.endsWith("/"))
                    {
                        newURL.append(basePath);
                    }
                    else // basePath is ".../servlet"
                    {
                        // Removing "servlet"
                        basePath = basePath.substring(0,basePath.lastIndexOf("/") + 1);
                        newURL.append(basePath);
                    }
                }
            }

            newURL.append(relPath);

            try
            {
                return new URL(newURL.toString());
            }
            catch(MalformedURLException ex2) { throw new RuntimeException(ex2); }
        }
    }

    public void registerInSVGDocument(SVGOMDocument doc)
    {
        synchronized(doc)
        {
            doc.setUserData("itsnat_svg_canvas",this,null);
        }
    }

    public static JSVGCanvasApplet getJSVGCanvasApplet(SVGOMDocument doc)
    {
        synchronized(doc)
        {
            return (JSVGCanvasApplet)doc.getUserData("itsnat_svg_canvas");
        }
    }

    public void createWrapperDocuments()
    {
        // Envolvemos en un objeto especial ItsNatSVGOMDocument porque Batik define "document" como una referencia
        // al objeto nativo DOM Java (SVGOMDocument) pero éste NO es ScriptableObject por lo que
        // no podemos asociar nuevas propiedades y métodos, esto es un problema en ItsNat
        // porque necesitamos vincular document.itsNatDoc y llamar a document.getItsNatDoc(), la alternativa es vincular el método con get/setUserData
        // pero como document.getItsNatDoc es un método público para el usuario habría que
        // decirle al usuario que usara otra sintaxis en Batik.
        // Casualmente descubrí que new Object(document) convertía document (o el objeto resultante)
        // en un Scriptable y a la vez todos los métodos de document seguían siendo visibles,
        // el problema es que el objeto resultante o el propio document pasaba a ser
        // una especie de referencia a Object como clase por lo que al asociar itsNatDoc y getItsNatDoc
        // se vinculaban estos a nivel de prototype de Object, introduciendo problemas
        // a la hora de utilizar los Object como colección de propiedades (en donde ya están dos siempre).

        // El WrapFactory por defecto no sirve, de hecho es el que yo creo que se usa
        // para hacer los wrapper JavaScript a los objetos Java vistos desde JavaScript,
        // al parecer el Scriptable que usa no es un ScriptableObject que permitiría añadir dinámicamente propiedades/métodos.

        // La alternativa es un objeto Java "normal" ItsNatSVGOMDocument que define get/setItsNatDoc
        // los cuales permiten definir itsNatDoc como si fuera una propiedad.

        // La solución es parcial puesto que los demás objetos DOM obtenidos
        // a través de document siguen siendo nativos por lo que no pueden definirse
        // propiedades/métodos nuevos. Esto no es problema pues la necesidad
        // es interna de ItsNat en este caso y se soluciona usando los métodos
        // DOM3 set/getUserData


        // Esto no ocurre con window que es un objeto WindowWrapper que deriva
        // de ImporterTopLevel que al final deriva de ScriptableObject

        // Links:
        // http://ejohn.org/blog/bringing-the-browser-to-the-server/#postcomment
        // https://developer.mozilla.org/en/Scripting_Java
        // http://www.mozilla.org/rhino/tutorial.html (viejo)
        // http://www.mozilla.org/rhino/scriptjava.html (viejo)
        // Nota: en general la implementación de interfaces y clases Java
        // extendiendo objetos JavaScript no funciona porque requiere crear
        // un class loader lo cual no está permitido en el contexto de un applet
        // no firmado.

        this.docBatik = new ItsNatSVGOMDocumentBatik(getSVGOMDocument(),this);
        this.docBrowser = new ItsNatSVGOMDocBatikInBrowser(docBatik);
    }

    public ItsNatSVGOMDocumentBatik getItsNatSVGOMDocumentBatik()
    {
        return docBatik;
    }

    public ItsNatSVGOMDocBatikInBrowser getItsNatSVGOMDocBatikInBrowser()
    {
        return docBrowser;
    }

    public RhinoInterpreterFixed getRhinoInterpreterFixed()
    {
        return (RhinoInterpreterFixed)WindowWrapperUtil.getWindow(winWrapper).getInterpreter();
    }

    public String getSrcFromBrowser()
    {
        // Esta llamada no tiene problemas de hilos como sí la tiene setSrcFromBrowser
        return getSVGOMDocument().getURL();
    }

    public void setSrcFromBrowser(final String url)
    {
        // El ejecutar la llamada dentro del hilo de Batik usando
        // RunnableQueue lo hacemos por Safari (3.1 Windows al menos)
        // En Safari y a partir de una actualización de Java 1.6
        // al parecer el hilo que usa Safari para llamar desde JavaScript en la página padre
        // a los métodos exportados por el applet, no es un hilo "bendecido" por el applet
        // por lo que curiosamente las limitaciones de seguridad son aún mayores
        // y por tanto la conexión HTTP desde el applet no se permite.
        // http://forums.java.net/jive/thread.jspa?messageID=300648
        // De todas formas de esta manera a lo mejor evitamos sospechosos "interrupted thread"
        // en otros navegadores.

        try
        {
            RunnableQueue queue = getRunnableQueue();
            if (queue == null)
            {
                loadDocumentFromURL(url);
            }
            else
            {
                queue.invokeAndWait(
                        new Runnable()
                        {
                            public void run()
                            {
                                loadDocumentFromURL(url);
                            }
                        }
                    );
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            // NO LA relanzamos para evitar que el applet muestre un mensaje
            // de error, el usuario podrá ver qué ha pasado a través de la
            // consola.
            // Así por ejemplo podemos cambiar el atributo del URL en el servidor
            // a una dirección inútil y volver a poner la antigua con el fin
            // de que ItsNat detecte el cambio.
        }
    }

    public RunnableQueue getRunnableQueue()
    {
        UpdateManager um = getUpdateManager();
        if (um == null)
        {
            // Cuando se recarga el applet (ej al recargar la página que contiene al applet)
            // al ejecutar un evento AJAX unload el UpdateManager ya es nulo
            // (el que sea un evento unload asíncrono no está del todo claro)
            return null;
        }
        RunnableQueue queue = um.getUpdateRunnableQueue();
        if (queue.getThread() == null)
        {
            // He detectado este caso cuando se hace pulsa un link
            // que por ejemplo tiende a recargar la página SVG,
            // un posterior invokeAndWait fallaría
            // Fue un test en el que el JSVGCanvas tenía un LinkActivationListener
            // registrado.
            return null;
        }
        return queue;
    }
}

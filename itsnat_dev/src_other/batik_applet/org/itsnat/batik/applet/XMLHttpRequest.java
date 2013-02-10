//=============================================================================
//Change History:
//Date     UserId      Defect          Description
//----------------------------------------------------------------------------
//07/02/05 ant         ???             Initial version.
//09/02/05 ant                         Support both HTTP GET and POST
//14/02/05 ant                         Fix call backs in functions
//20/02/05 ant                         Support HTTP HEAD
//28/02/05 ant                         HTTP basic authentication support
//
package org.itsnat.batik.applet;


import org.apache.batik.script.rhino.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// jmarranz: import org.apache.xerces.parsers.DOMParser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.script.Window;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.RunnableQueue;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XMLHttpRequest simulates the Mozilla XMLHttpRequest.
 *
 * Add this class to the Rhino classpath and then define to Rhino
 * with <code>defineClass('xmlhttp.XMLHttpRequest');</code>
 *
 * @author <a href="mailto:ant.elder@uk.ibm.com">Ant Elder </a>
 *
 * jmarranz notes:
 * Original got from: http://www.ibm.com/developerworks/library/ws-ajax1/
 * ws-ajax1code.zip => E4XUtils.jar => XMLHttpRequest
 * Tuned for ItsNat Java AJAX Framework
 * Some ideas from:
 * view-source:http://mcc.id.au/temp/2007/XMLHttpRequestWrapper.java
 * view-source:http://mcc.id.au/temp/2007/XMLHttpRequester.java
 */
public class XMLHttpRequest extends ScriptableObject
{
    private String url;
    private String httpMethod;
    private HttpURLConnection urlCon;

    private int httpStatus;
    private String httpStatusText;

    private Map requestHeaders;

    private String userName;
    private String password;

    private String responseText;
    private Document responseXML;

    private int readyState;
    private NativeFunction readyStateChangeFunction;

    private boolean asyncFlag;
    private Thread asyncThread;

    protected Window window;

    public XMLHttpRequest()
    {
    }

    public void jsConstructor()
    {
        // jmarranz:
        this.window = WindowWrapperUtil.getWindow(this);

        // Definiendo aquí podrá sobreescribirse luego si se quiere.
        jsFunction_setRequestHeader("User-Agent", ParsedURL.getGlobalUserAgent()); // "Batik/..."
        jsFunction_setRequestHeader("Cache-Control","no-store,max-age=0,no-cache");
        jsFunction_setRequestHeader("Expires", "0");
        jsFunction_setRequestHeader("Pragma", "no-cache");

        JSVGCanvasApplet canvas = getJSVGCanvasApplet();
        if (canvas != null) // Por si acaso pues al parecer se ha detectado un caso
        {
            String cookie = canvas.getCookie();
            if ((cookie != null) && (cookie.length() > 0))
                jsFunction_setRequestHeader("Cookie",cookie);
        }
    }

    public String getClassName()
    {
        // Este es el nombre de la clase para ser usado en JavaScript, el nombre público,
        // tal que new XMLHttpRequest() es suficiente si registramos esta clase
        // con ScriptableObject.defineClass y el objeto "window" como scope
        // tal que new window.XMLHttpRequest() también es válido lo cual
        // simula el soporte nativo de XMLHttpRequest típico de los navegadores.

        // Es decir no se necesita Packages.org.apache..., es más si usamos el package
        // por ejemplo new Packages.org.apache.batik.script.rhino.XMLHttpRequest(),
        // el objeto devuelto NO es un ScriptableObject sino el objeto Java,
        // el problema es que ni eso, no tiene métodos ni propiedades, porque el paquete org.apache.batik.script.rhino
        // parece que está bloqueado por Batik en JavaScript para no acceder al mismo
        // (cualquier otra clase en un paquete fuera de los de Batik si puede crearse
        // con un new Packages...NombreClase() y los métodos, atributos etc se ven
        // reflejados en JavaScript.

        return "XMLHttpRequest";
    }

    public RhinoInterpreterFixed getRhinoInterpreterFixed()
    {
        return (RhinoInterpreterFixed)window.getInterpreter();
    }

    public SVGOMDocument getSVGOMDocument()
    {
        return (SVGOMDocument)window.getBridgeContext().getDocument();
    }
    
    public JSVGCanvasApplet getJSVGCanvasApplet()
    {
        SVGOMDocument doc = getSVGOMDocument();
        return JSVGCanvasApplet.getJSVGCanvasApplet(doc);
    }

    public void jsFunction_setRequestHeader(String headerName, String value) {
        if (readyState > 1) {
            throw new IllegalStateException("request already in progress");
        }

        if (requestHeaders == null) {
            requestHeaders = new HashMap();
        }

        requestHeaders.put(headerName, value);
    }

    public Map jsFunction_getAllResponseHeaders() {
        if (readyState < 3) {
            throw new IllegalStateException(
                    "must call send before getting response headers");
        }
        return urlCon.getHeaderFields();
    }

    public String jsFunction_getResponseHeader(String headerName) {
        return jsFunction_getAllResponseHeaders().get(headerName).toString();
    }

    public void jsFunction_open(String httpMethod, String url,
            boolean asyncFlag, String userName, String password) {

        if (readyState != 0) {
            throw new IllegalStateException("already open");
        }

        this.httpMethod = httpMethod;

        if (url.startsWith("http")) {
            this.url = url;
        } else {
            throw new IllegalArgumentException("URL protocol must be http: "
                    + url);
        }

        this.asyncFlag = asyncFlag;

        if ("undefined".equals(userName) || "".equals(userName)) {
            this.userName = null;
        } else {
            this.userName = userName;
        }
        if ("undefined".equals(password) || "".equals(password)) {
            this.password = null;
        } else {
            this.password = password;
        }
        if (this.userName != null) {
            setAuthenticator();
        }

        setReadyState(1);
    }

    private void setAuthenticator() {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password
                        .toCharArray());
            }
        });
    }

    public void jsFunction_send(Object o)
    {
        final String content = (o == null) ? "" : o.toString();
        if (asyncFlag) {
            Runnable r = new Runnable() {
                public void run() {
                    doSend(content);
                }
            };
            this.asyncThread = new Thread(r);
            asyncThread.start();
        } else {
            doSend(content);
        }
    }

    public void jsFunction_abort() {
        if (asyncThread != null) {
            asyncThread.interrupt();
        }
    }

    /**
     * @return Returns the readyState.
     */
    public int jsGet_readyState() {
        return readyState;
    }

    /**
     * @return Returns the responseText.
     */
    public String jsGet_responseText() {
        if (readyState < 2) {
            throw new IllegalStateException("request not yet sent");
        }
        return responseText;
    }

    /**
     * @return Returns the responseXML as a DOM Document.
     */
    public Document jsGet_responseXML() {
        if (responseXML == null && responseText != null) {
            convertResponse2DOM();
        }
        return responseXML;
    }

    private void convertResponse2DOM() {
        try {

            //jmarranz: DOMParser parser = new DOMParser();
            // Reemplazado por la técnica estándar que también usa Xerces (en el caso de JVM de Sun)
            DocumentBuilderFactory docBuildFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuild = docBuildFac.newDocumentBuilder();
            StringReader sr = new StringReader(jsGet_responseText());
            //jmarranz: parser.parse(new InputSource(sr));
            //jmarranz: this.responseXML = parser.getDocument();
            this.responseXML = docBuild.parse(new InputSource(sr));

        } catch (SAXException e) {
            throw new RuntimeException("ex: " + e, e);
        } catch (IOException e) {
            throw new RuntimeException("ex: " + e, e);
        } catch (ParserConfigurationException e) {  // jmarranz
            throw new RuntimeException("ex: " + e, e);
        }
    }

    /**
     * @return Returns the htto status.
     */
    public int jsGet_status() {
        return httpStatus;
    }

    /**
     * @return Returns the http status text.
     */
    public String jsGet_statusText() {
        return httpStatusText;
    }

    /**
     * @return Returns the onreadystatechange.
     */
    public Object jsGet_onreadystatechange() {
        return readyStateChangeFunction;
    }

    /**
     * @param onreadystatechange
     *            The onreadystatechange to set.
     */
    public void jsSet_onreadystatechange(NativeFunction function) {
        readyStateChangeFunction = function;
    }

    private void doSend(String content) {

        connect(content);

        setRequestHeaders();

        try {
            urlCon.connect();
        } catch (IOException e) {
            throw new RuntimeException("ex: " + e, e);
        }

        sendRequest(content);

        if ("POST".equals(this.httpMethod) || "GET".equals(this.httpMethod)) {
            readResponse();
        }

        setReadyState(4);

    }

    private void connect(String content) {
        try {

            URL url = new URL(this.url);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod(httpMethod);
            if ("POST".equals(this.httpMethod) || content.length() > 0) {
                urlCon.setDoOutput(true);
            }
            if ("POST".equals(this.httpMethod) || "GET".equals(this.httpMethod)) {
                urlCon.setDoInput(true);
            }
            urlCon.setUseCaches(false);

        } catch (MalformedURLException e) {
            throw new RuntimeException("MalformedURLException: " + e, e);
        } catch (IOException e) {
            throw new RuntimeException("IOException: " + e, e);
        }
    }

    private void setRequestHeaders() {
        if (this.requestHeaders != null) {
            for (Iterator i = requestHeaders.keySet().iterator(); i.hasNext();) {
                String header = (String) i.next();
                String value = (String) requestHeaders.get(header);
                urlCon.setRequestProperty(header, value);
            }
        }
    }

    private void sendRequest(String content) {
        try {

            if ("POST".equals(this.httpMethod) || content.length() > 0) {
                OutputStreamWriter out = new OutputStreamWriter(urlCon
                        .getOutputStream(), "ASCII");
                out.write(content);
                out.flush();
                out.close();
            }

            httpStatus = urlCon.getResponseCode();
            httpStatusText = urlCon.getResponseMessage();

        } catch (IOException e) {
            throw new RuntimeException("IOException: " + e, e);
        }

        setReadyState(2);
    }

    private void readResponse() {
        try {

            InputStream is = urlCon.getInputStream();
            StringBuilder sb = new StringBuilder();

            setReadyState(3);

            int i;
            while ((i = is.read()) != -1) {
                sb.append((char) i);
            }
            is.close();

            this.responseText = sb.toString();

        } catch (IOException e) {
            throw new RuntimeException("IOException: " + e, e);
        }
    }

    private void setReadyState(int state) {
        this.readyState = state;
        callOnreadyStateChange();
    }

    private RunnableQueue getRunnableQueue()
    {
        JSVGCanvasApplet canvas = getJSVGCanvasApplet();
        if (canvas == null)
        {
            // He detectado este caso cuando se hace pulsa un link
            // que por ejemplo tiende a recargar la página SVG,
            // probablemente el documento sea nuevo y todavía
            // no está asociado el objeto canvas aunque me parece raro
            // o bien en el antiguo documento se han eliminado los "user data"
            // en el proceso de recarga
            return null;
        }
        return canvas.getRunnableQueue();
    }

    private void callOnreadyStateChange()
    {
        // En principio parece que no es necesario
        // ejecutar este código dentro de un AccessController.doPrivileged
        // como se sugiere en alguna de las implementaciones de XMLHttpRequest

        if (readyStateChangeFunction == null) return;

        if (this.asyncFlag && (readyState >= 2))
        {
            // En este caso estamos en un hilo nuevo (el método send ha sido llamado y el nuevo hilo ha sido creado y ejecutado)
            // por lo que el JavaScript que ejecutemos y que actualice el DOM
            // no se manifestará visualmente si no se ejecuta en el hilo
            // del update manager del JSVGCanvas
            // http://xmlgraphics.apache.org/batik/faq.html#display-does-not-update
            // http://xmlgraphics.apache.org/batik/faq.html#must-mouseover-to-change

            RunnableQueue queue = getRunnableQueue();
            if (queue == null)
            {
                callOnreadyStateChangeJSFunc();
            }
            else
            {
                try
                {
                    queue.invokeAndWait(
                            new Runnable()
                            {
                                public void run()
                                {
                                    callOnreadyStateChangeJSFunc();
                                }
                            }
                        );
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
        else
        {
            callOnreadyStateChangeJSFunc();
        }

/* ORIGINAL:
        if (readyStateChangeFunction != null) {
            Context cx = Context.enter();
            try {
                Scriptable scope = cx.initStandardObjects();
                readyStateChangeFunction.call(cx, scope, this, new Object[] {});
            } finally {
                Context.exit();
            }
        }
 */
    }

    private void callOnreadyStateChangeJSFunc()
    {
        // En principio parece que no es necesario
        // ejecutar este código dentro de un AccessController.doPrivileged
        // como se sugiere en alguna de las implementaciones de XMLHttpRequest

        getRhinoInterpreterFixed().callJSMethod(readyStateChangeFunction, this, new Object[0]);
    }
}


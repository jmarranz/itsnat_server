package org.itsnat.droid.impl.browser;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.serveritsnat.CustomFunction;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.util.UniqueIdGenerator;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;

import java.util.Iterator;
import java.util.Map;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by jmarranz on 4/06/14.
 */
public class ItsNatDroidBrowserImpl implements ItsNatDroidBrowser
{
    public final static String USER_AGENT = "Apache-HttpClient/UNAVAILABLE (java 1.4) ItsNatDroidBrowser"; // Valor por defecto de DefaultHttpClient sin parámetros en emulador 4.0.3, le añadimos ItsNatDroidBrowser
    protected ItsNatDroidImpl parent;
    protected HttpParams httpParams;
    protected HttpContext httpContext = new BasicHttpContext(); // Para las cookies (ej para las sesiones), la verdad es que no se si es multihilo pero no tengo más remedio
    protected Interpreter interp = new Interpreter(); // Global
    protected UniqueIdGenerator idGenerator = new UniqueIdGenerator();
    protected MapLight<String,ItsNatSessionImpl> sessionList = new MapLight<String, ItsNatSessionImpl>();
    protected int maxPagesInSession = 5;
    protected boolean sslSelfSignedAllowed = false; // Sólo poner a true en pruebas de desarrollo

    public ItsNatDroidBrowserImpl(ItsNatDroidImpl parent)
    {
        this.parent = parent;
        this.httpParams = getDefaultHttpParams();

        // http://stackoverflow.com/questions/3587254/how-do-i-manage-cookies-with-httpclient-in-android-and-or-java
        CookieStore cookieStore = new BasicCookieStore();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore); // httpContext tiene que ser global de otra manera las cookies no se retienen

        try
        {

            interp.set("NSAND", InflatedXML.XMLNS_ANDROID);
            // No definimos aquí set("itsNatDoc",null) o similar para poder definir métodos alert y toast
            // porque queda "itsNatDoc" como global y cualquier set() cambia valor global y por tanto ya no es local por Page
            //interp.set("itsNatDoc",new Object()); // Esto es necesario para los alert y toast que se definen luego y que usan itsNatDoc, luego cambiará con el objeto de verdad

            // Funciones de utilidad que se reflejarán en los Interpreter hijos, pero así se interpretan una sola vez
            StringBuilder code = new StringBuilder();

            code.append("import org.itsnat.droid.*;");
            code.append("import org.itsnat.droid.event.*;");
            code.append("import android.view.*;");
            code.append("import android.widget.*;");
            code.append("import " + CustomFunction.class.getName() + ";");

            code.append("arr(a){return new Object[]{a};}");
            code.append("arr(a){return new Object[]{a};}");
            code.append("arr(a,b){return new Object[]{a,b};}");
            code.append("arr(a,b,c){return new Object[]{a,b,c};}");
            code.append("arr(a,b,c,d){return new Object[]{a,b,c,d};}");

            interp.eval(code.toString());
        }
        catch (EvalError ex) { throw new ItsNatDroidException(ex); }
    }

    private static HttpParams getDefaultHttpParams()
    {
        // Parámetros copiados de los parámetros por defecto de AndroidHttpClient.newInstance(...)
        // podríamos crear un AndroidHttpClient y coger los parámetros pero el problema es que "hay que usarlo".
        HttpParams httpParams = new BasicHttpParams();

        httpParams.setParameter("http.useragent","Apache-HttpClient/UNAVAILABLE (java 1.4)"); // Emulador 4.0.3  SE CAMBIARÁ
        httpParams.setIntParameter("http.socket.timeout",60000);
        httpParams.setBooleanParameter("http.connection.stalecheck",false);
        httpParams.setIntParameter("http.connection.timeout",60000);
        httpParams.setBooleanParameter("http.protocol.handle-redirects",false);
        httpParams.setIntParameter("http.socket.buffer-size",8192);

        // AHORA cambiamos los que nos interesan para dejarlos por defecto
        httpParams.setParameter("http.useragent",USER_AGENT);  // Añadimos ItsNatDroidBrowser
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);

        return httpParams;
    }


    public ItsNatDroid getItsNatDroid()
    {
        return getItsNatDroidImpl();
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return parent;
    }

    public ItsNatSessionImpl getItsNatSession(String stdSessionId,String sessionToken,String id)
    {
        ItsNatSessionImpl session = sessionList.get(stdSessionId);
        if (session == null || !session.getToken().equals(sessionToken))
        {
            // Si el token ha cambiado es que se ha recargado el servidor, hay que tener en cuenta que los ids por ej del cliente
            // están basados en un contador en memoria
            session = new ItsNatSessionImpl(this,stdSessionId,sessionToken,id);
            sessionList.put(stdSessionId,session);
        }

        return session;
    }

    public void disposeEmptySessions()
    {
        for(Iterator<Map.Entry<String,ItsNatSessionImpl>> it = sessionList.getEntryList().iterator(); it.hasNext(); )
        {
            Map.Entry<String,ItsNatSessionImpl> entry = it.next();
            if (entry.getValue().getPageCount() == 0) it.remove();
        }
    }

    public void disposeSessionIfEmpty(ItsNatSessionImpl session)
    {
        if (session.getPageCount() == 0) sessionList.remove(session.getStandardSessionId());
    }

    public HttpParams getHttpParams()
    {
        return httpParams;
    }

    public HttpContext getHttpContext() { return httpContext; }

    public void setHttpParams(HttpParams httpParams)
    {
        this.httpParams = httpParams;
    }

    @Override
    public PageRequest createPageRequest()
    {
        return createPageRequestImpl();
    }

    public PageRequestImpl createPageRequestImpl()
    {
        return new PageRequestImpl(this);
    }

    public UniqueIdGenerator getUniqueIdGenerator()
    {
        return idGenerator;
    }

    public Interpreter getInterpreter()
    {
        return interp;
    }

    public int getMaxPagesInSession()
    {
        return maxPagesInSession;
    }

    public void setMaxPagesInSession(int maxPagesInSession)
    {
        this.maxPagesInSession = maxPagesInSession;
    }

    public boolean isSSLSelfSignedAllowed()
    {
        return sslSelfSignedAllowed;
    }

    public void setSSLSelfSignedAllowed(boolean enable)
    {
        this.sslSelfSignedAllowed = enable;
    }
}

package org.itsnat.droid.impl.browser;

import android.content.Context;
import android.view.View;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatSession;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.UserData;
import org.itsnat.droid.event.Event;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.util.UserDataImpl;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;

/**
 * Created by jmarranz on 4/06/14.
 */
public class PageImpl implements Page
{
    protected PageRequestImpl pageRequest; // Nos interesa únicamente para el reload, es un clone del original por lo que podemos tomar datos del mismo sin miedo a cambiarse
    protected InflatedLayoutPageImpl inflated;
    protected String loadedContent;
    protected String uniqueIdForInterpreter;
    protected Interpreter interp;
    protected ItsNatDocImpl itsNatDoc = new ItsNatDocImpl(this);
    protected ItsNatSessionImpl itsNatSession;
    protected String clientId;
    protected OnEventErrorListener eventErrorListener;
    protected OnServerStateLostListener stateLostListener;
    protected UserDataImpl userData;
    protected HttpParams httpParams;
    protected boolean enableEvtMonitors = true;
    protected List<EventMonitor> evtMonitorList;
    protected boolean dispose;

    public PageImpl(PageRequestImpl pageRequest,HttpParams httpParams,String loadedContent,AttrCustomInflaterListener inflateListener)
    {
        this.httpParams = httpParams;
        this.loadedContent = loadedContent;
        this.pageRequest = pageRequest;

        StringReader input = new StringReader(loadedContent);

        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();
        InflateRequestImpl inflateRequest = new InflateRequestImpl(browser.getItsNatDroidImpl());
        inflateRequest.setContext(pageRequest.getContext());
        if (inflateListener != null) inflateRequest.setAttrCustomInflaterListener(inflateListener);


        String[] loadScriptArr = new String[1];
        List<String> scriptList = new LinkedList<String>();
        this.inflated = (InflatedLayoutPageImpl)inflateRequest.inflateInternal(input, loadScriptArr,scriptList,this);

        String loadScript = loadScriptArr[0];

        this.uniqueIdForInterpreter = browser.getUniqueIdGenerator().generateId("i"); // i = interpreter
        this.interp = new Interpreter(new StringReader(""), System.out, System.err, false, new NameSpace(browser.getInterpreter().getNameSpace(), uniqueIdForInterpreter) ); // El StringReader está copiado del código fuente de beanshell2 https://code.google.com/p/beanshell2/source/browse/branches/v2.1/src/bsh/Interpreter.java
//long start = System.currentTimeMillis();
        try
        {
            interp.set("itsNatDoc",itsNatDoc);

            StringBuilder methods = new StringBuilder();
            methods.append("alert(data){itsNatDoc.alert(data);}");
            methods.append("toast(value,duration){itsNatDoc.toast(value,duration);}");
            methods.append("toast(value){itsNatDoc.toast(value);}");
            methods.append("eval(code){itsNatDoc.eval(code);}");
            interp.eval(methods.toString());

            if (!scriptList.isEmpty())
            {
                for(String code : scriptList)
                {
                    interp.eval(code);
                }
            }

            if (loadScript != null) // El caso null es cuando se devuelve un layout sin script (layout sin eventos)
                interp.eval(loadScript);
        }
        catch (EvalError ex) { throw new ItsNatDroidScriptException(ex,loadScript); }
        catch (Exception ex) { throw new ItsNatDroidScriptException(ex,loadScript); }

//long end = System.currentTimeMillis();
//System.out.println("LAPSE" + (end - start));

        if (getId() != null)
            getItsNatDocImpl().sendLoadEvent();
        else
            dispose(); // En el servidor
    }

    public ItsNatDroidBrowserImpl getItsNatDroidBrowserImpl()
    {
        return pageRequest.getItsNatDroidBrowserImpl();
    }

    public PageRequestImpl getPageRequestImpl()
    {
        return pageRequest;
    }

    @Override
    public ItsNatDroidBrowser getItsNatDroidBrowser()
    {
        return getItsNatDroidBrowserImpl();
    }

    @Override
    public View getRootView()
    {
        return getInflatedLayoutPageImpl().getRootView();
    }

    @Override
    public View findViewByXMLId(String id)
    {
        return getInflatedLayoutPageImpl().findViewByXMLId(id);
    }

    public HttpParams getHttpParams()
    {
        return httpParams;
    }

    @Override
    public String getURL()
    {
        return pageRequest.getURL();
    }

    public InflatedLayoutPageImpl getInflatedLayoutPageImpl()
    {
        return inflated;
    }

    @Override
    public String getLoadedContent()
    {
        return loadedContent;
    }

    public Interpreter getInterpreter()
    {
        return interp;
    }

    public void setSessionIdAndClientId(String stdSessionId,String sessionToken,String sessionId,String clientId)
    {
        this.clientId = clientId;
        this.itsNatSession = getItsNatDroidBrowserImpl().getItsNatSession(stdSessionId,sessionToken,sessionId);
        itsNatSession.registerPage(this);
    }

    public ItsNatSession getItsNatSession()
    {
        return itsNatSession;
    }

    public ItsNatSessionImpl getItsNatSessionImpl()
    {
        return itsNatSession;
    }

    public String getId()
    {
        return clientId;
    }

    public Context getContext()
    {
        return getInflatedLayoutPageImpl().getContext();
    }

    @Override
    public ItsNatView getItsNatView(View view)
    {
        return getItsNatViewImpl(view);
    }

    public ItsNatViewImpl getItsNatViewImpl(View view)
    {
        return ItsNatViewImpl.getItsNatView(this,view);
    }

    public UserData getUserData()
    {
        if (userData == null) this.userData = new UserDataImpl();
        return userData;
    }

    public ItsNatDoc getItsNatDoc()
    {
        return getItsNatDocImpl();
    }

    public ItsNatDocImpl getItsNatDocImpl()
    {
        return itsNatDoc;
    }

    public OnEventErrorListener getOnEventErrorListener()
    {
        return eventErrorListener;
    }

    @Override
    public void setOnEventErrorListener(OnEventErrorListener listener)
    {
        this.eventErrorListener = listener;
    }

    public OnServerStateLostListener getOnServerStateLostListener()
    {
        return stateLostListener;
    }

    public void setOnServerStateLostListener(OnServerStateLostListener listener)
    {
        this.stateLostListener = listener;
    }

    public void setEnableEventMonitors(boolean value) { this.enableEvtMonitors = value; }

    public void addEventMonitor(EventMonitor monitor)
    {
        if (evtMonitorList == null) this.evtMonitorList = new LinkedList<EventMonitor>();
        evtMonitorList.add(monitor);
    }

    public boolean removeEventMonitor(EventMonitor monitor)
    {
        if (evtMonitorList == null) return false;
        return evtMonitorList.remove(monitor);
    }

    public void fireEventMonitors(boolean before,boolean timeout,Event evt)
    {
        if (!this.enableEvtMonitors) return;

        if (evtMonitorList == null) return;

        for(EventMonitor curr : evtMonitorList)
        {
            if (before) curr.before(evt);
            else curr.after(evt,timeout);
        }
    }

    public PageRequest reusePageRequest()
    {
        return pageRequest.clone();
    }

    public void dispose()
    {
        if (dispose) return;
        this.dispose = true;
        if (getId() != null)
            itsNatDoc.sendUnloadEvent();
        if (itsNatSession != null) // itsNatSession es null cuando la página no contiene script de inicialización
        {
            itsNatSession.disposePage(this);
            getItsNatDroidBrowserImpl().disposeSessionIfEmpty(itsNatSession);
        }
    }

    public void executeScriptList(List<String> scriptList)
    {
        if (scriptList.isEmpty()) return;

        Interpreter interp = getInterpreter();
        for (String code : scriptList)
        {
            try
            {
                interp.eval(code);
            }
            catch (EvalError ex)
            {
                throw new ItsNatDroidScriptException(ex, code);
            }
            catch (Exception ex)
            {
                throw new ItsNatDroidScriptException(ex, code);
            }
        }

    }
}

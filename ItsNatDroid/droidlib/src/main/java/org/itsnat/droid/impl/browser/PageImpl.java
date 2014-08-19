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
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

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
    protected InflatedLayoutImpl inflated;
    protected String content;
    protected String uniqueId;
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

    public PageImpl(PageRequestImpl pageRequest,HttpParams httpParams,String content,AttrCustomInflaterListener inflateListener)
    {
        this.httpParams = httpParams;
        this.content = content;
        this.pageRequest = pageRequest;

        StringReader input = new StringReader(content);

        ItsNatDroidBrowserImpl browser = pageRequest.getItsNatDroidBrowserImpl();
        InflateRequestImpl inflateRequest = new InflateRequestImpl(browser.getItsNatDroidImpl());
        inflateRequest.setContext(pageRequest.getContext());
        if (inflateListener != null) inflateRequest.setAttrCustomInflaterListener(inflateListener);


        String[] loadScriptArr = new String[1];
        List<String> scriptList = new LinkedList<String>();
        InflatedLayoutImpl inflated = inflateRequest.inflateInternal(input, loadScriptArr,scriptList,this);
        this.inflated = inflated;

        String loadScript = loadScriptArr[0];

        this.uniqueId = browser.getUniqueIdGenerator().generateId("c"); // c = client (page)
        this.interp = new Interpreter(new StringReader(""), System.out, System.err, false, new NameSpace(browser.getInterpreter().getNameSpace(),uniqueId) ); // El StringReader está copiado del código fuente de beanshell2 https://code.google.com/p/beanshell2/source/browse/branches/v2.1/src/bsh/Interpreter.java
//long start = System.currentTimeMillis();
        try
        {
            interp.set("itsNatDoc",itsNatDoc);

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
    }

    public ItsNatDroidBrowserImpl getItsNatDroidBrowserImpl()
    {
        return pageRequest.getItsNatDroidBrowserImpl();
    }

    @Override
    public ItsNatDroidBrowser getItsNatDroidBrowser()
    {
        return getItsNatDroidBrowserImpl();
    }

    @Override
    public View getRootView()
    {
        return getInflatedLayoutImpl().getRootView();
    }

    @Override
    public View findViewByXMLId(String id)
    {
        return getInflatedLayoutImpl().findViewByXMLId(id);
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

    public InflatedLayoutImpl getInflatedLayoutImpl()
    {
        return inflated;
    }

    @Override
    public String getContent()
    {
        return content;
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
        return getInflatedLayoutImpl().getContext();
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
        itsNatDoc.sendUnloadEvent();
        itsNatSession.disposePage(this);
        getItsNatDroidBrowserImpl().disposeSessionIfEmpty(itsNatSession);
    }

    public void insertFragment(View parentView,String markup)
    {
        String[] loadScript = new String[1]; // Necesario pasar pero no se usa, no es tiempo de carga
        List<String> scriptList = new LinkedList<String>();

        getInflatedLayoutImpl().insertFragment(parentView,markup,loadScript,scriptList,this);

        if (!scriptList.isEmpty())
        {
            Interpreter interp = getInterpreter();
            for(String code : scriptList)
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
}

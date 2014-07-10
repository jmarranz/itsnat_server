package org.itsnat.droid.impl.browser;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatSession;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.UserData;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.util.UserDataImpl;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;

import java.io.StringReader;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;

/**
 * Created by jmarranz on 4/06/14.
 */
public class PageImpl implements Page
{
    protected ItsNatDroidBrowserImpl browser;
    protected String url;
    protected InflatedLayoutImpl inflated;
    protected byte[] content;
    protected String uniqueId;
    protected Interpreter interp;
    protected ItsNatDocImpl itsNatDoc = new ItsNatDocImpl(this);
    protected ItsNatSessionImpl itsNatSession;
    protected String clientId;
    protected OnServerStateLostListener listener;
    protected UserDataImpl userData;


    public PageImpl(ItsNatDroidBrowserImpl browser,String url,InflatedLayoutImpl inflated,byte[] content,String loadScript)
    {
        this.browser = browser;
        this.url = url;
        this.inflated = inflated;
        this.content = content;
        this.uniqueId = browser.getUniqueIdGenerator().generateId("c"); // c = client (page)
        this.interp = new Interpreter(new StringReader(""), System.out, System.err, false, new NameSpace(browser.getInterpreter().getNameSpace(),uniqueId) ); // El StringReader está copiado del código fuente de beanshell2 https://code.google.com/p/beanshell2/source/browse/branches/v2.1/src/bsh/Interpreter.java
//long start = System.currentTimeMillis();
        try
        {
            interp.set("itsNatDoc",itsNatDoc);
            interp.eval(loadScript);
        }
        catch (EvalError ex) { throw new ItsNatDroidScriptException(ex,loadScript); }

//long end = System.currentTimeMillis();
//System.out.println("LAPSE" + (end - start));
    }

    public ItsNatDroidBrowserImpl getItsNatDroidBrowserImpl()
    {
        return browser;
    }

    @Override
    public String getURL()
    {
        return url;
    }

    @Override
    public InflatedLayout getInflatedLayout()
    {
        return getInflatedLayoutImpl();
    }

    public InflatedLayoutImpl getInflatedLayoutImpl()
    {
        return inflated;
    }

    @Override
    public byte[] getContent()
    {
        return content;
    }

    public void setSessionIdAndClientId(String stdSessionId,String sessionToken,String sessionId,String clientId)
    {
        this.clientId = clientId;
        this.itsNatSession = browser.getItsNatSession(stdSessionId,sessionToken,sessionId);
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
        return ItsNatViewImpl.getItsNatView(this,view);
    }

    public UserData getUserData()
    {
        if (userData == null) this.userData = new UserDataImpl();
        return userData;
    }

    public ItsNatDoc getItsNatDoc()
    {
        return itsNatDoc;
    }

    public OnServerStateLostListener getOnServerStateLostListener()
    {
        return listener;
    }

    public void setOnServerStateLostListener(OnServerStateLostListener listener)
    {
        this.listener = listener;
    }

    public void dispose()
    {
        itsNatSession.disposePage(this);
        browser.disposeSessionIfEmpty(itsNatSession);
    }
}

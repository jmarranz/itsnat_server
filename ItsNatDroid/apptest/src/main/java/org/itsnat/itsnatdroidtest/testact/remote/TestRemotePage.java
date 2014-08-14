package org.itsnat.itsnatdroidtest.testact.remote;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.event.Event;
import org.itsnat.droid.event.NormalEvent;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import bsh.EvalError;

/**
 * Created by jmarranz on 13/08/14.
 */
public abstract class TestRemotePage implements OnPageLoadListener,OnPageLoadErrorListener,OnEventErrorListener
{
    protected final TestActivityTabFragment fragment;
    protected final ItsNatDroidBrowser droidBrowser;

    public TestRemotePage(final TestActivityTabFragment fragment, final ItsNatDroidBrowser droidBrowser)
    {
        this.fragment = fragment;
        this.droidBrowser = droidBrowser;
    }

    protected TestActivity getTestActivity()
    {
        return fragment.getTestActivity();
    }

    protected HttpParams prepareLoad()
    {
        TestActivity act = getTestActivity();
        Toast.makeText(act, "DOWNLOADING", Toast.LENGTH_SHORT).show();

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 4000);

        return httpParams;
    }

    protected void bindBackAndReloadButton(final Page page,View rootView)
    {
        View backButton = rootView.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragment.gotoLayoutIndex();
            }
        });

        View buttonReload = rootView.findViewById(R.id.buttonReload);
        if (buttonReload == null) throw new RuntimeException("FAIL");
        if (buttonReload != page.findViewByXMLId("buttonReload")) throw new RuntimeException("FAIL");

        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TestActivity act = getTestActivity();
                Toast.makeText(act, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                page.dispose();
                page.reusePageRequest().execute();
                //downloadLayoutRemote(act,droidBrowser);
            }
        });
    }

    @Override
    public void onPageLoad(final Page page)
    {
        if (page.getItsNatSession().getPageCount() > droidBrowser.getMaxPagesInSession())
            throw new RuntimeException("FAIL");

        final TestActivity act = getTestActivity();

        Log.v("TestActivity", "CONTENT:" + new String(page.getContent()));

        boolean showContentInAlert = false;
        if (showContentInAlert)
        {
            TestUtil.alertDialog(act, "LAYOUT", new String(page.getContent()));
        }

        View rootView = page.getRootView();
        changeLayout(rootView);
        Toast.makeText(act, "OK LAYOUT REMOTE", Toast.LENGTH_SHORT).show();


        bindBackAndReloadButton(page, rootView);

        page.setOnEventErrorListener(this);

        page.setOnServerStateLostListener(new OnServerStateLostListener()
        {
            @Override
            public void onServerStateLost(Page page)
            {
                TestUtil.alertDialog(act, "User Msg: SERVER STATE LOST!!");
            }
        });
    }

    @Override
    public void onError(Exception ex,PageRequest pageRequest)
    {
        TestActivity act = getTestActivity();
        Toast.makeText(act, "ERROR:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        //throw new RuntimeException(ex);
        ex.printStackTrace();
        if (ex instanceof ItsNatDroidScriptException)
        {
            ItsNatDroidScriptException exScr = (ItsNatDroidScriptException) ex;
            if (exScr.getCause() instanceof EvalError)
            {
                ((EvalError) exScr.getCause()).printStackTrace();
            }
            Log.v("TestActivity", "CODE:" + exScr.getScript());
        }

    }

    @Override
    public void onError(Exception ex, Event evt)
    {
        TestActivity act = getTestActivity();

        ex.printStackTrace();
        StringBuilder msg = new StringBuilder();
        msg.append("User Msg: Event processing error");
        if (evt instanceof NormalEvent)
            msg.append("\ntype: \" + ((NormalEvent) evt).getType()");
        msg.append("\nException Msg: " + ex.getMessage());

        TestUtil.alertDialog(act,msg.toString());
        if (ex instanceof ItsNatDroidServerResponseException)
            TestUtil.alertDialog(act, "User Msg: Server content returned error: " + ((ItsNatDroidServerResponseException) ex).getContent());
        else if (ex instanceof ItsNatDroidScriptException)
        {
            ItsNatDroidScriptException exScr = (ItsNatDroidScriptException) ex;
            if (exScr.getCause() instanceof EvalError)
            {
                ((EvalError) exScr.getCause()).printStackTrace();
            }
            Log.v("TestActivity", "CODE:" + exScr.getScript());
        }

    }

    protected void changeLayout(View rootView)
    {
        fragment.setRootView(rootView);
        fragment.updateFragmentLayout();
    }
}

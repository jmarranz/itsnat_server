package org.itsnat.itsnatdroidtest;

import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.Event;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;

import bsh.EvalError;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutRemote
{
    public static void test(MainActivity act)
    {
        ItsNatDroidBrowser droidBrowser = ItsNatDroidRoot.get().createItsNatDroidBrowser();
        downloadLayoutRemote(act,droidBrowser);
    }

    private static void downloadLayoutRemote(final MainActivity act,final ItsNatDroidBrowser droidBrowser)
    {
        Toast.makeText(act, "DOWNLOADING", Toast.LENGTH_SHORT).show();
        String url = "http://192.168.0.215:8080/itsnat_dev/ItsNatDroidServletExample?itsnat_doc_name=test_droid";

        boolean testSSLSelfSignedAllowed = false;
        if (testSSLSelfSignedAllowed)
        {
            droidBrowser.setSSLSelfSignedAllowed(true);
            url = "https://www.pcwebshop.co.uk"; // Alternativa: https://mms.nw.ru
        }

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

        boolean testSyncRequests = false;
        if (testSyncRequests)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(act)
                .setSynchronous(testSyncRequests)
                .setOnPageLoadListener(new OnPageLoadListener()
                {
                    @Override
                    public void onPageLoad(final Page page)
                    {

                        Log.v("MainActivity", "CONTENT:" + new String(page.getContent()));

                        boolean showContentInAlert = false;
                        if (showContentInAlert)
                        {
                            TestUtil.alertDialog(act, "XML", new String(page.getContent()));
                        }

                        View layout = page.getRootView();
                        act.setContentView(layout);
                        Toast.makeText(act, "OK XML REMOTE", Toast.LENGTH_SHORT).show();

                        View backButton = act.findViewById(R.id.back);
                        backButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                act.setMainLayout();
                            }
                        });

                        View button3 = act.findViewById(R.id.buttonReload);
                        if (button3 == null) throw new RuntimeException("FAIL");

                        View frameLayoutViewInner = page.findViewByXMLId("frameLayoutViewInner");
                        ItsNatView frameLayoutViewInnerItsNat = page.getItsNatView(frameLayoutViewInner);
                        frameLayoutViewInnerItsNat.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Toast.makeText(act, "Click Native OK", Toast.LENGTH_SHORT).show();
                            }
                        });
                        frameLayoutViewInnerItsNat.setOnTouchListener(new View.OnTouchListener()
                        {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent)
                            {
                                Toast.makeText(act, "Touch Native OK " + motionEvent.getAction(), Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

                        button3.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Toast.makeText(act, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                                page.requestReload().execute();
                                //downloadLayoutRemote(act,droidBrowser);
                            }
                        });

                        if (page.getItsNatSession().getPageCount() > droidBrowser.getMaxPagesInSession())
                            throw new RuntimeException("FAIL");

                        page.setOnEventErrorListener(new OnEventErrorListener()
                        {
                            @Override
                            public void onError(Exception ex, Event evt)
                            {
                                ex.printStackTrace();
                                TestUtil.alertDialog(act, "User Msg: Event processing error,type: " + evt.getType());
                                if (ex instanceof ItsNatDroidServerResponseException)
                                    TestUtil.alertDialog(act, "User Msg: Server content returned error: " + ((ItsNatDroidServerResponseException) ex).getContent());
                                else if (ex instanceof ItsNatDroidScriptException)
                                {
                                    ItsNatDroidScriptException exScr = (ItsNatDroidScriptException) ex;
                                    if (exScr.getCause() instanceof EvalError)
                                    {
                                        ((EvalError) exScr.getCause()).printStackTrace();
                                    }
                                    Log.v("MainActivity", "CODE:" + exScr.getScript());
                                }

                            }
                        });

                        page.setOnServerStateLostListener(new OnServerStateLostListener()
                        {
                            @Override
                            public void onServerStateLost(Page page)
                            {
                                TestUtil.alertDialog(act, "User Msg: SERVER STATE LOST!!");
                            }
                        });

                        page.addEventMonitor(new EventMonitor()
                        {
                            @Override
                            public void before(Event event)
                            {
                                Log.v("MainActivity", "Evt Monitor: before");
                            }

                            @Override
                            public void after(Event event, boolean timeout)
                            {
                                Log.v("MainActivity", "Evt Monitor: after, timeout: " + timeout);
                            }
                        });


                        //page.dispose();

                    }
                }).setOnPageLoadErrorListener(new OnPageLoadErrorListener()
        {
            @Override
            public void onError(Exception ex,PageRequest pageRequest)
            {
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
                    Log.v("MainActivity", "CODE:" + exScr.getScript());
                }

            }
        }).setAttrCustomInflaterListener(new AttrCustomInflaterListener()
        {
            @Override
            public void setAttribute(View view, String namespace, String name, String value)
            {
                System.out.println("NOT FOUND ATTRIBUTE: " + namespace + " " + name + " " + value);
            }

            @Override
            public void removeAttribute(View view, String namespace, String name)
            {
                System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
            }
        }).setHttpParams(httpParams)
          .setURL(url)
          .execute();
    }
}

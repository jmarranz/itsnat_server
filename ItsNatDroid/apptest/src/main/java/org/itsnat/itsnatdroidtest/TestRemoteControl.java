package org.itsnat.itsnatdroidtest;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
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
import org.itsnat.droid.event.Event;
import org.itsnat.droid.event.NormalEvent;
import org.itsnat.droid.impl.browser.PageImpl;

import bsh.EvalError;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestRemoteControl
{
    public static void test(MainActivity act,String url)
    {
        ItsNatDroidBrowser droidBrowser = ItsNatDroidRoot.get().createItsNatDroidBrowser();
        downloadLayoutRemote(act,droidBrowser,url);
    }

    private static void downloadLayoutRemote(final MainActivity act,final ItsNatDroidBrowser droidBrowser,String url)
    {
        Toast.makeText(act, "DOWNLOADING", Toast.LENGTH_SHORT).show();


        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 4000);


        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(act)
                .setOnPageLoadListener(new OnPageLoadListener()
                {
                    @Override
                    public void onPageLoad(final Page page)
                    {

                        Log.v("MainActivity", "CONTENT:" + new String(page.getContent()));

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

                        View buttonReload = act.findViewById(R.id.buttonReload);

                        buttonReload.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Toast.makeText(act, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                                page.reusePageRequest().execute();
                            }
                        });


                        page.setOnEventErrorListener(new OnEventErrorListener()
                        {
                            @Override
                            public void onError(Exception ex, Event evt)
                            {
                                ex.printStackTrace();
                                TestUtil.alertDialog(act, "User Msg: Event processing error, type: " + ((NormalEvent) evt).getType() + "\nException Msg: " + ex.getMessage());
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
            public void setAttribute(final Page page,View view, String namespace, String name, final String value)
            {
                if (name.equals("url"))
                {
                    ItsNatView itsNatView = page.getItsNatView(view);
                    itsNatView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            page.reusePageRequest().execute();
                        }
                    });
                }
                else System.out.println("NOT FOUND ATTRIBUTE: " + namespace + " " + name + " " + value);
            }

            @Override
            public void removeAttribute(Page page,View view, String namespace, String name)
            {
                System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
            }
        }).setHttpParams(httpParams)
          .setURL(url)
          .execute();
    }
}

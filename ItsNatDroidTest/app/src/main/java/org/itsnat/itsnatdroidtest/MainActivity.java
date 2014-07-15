package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.Event;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnLoadErrorListener;
import org.itsnat.droid.OnPageListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.impl.util.ValueUtil;


import java.io.InputStream;

import bsh.EvalError;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View compiledView = getLayoutInflater().inflate(R.layout.activity_main,null);
        setContentView(compiledView); // No pasamos directamente el id porque necesitamos la View para testear
        Toast.makeText(this, "OK COMPILED", Toast.LENGTH_SHORT).show();

//WeakMapWithValue.test(null);


        final View button = findViewById(R.id.buttonTest);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InputStream input = getResources().openRawResource(R.raw.activity_main_test);
                // Sólo para testear carga local
                InflateRequest inflateRequest = ItsNatDroidRoot.get().createInflateRequest();
                InflatedLayout layout = inflateRequest.setAttrCustomInflaterListener(new AttrCustomInflaterListener()
                {
                    @Override
                    public void setAttribute(View view,String namespace,String name, String value)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
                    }

                    @Override
                    public void removeAttribute(View view, String namespace, String name)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
                    }
                }).setContext(MainActivity.this)
                  .inflate(input);

                View rootView = layout.getRootView();
                setContentView(rootView);
                Toast.makeText(MainActivity.this, "OK XML LOCAL", Toast.LENGTH_SHORT).show();
                View button2 = findViewById(R.id.buttonTest);
                if (button2 == null)
                    throw new RuntimeException("FAIL");

                if (layout.findViewByXMLId("buttonTest") != button2) throw new RuntimeException("FAIL");

                button2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ItsNatDroidBrowser droidBrowser = ItsNatDroidRoot.get().createItsNatDroidBrowser();
                        HttpConnectionParams.setConnectionTimeout(droidBrowser.getHttpParams(), 3000);
                        downloadLayoutRemote(droidBrowser);
                    }
                });

                TestXMLInflate.test((RelativeLayout)compiledView,(RelativeLayout)rootView);
            }
        });

    }

    private void downloadLayoutRemote(final ItsNatDroidBrowser droidBrowser)
    {
        Toast.makeText(MainActivity.this, "DOWNLOADING", Toast.LENGTH_SHORT).show();
        String url = "http://192.168.0.215:8080/itsnat_dev/ItsNatDroidServletExample?itsnat_doc_name=test_droid";

        boolean testSSLSelfSignedAllowed = false;
        if (testSSLSelfSignedAllowed)
        {
            droidBrowser.setSSLSelfSignedAllowed(true);
            url = "https://www.pcwebshop.co.uk"; // Alternativa: https://mms.nw.ru
        }
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(this).setOnPageListener(new OnPageListener()
        {
            @Override
            public void onPage(final Page page)
            {

                Log.v("MainActivity", "CONTENT:" + new String(page.getContent()));

                boolean showContentInAlert = false;
                if (showContentInAlert)
                {
                    TestUtil.alertDialog(MainActivity.this,"XML",new String(page.getContent()));
                }

                View layout = page.getInflatedLayout().getRootView();
                setContentView(layout);
                Toast.makeText(MainActivity.this, "OK XML REMOTE", Toast.LENGTH_SHORT).show();
                View button3 = findViewById(R.id.buttonTest);
                if (button3 == null) throw new RuntimeException("FAIL");

                View frameLayoutViewInner = page.getInflatedLayout().findViewByXMLId("frameLayoutViewInner");
                ItsNatView frameLayoutViewInnerItsNat = page.getItsNatView(frameLayoutViewInner);
                frameLayoutViewInnerItsNat.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(MainActivity.this, "Click Native OK", Toast.LENGTH_SHORT).show();
                    }
                });
                frameLayoutViewInnerItsNat.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                    {
                        Toast.makeText(MainActivity.this, "Touch Native OK " + motionEvent.getAction(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                button3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(MainActivity.this, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                        downloadLayoutRemote(droidBrowser);
                    }
                });

                if (page.getItsNatSession().getPageCount() > droidBrowser.getMaxPagesInSession())
                    throw new RuntimeException("FAIL");

                page.setOnEventErrorListener(new OnEventErrorListener()
                {
                    @Override
                    public void onError(Exception ex, String type, InputEvent evt)
                    {
                        ex.printStackTrace();
                        TestUtil.alertDialog(MainActivity.this,"User Msg: Event processing error,type: " + type);
                        if (ex instanceof ItsNatDroidServerResponseException)
                            TestUtil.alertDialog(MainActivity.this,"User Msg: Server content returned error: " + ((ItsNatDroidServerResponseException)ex).getContent());
                    }
                });

                page.setOnServerStateLostListener(new OnServerStateLostListener()
                {
                    @Override
                    public void onServerStateLost(Page page)
                    {
                        TestUtil.alertDialog(MainActivity.this,"User Msg: SERVER STATE LOST!!");
                    }
                });

                page.addEventMonitor(new EventMonitor()
                {
                    @Override
                    public void before(Event event)
                    {
                        Log.v("MainActivity","Evt Monitor: before");
                    }

                    @Override
                    public void after(Event event, boolean timeout)
                    {
                        Log.v("MainActivity","Evt Monitor: after, timeout: " + timeout);
                    }
                });


                //page.dispose();

            }
        }).setOnLoadErrorListener(new OnLoadErrorListener()
        {
            @Override
            public void onError(Exception ex)
            {
                Toast.makeText(MainActivity.this, "ERROR:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
          .execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

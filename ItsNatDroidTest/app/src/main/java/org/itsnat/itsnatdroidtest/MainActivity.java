package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.OnErrorListener;
import org.itsnat.droid.OnPageListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;


import java.io.InputStream;
import java.io.StringReader;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;


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

        Interpreter i=new Interpreter();
        try
        {
            i.set("prueba","HOOOLA");
            i.eval("String prueba = \"ADIOS\";");
            StringBuilder code = new StringBuilder();
            code.append("arr(a){return new Object[]{a};}");
            code.append("arr(a,b){return new Object[]{a,b};}");
            code.append("arr(a,b,c){return new Object[]{a,b,c};}");
            code.append("arr(a,b,c,d){return new Object[]{a,b,c,d};}");
            i.eval(code.toString());
            //Toast.makeText(MainActivity.this, "BEAN SHELL " + i.get("prueba"), Toast.LENGTH_LONG).show();

            Interpreter i2 = new Interpreter(new StringReader(""), System.out, System.err, false, new NameSpace(i.getNameSpace(),"2") );

            i2.set("prueba2","HOOOLA");
            i2.eval("String prueba2 = \"ADIOS\";");
            i2.eval("System.out.println(\"PRUEBA: \" + arr(1,2)[0] + \" \" + arr(1,2)[1] + \" \" + arr(1,2).length);");
            //Toast.makeText(MainActivity.this, "BEAN SHELL " + i.get("prueba2") + " " + i2.get("prueba") + " " + i2.get("prueba2"), Toast.LENGTH_LONG).show();
        }
        catch (EvalError evalError)
        {
            evalError.printStackTrace();
        }
    }

    private void downloadLayoutRemote(final ItsNatDroidBrowser droidBrowser)
    {
        Toast.makeText(MainActivity.this, "DOWNLOADING", Toast.LENGTH_SHORT).show();
        String url = "http://192.168.0.215:8080/itsnat_dev/ItsNatServletExample?itsnat_doc_name=test_droid";

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(this).setOnPageListener(new OnPageListener()
        {
            @Override
            public void onPage(final Page page)
            {
                new AlertDialog.Builder(MainActivity.this).setTitle("XML").setMessage(new String(page.getContent())).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // continue with delete
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();


                View layout = page.getInflatedLayout().getRootView();
                setContentView(layout);
                Toast.makeText(MainActivity.this, "OK XML REMOTE", Toast.LENGTH_SHORT).show();
                View button3 = findViewById(R.id.buttonTest);
                if (button3 == null) throw new RuntimeException("FAIL");

                page.dispose();

                button3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Toast.makeText(MainActivity.this, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                        downloadLayoutRemote(droidBrowser);
                    }
                });
            }
        }).setOnErrorListener(new OnErrorListener()
        {
            @Override
            public void onError(Exception ex)
            {
                Toast.makeText(MainActivity.this, "ERROR:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //throw new RuntimeException(ex);
                ex.printStackTrace();
                if (ex instanceof ItsNatDroidScriptException) Log.v("MainActivity", "CODE:" + ((ItsNatDroidScriptException)ex).getScript());

            }
        }).setAttrCustomInflaterListener(new AttrCustomInflaterListener()
        {
            @Override
            public void setAttribute(View view,String namespace, String name, String value)
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

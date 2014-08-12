package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.itsnatdroidtest.local.TestLayoutLocal;
import org.itsnat.itsnatdroidtest.remote.TestRemoteControl;
import org.itsnat.itsnatdroidtest.remote.TestRemoteCore;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setMainLayout();

        String urlTestCore = loadURLTestCore();
        String urlTestRemCtrl = loadURLTestRemCtrl();

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("urlTestCore",urlTestCore);
        intent.putExtra("urlTestRemCtrl",urlTestRemCtrl);
        startActivity(intent);
    }

    public void setMainLayout()
    {
        setContentView(R.layout.activity_main);

        final EditText urlTestCore = (EditText)findViewById(R.id.urlTestCore);
        urlTestCore.setText(loadURLTestCore());

        final EditText urlTestRemCtrl = (EditText)findViewById(R.id.urlTestRemoteControl);
        urlTestRemCtrl.setText(loadURLTestRemCtrl());

        View testLocal = findViewById(R.id.testLocal);
        testLocal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveURL(urlTestCore.getText().toString(),urlTestRemCtrl.getText().toString());
                //TestLayoutLocal.test(MainActivity.this);
            }
        });

        final ItsNatDroidBrowser droidBrowser = ItsNatDroidRoot.get().createItsNatDroidBrowser();

        View testRemoteCore = findViewById(R.id.testRemoteCore);
        testRemoteCore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveURL(urlTestCore.getText().toString(), urlTestRemCtrl.getText().toString());
                String url = urlTestCore.getText().toString();
                TestRemoteCore.test(MainActivity.this, droidBrowser, url);
            }
        });

        View testRemoteControl = findViewById(R.id.testRemoteControl);
        testRemoteControl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveURL(urlTestCore.getText().toString(), urlTestRemCtrl.getText().toString());
                String url = urlTestRemCtrl.getText().toString();
                TestRemoteControl.test(MainActivity.this, droidBrowser, url);
            }
        });


        // Pruebas temporales

/*
        EditText editText = (EditText)findViewById(R.id.editTextPrueba);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
                // Log.v("MainActivity", "beforeTextChanged " + charSequence + " " + start + " " + count + " " + after);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                Log.v("MainActivity", "onTextChanged " + charSequence + " " + start + " " + before + " " + count);
                CharSequence newText = count > 0 ? charSequence.subSequence(start, start + count) : "";
                    Log.v("MainActivity", "------------- " + newText);
                if (before < count)
                {
                    CharSequence changedText = count > 0 ? charSequence.subSequence(start + before, start + count) : "";
                    Log.v("MainActivity", "ADDED-------- " + changedText);
                }
                else if (before == count)
                {
                    Log.v("MainActivity", "------------- NO CHANGE");
                }
                else if (before > count)
                {
                    Log.v("MainActivity", "REMOVED----- ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                Log.v("MainActivity", "afterTextChanged " + editable);
            }
        });

        editText.setText("PRUEBA");
*/

    }

    private String loadURLTestCore()
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String url = settings.getString("remoteUrlTestCore", "http://192.168.0.215:8080/itsnat_dev/ItsNatDroidServletExample?itsnat_doc_name=test_droid_core");
        return url;
    }

    private String loadURLTestRemCtrl()
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String url = settings.getString("remoteUrlTestRemCtrl", "http://192.168.0.215:8080/itsnat_dev/ItsNatDroidServletExample?itsnat_doc_name=test_droid_remote_ctrl");
        return url;
    }

    private void saveURL(String remoteUrlTestCore,String remoteUrlTestRemCtrl)
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("remoteUrlTestCore", remoteUrlTestCore);
        editor.putString("remoteUrlTestRemCtrl", remoteUrlTestRemCtrl);
        editor.commit();
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

package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.itsnat.itsnatdroidtest.testact.TestActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setMainLayout();

        startTestActivity();
    }

    public void startTestActivity()
    {
        String urlTestBase = loadURLBase();

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("urlTestBase",urlTestBase);
        startActivity(intent);
    }

    public void setMainLayout()
    {
        setContentView(R.layout.activity_main);


        View buttonGotoTests = findViewById(R.id.gotoTests);
        buttonGotoTests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startTestActivity();           }
        });

        final EditText urlTestBase = (EditText)findViewById(R.id.urlBase);
        urlTestBase.setText(loadURLBase());

        View buttonSaveUrls = findViewById(R.id.saveUrl);
        buttonSaveUrls.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveURLBase(urlTestBase.getText().toString());
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

    private String loadURLBase()
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String urlBase = settings.getString("remoteUrlBase", "http://192.168.1.215:8080/itsnat_dev/");
        return urlBase;
    }

    private void saveURLBase(String remoteUrlBase)
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("remoteUrlBase", remoteUrlBase);
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

package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setMainLayout();
    }

    protected void setMainLayout()
    {
        setContentView(R.layout.activity_main);

        final EditText editURL = (EditText)findViewById(R.id.remoteURL);
        editURL.setText(loadURL());
        View testLocal = findViewById(R.id.testLocal);
        testLocal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveURL(editURL.getText().toString());
                TestLayoutLocal.test(MainActivity.this);
            }
        });

        View testRemote = findViewById(R.id.testRemote);
        testRemote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = editURL.getText().toString();
                saveURL(url);
                TestLayoutRemote.test(MainActivity.this,url);
            }
        });


        // Pruebas temporales

        EditText editText = (EditText)findViewById(R.id.editTextPrueba);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
            {
                Log.v("MainActivity", "beforeTextChanged " + charSequence + " " + start + " " + count + " " + after);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                Log.v("MainActivity", "onTextChanged " + charSequence + " " + start + " " + before + " " + count);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                Log.v("MainActivity", "afterTextChanged " + editable);
            }
        });

        /*
        View parent = findViewById(R.id.frameParent);
        View child = findViewById(R.id.frameChild);

        if (true)
        {

            parent.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    System.out.println("IN PARENT " + motionEvent.getAction());
                    return true;
                }
            });

            child.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    System.out.println("IN CHILD " + motionEvent.getAction());
                    return true;
                }
            });
        }

        if (false)
        {
            parent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    System.out.println("IN PARENT ");
                }
            });

            child.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    System.out.println("IN CHILD ");
                }
            });
        }
        */
    }

    private String loadURL()
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String url = settings.getString("remoteUrlTestCore", "http://192.168.0.215:8080/itsnat_dev/ItsNatDroidServletExample?itsnat_doc_name=test_droid_core");
        return url;
    }

    private void saveURL(String url)
    {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("remoteUrlTestCore", url);
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

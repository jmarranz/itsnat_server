package org.itsnat.itsnatdroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


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

        View testLocal = findViewById(R.id.testLocal);
        testLocal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TestLayoutLocal.test(MainActivity.this);
            }
        });

        View testRemote = findViewById(R.id.testRemote);
        testRemote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TestLayoutRemote.test(MainActivity.this);
            }
        });


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

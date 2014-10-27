package org.itsnat.itsnatdroidtest.testact.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Esta clase permite que por ejemplo un ListView interno sea scrollable dentro del ScrollView, no es útil
 * para testear por ej los ListView
 *
 * http://stackoverflow.com/questions/6210895/listview-inside-scrollview-is-not-scrolling-on-android/11554684#11554684
 *
 * Created by jmarranz on 9/09/14.
 */
public class CustomScrollView extends ScrollView
{
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                Log.i("CustomScrollview", "onInterceptTouchEvent: DOWN super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                break; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                Log.i("CustomScrollview", "onInterceptTouchEvent: CANCEL super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                Log.i("CustomScrollview", "onInterceptTouchEvent: UP super false");
                break;

            default: Log.i("CustomScrollview", "onInterceptTouchEvent: " + action ); break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        Log.i("CustomScrollview", "onTouchEvent.action: " + ev.getAction() );
        return true;
    }
}
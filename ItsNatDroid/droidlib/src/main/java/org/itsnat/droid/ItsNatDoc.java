package org.itsnat.droid;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.event.EventStateless;
import org.itsnat.droid.event.UserEvent;

/**
 *
 * Created by jmarranz on 12/06/14.
 */
public interface ItsNatDoc
{
    public Page getPage();

    public void alert(Object value);
    public void alert(String title,Object value);
    public void toast(Object value,int duration);
    public void toast(Object value);
    public void eval(String code);
    public void postDelayed(Runnable task,long delay);

    public View findViewByXMLId(String id);

    public GenericHttpClient createGenericHttpClient();

    public UserEvent createUserEvent(String name);
    public void dispatchUserEvent(View currTarget,UserEvent evt);
    public void fireUserEvent(View currTarget,String name);

    public EventStateless createEventStateless();
    public void dispatchEventStateless(EventStateless evt,int commMode,long timeout);

    public void appendFragment(ViewGroup parentView, String markup);
    public void insertFragment(ViewGroup parentView, String markup,View viewRef);
}


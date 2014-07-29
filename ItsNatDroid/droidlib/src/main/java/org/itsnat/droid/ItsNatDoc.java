package org.itsnat.droid;

import android.view.View;

import org.itsnat.droid.Page;
import org.itsnat.droid.event.UserEvent;
import org.itsnat.droid.impl.browser.clientdoc.Node;

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

    public UserEvent createUserEvent(String name);
    public void dispatchUserEvent(Node currTarget,UserEvent evt);
    public void dispatchUserEvent(View currTarget,UserEvent evt);
    public void fireUserEvent(Node currTarget,String name);
    public void fireUserEvent(View currTarget,String name);
}


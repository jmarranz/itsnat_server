package org.itsnat.droid;

import android.view.View;

/**
 * Created by jmarranz on 19/05/14.
 */
public interface AttrCustomInflaterListener
{
    public void setAttribute(View view,String namespace,String name,String value);
    public void removeAttribute(View view,String namespace,String name);
}

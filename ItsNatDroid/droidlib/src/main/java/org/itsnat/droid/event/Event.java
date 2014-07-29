package org.itsnat.droid.event;

import java.util.HashMap;

/**
 * Created by jmarranz on 8/07/14.
 */
public interface Event
{
    public Object getExtraParam(String name);
    public void setExtraParam(String name,Object value);
}

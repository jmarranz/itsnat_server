package org.itsnat.droid.impl.browser.serveritsnat;

import org.itsnat.droid.impl.browser.serveritsnat.event.EventGenericImpl;

/**
 * Created by jmarranz on 8/07/14.
 */
public interface GlobalEventListener
{
    public boolean process(EventGenericImpl evt);
}

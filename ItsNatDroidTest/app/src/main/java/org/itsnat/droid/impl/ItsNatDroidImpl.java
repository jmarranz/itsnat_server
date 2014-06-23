package org.itsnat.droid.impl;

import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.impl.browser.ItsNatDroidBrowserImpl;
import org.itsnat.droid.impl.xmlinflater.InflateRequestImpl;


/**
 * Created by jmarranz on 3/05/14.
 */
public class ItsNatDroidImpl implements ItsNatDroid
{
    public static final ItsNatDroidImpl DEFAULT = new ItsNatDroidImpl();

    @Override
    public ItsNatDroidBrowser createItsNatDroidBrowser()
    {
        return new ItsNatDroidBrowserImpl(this);
    }

    public InflateRequest createInflateRequest()
    {
        // El modelo ItsNat está muy bien pero ofrecemos como alternativa que el propio programador se descargue sus layouts
        // y los gestione a su manera
        return new InflateRequestImpl();
    }
}

package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TouchEventListenerViewAdapter;

/**
 * Created by jmarranz on 11/08/14.
 */
public class ItsNatViewNullImpl extends ItsNatViewImpl
{
    public ItsNatViewNullImpl(ItsNatDocImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public View getView()
    {
        return null;
    }

    public String getXMLId()
    {
        return null;
    }

    @Override
    public void setXMLId(String id)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public ClickEventListenerViewAdapter getClickEventListenerViewAdapter()
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public TouchEventListenerViewAdapter getTouchEventListenerViewAdapter()
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public KeyEventListenerViewAdapter getKeyEventListenerViewAdapter()
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public FocusEventListenerViewAdapter getFocusEventListenerViewAdapter()
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public TextChangeEventListenerViewAdapter getTextChangeEventListenerViewAdapter()
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public void setTextChangeEventListenerViewAdapter(TextChangeEventListenerViewAdapter textChangeEvtListenerViewAdapter)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }


    public void setOnClickListener(View.OnClickListener l)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public void setOnTouchListener(View.OnTouchListener l)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public void setOnKeyListener(View.OnKeyListener l)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener l)
    {
        throw new ItsNatDroidException("INTERNAL ERROR");
    }

}

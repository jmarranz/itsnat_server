package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TouchEventListenerViewAdapter;

/**
 * Created by jmarranz on 11/08/14.
 */
public class ItsNatViewNullImpl extends ItsNatViewImpl
{
    public ItsNatViewNullImpl(PageImpl page)
    {
        super(page);
    }

    public View getView()
    {
        return null;
    }

    public String getXMLId()
    {
        return null;
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

package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;

import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.clientdoc.evtlistadapter.TouchEventListenerViewAdapter;

/**
 * Created by jmarranz on 11/08/14.
 */
public class ItsNatViewNotNullImpl extends ItsNatViewImpl
{
    protected View view;
    protected ClickEventListenerViewAdapter clickEvtListenerViewAdapter;
    protected TouchEventListenerViewAdapter touchEvtListenerViewAdapter;
    protected KeyEventListenerViewAdapter keyEvtListenerViewAdapter;
    protected FocusEventListenerViewAdapter focusEvtListenerViewAdapter;
    protected TextChangeEventListenerViewAdapter textChangeEvtListenerViewAdapter;

    public ItsNatViewNotNullImpl(PageImpl page, View view)
    {
        super(page);
        this.view = view;
    }

    public View getView()
    {
        return view;
    }

    public String getXMLId()
    {
        return page.getInflatedLayoutImpl().getXMLId(view);
    }

    public ClickEventListenerViewAdapter getClickEventListenerViewAdapter()
    {
        if (clickEvtListenerViewAdapter == null) this.clickEvtListenerViewAdapter = new ClickEventListenerViewAdapter(this);
        return clickEvtListenerViewAdapter;
    }

    public TouchEventListenerViewAdapter getTouchEventListenerViewAdapter()
    {
        if (touchEvtListenerViewAdapter == null) this.touchEvtListenerViewAdapter = new TouchEventListenerViewAdapter(this);
        return touchEvtListenerViewAdapter;
    }

    public KeyEventListenerViewAdapter getKeyEventListenerViewAdapter()
    {
        if (keyEvtListenerViewAdapter == null) this.keyEvtListenerViewAdapter = new KeyEventListenerViewAdapter(this);
        return keyEvtListenerViewAdapter;
    }

    public FocusEventListenerViewAdapter getFocusEventListenerViewAdapter()
    {
        if (focusEvtListenerViewAdapter == null) this.focusEvtListenerViewAdapter = new FocusEventListenerViewAdapter(this);
        return focusEvtListenerViewAdapter;
    }

    public TextChangeEventListenerViewAdapter getTextChangeEventListenerViewAdapter()
    {
        // Como el listener nativo se puede registrar muchas veces nosotros tenemos que hacerlo UNA sola vez y necesitamos detectarlo
        return textChangeEvtListenerViewAdapter;
    }

    public void setTextChangeEventListenerViewAdapter(TextChangeEventListenerViewAdapter textChangeEvtListenerViewAdapter)
    {
        this.textChangeEvtListenerViewAdapter = textChangeEvtListenerViewAdapter;
    }


    public void setOnClickListener(View.OnClickListener l)
    {
        ClickEventListenerViewAdapter evtListenerViewAdapter = getClickEventListenerViewAdapter();
        view.setOnClickListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnClickListener(l);
    }

    public void setOnTouchListener(View.OnTouchListener l)
    {
        TouchEventListenerViewAdapter evtListenerViewAdapter = getTouchEventListenerViewAdapter();
        view.setOnTouchListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnTouchListener(l);
    }

    public void setOnKeyListener(View.OnKeyListener l)
    {
        KeyEventListenerViewAdapter evtListenerViewAdapter = getKeyEventListenerViewAdapter();
        view.setOnKeyListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnKeyListener(l);
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener l)
    {
        FocusEventListenerViewAdapter evtListenerViewAdapter = getFocusEventListenerViewAdapter();
        view.setOnFocusChangeListener(evtListenerViewAdapter);
        evtListenerViewAdapter.setOnFocusChangeListener(l);
    }

}

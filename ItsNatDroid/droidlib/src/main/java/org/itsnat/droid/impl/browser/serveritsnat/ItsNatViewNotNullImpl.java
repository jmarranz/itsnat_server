package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.layout.page.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.ClickEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.FocusEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.KeyEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TextChangeEventListenerViewAdapter;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter.TouchEventListenerViewAdapter;

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

    public ItsNatViewNotNullImpl(ItsNatDocImpl itsNatDoc, View view)
    {
        super(itsNatDoc);
        this.view = view;
    }

    public InflatedLayoutPageImpl getInflatedLayoutPageImpl()
    {
        return itsNatDoc.getPageImpl().getInflatedLayoutPageImpl();
    }

    public View getView()
    {
        return view;
    }

    public String getXMLId()
    {
        return getInflatedLayoutPageImpl().getXMLId(view);
    }

    public void setXMLId(String id)
    {
        getInflatedLayoutPageImpl().setXMLId(id, view);
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

    public void registerEventListenerViewAdapter(String type)
    {
        if (type.equals("click"))
        {
            // No sabemos si ha sido registrado ya antes el ClickEventListenerViewAdapter, pero da igual puede llamarse todas las veces que se quiera
            ClickEventListenerViewAdapter evtListAdapter = getClickEventListenerViewAdapter();
            view.setOnClickListener(evtListAdapter);
        }
        else if (type.equals("change"))
        {
            // Como el listener nativo se puede registrar muchas veces nosotros tenemos que hacerlo UNA sola vez y necesitamos detectarlo
            // por ello evtListAdapter puede ser null
            TextChangeEventListenerViewAdapter evtListAdapter = getTextChangeEventListenerViewAdapter();
            if (evtListAdapter == null)
            {
                evtListAdapter = new TextChangeEventListenerViewAdapter(this);
                setTextChangeEventListenerViewAdapter(evtListAdapter);
                // El change está pensado para el componente EditText pero el método addTextChangedListener está a nivel de TextView, por si acaso
                ((TextView) view).addTextChangedListener(evtListAdapter); // Sólo registramos una vez
            }
        }
        else if (type.equals("focus") || type.equals("blur"))
        {
            FocusEventListenerViewAdapter evtListAdapter = getFocusEventListenerViewAdapter();
            view.setOnFocusChangeListener(evtListAdapter);
        }
        else if (type.startsWith("key"))
        {
            KeyEventListenerViewAdapter evtListAdapter = getKeyEventListenerViewAdapter();
            view.setOnKeyListener(evtListAdapter);
        }
        else if (type.startsWith("touch"))
        {
            TouchEventListenerViewAdapter evtListAdapter = getTouchEventListenerViewAdapter();
            view.setOnTouchListener(evtListAdapter);
        }

        // Es posible que sea un load, unload etc, no damos error, simplemente no tiene ViewAdapter
    }
}

package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.InputEvent;
import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidInputEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class EventListenerViewAdapter
{
    protected ItsNatViewImpl viewData;

    public EventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    protected void dispatch(String type,InputEvent nativeEvt)
    {
        dispatch(viewData,type,nativeEvt,true, DroidInputEventImpl.AT_TARGET,viewData.getView());
    }

    protected static void dispatch(ItsNatViewImpl viewData,String type,InputEvent nativeEvt,boolean checkUseCapture,int eventPhase,View viewTarget)
    {
        List<DroidEventListener> list = viewData.getEventListeners(type);
        if (list == null) return;

        View view = viewData.getView();
        for (DroidEventListener listener : list)
        {
            if (checkUseCapture && listener.isUseCapture())
            {
                PageImpl page = viewData.getPageImpl();
                List<ViewParent> tree = getViewTree(view);
                for(ViewParent viewParent : tree)
                {
                    ItsNatViewImpl viewParentData = page.getItsNatViewImpl((View)viewParent);
                    dispatch(viewParentData,type,nativeEvt,false, DroidInputEventImpl.CAPTURING_PHASE,viewTarget);
                }
            }

            DroidInputEventImpl evtWrapper = (DroidInputEventImpl)listener.createEventWrapper(nativeEvt);
            try
            {
                evtWrapper.setEventPhase(eventPhase);
                evtWrapper.setViewTarget(viewTarget);
                listener.dispatchEvent(evtWrapper);
            }
            catch(Exception ex)
            {
                // Desde aquí capturamos todos los fallos del proceso de eventos, el código anterior a dispatch(String,InputEvent) nunca debería
                // fallar, o bien porque es muy simple o porque hay llamadas al código del usuario que él mismo puede controlar sus fallos
                OnEventErrorListener errorListener = viewData.getPageImpl().getOnEventErrorListener();
                if (errorListener != null)
                {
                    errorListener.onError(ex, evtWrapper);
                    continue;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        }
    }

    protected static List<ViewParent> getViewTree(View view)
    {
        List<ViewParent> tree = new LinkedList<ViewParent>();
        ViewParent parent = view.getParent(); // Asegura que en la lista no está el View inicial
        getViewTree(parent,tree);
        return tree;
    }

    protected static void getViewTree(ViewParent view,List<ViewParent> tree)
    {
        if (view == null || !(view instanceof View)) return;
        tree.add(0, view);
        getViewTree(view.getParent(),tree);
    }
}

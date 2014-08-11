package org.itsnat.droid.impl.browser.clientdoc.evtlistadapter;

import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidInputEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/07/14.
 */
public abstract class DroidEventListenerViewAdapter
{
    protected ItsNatViewImpl viewData;

    public DroidEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        this.viewData = viewData;
    }

    protected void dispatch(String type,Object nativeEvt)
    {
        dispatch(viewData,type,nativeEvt);
    }

    public static void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt)
    {
        View viewTarget = viewDataCurrentTarget != null ? viewDataCurrentTarget.getView() : null; // En "unload" puede ser nulo
        dispatch(viewDataCurrentTarget,type,nativeEvt,true, DroidInputEventImpl.AT_TARGET,viewTarget);
    }

    protected static void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt,boolean checkUseCapture,int eventPhase,View viewTarget)
    {
        List<DroidEventListener> list = viewDataCurrentTarget.getEventListeners(type);
        if (list == null) return;

        View viewCurrentTarget = viewDataCurrentTarget.getView();
        for (DroidEventListener listener : list)
        {
            if (checkUseCapture && listener.isUseCapture())
            {
                dispatchCapture(viewDataCurrentTarget,viewCurrentTarget,type,nativeEvt,viewTarget);
            }

            DroidEventImpl evtWrapper = (DroidEventImpl)listener.createEventWrapper(nativeEvt);
            try
            {
                evtWrapper.setEventPhase(eventPhase);
                evtWrapper.setTarget(viewTarget);
                listener.dispatchEvent(evtWrapper);
            }
            catch(Exception ex)
            {
                // Desde aquí capturamos todos los fallos del proceso de eventos, el código anterior a dispatchEvent(String,InputEvent) nunca debería
                // fallar, o bien porque es muy simple o porque hay llamadas al código del usuario que él mismo puede controlar sus fallos
                OnEventErrorListener errorListener = viewDataCurrentTarget.getPageImpl().getOnEventErrorListener();
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

    private static void dispatchCapture(ItsNatViewImpl viewData,View view,String type,Object nativeEvt,View viewTarget)
    {
        PageImpl page = viewData.getPageImpl();
        List<ViewParent> tree = getViewTreeParent(view);
        for (ViewParent viewParent : tree)
        {
            ItsNatViewImpl viewParentData = page.getItsNatViewImpl((View) viewParent);
            dispatch(viewParentData, type, nativeEvt, false, DroidInputEventImpl.CAPTURING_PHASE, viewTarget);
        }
    }

    protected static List<ViewParent> getViewTreeParent(View view)
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

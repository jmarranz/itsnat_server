package org.itsnat.droid.impl.browser.clientdoc;

import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.event.DroidInputEventImpl;
import org.itsnat.droid.impl.browser.clientdoc.evtlistener.DroidEventListener;

import java.util.LinkedList;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by jmarranz on 22/08/14.
 */
public class DroidEventDispatcher
{
    protected ItsNatDocImpl parent;

    public DroidEventDispatcher(ItsNatDocImpl parent)
    {
        this.parent = parent;
    }

    public void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt)
    {
        String inlineCode = viewDataCurrentTarget.getOnTypeInlineCode("on" + type);
        if (inlineCode != null)
        {
            executeInlineEventHandler(viewDataCurrentTarget, inlineCode, type, nativeEvt);
        }

        View viewTarget = viewDataCurrentTarget.getView(); // En "unload" viewDataCurrentTarget es ItsNatViewNullImpl por lo que getView() es nulo
        dispatch(viewDataCurrentTarget,type,nativeEvt,true, DroidEventImpl.AT_TARGET,viewTarget);
    }

    private void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt,boolean checkUseCapture,int eventPhase,View viewTarget)
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

            DroidEventImpl evtWrapper = (DroidEventImpl)listener.createNormalEvent(nativeEvt);
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

    private void dispatchCapture(ItsNatViewImpl viewData,View view,String type,Object nativeEvt,View viewTarget)
    {
        PageImpl page = viewData.getPageImpl();
        List<ViewParent> tree = getViewTreeParent(view);
        for (ViewParent viewParent : tree)
        {
            ItsNatViewImpl viewParentData = page.getItsNatViewImpl((View) viewParent);
            dispatch(viewParentData, type, nativeEvt, false, DroidInputEventImpl.CAPTURING_PHASE, viewTarget);
        }
    }

    private static List<ViewParent> getViewTreeParent(View view)
    {
        List<ViewParent> tree = new LinkedList<ViewParent>();
        ViewParent parent = view.getParent(); // Asegura que en la lista no está el View inicial
        getViewTree(parent,tree);
        return tree;
    }

    private static void getViewTree(ViewParent view,List<ViewParent> tree)
    {
        if (view == null || !(view instanceof View)) return;
        tree.add(0, view);
        getViewTree(view.getParent(),tree);
    }

    private void executeInlineEventHandler(ItsNatViewImpl viewData,String inlineCode, String type, Object nativeEvt)
    {
        View view = viewData.getView();
        int eventGroupCode = DroidEventGroupInfo.getEventGroupCode(type);
        DroidEventListener listenerFake = new DroidEventListener(viewData.getPageImpl().getItsNatDocImpl(), view, type, null, null, false, -1, -1, eventGroupCode);
        DroidEventImpl event = (DroidEventImpl)listenerFake.createNormalEvent(nativeEvt);
        event.setEventPhase(DroidEventImpl.AT_TARGET);
        event.setTarget(event.getCurrentTarget()); // El inline handler no participa en capture en web

        Interpreter interp = viewData.getPageImpl().getInterpreter();
        try
        {
            interp.set("event", event);
            interp.eval(inlineCode);
            interp.set("event", null); // Para evitar un memory leak
        }
        catch (EvalError ex)
        {
            throw new ItsNatDroidScriptException(ex, inlineCode);
        }
        catch (Exception ex)
        {
            throw new ItsNatDroidScriptException(ex, inlineCode);
        }
    }
}

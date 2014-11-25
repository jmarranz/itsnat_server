package org.itsnat.droid.impl.xmlinflater.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.dom.layout.DOMScript;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutStandaloneImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.layout.page.XMLInflaterLayoutPage;
import org.itsnat.droid.impl.xmlinflater.layout.stdalone.XMLInflaterLayoutStandalone;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflaterLayout extends XMLInflater
{
    protected InflatedLayoutImpl inflatedLayout;
    protected AttrLayoutInflaterListener inflateLayoutListener;

    public XMLInflaterLayout(InflatedLayoutImpl inflatedLayout,AttrLayoutInflaterListener inflateLayoutListener,Context ctx)
    {
        super(ctx);
        this.inflatedLayout = inflatedLayout;
        this.inflateLayoutListener = inflateLayoutListener;
    }

    public static XMLInflaterLayout createXMLInflaterLayout(InflatedLayoutImpl inflatedLayout,AttrLayoutInflaterListener inflateLayoutListener, Context ctx,PageImpl page)
    {
        if (inflatedLayout instanceof InflatedLayoutPageImpl)
        {
            return new XMLInflaterLayoutPage((InflatedLayoutPageImpl)inflatedLayout,inflateLayoutListener,ctx,page);
        }
        else if (inflatedLayout instanceof InflatedLayoutStandaloneImpl)
        {
            return new XMLInflaterLayoutStandalone((InflatedLayoutStandaloneImpl)inflatedLayout,inflateLayoutListener,ctx);
        }
        return null; // Internal Error
    }

    public InflatedLayoutImpl getInflatedLayoutImpl()
    {
        return inflatedLayout;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return inflateLayoutListener;
    }

    public View inflateLayout(String[] loadScript, List<String> scriptList)
    {
        XMLDOMLayout domLayout = inflatedLayout.getXMLDOMLayout();
        if (loadScript != null)
            loadScript[0] = domLayout.getLoadScript();

        if (scriptList != null)
            fillScriptList(domLayout,scriptList);

        View rootView = inflateRootView(domLayout);
        return rootView;
    }

    private static void fillScriptList(XMLDOMLayout domLayout,List<String> scriptList)
    {
        List<DOMScript> scriptListFromTree = domLayout.getDOMScriptList();
        if (scriptListFromTree != null)
        {
            for (DOMScript script : scriptListFromTree)
                scriptList.add(script.getCode());
        }
    }

    private View inflateRootView(XMLDOMLayout domLayout)
    {
        DOMView rootDOMView = domLayout.getRootView();

        String viewName = rootDOMView.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

        PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        View rootView = createRootViewObjectAndFillAttributes(viewName, rootDOMView,pending);

        processChildViews(rootDOMView,rootView);

        pending.executeTasks();

        return rootView;
    }

    public View createRootViewObjectAndFillAttributes(String viewName,DOMView rootDOMView,PendingPostInsertChildrenTasks pending)
    {
        ClassDescViewMgr classDescViewMgr = inflatedLayout.getXMLInflateRegistry().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        View rootView = createViewObject(classDesc, rootDOMView,pending);

        setRootView(rootView); // Lo antes posible porque los inline event handlers lo necesitan, es el root View del template, no el View.getRootView() pues una vez insertado en la actividad de alguna forma el verdadero root cambia

        fillAttributesAndAddView(rootView,classDesc,null, rootDOMView,pending);

        return rootView;
    }

    public void setRootView(View rootView)
    {
        inflatedLayout.setRootView(rootView);
    }

    public View createViewObjectAndFillAttributesAndAdd(ViewGroup viewParent, DOMView domView, PendingPostInsertChildrenTasks pending)
    {
        // viewParent es null en el caso de parseo de fragment, por lo que NO tengas la tentación de llamar aquí
        // a setRootView(view); cuando viewParent es null "para reutilizar código"
        ClassDescViewMgr classDescViewMgr = inflatedLayout.getXMLInflateRegistry().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(domView.getName());
        View view = createViewObject(classDesc, domView,pending);

        fillAttributesAndAddView(view,classDesc,viewParent, domView,pending);

        return view;
    }

    private View createViewObject(ClassDescViewBased classDesc,DOMView domView,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.createViewObjectFromParser(inflatedLayout, domView,pending);
    }

    private void fillAttributesAndAddView(View view,ClassDescViewBased classDesc,ViewGroup viewParent,DOMView domView,PendingPostInsertChildrenTasks pending)
    {
        OneTimeAttrProcess oneTimeAttrProcess = classDesc.createOneTimeAttrProcess(view,viewParent);
        fillViewAttributes(classDesc,view, domView,oneTimeAttrProcess,pending); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        classDesc.addViewObject(viewParent, view, -1, oneTimeAttrProcess, inflatedLayout.getContext());
    }

    private void fillViewAttributes(ClassDescViewBased classDesc,View view,DOMView domView,OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        ArrayList<DOMAttr> attribList = domView.getDOMAttributeList();
        if (attribList != null)
        {
            for (int i = 0; i < attribList.size(); i++)
            {
                DOMAttr attr = attribList.get(i);
                setAttribute(classDesc, view, attr, oneTimeAttrProcess, pending);
            }
        }

        oneTimeAttrProcess.executeLastTasks();
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,DOMAttr attr,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.setAttribute(view,attr,this,ctx,oneTimeAttrProcess,pending);
    }

    protected void processChildViews(DOMView domViewParent, View viewParent)
    {
        LinkedList<DOMElement> childViewList = domViewParent.getChildDOMElementList();
        if (childViewList != null)
        {
            for (DOMElement childDOMView : childViewList)
            {
                View childView = inflateNextView((DOMView)childDOMView, viewParent);
            }
        }
    }

    public View insertFragment(DOMView rootDOMViewFragment)
    {
        return inflateNextView(rootDOMViewFragment,null);
    }

    private View inflateNextView(DOMView domView, View viewParent)
    {
        // Es llamado también para insertar fragmentos
        PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        View view = createViewObjectAndFillAttributesAndAdd((ViewGroup) viewParent, domView, pending);

        // No funciona, sólo funciona con XML compilados:
        //AttributeSet attributes = Xml.asAttributeSet(parser);
        //LayoutInflater inf = LayoutInflater.from(ctx);

        processChildViews(domView,view);

        pending.executeTasks();

        return view;
    }

}

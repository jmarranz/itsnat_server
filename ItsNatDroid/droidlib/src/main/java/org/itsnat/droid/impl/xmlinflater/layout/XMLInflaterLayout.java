package org.itsnat.droid.impl.xmlinflater.layout;

import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.layout.ScriptParsed;
import org.itsnat.droid.impl.model.layout.ViewParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflaterLayout
{
    protected InflatedLayoutImpl layout;

    public XMLInflaterLayout(InflatedLayoutImpl layout)
    {
        this.layout = layout;
    }

    public View inflateLayout(String[] loadScript, List<String> scriptList)
    {
        LayoutParsed layoutParsed = layout.getLayoutParsed();
        if (loadScript != null)
            loadScript[0] = layoutParsed.getLoadScript();

        if (scriptList != null)
            fillScriptList(layoutParsed,scriptList);

        View rootView = inflateRootView(layoutParsed);
        return rootView;
    }

    private static void fillScriptList(LayoutParsed layoutParsed,List<String> scriptList)
    {
        List<ScriptParsed> scriptListFromTree = layoutParsed.getScriptList();
        if (scriptListFromTree != null)
        {
            for (ScriptParsed script : scriptListFromTree)
                scriptList.add(script.getCode());
        }
    }

    private View inflateRootView(LayoutParsed layoutParsed)
    {
        ViewParsed rootViewParsed = layoutParsed.getRootView();

        String viewName = rootViewParsed.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

        PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        View rootView = createRootViewObjectAndFillAttributes(viewName,rootViewParsed,pending);

        processChildViews(rootViewParsed,rootView);

        pending.executeTasks();

        return rootView;
    }

    public View createRootViewObjectAndFillAttributes(String viewName,ViewParsed viewParsed,PendingPostInsertChildrenTasks pending)
    {
        ClassDescViewMgr classDescViewMgr = layout.getXMLInflateService().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        View rootView = createViewObject(classDesc,viewParsed,pending);

        setRootView(rootView); // Lo antes posible porque los inline event handlers lo necesitan, es el root View del template, no el View.getRootView() pues una vez insertado en la actividad de alguna forma el verdadero root cambia

        fillAttributesAndAddView(rootView,classDesc,null,viewParsed,pending);

        return rootView;
    }

    public void setRootView(View rootView)
    {
        layout.setRootView(rootView);
    }

    public View createViewObjectAndFillAttributesAndAdd(ViewGroup viewParent, ViewParsed viewParsed, PendingPostInsertChildrenTasks pending)
    {
        // viewParent es null en el caso de parseo de fragment, por lo que NO tengas la tentación de llamar aquí
        // a setRootView(view); cuando viewParent es null "para reutilizar código"
        ClassDescViewMgr classDescViewMgr = layout.getXMLInflateService().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewParsed.getName());
        View view = createViewObject(classDesc,viewParsed,pending);

        fillAttributesAndAddView(view,classDesc,viewParent,viewParsed,pending);

        return view;
    }

    private View createViewObject(ClassDescViewBased classDesc,ViewParsed viewParsed,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.createViewObjectFromParser(layout,viewParsed,pending);
    }

    private void fillAttributesAndAddView(View view,ClassDescViewBased classDesc,ViewGroup viewParent,ViewParsed viewParsed,PendingPostInsertChildrenTasks pending)
    {
        OneTimeAttrProcess oneTimeAttrProcess = classDesc.createOneTimeAttrProcess(view,viewParent);
        fillViewAttributes(classDesc,view,viewParsed,oneTimeAttrProcess,pending); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        classDesc.addViewObject(viewParent,view,-1,oneTimeAttrProcess,layout.getContext());
    }

    private void fillViewAttributes(ClassDescViewBased classDesc,View view,ViewParsed viewParsed,OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        ArrayList<AttrParsed> attribList = viewParsed.getAttributeList();
        if (attribList != null)
        {
            for (int i = 0; i < attribList.size(); i++)
            {
                AttrParsed attr = attribList.get(i);
                setAttribute(classDesc, view, attr, oneTimeAttrProcess, pending);
            }
        }

        oneTimeAttrProcess.executeLastTasks();
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,AttrParsed attr,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.setAttribute(view,attr, oneTimeAttrProcess,pending,layout);
    }

    protected void processChildViews(ViewParsed viewParsedParent, View viewParent)
    {
        LinkedList<ElementParsed> childViewList = viewParsedParent.getChildList();
        if (childViewList != null)
        {
            for (ElementParsed childViewParsed : childViewList)
            {
                View childView = inflateNextView((ViewParsed)childViewParsed, viewParent);
            }
        }
    }

    public View insertFragment(ViewParsed rootViewFragmentParsed)
    {
        return inflateNextView(rootViewFragmentParsed,null);
    }

    private View inflateNextView(ViewParsed viewParsed, View viewParent)
    {
        // Es llamado también para insertar fragmentos
        PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        View view = createViewObjectAndFillAttributesAndAdd((ViewGroup) viewParent, viewParsed, pending);

        // No funciona, sólo funciona con XML compilados:
        //AttributeSet attributes = Xml.asAttributeSet(parser);
        //LayoutInflater inf = LayoutInflater.from(ctx);

        processChildViews(viewParsed,view);

        pending.executeTasks();

        return view;
    }
}

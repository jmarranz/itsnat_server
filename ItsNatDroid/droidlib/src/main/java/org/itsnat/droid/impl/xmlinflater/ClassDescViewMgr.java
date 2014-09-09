package org.itsnat.droid.impl.xmlinflater;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescUnknown;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_view_View;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_CompoundButton;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_FrameLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_GridLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_LinearLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RelativeLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_TextView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ViewGroup;

import java.util.HashMap;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewMgr
{
    protected XMLLayoutInflateService parent;
    private final HashMap<String,ClassDescViewBased> classes = new HashMap<String,ClassDescViewBased>();

    public ClassDescViewMgr(XMLLayoutInflateService parent)
    {
        this.parent = parent;
        initClassDesc();
    }

    private void initClassDesc()
    {
        ClassDesc_view_View view_View = new ClassDesc_view_View();
        addClassDescViewBase(view_View);

          ClassDesc_widget_ViewGroup view_ViewGroup = new ClassDesc_widget_ViewGroup(view_View);
          addClassDescViewBase(view_ViewGroup);

            // AbsoluteLayout y su derivada (WebView) no tienen atributos

            ClassDescViewBased widget_AdapterView = new ClassDescViewBased("android.widget.AdapterView",view_ViewGroup); // AdapterView no tiene atributos
            addClassDescViewBase(widget_AdapterView);

                ClassDesc_widget_AbsListView widget_AbsListView = new ClassDesc_widget_AbsListView(widget_AdapterView);
                addClassDescViewBase(widget_AbsListView);

                    ClassDesc_widget_ListView widget_ListView = new ClassDesc_widget_ListView(widget_AbsListView);
                    addClassDescViewBase(widget_ListView);

            ClassDesc_widget_FrameLayout widget_FrameLayout = new ClassDesc_widget_FrameLayout(view_ViewGroup);
            addClassDescViewBase(widget_FrameLayout);

            ClassDesc_widget_GridLayout widget_GridLayout = new ClassDesc_widget_GridLayout(view_ViewGroup);
            addClassDescViewBase(widget_GridLayout);

            ClassDesc_widget_LinearLayout widget_LinearLayout = new ClassDesc_widget_LinearLayout(view_ViewGroup);
            addClassDescViewBase(widget_LinearLayout);

            ClassDesc_widget_RelativeLayout widget_RelativeLayout = new ClassDesc_widget_RelativeLayout(view_ViewGroup);
            addClassDescViewBase(widget_RelativeLayout);

          ClassDesc_widget_TextView widget_TextView = new ClassDesc_widget_TextView(view_View);
          addClassDescViewBase(widget_TextView);

            ClassDescViewBased widget_Button = new ClassDescViewBased("android.widget.Button",widget_TextView);
            addClassDescViewBase(widget_Button);

              ClassDesc_widget_CompoundButton widget_CompoundButton = new ClassDesc_widget_CompoundButton(widget_Button);
              addClassDescViewBase(widget_CompoundButton);
    }

    private void addClassDescViewBase(ClassDescViewBased viewDesc)
    {
        classes.put(viewDesc.getClassName(), viewDesc);
    }

    private static Class<View> resolveViewClass(String viewName) throws ClassNotFoundException
    {
        if (viewName.indexOf('.') == -1)
        {
            try
            {
                return resolveViewClass("android.view." + viewName);
            }
            catch (ClassNotFoundException e)
            {
                return resolveViewClass("android.widget." + viewName);
            }
        }
        else
        {
            return (Class<View>)Class.forName(viewName);
        }
    }

    public ClassDescViewBased get(String viewName)
    {
        Class<View> viewClass = null;
        try { viewClass = resolveViewClass(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        ClassDescViewBased classDesc = get(viewClass);
        return classDesc;
    }

    public ClassDescViewBased get(Class<View> viewClass)
    {
        ClassDescViewBased classDesc = classes.get(viewClass.getName());
        if (classDesc == null) classDesc = registerUnknown(viewClass);
        return classDesc; // Nunca es nulo
    }

    public ClassDescViewBased get(View view)
    {
        Class<View> viewClass = (Class<View>)view.getClass();
        return get(viewClass);
    }

    public ClassDescViewBased registerUnknown(Class<View> viewClass)
    {
        String className = viewClass.getName();
        // Tenemos que obtener los ClassDescViewBase de las clases base para que podamos saber lo más posible
        Class<?> superClass = (Class<?>)viewClass.getSuperclass();
        ClassDescViewBased parent = get((Class<View>)superClass); // Si fuera también unknown se llamará recursivamente de nuevo a este método
        ClassDescUnknown classDesc = new ClassDescUnknown(className,parent);

        classes.put(viewClass.getName(), classDesc);

        return classDesc;
    }
}

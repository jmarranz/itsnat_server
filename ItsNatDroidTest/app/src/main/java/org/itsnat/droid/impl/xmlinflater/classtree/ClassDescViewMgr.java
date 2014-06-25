package org.itsnat.droid.impl.xmlinflater.classtree;

import android.view.View;

import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;

import java.util.HashMap;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewMgr
{
    protected XMLLayoutInflateService parent;
    private final HashMap<String,ClassDescViewBase> classes = new HashMap<String,ClassDescViewBase>();

    public ClassDescViewMgr(XMLLayoutInflateService parent)
    {
        this.parent = parent;
        initClassDesc();
    }

    private void initClassDesc()
    {
        ClassDescViewView view_View = new ClassDescViewView();
        addClassDescViewBase(view_View);

        ClassDescViewBase view_ViewGroup = new ClassDescViewBase("android.view.ViewGroup",view_View);
        addClassDescViewBase(view_ViewGroup);

        ClassDescWidgetLinearLayout widget_LinearLayout = new ClassDescWidgetLinearLayout(view_ViewGroup);
        addClassDescViewBase(widget_LinearLayout);

        ClassDescViewBase widget_RelativeLayout = new ClassDescViewBase("android.widget.RelativeLayout",view_ViewGroup);
        addClassDescViewBase(widget_RelativeLayout);

        ClassDescWidgetTextView widget_TextView = new ClassDescWidgetTextView(view_View);
        addClassDescViewBase(widget_TextView);

        ClassDescViewBase widget_Button = new ClassDescViewBase("android.widget.Button",widget_TextView);
        addClassDescViewBase(widget_Button);
    }

    private void addClassDescViewBase(ClassDescViewBase viewDesc)
    {
        classes.put(viewDesc.getClassName(), viewDesc);
    }

    private ClassDescViewBase getInternal(String className)
    {
        ClassDescViewBase classDesc = classes.get(className);
        return classDesc; // Puede ser nulo
    }

    public ClassDescViewBase get(Class<View> viewClass)
    {
        ClassDescViewBase classDesc = getInternal(viewClass.getName());
        if (classDesc == null) classDesc = registerUnknown(viewClass);
        return classDesc; // Nunca es nulo
    }

    public ClassDescViewBase get(View view)
    {
        Class<View> viewClass = (Class<View>)view.getClass();
        return get(viewClass);
    }

    public ClassDescViewBase registerUnknown(Class<View> viewClass)
    {
        String className = viewClass.getName();
        // Tenemos que obtener los ClassDescViewBase de las clases base para que podamos saber lo más posible
        Class<View> superClass = (Class<View>)viewClass.getSuperclass();
        ClassDescViewBase parent = get(superClass); // Si fuera también unknown se llamará recursivamente de nuevo a este método
        ClassDescUnknown classDesc = new ClassDescUnknown(className,parent);

        classes.put(viewClass.getName(), classDesc);

        return classDesc;
    }
}

package org.itsnat.droid.impl.xmlinflater;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescUnknown;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_view_View;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsSpinner;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AdapterViewAnimator;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AdapterViewFlipper;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AnalogClock;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_CompoundButton;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ExpandableListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_FrameLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Gallery;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_GridLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_GridView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ImageView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_LinearLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ProgressBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RatingBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RelativeLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Spinner;
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
            init_view_ViewGroup_subClasses(view_ViewGroup);

            ClassDesc_widget_AnalogClock widget_AnalogClock = new ClassDesc_widget_AnalogClock(view_View);
            addClassDescViewBase(widget_AnalogClock);

            ClassDesc_widget_ImageView widget_ImageView = new ClassDesc_widget_ImageView(view_View);
            addClassDescViewBase(widget_ImageView);
                // ImageButton no tiene atributos
                    // ZoomButton no tiene atributos

            // android.support.v7.app.MediaRouteButton no tiene atributos

            ClassDesc_widget_ProgressBar widget_ProgressBar = new ClassDesc_widget_ProgressBar(view_View);
            addClassDescViewBase(widget_ProgressBar);
                ClassDescViewBased widget_AbsSeekBar = new ClassDescViewBased("android.widget.AbsSeekBar",widget_ProgressBar); // AbsSeekBar no tiene atributos
                addClassDescViewBase(widget_AbsSeekBar);
                    ClassDesc_widget_RatingBar widget_RatingBar = new ClassDesc_widget_RatingBar(widget_AbsSeekBar);
                    addClassDescViewBase(widget_RatingBar);


            ClassDesc_widget_TextView widget_TextView = new ClassDesc_widget_TextView(view_View);
            addClassDescViewBase(widget_TextView);

                ClassDescViewBased widget_Button = new ClassDescViewBased("android.widget.Button",widget_TextView);
                addClassDescViewBase(widget_Button);

                    ClassDesc_widget_CompoundButton widget_CompoundButton = new ClassDesc_widget_CompoundButton(widget_Button);
                    addClassDescViewBase(widget_CompoundButton);


    }

    private void init_view_ViewGroup_subClasses(ClassDesc_widget_ViewGroup view_ViewGroup)
    {
        // AbsoluteLayout y su derivada (WebView) no tienen atributos

        ClassDescViewBased widget_AdapterView = new ClassDescViewBased("android.widget.AdapterView",view_ViewGroup); // AdapterView no tiene atributos
        addClassDescViewBase(widget_AdapterView);

            ClassDesc_widget_AbsListView widget_AbsListView = new ClassDesc_widget_AbsListView(widget_AdapterView);
            addClassDescViewBase(widget_AbsListView);

                ClassDesc_widget_GridView widget_GridView = new ClassDesc_widget_GridView(widget_AbsListView);
                addClassDescViewBase(widget_GridView);

                ClassDesc_widget_ListView widget_ListView = new ClassDesc_widget_ListView(widget_AbsListView);
                addClassDescViewBase(widget_ListView);

                ClassDesc_widget_ExpandableListView widget_ExListView = new ClassDesc_widget_ExpandableListView(widget_ListView);
                addClassDescViewBase(widget_ExListView);

            ClassDesc_widget_AbsSpinner widget_AbsSpinner = new ClassDesc_widget_AbsSpinner(widget_AdapterView);
            addClassDescViewBase(widget_AbsSpinner);

                ClassDesc_widget_Gallery widget_Gallery = new ClassDesc_widget_Gallery(widget_AbsSpinner);
                addClassDescViewBase(widget_Gallery);

                ClassDesc_widget_Spinner widget_Spinner = new ClassDesc_widget_Spinner(widget_AbsSpinner);
                addClassDescViewBase(widget_Spinner);

            ClassDesc_widget_AdapterViewAnimator widget_AdapterViewAnimator = new ClassDesc_widget_AdapterViewAnimator(widget_AdapterView);
            addClassDescViewBase(widget_AdapterViewAnimator);

                ClassDesc_widget_AdapterViewFlipper widget_AdapterViewFlipper = new ClassDesc_widget_AdapterViewFlipper(widget_AdapterViewAnimator);
                addClassDescViewBase(widget_AdapterViewFlipper);

                // StackView no tiene atributos

        ClassDesc_widget_FrameLayout widget_FrameLayout = new ClassDesc_widget_FrameLayout(view_ViewGroup);
        addClassDescViewBase(widget_FrameLayout);

        ClassDesc_widget_GridLayout widget_GridLayout = new ClassDesc_widget_GridLayout(view_ViewGroup);
        addClassDescViewBase(widget_GridLayout);

        ClassDesc_widget_LinearLayout widget_LinearLayout = new ClassDesc_widget_LinearLayout(view_ViewGroup);
        addClassDescViewBase(widget_LinearLayout);

        ClassDesc_widget_RelativeLayout widget_RelativeLayout = new ClassDesc_widget_RelativeLayout(view_ViewGroup);
        addClassDescViewBase(widget_RelativeLayout);

        // android.support.v4.widget.DrawerLayout no tiene atributos y es clase final
    }


    private void addClassDescViewBase(ClassDescViewBased viewDesc)
    {
        classes.put(viewDesc.getClassName(), viewDesc);
    }

    public static Class<View> resolveViewClass(String viewName) throws ClassNotFoundException
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

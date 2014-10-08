package org.itsnat.droid.impl.xmlinflater;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescUnknown;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_gesture_GestureOverlayView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_view_View;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_view_ViewGroup;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_view_ViewStub;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsSeekBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AbsSpinner;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AdapterViewAnimator;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AdapterViewFlipper;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AnalogClock;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_AutoCompleteTextView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_CalendarView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_CheckedTextView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Chronometer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_CompoundButton;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_DatePicker;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ExpandableListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_FrameLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Gallery;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_GridLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_GridView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_HorizontalScrollView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ImageView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_LinearLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ListView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ProgressBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RadioGroup;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RatingBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_RelativeLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ScrollView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_SearchView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_SeekBar;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_SlidingDrawer;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Spinner;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Switch;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_TabWidget;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_TableLayout;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_TextView;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ToggleButton;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ViewAnimator;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_ViewFlipper;

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

    public XMLLayoutInflateService getXMLLayoutInflateService()
    {
        return parent;
    }

    private void initClassDesc()
    {
        ClassDesc_view_View view_View = new ClassDesc_view_View(this);
        addClassDescViewBase(view_View);

            ClassDesc_widget_AnalogClock widget_AnalogClock = new ClassDesc_widget_AnalogClock(this,view_View);
            addClassDescViewBase(widget_AnalogClock);

            ClassDesc_widget_ImageView widget_ImageView = new ClassDesc_widget_ImageView(this,view_View);
            addClassDescViewBase(widget_ImageView);
                // android.widget.ImageButton no tiene atributos
                    // android.widget.ZoomButton no tiene atributos

            // android.support.v7.app.MediaRouteButton no tiene atributos

            ClassDesc_widget_ProgressBar widget_ProgressBar = new ClassDesc_widget_ProgressBar(this,view_View);
            addClassDescViewBase(widget_ProgressBar);
                ClassDesc_widget_AbsSeekBar widget_AbsSeekBar = new ClassDesc_widget_AbsSeekBar(this,widget_ProgressBar);
                addClassDescViewBase(widget_AbsSeekBar);

                    ClassDesc_widget_RatingBar widget_RatingBar = new ClassDesc_widget_RatingBar(this,widget_AbsSeekBar);
                    addClassDescViewBase(widget_RatingBar);
                    ClassDesc_widget_SeekBar widget_SeekBar = new ClassDesc_widget_SeekBar(this,widget_AbsSeekBar);
                    addClassDescViewBase(widget_SeekBar);

                // android.support.v4.widget.ContentLoadingProgressBar no tiene atributos

            // android.support.v7.widget.Space no tiene atributos
            // android.view.SurfaceView no tiene atributos
                // android.opengl.GLSurfaceView no tiene atributos
                // android.widget.VideoView no tiene atributos

            ClassDesc_widget_TextView widget_TextView = new ClassDesc_widget_TextView(this,view_View);
            addClassDescViewBase(widget_TextView);

                ClassDescViewBased widget_Button = new ClassDescViewBased(this,"android.widget.Button",widget_TextView); // no tiene atributos
                addClassDescViewBase(widget_Button);

                    ClassDesc_widget_CompoundButton widget_CompoundButton = new ClassDesc_widget_CompoundButton(this,widget_Button);
                    addClassDescViewBase(widget_CompoundButton);

                        // CheckBox no tiene atributos
                        // RadioButton no tiene atributos y se testea indirectamente en RadioGroup

                        ClassDesc_widget_Switch widget_Switch = new ClassDesc_widget_Switch(this,widget_CompoundButton);
                        addClassDescViewBase(widget_Switch);
                        ClassDesc_widget_ToggleButton widget_ToggleButton = new ClassDesc_widget_ToggleButton(this,widget_CompoundButton);
                        addClassDescViewBase(widget_ToggleButton);

                ClassDesc_widget_CheckedTextView widget_CheckedTextView = new ClassDesc_widget_CheckedTextView(this,widget_TextView);
                addClassDescViewBase(widget_CheckedTextView);

                ClassDesc_widget_Chronometer widget_Chronometer = new ClassDesc_widget_Chronometer(this,widget_TextView);
                addClassDescViewBase(widget_Chronometer);

                // android.widget.DigitalClock no tiene atributos

                ClassDescViewBased widget_EditText = new ClassDescViewBased(this,"android.widget.EditText",widget_TextView); // no tiene atributos
                addClassDescViewBase(widget_EditText);

                    ClassDesc_widget_AutoCompleteTextView widget_AutoCompleteTextView = new ClassDesc_widget_AutoCompleteTextView(this,widget_EditText);
                    addClassDescViewBase(widget_AutoCompleteTextView);





            // 	android.view.TextureView no tiene atributos

            ClassDesc_view_ViewGroup view_ViewGroup = new ClassDesc_view_ViewGroup(this,view_View);
            addClassDescViewBase(view_ViewGroup);
            init_view_ViewGroup_subClasses(view_ViewGroup);

            ClassDesc_view_ViewStub view_ViewStub = new ClassDesc_view_ViewStub(this,view_View);
            addClassDescViewBase(view_ViewStub);
    }

    private void init_view_ViewGroup_subClasses(ClassDesc_view_ViewGroup view_ViewGroup)
    {
        // AbsoluteLayout y su derivada (WebView) no tienen atributos

        ClassDescViewBased widget_AdapterView = new ClassDescViewBased(this,"android.widget.AdapterView",view_ViewGroup); // AdapterView no tiene atributos
        addClassDescViewBase(widget_AdapterView);

            ClassDesc_widget_AbsListView widget_AbsListView = new ClassDesc_widget_AbsListView(this,widget_AdapterView);
            addClassDescViewBase(widget_AbsListView);

                ClassDesc_widget_GridView widget_GridView = new ClassDesc_widget_GridView(this,widget_AbsListView);
                addClassDescViewBase(widget_GridView);

                ClassDesc_widget_ListView widget_ListView = new ClassDesc_widget_ListView(this,widget_AbsListView);
                addClassDescViewBase(widget_ListView);

                ClassDesc_widget_ExpandableListView widget_ExListView = new ClassDesc_widget_ExpandableListView(this,widget_ListView);
                addClassDescViewBase(widget_ExListView);

            ClassDesc_widget_AbsSpinner widget_AbsSpinner = new ClassDesc_widget_AbsSpinner(this,widget_AdapterView);
            addClassDescViewBase(widget_AbsSpinner);

                ClassDesc_widget_Gallery widget_Gallery = new ClassDesc_widget_Gallery(this,widget_AbsSpinner);
                addClassDescViewBase(widget_Gallery);

                ClassDesc_widget_Spinner widget_Spinner = new ClassDesc_widget_Spinner(this,widget_AbsSpinner);
                addClassDescViewBase(widget_Spinner);

            ClassDesc_widget_AdapterViewAnimator widget_AdapterViewAnimator = new ClassDesc_widget_AdapterViewAnimator(this,widget_AdapterView);
            addClassDescViewBase(widget_AdapterViewAnimator);

                ClassDesc_widget_AdapterViewFlipper widget_AdapterViewFlipper = new ClassDesc_widget_AdapterViewFlipper(this,widget_AdapterViewAnimator);
                addClassDescViewBase(widget_AdapterViewFlipper);

                // StackView no tiene atributos

        // android.support.v4.widget.DrawerLayout no tiene atributos

        // 	android.app.FragmentBreadCrumbs no tiene atributos

        ClassDesc_widget_FrameLayout widget_FrameLayout = new ClassDesc_widget_FrameLayout(this,view_ViewGroup);
        addClassDescViewBase(widget_FrameLayout);
            // android.appwidget.AppWidgetHostView no tiene atributos

            ClassDesc_widget_CalendarView widget_CalendarView = new ClassDesc_widget_CalendarView(this,widget_FrameLayout);
            addClassDescViewBase(widget_CalendarView);

            ClassDesc_widget_DatePicker widget_DatePicker = new ClassDesc_widget_DatePicker(this,widget_FrameLayout);
            addClassDescViewBase(widget_DatePicker);

            ClassDesc_gesture_GestureOverlayView widget_GestureOverlayView = new ClassDesc_gesture_GestureOverlayView(this,widget_FrameLayout);
            addClassDescViewBase(widget_GestureOverlayView);

            ClassDesc_widget_HorizontalScrollView widget_HorizontalScrollView = new ClassDesc_widget_HorizontalScrollView(this,widget_FrameLayout);
            addClassDescViewBase(widget_HorizontalScrollView);

            // android.widget.MediaController no tiene atributos

            ClassDesc_widget_ScrollView widget_ScrollView = new ClassDesc_widget_ScrollView(this,widget_FrameLayout);
            addClassDescViewBase(widget_ScrollView);

            // android.widget.TabHost no tiene atributos
                // android.support.v13.app.FragmentTabHost no tiene atributos
            // android.widget.TimePicker no tiene atributos

            ClassDesc_widget_ViewAnimator widget_ViewAnimator = new ClassDesc_widget_ViewAnimator(this,widget_FrameLayout);
            addClassDescViewBase(widget_ViewAnimator);

                ClassDesc_widget_ViewFlipper widget_ViewFlipper = new ClassDesc_widget_ViewFlipper(this,widget_ViewAnimator);
                addClassDescViewBase(widget_ViewFlipper);

                // android.widget.ViewSwitcher no tiene atributos
                    // android.widget.ImageSwitcher no tiene atributos
                    // android.widget.TextSwitcher no tiene atributos

        ClassDesc_widget_GridLayout widget_GridLayout = new ClassDesc_widget_GridLayout(this,view_ViewGroup);
        addClassDescViewBase(widget_GridLayout);

        ClassDesc_widget_LinearLayout widget_LinearLayout = new ClassDesc_widget_LinearLayout(this,view_ViewGroup);
        addClassDescViewBase(widget_LinearLayout);

            // android.widget.NumberPicker no tiene atributos
            ClassDesc_widget_RadioGroup widget_RadioGroup = new ClassDesc_widget_RadioGroup(this,widget_LinearLayout);
            addClassDescViewBase(widget_RadioGroup);

            ClassDesc_widget_SearchView widget_SearchView = new ClassDesc_widget_SearchView(this,widget_LinearLayout);
            addClassDescViewBase(widget_SearchView);

            ClassDesc_widget_TabWidget widget_TabWidget = new ClassDesc_widget_TabWidget(this,widget_LinearLayout);
            addClassDescViewBase(widget_TabWidget);

            ClassDesc_widget_TableLayout widget_TableLayout = new ClassDesc_widget_TableLayout(this,widget_LinearLayout);
            addClassDescViewBase(widget_TableLayout);

            // android.widget.ZoomControls no tiene atributos

        // android.support.v4.view.PagerTitleStrip no tiene atributos
            // 	android.support.v4.view.PagerTabStrip no tiene atributos

        ClassDesc_widget_RelativeLayout widget_RelativeLayout = new ClassDesc_widget_RelativeLayout(this,view_ViewGroup);
        addClassDescViewBase(widget_RelativeLayout);
            // android.widget.DialerFilter no tiene atributos
            // android.widget.TwoLineListItem no tiene atributos

        ClassDesc_widget_SlidingDrawer widget_SlidingDrawer = new ClassDesc_widget_SlidingDrawer(this,view_ViewGroup);
        addClassDescViewBase(widget_SlidingDrawer);

        // android.support.v4.widget.SlidingPaneLayout no tiene atributos
        // android.support.v4.widget.SwipeRefreshLayout no tiene atributos
        // android.support.v4.view.ViewPager no tiene atributos

    }


    private void addClassDescViewBase(ClassDescViewBased viewDesc)
    {
        ClassDescViewBased old = classes.put(viewDesc.getClassName(), viewDesc);
        if (old != null) throw new ItsNatDroidException("Internal Error, duplicated: " + viewDesc.getClassName());
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
        ClassDescUnknown classDesc = new ClassDescUnknown(this,className,parent);

        classes.put(viewClass.getName(), classDesc);

        return classDesc;
    }
}

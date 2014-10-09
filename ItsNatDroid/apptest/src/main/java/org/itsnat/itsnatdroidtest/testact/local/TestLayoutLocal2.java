package org.itsnat.itsnatdroidtest.testact.local;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewStub;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;
import org.itsnat.itsnatdroidtest.testact.util.CustomScrollView;

import java.io.InputStream;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutLocal2 extends TestLayoutLocalBase
{
    public TestLayoutLocal2(TestActivityTabFragment fragment)
    {
        super(fragment);
    }

    public void test()
    {
        final TestActivity act = fragment.getTestActivity();
        final View compiledRootView = loadCompiledAndBindBackReloadButtons(R.layout.test_local_layout_compiled_2);

        final View buttonReload = compiledRootView.findViewById(R.id.buttonReload);
        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InputStream input = act.getResources().openRawResource(R.raw.test_local_layout_dynamic_2);

                InflatedLayout layout = loadDynamicAndBindBackReloadButtons(input);
                View dynamicRootView = layout.getRootView();

                initialConfiguration(act, dynamicRootView);

                TestLocalXMLInflate2.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        initialConfiguration(act, compiledRootView);
    }

    private static void initialConfiguration(TestActivity act, View rootView)
    {
        CalendarView calendarView = (CalendarView)rootView.findViewById(R.id.calendarViewTestId);
        DatePicker datePicker = (DatePicker)rootView.findViewById(R.id.datePickerTestId);
        if ("sdk".equals( Build.PRODUCT ) || "sdk_x86".equals( Build.PRODUCT ))
        {
            // La aceleración hardware hace caer el emulador 4.0.3 en estos componentes
            // http://kevsaidwhat.blogspot.com.es/2012/09/intel-atom-emulator-crashes-with.html
            calendarView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            datePicker.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        else
        {
            // Esto es sólo por jugar para asegurar que hay un calendarView y un datePicker en estos tests
            calendarView.getLayerType();
            datePicker.getLayerType();
        }

        defineTabHost(act,rootView);
        inflateViewStub(act,rootView);
    }

    private static void defineTabHost(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        TabHost tabHost = (TabHost)rootView.findViewById(R.id.tabHostTest);
        tabHost.setup();

        setNewTab(act, tabHost, "tab1", "Tab 1", android.R.drawable.star_on, R.id.tab1);
        setNewTab(act, tabHost, "tab2", "Tab 2", android.R.drawable.star_on, R.id.tab2);
        setNewTab(act, tabHost, "tab3", "Tab 3", android.R.drawable.star_on, R.id.tab3);

        //tabHost.setCurrentTabByTag("tab2"); //-- optional to set a tab programmatically.
    }

    private static void setNewTab(Context context,final TabHost tabHost, String tag, String title, int icon,final int contentID)
    {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(title, context.getResources().getDrawable(icon));
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    private static void inflateViewStub(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        ViewStub stub = (ViewStub)rootView.findViewById(R.id.viewStubTestId);
        LinearLayout inflated = (LinearLayout)stub.inflate();
    }


}

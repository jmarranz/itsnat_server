package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;

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

                defineInitalData(act,dynamicRootView);

                TestLocalXMLInflate2.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        defineInitalData(act,compiledRootView);
    }

    private static void defineInitalData(TestActivity act,View rootView)
    {
        defineAdapterViewAnimator(act, rootView);
    }



    private static void defineAdapterViewAnimator(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        AdapterViewFlipper viewFlipper = (AdapterViewFlipper)rootView.findViewById(R.id.adapterViewAnimatorTestId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(act,
                R.array.sports_array, android.R.layout.simple_list_item_1);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewFlipper.setAdapter(adapter);
    }

}

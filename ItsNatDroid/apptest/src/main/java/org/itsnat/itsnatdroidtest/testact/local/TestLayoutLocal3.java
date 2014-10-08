package org.itsnat.itsnatdroidtest.testact.local;

import android.view.View;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;
import org.itsnat.itsnatdroidtest.testact.util.CustomScrollView;

import java.io.InputStream;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutLocal3 extends TestLayoutLocalBase
{
    public TestLayoutLocal3(TestActivityTabFragment fragment)
    {
        super(fragment);
    }

    public void test()
    {
        final TestActivity act = fragment.getTestActivity();
        final View compiledRootView = loadCompiledAndBindBackReloadButtons(R.layout.test_local_layout_compiled_3);

        final View buttonReload = compiledRootView.findViewById(R.id.buttonReload);
        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InputStream input = act.getResources().openRawResource(R.raw.test_local_layout_dynamic_3);

                InflatedLayout layout = loadDynamicAndBindBackReloadButtons(input);
                View dynamicRootView = layout.getRootView();

                initialConfiguration(act, dynamicRootView);

                TestLocalXMLInflate3.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        initialConfiguration(act, compiledRootView);
    }

    private static void initialConfiguration(TestActivity act, View rootView)
    {

    }


}

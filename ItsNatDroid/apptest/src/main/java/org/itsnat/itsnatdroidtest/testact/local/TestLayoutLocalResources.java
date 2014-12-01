package org.itsnat.itsnatdroidtest.testact.local;

import android.graphics.drawable.ClipDrawable;
import android.view.View;
import android.widget.TextView;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;
import org.itsnat.itsnatdroidtest.testact.util.CustomScrollView;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutLocalResources extends TestLayoutLocalBase
{
    public TestLayoutLocalResources(TestActivityTabFragment fragment)
    {
        super(fragment);
    }

    public void test()
    {
        final TestActivity act = fragment.getTestActivity();
        final View compiledRootView = loadCompiledAndBindBackReloadButtons(R.layout.test_local_layout_compiled_resources);

        final View buttonReload = compiledRootView.findViewById(R.id.buttonReload);
        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InflatedLayout layout = loadDynamicAndBindBackReloadButtons(R.raw.test_local_layout_dynamic_resources);
                View dynamicRootView = layout.getRootView();

                initialConfiguration(act, dynamicRootView);

                TestLocalXMLInflateResources.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        initialConfiguration(act, compiledRootView);
    }

    private static void initialConfiguration(TestActivity act, View rootView)
    {
        TextView testClipDrawable = (TextView)rootView.findViewById(R.id.testClipDrawableId);
        ((ClipDrawable)testClipDrawable.getBackground()).setLevel(5000); // La mitad se verá

        TextView testClipDrawable2 = (TextView)rootView.findViewById(R.id.testClipDrawableId2);
        ((ClipDrawable)testClipDrawable2.getBackground()).setLevel(5000); // La mitad se verá
    }


}

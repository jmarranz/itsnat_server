package org.itsnat.itsnatdroidtest.testact.local;

import android.view.View;
import android.widget.Toast;

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
        final View compiledRootView = act.getLayoutInflater().inflate(R.layout.test_local_layout_compiled_2, null);
        changeLayout(fragment,compiledRootView);

        Toast.makeText(act, "OK COMPILED", Toast.LENGTH_SHORT).show();

        bindBackButton(compiledRootView);

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

                //defineInitalData(act,dynamicRootView);

                TestLocalXMLInflate2.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        //defineInitalData(act,compiledRootView);
    }

}

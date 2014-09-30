package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

                defineInitalData(act,dynamicRootView);

                TestLocalXMLInflate2.test((CustomScrollView) compiledRootView, (CustomScrollView) dynamicRootView);
            }
        });

        defineInitalData(act,compiledRootView);
    }

    private static void defineInitalData(TestActivity act,View rootView)
    {
        defineSpinnerDialog(act, rootView);
        defineSpinnerDropdown(act, rootView);
        defineAdapterViewAnimator(act, rootView);
    }

    private static void defineSpinnerDialog(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        Spinner gridView = (Spinner)rootView.findViewById(R.id.spinnerDialogTestId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(act,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Aunque parece que es para dropdown sirve para dialog también y queda mejor que sin definir
        gridView.setAdapter(adapter);
    }

    private static void defineSpinnerDropdown(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        Spinner gridView = (Spinner)rootView.findViewById(R.id.spinnerDropdownTestId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(act,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridView.setAdapter(adapter);
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

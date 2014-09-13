package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.droid.Page;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutLocal
{
    public static void test(final TestActivityTabFragment fragment)
    {
        final TestActivity act = fragment.getTestActivity();
        final View compiledRootView = act.getLayoutInflater().inflate(R.layout.test_local_layout_compiled, null);
        changeLayout(fragment,compiledRootView);

        Toast.makeText(act, "OK COMPILED", Toast.LENGTH_SHORT).show();

        View backButton = compiledRootView.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragment.gotoLayoutIndex();
            }
        });

        final View buttonReload = compiledRootView.findViewById(R.id.buttonReload);
        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InputStream input = act.getResources().openRawResource(R.raw.test_local_layout_dynamic);
                // XmlResourceParser input = act.getResources().getXml(R.xml.test_local_layout_dynamic); devuelve un XMLBlock parser que no funciona igual que un parser normal y falla

                // Sólo para testear carga local
                InflateRequest inflateRequest = ItsNatDroidRoot.get().createInflateRequest();
                InflatedLayout layout = inflateRequest.setAttrCustomInflaterListener(new AttrCustomInflaterListener()
                {
                    @Override
                    public void setAttribute(Page page, View view, String namespace, String name, String value)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
                    }

                    @Override
                    public void removeAttribute(Page page, View view, String namespace, String name)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
                    }
                }).setContext(act).inflate(new InputStreamReader(input));

                View dynamicRootView = layout.getRootView();
                changeLayout(fragment,dynamicRootView);

                Toast.makeText(act, "OK XML DYNAMIC", Toast.LENGTH_SHORT).show();

                View backButton = dynamicRootView.findViewById(R.id.back);
                backButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        fragment.gotoLayoutIndex();
                    }
                });

                View buttonReload = dynamicRootView.findViewById(R.id.buttonReload);
                if (buttonReload == null) throw new RuntimeException("FAIL");

                buttonReload.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        test(fragment);
                    }
                });

                // Test findViewByXMLId
                View textViewTest1 = dynamicRootView.findViewById(R.id.textViewTest1);
                if (textViewTest1 == null) throw new RuntimeException("FAIL");
                if (layout.findViewByXMLId("textViewTest1") != textViewTest1)
                    throw new RuntimeException("FAIL");

                defineGridView(act,dynamicRootView);
                defineExpandableListView(act,dynamicRootView);
                defineSpinnerDialog(act,dynamicRootView);
                defineSpinnerDropdown(act,dynamicRootView);

//System.out.println("DEFAULT VALUE: " + dynamicRootView.getDrawingCacheQuality());

                TestLocalXMLInflate.test((ScrollView) compiledRootView, (ScrollView) dynamicRootView);
            }
        });

        defineGridView(act,compiledRootView);
        defineExpandableListView(act,compiledRootView);
        defineSpinnerDialog(act,compiledRootView);
        defineSpinnerDropdown(act,compiledRootView);
    }

    private static void defineGridView(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        GridView gridView = (GridView)rootView.findViewById(R.id.gridViewTestId);
        CharSequence[] entries = res.getTextArray(R.array.sports_array);
        ((GridView)gridView).setAdapter(new ArrayAdapter<CharSequence>(act, android.R.layout.simple_list_item_1, entries));
    }

    private static void defineSpinnerDialog(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        Spinner gridView = (Spinner)rootView.findViewById(R.id.spinnerDialogTestId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(act,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Aunque parece que es para dropdown sirve para dialog también y queda mejor que sin definir
        ((Spinner)gridView).setAdapter(adapter);
    }

    private static void defineSpinnerDropdown(TestActivity act,View rootView)
    {
        Resources res = act.getResources();
        Spinner gridView = (Spinner)rootView.findViewById(R.id.spinnerDropdownTestId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(act,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)gridView).setAdapter(adapter);
    }

    private static void defineExpandableListView(TestActivity act,View rootView)
    {
        // http://stackoverflow.com/questions/17636735/expandable-listview-in-fragment

        Resources res = act.getResources();
        ExpandableListView listView = (ExpandableListView)rootView.findViewById(R.id.expanListViewTestId);

        final int NUM_GROUPS = 10;

        final String NAME = "NAME";
        final String IS_EVEN = "IS_EVEN";

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        for (int i = 0; i < NUM_GROUPS; i++) // 10 grupos
        {
            Map<String, String> curGroupMap = new HashMap<String, String>(); // Grupo
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, "Group " + i);
            // Comentamos el segundo texto del item de grupo porque simple_expandable_list_item_1 sólo tiene text1
            //curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd"); // No se muestra porque
        }

        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < NUM_GROUPS; i++) // 10 grupos
        {
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < 2; j++)
            {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, "Child " + i + " " + j);
                curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even" : "This child is odd");
            }
            childData.add(children);
        }

        // Set up our adapter
        SimpleExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(
                act,
                groupData,
                android.R.layout.simple_expandable_list_item_1, // https://github.com/android/platform_frameworks_base/blob/master/core/res/res/layout/simple_expandable_list_item_1.xml
                new String[] { NAME }, //new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1 }, // new int[] { android.R.id.text1, android.R.id.text2 },
                childData,
                android.R.layout.simple_expandable_list_item_2, // https://github.com/android/platform_frameworks_base/blob/master/core/res/res/layout/simple_expandable_list_item_2.xml
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );

        ((ExpandableListView)listView).setAdapter(mAdapter);
    }

    private static void changeLayout(TestActivityTabFragment fragment,View rootView)
    {
        fragment.setRootView(rootView);
        fragment.updateFragmentLayout();
    }
}

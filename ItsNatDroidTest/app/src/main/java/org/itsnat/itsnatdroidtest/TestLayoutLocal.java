package org.itsnat.itsnatdroidtest;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidRoot;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestLayoutLocal
{
    protected static void test(final MainActivity act) {

        final View compiledView = act.getLayoutInflater().inflate(R.layout.test_local_layout_compiled,null);
        act.setContentView(compiledView); // No pasamos directamente el id porque necesitamos la View para testear
        Toast.makeText(act, "OK COMPILED", Toast.LENGTH_SHORT).show();

        View backButton = act.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                act.setMainLayout();
            }
        });

        final View buttonTest = act.findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TEST de carga dinámica de layout guardado localmente
                InputStream input = act.getResources().openRawResource(R.raw.test_local_layout_dynamic);
                // Sólo para testear carga local
                InflateRequest inflateRequest = ItsNatDroidRoot.get().createInflateRequest();
                InflatedLayout layout = inflateRequest.setAttrCustomInflaterListener(new AttrCustomInflaterListener()
                {
                    @Override
                    public void setAttribute(View view, String namespace, String name, String value)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
                    }

                    @Override
                    public void removeAttribute(View view, String namespace, String name)
                    {
                        System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
                    }
                }).setContext(act).inflate(new InputStreamReader(input));

                View rootView = layout.getRootView();
                act.setContentView(rootView);
                Toast.makeText(act, "OK XML LOCAL", Toast.LENGTH_SHORT).show();

                View backButton = act.findViewById(R.id.back);
                backButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        act.setMainLayout();
                    }
                });

                View buttonTest = act.findViewById(R.id.buttonTest);
                if (buttonTest == null) throw new RuntimeException("FAIL");

                if (layout.findViewByXMLId("buttonTest") != buttonTest)
                    throw new RuntimeException("FAIL");

                buttonTest.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        test(act);
                    }
                });

                TestXMLInflate.test((RelativeLayout) compiledView, (RelativeLayout) rootView);
            }
        });

    }
}

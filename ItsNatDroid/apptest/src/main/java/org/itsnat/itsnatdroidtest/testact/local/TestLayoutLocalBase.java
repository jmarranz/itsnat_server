package org.itsnat.itsnatdroidtest.testact.local;

import android.view.View;
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

/**
 * Created by jmarranz on 16/07/14.
 */
public abstract class TestLayoutLocalBase implements AttrCustomInflaterListener
{
    protected final TestActivityTabFragment fragment;

    public TestLayoutLocalBase(final TestActivityTabFragment fragment)
    {
        this.fragment = fragment;
    }

    protected TestActivity getTestActivity()
    {
        return fragment.getTestActivity();
    }


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

    protected InflatedLayout loadDynamicAndBindBackReloadButtons(InputStream input)
    {
        // Sólo para testear carga local
        TestActivity act = getTestActivity();

        InflateRequest inflateRequest = ItsNatDroidRoot.get().createInflateRequest();
        InflatedLayout layout = inflateRequest.setAttrCustomInflaterListener(this)
                .setContext(act)
                .inflate(new InputStreamReader(input));

        View dynamicRootView = layout.getRootView();
        changeLayout(fragment, dynamicRootView);

        Toast.makeText(act, "OK XML DYNAMIC", Toast.LENGTH_SHORT).show();

        bindBackButton(dynamicRootView);
        bindReloadButtonDynamic(dynamicRootView);

        return layout;
    }

    protected void bindBackButton(View rootView)
    {
        View backButton = rootView.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragment.gotoLayoutIndex();
            }
        });
    }

    protected void bindReloadButtonDynamic(View dynamicRootView)
    {
        View buttonReload = dynamicRootView.findViewById(R.id.buttonReload);
        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                test();
            }
        });
    }

    protected static void changeLayout(TestActivityTabFragment fragment,View rootView)
    {
        fragment.setRootView(rootView);
        fragment.updateFragmentLayout();
    }

    public abstract void test();
}

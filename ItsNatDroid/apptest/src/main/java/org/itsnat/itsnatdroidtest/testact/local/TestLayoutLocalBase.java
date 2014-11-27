package org.itsnat.itsnatdroidtest.testact.local;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.InflateLayoutRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.droid.Page;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;

import java.io.InputStream;

/**
 * Created by jmarranz on 16/07/14.
 */
public abstract class TestLayoutLocalBase implements AttrLayoutInflaterListener,AttrDrawableInflaterListener
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
        System.out.println("NOT FOUND LAYOUT ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
    }

    @Override
    public void removeAttribute(Page page, View view, String namespace, String name)
    {
        System.out.println("NOT FOUND LAYOUT ATTRIBUTE (removeAttribute): " + namespace + " " + name);
    }

    @Override
    public void setAttribute(Page page, Drawable obj, String namespace, String name, String value)
    {
        System.out.println("NOT FOUND Drawable ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
    }

    @Override
    public void removeAttribute(Page page, Drawable obj, String namespace, String name)
    {
        System.out.println("NOT FOUND Drawable ATTRIBUTE (removeAttribute): " + namespace + " " + name);
    }

    protected View loadCompiledAndBindBackReloadButtons(int layoutId)
    {
        TestActivity act = getTestActivity();
        View compiledRootView = act.getLayoutInflater().inflate(layoutId, null);
        changeLayout(fragment, compiledRootView);

        Toast.makeText(act, "OK COMPILED", Toast.LENGTH_SHORT).show();

        bindBackButton(compiledRootView);

        return compiledRootView;
    }

    protected InflatedLayout loadDynamicAndBindBackReloadButtons(InputStream input)
    {
        // Sólo para testear carga local
        TestActivity act = getTestActivity();

        InflateLayoutRequest inflateRequest = ItsNatDroidRoot.get().createInflateLayoutRequest();
        InflatedLayout layout = inflateRequest
                .setEncoding("UTF-8")
                .setAttrLayoutInflaterListener(this)
                .setAttrDrawableInflaterListener(this)
                .setContext(act)
                .inflate(input);

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

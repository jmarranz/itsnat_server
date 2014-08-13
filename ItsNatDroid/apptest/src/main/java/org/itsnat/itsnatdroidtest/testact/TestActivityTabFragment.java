package org.itsnat.itsnatdroidtest.testact;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.local.TestLayoutLocal;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemoteControl;
import org.itsnat.itsnatdroidtest.testact.remote.TestRemoteCore;

/**
 * Created by jmarranz on 12/08/14.
 */
public class TestActivityTabFragment extends Fragment
{
    protected View rootView;
    protected int sectionNumber;
    public boolean changed = false;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TestActivityTabFragment newInstance(int sectionNumber) {
        TestActivityTabFragment fragment = new TestActivityTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public int getSectionNumber()
    {
        return sectionNumber;
    }

    public void setRootView(View rootView)
    {
        this.rootView = rootView;
    }

    public TestActivity getTestActivity()
    {
        return (TestActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) return rootView;

        Bundle bundle = getArguments();
        this.sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

        this.rootView = inflater.inflate(R.layout.fragment_index, container, false);



        View testLocal = rootView.findViewById(R.id.testLocal);
        testLocal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TestLayoutLocal.test(TestActivityTabFragment.this);
            }
        });

        final TestActivity act = getTestActivity();

        View testRemoteCore = rootView.findViewById(R.id.testRemoteCore);
        testRemoteCore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestCore();
                TestRemoteCore test = new TestRemoteCore(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });


        View testRemoteControl = rootView.findViewById(R.id.testRemoteControl);
        testRemoteControl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String url = act.getUrlTestRemCtrl();
                TestRemoteControl test = new TestRemoteControl(TestActivityTabFragment.this, act.getItsNatDroidBrowser());
                test.test(url);
            }
        });


        return rootView;
    }

    public void gotoLayoutIndex()
    {
        this.rootView = null;
        updateFragmentLayout();
    }

    public void updateFragmentLayout()
    {
        this.changed = true;
        TestActivityPagerAdapter pagerAdapter = getTestActivity().getTestActivityPagerAdapter();
        pagerAdapter.notifyDataSetChanged(); // Provoca la llamada FragmentPagerAdapter.getItemPosition(Object) para cada fragmento

        //act.getViewPager().invalidate();
        //act.getViewPager().destroyDrawingCache();
        //act.getViewPager().forceLayout();
        //act.getViewPager().requestLayout();
    }
}

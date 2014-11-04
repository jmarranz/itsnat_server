package org.itsnat.itsnatdroidtest.testact;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidRoot;
import org.itsnat.itsnatdroidtest.R;


public class TestActivity extends Activity implements ActionBar.TabListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    protected TestActivityPagerAdapter mTestActivityPagerAdapter;
    protected ViewPager mViewPager;
    protected ItsNatDroidBrowser droidBrowser;
    protected String urlTestCore;
    protected String urlTestRemResources;
    protected String urlTestRemCtrl;
    protected String urlTestStatelessCore;
    protected String urlTestComponents;
    protected String urlTestRemoteNoItsNat;

    //protected String urlTestCoreAttachServerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ItsNatDroidRoot.get() == null)
            ItsNatDroidRoot.init(getApplication());
        this.droidBrowser = ItsNatDroidRoot.get().createItsNatDroidBrowser();

        Intent intent = getIntent();

        String urlTestBase = intent.getStringExtra("urlTestBase");

        String itsNatServlet = "ItsNatDroidServletExample";

        this.urlTestCore =           urlTestBase + itsNatServlet + "?itsnat_doc_name=test_droid_core";
        this.urlTestRemResources =   urlTestBase + itsNatServlet + "?itsnat_doc_name=test_droid_remote_resources";
        this.urlTestRemCtrl =        urlTestBase + itsNatServlet + "?itsnat_doc_name=test_droid_remote_ctrl";
        this.urlTestStatelessCore =  urlTestBase + itsNatServlet + "?itsnat_doc_name=test_droid_stateless_core_initial";
        this.urlTestComponents =     urlTestBase + itsNatServlet + "?itsnat_doc_name=test_droid_components";
        this.urlTestRemoteNoItsNat = urlTestBase + "ItsNatDroidServletNoItsNat";

        getActionBar().setDisplayHomeAsUpEnabled(true); // Muestra y activa el simbolito del back

        setContentView(R.layout.activity_test);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mTestActivityPagerAdapter = new TestActivityPagerAdapter(getFragmentManager(),getResources());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTestActivityPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mTestActivityPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mTestActivityPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        //org.itsnat.droid.impl.util.MapLightAndRealPerformTest.test();
    }

    public ViewPager getViewPager()
    {
        return mViewPager;
    }

    public TestActivityPagerAdapter getTestActivityPagerAdapter()
    {
        return mTestActivityPagerAdapter;
    }


    public ItsNatDroidBrowser getItsNatDroidBrowser()
    {
        return droidBrowser;
    }

    public String getUrlTestCore()
    {
        return urlTestCore;
    }

    public String getUrlTestRemoteResources()
    {
        return urlTestRemResources;
    }

    public String getUrlTestRemCtrl()
    {
        return urlTestRemCtrl;
    }

    public String getUrlTestStatelessCore()
    {
        return urlTestStatelessCore;
    }

    public String getUrlTestComponents() { return urlTestComponents; }

    public String getUrlTestRemoteNoItsNat() { return urlTestRemoteNoItsNat; }


    //public String getUrlTestCoreAttachServerLauncher() { return urlTestCoreAttachServerLauncher; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void clickHandler(View view)
    {
        Toast.makeText(this,"Executed onClick handler",Toast.LENGTH_SHORT).show();
    }
}

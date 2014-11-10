package org.itsnat.itsnatdroidtest.testact.remote;

import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.EventMonitor;
import org.itsnat.droid.ItsNatDoc;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.event.Event;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestRemoteCore extends TestRemotePageBase
{
    public TestRemoteCore(final TestActivityTabFragment fragment,final ItsNatDroidBrowser droidBrowser)
    {
        super(fragment,droidBrowser);
    }

    public void test(String url)
    {
        HttpParams httpParams = prepareLoad();

        TestActivity act = getTestActivity();

        boolean testSSLSelfSignedAllowed = false;
        if (testSSLSelfSignedAllowed)
        {
            droidBrowser.setSSLSelfSignedAllowed(true);
            url = "https://www.pcwebshop.co.uk"; // Alternativa: https://mms.nw.ru
        }

        boolean testSyncRequests = false;
        if (testSyncRequests)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(act)
        .setSynchronous(testSyncRequests)
        .setOnPageLoadListener(this)
        .setOnPageLoadErrorListener(this)
        .setAttrLayoutInflaterListener(this)
        .setAttrDrawableInflaterListener(this)
        .setHttpParams(httpParams)
        .setURL(url)
        .execute();
    }

    @Override
    public void onPageLoad(final Page page)
    {
        super.onPageLoad(page);

        if (page.getId() == null)
            return;

        final TestActivity act = getTestActivity();

        ItsNatDoc itsNatDoc = page.getItsNatDoc();

        View testNativeListenersButton = itsNatDoc.findViewByXMLId("testNativeListenersId");
        ItsNatView testNativeListenersButtonItsNat = itsNatDoc.getItsNatView(testNativeListenersButton);
        testNativeListenersButtonItsNat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(act, "OK Click Native", Toast.LENGTH_SHORT).show();
            }
        });
        testNativeListenersButtonItsNat.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                Toast.makeText(act, "OK Touch Native, action:" + motionEvent.getAction(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        itsNatDoc.addEventMonitor(new EventMonitor()
        {
            @Override
            public void before(Event event)
            {
                Log.v("TestActivity", "Evt Monitor: before");
            }

            @Override
            public void after(Event event, boolean timeout)
            {
                Log.v("TestActivity", "Evt Monitor: after, timeout: " + timeout);
            }
        });

    }

}

package org.itsnat.itsnatdroidtest.testact.remote;

import android.view.View;

import org.apache.http.params.HttpParams;
import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestRemotePage extends TestRemotePageBase
{
    public TestRemotePage(final TestActivityTabFragment fragment, final ItsNatDroidBrowser droidBrowser)
    {
        super(fragment,droidBrowser);
    }

    public void test(String url)
    {
        HttpParams httpParams = prepareLoad();

        TestActivity act = getTestActivity();

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(act)
        .setOnPageLoadListener(this)
        .setOnPageLoadErrorListener(this)
        .setAttrCustomInflaterListener(new AttrCustomInflaterListener()
        {
            @Override
            public void setAttribute(final Page page,View view, String namespace, String name, final String value)
            {
               System.out.println("NOT FOUND ATTRIBUTE: " + namespace + " " + name + " " + value);
            }

            @Override
            public void removeAttribute(Page page,View view, String namespace, String name)
            {
                System.out.println("NOT FOUND ATTRIBUTE (removeAttribute): " + namespace + " " + name);
            }
        })
        .setHttpParams(httpParams)
        .setURL(url)
        .execute();
    }
}

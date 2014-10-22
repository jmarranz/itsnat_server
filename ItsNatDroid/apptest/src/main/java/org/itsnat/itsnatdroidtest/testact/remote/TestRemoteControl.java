package org.itsnat.itsnatdroidtest.testact.remote;

import android.view.View;

import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatView;
import org.itsnat.droid.Page;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;

/**
 * Created by jmarranz on 16/07/14.
 */
public class TestRemoteControl extends TestRemotePageBase
{
    public TestRemoteControl(final TestActivityTabFragment fragment,final ItsNatDroidBrowser droidBrowser)
    {
        super(fragment,droidBrowser);
    }

    public void test(String url)
    {
        executePageRequest(url);
    }

    @Override
    public void setAttribute(final Page page,View view, String namespace, String name, final String value)
    {
        if (name.equals("url"))
        {
            ItsNatView itsNatView = page.getItsNatDoc().getItsNatView(view);
            itsNatView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    page.dispose();
                    page.reusePageRequest().setURL(value).execute();
                }
            });
        }
        else super.setAttribute(page,view, namespace, name, value);
    }

}

package org.itsnat.droid.impl.browser;

import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatSession;
import org.itsnat.droid.Page;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 10/06/14.
 */
public class ItsNatSessionImpl implements ItsNatSession
{
    protected ItsNatDroidBrowserImpl browser;
    protected String stdSessionId;
    protected String id;
    protected String token;
    protected Map<String,PageImpl> pageMap = new HashMap<String, PageImpl>();
    protected List<Page> pageList = new LinkedList<Page>();

    public ItsNatSessionImpl(ItsNatDroidBrowserImpl browser,String stdSessionId,String token,String id)
    {
        this.browser = browser;
        this.stdSessionId = stdSessionId;
        this.token = token;
        this.id = id;
    }

    public ItsNatDroidBrowser getItsNatDroidBrowser()
    {
        return browser;
    }

    public String getStandardSessionId()
    {
        return stdSessionId;
    }

    public String getId()
    {
        return id;
    }

    public String getToken()
    {
        return token;
    }

    public int getPageCount()
    {
        return pageMap.size();
    }

    public List<Page> getPageList()
    {
        return Collections.unmodifiableList(pageList);
    }

    public void registerPage(PageImpl page)
    {
        PageImpl prev = pageMap.put(page.getId(), page);
        if (prev != null && prev != page) throw new ItsNatDroidException("Unexpected");
        pageList.add(page);
        removeExcess();
    }

    private void removeExcess()
    {
        int max = browser.getMaxPagesInSession();
        if (max < 0) return; // Ilimitado
        int size = pageList.size();
        for(int i = 0; i < (size - max); i++)
        {
            PageImpl page = (PageImpl)pageList.get(0);
            disposePage(page);
        }
    }

    public void disposePage(PageImpl page)
    {
        pageMap.remove(page.getId());
        pageList.remove(page);
    }
}

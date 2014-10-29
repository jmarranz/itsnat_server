package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.parser.LayoutParser;
import org.itsnat.droid.impl.parser.LayoutParserPage;
import org.itsnat.droid.impl.parser.LayoutParserStandalone;
import org.itsnat.droid.impl.parser.TreeViewParsed;
import org.itsnat.droid.impl.parser.TreeViewParsedCache;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.xmlinflater.page.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.xmlinflater.stdalone.InflatedLayoutStandaloneImpl;

import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateRequestImpl implements InflateRequest
{
    protected ItsNatDroidImpl itsNatDroid;
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;

    public InflateRequestImpl(ItsNatDroidImpl itsNatDroid)
    {
        this.itsNatDroid = itsNatDroid;
    }

    @Override
    public InflateRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return itsNatDroid;
    }

    @Override
    public InflateRequest setAttrCustomInflaterListener(AttrCustomInflaterListener inflateListener)
    {
        this.inflateListener = inflateListener;
        return this;
    }

    public AttrCustomInflaterListener getAttrCustomInflaterListener()
    {
        return inflateListener;
    }

    public Context getContext()
    {
        return ctx;
    }

    @Override
    public InflatedLayout inflate(Reader input)
    {
        String markup = IOUtil.read(input);
        return inflateInternal(markup,null,null,null);
    }

    public InflatedLayoutImpl inflateInternal(String markup,String[] loadScript,List<String> scriptList,PageImpl page)
    {
        TreeViewParsed treeViewParsed;

        TreeViewParsedCache treeViewParsedCache = getItsNatDroidImpl().getXMLLayoutInflateService().getTreeViewParsedCache();
        TreeViewParsed cachedTreeView = treeViewParsedCache.get(markup);
        if (cachedTreeView != null)
            treeViewParsed = cachedTreeView;
        else
        {
            boolean loadingPage = loadScript != null;
            LayoutParser layoutParser = page != null ? new LayoutParserPage(page.getItsNatServerVersion(), loadingPage) : new LayoutParserStandalone();
            treeViewParsed = layoutParser.inflate(markup);
            treeViewParsedCache.put(markup,treeViewParsed);
        }

        return inflateInternal(treeViewParsed,loadScript,scriptList,page);
    }

    public InflatedLayoutImpl inflateInternal(TreeViewParsed treeViewParsed,String[] loadScript,List<String> scriptList,PageImpl page)
    {
        InflatedLayoutImpl inflatedLayout = page != null ? new InflatedLayoutPageImpl(page,treeViewParsed,inflateListener,ctx) :
                                                           new InflatedLayoutStandaloneImpl(itsNatDroid,treeViewParsed,inflateListener,ctx);
        inflatedLayout.inflate(loadScript,scriptList);
        return inflatedLayout;
    }

}

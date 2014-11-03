package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflateRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.model.XMLParsedCache;
import org.itsnat.droid.impl.parser.layout.LayoutParser;
import org.itsnat.droid.impl.parser.layout.LayoutParserPage;
import org.itsnat.droid.impl.parser.layout.LayoutParserStandalone;
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
        LayoutParsed layoutParsed;

        XMLParsedCache<LayoutParsed> layoutParsedCache = getItsNatDroidImpl().getXMLLayoutInflateService().getLayoutParsedCache();
        LayoutParsed cachedLayout = layoutParsedCache.get(markup);
        if (cachedLayout != null)
            layoutParsed = cachedLayout;
        else
        {
            boolean loadingPage = loadScript != null;
            LayoutParser layoutParser = page != null ? new LayoutParserPage(page.getItsNatServerVersion(), loadingPage) : new LayoutParserStandalone();
            layoutParsed = layoutParser.parse(markup);
            layoutParsedCache.put(markup, layoutParsed);
        }

        return inflateInternal(layoutParsed,loadScript,scriptList,page);
    }

    public InflatedLayoutImpl inflateInternal(LayoutParsed layoutParsed,String[] loadScript,List<String> scriptList,PageImpl page)
    {
        InflatedLayoutImpl inflatedLayout = page != null ? new InflatedLayoutPageImpl(page, layoutParsed,inflateListener,ctx) :
                                                           new InflatedLayoutStandaloneImpl(itsNatDroid, layoutParsed,inflateListener,ctx);
        inflatedLayout.inflate(loadScript,scriptList);
        return inflatedLayout;
    }

}

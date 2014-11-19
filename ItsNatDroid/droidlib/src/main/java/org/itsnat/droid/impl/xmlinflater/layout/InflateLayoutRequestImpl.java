package org.itsnat.droid.impl.xmlinflater.layout;

import android.content.Context;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.InflateLayoutRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.layout.LayoutParsed;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflated.layout.page.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.xmlinflated.layout.stdalone.InflatedLayoutStandaloneImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;

import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateLayoutRequestImpl implements InflateLayoutRequest
{
    protected ItsNatDroidImpl itsNatDroid;
    protected Context ctx;
    protected AttrLayoutInflaterListener inflateLayoutListener;
    protected AttrDrawableInflaterListener inflateDrawableListener;

    public InflateLayoutRequestImpl(ItsNatDroidImpl itsNatDroid)
    {
        this.itsNatDroid = itsNatDroid;
    }

    @Override
    public InflateLayoutRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return itsNatDroid;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return inflateLayoutListener;
    }

    @Override
    public InflateLayoutRequest setAttrLayoutInflaterListener(AttrLayoutInflaterListener inflateLayoutListener)
    {
        this.inflateLayoutListener = inflateLayoutListener;
        return this;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return inflateDrawableListener;
    }

    @Override
    public InflateLayoutRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener inflateDrawableListener)
    {
        this.inflateDrawableListener = inflateDrawableListener;
        return this;
    }





    public Context getContext()
    {
        return ctx;
    }

    @Override
    public InflatedLayout inflate(Reader input)
    {
        String markup = IOUtil.read(input);
        return inflateLayoutStandalone(markup);
    }

    private InflatedLayoutImpl inflateLayoutStandalone(String markup)
    {
        XMLInflateRegistry xmlInflateRegistry = getItsNatDroidImpl().getXMLInflateRegistry();

        boolean loadingPage = true;
        String itsNatServerVersion = null;
        boolean remotePageOrFrag = false;
        LayoutParsed layoutParsed = xmlInflateRegistry.getLayoutParsedCache(markup,itsNatServerVersion,loadingPage,remotePageOrFrag);

        return inflateLayoutInternal(layoutParsed, null, null, null);
    }

    public InflatedLayoutImpl inflateLayoutInternal(LayoutParsed layoutParsed, String[] loadScript, List<String> scriptList, PageImpl page)
    {
        InflatedLayoutImpl inflatedLayout = page != null ? new InflatedLayoutPageImpl(page, layoutParsed, ctx) :
                                                           new InflatedLayoutStandaloneImpl(itsNatDroid, layoutParsed, inflateLayoutListener,ctx);
        XMLInflaterLayout xmlInflater = XMLInflaterLayout.createXMLInflatedLayout(inflatedLayout,ctx);
        inflatedLayout.setXMLInflaterLayout(xmlInflater); // Se necesita después para la inserción de fragments, cambio de atributos etc
        xmlInflater.inflateLayout(loadScript, scriptList);
        return inflatedLayout;
    }

}

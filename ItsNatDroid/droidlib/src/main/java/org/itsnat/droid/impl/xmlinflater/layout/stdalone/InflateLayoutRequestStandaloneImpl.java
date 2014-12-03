package org.itsnat.droid.impl.xmlinflater.layout.stdalone;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.DisplayMetrics;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.InflateLayoutRequest;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutStandaloneImpl;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.layout.InflateLayoutRequestImpl;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateLayoutRequestStandaloneImpl extends InflateLayoutRequestImpl implements InflateLayoutRequest
{
    protected Context ctx;
    protected String encoding = "UTF-8";
    protected int referenceDensity = DisplayMetrics.DENSITY_XHIGH; // 320 (xhdpi), por ej el Nexus 4
    protected AttrLayoutInflaterListener attrLayoutInflaterListener;
    protected AttrDrawableInflaterListener attrDrawableInflaterListener;

    public InflateLayoutRequestStandaloneImpl(ItsNatDroidImpl itsNatDroid)
    {
        super(itsNatDroid);
    }

    @Override
    public InflateLayoutRequest setContext(Context ctx)
    {
        this.ctx = ctx;
        return this;
    }

    @Override
    public InflateLayoutRequest setEncoding(String encoding)
    {
        this.encoding = encoding;
        return this;
    }

    @Override
    public String getEncoding()
    {
        return encoding;
    }

    @Override
    public InflateLayoutRequest setReferenceDensity(int referenceDensity)
    {
        this.referenceDensity = referenceDensity;
        return this;
    }

    public int getReferenceDensity()
    {
        return referenceDensity;
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return itsNatDroid;
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return attrLayoutInflaterListener;
    }

    @Override
    public InflateLayoutRequest setAttrLayoutInflaterListener(AttrLayoutInflaterListener inflateLayoutListener)
    {
        this.attrLayoutInflaterListener = inflateLayoutListener;
        return this;
    }

    public AttrDrawableInflaterListener getAttrDrawableInflaterListener()
    {
        return attrDrawableInflaterListener;
    }

    @Override
    public InflateLayoutRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener attrDrawableInflaterListener)
    {
        this.attrDrawableInflaterListener = attrDrawableInflaterListener;
        return this;
    }

    public Context getContext()
    {
        return ctx;
    }

    @Override
    public InflatedLayout inflate(InputStream input)
    {
        String markup = IOUtil.read(input,encoding);
        return inflateLayoutStandalone(markup);
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
        AssetManager assetManager = getContext().getResources().getAssets();
        XMLDOMLayout domLayout = xmlInflateRegistry.getXMLDOMLayoutCache(markup, itsNatServerVersion, loadingPage, remotePageOrFrag,assetManager);

        return inflateLayoutInternal(domLayout, null, null, null);
    }

    public InflatedLayoutImpl inflateLayoutInternal(XMLDOMLayout domLayout, String[] loadScript, List<String> scriptList, PageImpl page)
    {
        InflatedLayoutImpl inflatedLayout = page != null ? new InflatedLayoutPageImpl(itsNatDroid, domLayout,ctx) :
                                                           new InflatedLayoutStandaloneImpl(itsNatDroid, domLayout, ctx);
        XMLInflaterLayout xmlInflater = XMLInflaterLayout.createXMLInflaterLayout(inflatedLayout,referenceDensity, attrLayoutInflaterListener,attrDrawableInflaterListener, ctx, page);
        xmlInflater.inflateLayout(loadScript, scriptList);
        return inflatedLayout;
    }

}

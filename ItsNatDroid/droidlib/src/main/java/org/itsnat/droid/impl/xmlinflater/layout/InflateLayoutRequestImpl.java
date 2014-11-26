package org.itsnat.droid.impl.xmlinflater.layout;

import android.content.Context;
import android.content.res.AssetManager;

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

import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 5/06/14.
 */
public class InflateLayoutRequestImpl implements InflateLayoutRequest
{
    protected ItsNatDroidImpl itsNatDroid;
    protected Context ctx;
    protected String encoding;
    protected AttrLayoutInflaterListener attrLayoutInflaterListener;
    protected AttrDrawableInflaterListener attrDrawableInflaterListener;

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

    @Override
    public InflateLayoutRequest setEncoding(String encoding)
    {
        this.encoding = encoding;
        return this;
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
    public InflateLayoutRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener inflateDrawableListener)
    {
        this.attrDrawableInflaterListener = inflateDrawableListener;
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
        AssetManager assetManager = getContext().getResources().getAssets();
        XMLDOMLayout domLayout = xmlInflateRegistry.getXMLDOMLayoutCache(markup, itsNatServerVersion, loadingPage, remotePageOrFrag,assetManager);

        return inflateLayoutInternal(domLayout, null, null, null);
    }

    public InflatedLayoutImpl inflateLayoutInternal(XMLDOMLayout domLayout, String[] loadScript, List<String> scriptList, PageImpl page)
    {
        InflatedLayoutImpl inflatedLayout = page != null ? new InflatedLayoutPageImpl(itsNatDroid, domLayout,ctx) :
                                                           new InflatedLayoutStandaloneImpl(itsNatDroid, domLayout, ctx);
        XMLInflaterLayout xmlInflater = XMLInflaterLayout.createXMLInflaterLayout(inflatedLayout, attrLayoutInflaterListener,attrDrawableInflaterListener, ctx, page);
        inflatedLayout.setXMLInflaterLayout(xmlInflater); // Se necesita después para la inserción de fragments, cambio de atributos etc
        xmlInflater.inflateLayout(loadScript, scriptList);
        return inflatedLayout;
    }

}

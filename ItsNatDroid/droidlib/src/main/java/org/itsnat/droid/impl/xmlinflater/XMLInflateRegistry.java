package org.itsnat.droid.impl.xmlinflater;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.model.XMLParsedCache;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.parser.drawable.DrawableParser;
import org.itsnat.droid.impl.parser.layout.LayoutParser;
import org.itsnat.droid.impl.parser.layout.LayoutParserPage;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 25/06/14.
 */
public class XMLInflateRegistry
{
    protected ItsNatDroidImpl parent;
    private int sNextGeneratedId = 1; // No usamos AtomicInteger porque no lo usaremos en multihilo
    protected Map<String,Integer> newIdMap = new HashMap<String,Integer>();
    protected ClassDescViewMgr classDescViewMgr = new ClassDescViewMgr(this);
    protected ClassDescDrawableMgr classDescDrawableMgr = new ClassDescDrawableMgr(this);
    protected XMLParsedCache<LayoutParsed> layoutParsedCache = new XMLParsedCache<LayoutParsed>();
    protected XMLParsedCache<DrawableParsed> drawableParsedCache = new XMLParsedCache<DrawableParsed>();

    public XMLInflateRegistry(ItsNatDroidImpl parent)
    {
        this.parent = parent;
    }

    public ClassDescViewMgr getClassDescViewMgr()
    {
        return classDescViewMgr;
    }

    public ClassDescDrawableMgr getClassDescDrawableMgr()
    {
        return classDescDrawableMgr;
    }

    public XMLParsedCache<LayoutParsed> getLayoutParsedCache()
    {
        return layoutParsedCache;
    }

    public XMLParsedCache<DrawableParsed> getDrawableParsedCache()
    {
        return drawableParsedCache;
    }

    public LayoutParsed getLayoutParsedCache(String markup,String itsNatServerVersion)
    {
        // Este método DEBE ser multihilo, layoutParsedCache ya lo es.
        // No pasa nada si por una rarísima casualidad dos Layout idénticos hacen put, quedará el último, ten en cuenta que esto
        // es un caché
        LayoutParsed cachedLayout = layoutParsedCache.get(markup);
        if (cachedLayout != null) return cachedLayout;
        else
        {
            boolean loadingPage = true;
            LayoutParser layoutParser = new LayoutParserPage(itsNatServerVersion, loadingPage);
            LayoutParsed layoutParsed = layoutParser.parse(markup);
            layoutParsedCache.put(markup, layoutParsed);
            return layoutParsed;
        }
    }

    public DrawableParsed getDrawableParsedCache(String markup)
    {
        // Ver notas de getLayoutParsedCache()
        DrawableParsed cachedDrawable = drawableParsedCache.get(markup);
        if (cachedDrawable != null) return cachedDrawable;
        else
        {
            DrawableParsed drawableParsed = DrawableParser.parse(markup);
            drawableParsedCache.put(markup, drawableParsed);
            return drawableParsed;
        }
    }

    public int generateViewId()
    {
        // Inspirado en el código fuente de Android View.generateViewId()
        final int result = sNextGeneratedId;
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
        // No usamos compareAndSet porque no se debe usar en multihilo
        this.sNextGeneratedId = newValue;
        return result;
    }

    public int findIdAddIfNecessary(String name)
    {
        int id = findId(name);
        if (id == 0)
            id = addNewId(name);
        return id;
    }

    public int findId(String name)
    {
        Integer res = newIdMap.get(name);
        if (res == null)
            return 0; // No existe
        return res;
    }

    private int addNewId(String name)
    {
        int newId = generateViewId();
        newIdMap.put(name,newId);
        return newId;
    }
}


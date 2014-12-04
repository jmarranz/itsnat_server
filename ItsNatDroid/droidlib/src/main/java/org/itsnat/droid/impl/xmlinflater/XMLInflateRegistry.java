package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.ViewGroup;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrDynamic;
import org.itsnat.droid.impl.dom.DOMAttrLocalResource;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.dom.XMLDOMCache;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.domparser.drawable.XMLDOMDrawableParser;
import org.itsnat.droid.impl.domparser.layout.XMLDOMLayoutParser;
import org.itsnat.droid.impl.domparser.layout.XMLDOMLayoutParserFragment;
import org.itsnat.droid.impl.domparser.layout.XMLDOMLayoutParserPage;
import org.itsnat.droid.impl.domparser.layout.XMLDOMLayoutParserStandalone;
import org.itsnat.droid.impl.util.MimeUtil;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawableStandalone;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.DrawableUtil;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.Dimension;

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
    protected XMLDOMCache<XMLDOMLayout> domLayoutCache = new XMLDOMCache<XMLDOMLayout>();
    protected XMLDOMCache<XMLDOMDrawable> domDrawableCache = new XMLDOMCache<XMLDOMDrawable>();

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

    public XMLDOMLayout getXMLDOMLayoutCache(String markup, String itsNatServerVersion, boolean loadingPage, boolean remotePageOrFrag,AssetManager assetManager)
    {
        // Este método DEBE ser multihilo, el objeto domLayoutCache ya lo es.
        // No pasa nada si por una rarísima casualidad dos Layout idénticos hacen put, quedará el último, ten en cuenta que esto
        // es un caché.

        // Extraemos el markup sin el script de carga porque dos páginas generadas "iguales" SIEMPRE serán diferentes a nivel
        // de markup en el loadScript porque el id cambia y algún token aleatorio, sin el loadScript podemos conseguir
        // muchos más aciertos de cacheo y acelerar un montón al tener el parseo ya hecho.
        // Si el template no es generado por ItsNat server o bien el scripting está desactivado (itsNatServerVersion puede
        // ser no null pues es un header), loadScript será null y no pasa nada markupNoLoadScript[0] es el markup original
        String[] markupNoLoadScript = new String[1];
        String loadScript = null;
        if (itsNatServerVersion != null && loadingPage)
            loadScript = XMLDOMLayout.extractLoadScriptMarkup(markup, markupNoLoadScript);
        else
            markupNoLoadScript[0] = markup;

        XMLDOMLayout cachedDOMLayout = domLayoutCache.get(markupNoLoadScript[0]);
        if (cachedDOMLayout != null)
        {
            // Recuerda que cachedLayout tiene el timestamp actualizado por el hecho de llamar al get()
        }
        else
        {
            XMLDOMLayoutParser layoutParser;
            if (remotePageOrFrag)
                layoutParser = loadingPage ? new XMLDOMLayoutParserPage(this,assetManager,itsNatServerVersion) :
                                             new XMLDOMLayoutParserFragment(this,assetManager);
            else
                layoutParser = new XMLDOMLayoutParserStandalone(this,assetManager);

            cachedDOMLayout = layoutParser.parse(markup);
            cachedDOMLayout.setLoadScript(null); // Que quede claro que no se puede utilizar
            domLayoutCache.put(markupNoLoadScript[0], cachedDOMLayout);
        }

        XMLDOMLayout cloned = cachedDOMLayout.partialClone(); // No devolvemos nunca el que cacheamos "por si acaso"
        cloned.setLoadScript(loadScript);
        return cloned;
    }

    public XMLDOMDrawable getXMLDOMDrawableCache(String markup,AssetManager assetManager)
    {
        // Ver notas de getXMLDOMLayoutCache()
        XMLDOMDrawable cachedDrawable = domDrawableCache.get(markup);
        if (cachedDrawable != null) return cachedDrawable;
        else
        {
            XMLDOMDrawable xmlDOMDrawable = XMLDOMDrawableParser.parse(markup,this,assetManager);
            domDrawableCache.put(markup, xmlDOMDrawable);
            return xmlDOMDrawable;
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

    public int getIdentifierAddIfNecessary(String value, Context ctx)
    {
        // Procesamos aquí los casos de "@+id/...", la razón es que cualquier atributo que referencie un id (más allá
        // de android:id) puede registrar un nuevo atributo lo cual es útil si el android:id como tal está después,
        // después en android:id ya no hace falta que sea "@+id/...".
        // http://stackoverflow.com/questions/11029635/android-radiogroup-checkedbutton-property
        int id = 0;
        if (value.startsWith("@+id/") || value.startsWith("@id/")) // Si fuera el caso de "@+mypackage:id/name" ese caso no lo soportamos, no lo he visto nunca aunque en teoría está sintácticamente permitido
        {
            id = getIdentifier(value, ctx, false); // Tiene prioridad el recurso de Android, pues para qué generar un id nuevo si ya existe o bien ya fue registrado dinámicamente
            if (id <= 0)
            {
                int pos = value.indexOf('/');
                String idName = value.substring(pos + 1);
                if (value.startsWith("@+id/")) id = findIdAddIfNecessary(idName);
                else id = findId(idName);
                if (id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + value + "\"");
            }
        }
        else id = getIdentifier(value, ctx);
        return id;
    }

    public int getIdentifier(String attrValue, Context ctx)
    {
        return getIdentifier(attrValue,ctx,true);
    }

    public int getIdentifier(String value, Context ctx,boolean throwErr)
    {
        if ("0".equals(value) || "-1".equals(value) || "@null".equals(value)) return 0;

        int id;
        char first = value.charAt(0);
        if (first == '?')
        {
            id = getIdentifierTheme(value, ctx);
        }
        else if (first == '@')
        {
            // En este caso es posible que se haya registrado dinámicamente el id via "@+id/..." Tiene prioridad el registro de Android que el de ItsNat, para qué generar un id si ya existe como recurso
            id = getIdentifierResource(value, ctx);
            if (id > 0)
                return id;
            id = getIdentifierDynamicallyAdded(value,ctx);
        }
        else
            throw new ItsNatDroidException("Bad format in id declaration: " + value);

        if (throwErr && id <= 0) throw new ItsNatDroidException("Not found resource with id \"" + value + "\"");
        return id;
    }

    private static int getIdentifierTheme(String value, Context ctx)
    {
        // http://stackoverflow.com/questions/12781501/android-setting-linearlayout-background-programmatically
        // Ej. android:textAppearance="?android:attr/textAppearanceMedium"
        TypedValue outValue = new TypedValue();
        ctx.getTheme().resolveAttribute(getIdentifierResource(value, ctx), outValue, true);
        return outValue.resourceId;
    }

    private static int getIdentifierResource(String value, Context ctx)
    {
        Resources res = ctx.getResources();

        value = value.substring(1); // Quitamos el @ o #
        if (value.startsWith("+id/"))
            value = value.substring(1); // Quitamos el +
        String packageName;
        if (value.indexOf(':') != -1) // Tiene package el value, ej "android:" delegamos en Resources.getIdentifier() que lo resuelva
        {
            packageName = null;
        }
        else
        {
            packageName = ctx.getPackageName(); // El package es necesario como parámetro sólo cuando no está en la string (recursos locales)
        }

        return res.getIdentifier(value, null, packageName);
    }

    public int getIdentifierDynamicallyAdded(String value, Context ctx)
    {
        if (value.indexOf(':') != -1) // Tiene package, ej "@+android:id/", no se encontrará un id registrado como "@+id/..." y los posibles casos con package NO los hemos contemplado
            return 0; // No encontrado

        value = value.substring(1); // Quitamos el @ o #
        int pos = value.indexOf('/');
        String idName = value.substring(pos + 1);

        return findId(idName);
    }

    public static boolean isResource(String attrValue)
    {
        // No hace falta hacer un trim, un espacio al ppio invalida el atributo
        return attrValue.startsWith("@") || attrValue.startsWith("?");
    }

    public int getInteger(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getInteger(resId);
        }
        else
        {
            if (attrValue.startsWith("0x"))
            {
                attrValue = attrValue.substring(2);
                return Integer.parseInt(attrValue, 16);
            }
            return Integer.parseInt(attrValue);
        }
    }

    public float getFloat(String attrValue, Context ctx)
    {
        // Ojo, para valores sin sufijo de dimensión (por ej layout_weight o alpha)
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getDimension(resId); // No hay getFloat
        }
        else return Float.parseFloat(attrValue);
    }

    public String getString(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getString(resId);
        }
        else return attrValue;
    }

    public CharSequence getText(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getText(resId);
        }
        else return attrValue;
    }

    public CharSequence[] getTextArray(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getTextArray(resId);
        }
        else return null;
    }

    public boolean getBoolean(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            return ctx.getResources().getBoolean(resId);
        }
        else return Boolean.parseBoolean(attrValue);
    }

    private static int getDimensionSuffixAsInt(String suffix)
    {
        if (suffix.equals("dp") || suffix.equals("dip")) return TypedValue.COMPLEX_UNIT_DIP;
        else if (suffix.equals("px")) return TypedValue.COMPLEX_UNIT_PX;
        else if (suffix.equals("sp")) return TypedValue.COMPLEX_UNIT_SP;
        else if (suffix.equals("in")) return TypedValue.COMPLEX_UNIT_IN;
        else if (suffix.equals("mm")) return TypedValue.COMPLEX_UNIT_MM;
        else throw new ItsNatDroidException("Internal error");
    }

    private static String getDimensionSuffix(String value)
    {
        String valueTrim = value.trim();

        if (valueTrim.endsWith("dp")) return "dp";
        if (valueTrim.endsWith("dip")) // Concesión al pasado
            return "dip";
        else if (valueTrim.endsWith("px")) return "px";
        else if (valueTrim.endsWith("sp")) return "sp";
        else if (valueTrim.endsWith("in")) return "in";
        else if (valueTrim.endsWith("mm")) return "mm";
        else throw new ItsNatDroidException("ERROR unrecognized dimension: " + valueTrim);
    }

    private static float extractFloat(String value, String suffix)
    {
        int pos = value.lastIndexOf(suffix);
        value = value.substring(0, pos);
        return Float.parseFloat(value);
    }

    public Dimension getDimensionObject(String attrValue, Context ctx)
    {
        // El retorno es en px
        Resources res = ctx.getResources();
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            float num = res.getDimension(resId);
            return new Dimension(TypedValue.COMPLEX_UNIT_PX, num);
        }
        else
        {
            String valueTrim = attrValue.trim();
            String suffix = getDimensionSuffix(valueTrim);
            int complexUnit = getDimensionSuffixAsInt(suffix);
            float num = extractFloat(valueTrim, suffix);
            return new Dimension(complexUnit, num);
        }
    }

    public int getDimensionInt(String attrValue, Context ctx)
    {
        //return (int)getDimensionFloat(attrValue,ctx);
        return Math.round(getDimensionFloat(attrValue, ctx));
    }

    public float getDimensionFloat(String attrValue, Context ctx)
    {
        // El retorno es en px
        Resources res = ctx.getResources();

        Dimension dimen = getDimensionObject(attrValue, ctx);
        int unit = dimen.getComplexUnit();
        float num = dimen.getValue();
        switch (unit)
        {
            case TypedValue.COMPLEX_UNIT_DIP:
                return ValueUtil.dpToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_PX:
                return num;
            case TypedValue.COMPLEX_UNIT_SP:
                return ValueUtil.spToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_IN:
                return ValueUtil.inToPixel(num, res);
            case TypedValue.COMPLEX_UNIT_MM:
                return ValueUtil.mmToPixel(num, res);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue); // POR AHORA hay que ver si faltan más casos
    }

    protected int getDimensionWithNameInt(String value, Context ctx)
    {
        int dimension;

        // No hace falta hacer trim en caso de "match_parent" etc un espacio fastidia el attr
        if ("fill_parent".equals(value)) dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("match_parent".equals(value)) dimension = ViewGroup.LayoutParams.MATCH_PARENT;
        else if ("wrap_content".equals(value)) dimension = ViewGroup.LayoutParams.WRAP_CONTENT;
        else
        {
            dimension = getDimensionInt(value, ctx);
        }
        return dimension;
    }

    public int getColor(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue,ctx);
            return ctx.getResources().getColor(resId);
        }
        else if (attrValue.startsWith("#")) // Color literal. No hace falta hacer trim
        {
            return Color.parseColor(attrValue);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue);
    }

    public Drawable getDrawable(String attrValue, Context ctx)
    {
        if (isResource(attrValue))
        {
            int resId = getIdentifier(attrValue, ctx);
            if (resId <= 0) return null;
            return ctx.getResources().getDrawable(resId);
        }
        else if (attrValue.startsWith("#")) // Color literal. No hace falta hacer trim
        {
            int color = Color.parseColor(attrValue);
            return new ColorDrawable(color);
        }

        throw new ItsNatDroidException("Cannot process " + attrValue);
    }

    public Drawable getDrawable(DOMAttr attr, Context ctx,XMLInflater xmlInflater)
    {
        if (attr instanceof DOMAttrDynamic)
        {
            PageImpl page = null;
            if (xmlInflater instanceof XMLInflaterPage)
                page = ((XMLInflaterPage)xmlInflater).getPageImpl();

            if (attr instanceof DOMAttrRemote && page == null) throw new ItsNatDroidException("Unexpected"); // Si es remote hay page por medio

            ItsNatDroidImpl itsNatDroid = xmlInflater.getInflatedXML().getItsNatDroidImpl();
            int bitmapDensityReference = xmlInflater.getBitmapDensityReference();
            AttrLayoutInflaterListener attrLayoutInflaterListener = xmlInflater.getAttrLayoutInflaterListener();
            AttrDrawableInflaterListener attrDrawableInflaterListener = xmlInflater.getAttrDrawableInflaterListener();

            DOMAttrDynamic attrDyn = (DOMAttrDynamic)attr;
            String resourceMime = attrDyn.getResourceMime();
            if (MimeUtil.MIME_XML.equals(resourceMime))
            {
                // Esperamos un drawable no una animación
                XMLDOMDrawable xmlDOMDrawable = (XMLDOMDrawable) attrDyn.getResource();
                InflatedDrawable inflatedDrawable = page != null ? new InflatedDrawablePage(itsNatDroid, xmlDOMDrawable, ctx) : new InflatedDrawableStandalone(itsNatDroid, xmlDOMDrawable, ctx);
                XMLInflaterDrawable xmlInflaterDrawable = XMLInflaterDrawable.createXMLInflaterDrawable(inflatedDrawable,bitmapDensityReference,attrLayoutInflaterListener, attrDrawableInflaterListener, ctx, page);
                return xmlInflaterDrawable.inflateDrawable();
            }
            else if (MimeUtil.isMIMEImage(resourceMime))
            {
                byte[] byteArray = (byte[])attrDyn.getResource();
                boolean expectedNinePatch = attrDyn.isNinePatch();
                return DrawableUtil.createImageBasedDrawable(byteArray,bitmapDensityReference,expectedNinePatch,ctx.getResources());
            }
            else throw new ItsNatDroidException("Unsupported resource mime: " + resourceMime);
        }
        else if (attr instanceof DOMAttrLocalResource)
        {
            String attrValue = attr.getValue();
            return getDrawable(attrValue,ctx);
        }
        else throw new ItsNatDroidException("Internal Error");
    }

}


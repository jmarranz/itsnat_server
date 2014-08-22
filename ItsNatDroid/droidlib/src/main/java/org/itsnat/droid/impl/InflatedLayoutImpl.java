package org.itsnat.droid.impl;

import android.content.Context;
import android.util.Xml;
import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.util.WeakMapWithValue;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.XMLLayoutInflateService;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 16/06/14.
 */
public abstract class InflatedLayoutImpl implements InflatedLayout
{
    protected ItsNatDroidImpl parent;
    protected View rootView;
    protected WeakMapWithValue<String,View> mapIdViewXMLStd;
    protected Context ctx;
    protected AttrCustomInflaterListener inflateListener;
    protected MapLight<String,String> namespacesByPrefix = new MapLight<String,String>();
    protected String androidNSPrefix;

    public InflatedLayoutImpl(ItsNatDroidImpl parent,AttrCustomInflaterListener inflateListener,Context ctx)
    {
        // rootView se define a posteriori
        this.parent = parent;
        this.inflateListener = inflateListener;
        this.ctx = ctx;
    }

    public XMLLayoutInflateService getXMLLayoutInflateService()
    {
        return parent.getXMLLayoutInflateService();
    }

    public String getAndroidNSPrefix()
    {
        return androidNSPrefix;
    }

    public void addNamespace(String prefix,String ns)
    {
        namespacesByPrefix.put(prefix,ns);
        if (XMLLayoutInflateService.XMLNS_ANDROID.equals(ns))
            this.androidNSPrefix = prefix;
    }

    public MapLight<String,String> getNamespacesByPrefix()
    {
        return namespacesByPrefix;
    }

    public String getNamespace(String prefix)
    {
        return namespacesByPrefix.get(prefix);
    }

    @Override
    public ItsNatDroid getItsNatDroid()
    {
        return getItsNatDroidImpl();
    }

    public ItsNatDroidImpl getItsNatDroidImpl()
    {
        return parent;
    }

    @Override
    public View getRootView()
    {
        return rootView;
    }

    public void setRootView(View rootView)
    {
        this.rootView = rootView;
    }

    public AttrCustomInflaterListener getAttrCustomInflaterListener()
    {
        return inflateListener;
    }

    public Context getContext()
    {
        return ctx;
    }

    private WeakMapWithValue<String,View> getMapIdViewXMLStd()
    {
        if (mapIdViewXMLStd == null) mapIdViewXMLStd = new WeakMapWithValue<String,View>();
        return mapIdViewXMLStd;
    }

    public String unsetElementId(View view)
    {
        return getMapIdViewXMLStd().removeByValue(view);
    }

    public View getElementById(String id)
    {
        View viewFound = getMapIdViewXMLStd().getValueByKey(id);
        if (viewFound == null) return null;
        // Ojo, puede estar desconectado aunque el objeto Java esté "vivo"

        if (viewFound == rootView) return viewFound; // No está desconectado

        ViewParent parent = viewFound.getParent();
        while(parent != null)
        {
            if (parent == rootView) return viewFound;
            parent = parent.getParent();
        }
        // Está registrado pero sin embargo no está en el árbol de Views, podríamos eliminarlo (remove) para que no de la lata
        // pero si se vuelve a insertar perderíamos el elemento pues al reinsertar no podemos capturar la operación y definir el id,
        // tampoco es que sea demasiado importante porque el programador una vez que cambia el árbol de views por su cuenta
        // "rompe" los "contratos" de ItsNatDroid
        return null;
    }

    public String getXMLId(View view)
    {
        return getMapIdViewXMLStd().getKeyByValue(view);
    }

    public void setXMLId(String id, View view)
    {
        getMapIdViewXMLStd().put(id,view);
    }

    public View findViewByXMLId(String id)
    {
        // No llamamos a este método getElementById() porque devuelve un View no un DOM Node
        return getElementById(id);
    }

    public void inflate(Reader input,String[] loadScript,List<String> scriptList)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            inflate(parser,loadScript,scriptList);
        }
        catch (XmlPullParserException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        finally
        {
            try
            {
                input.close();
            }
            catch (IOException ex)
            {
                throw new ItsNatDroidException(ex);
            }
        }
    }

    private void inflate(XmlPullParser parser,String[] loadScript,List<String> scriptList)
    {
        try
        {
            View rootView = parseRootView(parser, loadScript,scriptList);

            setRootView(rootView);
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (XmlPullParserException ex)
        {
            throw new ItsNatDroidException(ex);
        }
    }

    private View parseRootView(XmlPullParser parser, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            int nsStart = parser.getNamespaceCount(parser.getDepth()-1);
            int nsEnd = parser.getNamespaceCount(parser.getDepth());
            for (int i = nsStart; i < nsEnd; i++)
            {
                String prefix = parser.getNamespacePrefix(i);
                String ns = parser.getNamespaceUri(i);
                addNamespace(prefix, ns);
            }

            if (getAndroidNSPrefix() == null)
                throw new ItsNatDroidException("Missing android namespace declaration in root element");

            String viewName = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            View rootView = createAndAddViewObjectAndFillAttributes(viewName,null, parser);

            View childView = parseNextView(parser, rootView, loadScript,scriptList);
            while(childView != null)
            {
                childView = parseNextView(parser, rootView, loadScript,scriptList);
            }

            return rootView;
        }

        throw new ItsNatDroidException("INTERNAL ERROR: NO ROOT VIEW");
    }

    protected abstract void parseScriptElement(XmlPullParser parser,View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException;

    public View parseNextView(XmlPullParser parser, View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            String viewName = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            if (viewName.equals("script"))
            {
                parseScriptElement(parser,viewParent,loadScript,scriptList);
            }
            else
            {
                View view = createAndAddViewObjectAndFillAttributes(viewName, viewParent, parser);

                // No funciona, sólo funciona con XML compilados:
                //AttributeSet attributes = Xml.asAttributeSet(parser);
                //LayoutInflater inf = LayoutInflater.from(ctx);
                //View currentTarget = inf.createAndAddViewObjectAndFillAttributes(viewName,null,attributes);

                View childView = parseNextView(parser, view, loadScript, scriptList);
                while (childView != null)
                {
                    childView = parseNextView(parser, view, loadScript, scriptList);
                }
                return view;
            }
        }
        return null;
    }

    public View createAndAddViewObjectAndFillAttributes(String viewName,View viewParent,XmlPullParser parser)
    {
        ClassDescViewMgr classDescViewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        Context ctx = getContext();
        int idStyle = findStyleAttribute(parser,ctx);
        View view = classDesc.createAndAddViewObject(viewParent,-1,idStyle,ctx);
        fillViewAttributes(classDesc,view, parser); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        return view;
    }

    private int findStyleAttribute(XmlPullParser parser,Context ctx)
    {
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            if (!ValueUtil.isEmpty(namespaceURI)) continue; // style no tiene namespace
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            if (!"style".equals(name)) continue;
            String value = parser.getAttributeValue(i);
            return AttrDesc.getIdentifier(value, ctx);
        }
        return 0;
    }

    private void fillViewAttributes(ClassDescViewBased classDesc,View view,XmlPullParser parser)
    {
        OneTimeAttrProcess oneTimeAttrProcess = new OneTimeAttrProcess();
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            setAttribute(classDesc,view,namespaceURI, name, value, oneTimeAttrProcess);
        }

        if (oneTimeAttrProcess.neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,String namespaceURI,String name,String value,OneTimeAttrProcess oneTimeAttrProcess)
    {
        return classDesc.setAttribute(view,namespaceURI, name, value, oneTimeAttrProcess,this);
    }
}

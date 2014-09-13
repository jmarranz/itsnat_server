package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.util.Xml;
import android.view.View;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.ItsNatDroid;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDesc_widget_Spinner;
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
    protected ViewMapByXMLId viewMapByXMLId;
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

    private ViewMapByXMLId getViewMapByXMLId()
    {
        if (viewMapByXMLId == null) viewMapByXMLId = new ViewMapByXMLId(this);
        return viewMapByXMLId;
    }

    public String unsetXMLId(View view)
    {
        return getViewMapByXMLId().unsetXMLId(view);
    }

    public String getXMLId(View view)
    {
        return getViewMapByXMLId().getXMLId(view);
    }

    public void setXMLId(String id, View view)
    {
        getViewMapByXMLId().setXMLId(id, view);
    }

    public View findViewByXMLId(String id)
    {
        if (viewMapByXMLId == null) return null;
        return viewMapByXMLId.findViewByXMLId(id);
    }

    public View inflate(Reader input,String[] loadScript,List<String> scriptList)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            return inflate(parser,loadScript,scriptList);
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

    private View inflate(XmlPullParser parser,String[] loadScript,List<String> scriptList)
    {
        try
        {
            return parseRootView(parser, loadScript,scriptList);
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

            PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

            View rootView = createRootViewObjectAndFillAttributes(viewName,parser,pending);

            processChildViews(parser,rootView,loadScript,scriptList);

            pending.executeTasks();

            return rootView;
        }

        throw new ItsNatDroidException("INTERNAL ERROR: NO ROOT VIEW");
    }

    protected abstract void parseScriptElement(XmlPullParser parser,View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException;

    protected View parseNextView(XmlPullParser parser, View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
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
                PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

                View view = createAndAddViewObjectAndFillAttributes(viewName, viewParent, parser,pending);

                // No funciona, sólo funciona con XML compilados:
                //AttributeSet attributes = Xml.asAttributeSet(parser);
                //LayoutInflater inf = LayoutInflater.from(ctx);

                processChildViews(parser,view,loadScript,scriptList);

                pending.executeTasks();

                return view;
            }
        }
        return null;
    }

    protected void processChildViews(XmlPullParser parser, View view, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
    {
        View childView = parseNextView(parser, view, loadScript, scriptList);
        while (childView != null)
        {
            childView = parseNextView(parser, view, loadScript, scriptList);
        }
    }

    private View createAndAddViewObject(ClassDescViewBased classDesc,View viewParent,XmlPullParser parser)
    {
        Context ctx = getContext();
        int idStyle = findStyleAttribute(parser, ctx);
        if (classDesc instanceof ClassDesc_widget_Spinner)
        {
            String spinnerMode = findSpinnerModeAttribute(parser, ctx);
            return ((ClassDesc_widget_Spinner)classDesc).createAndAddSpinnerObject(viewParent, -1, idStyle,spinnerMode, ctx);
        }
        else
        {
            return classDesc.createAndAddViewObject(viewParent, -1, idStyle, ctx);
        }
    }

    public View createRootViewObjectAndFillAttributes(String viewName,XmlPullParser parser,PendingPostInsertChildrenTasks pending)
    {
        ClassDescViewMgr classDescViewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        View view = createAndAddViewObject(classDesc,null,parser);
        setRootView(view); // Lo antes posible porque los inline event handlers lo necesitan
        fillViewAttributes(classDesc,view, parser,pending); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        return view;
    }

    public View createAndAddViewObjectAndFillAttributes(String viewName,View viewParent,XmlPullParser parser,PendingPostInsertChildrenTasks pending)
    {
        // viewParent es null en el caso de parseo de fragment
        ClassDescViewMgr classDescViewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        View view = createAndAddViewObject(classDesc,viewParent,parser);
        fillViewAttributes(classDesc,view, parser,pending); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        return view;
    }

    private int findStyleAttribute(XmlPullParser parser,Context ctx)
    {
        String value = findAttribute(null,"style",parser,ctx);
        if (value == null) return 0;
        return AttrDesc.getIdentifier(value, ctx);
    }

    private String findSpinnerModeAttribute(XmlPullParser parser,Context ctx)
    {
        return findAttribute(XMLLayoutInflateService.XMLNS_ANDROID,"spinnerMode",parser,ctx);
    }

    private String findAttribute(String namespaceURI,String name,XmlPullParser parser,Context ctx)
    {
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String currNamespaceURI = parser.getAttributeNamespace(i);
            if (!ValueUtil.equalsNullAllowed(currNamespaceURI,namespaceURI)) continue;
            String currName = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            if (!name.equals(currName)) continue;
            String value = parser.getAttributeValue(i);
            return value;
        }
        return null;
    }

    private void fillViewAttributes(ClassDescViewBased classDesc,View view,XmlPullParser parser,PendingPostInsertChildrenTasks pending)
    {
        OneTimeAttrProcess oneTimeAttrProcess = OneTimeAttrProcess.createOneTimeAttrProcess(view);

        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String namespaceURI = parser.getAttributeNamespace(i);
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            setAttribute(classDesc,view,namespaceURI, name, value, oneTimeAttrProcess,pending);
        }

        oneTimeAttrProcess.finish();
    }

    public boolean setAttribute(ClassDescViewBased classDesc,View view,String namespaceURI,String name,String value,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.setAttribute(view,namespaceURI, name, value, oneTimeAttrProcess,pending,this);
    }
}

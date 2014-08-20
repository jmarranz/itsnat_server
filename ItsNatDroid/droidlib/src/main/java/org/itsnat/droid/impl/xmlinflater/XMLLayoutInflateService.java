package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.util.Xml;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Created by jmarranz on 25/06/14.
 */
public class XMLLayoutInflateService
{
    public static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";

    protected ItsNatDroidImpl parent;
    protected ClassDescViewMgr classDescViewMgr = new ClassDescViewMgr(this);

    public XMLLayoutInflateService(ItsNatDroidImpl parent)
    {
        this.parent = parent;
    }

    public ClassDescViewMgr getClassDescViewMgr()
    {
        return classDescViewMgr;
    }

    public void inflate(Reader input,String[] loadScript,List<String> scriptList, InflatedLayoutImpl inflated)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            inflate(parser,loadScript,scriptList,inflated);
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

    private void inflate(XmlPullParser parser,String[] loadScript,List<String> scriptList, InflatedLayoutImpl inflated)
    {
        try
        {
            View rootView = parseRootView(parser, loadScript,scriptList, inflated);

            inflated.setRootView(rootView);
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

    private View parseRootView(XmlPullParser parser, String[] loadScript,List<String> scriptList, InflatedLayoutImpl inflated) throws IOException, XmlPullParserException
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
                inflated.addNamespace(prefix,ns);
            }

            if (inflated.getAndroidNSPrefix() == null)
                throw new ItsNatDroidException("Missing android namespace declaration in root element");

            String viewName = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            View rootView = createAndAddViewObjectAndFillAttributes(viewName,null, parser, inflated);

            View childView = parseNextView(parser, rootView, loadScript,scriptList, inflated);
            while(childView != null)
            {
                childView = parseNextView(parser, rootView, loadScript,scriptList, inflated);
            }

            return rootView;
        }

        throw new ItsNatDroidException("INTERNAL ERROR: NO ROOT VIEW");
    }

    public View parseNextView(XmlPullParser parser, View viewParent, String[] loadScript,List<String> scriptList, InflatedLayoutImpl inflated) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;

            String viewName = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            if (viewName.equals("script"))
            {
                PageImpl page = (inflated instanceof InflatedLayoutPageImpl) ? ((InflatedLayoutPageImpl)inflated).getPageImpl() : null;
                if (page != null)
                {
                    boolean isLoadScript = parser.getAttributeCount() == 1 &&
                            "id".equals(parser.getAttributeName(0)) &&
                            "itsnat_load_script".equals(parser.getAttributeValue(0));

                    while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;

                    String code = parser.getText();
                    if (isLoadScript) loadScript[0] = code;
                    else scriptList.add(code);

                    while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
                }
                else
                {
                    throw new ItsNatDroidException("<script> elements are not allowed in standalone parsing layouts");
                }

                continue;
            }


            View view = createAndAddViewObjectAndFillAttributes(viewName,viewParent, parser, inflated);

            // No funciona, sólo funciona con XML compilados:
            //AttributeSet attributes = Xml.asAttributeSet(parser);
            //LayoutInflater inf = LayoutInflater.from(ctx);
            //View currentTarget = inf.createAndAddViewObjectAndFillAttributes(viewName,null,attributes);

            View childView = parseNextView(parser, view, loadScript,scriptList, inflated);
            while(childView != null)
            {
                childView = parseNextView(parser, view, loadScript,scriptList, inflated);
            }
            return view;
        }
        return null;
    }

    public View createAndAddViewObjectAndFillAttributes(String viewName,View viewParent,XmlPullParser parser, InflatedLayoutImpl inflated)
    {
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        Context ctx = inflated.getContext();
        int idStyle = findStyleAttribute(parser,ctx);
        View view = classDesc.createAndAddViewObject(viewParent,-1,idStyle,ctx);
        fillViewAttributes(classDesc,view, parser,inflated); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        return view;
    }

    private int findStyleAttribute(XmlPullParser parser,Context ctx)
    {
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String namespace = parser.getAttributeNamespace(i);
            if (!namespace.isEmpty()) continue; // style no tiene namespace
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            if (!"style".equals(name)) continue;
            String value = parser.getAttributeValue(i);
            return AttrDesc.getIdentifier(value, ctx);
        }
        return 0;
    }

    private void fillViewAttributes(ClassDescViewBased classDesc,View view,XmlPullParser parser,InflatedLayoutImpl inflated)
    {
        OneTimeAttrProcess oneTimeAttrProcess = new OneTimeAttrProcess();
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {

            String namespace = parser.getAttributeNamespace(i);
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            classDesc.setAttribute(view,namespace, name, value, oneTimeAttrProcess,inflated);
        }

        if (oneTimeAttrProcess.neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams
    }


}

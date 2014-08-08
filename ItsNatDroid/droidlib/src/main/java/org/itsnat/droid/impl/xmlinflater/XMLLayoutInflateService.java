package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Xml;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.Page;
import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

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

    public void inflate(Reader input,String[] script, InflatedLayoutImpl inflated,PageImpl page)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);
            inflate(parser,script,inflated,page);
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

    private void inflate(XmlPullParser parser,String[] script, InflatedLayoutImpl inflated,PageImpl page)
    {
        try
        {
            View rootView = createNextView(parser,null,script,inflated,page);
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

    private View createNextView(XmlPullParser parser,View viewParent,String[] script,InflatedLayoutImpl inflated,PageImpl page) throws IOException, XmlPullParserException
    {
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) // Nodo de texto etc
                continue;
            String viewName = parser.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

            if (viewName.equals("script"))
            {
                while (parser.next() != XmlPullParser.TEXT) /*nop*/ ;
                script[0] = parser.getText();
                while (parser.next() != XmlPullParser.END_TAG) /*nop*/ ;
                continue;
            }


            View view = createAndAddViewObjectAndFillAttributes(viewName,viewParent, parser, inflated,page);

            // No funciona, sólo funciona con XML compilados:
            //AttributeSet attributes = Xml.asAttributeSet(parser);
            //LayoutInflater inf = LayoutInflater.from(ctx);
            //View currentTarget = inf.createAndAddViewObjectAndFillAttributes(viewName,null,attributes);

            View childView = createNextView(parser,view,script,inflated,page);
            while(childView != null)
            {
                childView = createNextView(parser,view,script,inflated,page);
            }
            return view;
        }
        return null;
    }

    public View createAndAddViewObjectAndFillAttributes(String viewName,View viewParent,XmlPullParser parser, InflatedLayoutImpl inflated,PageImpl page)
    {
        ClassDescViewBased classDesc = classDescViewMgr.get(viewName);
        Context ctx = inflated.getContext();
        int idStyle = findStyleAttribute(parser,ctx);
        View view = classDesc.createAndAddViewObject(viewParent,-1,idStyle,ctx);
        fillViewAttributes(classDesc,page,view, parser,inflated); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
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

    private void fillViewAttributes(ClassDescViewBased classDesc,PageImpl page,View view,XmlPullParser parser,InflatedLayoutImpl inflated)
    {
        OneTimeAttrProcess oneTimeAttrProcess = new OneTimeAttrProcess();
        for(int i = 0; i < parser.getAttributeCount(); i++)
        {
            String namespace = parser.getAttributeNamespace(i);
            String name = parser.getAttributeName(i); // El nombre devuelto no contiene el namespace
            String value = parser.getAttributeValue(i);
            classDesc.setAttribute(page,view,namespace, name, value, oneTimeAttrProcess,inflated);
        }

        if (oneTimeAttrProcess.neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams
    }
}

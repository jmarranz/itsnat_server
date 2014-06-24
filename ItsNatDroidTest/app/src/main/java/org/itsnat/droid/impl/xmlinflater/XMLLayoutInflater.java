package org.itsnat.droid.impl.xmlinflater;

import android.util.Xml;
import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBase;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewMgr;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jmarranz on 28/04/14.
 */
public class XMLLayoutInflater
{
    public static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";

    public static void inflate(InputStream input,String[] script, InflatedLayoutImpl inflated)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input, null);

            View rootView = createNextView(parser,null,script,inflated);
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

    private static View createNextView(XmlPullParser parser,View viewParent,String[] script,InflatedLayoutImpl inflated) throws IOException, XmlPullParserException
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

            ClassDescViewBase classDesc = getClassDescViewBase(viewName);
            View view = classDesc.createAndAddViewObjectAndFillAttributes(viewParent, parser, inflated);

            // No funciona, sólo funciona con XML compilados:
            //AttributeSet attributes = Xml.asAttributeSet(parser);
            //LayoutInflater inf = LayoutInflater.from(ctx);
            //View view = inf.createAndAddViewObjectAndFillAttributes(viewName,null,attributes);

            View childView = createNextView(parser,view,script,inflated);
            while(childView != null)
            {
                childView = createNextView(parser,view,script,inflated);
            }
            return view;
        }
        return null;
    }


    public static ClassDescViewBase getClassDescViewBase(String viewName)
    {
        Class<View> viewClass = null;
        try { viewClass = resolveViewClass(viewName); }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        ClassDescViewBase classDesc = ClassDescViewMgr.get(viewClass);
        return classDesc;
    }

    private static Class<View> resolveViewClass(String viewName) throws ClassNotFoundException
    {
        if (viewName.indexOf('.') == -1)
        {
            try
            {
                return resolveViewClass("android.view." + viewName);
            }
            catch (ClassNotFoundException e)
            {
                return resolveViewClass("android.widget." + viewName);
            }
        }
        else
        {
            return (Class<View>)Class.forName(viewName);
        }
    }

}

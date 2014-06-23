package org.itsnat.droid.impl.xmlinflater;

import android.content.Context;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrCustomInflaterListener;
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

    public static InflatedLayoutImpl inflate(InputStream input,String[] script, InflateRequestImpl request)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input, null);

            InflatedLayoutImpl inflated = new InflatedLayoutImpl();
            View rootView = createNextView(parser,null,script,request,inflated);
            inflated.setRootView(rootView);
            return inflated;
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (XmlPullParserException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (ClassNotFoundException ex)
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

    private static View createNextView(XmlPullParser parser,View viewParent,String[] script,InflateRequestImpl request,InflatedLayoutImpl inflated) throws IOException, XmlPullParserException, ClassNotFoundException
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

            Class<View> viewClass = resolveViewClass(viewName);

            ClassDescViewBase classDesc = ClassDescViewMgr.get(viewClass);
            View view = classDesc.createView(viewParent,parser,request,inflated);

            // No funciona, sólo funciona con XML compilados:
            //AttributeSet attributes = Xml.asAttributeSet(parser);
            //LayoutInflater inf = LayoutInflater.from(ctx);
            //View view = inf.createView(viewName,null,attributes);

            View childView = createNextView(parser,view,script,request,inflated);
            while(childView != null)
            {
                childView = createNextView(parser,view,script,request,inflated);
            }
            return view;
        }
        return null;
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

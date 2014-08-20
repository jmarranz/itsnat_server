package org.itsnat.droid.impl.browser;

import android.content.Context;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.InflatedLayoutImpl;
import org.itsnat.droid.impl.util.MapLight;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutPageImpl extends InflatedLayoutImpl
{
    protected PageImpl page;

    public InflatedLayoutPageImpl(PageImpl page,AttrCustomInflaterListener inflateListener, Context ctx)
    {
        super(page.getItsNatDroidBrowserImpl().getItsNatDroidImpl(), inflateListener, ctx);
        this.page = page;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }


    public void insertFragment(View parentView,String markup,String[] loadScript,List<String> scriptList)
    {
        if (page == null) throw new ItsNatDroidException("INTERNAL ERROR");

        // Preparamos primero el markup añadiendo un false parentView que luego quitamos, el false parentView es necesario
        // para declarar el namespace android, el false parentView será del mismo tipo que el de verdad para que los
        // LayoutParams se hagan bien

        StringBuilder newMarkup = new StringBuilder();

        newMarkup.append( "<" + parentView.getClass().getName() );

        MapLight<String,String> namespaceMap = getNamespacesByPrefix();
        for(Iterator<Map.Entry<String,String>> it = namespaceMap.getEntryList().iterator(); it.hasNext(); )
        {
            Map.Entry<String,String> entry = it.next();
            newMarkup.append( " xmlns:" + entry.getKey() + "=\"" + entry.getValue() + "\">" );
        }
        newMarkup.append( ">" );
        newMarkup.append( markup );
        newMarkup.append( "</" + parentView.getClass().getName() + ">");

        markup = newMarkup.toString();

        XmlPullParser parser = Xml.newPullParser();
        try
        {
            StringReader input = new StringReader(markup);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(input);

            ViewGroup falseParentView = (ViewGroup)parseNextView(parser,null,loadScript,scriptList);
            while(falseParentView.getChildCount() > 0)
            {
                View child = falseParentView.getChildAt(0);
                falseParentView.removeViewAt(0);
                ((ViewGroup)parentView).addView(child);
            }
        }
        catch (XmlPullParserException ex)
        {
            throw new ItsNatDroidException(ex);
        }
        catch (IOException ex)
        {
            throw new ItsNatDroidException(ex);
        }
    }

    protected void parseScriptElement(XmlPullParser parser,View viewParent, String[] loadScript,List<String> scriptList) throws IOException, XmlPullParserException
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

    public void setAttribute(View view,String namespaceURI,String name,String value)
    {
        ClassDescViewMgr classDescViewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = classDescViewMgr.get(view);
        viewClassDesc.setAttribute(view, namespaceURI, name, value, null, this);
    }

    public void removeAttribute(View view,String namespaceURI,String name)
    {
        ClassDescViewMgr viewMgr = page.getInflatedLayoutPageImpl().getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = viewMgr.get(view);
        viewClassDesc.removeAttribute(view, namespaceURI, name, page.getInflatedLayoutPageImpl());
    }
}

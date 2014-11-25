package org.itsnat.droid.impl.xmlinflated.layout;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.layout.XMLDOMLayout;
import org.itsnat.droid.impl.xmlinflater.layout.page.XMLInflaterLayoutPage;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutPageImpl extends InflatedLayoutImpl
{
    public InflatedLayoutPageImpl(ItsNatDroidImpl itsNatDroid,XMLDOMLayout domLayout,Context ctx)
    {
        super(itsNatDroid, domLayout,ctx);
    }

    public XMLInflaterLayoutPage getXMLLayoutInflaterPage()
    {
        return (XMLInflaterLayoutPage) xmlInflater;
    }

    public void setAttribute(View view, DOMAttr attr)
    {
        getXMLLayoutInflaterPage().setAttribute(view,attr);
    }

    public void removeAttribute(View view, String namespaceURI, String name)
    {
        getXMLLayoutInflaterPage().removeAttribute(view, namespaceURI, name);
    }
}

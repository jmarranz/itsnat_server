package org.itsnat.droid.impl.xmlinflated.layout.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.AttrParsed;
import org.itsnat.droid.impl.dom.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.layout.page.XMLInflaterLayoutPage;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutPageImpl extends InflatedLayoutImpl
{
    protected PageImpl page;

    public InflatedLayoutPageImpl(PageImpl page,LayoutParsed layoutParsed,Context ctx)
    {
        super(page.getItsNatDroidBrowserImpl().getItsNatDroidImpl(), layoutParsed, ctx);
        this.page = page;
    }

    public XMLInflaterLayoutPage getXMLLayoutInflaterPage()
    {
        return (XMLInflaterLayoutPage) xmlInflater;
    }

    public PageImpl getPageImpl()
    {
        return page;
    }

    public static int getChildIndex(ViewGroup parentView,View view)
    {
        if (view.getParent() != parentView) throw new ItsNatDroidException("View must be a direct child of parent View");
        // Esto es una chapuza pero no hay opción
        int size = parentView.getChildCount();
        for(int i = 0; i < size; i++)
        {
            if (parentView.getChildAt(i) == view)
                return i;
        }
        return -1; // No es hijo directo
    }

    public void setAttribute(View view, AttrParsed attr)
    {
        getXMLLayoutInflaterPage().setAttribute(view,attr);
    }

    public void removeAttribute(View view, String namespaceURI, String name)
    {
        getXMLLayoutInflaterPage().removeAttribute(view, namespaceURI, name);
    }

    public AttrLayoutInflaterListener getAttrLayoutInflaterListener()
    {
        return page.getPageRequestImpl().getAttrLayoutInflaterListener();
    }
}

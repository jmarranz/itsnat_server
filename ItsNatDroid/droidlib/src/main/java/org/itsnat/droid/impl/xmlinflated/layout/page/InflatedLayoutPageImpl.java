package org.itsnat.droid.impl.xmlinflated.layout.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.layout.XMLLayoutInflater;
import org.itsnat.droid.impl.xmlinflater.layout.page.XMLLayoutInflaterPage;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedLayoutPageImpl extends InflatedLayoutImpl
{
    protected PageImpl page;

    public InflatedLayoutPageImpl(PageImpl page,LayoutParsed layoutParsed, AttrCustomInflaterListener inflateListener, Context ctx)
    {
        super(page.getItsNatDroidBrowserImpl().getItsNatDroidImpl(), layoutParsed, inflateListener, ctx);
        this.page = page;
    }

    @Override
    public XMLLayoutInflater createXMLLayoutInflater()
    {
        return new XMLLayoutInflaterPage(this);
    }

    public XMLLayoutInflaterPage getXMLLayoutInflaterPage()
    {
        return (XMLLayoutInflaterPage)inflater;
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

}

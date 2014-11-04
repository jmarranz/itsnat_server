package org.itsnat.droid.impl.xmlinflater.layout.page;

import android.view.View;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.DroidEventGroupInfo;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewNotNullImpl;
import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflated.layout.page.InflatedLayoutPageImpl;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLLayoutInflater;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 4/11/14.
 */
public class XMLLayoutInflaterPage extends XMLLayoutInflater
{
    public XMLLayoutInflaterPage(InflatedLayoutPageImpl layout)
    {
        super(layout);
    }

    public InflatedLayoutPageImpl getInflatedLayoutPageImpl()
    {
        return (InflatedLayoutPageImpl)layout;
    }

    public PageImpl getPageImpl()
    {
        return getInflatedLayoutPageImpl().getPageImpl();
    }

    public void setAttribute(View view, AttrParsed attr)
    {
        ClassDescViewMgr classDescViewMgr = layout.getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = classDescViewMgr.get(view);
        setAttribute(viewClassDesc, view, attr, null,null);
    }

    public void removeAttribute(View view, String namespaceURI, String name)
    {
        ClassDescViewMgr viewMgr = layout.getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = viewMgr.get(view);
        removeAttribute(viewClassDesc, view, namespaceURI, name);
    }

    public boolean setAttribute(ClassDescViewBased viewClassDesc, View view, AttrParsed attr,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace
        String value = attr.getValue();
        if (ValueUtil.isEmpty(namespaceURI))
        {
            String type = getTypeInlineEventHandler(name);
            if (type != null)
            {
                ItsNatViewImpl viewData = getItsNatViewOfInlineHandler(type,view);
                viewData.setOnTypeInlineCode(name, value);
                if (viewData instanceof ItsNatViewNotNullImpl)
                    ((ItsNatViewNotNullImpl) viewData).registerEventListenerViewAdapter(type);

                return true;
            }
            else
                return super.setAttribute(viewClassDesc, view, attr, oneTimeAttrProcess,pending);
        }
        else
        {
            return super.setAttribute(viewClassDesc, view, attr, oneTimeAttrProcess,pending);
        }
    }

    protected boolean removeAttribute(ClassDescViewBased viewClassDesc, View view, String namespaceURI, String name)
    {
        if (ValueUtil.isEmpty(namespaceURI))
        {
            String type = getTypeInlineEventHandler(name);
            if (type != null)
            {
                ItsNatViewImpl viewData = getItsNatViewOfInlineHandler(type,view);
                viewData.removeOnTypeInlineCode(name);

                return true;
            }
            else return viewClassDesc.removeAttribute(view, namespaceURI, name, getInflatedLayoutPageImpl());
        }
        else
        {
            return viewClassDesc.removeAttribute(view, namespaceURI, name, getInflatedLayoutPageImpl());
        }
    }

    private ItsNatViewImpl getItsNatViewOfInlineHandler(String type,View view)
    {
        if (type.equals("load") || type.equals("unload"))
        {
            // El handler inline de load o unload sólo se puede poner una vez por layout por lo que obligamos
            // a que sea el View root de forma similar al <body> en HTML
            if (view != layout.getRootView())
                throw new ItsNatDroidException("onload/onunload handlers only can be defined in the view root of the layout");
        }
        return getInflatedLayoutPageImpl().getPageImpl().getItsNatDocImpl().getItsNatViewImpl(view);
    }

    private String getTypeInlineEventHandler(String name)
    {
        if (!name.startsWith("on")) return null;
        String type = name.substring(2);
        DroidEventGroupInfo eventGroup = DroidEventGroupInfo.getEventGroupInfo(type);
        if (eventGroup.getEventGroupCode() == DroidEventGroupInfo.UNKNOWN_EVENT)
            return null;
        return type;
    }
}

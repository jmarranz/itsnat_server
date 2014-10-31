package org.itsnat.droid.impl.xmlinflater.page;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.AttrCustomInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.DroidEventGroupInfo;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatViewNotNullImpl;
import org.itsnat.droid.impl.model.layout.LayoutParsed;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.droid.impl.xmlinflater.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

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

    public void setAttribute(View view, String namespaceURI, String name, String value)
    {
        ClassDescViewMgr classDescViewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = classDescViewMgr.get(view);
        setAttribute(viewClassDesc, view, namespaceURI, name, value, null,null);
    }

    public void removeAttribute(View view, String namespaceURI, String name)
    {
        ClassDescViewMgr viewMgr = getXMLLayoutInflateService().getClassDescViewMgr();
        ClassDescViewBased viewClassDesc = viewMgr.get(view);
        removeAttribute(viewClassDesc, view, namespaceURI, name);
    }

    public boolean setAttribute(ClassDescViewBased viewClassDesc, View view, String namespaceURI, String name, String value,
                                OneTimeAttrProcess oneTimeAttrProcess,PendingPostInsertChildrenTasks pending)
    {
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
                return super.setAttribute(viewClassDesc, view, namespaceURI, name, value, oneTimeAttrProcess,pending);
        }
        else
        {
            return super.setAttribute(viewClassDesc, view, namespaceURI, name, value, oneTimeAttrProcess,pending);
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
            else return viewClassDesc.removeAttribute(view, namespaceURI, name, page.getInflatedLayoutPageImpl());
        }
        else
        {
            return viewClassDesc.removeAttribute(view, namespaceURI, name, page.getInflatedLayoutPageImpl());
        }
    }

    private ItsNatViewImpl getItsNatViewOfInlineHandler(String type,View view)
    {
        if (type.equals("load") || type.equals("unload"))
        {
            // El handler inline de load o unload sólo se puede poner una vez por layout por lo que obligamos
            // a que sea el View root de forma similar al <body> en HTML
            if (view != getRootView())
                throw new ItsNatDroidException("onload/onunload handlers only can be defined in the view root of the layout");
        }
        return page.getItsNatDocImpl().getItsNatViewImpl(view);
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

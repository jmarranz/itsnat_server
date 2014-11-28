package org.itsnat.droid.impl.xmlinflater.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawableStandalone;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescChildElementDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescRootElementDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.page.XMLInflaterDrawablePage;
import org.itsnat.droid.impl.xmlinflater.drawable.stdalone.XMLInflaterDrawableStandalone;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 4/11/14.
 */
public abstract class XMLInflaterDrawable extends XMLInflater
{
    protected XMLInflaterDrawable(InflatedDrawable inflatedXML,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener,Context ctx)
    {
        super(inflatedXML,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx);
    }

    public static XMLInflaterDrawable createXMLInflaterDrawable(InflatedDrawable inflatedDrawable,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener, Context ctx,PageImpl page)
    {
        if (inflatedDrawable instanceof InflatedDrawablePage)
        {
            return new XMLInflaterDrawablePage((InflatedDrawablePage)inflatedDrawable,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx,page);
        }
        else if (inflatedDrawable instanceof InflatedDrawableStandalone)
        {
            return new XMLInflaterDrawableStandalone((InflatedDrawableStandalone)inflatedDrawable,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx);
        }
        return null; // Internal Error
    }


    public InflatedDrawable getInflatedDrawable()
    {
        return (InflatedDrawable)inflatedXML;
    }

    public Drawable inflateDrawable()
    {
        return inflateRoot(getInflatedDrawable().getXMLDOMDrawable());
    }


    private Drawable inflateRoot(XMLDOMDrawable xmlDOMDrawable)
    {
        DOMElement rootDOMElem = xmlDOMDrawable.getRootElement();

        //PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        Drawable rootDrawable = createRootDrawableAndFillAttributes(rootDOMElem);

        // processChildElements(rootDOMElem,rootDrawable);

        //pending.executeTasks();

        return rootDrawable;
    }

    public Drawable createRootDrawableAndFillAttributes(DOMElement rootDOMElem)
    {
        String name = rootDOMElem.getName();
        ClassDescDrawableMgr classDescViewMgr = getInflatedDrawable().getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescRootElementDrawable classDesc = (ClassDescRootElementDrawable)classDescViewMgr.get(name);
        if (classDesc == null) // Aquí no hay un View que es raiz de todos
            throw new ItsNatDroidException("Drawable type still not supported: " + name);
        Drawable drawable = createRootDrawable(classDesc,rootDOMElem);

        setRootDrawable(drawable);

        fillAttributes(classDesc, drawable, rootDOMElem,ctx);

        return drawable;
    }

    private Drawable createRootDrawable(ClassDescRootElementDrawable classDesc,DOMElement rootDOMElem)
    {
        return classDesc.createRootDrawable(rootDOMElem,this, ctx);
    }

    public void setRootDrawable(Drawable rootDrawable)
    {
        getInflatedDrawable().setDrawable(rootDrawable);
    }

    private <Tdrawable> void fillAttributes(ClassDescDrawable classDesc,Tdrawable drawable,DOMElement domElement,Context ctx)
    {
        ArrayList<DOMAttr> attribList = domElement.getDOMAttributeList();
        if (attribList != null)
        {
            for (int i = 0; i < attribList.size(); i++)
            {
                DOMAttr attr = attribList.get(i);
                setAttribute(classDesc, drawable, attr,ctx);
            }
        }
    }

    public <Tdrawable> boolean setAttribute(ClassDescDrawable classDesc,Tdrawable drawable,DOMAttr attr,Context ctx)
    {
        return classDesc.setAttribute(drawable,attr,this,ctx);
    }

    public ArrayList<ChildElementDrawable> processRootChildElements(DOMElement domElemParent)
    {
        LinkedList<DOMElement> childDOMElemList = domElemParent.getChildDOMElementList();
        if (childDOMElemList == null) return null;

        ArrayList<ChildElementDrawable> childDrawableList = new ArrayList<ChildElementDrawable>(childDOMElemList.size());
        for (DOMElement childDOMElem : childDOMElemList)
        {
            ChildElementDrawable childDrawable = inflateNextElement(childDOMElem,domElemParent,null);
            childDrawableList.add(childDrawable);
        }
        return childDrawableList;
    }

    protected ChildElementDrawable inflateNextElement(DOMElement domElement,DOMElement domElementParent,ChildElementDrawable parentChildDrawable)
    {
        ChildElementDrawable childDrawable = createChildElementDrawableAndFillAttributes(domElement,domElementParent,parentChildDrawable);

        processChildElements(domElement,childDrawable);

        return childDrawable;
    }

    private void getFullName(DOMElement domElement,StringBuilder name)
    {
        if (domElement.getParentDOMElement() != null)
        {
            getFullName(domElement.getParentDOMElement(),name);
            name.append(':');
        }
        name.append(domElement.getName());
    }

    private String getFullName(DOMElement domElement)
    {
        StringBuilder name = new StringBuilder();
        getFullName(domElement,name);
        return name.toString();
    }

    public ChildElementDrawable createChildElementDrawableAndFillAttributes(DOMElement domElement,DOMElement domElementParent,ChildElementDrawable parentChildDrawable)
    {
        String name = getFullName(domElement);
        ClassDescDrawableMgr classDescViewMgr = getInflatedDrawable().getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescChildElementDrawable classDesc = (ClassDescChildElementDrawable)classDescViewMgr.get(name);
        ChildElementDrawable childDrawable = createChildElementDrawable(classDesc, domElement,parentChildDrawable);

        fillAttributes(classDesc, childDrawable, domElement,ctx);

        return childDrawable;
    }

    private ChildElementDrawable createChildElementDrawable(ClassDescChildElementDrawable classDesc,DOMElement domElement,ChildElementDrawable parentChildDrawable)
    {
        return classDesc.createChildElementDrawable(domElement, this,parentChildDrawable, ctx);
    }

    public void processChildElements(DOMElement domElemParent,ChildElementDrawable parentChildDrawable)
    {
        LinkedList<DOMElement> childDOMElemList = domElemParent.getChildDOMElementList();
        if (childDOMElemList == null) return;

        parentChildDrawable.initChildElementDrawableList(childDOMElemList.size());
        for (DOMElement childDOMElem : childDOMElemList)
        {
            ChildElementDrawable childDrawable = inflateNextElement(childDOMElem,domElemParent,parentChildDrawable);
            parentChildDrawable.addChildElementDrawable(childDrawable);
        }
    }
}

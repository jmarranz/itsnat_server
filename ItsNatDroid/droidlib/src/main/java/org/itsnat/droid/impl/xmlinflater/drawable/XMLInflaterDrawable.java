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
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableChild;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawablePage;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawableStandalone;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescElementDrawableChild;
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
    protected XMLInflaterDrawable(InflatedDrawable inflatedXML,int bitmapDensityReference,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener,Context ctx)
    {
        super(inflatedXML,bitmapDensityReference,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx);
    }

    public static XMLInflaterDrawable createXMLInflaterDrawable(InflatedDrawable inflatedDrawable,int bitmapDensityReference,AttrLayoutInflaterListener attrLayoutInflaterListener,AttrDrawableInflaterListener attrDrawableInflaterListener, Context ctx,PageImpl page)
    {
        if (inflatedDrawable instanceof InflatedDrawablePage)
        {
            return new XMLInflaterDrawablePage((InflatedDrawablePage)inflatedDrawable,bitmapDensityReference,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx,page);
        }
        else if (inflatedDrawable instanceof InflatedDrawableStandalone)
        {
            return new XMLInflaterDrawableStandalone((InflatedDrawableStandalone)inflatedDrawable,bitmapDensityReference,attrLayoutInflaterListener,attrDrawableInflaterListener,ctx);
        }
        return null; // Internal Error
    }


    public InflatedDrawable getInflatedDrawable()
    {
        return (InflatedDrawable)inflatedXML;
    }

    public Drawable inflateDrawable()
    {
        return inflateRoot(getInflatedDrawable().getXMLDOMDrawable()).getDrawable();
    }

    private ElementDrawableRoot inflateRoot(XMLDOMDrawable xmlDOMDrawable)
    {
        DOMElement rootDOMElem = xmlDOMDrawable.getRootElement();
        return createRootDrawableAndFillAttributes(rootDOMElem,getInflatedDrawable());
    }

    private ElementDrawableRoot inflateRoot(DOMElement rootDOMElem,InflatedDrawable inflatedDrawable)
    {
        //PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        ElementDrawableRoot rootDrawable = createRootDrawableAndFillAttributes(rootDOMElem,inflatedDrawable);

        // processChildElements(rootDOMElem,rootDrawable);

        //pending.executeTasks();

        return rootDrawable;
    }

    public ElementDrawableRoot createRootDrawableAndFillAttributes(DOMElement rootDOMElem,InflatedDrawable inflatedDrawable)
    {
        String name = rootDOMElem.getName();
        ClassDescDrawableMgr classDescViewMgr = getInflatedDrawable().getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescRootElementDrawable classDesc = (ClassDescRootElementDrawable)classDescViewMgr.get(name);
        if (classDesc == null) // Aquí no hay una clase View que sea raiz de todos
            throw new ItsNatDroidException("Drawable type is not supported: " + name);
        ElementDrawableRoot drawableElem = createRootElementDrawable(classDesc, rootDOMElem);
        Drawable drawable = drawableElem.getDrawable();

        inflatedDrawable.setDrawable(drawable);

        fillAttributes(classDesc, drawable, rootDOMElem,ctx);

        return drawableElem;
    }

    private ElementDrawableRoot createRootElementDrawable(ClassDescRootElementDrawable classDesc, DOMElement rootDOMElem)
    {
        return classDesc.createRootElementDrawable(rootDOMElem, this, ctx);
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

/*
    public ArrayList<ElementDrawable> processRootChildElements(DOMElement domElemParent,ElementDrawableRoot elementDrawableRoot)
    {
        LinkedList<DOMElement> childDOMElemList = domElemParent.getChildDOMElementList();
        if (childDOMElemList == null) return null;

        ArrayList<ElementDrawable> childDrawableList = new ArrayList<ElementDrawable>(childDOMElemList.size());
        for (DOMElement childDOMElem : childDOMElemList)
        {
            ElementDrawable childDrawable = inflateNextElement(childDOMElem,domElemParent,elementDrawableRoot);
            childDrawableList.add(childDrawable);
        }
        return childDrawableList;
    }
*/

    protected ElementDrawable inflateNextElement(DOMElement domElement,DOMElement domElementParent,ElementDrawable parentChildDrawable)
    {
        ElementDrawable childDrawable = createChildElementDrawableAndFillAttributes(domElement,domElementParent,parentChildDrawable);

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

    public ElementDrawable createChildElementDrawableAndFillAttributes(DOMElement domElement,DOMElement domElementParent,ElementDrawable parentChildDrawable)
    {
        String parentName = getFullName(domElementParent);
        String name = parentName + ":" + domElement.getName();
        ClassDescDrawableMgr classDescViewMgr = getInflatedDrawable().getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescElementDrawableChild classDesc = (ClassDescElementDrawableChild)classDescViewMgr.get(name);
        if (classDesc == null)
        {
            // name = parentName + ":*";
            name = "*";
            classDesc = (ClassDescElementDrawableChild)classDescViewMgr.get(name);
            if (classDesc == null) throw new ItsNatDroidException("Not found descriptor: " + name);
        }

        ElementDrawableChild childDrawable = createChildElementDrawable(classDesc, domElement, parentChildDrawable);

        fillAttributes(classDesc, childDrawable, domElement,ctx);

        return childDrawable;
    }

    private ElementDrawableChild createChildElementDrawable(ClassDescElementDrawableChild classDesc,DOMElement domElement,ElementDrawable parentChildDrawable)
    {
        return classDesc.createChildElementDrawable(domElement, this, parentChildDrawable, ctx);
    }

    public void processChildElements(DOMElement domElemParent,ElementDrawable parentChildDrawable)
    {
        LinkedList<DOMElement> childDOMElemList = domElemParent.getChildDOMElementList();
        if (childDOMElemList == null) return;

        parentChildDrawable.initChildElementDrawableList(childDOMElemList.size());
        for (DOMElement childDOMElem : childDOMElemList)
        {
            ElementDrawable childDrawable = inflateNextElement(childDOMElem,domElemParent,parentChildDrawable);
            parentChildDrawable.addChildElementDrawable(childDrawable);
        }
    }
}

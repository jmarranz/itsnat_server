package org.itsnat.droid.impl.xmlinflater.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.InflatedDrawable;
import org.itsnat.droid.impl.xmlinflater.XMLInflater;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by jmarranz on 4/11/14.
 */
public class XMLInflaterDrawable extends XMLInflater
{
    protected InflatedDrawable inflatedDrawable;

    protected XMLInflaterDrawable(InflatedDrawable inflatedDrawable,Context ctx)
    {
        super(ctx);
        this.inflatedDrawable = inflatedDrawable;
    }

    public static XMLInflaterDrawable createXMLInflaterDrawable(InflatedDrawable inflatedDrawable,Context ctx)
    {
        return new XMLInflaterDrawable(inflatedDrawable,ctx);
    }

    public InflatedDrawable getInflatedDrawable()
    {
        return inflatedDrawable;
    }

    public Drawable inflateDrawable()
    {
        return inflateRoot(inflatedDrawable.getXMLDOMDrawable());
    }


    private Drawable inflateRoot(XMLDOMDrawable xmlDOMDrawable)
    {
        DOMElement rootDOMElem = xmlDOMDrawable.getRootElement();

        String name = rootDOMElem.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

        //PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        Drawable rootDrawable = createRootDrawableAndFillAttributes(name, rootDOMElem);

        processChildElements(rootDOMElem,rootDrawable);

        //pending.executeTasks();

        return rootDrawable;
    }

    public Drawable createRootDrawableAndFillAttributes(String name,DOMElement rootDOMElem)
    {
        ClassDescDrawableMgr classDescViewMgr = inflatedDrawable.getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescDrawable classDesc = classDescViewMgr.get(name);
        Drawable drawable = createRootDrawable(classDesc,rootDOMElem);

        setRootDrawable(drawable);

        fillAttributes(classDesc, drawable, rootDOMElem,ctx);

        return drawable;
    }

    private Drawable createRootDrawable(ClassDescDrawable classDesc,DOMElement rootDOMElem)
    {
        return classDesc.createRootDrawable(rootDOMElem,inflatedDrawable, ctx);
    }

    public void setRootDrawable(Drawable rootDrawable)
    {
        inflatedDrawable.setDrawable(rootDrawable);
    }

    private void fillAttributes(ClassDescDrawable classDesc,Drawable drawable,DOMElement domElement,Context ctx)
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

    public boolean setAttribute(ClassDescDrawable classDesc,Drawable drawable,DOMAttr attr,Context ctx)
    {
        return classDesc.setAttribute(drawable,attr,this,ctx);
    }

    protected void processChildElements(DOMElement domElemParent, Drawable drawable)
    {
        LinkedList<DOMElement> childViewList = domElemParent.getChildDOMElementList();
        if (childViewList != null)
        {
            for (DOMElement childDOMElem : childViewList)
            {
                inflateNextElement(childDOMElem,domElemParent, drawable);
            }
        }
    }

    private void inflateNextElement(DOMElement domElement,DOMElement domElementParent, Drawable drawable)
    {
        /*
        // Es llamado también para insertar fragmentos
        //PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        View view = createViewObjectAndFillAttributesAndAdd((ViewGroup) viewParent, viewParsed, pending);

        processChildViews(viewParsed,view);

        //pending.executeTasks();

        return view;
        */
    }

/*
    private View createViewObject(ClassDescViewBased classDesc,ViewParsed viewParsed,PendingPostInsertChildrenTasks pending)
    {
        return classDesc.createViewObjectFromParser(layout,viewParsed,pending);
    }

    private void fillAttributesAndAddView(View view,ClassDescViewBased classDesc,ViewGroup viewParent,ViewParsed viewParsed,PendingPostInsertChildrenTasks pending)
    {
        OneTimeAttrProcess oneTimeAttrProcess = classDesc.createOneTimeAttrProcess(view,viewParent);
        fillViewAttributes(classDesc,view,viewParsed,oneTimeAttrProcess,pending); // Los atributos los definimos después porque el addView define el LayoutParameters adecuado según el padre (LinearLayout, RelativeLayout...)
        classDesc.addViewObject(viewParent,view,-1,oneTimeAttrProcess,layout.getContext());
    }



    public View insertFragment(ViewParsed rootViewFragmentParsed)
    {
        return inflateNextView(rootViewFragmentParsed,null);
    }


    */
}

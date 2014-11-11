package org.itsnat.droid.impl.xmlinflater.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.model.AttrParsed;
import org.itsnat.droid.impl.model.ElementParsed;
import org.itsnat.droid.impl.model.drawable.DrawableParsed;
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
        return inflateRoot(inflatedDrawable.getDrawableParsed());
    }


    private Drawable inflateRoot(DrawableParsed drawableParsed)
    {
        ElementParsed rootElemParsed = drawableParsed.getRootElement();

        String name = rootElemParsed.getName(); // viewName lo normal es que sea un nombre corto por ej RelativeLayout

        //PendingPostInsertChildrenTasks pending = new PendingPostInsertChildrenTasks();

        Drawable rootDrawable = createRootDrawableAndFillAttributes(name, rootElemParsed);

        processChildElements(rootElemParsed,rootDrawable);

        //pending.executeTasks();

        return rootDrawable;
    }

    public Drawable createRootDrawableAndFillAttributes(String name,ElementParsed rootElemParsed)
    {
        ClassDescDrawableMgr classDescViewMgr = inflatedDrawable.getXMLInflateRegistry().getClassDescDrawableMgr();
        ClassDescDrawable classDesc = classDescViewMgr.get(name);
        Drawable drawable = createRootDrawable(classDesc,rootElemParsed);

        setRootDrawable(drawable);

        fillAttributes(classDesc, drawable, rootElemParsed,ctx);

        return drawable;
    }

    private Drawable createRootDrawable(ClassDescDrawable classDesc,ElementParsed rootElemParsed)
    {
        return classDesc.createRootDrawable(rootElemParsed,inflatedDrawable, ctx);
    }

    public void setRootDrawable(Drawable rootDrawable)
    {
        inflatedDrawable.setDrawable(rootDrawable);
    }

    private void fillAttributes(ClassDescDrawable classDesc,Drawable drawable,ElementParsed elemParsed,Context ctx)
    {
        ArrayList<AttrParsed> attribList = elemParsed.getAttributeList();
        if (attribList != null)
        {
            for (int i = 0; i < attribList.size(); i++)
            {
                AttrParsed attr = attribList.get(i);
                setAttribute(classDesc, drawable, attr,ctx);
            }
        }
    }

    public boolean setAttribute(ClassDescDrawable classDesc,Drawable drawable,AttrParsed attr,Context ctx)
    {
        return classDesc.setAttribute(drawable,attr,this,ctx);
    }

    protected void processChildElements(ElementParsed elemParsedParent, Drawable drawable)
    {
        LinkedList<ElementParsed> childViewList = elemParsedParent.getChildList();
        if (childViewList != null)
        {
            for (ElementParsed childElemParsed : childViewList)
            {
                inflateNextElement(childElemParsed,elemParsedParent, drawable);
            }
        }
    }

    private void inflateNextElement(ElementParsed elemParsed,ElementParsed elemParsedParent, Drawable drawable)
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

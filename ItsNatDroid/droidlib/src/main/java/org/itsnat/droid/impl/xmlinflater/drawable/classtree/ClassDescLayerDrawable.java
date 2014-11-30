package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.ChildElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

import java.util.ArrayList;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescLayerDrawable extends ClassDescRootElementDrawable<LayerDrawable>
{
    public ClassDescLayerDrawable(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"layer-list");
    }

    @Override
    public LayerDrawable createRootDrawable(DOMElement rootElem,XMLInflaterDrawable inflaterDrawable,Context ctx)
    {
        // http://stackoverflow.com/questions/20120725/layerdrawable-programatically

        ArrayList<ChildElementDrawable> itemList = inflaterDrawable.processRootChildElements(rootElem);
        Drawable[] drawableLayers = new Drawable[itemList.size()];
        for(int i = 0; i < itemList.size(); i++)
        {
            LayerDrawableItem item = (LayerDrawableItem)itemList.get(i);
            drawableLayers[i] = item.getDrawable();
        }

        LayerDrawable drawable = new LayerDrawable(drawableLayers);

        for(int i = 0; i < itemList.size(); i++)
        {
            LayerDrawableItem item = (LayerDrawableItem)itemList.get(i);
            item.setParentDrawable(drawable); // Por si acaso aunque ya es tarde y no se necesita

            drawable.setId(i,item.getId());
            drawable.setLayerInset(i,item.getLeft(),item.getTop(),item.getRight(),item.getBottom());
        }

        return drawable;
    }

    @Override
    protected boolean isAttributeIgnored(LayerDrawable draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;
        return isSrcAttribute(namespaceURI, name); // Se trata de forma especial en otro lugar
    }

    private static boolean isSrcAttribute(String namespaceURI,String name)
    {
        return InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && name.equals("src");
    }

    @Override
    public Class<LayerDrawable> getDrawableClass()
    {
        return LayerDrawable.class;
    }

    protected void init()
    {
        super.init();

        //addAttrDesc(new AttrDescDrawableReflecMethodBoolean(this, "dither", true));
    }


}

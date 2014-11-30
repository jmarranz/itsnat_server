package org.itsnat.droid.impl.xmlinflater.drawable;

import org.itsnat.droid.impl.xmlinflater.ClassDescMgr;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescBitmapDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescLayerDrawable;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescLayerDrawableItem;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescLayerDrawableItemBridge;
import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescNinePatchDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescDrawableMgr extends ClassDescMgr<ClassDescDrawable>
{
    public ClassDescDrawableMgr(XMLInflateRegistry parent)
    {
        super(parent);
        initClassDesc();
    }

    public ClassDescDrawable get(String className)
    {
        return classes.get(className);
    }

    @Override
    protected void initClassDesc()
    {
        ClassDescBitmapDrawable bitmap = new ClassDescBitmapDrawable(this);
        addClassDesc(bitmap);

        ClassDescNinePatchDrawable ninePatch = new ClassDescNinePatchDrawable(this);
        addClassDesc(ninePatch);

        ClassDescLayerDrawable layer = new ClassDescLayerDrawable(this);
        addClassDesc(layer);

            ClassDescLayerDrawableItem itemLayer = new ClassDescLayerDrawableItem(this);
            addClassDesc(itemLayer);

                ClassDescLayerDrawableItemBridge itemLayerBitmap = new ClassDescLayerDrawableItemBridge(this);
                addClassDesc(itemLayerBitmap);

    }


}

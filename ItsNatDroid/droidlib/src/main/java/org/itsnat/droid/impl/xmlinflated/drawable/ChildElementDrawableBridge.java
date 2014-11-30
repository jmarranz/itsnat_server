package org.itsnat.droid.impl.xmlinflated.drawable;

import org.itsnat.droid.impl.xmlinflater.drawable.classtree.ClassDescRootElementDrawable;

/**
 * Created by jmarranz on 30/11/14.
 */
public abstract class ChildElementDrawableBridge extends ChildElementDrawable
{
    protected ClassDescRootElementDrawable classDescBridge;

    protected ChildElementDrawableBridge(ClassDescRootElementDrawable classDescBridge)
    {
        this.classDescBridge = classDescBridge;
    }

    public ClassDescRootElementDrawable getClassDescRootDrawableBridge()
    {
        return classDescBridge;
    }
}

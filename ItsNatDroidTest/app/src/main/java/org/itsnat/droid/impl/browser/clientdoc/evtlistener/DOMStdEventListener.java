package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.Node;

/**
 * Created by jmarranz on 4/07/14.
 */
public class DOMStdEventListener extends NormalEventListener
{
    protected String type;
    protected boolean useCapture;
    protected int typeCode;

    public DOMStdEventListener(ItsNatDocImpl parent,View view,String type,String customFunc,String id,boolean useCapture,int commMode,long timeout,int typeCode)
    {
        super(parent,"domstd",view,customFunc,id,commMode,timeout);
        this.type = type;
        this.useCapture = useCapture;
        this.typeCode = typeCode;
    }

    public String getType()
    {
        return type;
    }

}

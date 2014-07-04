package org.itsnat.droid.impl.browser.clientdoc.evtlistener;

import android.view.View;

import org.itsnat.droid.impl.browser.clientdoc.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.clientdoc.Node;

/**
 * Created by jmarranz on 4/07/14.
 */
public class DOMStdEventListener
{
    protected ItsNatDocImpl parent;
    protected View view;
    protected String type;
    protected String listenerId;
    protected String customFunction;
    protected boolean useCapture;
    protected int commMode;
    protected long timeout;
    protected int typeCode;

    public DOMStdEventListener(ItsNatDocImpl parent,View view, String type, String listenerId, String customFunction, boolean useCapture, int commMode, long timeout, int typeCode)
    {
        this.parent = parent;
        this.view = view;
        this.type = type;
        this.listenerId = listenerId;
        this.customFunction = customFunction;
        this.useCapture = useCapture;
        this.commMode = commMode;
        this.timeout = timeout;
        this.typeCode = typeCode;
    }
}

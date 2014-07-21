/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.event.server.droid;

import org.itsnat.core.event.droid.DroidKeyEvent;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 *
 * @author jmarranz
 */
public class ServerItsNatDroidKeyEventImpl extends ServerItsNatDroidEventImpl implements DroidKeyEvent
{
    protected int keyCode;
    
    public ServerItsNatDroidKeyEventImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }

    public int getKeyCode()
    {
        return keyCode;
    }

    public void setKeyCode(int keyCode)
    {
        this.keyCode = keyCode;
    }    
}

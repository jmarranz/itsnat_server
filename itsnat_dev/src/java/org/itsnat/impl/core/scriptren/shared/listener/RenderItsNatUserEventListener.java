/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.listener;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.listener.ItsNatNormalEventListenerWrapperImpl;

/**
 *
 * @author jmarranz
 */
public interface RenderItsNatUserEventListener
{
    public String addCustomFunctionCode(ItsNatNormalEventListenerWrapperImpl itsNatListener,StringBuilder code,ClientDocumentStfulDelegateImpl clientDoc);
}

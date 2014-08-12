/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.resp;

/**
 *
 * @author jmarranz
 */
public class ResponseEventDelegateDroidImpl extends ResponseEventDelegateImpl
{
    public ResponseEventDelegateDroidImpl(ResponseImpl response)
    {
        super(response);
    }
    
    @Override
    public void sendPendingCode(String code,boolean error)
    {        
        response.writeResponse(code);
    }
    
}

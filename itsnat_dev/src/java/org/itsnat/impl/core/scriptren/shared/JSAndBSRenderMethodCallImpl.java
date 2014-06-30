/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderMethodCallImpl extends JSAndBSRenderImpl
{
    public static String getCallMethodCode(Object object,String methodName,Object[] params,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( javaToScript(object,cacheIfPossible,clientDoc) );
        code.append( "." + methodName + "(" );
        if (params != null)
        {
            for(int i = 0; i < params.length; i++)
                code.append( javaToScript(params[i],cacheIfPossible,clientDoc) );
        }
        code.append( ")" );

        if (endSentence)
            code.append( ";" );
        return code.toString();
    }    
}

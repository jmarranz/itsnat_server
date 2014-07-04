/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2014 Jose Maria Arranz Santamaria, Spanish citizen

  This software is free software; you can redistribute it and/or modify it
  under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 3 of
  the License, or (at your option) any later version.
  This software is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details. You should have received
  a copy of the GNU Lesser General Public License along with this program.
  If not, see <http://www.gnu.org/licenses/>.
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

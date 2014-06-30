/*
  ItsNat Java Web Application Framework
  Copyright (C) 2007-2011 Jose Maria Arranz Santamaria, Spanish citizen

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

package org.itsnat.impl.core.scriptren.bsren;

import org.itsnat.impl.core.scriptren.jsren.*;
import java.io.Serializable;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.scriptren.jsren.dom.node.JSRenderNodeImpl;
import org.itsnat.impl.core.dompath.NodeLocationWithParentImpl;
import org.itsnat.impl.core.scriptren.shared.ScriptUtilImpl;

/**
 *
 * @author jmarranz
 */
public abstract class BSScriptUtilImpl extends ScriptUtilImpl implements ScriptUtil,Serializable
{
    /**
     * Creates a new instance of ScriptUtil
     */
    public BSScriptUtilImpl()
    {
    }

    public abstract ClientDocumentStfulDelegateImpl getCurrentClientDocumentStfulDelegate();
 
    public String encodeURIComponent(String text)
    {
        throw new ItsNatException("Not implemented, only for JavaScript"); // El método es sobre JavaScript
    }

    public String encodeURIComponent(char c)
    {
        throw new ItsNatException("Not implemented, only for JavaScript"); // El método es sobre JavaScript
    }

    
    protected String renderAddNodeToCache(NodeLocationWithParentImpl nodeLoc)
    {
        return JSRenderNodeImpl.addNodeToCache(nodeLoc);
    }
    
    protected String renderGetCallMethodCode(Object obj,String methodName,Object[] params,boolean endSentence,ClientDocumentStfulDelegateImpl clientDoc)
    {      
        return JSRenderMethodCallImpl.getCallMethodCode(obj,methodName,params,endSentence,false,clientDoc);    
    }
    
    protected String renderSetPropertyCode(Object obj,String propertyName,Object value,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateImpl clientDoc)
    {          
        return JSRenderImpl.getSetPropertyCode(obj,propertyName,value,endSentence,false,clientDoc);    
    }
    
    protected String renderGetPropertyCode(Object obj,String propertyName,boolean endSentence,ClientDocumentStfulDelegateImpl clientDoc)
    {              
        return JSRenderImpl.getGetPropertyCode(obj,propertyName,endSentence,false,clientDoc); 
    }
    
    protected String javaToScript(Object value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return JSRenderImpl.javaToJS(value,false,clientDoc);    
    }    
    
}

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

package org.itsnat.impl.core.scriptren.jsren;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.web.BrowserWeb;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author jmarranz
 */
public abstract class JSRenderMethodCallImpl extends JSRenderImpl
{
    public static JSRenderMethodCallImpl getJSRenderMethodCall(Element elem)
    {
        if (elem instanceof HTMLElement)
            return JSRenderMethodCallHTMLImpl.SINGLETON;
        else
            return JSRenderMethodCallOtherNSImpl.SINGLETON;
    }

    public static void addCallMethodCode(Object object,String methodName,Object[] params,boolean endSentence,ItsNatStfulDocumentImpl itsNatDoc)
    {
        // Asegura que cada cliente recibe el código específico sin trucos raros
        ClientDocumentStfulImpl[] allClient = itsNatDoc.getAllClientDocumentStfulsCopy();
        for(int i = 0; i < allClient.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = allClient[i];
            if (!(clientDoc.getClientDocumentStfulDelegate() instanceof ClientDocumentStfulDelegateWebImpl)) continue;
            if (clientDoc.isSendCodeEnabled())
            {
                ClientDocumentStfulDelegateWebImpl clientDocDeleg = (ClientDocumentStfulDelegateWebImpl)clientDoc.getClientDocumentStfulDelegate();
                clientDoc.addCodeToSend( getCallMethodCode(object,methodName,params,endSentence,true,clientDocDeleg) );
            }
        }
    }

    public static String getCallMethodCode(Object object,String methodName,Object[] params,boolean endSentence,boolean cacheIfPossible,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        code.append( javaToJS(object,cacheIfPossible,clientDoc) );
        code.append( "." + methodName + "(" );
        if (params != null)
        {
            for(int i = 0; i < params.length; i++)
                code.append( javaToJS(params[i],cacheIfPossible,clientDoc) );
        }
        code.append( ")" );

        if (endSentence)
            code.append( ";" );
        return code.toString();
    }

    public static void addCallMethodHTMLFormControlCode(Element elem,String methodName,ItsNatStfulDocumentImpl itsNatDoc)
    {
        // Métodos blur(),focus(),click(),select()
        ClientDocumentStfulImpl[] allClient = itsNatDoc.getAllClientDocumentStfulsCopy();
        for(int i = 0; i < allClient.length; i++)
        {
            ClientDocumentStfulImpl clientDoc = allClient[i];
            if (!(clientDoc.getClientDocumentStfulDelegate() instanceof ClientDocumentStfulDelegateWebImpl)) continue;            
            if (clientDoc.isSendCodeEnabled())
            {
                ClientDocumentStfulDelegateWebImpl clientDocDeleg = (ClientDocumentStfulDelegateWebImpl)clientDoc.getClientDocumentStfulDelegate();                
                clientDoc.addCodeToSend( getCallMethodFormControlCode(elem,methodName,true,clientDocDeleg) );
            }
        }
    }

    public static String getCallMethodFormControlCode(Element elem,String methodName,boolean cacheIfPossible,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String elemRef = javaToJS(elem,cacheIfPossible,clientDoc);

        return getCallMethodFormControlCode(elem,elemRef,methodName,clientDoc);
    }

    public static String getCallMethodFormControlCode(Element elem,String elemRef,String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        if (methodName.equals("blur")||methodName.equals("focus"))
        {
            JSRenderMethodCallImpl render = getJSRenderMethodCall(elem);
            return render.getCallBlurFocusFormControlCode(elem,elemRef,methodName,clientDoc);
        }
        else
            return getCallMethodFormControlCodeDefault(elemRef,methodName);
    }

    public static String getCallFormControlFocusBlurCodeDefault(Element elem,String methodName,boolean cacheIfPossible,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        String elemRef = javaToJS(elem,cacheIfPossible,clientDoc);
        return getCallFormControlFocusBlurCodeDefault(elemRef,methodName);
    }

    public String getCallBlurFocusFormControlCode(Element elem,String elemRef,String methodName,ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return getCallFormControlFocusBlurCodeDefault(elemRef,methodName);
    }

    protected static String getCallFormControlFocusBlurCodeDefault(String elemRef,String methodName)
    {
        // methodName es "blur" o "focus"
        return elemRef + "." + methodName + "();";
    }

    protected static String getCallMethodFormControlCodeDefault(String elemRef,String methodName)
    {
        return elemRef + "." + methodName + "();";
    }

    public abstract boolean isFocusOrBlurMethodWrong(String methodName,Element formElem,BrowserWeb browser);

}

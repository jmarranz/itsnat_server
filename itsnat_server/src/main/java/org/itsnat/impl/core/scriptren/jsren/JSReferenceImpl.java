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

import org.itsnat.impl.core.scriptren.shared.ScriptReference;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 * ESTA CLASE ES INTERNA
 *
 * @author jmarranz
 */
public class JSReferenceImpl implements ScriptReference
{
    protected JSScriptUtilImpl scriptUtil;
    protected Object value;
    protected String tmpVarName;

    /** Creates a new instance of CodeValueImpl */
    public JSReferenceImpl(Object value,JSScriptUtilImpl scriptUtil)
    {
        this.value = value;
        this.scriptUtil = scriptUtil;
    }

    public String getCode()
    {
        /* "Alguien" ha invocado este método porque quiere este código
         * para usarlo en alguna sentencia JavaScript,
         * consideremos que contiene this.value el código
         * de una llamada a una función: myCall(params);
         * dicho código ya se añadió a los Document Listener
         * por lo que debemos generar una variable "al vuelo" e insertar
         * para que quede algo así: var tmp_111111 = myCall(params);
         * y se usará la variable en vez del código de la llamada
         * Así podemos conseguir en Java que este objeto parezca una referencia JavaScript en ese caso
         */
         if (tmpVarName != null)
             return tmpVarName; // Ya se generó (value es un CodeFragmentImpl y ya se pasó por aquí)

         if (value instanceof JSCodeFragmentImpl)
         {
             JSCodeFragmentImpl fragment = (JSCodeFragmentImpl)value;
             ItsNatStfulDocumentImpl itsNatDoc = scriptUtil.getItsNatStfulDocument();
             this.tmpVarName = itsNatDoc.getUniqueIdGenerator().generateId("itsNatRef");
             fragment.setCode("var " + tmpVarName + "=" + fragment.getCode());
             return tmpVarName;
         }
         else
             return scriptUtil.toScript(value);
    }

    private JSReferenceImpl addCodeToSendListeners(String code)
    {
        JSCodeFragmentImpl fragment = new JSCodeFragmentImpl(code);
        // Es muy importante que el objeto sea el mismo y compartido por el propietario y los observers
        // pues el código puede ser modificado (setCode) y su modificación debe ser manifestada en todos
        ItsNatStfulDocumentImpl itsNatDoc = scriptUtil.getItsNatStfulDocument();
        itsNatDoc.addCodeToSend(fragment);
        return new JSReferenceImpl(fragment,scriptUtil);
    }

    public ScriptReference setProperty(String propName,Object newValue,boolean endSentence)
    {
        String code = scriptUtil.getSetPropertyCode(this,propName,newValue,endSentence);
        return addCodeToSendListeners(code);
    }

    public ScriptReference getProperty(String propName,boolean endSentence)
    {
        String code = scriptUtil.getGetPropertyCode(this,propName,endSentence);
        return addCodeToSendListeners(code);
    }

    public ScriptReference callMethod(String methodName,Object[] params,boolean endSentence)
    {
        String code = scriptUtil.getCallMethodCode(this,methodName,params,endSentence);
        return addCodeToSendListeners(code);
    }
}

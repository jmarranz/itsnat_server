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

package org.itsnat.impl.core.jsren.dom.event.domstd.w3c;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class JSRenderW3CAdobeSVGMutationEventImpl extends JSRenderW3CAdobeSVGEventImpl
{
    public static final JSRenderW3CAdobeSVGMutationEventImpl SINGLETON = new JSRenderW3CAdobeSVGMutationEventImpl();

    /**
     * Creates a new instance of JSRenderW3CHTMLEventImpl
     */
    public JSRenderW3CAdobeSVGMutationEventImpl()
    {
    }

    public String getInitEvent(Event evt,String evtVarName,ClientDocumentStfulImpl clientDoc)
    {
        MutationEvent mutEvt = (MutationEvent)evt;

        StringBuffer code = new StringBuffer();
        code.append( super.getInitEvent(evt,evtVarName,clientDoc) );

        code.append( evtVarName + ".relatedNode = " + clientDoc.getNodeReference(mutEvt.getRelatedNode(),true,false) + ";\n" );  // No estoy seguro si el relatedNode puede ser null pero evitamos lanzar error en ese caso (la opción más prudente)
        code.append( evtVarName + ".prevValue = " + toTransportableStringLiteral(mutEvt.getPrevValue(),clientDoc.getBrowser()) + ";\n" );
        code.append( evtVarName + ".newValue = " + toTransportableStringLiteral(mutEvt.getNewValue(),clientDoc.getBrowser()) + ";\n" );
        code.append( evtVarName + ".attrName = " + "\"" + mutEvt.getAttrName() + "\";\n" );
        code.append( evtVarName + ".attrChange = " + mutEvt.getAttrChange() + ";\n" );
        // Al parecer la propiedad attrChange NO existe en el evento de verdad
        // pero nosotros como la conocemos la incluimos.
        // Quizás se pueda deducir a partir de prevValue y newValue
        return code.toString();
    }
}

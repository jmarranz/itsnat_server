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

package org.itsnat.impl.core.scriptren.jsren.node;

import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 *
 * @author jmarranz
 */
public class JSRenderProcessingInstructionImpl extends JSRenderNotChildrenNodeImpl
{
    public static final JSRenderProcessingInstructionImpl SINGLETON = new JSRenderProcessingInstructionImpl();

    /** Creates a new instance of JSProcessingInstructionRender */
    public JSRenderProcessingInstructionImpl()
    {
    }

    @Override
    public String createNodeCode(Node node,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // No funciona en MSIE 8, con innerHTML sí pero 
        ProcessingInstruction nodeProcInst = (ProcessingInstruction)node;
        Browser browser = clientDoc.getClientDocumentStful().getBrowser();
        return "itsNatDoc.doc.createProcessingInstruction(\"" + nodeProcInst.getTarget() + "\"," + toTransportableStringLiteral(nodeProcInst.getData(),true,browser) + ")";
    }

}

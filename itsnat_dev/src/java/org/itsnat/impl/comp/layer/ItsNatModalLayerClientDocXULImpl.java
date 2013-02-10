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

package org.itsnat.impl.comp.layer;

import org.itsnat.impl.core.clientdoc.ClientDocumentStfulImpl;
import org.itsnat.impl.core.jsren.dom.node.JSRenderElementImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatModalLayerClientDocXULImpl extends ItsNatModalLayerClientDocImpl
{
    public ItsNatModalLayerClientDocXULImpl(ItsNatModalLayerXULImpl parentComp,ClientDocumentStfulImpl clientDoc)
    {
        super(parentComp,clientDoc);
    }

    public void initModalLayer()
    {
        Element layerElem = parentComp.getElement(); // Debería usarse siempre un <panel>
        String ref = clientDoc.getNodeReference(layerElem,true,true);
        StringBuilder code = new StringBuilder();
        code.append( "var elem = " + ref + ";\n" );
        code.append( "if (elem.openPopupAtScreen) elem.openPopupAtScreen(0,0,false);\n" );

        String background = parentComp.getBackground();
        float opacity = parentComp.getOpacity();

        String backgroundProp;
        if (background != null)
            backgroundProp = background;
        else
            backgroundProp = null;

        code.append( "elem.style.opacity = \"" + opacity + "\";\n" );
        if (backgroundProp != null)
            code.append( "elem.style.background = \"" + backgroundProp + "\";\n" );

        clientDoc.addCodeToSend(code.toString());
    }

    public void preRemoveLayer()
    {
    }

    protected void renderShowHide(Element elem,String elemVarName,boolean hide,StringBuilder code,JSRenderElementImpl render)
    {
        if (NamespaceUtil.isXULElement(elem) &&
            "panel".equals(elem.getLocalName()))
        {
            // No usamos hidden o display porque al ocultar de esta forma provoca la emisión
            // del evento popuphidden, en donde lo normal es destruir el modal layer.
            if (hide)
            {
                code.append(elemVarName + ".itsnat_collapsed = " + elemVarName + ".collapsed;");
                code.append(elemVarName + ".collapsed = true;\n");
            }
            else
            {
                code.append(elemVarName + ".collapsed = " + elemVarName + ".itsnat_collapsed;\n");
            }
        }
        else super.renderShowHide(elem,elemVarName, hide,code, render);
    }

}

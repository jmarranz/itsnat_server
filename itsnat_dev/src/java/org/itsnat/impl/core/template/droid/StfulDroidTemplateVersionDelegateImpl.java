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

package org.itsnat.impl.core.template.droid;

import java.io.Writer;
import org.itsnat.impl.core.markup.render.DOMRenderImpl;
import org.itsnat.impl.core.scriptren.bsren.node.BSRenderElementScriptImpl;
import org.itsnat.impl.core.template.MarkupTemplateVersionImpl;
import org.itsnat.impl.core.template.StfulTemplateVersionDelegateImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 *
 * @author jmarranz
 */
public class StfulDroidTemplateVersionDelegateImpl extends StfulTemplateVersionDelegateImpl
{

    public StfulDroidTemplateVersionDelegateImpl(MarkupTemplateVersionImpl parent)
    {
        super(parent);
    }
    
    @Override
    public void serializeNode(Node node,Writer out,DOMRenderImpl nodeRender)
    {
        if (is_SCRIPT_or_STYLE_Element(node)) // Nos interesa sólo el caso <script>, <style> a día de hoy no existe en Android y no lo hemos inventado
        {
            Element elem = (Element)node;
            String src = elem.getAttribute("src");
            if (!"".equals(src))   
            {
                // Serializamos un clone para evitar generar mutation events, cambiamos <script src=".."> por un <script> con un nodo de texto                
                Element elemCloned = (Element)elem.cloneNode(true);                
                String scriptCode = BSRenderElementScriptImpl.getScript(elemCloned);
                elemCloned.setTextContent(scriptCode);
                super.serializeNode(elemCloned, out, nodeRender);
                return;
            }        
        }

        super.serializeNode(node, out, nodeRender);
    }
}

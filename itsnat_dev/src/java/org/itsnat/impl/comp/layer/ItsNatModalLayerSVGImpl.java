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

import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.web.ItsNatSVGDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.web.ClientDocumentStfulDelegateWebImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatModalLayerSVGImpl extends ItsNatModalLayerImpl implements ItsNatModalLayer
{
    /** Creates a new instance of ItsNatModalLayerSVGImpl */
    public ItsNatModalLayerSVGImpl(Element element,boolean cleanBelow,int zIndex,float opacity,String background,NameValue[] artifacts,ItsNatSVGDocComponentManagerImpl compMgr)
    {
        super(element,cleanBelow,zIndex,opacity,background,artifacts,compMgr);

        init();
    }

    public ItsNatModalLayerSVGImpl(Element element,NameValue[] artifacts,ItsNatSVGDocComponentManagerImpl compMgr)
    {
        super(element,artifacts,compMgr);

        init();
    }

    public String getDefaultBackground()
    {
        return "black";
    }

    public ItsNatModalLayerClientDocImpl createItsNatModalLayerClientDoc(ClientDocumentStfulDelegateWebImpl clientDoc)
    {
        return new ItsNatModalLayerClientDocSVGImpl(this,clientDoc);
    }

    public Node createDefaultNode()
    {
        return getItsNatDocument().getDocument().createElementNS(NamespaceUtil.SVG_NAMESPACE,"rect");
    }

}

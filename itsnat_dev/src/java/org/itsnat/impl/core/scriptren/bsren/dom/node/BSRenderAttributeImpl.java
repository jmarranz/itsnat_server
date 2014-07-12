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

package org.itsnat.impl.core.scriptren.bsren.dom.node;

import java.util.List;
import java.util.Map;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.NodeScriptRefImpl;
import org.itsnat.impl.core.scriptren.shared.dom.node.RenderAttribute;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class BSRenderAttributeImpl extends BSRenderNodeImpl implements RenderAttribute
{
    private static final BSRenderAttributeImpl SINGLETON = new BSRenderAttributeImpl();
    
    public static BSRenderAttributeImpl getBSRenderAttribute()
    {
        return SINGLETON;
    }    
    
    public boolean isIgnored(Attr attr,Element elem)
    {
        return false; // Por ahora nada
    }
    
    public String setAttributeCode(Attr attr,Element elem,boolean newElem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (isIgnored(attr,elem))
            return "";
        String attrName = attr.getName();
        String bsValue = toBSAttrValue(attr,elem,clientDoc);
        return setAttributeCode(attr,attrName,bsValue,elem,newElem,clientDoc);
    }    
    
    protected String toBSAttrValue(Attr attr,Element elem,ClientDocumentStfulDelegateImpl clientDoc)
    {
        String value = attr.getValue();
        return toBSAttrValue(value,clientDoc);
    }    
    
    protected String toBSAttrValue(String value,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return toTransportableStringLiteral(value,clientDoc.getBrowser());
    }    
    
    public String setAttributeCode(Attr attr,Element elem,String elemVarName,boolean newElem,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (isIgnored(attr,elem))
            return "";
        String attrName = attr.getName();
        String bsValue = toBSAttrValue(attr,elem,clientDoc);
        return setAttributeCode(attr,attrName,bsValue,elem,elemVarName,newElem,clientDoc);
    }    
    
    protected String setAttributeCode(Attr attr,String attrName,String bsValue,Element elem,boolean newElem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation(elem,true);
        return setAttributeCode(attr,attrName,bsValue,new NodeScriptRefImpl(nodeLoc),newElem);
    }    

    protected String setAttributeCode(Attr attr,String attrName,String bsValue,Element elem,String elemVarName,boolean newElem,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return setAttributeCode(attr,attrName,bsValue,new NodeScriptRefImpl(elemVarName,clientDoc),newElem);
    }

    public String setAttributeCode(Attr attr,String attrName,String bsValue,NodeScriptRefImpl nodeRef,boolean newElem)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {        
            String namespaceURIScript = shortNamespaceURI(namespaceURI);
            
            attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS            
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + "," + namespaceURIScript + ",\"" + attrName + "\"," + bsValue + ");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.setAttributeNS(" + elemVarName + "," + namespaceURIScript + ",\"" + attrName + "\"," + bsValue + ");\n";
            }                
        }
        else
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.setAttribute2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + attrName + "\"," + bsValue + ");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.setAttribute(" + elemVarName + ",\"" + attrName + "\"," + bsValue + ");\n";
            }
        }

    }        
    
    private void toArraysForBatch(Element elem,List<Attr> attrList,StringBuilder attrNameArr,StringBuilder attrValueArr,ClientDocumentStfulDelegateImpl clientDoc)
    {
        int i = 0;
        for(Attr attr : attrList)
        {
            String name = attr.getName();
            String bsValue = toBSAttrValue(attr,elem,clientDoc);
            attrNameArr.append("\"" + name + "\"");
            attrValueArr.append(bsValue);
            i++;

            if (i < attrList.size()) { attrNameArr.append(','); attrValueArr.append(','); }                
        }                
    }
    
    public String setAttributeCodeBatch(Element elem,String elemVarName,Map<String,List<Attr>> mapByNamespace,ClientDocumentStfulDelegateImpl clientDoc)    
    {
        StringBuilder code = new StringBuilder();
        for(Map.Entry<String,List<Attr>> entry : mapByNamespace.entrySet())
        {
            String namespaceURI = entry.getKey();
            String namespaceURIScript = shortNamespaceURI(namespaceURI);  
            List<Attr> attribList =  entry.getValue();
            StringBuilder attrNameArr = new StringBuilder();
            StringBuilder attrValueArr = new StringBuilder();  
            toArraysForBatch(elem,attribList,attrNameArr,attrValueArr,clientDoc);
            
           code.append( "itsNatDoc.setAttributeNSBatch(" + elemVarName + "," + namespaceURIScript + ",new String[]{" + attrNameArr.toString() + "},new String[]{" + attrValueArr.toString() + "});\n" );             
        }
                                    
        return code.toString();                     
    }
    
    public String setAttributeCodeBatch(Element elem,String elemVarName,List<Attr> attrListNoNamespace,ClientDocumentStfulDelegateImpl clientDoc)    
    {
        StringBuilder attrNameArr = new StringBuilder();
        StringBuilder attrValueArr = new StringBuilder();            
        toArraysForBatch(elem,attrListNoNamespace,attrNameArr,attrValueArr,clientDoc);

        String code =  "itsNatDoc.setAttributeNSBatch(" + elemVarName + ",null,new String[]{" + attrNameArr.toString() + "},new String[]{" + attrValueArr.toString() + "});\n" ; 
                                    
        return code;                     
    }    
    
    public String removeAttributeCode(Attr attr,Element elem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (isIgnored(attr,elem))
            return "";
        String attrName = attr.getName();
        return removeAttributeCode(attr,attrName,elem,clientDoc);
    }    
    
    protected String removeAttributeCode(Attr attr,String attrName,Element elem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation(elem,true);
        return removeAttributeCode(attr,attrName,elem,new NodeScriptRefImpl(nodeLoc));
    }

    protected String removeAttributeCode(Attr attr,String attrName,Element elem,NodeScriptRefImpl nodeRef)
    {
        String namespaceURI = attr.getNamespaceURI();
        if (namespaceURI != null)
        {
            String namespaceURIScript = shortNamespaceURI(namespaceURI);            
            
            attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + "," + namespaceURIScript + ",\"" + attrName + "\");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttributeNS(" + elemVarName + "," + namespaceURIScript + ",\"" + attrName + "\");\n";
            }            
        }
        else
        {
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttribute2(" + nodeLoc.toScriptNodeLocation(true) + ",\"" + attrName + "\");\n";
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                return "itsNatDoc.removeAttribute(" + elemVarName + ",\"" + attrName + "\");\n";
            }        
        }        
        
    }    
    

}

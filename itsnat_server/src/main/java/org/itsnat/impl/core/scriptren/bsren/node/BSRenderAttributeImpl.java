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

package org.itsnat.impl.core.scriptren.bsren.node;

import java.util.List;
import java.util.Map;
import org.itsnat.impl.core.clientdoc.ClientDocumentStfulDelegateImpl;
import org.itsnat.impl.core.clientdoc.droid.ClientDocumentStfulDelegateDroidImpl;
import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.shared.node.NodeScriptRefImpl;
import org.itsnat.impl.core.scriptren.shared.node.RenderAttribute;
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

    public String setAttributeCode(Attr attr,Element elem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        if (isIgnored(attr,elem))
            return "";
        String attrName = attr.getName();
        String bsValue = toBSAttrValue(attr,elem,clientDoc);
        return setAttributeCode(attr,attrName,bsValue,elem,clientDoc);
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

    private static boolean isAttrRemote(Attr attr) 
    {
        return attr.getValue().startsWith("@remote:");
    }
    
    public String setAttributeCode(Attr attr,Element elem,String elemVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        if (isIgnored(attr,elem))
            return "";
        String attrName = attr.getName();
        String bsValue = toBSAttrValue(attr,elem,clientDoc);
        return setAttributeCode(attr,attrName,bsValue,elem,elemVarName,clientDoc);
    }

    protected String setAttributeCode(Attr attr,String attrName,String bsValue,Element elem,ClientDocumentStfulDelegateDroidImpl clientDoc)
    {
        NodeLocationImpl nodeLoc = clientDoc.getNodeLocation(elem,true);
        return setAttributeCode(attr,attrName,bsValue,new NodeScriptRefImpl(nodeLoc));
    }

    protected String setAttributeCode(Attr attr,String attrName,String bsValue,Element elem,String elemVarName,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return setAttributeCode(attr,attrName,bsValue,new NodeScriptRefImpl(elemVarName,clientDoc));
    }

    public String setAttributeCode(Attr attr,String attrName,String bsValue,NodeScriptRefImpl nodeRef)
    {
        StringBuilder code = new StringBuilder();
        
        boolean isAttrRemote = isAttrRemote(attr);
        String metadataPrefix = "";
        String metadataSuffix = "";
        if (isAttrRemote)
        {
            metadataPrefix = "/*[s*/";  // s = single attribute
            metadataSuffix = "/*s]*/";        
        }
        
        String namespaceURI = attr.getNamespaceURI();
        String bsAttrName;
        if (namespaceURI != null)
        {
            String namespaceURIScript = shortNamespaceURI(namespaceURI);
            attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS, así admitimos que el programador use "android:localname" si quiere
            bsAttrName = "\"" + attrName + "\"";            
            
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                code.append("itsNatDoc.setAttributeNS2(" + nodeLoc.toScriptNodeLocation(true) + "," + metadataPrefix + namespaceURIScript + "," + bsAttrName + "," + bsValue + metadataSuffix + ");\n");
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                code.append("itsNatDoc.setAttributeNS(" + elemVarName + "," + metadataPrefix + namespaceURIScript + "," + bsAttrName + "," + bsValue + metadataSuffix + ");\n");
            }
        }
        else
        {
            bsAttrName = "\"" + attrName + "\"";            
            
            if (nodeRef.getNodeRef() instanceof NodeLocationImpl)
            {
                NodeLocationImpl nodeLoc = (NodeLocationImpl)nodeRef.getNodeRef();
                code.append("itsNatDoc.setAttribute2(" + nodeLoc.toScriptNodeLocation(true) + "," + metadataPrefix + bsAttrName + "," + bsValue + metadataSuffix + ");\n");
            }
            else
            {
                String elemVarName = (String)nodeRef.getNodeRef();
                code.append("itsNatDoc.setAttribute(" + elemVarName + "," + metadataPrefix + bsAttrName + "," + bsValue + metadataSuffix + ");\n");
            }
        }

        return code.toString();
    }

    private void toArraysForBatch(Element elem,List<Attr> attrList,boolean namespaceBased,StringBuilder bsAttrNameBuilder,StringBuilder bsAttrValueBuilder,boolean[] someAttrRemote,ClientDocumentStfulDelegateImpl clientDoc)
    {
        int len = attrList.size();
        String[] bsAttrNameArray = new String[len];
        String[] bsAttrValueArray = new String[len];
        
        int i = 0;
        for(Attr attr : attrList)
        {
            boolean isAttrRemote = isAttrRemote(attr);
            String metadataKeyPrefix = "";
            String metadataKeySuffix = "";
            String metadataValuePrefix = "";
            String metadataValueSuffix = "";            
            if (isAttrRemote)
            {
                someAttrRemote[0] = true;
                
                metadataKeyPrefix = "/*[k*/"; // k = key
                metadataKeySuffix =  "/*k]*/";        
                metadataValuePrefix = "/*[v*/"; // v = value
                metadataValueSuffix =  "/*v]*/";                
            }                    
            
            String name = namespaceBased ? attr.getLocalName() : attr.getName(); // Usamos getLocalName() y no getName() cuando el namespace va aparte como dato y no tiene sentido prefix aunque lo tenga, si no tiene namespace hay que usar getName() porque getLocalName() devuelve null            
            String bsAttrName = metadataKeyPrefix + "\"" + name + "\"" + metadataKeySuffix;             
            String bsValue = metadataValuePrefix + toBSAttrValue(attr,elem,clientDoc) + metadataValueSuffix;
            bsAttrNameArray[i] = bsAttrName;
            bsAttrValueArray[i] = bsValue;
            i++;
        }
        
        for(i = 0; i < bsAttrNameArray.length; i++)
        {
            bsAttrNameBuilder.append(bsAttrNameArray[i]);
            bsAttrValueBuilder.append(bsAttrValueArray[i]);

            if (i < bsAttrNameArray.length - 1) { bsAttrNameBuilder.append(','); bsAttrValueBuilder.append(','); }
        }        
    }

    public String setAttributeCodeBatchNS(Element elem,String elemVarName,Map<String,List<Attr>> mapByNamespace,ClientDocumentStfulDelegateImpl clientDoc)
    {
        StringBuilder code = new StringBuilder();
        for(Map.Entry<String,List<Attr>> entry : mapByNamespace.entrySet())
        {
            String namespaceURI = entry.getKey();
            String namespaceURIScript = shortNamespaceURI(namespaceURI);
            List<Attr> attribList =  entry.getValue();           
            code.append( setAttributeCodeBatch(elem,elemVarName,namespaceURIScript,attribList,clientDoc) );
        }
      
        return code.toString();
    }

    public String setAttributeCodeBatch(Element elem,String elemVarName,List<Attr> attrListNoNamespace,ClientDocumentStfulDelegateImpl clientDoc)
    {
        return setAttributeCodeBatch(elem,elemVarName,null,attrListNoNamespace,clientDoc);
    }

    private String setAttributeCodeBatch(Element elem,String elemVarName,String namespaceURIScript,List<Attr> attrList,ClientDocumentStfulDelegateImpl clientDoc)
    {
        // namespaceURIScript puede ser null
        
        StringBuilder code = new StringBuilder();        
        
        boolean[] someAttrRemote = new boolean[1];
        StringBuilder bsAttrNameBuilder = new StringBuilder();
        StringBuilder bsAttrValueBuilder = new StringBuilder();
        toArraysForBatch(elem,attrList,namespaceURIScript != null,bsAttrNameBuilder,bsAttrValueBuilder,someAttrRemote,clientDoc);
        
        String metadataNSPrefix = "";
        String metadataNSSuffix = "";          
        if (someAttrRemote[0])
        {
            metadataNSPrefix = "/*[n*/"; // n = namespace
            metadataNSSuffix =  "/*n]*/";                       
        }                         
        
        code.append("itsNatDoc.setAttrBatch(" + elemVarName + "," + metadataNSPrefix + namespaceURIScript + metadataNSSuffix + ",new String[]{" + bsAttrNameBuilder.toString() + "},new String[]{" + bsAttrValueBuilder.toString() + "});\n");       
            
        return code.toString();
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

            attrName = attr.getLocalName(); // Es el localName de acuerdo a la documentación oficial de removeAttributeNS, ver notas en setAttributeCode(Attr attr,String attrName,String bsValue,NodeScriptRefImpl nodeRef)
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.itsnat.impl.core.scriptren.shared.dom.node;

import org.itsnat.impl.core.dompath.NodeLocationImpl;
import org.itsnat.impl.core.scriptren.shared.JSAndBSRenderImpl;

/**
 *
 * @author jmarranz
 */
public class JSAndBSRenderNodeImpl extends JSAndBSRenderImpl
{
   
    public static String getNodeLocation(NodeLocationImpl nodeLoc,boolean errIfNull)
    {
        String nodeRef = nodeLoc.toScriptNodeLocation(errIfNull);
        if (nodeRef.equals("null")) return "null";
        return nodeRef;
    }        
}

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

package org.itsnat.impl.comp;

import org.itsnat.comp.ItsNatElementComponent;
import org.itsnat.comp.ItsNatElementComponentUI;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.util.MiscUtil;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatElementComponentImpl extends ItsNatComponentImpl implements ItsNatElementComponent
{

    /**
     * Creates a new instance of ItsNatElementComponentImpl
     */
    public ItsNatElementComponentImpl(Element node,NameValue[] artifacts,ItsNatDocComponentManagerImpl componentMgr)
    {
        super(node,artifacts,componentMgr);
    }

    public String getStringArtifactOrAttribute(String name,String defaultValue)
    {
        Object valueObj = getArtifact(name,false); // Evitamos buscar en el documento en caso fallido porque debería haberse registrado para el componente concreto
        if (valueObj == null)
        {
            if (getElement().hasAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,name))
                return getElement().getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,name);
            else
                return defaultValue;
        }
        else if (valueObj instanceof String)
            return (String)valueObj;
        else
            return valueObj.toString();
    }

    public int getIntegerArtifactOrAttribute(String name,int defaultValue)
    {
        Object valueObj = getArtifact(name,false); // Evitamos buscar en el documento en caso fallido porque debería haberse registrado para el componente concreto
        if (valueObj == null)
        {
            String valueStr = getElement().getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,name);
            if (!valueStr.equals(""))
                return Integer.parseInt(valueStr);
            else
                return defaultValue;
        }
        else if (valueObj instanceof Integer)
            return ((Integer)valueObj).intValue();
        else
            return Integer.parseInt(valueObj.toString());
    }

    public float getFloatArtifactOrAttribute(String name,float defaultValue)
    {
        Object valueObj = getArtifact(name,false); // Evitamos buscar en el documento en caso fallido porque debería haberse registrado para el componente concreto
        if (valueObj == null)
        {
            String valueStr = getElement().getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,name);
            if (!valueStr.equals(""))
                return Float.parseFloat(valueStr);
            else
                return defaultValue;
        }
        else if (valueObj instanceof Float)
            return ((Float)valueObj).floatValue();
        else
            return Float.parseFloat(valueObj.toString());
    }

    public boolean getBooleanArtifactOrAttribute(String name,boolean defaultValue)
    {
        Object valueObj = getArtifact(name,false); // Evitamos buscar en el documento en caso fallido porque debería haberse registrado para el componente concreto
        if (valueObj == null)
        {
            String valueStr = getElement().getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,name);
            if (!valueStr.equals(""))
                return MiscUtil.getBoolean(valueStr);
            else
                return defaultValue;
        }
        else if (valueObj instanceof Boolean)
            return ((Boolean)valueObj).booleanValue();
        else
            return MiscUtil.getBoolean(valueObj.toString());
    }

    public boolean getDefaultSelectionOnComponentsUsesKeyboard()
    {
        ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();
        return getBooleanArtifactOrAttribute("selectionUsesKeyboard",compMgr.isSelectionOnComponentsUsesKeyboard());
    }

    public Element getElement()
    {
        return (Element)node;
    }

    public ItsNatElementComponentUI getItsNatElementComponentUI()
    {
        return (ItsNatElementComponentUI)compUI;
    }

    public abstract Object createDefaultStructure();

    public Object getDeclaredStructure(Class expectedClass)
    {
        Element elem = getElement();
        Object structure = getArtifact("useStructure",false); // Evitamos buscar en el documento en caso fallido porque debería haberse registrado para el componente concreto pues no puede haber un registro más global con "useStructure" pues hay diferentes tipos de estructuras (listas, tablas etc)
        if (structure == null)
        {
            String structureName = elem.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"useStructure");
            if (structureName.length() > 0)
            {
                structure = getArtifact(structureName,true); // busca también en el documento el objeto estructura con el nombre dado en el atributo
                if (structure == null) throw new ItsNatException("Artifact useStructure not found with name: \"" + structureName + "\"",this);
            }
            else
            {
                structure = createDefaultStructure(); // No puede ser null
            }
        }

        if (!expectedClass.isInstance(structure))
            throw new ItsNatException("Expected an " + expectedClass.getName() + " object",structure);
        return structure;
    }

}

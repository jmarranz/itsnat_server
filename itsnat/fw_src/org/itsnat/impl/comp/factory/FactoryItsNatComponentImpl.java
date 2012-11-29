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

package org.itsnat.impl.comp.factory;

import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class FactoryItsNatComponentImpl
{
    public FactoryItsNatComponentImpl()
    {
    }

    public abstract String getCompType();  // En componentes de elementos HTML reconocidos puede ser nulo (opcional)
    public abstract String getKey();
    public abstract ItsNatComponent createItsNatComponent(Element elem,String compType,NameValue[] artifacts,boolean ignoreIsComponentAttr,boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr);

    public static boolean hasBeforeAfterCreateItsNatComponentListener(boolean execCreateFilters,ItsNatDocComponentManagerImpl compMgr)
    {
        return execCreateFilters && compMgr.hasBeforeAfterCreateItsNatComponentListener();
    }

    public static ItsNatComponent processBeforeCreateItsNatComponentListener(Element element,String compType,NameValue[] params,NameValue[] artifacts,ItsNatDocComponentManagerImpl compMgr)
    {
        NameValue[] allArtifacts = null;
        if ((params != null)&&(artifacts != null))
        {
            allArtifacts = new NameValue[params.length + artifacts.length];
            System.arraycopy(params,    0, allArtifacts, 0,             params.length);
            System.arraycopy(artifacts, 0, allArtifacts, params.length, artifacts.length);
        }
        else if (params != null)
        {
            allArtifacts = params;
        }
        else if (artifacts != null)
        {
            allArtifacts = artifacts;
        }
        return compMgr.processBeforeCreateItsNatComponentListener(element,compType,allArtifacts);
    }

    public static ItsNatComponent processAfterCreateItsNatComponentListener(ItsNatComponent comp,ItsNatDocComponentManagerImpl compMgr)
    {
        return compMgr.processAfterCreateItsNatComponentListener(comp);
    }

    public static void registerItsNatComponent(boolean execCreateFilters,ItsNatComponent comp,ItsNatDocComponentManagerImpl compMgr)
    {
        // Si execCreateFilters es que estamos llamando desde un método específico
        // de creación del componente, no desde el método genérico de creación
        // que ya hace este registro.
        if (execCreateFilters) compMgr.registerItsNatComponent(comp);
    }

    public static NameValue getUseStructureParam(Object structure)
    {
        return structure != null? new NameValue("useStructure",structure) : null;
    }

    public static NameValue[] getUseStructureParamArray(Object structure)
    {
        NameValue useStructureParam = getUseStructureParam(structure);
        if (useStructureParam == null) return null;
        return new NameValue[] { useStructureParam };
    }
}

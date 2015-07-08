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

package org.itsnat.impl.comp.mgr;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.impl.comp.ItsNatComponentImpl;
import org.itsnat.impl.comp.label.ItsNatLabelEditorDefaultImpl;
import org.itsnat.impl.comp.layer.ItsNatModalLayerImpl;
import org.itsnat.impl.comp.list.ItsNatListCellEditorDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatTableCellEditorDefaultImpl;
import org.itsnat.impl.comp.tree.ItsNatTreeCellEditorDefaultImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentAttachedClientImpl;
import org.itsnat.impl.core.doc.ItsNatStfulDocumentImpl;

/**
 * Actualmente los namespaces con eventos no X/HTML como son SVG y XUL soportan
 * XHTML embebido en los principales navegadores (FireFox, Opera, Chrome y Safari).
 * 
 * @author jmarranz
 */
public abstract class ItsNatStfulDocComponentManagerImpl extends ItsNatDocComponentManagerImpl 
{
    protected LinkedList<ItsNatModalLayerImpl> modalLayers;

    /** Creates a new instance of ItsNatStfulDocComponentManagerImpl */
    public ItsNatStfulDocComponentManagerImpl(ItsNatStfulDocumentImpl itsNatDoc)
    {
        super(itsNatDoc);
    }


    public ItsNatStfulDocumentImpl getItsNatStfulDocument()
    {
        return (ItsNatStfulDocumentImpl)itsNatDoc;
    }

    public boolean hasItsNatModalLayers()
    {
        if (modalLayers == null) return false;
        return !modalLayers.isEmpty();
    }

    public LinkedList<ItsNatModalLayerImpl> getItsNatModalLayers()
    {
        if (modalLayers == null) this.modalLayers = new LinkedList<ItsNatModalLayerImpl>();
        return modalLayers;
    }

    public void addClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasItsNatModalLayers())
        {
            // Es el caso de modal layers ya mostrados, es útil notificar
            // para que también en este caso se redimensionen los modal layers
            // en los observadores cuando cambia el tamaño de la ventana
            // o el layout y en MSIE v6 para ocultar también los HTML select
            // que están "detrás".
            LinkedList<ItsNatModalLayerImpl> modalLayers = getItsNatModalLayers();
            for(ItsNatModalLayerImpl comp : modalLayers)
            {
                comp.addClientDocumentAttachedClient(clientDoc);
            }
        }

        if (hasItsNatComponents())
        {
            WeakHashMap<ItsNatComponent,Object> compMap = getItsNatComponentWeakMap();
            for(Map.Entry<ItsNatComponent,Object> entry : compMap.entrySet())
            {
                ItsNatComponent comp = entry.getKey();
                if (comp instanceof ItsNatModalLayerImpl) continue; // Evitamos llamar dos veces, los modal layer deben llamarse en el orden de creación
                else if (!(comp instanceof ItsNatComponentImpl)) continue; // Componente del usuario

                ((ItsNatComponentImpl)comp).addClientDocumentAttachedClient(clientDoc);
            }
        }
    }

    public void removeClientDocumentAttachedClient(ClientDocumentAttachedClientImpl clientDoc)
    {
        if (hasItsNatModalLayers())
        {
            LinkedList<ItsNatModalLayerImpl> modalLayers = getItsNatModalLayers();
            for(ItsNatModalLayerImpl comp : modalLayers)
            {
                // Da igual el orden en que se itere pues total el cliente se está cerrando
                // NO debería hacerse nada más que desregistrar pues el cliente puede
                // estar ya invalidado.
                comp.removeClientDocumentAttachedClient(clientDoc);
            }
        }

        if (hasItsNatComponents())
        {
            WeakHashMap<ItsNatComponent,Object> compMap = getItsNatComponentWeakMap();
            for(Map.Entry<ItsNatComponent,Object> entry : compMap.entrySet())
            {
                ItsNatComponent comp = entry.getKey();
                if (comp instanceof ItsNatModalLayerImpl) continue; // Evitamos llamar dos veces, los modal layer deben llamarse en el orden de creación
                else if (!(comp instanceof ItsNatComponentImpl)) continue; // Componente del usuario

                ((ItsNatComponentImpl)comp).removeClientDocumentAttachedClient(clientDoc);
            }
        }
    }


    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor)
    {
        return new ItsNatLabelEditorDefaultImpl(compEditor,this);
    }

    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatListCellEditorDefaultImpl(compEditor,this);
    }

    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatTableCellEditorDefaultImpl(compEditor,this);
    }

    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor)
    {
        return new ItsNatTreeCellEditorDefaultImpl(compEditor,this);
    }
}

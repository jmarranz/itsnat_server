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

package org.itsnat.impl.comp.list;

import org.itsnat.core.event.CustomParamTransport;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.ItsNatComponentUI;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.comp.mgr.ItsNatStfulDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.w3c.dom.events.Event;
import org.w3c.dom.html.HTMLSelectElement;

/**
 * Parecido a JList
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectMultImpl extends ItsNatHTMLSelectImpl implements ItsNatHTMLSelectMult,ItsNatListMultSelInternal,ListSelectionListener
{
    protected ListSelectionModelMgrImpl selModelMgr;

    /**
     * Creates a new instance of ItsNatHTMLSelectMultImpl
     */
    public ItsNatHTMLSelectMultImpl(HTMLSelectElement element,NameValue[] artifacts,ItsNatStfulDocComponentManagerImpl componentMgr)
    {
        super(element,artifacts,componentMgr);

        // Aunque pongamos en <select> con multiple="multiple"
        // el selection model puede estar en modo de selección única
        // y funcionará pues el componente quitará el actual al ser cambiado por otro
        // No imponemos multiple="multiple" así podríamos en teoría asociar al tipo de select que queramos
        // getHTMLSelectElement().setMultiple(true); // Para asegurarnos

        init();
    }

    public ItsNatListMultSelSharedImpl getItsNatListMultSelShared()
    {
        return (ItsNatListMultSelSharedImpl)listDelegate;
    }

    public ItsNatListSharedImpl createItsNatListShared()
    {
        return new ItsNatListMultSelSharedImpl(this);
    }

    public void setDefaultModels()
    {
        super.setDefaultModels();

        getItsNatListMultSelShared().setDefaultModels();
    }

    public void unbindModels()
    {
        getItsNatListMultSelShared().unbindModels();

        super.unbindModels();
    }

    public void unsetListSelectionModel()
    {
        if (selModelMgr != null)
        {
            selModelMgr.getListSelectionModel().removeListSelectionListener(this);
            selModelMgr.dispose();
        }
        // No anulamos el selModelMgr para que se pueda recuperar el ListSelectionModel después de un disposeEffective
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        if (changeBasedDelegate.isChangeEvent(type,clientDoc))
        {
            // En selección múltiple el selectedIndex sirve de muy poco (o nada) por ejemplo
            // cuando se selecciona/deselecciona con la tecla CTRL o SHIFT pulsada
            // La mejor manera es traerse el estado de todos los elementos
            CustomParamTransport selectedIndexes = new CustomParamTransport("selectedIndexes","itsNatDoc.getSelectedHTMLSelect(event.getCurrentTarget())");
            return new ParamTransport[]{selectedIndexes};
        }
        else
            return null;
    }

    public void handleEventOnChange(Event evt)
    {
        // Ejecutado como respuesta al evento "change" en el SELECT en el navegador
        // El <select multiple="multiple"> permite múltiple selección
        // En selección múltiple no sabemos cuales fueron seleccionados/quitados
        // por eso nos bajamos una foto del estado actual del componente
        // esto no garantiza que quede así pues el ListSelectionModel tiene
        // la última palabra según el modo de selección, a partir de la detección
        // de los cambios habidos obtendremos el estado final

        // NO hacemos setServerUpdatingFromClient(true) porque necesitamos
        // que el servidor propague al cliente los elementos que verdaderamente han de quedar seleccionados
        // que puede diferir de lo que hay en el cliente cuando se genera el evento de acuerdo
        // con las reglas de selección del ListSelectionModel

        ItsNatEvent itsNatEvent = (ItsNatEvent)evt;
        String indexesStr = (String)itsNatEvent.getExtraParam("selectedIndexes");

        int[] indices;
        if (indexesStr.length() > 0) // El split(",") ante una cadena vacía también genera un item erroneamente
        {
            String[] indexesStrArr = indexesStr.split(",");
            indices = new int[indexesStrArr.length];
            for(int i = 0; i < indexesStrArr.length; i++)
            {
                indices[i] = Integer.parseInt(indexesStrArr[i]);
            }
        }
        else indices = new int[0];

        selModelMgr.setSelectedIndices(indices);
    }

    public ItsNatListMultSelUIInternal getItsNatListMultSelUIInternal()
    {
        return (ItsNatListMultSelUIInternal)compUI;
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultListModel();
    }

    public ListModel createDefaultListModel()
    {
        return new DefaultListModel();
    }

    public ItsNatListMultSelUIInternal createDefaultItsNatHTMLSelectMultUI()
    {
        return new ItsNatHTMLSelectMultUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatHTMLSelectMultUI();
    }

    public ListModel getListModel()
    {
        return (ListModel)dataModel;
    }

    public void setListModel(ListModel dataModel)
    {
        setDataModel(dataModel);
    }

    public void	setListData(Object[] listData)
    {
        getItsNatListMultSelShared().setListData(listData);
    }

    public void	setListData(List<Object> listData)
    {
        getItsNatListMultSelShared().setListData(listData);
    }

    public ListSelectionModelMgrImpl getListSelectionModelMgr()
    {
        return selModelMgr;
    }

    public ListSelectionModel getListSelectionModel()
    {
        if (selModelMgr == null)
            return null;
        return selModelMgr.getListSelectionModel();
    }

    public void setListSelectionModel(ListSelectionModel selectionModel)
    {
        unsetListSelectionModel();

        this.selModelMgr = getItsNatListMultSelShared().setListSelectionModel(selectionModel);

        if (markupDrivenUtil != null)
            ((ItsNatHTMLSelectMultMarkupDrivenUtil)markupDrivenUtil).postSetDefaultListSelectionModel();

        // A partir de ahora los cambios en la selección se notificarán al DOM
        // pues hemos de cambiar el atributo selected
        selectionModel.addListSelectionListener(this);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        // Ha habido un cambio en la selección a través del SelectionModel
        // necesitamos sincronizar el DOM de acuerdo al mismo, pues el DOM
        // es el que manifiesta visualmente la selección
        // No llamamos a disableSendCodeToRequesterIfServerUpdating()/enableSendCodeToRequester
        // porque es el servidor y no el cliente el que decide qué elementos al final estarán seleccionados
        // por lo que no hay el caso de actualización del servidor respecto al cliente sin más
        // puede haber redundancia (re-seleccionar algunos elmentos en el cliente ya seleccionados) pero se asume.

        ListSelectionModel selModel = (ListSelectionModel)e.getSource();
        if (selModel.getValueIsAdjusting())
            return;

        if (!isUIEnabled()) return; // Caso de llamada indirecta en markup driven cuando se elimina un elemento (al actualizar el selection model), dicho elemento todavía no está eliminado y aquí se supone eliminado

        int first = e.getFirstIndex();
        int last = e.getLastIndex();

        ItsNatListMultSelUIInternal compUI = getItsNatListMultSelUIInternal();

        for(int i = first; i <= last; i++)
        {
            boolean selected = selModel.isSelectedIndex(i);
            compUI.setSelectedIndex(i,selected);
        }
    }

    public int getSelectedIndex()
    {
        return getListSelectionModel().getMinSelectionIndex();
    }

    public void setSelectedIndex(int index)
    {
        getListSelectionModel().setSelectionInterval(index,index);
    }

    public int[] getSelectedIndices()
    {
        return selModelMgr.getSelectedIndices();
    }

    public void setSelectedIndices(int[] indices)
    {
        selModelMgr.setSelectedIndices(indices);
    }

    public void initialSyncWithDataModel()
    {
        super.initialSyncWithDataModel();

        getItsNatListMultSelShared().initialSyncSelModelWithDataModel();
    }

    public void insertElementAtInternal(int index,Object anObject)
    {
        super.insertElementAtInternal(index,anObject);

        selModelMgr.insertElementUpdateModel(index);
    }

    public void removeElementRangeInternal(int fromIndex,int toIndex)
    {
        super.removeElementRangeInternal(fromIndex,toIndex);

        selModelMgr.removeRangeUpdateModel(fromIndex,toIndex);
    }

    public boolean isCombo()
    {
        return false;
    }

}

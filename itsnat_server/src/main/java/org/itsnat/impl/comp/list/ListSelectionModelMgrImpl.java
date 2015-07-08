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

import java.io.Serializable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author jmarranz
 */
public class ListSelectionModelMgrImpl implements Serializable
{
    protected ListSelectionModel selectionModel;
    protected int size = 0;
    protected boolean disposed = false; // Por ahora no se usa, pero podría usarse para evitar accesos al ListSelectionModel

    /** Creates a new instance of ListSelectionModelMgrImpl */
    public ListSelectionModelMgrImpl(ListSelectionModel selectionModel)
    {
        if (selectionModel == null)
            selectionModel = EmptyListSelectionModelImpl.SINGLETON;

        this.selectionModel = selectionModel;

        //syncWithDataModel();
    }

    public static ListSelectionModelMgrImpl newListSelectionModelMgr(ListSelectionModel selectionModel,int size)
    {
        ListSelectionModelMgrImpl selModelMgr = new ListSelectionModelMgrImpl(selectionModel);

        selModelMgr.syncWithDataModel(size);

        return selModelMgr;
    }

    public void dispose()
    {
        this.disposed = true;

        // NO hacemos nada más, pues al llamar a removeAllUpdateModel()
        // se llaman también a los listeners del programador indicando que algunos
        // elementos dejan de estar seleccionados porque van a quitarse del modelo.
        // Cuando hacemos dispose de un componente es porque normalmente el elemento DOM
        // asociado al componente va a ser eliminado, estas llamadas de de-selección
        // suelen por tanto inútiles y pueden interferir en la destrucción del componente y el DOM asociado.
        // Si se quiere reutilizar el ListSelectionModel en otro componente, el programador
        // deberá reiniciarlo, de todas formas la reutilización no sirve de gran cosa (mejor usar uno nuevo ya está vacío)
        // y la reutilización con elementos puede no funcionar.

        // removeAllUpdateModel();
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        int oldSize = this.size;
        int diff = size - oldSize;
        if (diff > 0) // Han sido añadidas al final
        {
            insertElementUpdateModel(oldSize,diff);
        }
        else if (diff < 0) // Han sido eliminadas del final
        {
            diff = -diff; // lo ponemos como positivo
            removeRangeUpdateModel(oldSize - diff,oldSize - 1);
        }
    }

    public ListSelectionModel getListSelectionModel()
    {
        return selectionModel;
    }

    public void syncWithDataModel(int size)
    {
        if (this.size > 0) // Se ha cambiado el data model
            removeAllUpdateModel();

        this.size = 0;

        // Iniciando el selection model, el data model es el que manda
        // porque si no hay datos nos da igual como esté definido el selectionModel
        // dado como argumento.
        // Es usado cuando se define el SelectionModel existiendo ya
        // un data model que puede contener ya datos.

        insertElementUpdateModel(0,size);

        // Por si acaso hubiera elementos seleccionados en el modelo usado por el usuario
        ListSelectionModel selectionModel = getListSelectionModel();
        selectionModel.clearSelection();
    }

    public void insertElementUpdateModel(int index)
    {
        insertElementUpdateModel(index,1);
    }

    public void insertElementUpdateModel(int index,int length)
    {
        if (length == 0) return;

        this.size += length;

        ListSelectionModel selectionModel = getListSelectionModel();

        boolean oldAdjusting = selectionModel.getValueIsAdjusting();
        selectionModel.setValueIsAdjusting(true); // Evita procesar muchos eventos para procesar al final todos en uno
        try
        {
            selectionModel.insertIndexInterval(index,length,true);
            // Si hay cambio de selección la notificación se hace DESPUES de insertar
            // y por tanto con la nueva numeración.

            // Por defecto pone los nuevos índices seleccionados en ciertos casos
            // (cuando hay selección múltiple y el nuevo elemento está antes de uno ya seleccionado por ejemplo en modo SINGLE_INTERVAL_SELECTION)
            // el caso es que no genera evento o si lo genera no incluye los índices de los nuevos elementos seleccionados

            // Se ha detectado el extraño caso de selection model vacío (anteriormente con algo)
            // pero que al añadir un primer elemento (index = 0, length = 0) el caso es que genera
            // un evento con índices 0 y 1 existiendo un único elemento en teoría (size es 1).

            if (selectionModel.isSelectedIndex(index))
            {
                // Fueron seleccionados, los quitamos, no queremos que el nuevo elemento añadido esté seleccionado:
                removeSelectionInterval(index, index + length - 1);
            }
        }
        finally
        {
            selectionModel.setValueIsAdjusting(false); // Envía un evento con todos los cambios de acuerdo al estado final. Evitamos así el "mal" evento lanzado en insertIndexInterval en donde consta que el nuevo elemento está seleccionado (que luego se envía otro en donde ha cambiado el estado)
            selectionModel.setValueIsAdjusting(oldAdjusting); // Restaura
        }

    }

    public void removeRangeUpdateModel(int fromIndex,int toIndex)
    {
        // Ha de llamarse DESPUES de eliminar los elementos DOM relacionados
        // porque al eliminar el rango en el selection model
        // lanza un evento para actualizar la selección de los elementos
        // de acuerdo ya *con la nueva numeración* (pues hay "corrimiento" de los siguientes)
        // por tanto en la decoración etc hemos de suponer que los elementos se eliminaron ya
        // Este evento en cuanto a la decoración no hace nada pues
        // el estado de selección del elemento en sí no cambia, sólo cambia su numeración
        // (incluso en el caso de eliminar un elemento en medio en modo SINGLE_INTERVAL_SELECTION)
        // Sin embargo he comprobado que al menos en JVM 1.4 en la notificación
        // alguno de los índices está fuera del nuevo rango (puede engañarnos
        // dándonos a entender que no se ha eliminado todavía), ignorar esos casos.

        getListSelectionModel().removeIndexInterval(fromIndex,toIndex);
        this.size -= toIndex - fromIndex + 1;
        if (size == 0)
            getListSelectionModel().clearSelection(); // Por si acaso
    }

    public void removeAllUpdateModel()
    {
        int last = this.size - 1;
        if (last >= 0)
            removeRangeUpdateModel(0,last);
    }

    public void changeSelectionModel(int index,boolean toggle, boolean extend, boolean selected)
    {
        ListSelectionModel sm = getListSelectionModel();

        // Es como está en JTable (1.4/1.5) y similar a http://developer.classpath.org/doc/javax/swing/JTable-source.html#line.4945
        // pero el comportamiento también es válido para los List (con selección múltiple sobre todo)
        if (extend && toggle) // shift+ctrl-click
        {
            sm.setAnchorSelectionIndex(index);
        }
        else if (toggle) // ctrl-click
        {
            if (selected)
                removeSelectionInterval(index, index);
            else
                addSelectionInterval(index, index);
        }
        else if (extend) // shift-click
        {
            //sm.setLeadSelectionIndex(index);
            sm.setSelectionInterval(sm.getAnchorSelectionIndex(), index); // Como en JTable 1.5
            // Si index es menor que el anchor el propio selection model hace el cambio
        }
        else // click (sin teclas)
        {
            sm.setSelectionInterval(index, index);
        }
    }

    public int[] getSelectedIndices()
    {
        ListSelectionModel selModel = getListSelectionModel();

        int iMin = selModel.getMinSelectionIndex();
        int iMax = selModel.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0))
            return new int[0];

        int[] indices = new int[1 + (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++)
        {
            // No todos en el rango están seleccionados
            if (selModel.isSelectedIndex(i))
            {
                indices[n] = i;
                n++;
            }
        }
        int[] indicesFinal = new int[n];
        System.arraycopy(indices, 0, indicesFinal, 0, n);
        return indicesFinal;
    }

    public void addSelectionInterval(int first,int end)
    {
        ListSelectionModel selModel = getListSelectionModel();
        int mode = selModel.getSelectionMode();
        if (mode == ListSelectionModel.SINGLE_INTERVAL_SELECTION)
        {
            // El DefaultSelectionModel de la JVM 1.4 tiene un penoso error
            // conceptual en addSelectionInterval en el caso SINGLE_INTERVAL_SELECTION,
            // pues en este caso debería ver si hay seleccionados adjacentes para formar
            // un intervalo más grande, pues no es así lo que hace es un setSelectionInterval,
            // según la documentación de setSelectionMode(int) los métodos setSelectionInterval
            // y addSelectionInterval son idénticos en este modo, esto se mantiene en la documentación
            // de la 1.5 pero no es verdad, en la 1.5 forma un nuevo conjunto con los adjacentes.
            // setSelectionInterval que viene a ser una substitución de seleccionados haya lo que haya más que un "añadir".
            // En el caso JVM 1.5 funciona correctamente.
            // Para hacer que funcione en ambos casos extendemos first y end
            // a los contiguos por defecto y por exceso seleccionados.

            for(int i = first - 1; (i >= 0) && selModel.isSelectedIndex(i); i--)
                first = i;

            int max = selModel.getMaxSelectionIndex();
            for(int i = end + 1; (i <= max) && selModel.isSelectedIndex(i); i++)
                end = i;
        }

        selModel.addSelectionInterval(first,end);
    }

    public void removeSelectionInterval(int first,int end)
    {
        ListSelectionModel selModel = getListSelectionModel();
        selModel.removeSelectionInterval(first,end);
    }

    public void setSelectedIndices(int[] indices)
    {
        // Este método es muy útil para procesar el evento "change" en el SELECT en el navegador
        // El <select multiple="multiple"> permite múltiple selección
        // pero es el ListSelectionModel el que en última instancia
        // debe imponer cuales están seleccionados o no, por ello
        // detectamos los cambios que han habido en el cliente y los
        // notificamos al selection model que decida.

        // NO hacemos setServerUpdatingFromClient(true) porque necesitamos
        // que el servidor propague al cliente los que verdaderamente han de quedar seleccionados
        // que puede diferir de lo que hay en el cliente cuando se genera el evento

        // Ej. si el selection model está en modo selección única, al seleccionar
        // otro en el cliente habrá dos seleccionados, detectaremos que el nuevo item seleccionado
        // es el que ha cambiado su estado y notificaremos al selection model,
        // el selection model decidirá que el previamente seleccionado debe de dejar de estarlo.

        // Todo esto también evita hacer un clearSelection()
        // que mandaría un montón de código al cliente
        // Si la selección no cambia no se generan eventos.

        boolean[] newState = new boolean[size];
        for(int i = 0; i < indices.length; i++)
        {
            int index = indices[i];
            newState[index] = true;
        }

        // Obtenemos la "foto" de los cambiados antes de hacer algún cambio en la selección
        // pues el cambio en uno puede cambiar a otros segun el modo de selección
        ListSelectionModel selModel = getListSelectionModel();
        boolean[] changed = new boolean[size];
        for(int i = 0; i < size; i++)
        {
            boolean selected = newState[i];
            changed[i] = (selected != selModel.isSelectedIndex(i));
        }

        boolean oldAdjusting = selModel.getValueIsAdjusting();
        selModel.setValueIsAdjusting(true); // Evita procesar muchos eventos para procesar al final todos en uno

        boolean isSelectionInterval = false;
        int first = -1;
        int end = -1;
        try
        {
            // Agrupamos los cambios en intervalos pues en el modo de selección
            // de un sólo intervalo necesitamos pasarlo en una sóla llamada
            // pues si pasamos de uno en uno el selecion model detectará huecos que pueden no existir quitando
            // un intervalo existente que podría verdaderamente estar seguido del nuevo
            for(int i = 0; i < size; i++)
            {
                if (changed[i])  // Ha cambiado
                {
                    boolean selected = newState[i];
                    if (selected)
                    {
                        if (first >= 0) // hay un intervalo ya abierto
                        {
                            if (isSelectionInterval)
                            {
                                end = i; // Uno más al intervalo continuo
                            }
                            else
                            {
                                // Cerramos el intervalo de no seleccionados primero
                                removeSelectionInterval(first,end);
                                // Iniciamos el nuevo intervalo de seleccionados
                                isSelectionInterval = true;
                                first = i;
                                end = i;
                            }
                        }
                        else
                        {
                            // Iniciamos el nuevo intervalo de seleccionados
                            isSelectionInterval = true;
                            first = i;
                            end = i;
                        }
                    }
                    else
                    {
                        if (first >= 0) // hay un intervalo ya abierto
                        {
                            if (!isSelectionInterval)
                            {
                                end = i; // Uno más al intervalo continuo
                            }
                            else
                            {
                                // Cerramos el intervalo de seleccionados primero
                                addSelectionInterval(first,end);
                                // Iniciamos el nuevo intervalo de NO seleccionados
                                isSelectionInterval = false;
                                first = i;
                                end = i;
                            }
                        }
                        else
                        {
                            // Iniciamos el nuevo intervalo de NO seleccionados
                            isSelectionInterval = false;
                            first = i;
                            end = i;
                        }
                    }
                }
                else // No ha cambiado
                {
                    // Este elemento no ha cambiado por tanto
                    // no vamos ni añadir ni a quitar de la selección
                    // por ello hemos de cerrar el intervalo que haya pendiente pues ya no hay continuidad
                    if (first >= 0) // Hay un intervalo pendiente
                    {
                        if (isSelectionInterval)
                            addSelectionInterval(first,end);
                        else
                            removeSelectionInterval(first,end);
                    }
                    first = -1;
                    end = -1;
                }
            } // Fin del for

            // Cerramos el intervalo que haya quedado pendiente de añadir/quitar
            if (first >= 0) // Hay un intervalo pendiente
            {
                if (isSelectionInterval)
                    addSelectionInterval(first,end);
                else
                    removeSelectionInterval(first,end);
            }
        }
        finally
        {
            selModel.setValueIsAdjusting(false); // Envía un evento con todos los cambios
            selModel.setValueIsAdjusting(oldAdjusting); // Restaura
        }
    }
}

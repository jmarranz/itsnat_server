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

package org.itsnat.impl.comp.tree;

import java.io.Serializable;
import org.itsnat.impl.comp.list.ListSelectionModelMgrImpl;
import org.itsnat.core.ItsNatException;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * El modelo de selección de los nodos de un árbol es como una lista
 * considerando los nodos como filas (rows)
 *
 * @author jmarranz
 */
public class TreeSelectionModelMgrImpl implements TreeSelectionListener,ListSelectionListener,Serializable
{
    protected ItsNatTreeImpl tree;
    protected TreeSelectionModel treeSelection;
    protected ListSelectionModelMgrImpl listSelMgr = new ListSelectionModelMgrImpl(new DefaultListSelectionModel()); // Es interno no debe usarse fuera de la clase
    protected boolean synchTreeModelFromListModel = false; // temporalmente puede estar a true pero debe devolverse a false
    protected boolean synchListModelFromTreeModel = true;  // idem pero al revés
    protected int currentMode = -1;


    /**
     * Creates a new instance of TreeSelectionModelMgrImpl
     */
    public TreeSelectionModelMgrImpl(ItsNatTreeImpl tree,TreeSelectionModel treeSelection)
    {
        if (treeSelection == null)
            treeSelection = EmptyTreeSelectionModelImpl.SINGLETON;

        this.tree = tree;
        this.treeSelection = treeSelection;

        treeSelection.addTreeSelectionListener(this);
        listSelMgr.getListSelectionModel().addListSelectionListener(this);

        syncSelectionMode();

        //syncSelectionModelWithDataModel();
    }

    public void dispose()
    {
        treeSelection.removeTreeSelectionListener(this);
        listSelMgr.dispose();
    }

    public TreeSelectionModel getTreeSelectionModel()
    {
        return treeSelection;
    }

    private ListSelectionModelMgrImpl getListSelectionModelMgr()
    {
        // NO usar el ListSelectionModel fuera de esta clase

        syncSelectionMode();

        return listSelMgr;
    }

    private void syncSelectionMode()
    {
        int newMode = treeSelection.getSelectionMode();
        if (currentMode != newMode)
        {
            this.currentMode = newMode;
            int newListMode;
            switch(newMode)
            {
                case TreeSelectionModel.SINGLE_TREE_SELECTION:
                    newListMode = ListSelectionModel.SINGLE_SELECTION;
                    break;
                case TreeSelectionModel.CONTIGUOUS_TREE_SELECTION:
                    newListMode = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
                    break;
                case TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION:
                    newListMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
                    break;
                default: throw new ItsNatException("INTERNAL ERROR");
            }

            listSelMgr.getListSelectionModel().setSelectionMode(newListMode);
        }
    }

    public void syncSelectionModelWithDataModel()
    {
        removeAllUpdateModel();

        int size = tree.getRowCount();
        // synchTreeModelFromListModel debe ser false

        getListSelectionModelMgr().syncWithDataModel(size);
    }

    public boolean isRowSelected(int row)
    {
        return getListSelectionModelMgr().getListSelectionModel().isSelectedIndex(row);
    }

    public void changeSelectionModel(int row,boolean toggle, boolean extend, boolean selected)
    {
        // A través del list model actualizaremos después el tree model

        // El comportamiento de la selección en un JList/JTable es básicamente
        // el mismo que en un JTree:
        // BasicTreeUI.selectPathForEvent(TreePath path, MouseEvent event)
        this.synchTreeModelFromListModel = true;
        // los cambios se notificarán a través de eventos
        getListSelectionModelMgr().changeSelectionModel(row,toggle,extend,selected);
        this.synchTreeModelFromListModel = false;
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        if (tree.isExpandsSelectedPaths())
        {
            TreePath[] paths = e.getPaths();
            for(int i = 0; i < paths.length; i++)
                tree.expandPath(paths[i]);
        }

        // Actualizar el list sel. model con los cambios en el modelo
        if (!synchListModelFromTreeModel) return;

        boolean oldState = synchTreeModelFromListModel;
        this.synchTreeModelFromListModel = false; // por si acaso (actualización recursiva al cambiar el list selection)

        ListSelectionModelMgrImpl listSelModelMgr = getListSelectionModelMgr();
        ListSelectionModel listSelection = listSelModelMgr.getListSelectionModel();

        TreePath[] paths = e.getPaths();
        int[] rows = tree.getRowsForPaths(paths);

        boolean oldAdjusting = listSelection.getValueIsAdjusting();
        listSelection.setValueIsAdjusting(true);  // Evita procesar muchos eventos para procesar al final todos en uno
        try
        {
            for(int i = 0; i < paths.length; i++)
            {
                int row = rows[i];


                if (row >= 0)
                {
                    if (e.isAddedPath(i)) // es más eficiente que pasar el TreePath
                    {
                        listSelModelMgr.addSelectionInterval(row,row);
                    }
                    else
                    {
                        listSelModelMgr.removeSelectionInterval(row,row);
                    }
                }
                else // No se visualiza por tanto no puede estar seleccionado
                {
                    throw new ItsNatException("A hidden node can not be selected/unselected",e);
                }
            }
        }
        finally
        {
            listSelection.setValueIsAdjusting(false); // Envía un único evento con todos los cambios
            listSelection.setValueIsAdjusting(oldAdjusting); // Restaura
        }

        this.synchTreeModelFromListModel = oldState;
    }

    public void valueChanged(ListSelectionEvent e)
    {
        // Actualizar el tree sel. model con los cambios
        if (!synchTreeModelFromListModel) return;

        ListSelectionModel listSelection = getListSelectionModelMgr().getListSelectionModel();
        if (listSelection.getValueIsAdjusting())
            return;

        boolean oldState = synchListModelFromTreeModel;
        this.synchListModelFromTreeModel = false; // por si acaso (actualización recursiva al cambiar el tree selection)

        if (listSelection.isSelectionEmpty())
            treeSelection.clearSelection();
        else
        {
            if (treeSelection.getSelectionMode() == TreeSelectionModel.CONTIGUOUS_TREE_SELECTION)
            {
                // En este caso no podemos ir uno a uno porque al quitar uno podemos
                // quitar otro recién añadido al romperse temporalmente la continuidad (cuando un posible siguiente también será removido)
                if (listSelection.isSelectionEmpty())
                    treeSelection.clearSelection();
                else
                {
                    int first = listSelection.getMinSelectionIndex();
                    int last = listSelection.getMaxSelectionIndex();
                    TreePath[] paths = new TreePath[last - first + 1];
                    for(int i = first; i <= last; i++)
                    {
                        TreePath path = tree.getTreePathForRow(i);
                        paths[i - first] = path;
                    }
                    treeSelection.setSelectionPaths(paths); // Así si apenas cambia uno será lo suficientemente inteligente para notificar sólo ese cambio
                }
            }
            else
            {
                // Resto de los casos (single, multiple no continuo)
                int firstRow = e.getFirstIndex();
                int lastRow = e.getLastIndex();
                for(int i = firstRow; i <= lastRow; i++)
                {
                    TreePath path = tree.getTreePathForRow(i);
                    // No debe ser null path, pues sería un signo de desincronización
                    boolean selected = listSelection.isSelectedIndex(i);
                    if (selected)
                        treeSelection.addSelectionPath(path);
                    else
                        treeSelection.removeSelectionPath(path);
                }
            }
        }

        this.synchListModelFromTreeModel = oldState;
    }

    public void insertElementUpdateModel(int i,TreePath parentPath)
    {
        TreePath path = tree.toTreePath(i,parentPath);
        insertElementUpdateModel(path);
    }

    public void insertElementUpdateModel(TreePath path)
    {
        int row = tree.getRowForPath(path);
        if (row >= 0)
        {
            getListSelectionModelMgr().insertElementUpdateModel(row);
            // Ahora los hijos del nuevo nodo
            insertAllChildElementsUpdateModel(path);
        }
    }

    public void insertAllChildElementsUpdateModel(TreePath parentPath)
    {
        Object parentNode = parentPath.getLastPathComponent();
        TreeModel dataModel = tree.getTreeModel();
        int len = dataModel.getChildCount(parentNode);
        for(int i = 0; i < len; i++)
        {
            Object childNode = dataModel.getChild(parentNode,i);
            //TreePath childPath = parentPath.pathByAddingChild(childNode);
            insertElementUpdateModel(i,parentPath);
        }
    }

    public void removeElementUpdateModel(int i,Object childNode,TreePath parentPath)
    {
        int parentRow = tree.getRowForPath(parentPath);
        if (parentRow == -1) return;
        TreePath childPath = parentPath.pathByAddingChild(childNode);
        int rows = tree.getRowCountSubTree(childPath);
        if (rows == 0) return; // Si es 0 es que el nodo que se borra no se ve

        int rowStart = parentRow + i + 1;
        int rowEnd = rowStart + rows - 1; // quitamos 1 pues se incluye el propio nodo en la cuenta de getRowCountSubTree()

        removeRangeUpdateModel(rowStart,rowEnd);
    }

    private void removeRangeUpdateModel(int rowStart,int rowEnd)
    {
        // En este caso no hay problema respecto a llamarse este método antes
        // o después de eliminar los elementos DOM relacionados,
        // porque aunque ciertamente el ListSelectionModel.removeRangeUpdateModel
        // lanzará un evento en caso de "corrimiento" usando ya la nueva numeración,
        // sin embargo dicho evento es sólo recibido por el TreeSelecionModelMgrImpl
        // el cual no tiene que hacer nada porque la selección la recuerda por tree nodes
        // y no por índices de filas por lo que el corrimiento de índices no afecta al TreeSelectionModel
        // De todas formas es mejor llamar DESPUES de eliminar el DOM para seguir la filosofía
        // seguida con el ListSelectionModel
        getListSelectionModelMgr().removeRangeUpdateModel(rowStart,rowEnd);
    }

    public void removeAllChildElementsUpdateModel(TreePath path)
    {
        // Esta es la situación: los nodos por debajo de path
        // han cambiado profundamente, puede que esté vacío o tenga
        // nodos que no tienen nada que ver con lo que el selection
        // recordaba, por lo que no tiene sentido
        // recorrerlos, tenemos que quitar las rows que ocupaban los antiguos
        // nodos como si path no tuviera hijos.
        // Lo que hacemos es averiguar el row del nodo anterior
        // y el posterior al path y así sabemos cuantos rows
        // ocupaban tanto el propio path como los hijos

        if (path == null) // Es el caso del propio root
        {
            removeRootUpdateModel();
        }
        else
        {
            TreePath prevPath = tree.getPreviousPath(path);

            int rowStart,rowEnd;
            if (prevPath == null) // es el root pues no hay hijo anterior ni padre (no hay nodo "antes")
            {
                int rootRow = tree.getRowForPath(path);
                if (rootRow == -1)
                    return;  // No es visible
                rowStart = 1;
                int rows = getListSelectionModelMgr().getSize();
                rowEnd = rows - 1;
                if (rowStart > rowEnd) // no tuvo hijos
                    return;
            }
            else
            {
                int rowPrev = tree.getRowForPath(prevPath);
                if (rowPrev == -1) // Si el nodo anterior o el padre no tiene row (no se ve) el siguiente o el hijo tampoco, nada que hacer
                    return;
                rowStart = rowPrev + 2; // pues el propio nodo no hay que quitarlo

                TreePath nextPath = tree.getNextPath(path,false); // los hijos no se incluyen
                if (nextPath != null)
                {
                    int rowNext = tree.getRowForPath(nextPath);
                    // En este contexto rowNext no puede ser -1 aunque sea el siguiente del padre (o del padre del padre etc) pues path tiene row por tanto es visible (pensar)
                    rowEnd = rowNext - 1;
                }
                else // Es el último, pero pudo tener hijos los cuales cuentan como rows
                {
                    int rows = getListSelectionModelMgr().getSize();
                    rowEnd = rows - 1;
                    if (rowStart > rowEnd) // no tuvo hijos
                        return;
                }
            }

            removeRangeUpdateModel(rowStart,rowEnd);
        }
    }

    public void removeRootUpdateModel()
    {
        boolean syncTreeOld = this.synchTreeModelFromListModel;
        this.synchTreeModelFromListModel = false;

        getListSelectionModelMgr().removeAllUpdateModel();

        this.synchTreeModelFromListModel = syncTreeOld;


        boolean syncListOld = this.synchListModelFromTreeModel;
        this.synchListModelFromTreeModel = false;

        treeSelection.clearSelection();

        this.synchListModelFromTreeModel = syncListOld;
    }

    public void removeAllUpdateModel()
    {
        // getListSelectionModelMgr().removeAllUpdateModel();
        treeSelection.clearSelection(); // Yo creo que sobra pero por si acaso
    }
}

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

import java.util.ArrayList;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.itsnat.comp.ItsNatComponentUI;
import org.itsnat.comp.tree.ItsNatTree;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.comp.tree.ItsNatTreeCellUI;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.comp.tree.ItsNatTreeUI;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatException;
import org.itsnat.core.NameValue;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.comp.*;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByClientJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersByDocJoystickImpl;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersJoystick;
import org.itsnat.impl.comp.listener.ItsNatCompDOMListenersJoystickSharedImpl;
import org.itsnat.impl.comp.listener.JoystickModeComponent;
import org.itsnat.impl.comp.mgr.ItsNatDocComponentManagerImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentImpl;
import org.itsnat.impl.core.domutil.DOMUtilInternal;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.MouseEvent;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatTreeImpl extends ItsNatElementComponentImpl implements ItsNatTree, ItsNatComponentInternal, TreeModelListener, JoystickModeComponent
{
    protected boolean enabled = true;
    protected TreeSelectionModelMgrImpl selModelMgr;
    protected ItsNatTreeCellRenderer renderer;
    protected TreeCellEditorProcessor editorProcessor = new TreeCellEditorProcessor(this);
    protected EventListenerList expansionListenerList = new EventListenerList();
    protected EventListenerList willExpandListenerList = new EventListenerList();
    protected boolean expandsSelectedPaths = false;
    protected boolean treeTable; // No puede cambiar
    protected boolean rootless; // No puede cambiar
    protected ItsNatTreeStructure structure;
    protected int toggleClickCount = 2;
    protected DefaultRowMapperImpl rowMapper = new DefaultRowMapperImpl(this);
    protected boolean selectionUsesKeyboard;

    /**
     * Creates a new instance of ItsNatTreeImpl
     */
    public ItsNatTreeImpl(boolean treeTable, boolean rootless, Element element, ItsNatTreeStructure structure, NameValue[] artifacts, ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element, artifacts, componentMgr);

        constructor(treeTable,rootless,structure);
    }

    public ItsNatTreeImpl(Element element, NameValue[] artifacts, ItsNatDocComponentManagerImpl componentMgr)
    {
        super(element, artifacts, componentMgr);

        constructor(getBooleanArtifactOrAttribute("treeTable", false),
                    getBooleanArtifactOrAttribute("rootless", false),
                    null);
    }

    public void constructor(boolean treeTable, boolean rootless,ItsNatTreeStructure structure)
    {
        this.selectionUsesKeyboard = getDefaultSelectionOnComponentsUsesKeyboard();

        this.treeTable = treeTable;
        this.rootless = rootless;
        this.structure = structure != null ? structure : (ItsNatTreeStructure)getDeclaredStructure(ItsNatTreeStructure.class);
    }

    public ItsNatCompDOMListenersByDocImpl createItsNatCompDOMListenersByDoc()
    {
        return new ItsNatCompDOMListenersByDocJoystickImpl(this);
    }

    public ItsNatCompDOMListenersByClientImpl createItsNatCompDOMListenersByClient(ClientDocumentImpl clientDoc)
    {
        return new ItsNatCompDOMListenersByClientJoystickImpl(this,clientDoc);
    }

    @Override
    public void enableEventListenersByDoc()
    {
        super.enableEventListenersByDoc();

        enableEventListener("click");
        enableEventListener("dblclick"); // Para cuando se pulsa el icon folder con doble click

        editorProcessor.enableEventListenersByDoc();
    }

    @Override
    public void disableEventListenersByDoc(boolean updateClient)
    {
        super.disableEventListenersByDoc(updateClient);

        editorProcessor.disableEventListeners(updateClient);
    }

    public boolean isTreeTable()
    {
        return treeTable;
    }

    @Override
    public void init()
    {
        ItsNatDocComponentManagerImpl compMgr = getItsNatComponentManagerImpl();

        setItsNatTreeCellRenderer(compMgr.createDefaultItsNatTreeCellRenderer());

        super.init();

        setItsNatTreeCellEditor(compMgr.createDefaultItsNatTreeCellEditor(null));
    }

    public boolean isJoystickMode()
    {
        return getItsNatCompDOMListenersByDocJoystick().isJoystickEnabled();
    }

    public void setJoystickMode(boolean value)
    {
        getItsNatCompDOMListenersByDocJoystick().setJoystickEnabled(value);
    }

    /* Esté método se hará público en el futuro */
    public boolean isJoystickMode(ClientDocument clientDoc)
    {
        return getItsNatCompDOMListenersByClientJoystick((ClientDocumentImpl)clientDoc).isJoystickEnabled();
    }

    /* Esté método se hará público en el futuro */
    public void setJoystickMode(ClientDocument clientDoc,boolean value)
    {
        getItsNatCompDOMListenersByClientJoystick((ClientDocumentImpl)clientDoc).setJoystickEnabled(value);
    }

    public ItsNatCompDOMListenersByDocJoystickImpl getItsNatCompDOMListenersByDocJoystick()
    {
        return (ItsNatCompDOMListenersByDocJoystickImpl)domListenersByDoc;
    }

    public ItsNatCompDOMListenersByClientJoystickImpl getItsNatCompDOMListenersByClientJoystick(ClientDocumentImpl clientDoc)
    {
        return (ItsNatCompDOMListenersByClientJoystickImpl)getItsNatCompDOMListenersByClient(clientDoc);
    }

    @Override
    public void unbindModels()
    {
        unsetTreeSelectionModel();

        super.unbindModels();
    }

    public void unbindDataModel()
    {
        TreeModel dataModel = getTreeModel();
        dataModel.removeTreeModelListener(this);
    }

    @Override
    public void setDefaultModels()
    {
        super.setDefaultModels();

        // Después de la iniciación del data model

        TreeSelectionModel defSelModel = new DefaultTreeSelectionModel();
        defSelModel.setRowMapper(getRowMapper());
        setTreeSelectionModel(defSelModel);
    }

    public boolean isRootless()
    {
        return rootless;
    }

    public int getToggleClickCount()
    {
        return toggleClickCount;
    }

    public void setToggleClickCount(int toggleClickCount)
    {
        if ((toggleClickCount < 0) || (toggleClickCount > 2))
            throw new ItsNatException("Bad value, must be between 0-2: " + toggleClickCount,this);

        this.toggleClickCount = toggleClickCount;
    }

    public boolean mustToggleExpansionStateNode(String evtType)
    {
        // Hacemos lo mismo que en el JTree
        // Notar que el valor 0 (permitido) desactiva la expansión
        int toggleClickCount = getToggleClickCount();
        return (((toggleClickCount == 1) && evtType.equals("click") && evtType.equals("mouseup")) ||
                ((toggleClickCount == 2) && evtType.equals("dblclick")));
    }

    public ItsNatTreeStructure getItsNatTreeStructure()
    {
        return structure;
    }

    public Object createDefaultStructure()
    {
        return getItsNatComponentManager().createDefaultItsNatTreeStructure();
    }

    public TreeCellEditorProcessor getTreeCellEditorProcessor()
    {
        return editorProcessor;
    }

    public void addTreeExpansionListener(TreeExpansionListener tel)
    {
        expansionListenerList.add(TreeExpansionListener.class, tel);
    }

    public void removeTreeExpansionListener(TreeExpansionListener tel)
    {
        expansionListenerList.remove(TreeExpansionListener.class, tel);
    }

    public boolean hasTreeExpansionListeners()
    {
        return expansionListenerList.getListenerCount() > 0;
    }

    public TreeExpansionListener[] getTreeExpansionListeners()
    {
        return expansionListenerList.getListeners(TreeExpansionListener.class);
    }

    public void addTreeWillExpandListener(TreeWillExpandListener tel)
    {
        willExpandListenerList.add(TreeWillExpandListener.class, tel);
    }

    public void removeTreeWillExpandListener(TreeWillExpandListener tel)
    {
        willExpandListenerList.remove(TreeWillExpandListener.class, tel);
    }

    public boolean hasTreeWillExpandListeners()
    {
        return willExpandListenerList.getListenerCount() > 0;
    }

    public TreeWillExpandListener[] getTreeWillExpandListeners()
    {
        return willExpandListenerList.getListeners(TreeWillExpandListener.class);
    }

    public ItsNatTreeUI getItsNatTreeUI()
    {
        return (ItsNatTreeUI) compUI;
    }

    public ItsNatTreeUIImpl getItsNatTreeUIImpl()
    {
        return (ItsNatTreeUIImpl) compUI;
    }
    
    public ItsNatTreeUI createDefaultItsNatTreeUI()
    {
        return new ItsNatTreeUIImpl(this);
    }

    public ItsNatComponentUI createDefaultItsNatComponentUI()
    {
        return createDefaultItsNatTreeUI();
    }

    public void bindDataModel()
    {
        // A partir de ahora los cambios los repercutimos en el DOM por eventos
        // No se debe cambiar el DOM select por otra vía que por el objeto dataModel del usuario (si es mutable)
        TreeModel dataModel = getTreeModel();
        dataModel.addTreeModelListener(this);
    }

    @Override
    public void initialSyncWithDataModel()
    {
        super.initialSyncWithDataModel();

        // El dataModel del usuario manda sobre el DOM
        // La sincronización del Selection Model y del UI van separados en el caso de Trees

        if (selModelMgr != null)
            selModelMgr.syncSelectionModelWithDataModel();
    }

    public void initialSyncUIWithDataModel()
    {
        // Elimina todos los nodos incluido el root
        removeRootDOMInternal();

        TreeModel dataModel = getTreeModel();
        Object rootNode = dataModel.getRoot();
        addRootDOMInternal(rootNode);
    }

    public Object createDefaultModelInternal()
    {
        return createDefaultTreeModel();
    }

    public TreeModel createDefaultTreeModel()
    {
        return new DefaultTreeModel(null);
    }

    public TreeSelectionModelMgrImpl getTreeSelectionModelMgr()
    {
        return selModelMgr;
    }

    public TreeSelectionModel getTreeSelectionModel()
    {
        if (selModelMgr == null)
            return null;
        return selModelMgr.getTreeSelectionModel();
    }

    public void setTreeSelectionModel(TreeSelectionModel selectionModel)
    {
        unsetTreeSelectionModel();

        this.selModelMgr = new TreeSelectionModelMgrImpl(this, selectionModel);
    }

    public void unsetTreeSelectionModel()
    {
        if (selModelMgr != null)
            selModelMgr.dispose(); // Aunque sea el mismo "reseteamos"
    }

    public ItsNatTreeCellRenderer getItsNatTreeCellRenderer()
    {
        return renderer;
    }

    public void setItsNatTreeCellRenderer(ItsNatTreeCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public TreeModel getTreeModel()
    {
        return (TreeModel) dataModel;
    }

    public void setTreeModel(TreeModel dataModel)
    {
        setDataModel(dataModel);
    }

    public ItsNatTreeCellEditor getItsNatTreeCellEditor()
    {
        TreeCellEditorProcessor editorProcessor = getTreeCellEditorProcessor();
        return editorProcessor.getItsNatTreeCellEditor();
    }

    public void setItsNatTreeCellEditor(ItsNatTreeCellEditor cellEditor)
    {
        editorProcessor.setItsNatTreeCellEditor(cellEditor);
    }

    public void addRootDOMInternal(Object rootNode)
    {
        if (rootNode == null) return;

        getItsNatTreeUIImpl().addRootNode(rootNode);

        addInternalEventListenerJoystickMode();

        if (selModelMgr != null)
            selModelMgr.insertElementUpdateModel(new TreePath(rootNode));
    }

    public void removeRootDOMInternal()
    {
        if (selModelMgr != null)
            selModelMgr.removeRootUpdateModel();

        removeInternalEventListenerJoystickMode();

        getItsNatTreeUIImpl().removeRootNode();
    }

    public void insertTreeNodeDOMInternal(int index,TreePath parentPath)
    {
        getItsNatTreeUIImpl().insertTreeNodeAt(index, parentPath);

        addInternalEventListenerJoystickMode(index,parentPath);

        if (selModelMgr != null)  // en este caso es casi seguro que es no nulo
            selModelMgr.insertElementUpdateModel(index, parentPath);
    }

    public void removeAllChildTreeNodesDOMInternal(TreePath parentPath)
    {
        removeInternalEventListenerJoystickModeChildNodes(parentPath);

        getItsNatTreeUIImpl().removeAllChildTreeNodes(parentPath);

        if (selModelMgr != null)  // en este caso es casi seguro que es no nulo
            selModelMgr.removeAllChildElementsUpdateModel(parentPath);
    }

    public void removeTreeNodeDOMInternal(int index,Object childNode,TreePath parentPath)
    {
        removeInternalEventListenerJoystickMode(index,parentPath);

        getItsNatTreeUIImpl().removeTreeNodeAt(index, parentPath);

        if (selModelMgr != null)  // en este caso es casi seguro que es no nulo
            selModelMgr.removeElementUpdateModel(index, childNode, parentPath);
    }

    public void treeStructureChanged(TreeModelEvent e)
    {
        TreePath parentPath = e.getTreePath();
        if (parentPath == null)
        {
            // Es el caso de eliminación del root
            removeRootDOMInternal();
        }
        else
        {
            int[] indices;
            Object[] children;

            if (parentPath.getPathCount() == 1) // El root ha sido insertado (y seguramente todo el árbol)
            {
                removeRootDOMInternal();

                indices = null;
                children = null;
            }
            else
            {
                removeAllChildTreeNodesDOMInternal(parentPath);

                Object parentNode = parentPath.getLastPathComponent();
                TreeModel dataModel = getTreeModel();
                int len = dataModel.getChildCount(parentNode);
                children = new Object[len];
                indices = new int[len];
                for (int i = 0; i < len; i++)
                {
                    Object childNode = dataModel.getChild(parentNode, i);
                    children[i] = childNode;
                    indices[i] = i;
                }
            }

            TreeModelEvent eIns = new TreeModelEvent(e.getSource(), e.getPath(), indices, children);
            treeNodesInserted(eIns);
        }
    }

    public void treeNodesRemoved(TreeModelEvent e)
    {
        TreePath parentPath = e.getTreePath();
        int[] indices = e.getChildIndices();
        if (indices != null)
        {
            Object[] children = e.getChildren(); // Hay que tener en cuenta que YA no son hijos del nodo padre, esta es la única forma de obtenerlos

            for (int i = 0; i < indices.length; i++)
            {
                int index = indices[i];
                Object childNode = children[i];
                removeTreeNodeDOMInternal(index,childNode,parentPath);
            }
        }
        else // es el root
        {
            removeRootDOMInternal();
        }
    }

    public void treeNodesInserted(TreeModelEvent e)
    {
        TreePath parentPath = e.getTreePath();
        int[] indices = e.getChildIndices();
        if (indices != null)
        {
            for (int i = 0; i < indices.length; i++)
            {
                int index = indices[i];
                insertTreeNodeDOMInternal(index,parentPath);
            }
        }
        else
        {
            Object rootNode = parentPath.getLastPathComponent();
            addRootDOMInternal(rootNode);
        }
    }

    public void treeNodesChanged(TreeModelEvent e)
    {
        ItsNatTreeUIImpl treeUI = getItsNatTreeUIImpl();
        TreePath parentPath = e.getTreePath();
        int[] indices = e.getChildIndices();
        if (indices != null)
        {
            for (int i = 0; i < indices.length; i++)
            {
                int index = indices[i];
                TreePath path = toTreePath(index, parentPath);
                treeUI.setTreeNodeValueAt(path, false);
            }
        }
        else
        {
            treeUI.setTreeNodeValueAt(parentPath, false);
        }
    }

    public TreePath toTreePath(int i, TreePath parentPath)
    {
        TreeModel dataModel = getTreeModel();
        Object parentNode = parentPath.getLastPathComponent();
        Object childNode = dataModel.getChild(parentNode, i);
        return parentPath.pathByAddingChild(childNode);
    }

    @Override
    public void processDOMEvent(Event evt)
    {
        String type = evt.getType();
        if (type.equals("click") || type.equals("dblclick") || type.equals("mouseup"))
        {
            Node nodeClicked = (Node) evt.getTarget(); // Puede ser un nodo interior del elemento pulsado

            ItsNatTreeUI compUI = getItsNatTreeUIImpl();
            ItsNatTreeCellUI nodeInfo = compUI.getItsNatTreeCellUIFromNode(nodeClicked);
            if (nodeInfo != null)
            {
                // Vemos qué parte se ha pulsado
                Element parentElem = nodeInfo.getParentElement(); // para acelerar la búsqueda

                if (parentElem == nodeClicked) // Raro, seguramente el evento ha sido enviado por código
                {
                    // No sabemos que hacer (expandir, no expandir etc) al menos seleccionamos el nodo pues es seguramente lo que se pretende al enviar el click a este elemento
                    selectTreeNode(nodeInfo, evt);
                }
                else if (DOMUtilInternal.isChildOrSame(nodeClicked, nodeInfo.getHandleElement(), parentElem))
                {
                    // Handle pulsado
                    toggleExpansionStateNode(nodeInfo);
                }
                else if (DOMUtilInternal.isChildOrSame(nodeClicked, nodeInfo.getIconElement(), parentElem))
                {
                    // Icon pulsado
                    // Hacemos lo mismo que en el JTree
                    if (mustToggleExpansionStateNode(type))
                    {
                        toggleExpansionStateNode(nodeInfo);
                    }
                    selectTreeNode(nodeInfo, evt);
                }
                else if (DOMUtilInternal.isChildOrSame(nodeClicked, nodeInfo.getLabelElement(), parentElem))
                {
                    // Contenido pulsado
                    // Hacemos lo mismo que en el JTree, pero evitamos hacer la expansión
                    // si el evento coincide con el que activa el editor (queda feo ambas cosas mezcladas)
                    if (mustToggleExpansionStateNode(type) &&
                            ((getItsNatTreeCellEditor() == null) ||
                            !type.equals(getEditorActivatorEvent())))
                    {
                        toggleExpansionStateNode(nodeInfo);
                    }
                    selectTreeNode(nodeInfo, evt);
                }
            }
        }

        super.processDOMEvent(evt);
    }

    public void selectTreeNode(ItsNatTreeCellUI nodeInfo, Event evt)
    {
        int row = nodeInfo.getRow();
        if (row >= 0) // por si acaso
        {
            MouseEvent mouseEvt = (MouseEvent) evt;
            boolean toggle;
            if (!isSelectionUsesKeyboard())
                toggle = true;
            else
                toggle = mouseEvt.getCtrlKey();

            boolean extend = mouseEvt.getShiftKey();
            boolean selected = selModelMgr.isRowSelected(row);
            selModelMgr.changeSelectionModel(row, toggle, extend, selected);
        }
    }

    public TreePath getPreviousPath(TreePath path)
    {
        // Devolvemos el nodo previo como path en el sentido inverso de recorrido del árbol
        TreePath prevSiblingPath = getPreviousSiblingPath(path);
        if (prevSiblingPath != null)
            return prevSiblingPath; // Devolvemos el propio padre

        return path.getParentPath(); // Puede ser null, no hay padre, caso de Root
    }

    public TreePath getPreviousSiblingPath(TreePath path)
    {
        // Devolvemos el nodo previo en el mismo nivel
        if (path.getParentPath() == null)
            return null; // No hay anterior , es el root

        TreeModel dataModel = getTreeModel();
        Object dataNode = path.getLastPathComponent();
        TreePath parentPath = path.getParentPath();
        Object parentNode = parentPath.getLastPathComponent();
        int index = dataModel.getIndexOfChild(parentNode, dataNode);
        if (index > 0) // tiene nodo previo
        {
            Object prevNode = dataModel.getChild(parentNode, index - 1);
            return parentPath.pathByAddingChild(prevNode);
        }
        else
        {
            // Es el primero
            return null;
        }
    }

    public TreePath getNextPath(TreePath path)
    {
        return getNextPath(path, true);
    }

    public TreePath getNextPath(TreePath path, boolean childIncluded)
    {
        // Devolvemos el nodo siguiente: o el primer hijo o en el mismo nivel o superior del path en el sentido directo de recorrido del árbol

        if (childIncluded)
        {
            // Devolvemos el primer hijo si tiene
            TreeModel dataModel = getTreeModel();
            Object dataNode = path.getLastPathComponent();
            int count = dataModel.getChildCount(dataNode);
            if (count > 0)
            {
                Object childNode = dataModel.getChild(dataNode, 0);
                return path.pathByAddingChild(childNode);
            }
        }

        TreePath siblingPath = getNextSiblingPath(path);
        if (siblingPath != null)
        {
            return siblingPath;
        }
        TreePath parentPath = path.getParentPath();
        if (parentPath == null)
        {
            return null; // Es el root, no hay padre
            // Es el último, devolvemos el nodo siguiente al del padre
            // y si no el padre del padre etc
            // childIncluded es false para no meternos en los hijos del padre de nuevo
        }
        return getNextPath(parentPath, false);
    }

    public TreePath getNextSiblingPath(TreePath path)
    {
        // Devolvemos el nodo siguiente en el mismo nivel
        if (path.getPathCount() == 1)
            return null; // Es el root, no hay siguiente en el mismo nivel ni hay padre

        TreeModel dataModel = getTreeModel();
        Object dataNode = path.getLastPathComponent();

        // Devolvemos el siguiente en el mismo nivel que path
        TreePath parentPath = path.getParentPath();
        Object parentNode = parentPath.getLastPathComponent();
        int count = dataModel.getChildCount(parentNode);
        int index = dataModel.getIndexOfChild(parentNode, dataNode);
        if (count > index + 1) // tiene nodo siguiente
        {
            Object nextNode = dataModel.getChild(parentNode, index + 1);
            return parentPath.pathByAddingChild(nextNode);
        }
        else
        {
            // Es el último
            return null;
        }
    }

    public TreePath getLastPath()
    {
        TreeModel dataModel = getTreeModel();
        Object root = dataModel.getRoot();
        if (root == null)
            return null;
        TreePath rootPath = new TreePath(root);
        return getLastPathSubTree(rootPath);
    }

    public TreePath getLastPathSubTree(TreePath path)
    {
        if (path == null)
            return null;
        Object node = path.getLastPathComponent();
        TreeModel dataModel = getTreeModel();
        int count = dataModel.getChildCount(node);
        if (count == 0)
            return path;
        Object childNode = dataModel.getChild(node, count - 1);
        TreePath childPath = path.pathByAddingChild(childNode);
        return getLastPathSubTree(childPath);
    }

    public int getTreeNodeCount()
    {
        TreeModel dataModel = getTreeModel();
        Object root = dataModel.getRoot();
        return getTreeNodeCount(root);
    }

    public int getTreeNodeCount(Object node)
    {
        if (node == null)
            return 0;

        int count = 1; // propio nodo
        TreeModel dataModel = getTreeModel();
        int childCount = dataModel.getChildCount(node);
        for (int i = 0; i < childCount; i++)
        {
            Object child = dataModel.getChild(node, i);
            count += getTreeNodeCount(child);
        }
        return count;
    }

    public void startEditingAtPath(TreePath path)
    {
        getTreeCellEditorProcessor().startEdition(path);
    }

    public void startEditingAtRow(int row)
    {
        getTreeCellEditorProcessor().startEdition(row);
    }

    public boolean isEditing()
    {
        return getTreeCellEditorProcessor().isEditing();
    }

    public TreePath getEditingPath()
    {
        return getTreeCellEditorProcessor().getTreePath();
    }

    public int getEditingRow()
    {
        return getTreeCellEditorProcessor().getRow();
    }

    public String getEditorActivatorEvent()
    {
        return getTreeCellEditorProcessor().getEditorActivatorEvent();
    }

    public void setEditorActivatorEvent(String editorActivatorEvent)
    {
        getTreeCellEditorProcessor().setEditorActivatorEvent(editorActivatorEvent);
    }

    public boolean isEditingEnabled()
    {
        return getTreeCellEditorProcessor().isEditingEnabled();
    }

    public void setEditingEnabled(boolean value)
    {
        getTreeCellEditorProcessor().setEditingEnabled(value);
    }

    public void setExpandsSelectedPaths(boolean newValue)
    {
        this.expandsSelectedPaths = newValue;
    }

    public boolean isExpandsSelectedPaths()
    {
        return expandsSelectedPaths;
    }

    public ParamTransport[] getInternalParamTransports(String type,ClientDocumentImpl clientDoc)
    {
        return null;
    }

    public Node createDefaultNode()
    {
        throw new ItsNatException("There is no default Element and later attachment is not allowed",this);
    }

    public boolean isExpandedNode(TreePath treePath)
    {
        ItsNatTreeCellUI nodeInfo = getItsNatTreeUIImpl().getItsNatTreeCellUIFromTreePath(treePath);
        if (nodeInfo == null) return true; // Es el caso de nodo root en rootless mode
        return nodeInfo.isExpanded();
    }

    public void expandNode(TreePath treePath)
    {
        ItsNatTreeCellUI nodeInfo = getItsNatTreeUIImpl().getItsNatTreeCellUIFromTreePath(treePath);
        if (nodeInfo == null) return; // Es el caso de nodo root en rootless mode
        expandNode(nodeInfo);
    }

    public void collapseNode(TreePath treePath)
    {
        ItsNatTreeCellUI nodeInfo = getItsNatTreeUIImpl().getItsNatTreeCellUIFromTreePath(treePath);
        if (nodeInfo == null) return; // Es el caso de nodo root en rootless mode
        collapseNode(nodeInfo);
    }

    public void toggleExpansionStateNode(TreePath treePath)
    {
        ItsNatTreeCellUI nodeInfo = getItsNatTreeUIImpl().getItsNatTreeCellUIFromTreePath(treePath);
        if (nodeInfo == null) return; // Es el caso de nodo root en rootless mode
        toggleExpansionStateNode(nodeInfo);
    }

    public void expandPath(TreePath path)
    {
        while (path != null)
        {
            expandNode(path);
            path = path.getParentPath();
        }
    }

    public void collapsePath(TreePath path)
    {
        while (path != null)
        {
            collapseNode(path);
            path = path.getParentPath();
        }
    }

    public void expandNode(ItsNatTreeCellUI nodeInfo)
    {
        expandCollapseNode(nodeInfo, true);
    }

    public void collapseNode(ItsNatTreeCellUI nodeInfo)
    {
        expandCollapseNode(nodeInfo, false);
    }

    public void toggleExpansionStateNode(ItsNatTreeCellUI nodeInfo)
    {
        expandCollapseNode(nodeInfo, !nodeInfo.isExpanded());
    }

    public void expandCollapseNode(ItsNatTreeCellUI treeCellUI, boolean expand)
    {
        boolean wasExpanded = treeCellUI.isExpanded();
        if (wasExpanded == expand)
            return; // No cambia el estado

        TreeExpansionEvent event = null;
        TreePath treePath = treeCellUI.getTreePath();
        if (hasTreeWillExpandListeners())
        {
            event = new TreeExpansionEvent(this, treePath);
            TreeWillExpandListener[] listeners = getTreeWillExpandListeners();
            try
            {
                for (int i = 0; i < listeners.length; i++)
                {
                    if (expand)
                        listeners[i].treeWillExpand(event);
                    else
                        listeners[i].treeWillCollapse(event);
                }
            }
            catch (ExpandVetoException ex)
            {
                // Cancelada la expansión/cancelación
                return;
            }
        }

        treeCellUI.expand(expand);

        // Ha cambiado el estado, notificamos
        if (hasTreeExpansionListeners())
        {
            if (event == null) // Como es un objeto de sólo lectura si ya se creó antes lo reutilizamos
                event = new TreeExpansionEvent(this, treePath);

            TreeExpansionListener[] listeners = getTreeExpansionListeners();
            for (int i = 0; i < listeners.length; i++)
            {
                if (expand)
                    listeners[i].treeExpanded(event);
                else
                    listeners[i].treeCollapsed(event);
            }
        }
    }

    public RowMapper getRowMapper()
    {
        return rowMapper;
    }

    public DefaultRowMapperImpl getDefaultRowMapper()
    {
        return rowMapper;
    }

    public TreePath getTreePathForRow(int row)
    {
        return rowMapper.getPathForRow(row);
    }

    public int[] getRowsForPaths(TreePath[] path)
    {
        return rowMapper.getRowsForPaths(path);
    }

    public int getRowCount()
    {
        return rowMapper.getRowCount();
    }

    public int getRowForPath(TreePath path)
    {
        return rowMapper.getRowForPath(path);
    }

    public int getRowCountSubTree(TreePath path)
    {
        if (path == null)
            return 0;
        return rowMapper.getRowCountSubTree(path.getLastPathComponent());
    }

    public void setSelectionUsesKeyboard(boolean value)
    {
        this.selectionUsesKeyboard = value;
    }

    public boolean isSelectionUsesKeyboard()
    {
        return selectionUsesKeyboard;
    }

    public Element[] getContentElementList(int fromRow,int toRow)
    {
        ItsNatTreeUI compUI = getItsNatTreeUIImpl();
        int len = toRow - fromRow + 1;
        Element[] elemList = new Element[len * 3]; // handle, icon, label
        for(int row = fromRow; row <= toRow; row++)
        {
            ItsNatTreeCellUI cellUI = compUI.getItsNatTreeCellUIFromRow(row);
            if (cellUI == null) continue; // por ejemplo en el caso rootless será null para row = 0

            int pos = (row - fromRow) * 3;
            elemList[pos] = cellUI.getHandleElement();
            elemList[pos + 1] = cellUI.getIconElement();
            elemList[pos + 2] = cellUI.getLabelElement();
        }
        return elemList;
    }

    public Element[] getContentElementList(int index,TreePath parentPath)
    {
        ItsNatTreeUI compUI = getItsNatTreeUIImpl();
        int fromRow = compUI.getRow(index,parentPath);
        int rowCount = compUI.getRowCount(index,parentPath);

        return getContentElementList(fromRow,fromRow + rowCount - 1);
    }

    public Element[] getContentElementList()
    {
        ItsNatTreeUI compUI = getItsNatTreeUIImpl();
        int len = compUI.getRowCount();
        return getContentElementList(0, len - 1);
    }

    public void removeInternalEventListenerJoystickModeChildNodes(TreePath parentPath)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        ItsNatTreeUI compUI = getItsNatTreeUIImpl();
        int fromRow = compUI.getRow(parentPath);
        fromRow++; // desde el primer hijo, el propio padre no se cuenta (no se elimina).
        int rowCount = compUI.getRowCount(parentPath);
        rowCount--; // porque el propio padre no se cuenta (no se elimina).
        Element[] elemList = getContentElementList(fromRow,fromRow + rowCount - 1);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickMode()
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementList();

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickMode()
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementList();

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public void addInternalEventListenerJoystickMode(int index,TreePath parentPath)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementList(index,parentPath);

        ItsNatCompDOMListenersJoystickSharedImpl.addEventListenerJoystick(domListeners, elemList);
    }

    public void removeInternalEventListenerJoystickMode(int index,TreePath parentPath)
    {
        ArrayList<ItsNatCompDOMListenersJoystick> domListeners = ItsNatCompDOMListenersJoystickSharedImpl.getMustAddRemove(this);
        if (domListeners.isEmpty())
            return;

        Element[] elemList = getContentElementList(index,parentPath);

        ItsNatCompDOMListenersJoystickSharedImpl.removeEventListenerJoystick(domListeners, elemList);
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        this.enabled = b;
    }

}

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.ButtonGroup;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormalLabel;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButtonLabel;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.core.ItsNatDOMException;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.button.toggle.ItsNatButtonGroupImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatFreeButtonNormalDefaultImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatFreeButtonNormalLabelImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatFreeCheckBoxDefaultImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatFreeCheckBoxLabelImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatFreeRadioButtonDefaultImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatFreeRadioButtonLabelImpl;
import org.itsnat.impl.comp.factory.inc.FactoryItsNatFreeIncludeImpl;
import org.itsnat.impl.comp.factory.label.FactoryItsNatFreeLabelImpl;
import org.itsnat.impl.comp.factory.layer.FactoryItsNatModalLayerImpl;
import org.itsnat.impl.comp.factory.list.FactoryItsNatFreeComboBoxImpl;
import org.itsnat.impl.comp.factory.list.FactoryItsNatFreeListMultSelImpl;
import org.itsnat.impl.comp.factory.table.FactoryItsNatFreeTableImpl;
import org.itsnat.impl.comp.factory.tree.FactoryItsNatFreeTreeImpl;
import org.itsnat.impl.comp.label.ItsNatLabelRendererDefaultImpl;
import org.itsnat.impl.comp.list.ItsNatListCellRendererDefaultImpl;
import org.itsnat.impl.comp.list.ItsNatListStructureDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatTableCellRendererDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatTableHeaderCellRendererDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatTableStructureDefaultImpl;
import org.itsnat.impl.comp.tree.ItsNatTreeCellRendererDefaultImpl;
import org.itsnat.impl.comp.tree.ItsNatTreeStructureDefaultImpl;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.domutil.NamespaceUtil;
import org.itsnat.impl.core.listener.AutoBuildCompBeforeAfterMutationRenderListener;
import org.itsnat.impl.core.mut.doc.DocMutationEventListenerImpl;
import org.itsnat.impl.core.servlet.ItsNatServletImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateImpl;
import org.itsnat.impl.core.template.ItsNatDocumentTemplateVersionImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatDocComponentManagerImpl implements ItsNatComponentManager,Serializable
{
    protected static final Map<String,FactoryItsNatComponentImpl> FACTORIES = new HashMap<String,FactoryItsNatComponentImpl>(); // No sincronizamos porque va a ser siempre usada en modo lectura
    static
    {
        addFactory(FactoryItsNatFreeButtonNormalDefaultImpl.SINGLETON);
        addFactory(FactoryItsNatFreeButtonNormalLabelImpl.SINGLETON);
        addFactory(FactoryItsNatFreeCheckBoxDefaultImpl.SINGLETON);
        addFactory(FactoryItsNatFreeCheckBoxLabelImpl.SINGLETON);
        addFactory(FactoryItsNatFreeComboBoxImpl.SINGLETON);
        addFactory(FactoryItsNatFreeIncludeImpl.SINGLETON);
        addFactory(FactoryItsNatFreeLabelImpl.SINGLETON);
        addFactory(FactoryItsNatFreeListMultSelImpl.SINGLETON);
        addFactory(FactoryItsNatFreeRadioButtonDefaultImpl.SINGLETON);
        addFactory(FactoryItsNatFreeRadioButtonLabelImpl.SINGLETON);
        addFactory(FactoryItsNatFreeTableImpl.SINGLETON);
        addFactory(FactoryItsNatFreeTreeImpl.SINGLETON);
        addFactory(FactoryItsNatModalLayerImpl.SINGLETON);
    }

    protected static void addFactory(FactoryItsNatComponentImpl factory)
    {
        FACTORIES.put(factory.getKey(),factory);
    }

    protected static FactoryItsNatComponentImpl getFactoryItsNatComponentStatic(String compName)
    {
        return FACTORIES.get(compName);
    }

    protected FactoryItsNatComponentImpl getFactoryItsNatComponent(Element elem,String compType)
    {
        return getFactoryItsNatComponentStatic(compType);
    }

    protected ItsNatDocumentImpl itsNatDoc;
    protected transient WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByName;  // Un ItsNatButtonGroup necesita estar asociado a un radio button si ninguno lo referencia se puede perder, así evitamos memory leaks por recreación de radio buttons
    protected transient WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByButtonGroup;  // "
    protected Map<Node,ItsNatComponent> components;
    protected Map<Node,Object> excludedNodesAsComponents;
    protected boolean selectionOnComponentsUsesKeyboard;
    protected boolean markupDrivenComponents;
    protected boolean autoBuildComponents;
    protected LinkedList<CreateItsNatComponentListener> createCompListeners;
    protected transient WeakHashMap<ItsNatComponent,Object> weakMapComponents;

    /** Creates a new instance of ItsNatDocComponentManagerImpl */
    public ItsNatDocComponentManagerImpl(ItsNatDocumentImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;

        ItsNatDocumentTemplateVersionImpl templateVer = itsNatDoc.getItsNatDocumentTemplateVersion();

        this.selectionOnComponentsUsesKeyboard = templateVer.isSelectionOnComponentsUsesKeyboard();
        this.markupDrivenComponents = templateVer.isMarkupDrivenComponents();
        this.autoBuildComponents = templateVer.isAutoBuildComponents();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        {
            Map<String,ItsNatButtonGroupImpl> mapTmp = null;

            if (buttonGroupsByName != null)
            {
                // Tenemos que copiar elemento a elemento porque los "values"
                // son WeakReference (no serializables)
                mapTmp = new HashMap<String,ItsNatButtonGroupImpl>();
                for(Map.Entry<String,WeakReference<ItsNatButtonGroupImpl>> entry : buttonGroupsByName.entrySet())
                {
                    String key = entry.getKey(); 
                    WeakReference<ItsNatButtonGroupImpl> weakRef = entry.getValue();
                    mapTmp.put(key, weakRef.get());
                }
            }
            out.writeObject(mapTmp);
        }
        
        {
            Map<ButtonGroup,ItsNatButtonGroupImpl> mapTmp = null;
            if (buttonGroupsByButtonGroup != null)
            {
                // Tenemos que copiar elemento a elemento porque los "values"
                // son WeakReference (no serializables)
                mapTmp = new HashMap<ButtonGroup,ItsNatButtonGroupImpl>();
                for(Map.Entry<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> entry : buttonGroupsByButtonGroup.entrySet())
                {
                    ButtonGroup key = entry.getKey();
                    WeakReference<ItsNatButtonGroupImpl> weakRef = entry.getValue();
                    mapTmp.put(key, weakRef.get());
                }
            }
            out.writeObject(mapTmp);
        }
        
        {
            Map<ItsNatComponent,Object> mapTmp = null;
            if (weakMapComponents != null)
                mapTmp = new HashMap<ItsNatComponent,Object>(weakMapComponents);
            out.writeObject(mapTmp);
        }
        
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        {
            Map<String,ItsNatButtonGroupImpl> mapTmp = (Map<String,ItsNatButtonGroupImpl>)in.readObject();
            if (mapTmp != null)
            {
                WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByName = getButtonGroupsByNameMap();
                for(Map.Entry<String,ItsNatButtonGroupImpl> entry : mapTmp.entrySet())
                {
                    String key = entry.getKey(); 
                    ItsNatButtonGroupImpl value = entry.getValue();
                    buttonGroupsByName.put(key,new WeakReference<ItsNatButtonGroupImpl>(value));
                }
            }
        }
        
        {
            Map<ButtonGroup,ItsNatButtonGroupImpl> mapTmp = (Map<ButtonGroup,ItsNatButtonGroupImpl>)in.readObject();
            if (mapTmp != null)
            {
                WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByButtonGroup = getButtonGroupsByButtonGroupMap();
                for(Map.Entry<ButtonGroup,ItsNatButtonGroupImpl> entry : mapTmp.entrySet())
                {
                    ButtonGroup key = entry.getKey();
                    ItsNatButtonGroupImpl value = entry.getValue();
                    buttonGroupsByButtonGroup.put(key,new WeakReference<ItsNatButtonGroupImpl>(value));
                }
            }
        }
        
        {
            Map<ItsNatComponent,Object> mapTmp = (Map<ItsNatComponent,Object>)in.readObject();
            if (mapTmp != null)
                getItsNatComponentWeakMap().putAll(mapTmp);
        }
        
        in.defaultReadObject();
    }

    public ItsNatDocument getItsNatDocument()
    {
        return itsNatDoc;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl()
    {
        return itsNatDoc;
    }

    public boolean isSelectionOnComponentsUsesKeyboard()
    {
        return selectionOnComponentsUsesKeyboard;
    }

    public void setSelectionOnComponentsUsesKeyboard(boolean value)
    {
        this.selectionOnComponentsUsesKeyboard = value;
    }

    public boolean isMarkupDrivenComponents()
    {
        return markupDrivenComponents;
    }

    public void setMarkupDrivenComponents(boolean value)
    {
        this.markupDrivenComponents = value;
    }

    public boolean isAutoBuildComponents()
    {
        return autoBuildComponents;
    }

    public void setAutoBuildComponents(boolean value)
    {
        if (this.autoBuildComponents == value)
            return; // No cambia nada

        this.autoBuildComponents = value;

        DocMutationEventListenerImpl mutEventListener = getItsNatDocumentImpl().getDocMutationEventListener();
        AutoBuildCompBeforeAfterMutationRenderListener autoBuildListener;
        if (value)
            autoBuildListener = new AutoBuildCompBeforeAfterMutationRenderListener(this);
        else
            autoBuildListener = null;

        mutEventListener.setAutoBuildCompBeforeAfterMutationRenderListener(autoBuildListener);
    }

    public Map<Node,ItsNatComponent> getComponentMap()
    {
        if (components == null)
            this.components = new HashMap<Node,ItsNatComponent>();
        return components;
    }

    public Map<Node,Object> getExcludedNodesAsComponentsMap()
    {
        if (excludedNodesAsComponents == null)
            this.excludedNodesAsComponents = new HashMap<Node,Object>();
        return excludedNodesAsComponents;
    }

    public WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>> getButtonGroupsByNameMap()
    {
        if (buttonGroupsByName == null)
            this.buttonGroupsByName = new WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>>(); // Ahorramos memoria si no se usan componentes
        return buttonGroupsByName;
    }

    public WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> getButtonGroupsByButtonGroupMap()
    {
        if (buttonGroupsByButtonGroup == null)
            this.buttonGroupsByButtonGroup = new WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>>(); // Ahorramos memoria si no se usan componentes
        return buttonGroupsByButtonGroup;
    }

    public ItsNatButtonGroup getItsNatButtonGroup(String name)
    {
        if ((name == null) || name.equals("")) return null;

        WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByName = getButtonGroupsByNameMap();
        ItsNatButtonGroupImpl itsNatButtonGroup = null;
        WeakReference<ItsNatButtonGroupImpl> weakButtonGrp = buttonGroupsByName.get(name);
        if (weakButtonGrp != null)
            itsNatButtonGroup = weakButtonGrp.get(); // puede ser null
        if (itsNatButtonGroup == null)
            itsNatButtonGroup = addButtonGroup(name);
        return itsNatButtonGroup;
    }

    public ItsNatButtonGroup getItsNatButtonGroup(ButtonGroup group)
    {
        if (group == null) return null;

        WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByButtonGroup = getButtonGroupsByButtonGroupMap();
        ItsNatButtonGroupImpl itsNatButtonGroup = null;
        WeakReference<ItsNatButtonGroupImpl> weakButtonGrp = buttonGroupsByButtonGroup.get(group);
        if (weakButtonGrp != null)
            itsNatButtonGroup = weakButtonGrp.get(); // puede ser null
        if (itsNatButtonGroup == null)
            itsNatButtonGroup = addButtonGroup(group);
        return itsNatButtonGroup;
    }

    public ItsNatButtonGroup createItsNatButtonGroup()
    {
        return addButtonGroup(new ButtonGroup());
    }

    public ItsNatButtonGroupImpl addButtonGroup(String name)
    {
        // Suponemos que el nombre pasado no es usado por otro (ya se ha comprobado).

        return addButtonGroup(new ButtonGroup(),name);
    }

    public ItsNatButtonGroupImpl addButtonGroup(ButtonGroup group)
    {
        // Suponemos que el ButtonGroup pasado no es usado por otro (ya se ha comprobado).

        // El nombre lo generamos para que sea único
        String name = itsNatDoc.getUniqueIdGenerator().generateId("buttonGroup");
        return addButtonGroup(group,name);
    }

    public ItsNatButtonGroupImpl addButtonGroup(ButtonGroup group,String name)
    {
        ItsNatButtonGroupImpl itsNatButtonGroup = new ItsNatButtonGroupImpl(name,group);
        return addButtonGroup(itsNatButtonGroup);
    }

    public ItsNatButtonGroupImpl addButtonGroup(ItsNatButtonGroupImpl itsNatButtonGroup)
    {
        // Suponemos que el ItsNatButtonGroupImpl pasado es nuevo (ya se ha comprobado).

        WeakReference<ItsNatButtonGroupImpl> weakButtonGrp = new WeakReference<ItsNatButtonGroupImpl>(itsNatButtonGroup);

        WeakHashMap<String,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByName = getButtonGroupsByNameMap();
        buttonGroupsByName.put(itsNatButtonGroup.getName(),weakButtonGrp); // El objeto String name está sujeto por el propio ItsNatButtonGroupImpl, si este se pierde se pierde su Id y la entrada en el mapa lo cual es deseable

        WeakHashMap<ButtonGroup,WeakReference<ItsNatButtonGroupImpl>> buttonGroupsByButtonGroup = getButtonGroupsByButtonGroupMap();
        buttonGroupsByButtonGroup.put(itsNatButtonGroup.getButtonGroup(),weakButtonGrp); // El ButtonGroup Swing está sujeto por el propio ItsNatButtonGroupImpl, si este se pierde se pierde su entrada en el mapa lo cual es deseable

        return itsNatButtonGroup;
    }

    public void removeItsNatComponent(ItsNatComponent comp,boolean dispose)
    {
        Map<Node,ItsNatComponent> components = getComponentMap();
        components.remove(comp.getNode());
        if (dispose) comp.dispose();
    }

    public ItsNatComponent removeItsNatComponent(Node node)
    {
        return removeItsNatComponent(node,false);
    }

    public ItsNatComponent removeItsNatComponent(Node node,boolean dispose)
    {
        ItsNatComponent comp = findItsNatComponent(node);
        if (comp == null) return null;
        removeItsNatComponent(comp,dispose);
        return comp;
    }

    public ItsNatComponent removeItsNatComponent(String id)
    {
        Document doc = getItsNatDocumentImpl().getDocument();
        Element node = doc.getElementById(id);
        return removeItsNatComponent(node);
    }

    public void removeExcludedNodeAsItsNatComponent(Node node)
    {
        Map<Node,Object> nodes = getExcludedNodesAsComponentsMap();
        nodes.remove(node);
    }

    public ItsNatComponent findItsNatComponentById(String id)
    {
        Document doc = getItsNatDocumentImpl().getDocument();
        Element node = doc.getElementById(id);
        return findItsNatComponent(node);
    }

    public ItsNatComponent findItsNatComponent(Node node)
    {
        if (node == null)
            return null;

        if (node.getNodeType() != Node.ELEMENT_NODE)
            return null; // Sólo Element son componentes, así aceleramos la búsqueda

        Map<Node,ItsNatComponent> components = getComponentMap();
        return components.get(node);
    }

    public boolean isExcludedNodeAsItsNatComponent(Node node)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return true;  // Sólo pueden ser componentes los Element, otro tipo de nodos son excluidos por defecto

        Map<Node,Object> nodes = getExcludedNodesAsComponentsMap();
        return nodes.containsKey(node);
    }

    public ItsNatComponent addItsNatComponent(ItsNatComponent comp)
    {
        if (comp == null) return null;

        Map<Node,ItsNatComponent> components = getComponentMap();
        return components.put(comp.getNode(),comp);
    }

    public void addExcludedNodeAsItsNatComponent(Node node)
    {
        if (node.getNodeType() != Node.ELEMENT_NODE)
            throw new ItsNatDOMException("Only Element nodes can be associated to components",node);

        Map<Node,Object> nodes = getExcludedNodesAsComponentsMap();
        nodes.put(node,null);
    }

    public ItsNatComponent createItsNatComponent(Node node)
    {
        return createItsNatComponent(node,null,null);
    }

    public ItsNatComponent createItsNatComponent(Node node,String componentType,NameValue[] artifacts)
    {
        return createItsNatComponent(node,componentType,artifacts,false);
    }

    public ItsNatComponent createItsNatComponentById(String id)
    {
        return createItsNatComponentById(id,null,null);
    }

    public ItsNatComponent createItsNatComponentById(String id,String compType,NameValue[] artifacts)
    {
        Document doc = getItsNatDocumentImpl().getDocument();
        Element node = doc.getElementById(id);
        return createItsNatComponent(node,compType,artifacts);
    }

    public ItsNatComponent createItsNatComponent(Node node,String componentType,NameValue[] artifacts,boolean autoBuildMode)
    {
        if (node == null)
            return null;

        if (node.getNodeType() != Node.ELEMENT_NODE)
            return null; // Sólo Element pueden ser componentes a día de hoy
        Element element = (Element)node;

        if (componentType == null) // Esto incluye el caso de creación explícita del usuario pero no especifica el componentType, pero sin embargo está en el HTML
            componentType = getCompTypeAttribute(element);   // Puede ser null

        ItsNatComponent comp = null;
        boolean doFilters = hasBeforeAfterCreateItsNatComponentListener();
        if (doFilters) comp = processBeforeCreateItsNatComponentListener(node,componentType,artifacts);

        if (comp == null)
        {
            // A día de hoy sólo nodos Element pueden ser componentes
            // Si compType está definido, éste tiene prioridad sobre el tipo de componente
            // por defecto asociado al tag name.
            // compType puede ser null => el componente se deduce del tagName
            FactoryItsNatComponentImpl factory = getFactoryItsNatComponent(element,componentType);
            if (factory != null)
                comp = factory.createItsNatComponent(element,componentType,artifacts,autoBuildMode,false,this);
        }

        if (comp == null) return null;

        if (doFilters) comp = processAfterCreateItsNatComponentListener(comp);

        registerItsNatComponent(comp);

        return comp; // Puede ser null
    }

    public void registerItsNatComponent(ItsNatComponent comp)
    {
        getItsNatComponentWeakMap().put(comp,null);
    }

    public boolean hasItsNatComponents()
    {
        if (weakMapComponents == null)
            return false;
        return weakMapComponents.size() > 0;
    }

    public WeakHashMap<ItsNatComponent,Object> getItsNatComponentWeakMap()
    {
        if (weakMapComponents == null)
            this.weakMapComponents = new WeakHashMap<ItsNatComponent,Object>();
        return weakMapComponents;
    }

    public boolean hasBeforeAfterCreateItsNatComponentListener()
    {
        ItsNatDocumentTemplateImpl docTemplate = itsNatDoc.getItsNatDocumentTemplateImpl();
        ItsNatServletImpl servlet = docTemplate.getItsNatServletImpl();
        boolean res;

        res = servlet.hasCreateItsNatComponentList();
        if (res) return true;

        res = docTemplate.hasCreateItsNatComponentList();
        if (res) return true;

        res = hasCreateItsNatComponentList();
        if (res) return true;

        return false;
    }

    public ItsNatComponent processBeforeCreateItsNatComponentListener(Node node,String componentType,NameValue[] artifacts)
    {
        ItsNatComponent comp = null;
        Iterator<CreateItsNatComponentListener> createCompIterator = null;

        ItsNatDocumentTemplateImpl docTemplate = itsNatDoc.getItsNatDocumentTemplateImpl();
        ItsNatServletImpl servlet = docTemplate.getItsNatServletImpl();

        createCompIterator = servlet.getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processBeforeCreateItsNatComponentListener(createCompIterator,node,componentType,artifacts);
            if (comp != null) return comp;
        }

        createCompIterator = docTemplate.getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processBeforeCreateItsNatComponentListener(createCompIterator,node,componentType,artifacts);
            if (comp != null) return comp;
        }

        createCompIterator = getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processBeforeCreateItsNatComponentListener(createCompIterator,node,componentType,artifacts);
            if (comp != null) return comp;
        }

        return null;
    }

    public ItsNatComponent processAfterCreateItsNatComponentListener(ItsNatComponent comp)
    {
        // Permitimos así que el usuario pueda crear sus propios componentes
        // que serán instanciados cuando sea llamado este método
        // incluso podría substituir a los que hay por defecto segun el nodo
        // Ver más arriba la llamada a beforeRender()
        ItsNatDocumentTemplateImpl docTemplate = itsNatDoc.getItsNatDocumentTemplateImpl();
        ItsNatServletImpl servlet = docTemplate.getItsNatServletImpl();

        Iterator<CreateItsNatComponentListener> createCompIterator = null;
        createCompIterator = servlet.getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processAfterCreateItsNatComponentListener(createCompIterator,comp);
            if (comp == null) return null; // Componente rechazado
        }

        createCompIterator = docTemplate.getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processAfterCreateItsNatComponentListener(createCompIterator,comp);
            if (comp == null) return null; // Componente rechazado
        }

        createCompIterator = getCreateItsNatComponentListenerIterator();
        if (createCompIterator != null)
        {
            comp = processAfterCreateItsNatComponentListener(createCompIterator,comp);
            if (comp == null) return null; // Componente rechazado
        }

        return comp;
    }

    public ItsNatComponent processBeforeCreateItsNatComponentListener(Iterator<CreateItsNatComponentListener> it,Node node,String componentType,NameValue[] artifacts)
    {
        ItsNatComponent comp = null;

        // Permitimos así que el usuario pueda crear sus propios componentes
        // que serán instanciados cuando sea llamado este método
        // incluso podría substituir a los que hay por defecto segun el nodo
        // No debería añadirse nuevos listeners cuando ya haya documentos cargados
        // El programador debería tener en cuenta que se puede llamar a beforeRender/afterRender en multihilo
        // aunque con un sólo hilo por documento.

        // Aunque se usa una colección del ItsNatDocumentTemplate y servlet no hay problema de hilos
        // mientras no se modifique concurrentemente la lista, esto no debería
        // ocurrir, el usuario debe registrar sus CreateItsNatComponentListener
        // en tiempo de creación del template (init() del Servlet).
        while(it.hasNext())
        {
            CreateItsNatComponentListener listener = it.next();
            comp = listener.before(node,componentType,artifacts,this);
            if (comp != null)
                break;
        }

        return comp;
    }

    public ItsNatComponent processAfterCreateItsNatComponentListener(Iterator<CreateItsNatComponentListener> it,ItsNatComponent comp)
    {
        // Permitimos así que el usuario pueda crear sus propios componentes
        // que serán instanciados cuando sea llamado este método
        // incluso podría substituir a los que hay por defecto segun el nodo
        // Ver más arriba la llamada a beforeRender()
        while(it.hasNext())
        {
            CreateItsNatComponentListener listener = it.next();
            comp = listener.after(comp);
            if (comp == null)
                return null; // Componente rechazado
        }
        return comp;
    }

    public ItsNatComponent addItsNatComponent(Node node)
    {
        return addItsNatComponent(node,null,null);
    }

    public ItsNatComponent addItsNatComponent(Node node,String componentType,NameValue[] artifacts)
    {
        return addItsNatComponent(node,componentType,artifacts,false);
    }

    public ItsNatComponent addItsNatComponentById(String id)
    {
        return addItsNatComponentById(id,null,null);
    }

    public ItsNatComponent addItsNatComponentById(String id,String compType,NameValue[] artifacts)
    {
        Document doc = getItsNatDocumentImpl().getDocument();
        Element node = doc.getElementById(id);
        return addItsNatComponent(node,compType,artifacts);
    }

    public ItsNatComponent addItsNatComponent(Node node,String componentType,NameValue[] artifacts,boolean autoBuildMode)
    {
        if (node == null)
            return null;

        if (isExcludedNodeAsItsNatComponent(node))
            return null;

        if (autoBuildMode)
        {
            if (findItsNatComponent(node) != null) // para evitar añadir dos veces
                return null;
        }

        ItsNatComponent component = createItsNatComponent(node,componentType,artifacts,autoBuildMode); // puede ser null
        if (component != null)
        {
            ItsNatComponent oldComp = addItsNatComponent(component);  // Si es null no hace nada
            if (oldComp != null)
                throw new ItsNatDOMException("A component is already associated to this node",node);
        }
        return component; // puede ser null
    }

    public ItsNatComponent[] buildItsNatComponents()
    {
        Document doc = getItsNatDocument().getDocument();
        return buildItsNatComponents(doc);
    }

    public ItsNatComponent[] buildItsNatComponents(Node node)
    {
        LinkedList<ItsNatComponent> listComp = new LinkedList<ItsNatComponent>();

        buildItsNatComponents(node,listComp);

        return listComp.toArray(new ItsNatComponent[listComp.size()]);
    }

    public void buildItsNatComponentsInternal()
    {
        Document doc = getItsNatDocument().getDocument();
        buildItsNatComponents(doc,null);
    }

    public LinkedList<ItsNatComponent> buildItsNatComponents(Node node,LinkedList<ItsNatComponent> listComp)
    {
        // Primero procesamos el propio nodo pues si es un componente
        // puede crear y eliminar nodos hijo, luego procesamos los hijos tal y como ha quedado

        ItsNatComponent comp = addItsNatComponent(node,null,null,true); // Si no puede ser un componente no hace nada
        if (comp != null)
        {
            if (listComp == null)
                listComp = new LinkedList<ItsNatComponent>(); // Se crea cuando se necesita
            listComp.add(comp);
        }

        Node child = node.getFirstChild();
        while(child != null)
        {
            listComp = buildItsNatComponents(child,listComp);
            child = child.getNextSibling();
        }

        return listComp;
    }

    public ItsNatComponent[] removeItsNatComponents(boolean dispose)
    {
        Map<Node,ItsNatComponent> components = getComponentMap();
        if (!components.isEmpty())
        {
            ItsNatComponent[] listRes = new ItsNatComponent[components.size()];
            int i = 0;
            for (Map.Entry<Node,ItsNatComponent> entry : components.entrySet())
            {
                ItsNatComponent comp = entry.getValue();
                if (dispose) comp.dispose();
                listRes[i] = comp;
                i++;
            }
            components.clear();
            return listRes;
        }
        else
            return null;
    }

    public ItsNatComponent[] removeItsNatComponents(Node node,boolean dispose)
    {
        LinkedList<ItsNatComponent> listComp = new LinkedList<ItsNatComponent>();
        removeItsNatComponents(node,dispose,listComp);
        return listComp.toArray(new ItsNatComponent[listComp.size()]);
    }

    public void removeItsNatComponents(Node node,boolean dispose,LinkedList<ItsNatComponent> listComp)
    {
        // Primero los hijos por si acaso
        Node child = node.getFirstChild();
        while(child != null)
        {
            removeItsNatComponents(child,dispose,listComp);
            child = child.getNextSibling();
        }

        ItsNatComponent comp = removeItsNatComponent(node,dispose);
        if ((comp != null)&&(listComp != null))
            listComp.add(comp);
    }

    public void startLoading()
    {
        if (isAutoBuildComponents())
            initAutoBuildComponents();
    }

    public void initAutoBuildComponents()
    {
        buildItsNatComponentsInternal();

        // Registramos un listener tal que las mutaciones en el documento
        // desde ahora se manifiesten en añadido o quitado automático de componentes
        // Registramos después de hacer la llamada buildItsNatComponents();
        // porque se hace de arriba abajo tal que aunque el componente añada y quite
        // nodos que puedan tener a su vez componentes posteriormente se procesan
        // los hijos del nodo-componente tal y como quedan tras crear el componente
        // Si el componente creara/borrara nodos con posibles componentes en otro lugar diferente a bajo sí mismo
        // no se detectaría pero no hay ningún componente que haga esto.
        // En dicho caso se registraría antes este listener aunque ralentiza más pues hay que evitar
        // añadir componentes cuando ya se han añadido

        DocMutationEventListenerImpl mutEventListener = getItsNatDocumentImpl().getDocMutationEventListener();
        mutEventListener.setAutoBuildCompBeforeAfterMutationRenderListener(new AutoBuildCompBeforeAfterMutationRenderListener(this));
    }

    public static String getCompTypeAttribute(Element element)
    {
        String value = element.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"compType");
        if (value.equals(""))
            return null;
        return value;
    }

    public static boolean isComponentAttribute(Element element)
    {
        String value = element.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"isComponent");
        return value.equals("true");
    }

    public static boolean explicitIsNotComponentAttribute(Element element)
    {
        // En este caso es true sólo cuando isComponent existe y está puesto como "false" explícitamente
        String value = element.getAttributeNS(NamespaceUtil.ITSNAT_NAMESPACE,"isComponent");
        return value.equals("false");
    }

    public static boolean declaredAsComponent(Element element)
    {
        String type = getCompTypeAttribute(element);
        return (type != null);
    }

    public ItsNatLabelRenderer createDefaultItsNatLabelRenderer()
    {
        return new ItsNatLabelRendererDefaultImpl(this);
    }

    public ItsNatListCellRenderer createDefaultItsNatListCellRenderer()
    {
        return new ItsNatListCellRendererDefaultImpl(this);
    }

    public ItsNatTableCellRenderer createDefaultItsNatTableCellRenderer()
    {
        return new ItsNatTableCellRendererDefaultImpl(this);
    }

    public ItsNatTableHeaderCellRenderer createDefaultItsNatTableHeaderCellRenderer()
    {
        return new ItsNatTableHeaderCellRendererDefaultImpl(this);
    }

    public ItsNatTreeCellRenderer createDefaultItsNatTreeCellRenderer()
    {
        return new ItsNatTreeCellRendererDefaultImpl(this);
    }

    public ItsNatFreeInclude createItsNatFreeInclude(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeIncludeImpl.SINGLETON.createItsNatFreeInclude(element,artifacts,true,this);
    }

    public ItsNatFreeLabel createItsNatFreeLabel(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeLabelImpl.SINGLETON.createItsNatFreeLabel(element,artifacts,true,this);
    }

    public ItsNatFreeButtonNormal createItsNatFreeButtonNormal(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeButtonNormalDefaultImpl.SINGLETON.createItsNatFreeButtonNormalDefault(element,artifacts,true,this);
    }

    public ItsNatFreeButtonNormalLabel createItsNatFreeButtonNormalLabel(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeButtonNormalLabelImpl.SINGLETON.createItsNatFreeButtonNormalLabel(element,artifacts,true,this);
    }

    public ItsNatFreeCheckBox createItsNatFreeCheckBox(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeCheckBoxDefaultImpl.SINGLETON.createItsNatFreeCheckBox(element,artifacts,true,this);
    }

    public ItsNatFreeCheckBoxLabel createItsNatFreeCheckBoxLabel(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeCheckBoxLabelImpl.SINGLETON.createItsNatFreeCheckBoxLabel(element,artifacts,true,this);
    }

    public ItsNatFreeRadioButton createItsNatFreeRadioButton(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeRadioButtonDefaultImpl.SINGLETON.createItsNatFreeRadioButton(element,artifacts,true,this);
    }

    public ItsNatFreeRadioButtonLabel createItsNatFreeRadioButtonLabel(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeRadioButtonLabelImpl.SINGLETON.createItsNatFreeRadioButtonLabel(element,artifacts,true,this);
    }

    public ItsNatFreeComboBox createItsNatFreeComboBox(Element element,ItsNatListStructure structure,NameValue[] artifacts)
    {
        return FactoryItsNatFreeComboBoxImpl.SINGLETON.createItsNatFreeComboBox(element,structure,artifacts,true,this);
    }

    public ItsNatFreeListMultSel createItsNatFreeListMultSel(Element element,ItsNatListStructure structure,NameValue[] artifacts)
    {
        return FactoryItsNatFreeListMultSelImpl.SINGLETON.createItsNatFreeListMultSel(element,structure,artifacts,true,this);
    }

    public ItsNatFreeTable createItsNatFreeTable(Element element,ItsNatTableStructure structure,NameValue[] artifacts)
    {
        return FactoryItsNatFreeTableImpl.SINGLETON.createItsNatFreeTable(element,structure,artifacts,true,this);
    }

    public ItsNatFreeTree createItsNatFreeTree(Element element,NameValue[] artifacts)
    {
        return FactoryItsNatFreeTreeImpl.SINGLETON.createItsNatFreeTree(element,artifacts,true,this);
    }

    public ItsNatFreeTree createItsNatFreeTree(Element element,boolean treeTable,boolean rootless,ItsNatTreeStructure structure,NameValue[] artifacts)
    {
        return FactoryItsNatFreeTreeImpl.SINGLETON.createItsNatFreeTree(element,treeTable,rootless,structure,artifacts,true,this);
    }

    public ItsNatListStructure createDefaultItsNatListStructure()
    {
        return ItsNatListStructureDefaultImpl.newItsNatListStructureDefault();
    }

    public ItsNatTableStructure createDefaultItsNatTableStructure()
    {
        return ItsNatTableStructureDefaultImpl.newItsNatTableStructureDefault();
    }

    public ItsNatTreeStructure createDefaultItsNatTreeStructure()
    {
        return ItsNatTreeStructureDefaultImpl.newItsNatTreeStructureDefault();
    }

    public boolean hasCreateItsNatComponentList()
    {
        if (createCompListeners == null) return false;
        return !createCompListeners.isEmpty();
    }

    public LinkedList<CreateItsNatComponentListener> getCreateItsNatComponentList()
    {
        if (createCompListeners == null)
            this.createCompListeners = new LinkedList<CreateItsNatComponentListener>(); // Sólo se crea si se necesita
        return createCompListeners;
    }

    public Iterator<CreateItsNatComponentListener> getCreateItsNatComponentListenerIterator()
    {
        if (!hasCreateItsNatComponentList()) return null;
        return createCompListeners.iterator();
    }

    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.add(listener);
    }

    public void removeCreateItsNatComponentListener(CreateItsNatComponentListener listener)
    {
        LinkedList<CreateItsNatComponentListener> list = getCreateItsNatComponentList();
        list.remove(listener);
    }

}

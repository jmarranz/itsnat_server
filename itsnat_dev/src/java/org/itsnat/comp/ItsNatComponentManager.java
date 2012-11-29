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

package org.itsnat.comp;

import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.button.ItsNatButtonGroup;
import org.itsnat.comp.table.ItsNatTableHeaderCellRenderer;
import org.itsnat.comp.table.ItsNatTableCellRenderer;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.tree.ItsNatTreeStructure;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.comp.tree.ItsNatTreeCellRenderer;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.comp.list.ItsNatListCellRenderer;
import org.itsnat.comp.label.ItsNatLabelRenderer;
import org.itsnat.comp.label.ItsNatLabelEditor;
import javax.swing.ButtonGroup;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormal;
import org.itsnat.comp.button.normal.ItsNatFreeButtonNormalLabel;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBox;
import org.itsnat.comp.button.toggle.ItsNatFreeCheckBoxLabel;
import org.itsnat.comp.list.ItsNatFreeComboBox;
import org.itsnat.comp.inc.ItsNatFreeInclude;
import org.itsnat.comp.label.ItsNatFreeLabel;
import org.itsnat.comp.list.ItsNatFreeListMultSel;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButton;
import org.itsnat.comp.button.toggle.ItsNatFreeRadioButtonLabel;
import org.itsnat.comp.table.ItsNatFreeTable;
import org.itsnat.comp.tree.ItsNatFreeTree;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.NameValue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility interface to manage (create, register etc) ItsNat components of
 * the associated {@link ItsNatDocument}.
 *
 * @see ItsNatDocument#getItsNatComponentManager()
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatComponentManager
{
    /**
     * Returns the associated ItsNat document (parent of this object).
     *
     * @return the associated ItsNat document.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Informs whether components are built automatically using the necessary
     * markup declarations.
     *
     * <p>If this feature is enabled any new DOM sub tree added to the document
     * is automatically traversed, any component declared in markup
     * is automatically built and registered into the component manager
     * associated to a DOM element.
     * </p>
     *
     * <p>When a node is removed from the document tree the associated component if any
     * is removed and disposed automatically.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#isAutoBuildComponents()}</p>
     *
     * @return true if automatic component build is enabled.
     * @see #setAutoBuildComponents(boolean)
     */
    public boolean isAutoBuildComponents();

    /**
     * Sets whether components are built automatically using the necessary
     * markup declarations.
     *
     * @param value true to enable automatic component build.
     * @see #isAutoBuildComponents()
     */
    public void setAutoBuildComponents(boolean value);

    /**
     * Informs whether the keyboard is necessary for selection on components.
     *
     * <p>The default value is defined by {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#isSelectionOnComponentsUsesKeyboard()}</p>
     *
     * <p>This feature only affects to non-XML documents with events enabled
     * and free components with multiple selection support (lists, tables and trees).</p>
     *
     * @return true if selection uses keyboard. True by default.
     * @see #setSelectionOnComponentsUsesKeyboard(boolean)
     * @see ItsNatFreeListMultSel#isSelectionUsesKeyboard()
     * @see org.itsnat.comp.table.ItsNatTable#isSelectionUsesKeyboard()
     * @see org.itsnat.comp.tree.ItsNatTree#isSelectionUsesKeyboard()
     */
    public boolean isSelectionOnComponentsUsesKeyboard();

    /**
     * Informs whether the keyboard is necessary for selection on components
     *
     * <p>This method does not change the current state of already created components
     * but may be the default value of new components with this feature.
     * </p>
     *
     * @param value true to specify the keyboard is necessary for selection.
     * @see #isSelectionOnComponentsUsesKeyboard()
     */
    public void setSelectionOnComponentsUsesKeyboard(boolean value);


    /**
     * Informs whether markup driven mode is used in new components.
     *
     * <p>The default value is defined by {@link org.itsnat.core.tmpl.ItsNatDocumentTemplate#isMarkupDrivenComponents()}</p>
     *
     * <p>This feature only affects to some HTML form based components.</p>
     *
     * @return true if components are markup driven.
     * @see #setMarkupDrivenComponents(boolean)
     * @see org.itsnat.comp.button.toggle.ItsNatHTMLInputButtonToggle#isMarkupDriven()
     * @see org.itsnat.comp.text.ItsNatHTMLInputTextBased#isMarkupDriven()
     * @see org.itsnat.comp.text.ItsNatHTMLTextArea#isMarkupDriven()
     */
    public boolean isMarkupDrivenComponents();

    /**
     * Sets whether markup driven mode is used in components.
     *
     * <p>This method does not change the current state of already created components
     * but may be the default value of new components with this feature.
     * </p>
     *
     * @param value true to enable markup driven.
     * @see #isMarkupDrivenComponents()
     */
    public void setMarkupDrivenComponents(boolean value);

    /**
     * Returns the ItsNat button group with the specified name.
     *
     * @param name the button group name to search for.
     * @return the ItsNat button group, if not found a new one is registered and returned.
     * @see #getItsNatButtonGroup(javax.swing.ButtonGroup)
     */
    public ItsNatButtonGroup getItsNatButtonGroup(String name);

    /**
     * Returns the ItsNat button group with the specified Swing button group.
     *
     * @param group the Swing button group to search for.
     * @return the ItsNat button group, if not found a new one is registered and returned.
     * @see #getItsNatButtonGroup(String)
     */
    public ItsNatButtonGroup getItsNatButtonGroup(ButtonGroup group);

    /**
     * Creates a new ItsNat button group with a generated name and a new <code>javax.swing.ButtonGroup</code>.
     *
     * <p>Generated name is unique per document.</p>
     *
     * @return a new ItsNat button group.
     */
    public ItsNatButtonGroup createItsNatButtonGroup();

    /**
     * Adds a new user defined component factory. This listener is called when the framework needs
     * to create a component instance.
     *
     * @param listener the listener factory to register.
     * @see #removeCreateItsNatComponentListener(CreateItsNatComponentListener)
     * @see org.itsnat.core.tmpl.ItsNatDocumentTemplate#addCreateItsNatComponentListener(CreateItsNatComponentListener)
     * @see org.itsnat.core.ItsNatServlet#addCreateItsNatComponentListener(CreateItsNatComponentListener)
     */
    public void addCreateItsNatComponentListener(CreateItsNatComponentListener listener);

    /**
     * Removes the specified user defined component factory.
     *
     * @param listener the listener factory to remove.
     * @see #addCreateItsNatComponentListener(CreateItsNatComponentListener)
     */
    public void removeCreateItsNatComponentListener(CreateItsNatComponentListener listener);

    /**
     * Creates a new component object associated to the specified node.
     *
     * <p>All registered {@link CreateItsNatComponentListener} listeners are used
     * to create the component.</p>
     *
     * @param node the node to associate the new component.
     * @param compType the component type, if null the type is obtained from the node.
     * @param artifacts declared artifacts, may be null.
     * @return the new component, is null if no component can be created with the submitted parameters.
     * @see #addCreateItsNatComponentListener(CreateItsNatComponentListener)
     * @see org.itsnat.core.tmpl.ItsNatDocumentTemplate#addCreateItsNatComponentListener(CreateItsNatComponentListener)
     * @see org.itsnat.core.ItsNatServlet#addCreateItsNatComponentListener(CreateItsNatComponentListener)
     */
    public ItsNatComponent createItsNatComponent(Node node,String compType,NameValue[] artifacts);

    /**
     * Creates a new component object associated to the specified node.
     *
     * <p>Current implementation calls:</p>
     * <blockquote><pre>
     *  return createItsNatComponent(node,null,null);
     * </pre></blockquote>
     *
     *
     * @param node the node to associate the new component.
     * @return the new component, is null if no component can be created.
     * @see #createItsNatComponent(org.w3c.dom.Node,String,NameValue[])
     */
    public ItsNatComponent createItsNatComponent(Node node);

    /**
     * Creates a new component object associated to the node with the specified id attribute.
     *
     * <p>Current implementation calls:</p>
     * <blockquote><pre>
     *  return createItsNatComponentById(id,null,null);
     * </pre></blockquote>
     *
     *
     * @param id the attribute id of the node to associate the new component.
     * @return the new component, is null if no component can be created.
     * @see #createItsNatComponentById(String,String,NameValue[])
     */
    public ItsNatComponent createItsNatComponentById(String id);

    /**
     * Creates a new component object associated to the node with the specified id attribute.
     *
     * <p>Current implementation:</p>
     * <blockquote><pre>
     *   Document doc = getItsNatDocument().getDocument();
     *   Element node = doc.getElementById(id);
     *   return createItsNatComponent(node,compType,artifacts);
     * </pre></blockquote>
     *
     *
     * @param id the attribute id of the node to associate the new component.
     * @param compType the component type, if null the type is obtained from the node.
     * @param artifacts declared artifacts, may be null.
     * @return the new component, is null if no component can be created.
     * @see #createItsNatComponent(org.w3c.dom.Node,String,NameValue[])
     */
    public ItsNatComponent createItsNatComponentById(String id,String compType,NameValue[] artifacts);

    /**
     * Creates and adds to the manager registry a new component object associated to the specified node.
     *
     * <p>Component creation is delegated to the method {@link #createItsNatComponent(org.w3c.dom.Node,String,NameValue[])},
     * if no component can be created nothing is registered.</p>
     *
     * <p>If the node specified was registered as excluded calling
     * {@link #addExcludedNodeAsItsNatComponent(org.w3c.dom.Node)} no component is created and registered.</p>
     *
     *
     * @param node the node to associate the new component.
     * @param compType the component type, if null the type is obtained from the node.
     * @param artifacts declared artifacts, may be null.
     * @return the new component, is null if no component can be created with the submitted parameters.
     * @see #addItsNatComponent(ItsNatComponent)
     */
    public ItsNatComponent addItsNatComponent(Node node,String compType,NameValue[] artifacts);

    /**
     * Creates and adds to the manager registry a new component object associated to the specified node.
     *
     * <p>Current implementation calls:</p>
     * <blockquote><pre>
     * return addItsNatComponent(node,null,null);
     * </pre></blockquote>
     *
     * @param node the node to associate the new component.
     * @return the new component, is null if no component can be created with the submitted parameters.
     * @see #addItsNatComponent(org.w3c.dom.Node,String,NameValue[])
     */
    public ItsNatComponent addItsNatComponent(Node node);

    /**
     * Creates and adds to the manager registry a new component object associated to the node with the specified id attribute.
     *
     * <p>Current implementation calls:</p>
     * <blockquote><pre>
     * return addItsNatComponentById(id,null,null);
     * </pre></blockquote>
     *
     *
     * @param id the attribute id of the node to associate the new component.
     * @return the new component, is null if no component can be created with the submitted parameters.
     * @see #addItsNatComponentById(String,String,NameValue[])
     */
    public ItsNatComponent addItsNatComponentById(String id);

    /**
     * Creates and adds to the manager registry a new component object associated to the node with the specified id attribute.
     *
     * <p>Current implementation:</p>
     * <blockquote><pre>
     *   Document doc = getItsNatDocument().getDocument();
     *   Element node = doc.getElementById(id);
     *   return addItsNatComponent(node,compType,artifacts);
     * </pre></blockquote>
     *
     *
     * @param id the attribute id of the node to associate the new component.
     * @param compType the component type, if null the type is obtained from the node.
     * @param artifacts declared artifacts, may be null.
     * @return the new component, is null if no component can be created with the submitted parameters.
     * @see #addItsNatComponent(org.w3c.dom.Node,String,NameValue[])
     */
    public ItsNatComponent addItsNatComponentById(String id,String compType,NameValue[] artifacts);

    /**
     * Adds to the manager registry the specified component object.
     *
     * <p>This method ignores the "excluded registry".</p>
     *
     * @param comp the component to register.
     * @return the previously registered component associated to the node of the new component or null
     * @see #addItsNatComponent(org.w3c.dom.Node,String,NameValue[])
     * @see #addExcludedNodeAsItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent addItsNatComponent(ItsNatComponent comp);

    /**
     * Returns the registered component associated to the node which id attribute is specified.
     *
     * <p>Current implementation:</p>
     * <blockquote><pre>
     *   Document doc = getItsNatDocument().getDocument();
     *   Element node = doc.getElementById(id);
     *   return findItsNatComponent(node);
     * </pre></blockquote>
     *
     * @param id the attribute id of the node to search.
     * @return the component associated to the specified node or null if not found.
     * @see #findItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent findItsNatComponentById(String id);

    /**
     * Returns the registered component associated to the specified node.
     *
     * @param node the node the search the associated component.
     * @return the component associated to the specified node or null if not found.
     * @see #addItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent findItsNatComponent(Node node);

    /**
     * Unregisters the component associated to the specified node.
     *
     * @param node the node the search the associated component.
     * @return the component associated to the specified node or null if not found.
     * @see #findItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent removeItsNatComponent(Node node);

    /**
     * Unregisters the component associated to the node specified by the id attribute.
     *
     * <p>Current implementation:</p>
     * <blockquote><pre>
     *   Document doc = getItsNatDocument().getDocument();
     *   Element node = doc.getElementById(id);
     *   return removeItsNatComponent(node);
     * </pre></blockquote>
     *
     * @param id the attribute id of the node to search.
     * @return the component associated to the specified node or null if not found.
     * @see #removeItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent removeItsNatComponent(String id);

    /**
     * Registers the specified node in the excluded registry.
     *
     * <p>No component is created and registered with {@link #addItsNatComponent(org.w3c.dom.Node,String,NameValue[])}
     * if the specified node is in the excluded registry.</p>
     *
     *
     * @param node the node to exclude.
     */
    public void addExcludedNodeAsItsNatComponent(Node node);

    /**
     * Removes the specified node from the excluded registry.
     *
     * @param node the node to exclude.
     * @see #addExcludedNodeAsItsNatComponent(org.w3c.dom.Node)
     */
    public void removeExcludedNodeAsItsNatComponent(Node node);

    /**
     * Informs whether the specified node is in the excluded registry.
     *
     * @param node the node to search for.
     * @see #addExcludedNodeAsItsNatComponent(org.w3c.dom.Node)
     */
    public boolean isExcludedNodeAsItsNatComponent(Node node);

    /**
     * Navigates recursively the subtree below the specified node (included) and for every node tries
     * to create and register the built-in component associated to the node.
     *
     * @param node the node parent of the subtree.
     * @return an array with the components created and registered.
     * @see #buildItsNatComponents()
     * @see #removeItsNatComponents(org.w3c.dom.Node,boolean)
     * @see #addItsNatComponent(org.w3c.dom.Node)
     */
    public ItsNatComponent[] buildItsNatComponents(Node node);

    /**
     * Navigates recursively the document tree and for every node tries
     * to create and register the built-in component associated to the node.
     *
     * <p>Current implementation is:</p>
     * <blockquote><pre>
     *  Document doc = getItsNatDocument().getDocument();
     *  return buildItsNatComponents(doc);
     * </pre></blockquote>
     *
     * @return an array with the components created and registered.
     * @see #buildItsNatComponents(org.w3c.dom.Node)
     */
    public ItsNatComponent[] buildItsNatComponents();

    /**
     * Navigates recursively the subtree below the specified node (included) and for every node removes
     * the associated component if any from the registry and optionally disposes it.
     *
     * @param node the node parent of the subtree.
     * @param dispose if every component found is disposed.
     * @return an array with the components removed.
     * @see #removeItsNatComponents(boolean)
     * @see #removeItsNatComponent(org.w3c.dom.Node)
     * @see #buildItsNatComponents(org.w3c.dom.Node)
     */
    public ItsNatComponent[] removeItsNatComponents(Node node,boolean dispose);

    /**
     * Navigates recursively the document and for every node removes
     * the associated component if any from the registry and optionally disposes it.
     *
     * @param dispose if every component found is disposed.
     * @return an array with the components removed.
     * @see #removeItsNatComponents(org.w3c.dom.Node,boolean)
     */
    public ItsNatComponent[] removeItsNatComponents(boolean dispose);

    /**
     * Creates a default label editor instance using the specified component
     * as the editor.
     *
     * <p>Current implementation supports {@link org.itsnat.comp.text.ItsNatHTMLInputText},
     * {@link org.itsnat.comp.list.ItsNatHTMLSelectComboBox}, {@link org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox}
     * and {@link org.itsnat.comp.text.ItsNatHTMLTextArea} as editors.</p>
     *
     * @param compEditor the component used as editor. If null {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) is used.
     * @return a default label editor instance.
     */
    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor);

    /**
     * Creates a default list editor instance using the specified component
     * as the editor.
     *
     * <p>Current implementation supports {@link org.itsnat.comp.text.ItsNatHTMLInputText},
     * {@link org.itsnat.comp.list.ItsNatHTMLSelectComboBox}, {@link org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox}
     * and {@link org.itsnat.comp.text.ItsNatHTMLTextArea} as editors.</p>
     *
     * @param compEditor the component used as editor. If null {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) is used.
     * @return a default list item editor instance.
     */
    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor);

    /**
     * Creates a default table editor instance using the specified component
     * as the editor.
     *
     * <p>Current implementation supports {@link org.itsnat.comp.text.ItsNatHTMLInputText},
     * {@link org.itsnat.comp.list.ItsNatHTMLSelectComboBox}, {@link org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox}
     * and {@link org.itsnat.comp.text.ItsNatHTMLTextArea} as editors.</p>
     *
     * @param compEditor the component used as editor. If null {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) is used.
     * @return a default table cell editor instance.
     */
    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor);
    //public ItsNatTableHeaderCellEditor createItsNatTableHeaderCellEditor();

    /**
     * Creates a default tree node editor instance using the specified component
     * as the editor.
     *
     * <p>Current implementation supports {@link org.itsnat.comp.text.ItsNatHTMLInputText},
     * {@link org.itsnat.comp.list.ItsNatHTMLSelectComboBox}, {@link org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox}
     * and {@link org.itsnat.comp.text.ItsNatHTMLTextArea} as editors.</p>
     *
     * @param compEditor the component used as editor. If null {@link org.itsnat.comp.text.ItsNatHTMLInputText} (text not formatted version) is used.
     * @return a default tree node editor instance.
     */
    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor);

    /**
     * Creates a new default label renderer.
     *
     * @return a new default label renderer.
     */
    public ItsNatLabelRenderer createDefaultItsNatLabelRenderer();

    /**
     * Creates a new default list element content renderer.
     *
     * @return a new default list element content renderer.
     */
    public ItsNatListCellRenderer createDefaultItsNatListCellRenderer();

    /**
     * Creates a new default table cell content renderer.
     *
     * @return a new default table cell content renderer.
     */
    public ItsNatTableCellRenderer createDefaultItsNatTableCellRenderer();

    /**
     * Creates a new default table header cell content renderer.
     *
     * @return a new default table header cell content renderer.
     */
    public ItsNatTableHeaderCellRenderer createDefaultItsNatTableHeaderCellRenderer();

    /**
     * Creates a new default tree node content renderer.
     *
     * @return a new default tree node content renderer.
     */
    public ItsNatTreeCellRenderer createDefaultItsNatTreeCellRenderer();

    /**
     * Creates a new default list structure manager.
     *
     * @return a new default list structure manager.
     */
    public ItsNatListStructure createDefaultItsNatListStructure();

    /**
     * Creates a new default table structure manager.
     *
     * @return a new default table structure manager.
     */
    public ItsNatTableStructure createDefaultItsNatTableStructure();

    /**
     * Creates a new default tree structure manager.
     *
     * @return a new default tree structure manager.
     */
    public ItsNatTreeStructure createDefaultItsNatTreeStructure();

    /**
     * Creates a new free include component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free include component.
     */
    public ItsNatFreeInclude createItsNatFreeInclude(Element element,NameValue[] artifacts);

    /**
     * Creates a new free label component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free label component.
     */
    public ItsNatFreeLabel createItsNatFreeLabel(Element element,NameValue[] artifacts);

    /**
     * Creates a new free normal button component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free normal button component.
     */
    public ItsNatFreeButtonNormal createItsNatFreeButtonNormal(Element element,NameValue[] artifacts);

    /**
     * Creates a new free normal button component with a label.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free normal button component.
     */
    public ItsNatFreeButtonNormalLabel createItsNatFreeButtonNormalLabel(Element element,NameValue[] artifacts);

    /**
     * Creates a new free check box component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free check box component.
     */
    public ItsNatFreeCheckBox createItsNatFreeCheckBox(Element element,NameValue[] artifacts);

    /**
     * Creates a new free check box component with a label.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free check box component.
     */
    public ItsNatFreeCheckBoxLabel createItsNatFreeCheckBoxLabel(Element element,NameValue[] artifacts);


    /**
     * Creates a new free radio button component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free radio buttom component.
     */
    public ItsNatFreeRadioButton createItsNatFreeRadioButton(Element element,NameValue[] artifacts);

    /**
     * Creates a new free radio buttom component with a label.
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free radio buttom component.
     */
    public ItsNatFreeRadioButtonLabel createItsNatFreeRadioButtonLabel(Element element,NameValue[] artifacts);


    /**
     * Creates a new free combo box component.
     *
     *
     * @param element the DOM element associated. Can not be null.
     * @param structure the specified structure, if null then is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free combo box component.
     * @see #createDefaultItsNatListStructure()
     */
    public ItsNatFreeComboBox createItsNatFreeComboBox(Element element,ItsNatListStructure structure,NameValue[] artifacts);

    /**
     * Creates a new free list component.
     *
     * @param element the DOM element associated. Can not be null.
     * @param structure the specified structure, if null then is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free list component.
     * @see #createDefaultItsNatListStructure()
     */
    public ItsNatFreeListMultSel createItsNatFreeListMultSel(Element element,ItsNatListStructure structure,NameValue[] artifacts);

    /**
     * Creates a new free table component.
     *
     *
     * @param element the DOM element associated. Can not be null.
     * @param structure the specified structure, if null then is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free table component.
     * @see #createDefaultItsNatTableStructure()
     */
    public ItsNatFreeTable createItsNatFreeTable(Element element,ItsNatTableStructure structure,NameValue[] artifacts);

    /**
     * Creates a new free tree component.
     *
     *
     * @param element the DOM element associated. Can not be null.
     * @param treeTable true if this tree is a tree-table.
     * @param rootless  true if this tree is rootless (root node has not view).
     * @param structure the specified structure, if null then is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free tree component.
     * @see #createDefaultItsNatTreeStructure()
     * @see #createItsNatFreeTree(Element,NameValue[])
     */
    public ItsNatFreeTree createItsNatFreeTree(Element element,boolean treeTable,boolean rootless,ItsNatTreeStructure structure,NameValue[] artifacts);

    /**
     * Creates a new free tree component.
     *
     * <p>By default is not a tree-table unless "treeTable" is defined in the artifacts (value "true") or as a markup attribute (value "true").</p>
     * <p>By default the root node has view unless "rootless" is defined in the artifacts (value "true") or as a markup attribute (value "true").</p>
     * <p>The structure is obtained from artifacts ("useStructure"), if not defined then from markup ("useStructure" attribute), if not defined the default structure is used.
     *
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new free tree component.
     * @see #createItsNatFreeTree(Element,boolean,boolean,ItsNatTreeStructure,NameValue[])
     */
    public ItsNatFreeTree createItsNatFreeTree(Element element,NameValue[] artifacts);

    /**
     * Creates a new modal layer component using the specified element if provided.
     *
     * <p>The z-index associated to the modal layer is the number of modal layer components
     * created before (and not disposed) + 1.
     * </p>
     * <p>If the background parameter is null, no background property is set. This value is not
     * recommended if you want transparency because in some browsers like MSIE 6 if background is not set
     * the layer allows clicking below. Use opacity=0 and any background color to get a transparent layer
     * capturing click events.
     * </p>
     * <p>In SVG the background parameter is used to the fill property</p>
     *
     * @param element the DOM element used to cover the page. May be null (an internal element is used).
     * @param cleanBelow use the clean layer technique.
     * @param opacity a value between 0 and 1 defining the opacity of the modal layer (1 = opaque, 0 = transparent).
     * @param background the background CSS property of the modal layer (fill property in SVG). If null no background is set.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new modal layer component.
     */
    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean cleanBelow,int zIndex,float opacity,String background,NameValue[] artifacts);

    /**
     * Creates a new modal layer component using the specified element if provided.
     *
     * <p>By default z-index is the number of current modal layers + 1 unless "zIndex" is defined as artifact or as a markup attribute.</p>
     *
     * @param element the DOM element used to cover the page. May be null (an internal element is used).
     * @param cleanBelow use the clean layer technique.
     * @param opacity a value between 0 and 1 defining the opacity of the modal layer (1 = opaque, 0 = transparent).
     * @param background the background CSS property of the modal layer (fill property in SVG). If null no background is set.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new modal layer component.
     * @see #createItsNatModalLayer(Element,boolean,int,float,String,NameValue[])
     */
    public ItsNatModalLayer createItsNatModalLayer(Element element,boolean cleanBelow,float opacity,String background,NameValue[] artifacts);

    /**
     * Creates a new modal layer component using the specified element if provided
     *
     * <p>By default cleanBelow is false unless "cleanBelow" is defined as artifact or as a markup attribute.</p>
     * <p>By default z-index is the number of current modal layers + 1 unless "zIndex" is defined as artifact or as a markup attribute.</p>
     * <p>By default opacity is 1.0 unless "opacity" is defined as artifact or as a markup attribute.</p>
     * <p>By default background is "black" unless "background" is defined as artifact or as a markup attribute. In XUL is set to null.</p>
     *
     * @param element the DOM element associated. Can not be null.
     * @param artifacts artifacts used to construct the component. May be null.
     * @return a new modal layer component.
     * @see #createItsNatModalLayer(Element,boolean,int,float,String,NameValue[])
     */
    public ItsNatModalLayer createItsNatModalLayer(Element element,NameValue[] artifacts);
}

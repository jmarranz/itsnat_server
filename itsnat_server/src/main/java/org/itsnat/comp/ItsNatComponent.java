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

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import org.itsnat.comp.label.ItsNatLabel;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatUserData;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.Node;
import org.w3c.dom.events.EventListener;

/**
 * Is the base interface which every ItsNat component implements.
 *
 * <p>Some components have property change tracking like {@link org.itsnat.comp.label.ItsNatLabel} and
 * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted} components. If no property is tracked
 * by the component, listeners registered using {@link #addPropertyChangeListener(PropertyChangeListener)}
 * {@link #addPropertyChangeListener(String,PropertyChangeListener)}
 * or {@link #addVetoableChangeListener(VetoableChangeListener)}
 * are never called.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ItsNatComponentManager#createItsNatComponent(org.w3c.dom.Node,String,org.itsnat.core.NameValue[])
 */
public interface ItsNatComponent extends ItsNatUserData
{
    /**
     * Returns the associated DOM node to this component.
     *
     * @return the associated DOM node.
     */
    public Node getNode();

    /**
     * Changes the associated DOM node (reattachment).
     *
     * @param node the new DOM node.
     */
    public void setNode(Node node);

    /**
     * Registers the specified event listener to the component node.
     *
     * <p>The specified event type is automatically enabled (see {@link #enableEventListener(String)}).</p>
     * <p>If this component has a default behavior associated to the specified event type, the listener
     * is executed <i>before</i> this default behavior if <code>before</code> parameter is true,
     * otherwise is executed <i>after</i>.</p>
     * <p>Several listeners may be registered to the same event type.</p>
     *
     * @param type the event type to listen.
     * @param listener the listener to be executed.
     * @param before if true the listener is executed before the default behavior of the component.
     * @see #removeEventListener(String,EventListener,boolean)
     * @see #addEventListener(ClientDocument,String,EventListener,boolean)
     */
    public void addEventListener(String type,EventListener listener,boolean before);

    /**
     * Unregisters the specified event listener from the component node.
     *
     * @param type the event type of the listener to unregister.
     * @param listener to unregister.
     * @param before if the listener to unregister is a "before" or "after" listener.
     * @see #addEventListener(String,EventListener,boolean)
     * @see #removeEventListener(ClientDocument,String,EventListener,boolean)
     */
    public void removeEventListener(String type,EventListener listener,boolean before);

    /**
     * Registers the specified event listener to the component node.
     *
     * <p>Current implementation calls {@link #addEventListener(String,EventListener,boolean)}
     * with <code>before</code> parameter set to false (executed after).</p>
     *
     * @param type the event type to listen.
     * @param listener the listener to be executed.
     * @see #addEventListener(ClientDocument,String,EventListener)
     */
    public void addEventListener(String type,EventListener listener);

    /**
     * Unregisters the specified event listener from the component node.
     *
     * <p>Current implementation calls {@link #removeEventListener(String,EventListener,boolean)}
     * with <code>before</code> parameter set to false (unregisters the "after" listener).</p>
     *
     * @param type the event type of the listener to unregister.
     * @param listener to unregister.
     * @see #removeEventListener(ClientDocument,String,EventListener)
     */
    public void removeEventListener(String type,EventListener listener);


    /**
     * Registers the specified event listener to the component node for the specific client.
     *
     * <p>The specified event type is automatically enabled (see {@link #enableEventListener(String)}).</p>
     * <p>If this component has a default behavior associated to the specified event type, the listener
     * is executed <i>before</i> this default behavior if <code>before</code> parameter is true,
     * otherwise is executed <i>after</i>.</p>
     * <p>Several listeners may be registered to the same event type.</p>
     *
     * @param clientDoc the client source of events.
     * @param type the event type to listen.
     * @param listener the listener to be executed.
     * @param before if true the listener is executed before the default behavior of the component.
     * @see #removeEventListener(String,EventListener,boolean)
     * @see #addEventListener(String,EventListener,boolean)
     */
    public void addEventListener(ClientDocument clientDoc,String type,EventListener listener,boolean before);

    /**
     * Unregisters the specified event listener from the component node for the specific client.
     *
     * @param clientDoc the client source of events.
     * @param type the event type of the listener to unregister.
     * @param listener to unregister.
     * @param before if the listener to unregister is a "before" or "after" listener.
     * @see #addEventListener(String,EventListener,boolean)
     * @see #removeEventListener(String,EventListener,boolean)
     */
    public void removeEventListener(ClientDocument clientDoc,String type,EventListener listener,boolean before);

    /**
     * Registers the specified event listener to the component node for the specific client.
     *
     * <p>Current implementation calls {@link #addEventListener(String,EventListener,boolean)}
     * with <code>before</code> parameter set to false (executed after).</p>
     *
     * @param clientDoc the client source of events.
     * @param type the event type to listen.
     * @param listener the listener to be executed.
     * @see #addEventListener(String,EventListener)
     */
    public void addEventListener(ClientDocument clientDoc,String type,EventListener listener);

    /**
     * Unregisters the specified event listener from the component node for the specific client.
     *
     * <p>Current implementation calls {@link #removeEventListener(String,EventListener,boolean)}
     * with <code>before</code> parameter set to false (unregisters the "after" listener).</p>
     *
     * @param clientDoc the client source of events.
     * @param type the event type of the listener to unregister.
     * @param listener to unregister.
     * @see #removeEventListener(String,EventListener)
     */
    public void removeEventListener(ClientDocument clientDoc,String type,EventListener listener);

    /**
     * Enable the component to receive events of the specified type.
     *
     * <p>If this component has a default behavior associated to the specified type
     * (and is disabled by default) then is enabled.</p>
     *
     * @param type the event type to enable.
     * @see #addEventListener(String,org.w3c.dom.events.EventListener)
     * @see #enableEventListener(ClientDocument,String)
     */
    public void enableEventListener(String type);

    /**
     * Disables the component to receive events of the specified type.
     *
     * <p>No event listener added with {@link #addEventListener(String,org.w3c.dom.events.EventListener)}
     * is unregistered.</p>
     *
     * @param type the event type to enable.
     * @see #disableEventListener(ClientDocument,String)
     */
    public void disableEventListener(String type);

    /**
     * Enable the component to receive events of the specified type and client.
     *
     * <p>If this component has a default behavior associated to the specified type
     * (and is disabled by default) then is enabled.</p>
     *
     * @param clientDoc the client source of events.
     * @param type the event type to enable.
     * @see #addEventListener(String,org.w3c.dom.events.EventListener)
     * @see #enableEventListener(String)
     */
    public void enableEventListener(ClientDocument clientDoc,String type);

    /**
     * Disables the component to receive events of the specified type and client.
     *
     * <p>No event listener added with {@link #addEventListener(String,org.w3c.dom.events.EventListener)}
     * is unregistered.</p>
     *
     * @param clientDoc the client source of events.
     * @param type the event type to enable.
     * @see #disableEventListener(String)
     */
    public void disableEventListener(ClientDocument clientDoc,String type);

    /**
     * Sets the parameters used to fire and receive AJAX/SCRIPT events by this component.
     *
     * <p>All current registered event listeners are affected.</p>
     *
     *
     * @param type the DOM event type name (click, change etc).
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of events. If negative no timeout is defined.
     * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see #setEventListenerParams(ClientDocument,String,boolean,int,ParamTransport[],String,long)
     */
    public void setEventListenerParams(String type,boolean useCapture,int commMode,
            ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Sets the parameters used to fire and receive AJAX/SCRIPT events by this component and specified client.
     *
     * <p>All current registered event listeners are affected.</p>
     *
     * @param clientDoc the client source of events.
     * @param type the DOM event type name (click, change etc).
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of events. If negative no timeout is defined.
     * @see org.itsnat.core.ItsNatDocument#addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see #setEventListenerParams(String,boolean,int,ParamTransport[],String,long)
     */
    public void setEventListenerParams(ClientDocument clientDoc,String type,boolean useCapture,int commMode,
            ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Returns the ItsNat document this document is associated to.
     *
     * @return the associated ItsNat document.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Returns the ItsNat component manager parent of this component.
     *
     * @return the parent ItsNat component manager.
     */
    public ItsNatComponentManager getItsNatComponentManager();

    /**
     * Returns the user interface manager of this component.
     *
     * @return the user interface manager.
     */
    public ItsNatComponentUI getItsNatComponentUI();

    /**
     * Disposes this component. A disposed component can no longer be used.
     *
     * <p>Typical dispose operations are: disable event listeners and disconnect data and selection models.
     * A component "disposed" <i>should</i> be garbage collected.</p>
     *
     * @see #isDisposed()
     * @see #disableEventListener(String).
     */
    public void dispose();

    /**
     * Informs whether this component was disposed.
     *
     * @return true if this component was disposed.
     * @see #dispose()
     */
    public boolean isDisposed();

    /**
     * Registers a new property change listener listening for any property change.
     *
     * @param listener the property change listener to register.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes the specified property change listener listening for any property change.
     *
     * @param listener the property change listener to unregister.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns all registered listeners listening for any property change.
     *
     * @return a listener array.
     */
    public PropertyChangeListener[] getPropertyChangeListeners();

    /**
     * Registers a new property change listener listening for the specified property name.
     *
     * @param propertyName the property name.
     * @param listener the property change listener to register.
     */
    public void addPropertyChangeListener(String propertyName,PropertyChangeListener listener);

    /**
     * Removes the specified property change listener listening for the specified property name.
     *
     * @param propertyName the property name.
     * @param listener the property change listener to unregister.
     */
    public void removePropertyChangeListener(String propertyName,PropertyChangeListener listener);

    /**
     * Returns all registered listeners listening for changes in the specified property.
     *
     * @param propertyName the property name.
     * @return a listener array.
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    /**
     * Registers a new vetoable property change listener listening for any property change.
     *
     * @param listener the vetoable property change listener to register.
     */
    public void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Removes the specified vetoable property change listener listening for any property change.
     *
     * @param listener the vetoable property change listener to unregister.
     */
    public void removeVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Returns all registered listeners listening for any vetoable property change.
     *
     * @return a listener array.
     */
    public VetoableChangeListener[] getVetoableChangeListeners();

    /**
     * Registers an artifact with the specified name.
     *
     * @param name the artifact name
     * @param value the artifact.
     * @see #getArtifact(String)
     * @see #removeArtifact(String)
     * @see org.itsnat.core.NameValue
     */
    public void registerArtifact(String name,Object value);

    /**
     * Returns the artifact with the specified name.
     *
     * @param name the artifact name to look for.
     * @return the artifact or null if not found.
     * @see #registerArtifact(String,Object)
     * @see #getArtifact(String,boolean)
     */
    public Object getArtifact(String name);

    /**
     * Removes the artifact with the specified name.
     *
     * @param name the artifact name to look for.
     * @return the removed artifact.
     * @see #registerArtifact(String,Object)
     */
    public Object removeArtifact(String name);

    /**
     * Returns the artifact with the specified name.
     *
     * <p>If no artifact is found and <code>cascade</code> is true,
     * the method {@link ItsNatDocument#getArtifact(String,boolean)}
     * is called with <code>cascade</code> set to true to continue searching.</p>
     *
     * @param name the artifact name to look for.
     * @return the artifact or null if not found.
     * @see #getArtifact(String)
     */
    public Object getArtifact(String name,boolean cascade);

    /**
     * Informs whether this component receives UI events.
     *
     * @return true if this component is enabled. By default is true.
     */
    public boolean isEnabled();

    /**
     * Sets whether this component can receive UI events.
     *
     * @param b true to enable this component.
     */
    public void setEnabled(boolean b);

}

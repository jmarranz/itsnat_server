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

package org.itsnat.core.tmpl;

import java.text.DateFormat;
import java.text.NumberFormat;
import org.itsnat.comp.CreateItsNatComponentListener;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.w3c.dom.events.EventListener;

/**
 * Represents a document (page) template. Concrete documents are created
 * using this template.
 *
 * @see org.itsnat.core.ItsNatServlet#registerItsNatDocumentTemplate(String,String,Object)
 * @see org.itsnat.core.ItsNatServlet#getItsNatDocumentTemplate(String)
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatDocumentTemplate extends MarkupTemplate
{

    /**
     * Informs whether the debug mode is enabled.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isDebugMode()}</p>
     *
     * @return true if debug is enabled.
     */
    public boolean isDebugMode();

    /**
     * Sets the debug mode.
     *
     * @param debugMode true to set debug mode.
     */
    public void setDebugMode(boolean debugMode);

    /**
     * Returns the default client error mode.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getClientErrorMode()}</p>
     *
     * @return the default client error mode.
     * @see #setClientErrorMode(int)
     */
    public int getClientErrorMode();

    /**
     * Sets the default client error mode.
     *
     * @param mode the default client error mode.
     * @see #getClientErrorMode()
     */
    public void setClientErrorMode(int mode);

    /**
     * Returns the default communication mode, AJAX or SCRIPT, for events.
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getCommMode()}</p>
     *
     * @return the communication mode.
     * @see #setCommMode(int)
     * @see org.itsnat.core.CommMode
     */
    public int getCommMode();

    /**
     * Sets the default communication mode, AJAX or SCRIPT, for events.
     *
     * @param commMode the new communication mode.
     * @see #getCommMode()
     */
    public void setCommMode(int commMode);

    /**
     * Returns the default timeout in client of asynchronous AJAX/SCRIPT events.
     *
     * <p>This feature only affects to non-XML documents with events enabled
     * and is ignored in synchronous AJAX events</p>
     *
     * <p>When an unfinished request takes more time than the specified timeout,
     * the request is aborted.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getEventTimeout()}</p>
     *
     * @return the timeout of asynchronous AJAX/SCRIPT events in milliseconds.
     * @see #setEventTimeout(long)
     */
    public long getEventTimeout();

    /**
     * Sets the default timeout of asynchronous AJAX/SCRIPT events.
     *
     * @param timeout the new timeout. If negative no timeout is defined.
     * @see #getEventTimeout()
     */
    public void setEventTimeout(long timeout);

    /**
     * Returns whether JavaScript code and/or markup sent to the client is
     * automatically compressed if the browser accepts this encoding.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getUseGZip()}</p>
     *
     * @return the bitwise value containing whether gzip is used to encode JavaScript code and/or markup.
     * @see #setUseGZip(int)
     */
    public int getUseGZip();

    /**
     * Sets whether JavaScript code and/or markup sent to the client is
     * automatically compressed if the browser accepts this encoding.
     *
     * <p>A bitwise value must be used using {@link org.itsnat.core.UseGZip} constants, for instance:
     * setUseGZip(UseGZip.MARKUP | UseGZip.SCRIPT).</p>
     *
     * @param value a bitwise value.
     * @see #getUseGZip()
     * @see org.itsnat.core.UseGZip
     */
    public void setUseGZip(int value);

    /**
     * Informs whether the initial JavaScript code is sent inline into the loaded page
     * or is loaded externally.
     *
     * <p>This feature only affects to non-XML documents
     * with scripting enabled.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isLoadScriptInline()}</p>
     *
     * @return true if fast mode is enabled.
     * @see #setLoadScriptInline(boolean)
     */
    public boolean isLoadScriptInline();

    /**
     * Sets the initial JavaScript code is sent inline into the loaded page
     * or is loaded externally.
     *
     * @param value true to sent inline.
     * @see #isLoadScriptInline()
     */
    public void setLoadScriptInline(boolean value);

    /**
     * Informs whether the fast load mode is enabled.
     *
     * <p>This feature only affects to non-XML documents
     * with scripting enabled.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isFastLoadMode()}</p>
     *
     * @return true if fast mode is enabled.
     * @see #setFastLoadMode(boolean)
     */
    public boolean isFastLoadMode();

    /**
     * Sets whether fast load mode is used.
     *
     * @param fastLoadMode true to enable fast load.
     * @see #isFastLoadMode()
     */
    public void setFastLoadMode(boolean fastLoadMode);

    /**
     * Informs whether the speed oriented node cache is enabled.
     *
     * <p>This feature only affects to non-XML documents
     * with scripting enabled.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isNodeCacheEnabled()}</p>
     *
     * @return true if node cache is enabled.
     * @see #setNodeCacheEnabled(boolean)
     */
    public boolean isNodeCacheEnabled();

    /**
     * Sets whether the speed oriented node cache is enabled.
     *
     * @param enable true to enable node cache.
     * @see #isNodeCacheEnabled()
     */
    public void setNodeCacheEnabled(boolean enable);

    /**
     * Informs whether components are built automatically using the necessary
     * markup declarations.
     *
     * <p>If this feature is enabled the initial document tree is automatically traversed, any component
     * declared in markup is automatically built and registered into the component manager
     * associated to a DOM element, including DOM elements added to the tree in any time.
     * </p>
     *
     * <p>When a node is removed from the tree the associated component, if any,
     * is removed and disposed automatically.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isAutoBuildComponents()}</p>
     *
     * @return true if automatic component build is enabled.
     * @see #setAutoBuildComponents(boolean)
     */
    public boolean isAutoBuildComponents();

    /**
     * Sets whether components are build automatically using the necessary
     * markup declarations.
     *
     * @param value true to enable automatic component build.
     * @see #isAutoBuildComponents()
     */
    public void setAutoBuildComponents(boolean value);

    /**
     * Informs whether dom utils and components use by default the original
     * (saved as pattern) markup to render.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isUsePatternMarkupToRender()}</p>
     *
     * @return true if by default the original markup is used.
     * @see #setUsePatternMarkupToRender(boolean)
     */
    public boolean isUsePatternMarkupToRender();

    /**
     * Sets whether dom utils and components use by default the original
     * (saved as pattern) markup to render.
     *
     * @param value true to enable the use of original markup to render.
     * @see #isUsePatternMarkupToRender()
     */
    public void setUsePatternMarkupToRender(boolean value);

    /**
     * Returns the default data format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getDefaultDateFormat()}</p>
     *
     * @return the default date format.
     * @see #setDefaultDateFormat(DateFormat)
     */
    public DateFormat getDefaultDateFormat();

    /**
     * Sets the default data format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * @param format the default data format.
     * @see #getDefaultDateFormat()
     */
    public void setDefaultDateFormat(DateFormat format);

    /**
     * Returns the default number format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getDefaultNumberFormat()}</p>
     *
     * @return the default data format.
     * @see #setDefaultNumberFormat(NumberFormat)
     */
    public NumberFormat getDefaultNumberFormat();

    /**
     * Sets the default data format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * @param format the default data format.
     * @see #getDefaultNumberFormat()
     */
    public void setDefaultNumberFormat(NumberFormat format);

    /**
     * Returns the default max wait until a server fired event with
     * {@link org.itsnat.core.ItsNatDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event)}
     * is processed by the client and returns.
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled.</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getEventDispatcherMaxWait()}</p>
     *
     * @return the default max wait in milliseconds.
     * @see #setEventDispatcherMaxWait(long)
     */
    public long getEventDispatcherMaxWait();

    /**
     * Sets the default max wait until a server fired event with
     * {@link org.itsnat.core.ItsNatDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event)}
     * is processed by the client and returns.
     *
     * @param wait the default max wait in milliseconds.
     * @see #getEventDispatcherMaxWait()
     */
    public void setEventDispatcherMaxWait(long wait);

    /**
     * Returns the max number of open clients (owner and attached) associated to a document in server.
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled</p>
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getMaxOpenClientsByDocument()}</p>
     *
     * @return the max number of open clients. Defaults to 10.
     * @see #setMaxOpenClientsByDocument(int)
     */
    public int getMaxOpenClientsByDocument();

    /**
     * Sets the max number of open clients (owner and attached) associated to a document in server.
     *
     * @param value the new max number of open clients.
     * @see #getMaxOpenClientsByDocument()
     */
    public void setMaxOpenClientsByDocument(int value);

    /**
     * Informs whether referrer feature is enabled by default.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isReferrerEnabled()}</p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled.</p>
     *
     * @return true if referrer is enabled.
     * @see #setReferrerEnabled(boolean)
     * @see #isEventsEnabled()
     * @see org.itsnat.core.ItsNatServletRequest#getItsNatDocumentReferrer()
     */
    public boolean isReferrerEnabled();

    /**
     * Sets whether referrer feature is enabled by default.
     *
     * @param enabled if referrer is enabled.
     * @see #isReferrerEnabled()
     */
    public void setReferrerEnabled(boolean enabled);

    /**
     * Informs whether referrer "push" feature is enabled by default.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isReferrerPushEnabled()}</p>
     *
     * @return true if referrer "push" is enabled, if false documents created with this template can not be accessed by the referrer.
     * @see #setReferrerPushEnabled(boolean)
     * @see #isReferrerEnabled()
     * @see org.itsnat.core.ItsNatDocument#addReferrerItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public boolean isReferrerPushEnabled();

    /**
     * Sets whether referrer "push" feature is enabled by default.
     *
     * @param enabled if referrer "push" is enabled.
     * @see #isReferrerPushEnabled()
     */
    public void setReferrerPushEnabled(boolean enabled);

    /**
     * Informs whether events (AJAX or SCRIPT based) are enabled.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isEventsEnabled()}</p>
     *
     * <p>This feature only affects to non-XML documents.</p>
     *
     * @return true if events are enabled.
     * @see #setEventsEnabled(boolean)
     */
    public boolean isEventsEnabled();

    /**
     * Sets whether events (AJAX or SCRIPT based) are enabled.
     *
     * @param enabled if events are enabled.
     * @see #isEventsEnabled()
     */
    public void setEventsEnabled(boolean enabled);

    /**
     * Informs whether JavaScript is enabled.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isScriptingEnabled()}</p>
     *
     * <p>This feature only affects to non-XML documents.</p>
     *
     * @return true if scripting is enabled.
     * @see #setScriptingEnabled(boolean)
     */
    public boolean isScriptingEnabled();

    /**
     * Sets whether JavaScript is enabled.
     *
     * <p>If JavaScript is disabled then events are also disabled.</p>
     *
     * @param enabled if JavaScript is enabled.
     * @see #isScriptingEnabled()
     * @see #setEventsEnabled(boolean)
     */
    public void setScriptingEnabled(boolean enabled);

    /**
     * Informs whether the auto clean event listeners mode is enabled.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isAutoCleanEventListeners()}</p>
     *
     * <p>This feature only affects to non-XML documents.</p>
     *
     * @return true if enabled.
     * @see #setAutoCleanEventListeners(boolean)
     */
    public boolean isAutoCleanEventListeners();

    /**
     * Sets whether the auto clean event listeners mode is enabled.
     *
     * @param enabled if auto clean event listeners is enabled.
     * @see #isAutoCleanEventListeners()
     */
    public void setAutoCleanEventListeners(boolean enabled);

    /**
     * Informs whether the internal unload event sent by ItsNat when end user leaves the page,
     * is synchronously sent when the transport is AJAX (XMLHttpRequest) mode by default.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isUseXHRSyncOnUnloadEvent()}</p>
     *
     * <p>This feature only affects to non-XML documents.</p>
     *
     * @return true if enabled.
     * @see #setUseXHRSyncOnUnloadEvent(boolean)
     */
    public boolean isUseXHRSyncOnUnloadEvent();

    /**
     * Sets whether the internal unload event sent by ItsNat when end user leaves the page,
     * is synchronously sent when the transport is AJAX (XMLHttpRequest) mode by default.
     *
     * @param enabled if unload event is sent synchronously in AJAX mode.
     * @see #isUseXHRSyncOnUnloadEvent()
     */
    public void setUseXHRSyncOnUnloadEvent(boolean enabled);

    /**
     * Informs whether the keyboard is necessary for selection on components.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isSelectionOnComponentsUsesKeyboard()}</p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled.</p>
     *
     * @return true if selection uses keyboard.
     * @see #setSelectionOnComponentsUsesKeyboard(boolean)
     */
    public boolean isSelectionOnComponentsUsesKeyboard();

    /**
     * Informs whether the keyboard is necessary for selection on components
     *
     * @param value true to specify the keyboard is necessary for selection.
     * @see #isSelectionOnComponentsUsesKeyboard()
     */
    public void setSelectionOnComponentsUsesKeyboard(boolean value);

    /**
     * Informs whether a joystick is enough to control, for instance, components
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isJoystickMode()}</p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled.</p>
     *
     * @return true if joystick mode is on.
     * @see #setJoystickMode(boolean)
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control, for instance, components
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);


    /**
     * Informs whether markup driven mode is used in components.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#isMarkupDrivenComponents()}</p>
     *
     * <p>This feature only affects to some HTML form based components.</p>
     *
     * @return true if components are markup driven.
     * @see #setMarkupDrivenComponents(boolean)
     */
    public boolean isMarkupDrivenComponents();

    /**
     * Sets whether markup driven mode is used in components.
     *
     * @param value true to enable markup driven.
     * @see #isMarkupDrivenComponents()
     */
    public void setMarkupDrivenComponents(boolean value);
   

    /**
     * Registers a new ItsNat request listener. This listener is called when the framework loads
     * a new requested document using this template or an event is received
     * with a document target loaded by this template.
     *
     * @param listener the listener register.
     * @see #removeItsNatServletRequestListener(ItsNatServletRequestListener)
     * @see org.itsnat.core.ItsNatServlet#addItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public void addItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * Unregisters the specified user defined request listener.
     *
     * @param listener the request listener to remove.
     * @see #addItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public void removeItsNatServletRequestListener(ItsNatServletRequestListener listener);


    /**
     * Adds a remote control listener to this template. This listener is called when a remote view/control
     * is requested to control a document loaded using this template.
     *
     * <p>The listener is called <i>before</i> calling the document registered listener counterparts (if defined).</p>
     *
     * @param listener the listener to add.
     * @see #removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see org.itsnat.core.ItsNatServlet#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see org.itsnat.core.ItsNatDocument#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     */
    public void addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener);


    /**
     * Removes the specified remote control listener.
     *
     * @param listener the listener to remove.
     * @see #addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     */
    public void removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener listener);

    /**
     * Adds a global event listener to this template. This listener is called when any DOM event
     * (standard or extended) is received by a document loaded using this template.
     *
     * <p>The listener is called <i>before</i> calling any DOM event listener registered in
     * documents based on this template. This listener registry is <i>passive</i>, in no way the client
     * is modified (no listener is registered on the client) and usually used
     * for monitoring.</p>
     *
     * @param listener the listener to add.
     * @see #removeEventListener(EventListener)
     * @see org.itsnat.core.ItsNatServlet#addEventListener(EventListener)
     * @see org.itsnat.core.ItsNatDocument#addEventListener(EventListener)
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes the specified global event listener registered in this template.
     *
     * @param listener the listener to remove.
     * @see #addEventListener(EventListener)
     */
    public void removeEventListener(EventListener listener);


    /**
     * Adds a new user defined component factory. This listener is called when the framework needs
     * to create a component instance.
     *
     * @param listener the listener factory to register.
     * @see #removeCreateItsNatComponentListener(CreateItsNatComponentListener)
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
     * the method {@link org.itsnat.core.ItsNatServletConfig#getArtifact(String)}
     * is called to continue searching.</p>
     *
     * @param name the artifact name to look for.
     * @return the artifact or null if not found.
     * @see #getArtifact(String)
     */
    public Object getArtifact(String name,boolean cascade);
}

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

package org.itsnat.core;

import java.text.DateFormat;
import java.text.NumberFormat;
import javax.servlet.ServletConfig;

/**
 * Is the wrapper object defined by ItsNat to wrap the <code>javax.servlet.ServletConfig</code> object.
 *
 * <p>The main use is to setup the default behavior of the servlet.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatServletConfig extends ItsNatUserData
{
    /**
     * Returns the servlet configuration object wrapped.
     *
     * @return the internal servlet configuration object.
     */
    public ServletConfig getServletConfig();

    /**
     * Returns the application context.
     *
     * <p>This object instance is shared by all {@link ItsNatServlet}s
     * defined by the application.</p>
     *
     * @return the application context.
     */
    public ItsNatServletContext getItsNatServletContext();


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
     * Returns the encoding used. This encoding is used to render
     * to text and in the header sent to clients.
     *
     * @return the encoding, UTF-8 by default.
     * @see #setDefaultEncoding(String)
     */
    public String getDefaultEncoding();

    /**
     * Sets the encoding used.
     *
     * @param encoding the new encoding.
     * @see #getDefaultEncoding()
     */
    public void setDefaultEncoding(String encoding);

    /**
     * Informs whether static nodes are serialized as text and globally cached when
     * the template is loaded to save memory (it improves the performance too).
     *
     * <p>This cache flag is configured by MIME type.</p>
     *
     * @param mime the MIME type.
     * @return true if caching is enabled. By default is true if mime is "text/html", "application/xhtml+xml" or "image/svg+xml" else is false.
     * @see #setOnLoadCacheStaticNodes(String,boolean)
     */
    public boolean isOnLoadCacheStaticNodes(String mime);

    /**
     * Sets whether static nodes are serialized as text and globally cached when
     * the template is loaded to save memory (it improves the performance too).
     *
     * @param mime the MIME type.
     * @param cache true to enable the static node cache to the specified MIME.
     * @see #isOnLoadCacheStaticNodes(String)
     */
    public void setOnLoadCacheStaticNodes(String mime,boolean cache);

    /**
     * Informs whether the debug mode is enabled.
     *
     * <p>In debug mode the framework makes more checks to ensure is correctly used.</p>
     *
     * @return true if debug is enabled. True by default.
     * @see #setDebugMode(boolean)
     */
    public boolean isDebugMode();

    /**
     * Sets the debug mode.
     *
     * @param debugMode true to set debug mode.
     * @see #isDebugMode()
     */
    public void setDebugMode(boolean debugMode);

    /**
     * Returns the default client error mode.
     *
     * <p>This value specifies whether the browser catches JavaScript errors
     * and if they are shown to the user using a JavaScript <code>alert</code> call.</p>
     *
     * <p>This feature only affects to non-XML documents
     * with scripting enabled not to fragments</p>
     *
     * @return the default client error mode. By default {@link ClientErrorMode#SHOW_SERVER_AND_CLIENT_ERRORS}
     * @see #setClientErrorMode(int)
     * @see ClientErrorMode
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
     * with events enabled not to fragments</p>
     *
     * @return the communication mode. {@link CommMode#XHR_ASYNC_HOLD} by default.
     * @see #setCommMode(int)
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
     * <p>This feature only affects to non-XML documents with events enabled (not to fragments)
     * and is ignored in synchronous AJAX events</p>
     *
     * <p>When an unfinished request takes more time than the specified timeout,
     * the request is aborted.</p>
     *
     * @return the timeout of asynchronous AJAX/SCRIPT events in milliseconds. -1 by default (no timeout).
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
     * <p>This feature only affects to documents not to fragments</p>
     *
     * @return the bitwise value containing whether gzip is used to encode JavaScript code and/or markup. By default JavaScript code is compressed.
     * @see #setUseGZip(int)
     */
    public int getUseGZip();

    /**
     * Sets whether JavaScript code and/or markup sent to the client is
     * automatically compressed if the browser accepts this encoding.
     *
     * <p>A bitwise value must be used using {@link UseGZip} constants, for instance:
     * <code>setUseGZip(UseGZip.MARKUP | UseGZip.SCRIPT)</code>.</p>
     *
     * @param value a bitwise value.
     * @see #getUseGZip()
     * @see UseGZip
     */
    public void setUseGZip(int value);

    /**
     * Informs whether the initial JavaScript code is sent inline into the loaded page
     * or is loaded externally.
     *
     * <p>This feature only affects to non-XML documents
     * with scripting enabled not to fragments</p>
     *
     * @return true if script is sent inline. True by default.
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
     * with scripting enabled not to fragments</p>
     *
     * @return true if fast mode is enabled. True by default.
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
     * with scripting enabled not to fragments</p>
     *
     * @return true if node cache is enabled. True by default.
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
     * <p>When a node is removed from the tree the associated component if any
     * is removed and disposed automatically.</p>
     *
     * <p>This feature only affects to documents not to fragments</p>
     *
     * @return true if automatic component build is enabled. False by default.
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
     * Returns the default date format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * <p>This feature only affects to documents not to fragments</p>
     *
     * @return the default data format. By default returns <code>DateFormat.getInstance()</code>.
     * @see #setDefaultDateFormat(DateFormat)
     */
    public DateFormat getDefaultDateFormat();

    /**
     * Sets the default date format used by components such as
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
     * <p>This feature only affects to documents not to fragments</p>
     *
     * @return the default data format. By default returns <code>NumberFormat.getInstance()</code>.
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
     * {@link ItsNatDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event)}
     * is processed by the client and returns.
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled not to fragments</p>
     *
     * @return the default max wait in milliseconds. By default is 0 (undefined).
     * @see #setEventDispatcherMaxWait(long)
     */
    public long getEventDispatcherMaxWait();

    /**
     * Sets the default max wait until a server fired event with
     * {@link ItsNatDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event)}
     * is processed by the client and returns.
     *
     * @param wait the default max wait in milliseconds.
     * @see #getEventDispatcherMaxWait()
     */
    public void setEventDispatcherMaxWait(long wait);

    /**
     * Returns the max number of open clients (owner and attached) associated to a document in server.
     *
     * <p>When this number is surpassed, older clients (clients not accesed for a long time ago)
     * are invalidated/removed from the server.
     * </p>
     *
     * <p>A negative number means no limit.</p>
     *
     * <p>This feature is useful to limit the number of attached clients and to clean
     * orphan attached clients.</p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled not to fragments</p>
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
     * <p>This feature only affects to non-XML documents
     * with events enabled not to fragments.</p>
     *
     * <p>This feature only works if events are enabled.</p>
     *
     * @return true if referrer is enabled. By default is false.
     * @see #setReferrerEnabled(boolean)
     * @see #isEventsEnabled()
     * @see ItsNatServletRequest#getItsNatDocumentReferrer()
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
     * <p>This feature only affects to documents not to fragments</p>
     *
     * @return true if referrer "push" is enabled, if false no document can be accessed by the referrer.
     * @see #setReferrerPushEnabled(boolean)
     * @see #isReferrerEnabled()
     * @see ItsNatDocument#addReferrerItsNatServletRequestListener(ItsNatServletRequestListener)
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
     * <p>This feature only affects to non-XML documents not to fragments.</p>
     *
     * @return true if events are enabled. By default is true.
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
     * <p>This feature only affects to non-XML documents not to fragments.</p>
     *
     * @return true if JavaScript is enabled. By default is true.
     * @see #setScriptingEnabled(boolean)
     */
    public boolean isScriptingEnabled();

    /**
     * Sets whether JavaScript is enabled.
     *
     * <p>If JavaScript is disabled then events are disabled too.</p>
     *
     * @param enabled if JavaScript is enabled.
     * @see #isScriptingEnabled()
     * @see #setEventsEnabled(boolean)
     */
    public void setScriptingEnabled(boolean enabled);

    /**
     * Informs whether the auto clean event listeners mode is enabled.
     *
     * <p>This feature only affects to non-XML documents not to fragments.</p>
     *
     * @return true if enabled. By default is true.
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
     * <p>In some very concrete circumstances and old browsers like MSIE 6-7 synchronously AJAX
     * events can hang the browser. If this method returns false, asynchronous mode is used.
     * </p>
     * 
     * <p>This feature only affects to non-XML documents not to fragments.</p>
     *
     * @return true if enabled. By default is true.
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
     * Informs whether dom utils and components use by default the original
     * (saved as pattern) markup to render.
     *
     * <p>This feature only affects to documents not to fragments.</p>
     *
     * @return true if the original markup is used. False by default.
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
     * Informs whether the keyboard is necessary for selection on components.
     *
     * <p>Some components with multiple selection like free lists, tables
     * and trees use the CTRL key for selection like the standard HTML select control
     * uses it.</p>
     *
     * <p>In mobile devices (without or with a "minimal" keyboard) CTRL key
     * is not needed for selection in HTML select controls, because the CTRL key is "ever" pressed.
     * </p>
     *
     * <p>This flag can be used to simulate this behavior on the free components
     * using the CTRL key.</p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled not to fragments.</p>
     *
     * @return true if selection uses keyboard. True by default.
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
     * <p>Some mobile devices do not have stylus and some kind of joystick
     * is the only way to "navigate" through the "live" elements, usually links,
     * form controls and DOM nodes with event listeners associated.
     * </p>
     *
     * <p>This flag is useful as a hint for "free" components with
     * selectable items. In joystick mode every selectable item have
     * a mouse listener associated, therefore users can navigate to the
     * desired item using the joystick. If not in joystick mode the component
     * usually only need an event listener associated to the parent element
     * of the component (because the mouse, pointer or stylus marks the selected item).
     * </p>
     *
     * <p>This feature only affects to non-XML documents
     * with events enabled not to fragments.</p>
     *
     * @return true if joystick mode is on. False by default.
     * @see #setJoystickMode(boolean)
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control, for instance, components
     * (mouse, pointer or stylus not present or not necessary).
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);

    /**
     * Informs whether markup driven mode is used in components.
     *
     * <p>This feature only affects to documents not to fragments and
     * only some HTML form based components are affected.</p>
     *
     * @return true if components are markup driven. False by default.
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
     * Returns the bitmap density reference to be used in Android client to render bitmaps in Android layouts.
     *
     * <p>The default value is defined by {@link org.itsnat.core.ItsNatServletConfig#getClientErrorMode()}</p>
     *
     * @return the default bitmap density reference. 320 (xhdpi) by default.
     * @see #setBitmapDensityReference(int)
     */
    public int getBitmapDensityReference();

    /**
     * Sets the bitmap density reference to be used in Android client to render bitmaps in Android layouts.
     *
     * @param density the new density.
     * @see #getBitmapDensityReference()
     */
    public void setBitmapDensityReference(int density);    
}

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
import java.util.Enumeration;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.domutil.ElementGroupManager;
import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.event.ItsNatAttachedClientEventListener;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Is the ItsNat wrapper of a <code>org.w3c.dom.Document</code> object. Usually represents a web page
 * loaded by a client.
 *
 * <p>Provides many useful utilities to simulate "The Browser Is The Server" approach of
 * ItsNat like event listener registries. Is the factory of DOM utility objects and ItsNat components.</p>
 *
 * <p>ItsNat document objects are created using a {@link ItsNatDocumentTemplate} as pattern and
 * usually obtained calling {@link ItsNatServletRequest#getItsNatDocument()}
 * or {@link ItsNatServletResponse#getItsNatDocument()}
 * when a new document (page) is loading or calling {@link org.itsnat.core.event.ItsNatEvent#getItsNatDocument()}
 * when an event is received.</p>
 *
 * <p>Is not thread save and any object depending on this document (DOM utility objects,
 * components etc) is not thread save too. This is not a problem because any ItsNat request/response
 * thread synchronizes this object before calling user defined code, user defined code
 * executed by an ItsNat request/response thread may be unaware of synchronization isues.
 * </p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatDocument extends ItsNatUserData
{
    /**
     * Returns the client identity. This value is unique per {@link ItsNatSession}
     * and never reused in this context.
     *
     * <p>The identity value is unique no other session-identified object in the same session
     * shares the same id.</p>
     *
     * <p>Although this object is garbage collected, the identity value is never reused
     * by another session-identified object in the same session.</p>
     *
     * @return the identity value.
     */
    public String getId();

    /**
     * Returns the node containing this document.
     *
     * <p>This method returns a non-null object if this document was loaded
     * using an &lt;iframe&gt; or &lt;object&gt; or &lt;embed&gt; following the automatic binding technique
     * (the data or src attribute has the format "?itsnat_doc_name=docName&...").
     * </p>
     *
     * <p>The returned node is owned by a different DOM document (the parent document of this document),
     * use <code>Document.importNode(Node,boolean)</code> to migrate nodes between documents.
     * </p>
     *
     * <p>Use {@link ItsNatNode#getItsNatDocument()} to get the parent ItsNat document</p>
     *
     * <p>ItsNat documents and dependent objects are single threaded, ItsNat
     * automatically locks the document associated to the web request thread
     * and parent documents, in this case synchronization is not needed.
     * </p>
     *
     * @return the node containing this document. May be null.
     */
    public Node getContainerNode();

    /**
     * Returns the default communication mode, AJAX or SCRIPT, for events. Is used for new event listeners when no communication mode is specified.
     * is specified.
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getCommMode()}</p>
     *
     * <p>This feature only affects to non-XML documents with events enabled.</p>
     *
     * @return the communication mode.
     * @see #setCommMode(int)
     * @see CommMode
     */
    public int getCommMode();

    /**
     * Sets the default communication mode, AJAX or SCRIPT, for events. Current defined event listeners
     * are not affected.
     *
     * @param commMode the new communication mode.
     * @see #getCommMode()
     */
    public void setCommMode(int commMode);

    /**
     * Returns the default timeout in client of asynchronous AJAX/SCRIPT events.
     *
     * <p>When an unfinished request takes more time than the specified timeout,
     * the request is aborted.</p>
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getEventTimeout()}</p>
     *
     * <p>This feature only affects non-XML documents with events enabled and
     * is ignored in synchronous AJAX events.</p>
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
     * Returns the default data format used by components such as
     * {@link org.itsnat.comp.text.ItsNatHTMLInputTextFormatted}.
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getDefaultDateFormat()}</p>
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
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getDefaultNumberFormat()}</p>
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
     * Returns the default max wait until a server fired event calling
     * {@link ClientDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event,int,long)}
     * is processed by the client and returns.
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getEventDispatcherMaxWait()}</p>
     *
     * <p>This feature only affects non-XML documents with events enabled.</p>
     *
     * @return the default max wait in milliseconds.
     * @see #setEventDispatcherMaxWait(long)
     */
    public long getEventDispatcherMaxWait();

    /**
     * Sets the default max wait until a server fired event with
     * {@link ClientDocument#dispatchEvent(org.w3c.dom.events.EventTarget,org.w3c.dom.events.Event,int,long)}
     * is processed by the client and returns.
     *
     * @param wait the default max wait in milliseconds.
     * @see #getEventDispatcherMaxWait()
     */
    public void setEventDispatcherMaxWait(long wait);

    /**
     * Returns the max number of open clients (owner and attached) associated to a document in server.
     *
     * <p>This feature only affects to non-XML documents with events enabled</p>
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#getMaxOpenClientsByDocument()}</p>
     *
     * @return the max number of open clients. Defaults to 10.
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
     * Informs whether dom utils and components use by default the original
     * (saved as pattern) markup to render.
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#isUsePatternMarkupToRender()}</p>
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
     * Informs whether a joystick is enough to control, for instance, components
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>The default value is defined by {@link ItsNatDocumentTemplate#isJoystickMode()}</p>
     *
     * <p>This feature only affects to non-XML documents with events enabled.</p>
     *
     * @return true if joystick mode is on.
     * @see #setJoystickMode(boolean)
     * @see org.itsnat.comp.list.ItsNatFreeList#isJoystickMode()
     * @see org.itsnat.comp.table.ItsNatTable#isJoystickMode()
     * @see org.itsnat.comp.tree.ItsNatTree#isJoystickMode()
     */
    public boolean isJoystickMode();

    /**
     * Informs whether a joystick is enough to control, for instance, components
     * (some kind of mouse, pointer or stylus not present or not necessary).
     *
     * <p>This method does not change the current state of already created components
     * but may be the default value of new components with this feature.
     * </p>
     *
     * @param value true to enable joystick mode.
     * @see #isJoystickMode()
     */
    public void setJoystickMode(boolean value);

    /**
     * If this document was created when processing a stateless event, that is, it was loaded as the result of processing a stateless event fired with a JavaScript call
     * document.getItsNatDoc().dispatchEventStateless(...); provided a <code>itsnat_doc_name</code> parameter.
     *
     * @return true if is a stateless document.
     */
    public boolean isCreatedByStatelessEvent();

    /**
     * Parses and converts to DOM the specified string containing markup
     * usually to be inserted into the document.
     *
     * <p>The specified markup must not be a complete document and must
     * be a well-formatted markup fragment.
     * </p>
     *
     * <p>This method does not perform caching, markup code is ever parsed
     * from the scratch. Use with small markup fragments. For very small
     * fragments use normal DOM methods and for big fragments
     * use the fragment templating system.
     * </p>
     *
     * <p>Be careful with the security using this method when the string
     * is constructed containing user data. For instance, a malicious user can add
     * unexpected elements like &lt;script&gt;, &lt;link&gt; or &lt;object&gt;.
     * This is not the recommended approach, insert user provided data using DOM APIs
     * <i>after</i> this call.
     * </p>
     *
     * @param code the markup to parse and convert to DOM
     * @return a document fragment with the result of parsing.
     */
    public DocumentFragment toDOM(String code);

    /**
     * Returns the template this document is based on.
     *
     * @return the template of this document.
     */
    public ItsNatDocumentTemplate getItsNatDocumentTemplate();

    /**
     * Returns the <code>org.w3c.dom.Document</code> wrapped by this object.
     *
     * @return the wrapped DOM Document object.
     */
    public Document getDocument();

    /**
     * Returns the document proxy of the owner of this document. This object represents
     * the browser document/page that requested (loaded) this document by first time (the owner).
     *
     * @return the document proxy of the owner of this document.
     */
    public ClientDocument getClientDocumentOwner();

    /**
     * Returns the scripting utility.
     *
     * @return the scripting utility.
     */
    public ScriptUtil getScriptUtil();

    /**
     * Returns the component manager utility.
     *
     * @return the component manager.
     */
    public ItsNatComponentManager getItsNatComponentManager();

    /**
     * Informs whether this document is in the load phase.
     *
     * @return true if this document is in the load phase.
     * @see org.itsnat.core.event.ItsNatServletRequestListener#processRequest(ItsNatServletRequest,ItsNatServletResponse)
     */
    public boolean isLoading();

    /**
     * Informs whether this document is invalid.
     *
     * <p>The document is marked as invalid when is unloaded (no more attached to a browser document/page).
     * XML documents are automatically invalidated after loading.</p>
     *
     * <p>If invalid the document is not found anymore on session registry ({@link ItsNatSession#getItsNatDocumentById(String)}).
     *
     * @return true if this document is invalid.
     * @see #setInvalid()
     */
    public boolean isInvalid();

    /**
     * Sets this document as invalid.
     *
     * @see #isInvalid()
     */
    public void setInvalid();

    /**
     * Returns the value associated to the specified attribute name.
     *
     * <p>This method is symmetric to
     * <code>ServletRequest.getAttribute(String)</code> and
     * <code>ServletContext.getAttribute(String)</code>.
     * </p>
     *
     * <p>The purpose is to provide a document level attribute registry to complement
     * <code>ServletRequest</code> and <code>ServletContext</code> attribute registries.</p>
     *
     * <p>Is called by {@link ItsNatVariableResolver#getVariable(String)}
     * when the variable resolver was created using {@link #createItsNatVariableResolver()}
     * and does not contain the specified variable name.
     * </p>
     *
     * @param name the attribute name to search.
     * @return the attribute value of null if no attribute exists with the given name.
     * @see #setAttribute(String,Object)
     * @see #getAttributeNames()
     * @see #removeAttribute(String)
     */
    public Object getAttribute(String name);

    /**
     * Registers the specified attribute name and value.
     *
     * <p>This method is symmetric to
     * <code>ServletRequest.setAttribute(String,Object)</code> and
     * <code>ServletContext.setAttribute(String,Object)</code>.
     * </p>
     *
     * @param name the attribute name.
     * @param value the attribute value.
     * @see #getAttribute(String)
     */
    public void setAttribute(String name,Object value);

    /**
     * Returns an enumeration with the registered attribute names and values.
     *
     * <p>This method is symmetric to
     * <code>ServletRequest.getAttributeNames()</code> and
     * <code>ServletContext.getAttributeNames()</code>.
     * </p>
     *
     * @return an enumeration with the registered attributes.
     * @see #getAttribute(String)
     */
    public Enumeration<String> getAttributeNames();

    /**
     * Unregisters the specified attribute with the given name.
     *
     * <p>This method is symmetric to
     * <code>ServletRequest.removeAttribute(String)</code> and
     * <code>ServletContext.removeAttribute(String)</code>.
     * </p>
     *
     * @param name the attribute name.
     * @see #getAttribute(String)
     */
    public void removeAttribute(String name);

    /**
     * Creates a variable resolver bound to this document.
     *
     * @return a variable resolver bound to this document.
     * @see #createItsNatVariableResolver(boolean)
     */
    public ItsNatVariableResolver createItsNatVariableResolver();

    /**
     * Creates a variable resolver bound to this document if the <code>disconnected</code>
     * parameter is set to false.
     *
     * <p>Set <code>disconnected</code> as true if only local variables of
     * the returned variable resolver must be used
     * (this avoids any attempt to resolve unexpected variables in markup).</p>
     *
     * @param disconnected if the new variable resolver is disconnected from the document.
     * @return a variable resolver bound to this document.
     * @see #createItsNatVariableResolver()
     */
    public ItsNatVariableResolver createItsNatVariableResolver(boolean disconnected);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with the
     * document default communication mode and timeout, no extra parameters and no custom JavaScript code.
     *
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean )
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with no extra
     * parameters and no custom JavaScript code, and document default event timeout.
     *
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param commMode communication mode.
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,int )
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,int commMode);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with
     * document default communication mode and timeout, the specified extra
     * parameter and no custom JavaScript code.
     *
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param extraParam client to server data transport and synchronization rule.
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,ParamTransport )
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,ParamTransport extraParam);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with
     * document default communication mode and timeout, the specified extra
     * parameters and no custom JavaScript code.
     *
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param extraParams client to server data transport and synchronization rules.
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,ParamTransport[] )
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,ParamTransport[] extraParams);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with
     * document default communication mode and event timeout, no extra
     * parameters and the specified custom JavaScript code.
     *
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired.
     * @see #addEventListener(EventTarget,String,EventListener,boolean,int,ParamTransport[],String,long)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,String )
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,String preSendCode);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code>.
     *
     * <p>When this listener is registered the server sends custom JavaScript code to register
     * a handler in the client side with the specified node target and DOM event type.
     * When an event is fired this handler forwards the client event to the server
     * and the registered listener receives an <code>org.w3c.dom.events.Event</code> with
     * the same client event data.</p>
     *
     * <p>If two or more listeners are registered sharing the same node target and event type,
     * they all will receive the same event.</p>
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events. If negative no timeout is defined.
     * @see #removeEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean)
     * @see ClientDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,int ,ParamTransport[] ,String ,long )
     * @see CommMode
     */
    public void addEventListener(EventTarget target,String type,EventListener listener,boolean useCapture,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Removes the listener registration with the specified node target, name, listener and capture.
     *
     * @param target the target element. Can not be null.
     * @param type the DOM event type name.
     * @param listener the registered listener.
     * @param useCapture event capture mode.
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#removeEventListener(org.w3c.dom.events.EventTarget, java.lang.String, org.w3c.dom.events.EventListener, boolean)
     */
    public void removeEventListener(EventTarget target,String type,EventListener listener,boolean useCapture);


    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> to receive any node mutation
     * as a mutation event fired by the client, using the default communication mode and timeout of the document
     * and no custom JavaScript code.
     *
     * @param target target element. Can not be null.
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @see #addMutationEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,boolean,int,String,long)
     * @see ClientDocument#addMutationEventListener(org.w3c.dom.events.EventTarget, org.w3c.dom.events.EventListener, boolean)
     */
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> to receive any node mutation
     * as a mutation event fired by the client.
     *
     * <p>When a mutation event is received, this event carries the necessary
     * data to synchronize the server target element with the client. After this
     * automatic synchronization, the listener is called.
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *  ParamTransport[] params = new ParamTransport[]{ new ClientMutation() };
     *        addEventListener(target,"DOMAttrModified",listener,useCapture,commMode,params,preSendCode,eventTimeout);
     *        addEventListener(target,"DOMNodeInserted",listener,useCapture,commMode,params,preSendCode,eventTimeout);
     *        addEventListener(target,"DOMNodeRemoved",listener,useCapture,commMode,params,preSendCode,eventTimeout);
     *        addEventListener(target,"DOMCharacterDataModified",listener,useCapture,commMode,params,preSendCode,eventTimeout);
     * </pre></blockquote>
     *
     * <p>If the target is the <code>org.w3c.dom.Document</code> object, every document change in the client
     * is automatically synchronized in the server.</p>
     *
     * <p>Mutation events only works with some W3C browses like Mozilla/FireFox, Safari and Opera (MSIE 6 does not support W3C DOM mutation events).</p>
     *
     * @param target target element. Can not be null.
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @param commMode communication mode.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events. If negative no timeout is defined.
     * @see #removeMutationEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,boolean)
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addMutationEventListener(EventTarget ,EventListener ,boolean ,int ,String ,long )
     */
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout);

    /**
     * Removes the mutation listener registration with the specified node target, listener and capture.
     *
     *
     * @param target the target element. Can not be null.
     * @param listener the registered listener.
     * @param useCapture event capture mode.
     * @see #addMutationEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,boolean,int,String,long)
     * @see ClientDocument#removeMutationEventListener(EventTarget ,EventListener ,boolean )
     */
    public void removeMutationEventListener(EventTarget target,EventListener listener,boolean useCapture);

    /**
     * Registers a "user" <code>EventListener</code>.
     *
     * <p>When this listener is registered the server sends custom JavaScript code to register
     * a handler in the client side with the specified node target and name.</p>
     *
     * <p>User events are fired in the client calling the ItsNat JavaScript method
     * <code>fireUserEvent(targetNode,name)</code> or <code>dispatchUserEvent(targetNode,evt)</code>
     * (where <code>evt</code> is a special user event see Manual for more info).
     * For instance:</p>
     *
     * <code>document.getItsNatDoc().fireUserEvent(document.body,"myUserAction");</code>
     *
     * <p>Target node and name must be the same parameters used to register the listener.</p>
     *
     * <p>If two or more listeners are registered sharing the same node target and name,
     * they all will receive the same event fired by <code>fireUserEvent</code> with this
     * target/name pair. If a listener instance was already registered and is registered again
     * with the same target and name, this second call does nothing.</p>
     *
     *
     * @param target target element. Can not be null.
     * @param name the user defined event type name.
     * @param listener the listener to receive events.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events. If negative no timeout is defined.
     * @see #removeUserEventListener(org.w3c.dom.events.EventTarget,String,EventListener)
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#addUserEventListener(org.w3c.dom.events.EventTarget, java.lang.String, org.w3c.dom.events.EventListener, int, org.itsnat.core.event.ParamTransport[], java.lang.String, long)
     * @see CommMode
     */
    public void addUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Registers a "user" <code>EventListener</code>.
     *
     * <p>This method calls {@link #addUserEventListener(EventTarget,String,EventListener,int,ParamTransport[],String,long)}
     * with the document default values (communication mode, event timeout) and no extra parameters and custom code.</p>
     *
     *
     * @param target target element. Can not be null.
     * @param name the user defined event type name.
     * @param listener the listener to receive events.
     * @see ClientDocument#addUserEventListener(EventTarget ,String ,EventListener )
     */
    public void addUserEventListener(EventTarget target,String name,EventListener listener);

    /**
     * Removes the listener registration with the specified node target, name and listener.
     *
     * @param target the target element. Can not be null.
     * @param name the user defined event type name.
     * @param listener the registered listener.
     * @see #addUserEventListener(org.w3c.dom.events.EventTarget,String,EventListener,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ClientDocument#removeUserEventListener(EventTarget ,String ,EventListener)
     */
    public void removeUserEventListener(EventTarget target,String name,EventListener listener);

    /**
     * Adds a global event listener to this document. This listener is called when any DOM event
     * (standard or extended) is received by this document.
     *
     * <p>The listener is called <i>before</i> calling any DOM event listener registered in
     * this document. This listener registry is <i>passive</i>, in no way the client
     * is modified (no listener is registered on the client) and usually used
     * for monitoring.</p>
     *
     * @param listener the listener to add.
     * @see #removeEventListener(EventListener)
     * @see ItsNatServlet#addEventListener(EventListener)
     * @see ItsNatDocumentTemplate#addEventListener(EventListener)
     * @see ClientDocument#addEventListener(org.w3c.dom.events.EventListener)
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes the specified global event listener registered in this document.
     *
     * @param listener the listener to remove.
     * @see #addEventListener(EventListener)
     * @see ClientDocument#removeEventListener(org.w3c.dom.events.EventListener)
     */
    public void removeEventListener(EventListener listener);

    /**
     * Add JavaScript code to send to any client (owner or remote control) bound to this document (pending code).
     * This code is distributed to the clients calling {@link ClientDocument#addCodeToSend(Object)}.
     *
     * @param code the code to send, <code>Object.toString()</code> is called to convert to string.
     * @see #isSendCodeEnabled()
     * @see org.itsnat.core.script.ScriptUtil
     * @throws ItsNatException if no code can be added.
     */
    public void addCodeToSend(Object code);

    /**
     * Informs whether JavaScript code can be added to send to the client
     * calling {@link #addCodeToSend(Object)}
     *
     * @return true if new code can be added.
     * @see #disableSendCode()
     */
    public boolean isSendCodeEnabled();

    /**
     * Disables the {@link #addCodeToSend(Object)} method, no new code can be added
     * to send to the client.
     *
     * @see #enableSendCode()
     */
    public void disableSendCode();

    /**
     * Enables the {@link #addCodeToSend(Object)} method, new code can be added
     * to send to the client.
     *
     * @see #disableSendCode()
     */
    public void enableSendCode();

    /**
     * Registers a new <code>CodeToSendListener</code>, this listener is called
     * every time {@link #addCodeToSend(Object)} is called.
     *
     * @param listener the new listener.
     * @see #removeCodeToSendListener(CodeToSendListener)
     */
    public void addCodeToSendListener(CodeToSendListener listener);

    /**
     * Removes a previously registered <code>CodeToSendListener</code>.
     *
     * @param listener the new listener.
     * @see #addCodeToSendListener(CodeToSendListener)
     */
    public void removeCodeToSendListener(CodeToSendListener listener);

    /**
     * Returns the element group manager utility.
     *
     * @return the element group manager utility.
     */
    public ElementGroupManager getElementGroupManager();

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
     * the method {@link ItsNatDocumentTemplate#getArtifact(String,boolean)}
     * is called with <code>cascade</code> set to true to continue searching.</p>
     *
     * @param name the artifact name to look for.
     * @return the artifact or null if not found.
     * @see #getArtifact(String)
     */
    public Object getArtifact(String name,boolean cascade);


    /**
     * Adds a remote control listener to this document. This listener is called when a remote view/control
     * is requested to control a document loaded using this template.
     *
     * @param listener the listener to add.
     * @see #removeItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see ItsNatServlet#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
     * @see ItsNatDocumentTemplate#addItsNatAttachedClientEventListener(ItsNatAttachedClientEventListener)
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
     * Create a remote event to be dispatched locally or to the client.
     *
     * <p>This method works the same as <code>org.w3c.dom.events.Document.createEvent(String)</code>,
     * in this case the event object created is ready to be dispatched to the
     * remote event listeners registered calling:
     * <ul>
     *   <li><code>EventTarget.dispatchEvent(Event)</code>: this method detects
     *   whether the submitted event was created by this factory method then redirects
     *   to {@link #dispatchEvent(EventTarget,Event)}.
     *   </li>
     *   <li>{@link #dispatchEvent(EventTarget,Event)}</li>
     *   <li>{@link #dispatchEventLocally(EventTarget,Event)}</li>
     *   <li>{@link ClientDocument#dispatchEvent(EventTarget,Event)}</li>
     * </ul>
     *
     * </p>
     *
     * @param eventType The <code>eventType</code> parameter specifies the
     *   type of <code>Event</code> interface to be created.
     * @return the new event.
     */
    public Event createEvent(String eventType) throws DOMException;

    /**
     * Dispatches the specified event to the specified node target.
     *
     * <p>If the current thread was not started calling {@link ClientDocument#startEventDispatcherThread(Runnable)}
     * current implementation calls {@link #dispatchEventLocally(EventTarget,Event)},
     * otherwise calls {@link ClientDocument#dispatchEvent(EventTarget,Event)}.
     * </p>
     *
     * <p>This method is called by <code>org.w3c.dom.events.EventTarget#dispatchEvent(Event)</code>
     * when the event object was created calling {@link #createEvent(String)}.
     * </p>
     *
     * @param target the event target DOM object.
     * @param evt the DOM event to send to target.
     * @return The return value is false if at least one of the event handlers which handled this event called Event.preventDefault(). Otherwise it returns true.
     */
    public boolean dispatchEvent(EventTarget target,Event evt) throws EventException;

    /**
     * Dispatches the specified event to the specified node target directly on the server.
     *
     * <p>The event is routed across the server DOM tree and dispatched to listeners as specified by the W3C DOM Events
     * standard including bubbling and capturing.
     * </p>
     *
     * @param target the event target DOM object.
     * @param evt the DOM event to send to target.
     * @return The return value is false if at least one of the event handlers which handled this event called Event.preventDefault(). Otherwise it returns true.
     */
    public boolean dispatchEventLocally(EventTarget target,Event evt) throws EventException;

    /**
     * Returns the time in milliseconds when this object was created.
     *
     * @return the creation time.
     */
    public long getCreationTime();

    /**
     * Registers a new ItsNat referrer request listener. This listener is called when the framework loads
     * a new target document and this document is being unloaded <i>before</i> the target request listeners are executed
     * (referrer push).
     *
     * @param listener the listener to register.
     * @see #removeReferrerItsNatServletRequestListener(ItsNatServletRequestListener)
     * @see ItsNatDocumentTemplate#isReferrerPushEnabled()
     */
    public void addReferrerItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * Unregisters the specified user defined referrer request listener.
     *
     * @param listener the listener to remove.
     * @see #addReferrerItsNatServletRequestListener(ItsNatServletRequestListener)
     */
    public void removeReferrerItsNatServletRequestListener(ItsNatServletRequestListener listener);

    /**
     * Informs whether the specified node is marked as "child nodes disconnected"
     *
     * @param node the node to ask.
     * @return true if the specified node is marked as "child nodes disconnected"
     * @see #disconnectChildNodesFromClient(Node)
     * @see #reconnectChildNodesToClient(Node)
     */
    public boolean isDisconnectedChildNodesFromClient(Node node);

    /**
     * Removes the child nodes of the specified node to save server memory but not in client.
     *
     * <p>After this call the child nodes in client are not longer in sync with server
     * and can be freely manipulated (including insertion and removing).
     * </p>
     *
     * <p>The specified node is marked as "child nodes disconnected", the inmmediate call to
     * {@link #isDisconnectedChildNodesFromClient(Node)} will return true.
     * </p>
     *
     * <p>Returns the removed child nodes following the same criteria as {@link org.itsnat.core.domutil.ItsNatDOMUtil#extractChildren(Node)}.
     *
     * <p>If the content of the parent node is already disconnected or is a node not into the document
     * an {@link ItsNatException} is thrown.
     * </p>
     *
     * @param node the parent node which content is going to be disconnected and removed.
     * @return null if empty, the child node when only a child node or a DocumentFragment containing the child nodes.
     * @see #reconnectChildNodesToClient(Node)
     */
    public Node disconnectChildNodesFromClient(Node node);

    /**
     * Reconnects further new child nodes of the specified node with the client, the child nodes in client are removed
     * and again the client will be in sync with server.
     *
     * <p>Further insertion of child nodes will be also effective in client.</p>
     *
     * <p>This method is automatically/implicitly called by ItsNat when a node is inserted into a
     * "child nodes disconnected" node, the node inserted then in also inserted into the client now in sync.
     * </p>
     *
     * <p>If the speficied node is not a "child nodes disconnected" node this call does nothing.</p>
     *
     * @param node the parent node which content will be in sync again with client.
     * @see #disconnectChildNodesFromClient(Node)
     */
    public void reconnectChildNodesToClient(Node node);
}

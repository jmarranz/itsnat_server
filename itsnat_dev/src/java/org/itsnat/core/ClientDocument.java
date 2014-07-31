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

import org.itsnat.core.event.CodeToSendListener;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.core.script.ScriptUtil;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Represents the document/page of the brower. A client document may be a "normal" document (owner)
 * or a remote view/control document.
 *
 * @see ItsNatDocument#getClientDocumentOwner()
 * @see org.itsnat.core.event.ItsNatEvent#getClientDocument()
 * @see ItsNatServletRequest#getClientDocument()
 * @author Jose Maria Arranz Santamaria
 */
public interface ClientDocument extends ItsNatUserData
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
     * Returns the parent ItsNat session. If this object is a remote control client the session
     * may be different to the document owner session.
     *
     * @return the ItsNat session.
     */
    public ItsNatSession getItsNatSession();

    /**
     * Returns the ItsNat document associated.
     *
     * <p>If this object is a remote control client
     * the document returned may be invalid (see {@link ItsNatDocument#isInvalid()}).
     * </p>
     *
     * @return the ItsNat document, may be null if this client is a remote control client.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Add JavaScript code to send to the client (pending code). This code is removed and
     * sent to the client as the return of the load process or any event.
     *
     * <p>Use this method exceptionally when <i>only</i> this client must receive the code submitted,
     * otherwise use {@link ItsNatDocument#addCodeToSend(Object)}</p>
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
     * Returns the time in milliseconds when this object was created.
     *
     * @return the creation time
     */
    public long getCreationTime();

    /**
     * Returns the time in milliseconds of the last access to the document using
     * this client.
     *
     * @return the time of the last access.
     */
    public long getLastRequestTime();

    /**
     * Returns the scripting utility.
     *
     * @return the scripting utility.
     */
    public ScriptUtil getScriptUtil();

    /**
     * Creates a COMET notifier bound to this client and to the server document associated.
     *
     * <p>Current implementation calls {@link #createCometNotifier(long)} with the
     * default event timeout returned by {@link ItsNatDocument#getEventTimeout()}.</p>
     *
     * @return a new COMET notifier.
     */
    public CometNotifier createCometNotifier();

    /**
     * Creates a COMET notifier bound to this client and to the server document associated.
     *
     * <p>The communication mode is the pure asynchronous mode of the default communication mode 
     * ({@link CommMode#XHR_ASYNC} or {@link CommMode#SCRIPT})
     * </p>
     *
     * @param eventTimeout the timeout for any event used to notify the client. If negative no timeout is defined.
     * @return a new COMET notifier.
     * @see #createCometNotifier(int,long)
     */
    public CometNotifier createCometNotifier(long eventTimeout);

    /**
     * Creates a COMET notifier bound to this client and to the server document associated.
     *
     * @param commMode the communication mode, must be pure asynchronous.
     * @param eventTimeout the timeout for any event used to notify the client. If negative no timeout is defined.
     * @return a new COMET notifier.
     * @see CommMode
     */
    public CometNotifier createCometNotifier(int commMode,long eventTimeout);

    /**
     * Creates a COMET notifier bound to this client and to the server document associated.
     *
     * @param commMode the communication mode, must be pure asynchronous.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.      
     * @param eventTimeout the timeout for any event used to notify the client. If negative no timeout is defined.
     * @return a new COMET notifier.
     * @see CommMode
     */
    public CometNotifier createCometNotifier(int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);    
    
    /**
     * Executes the specified task in a new thread, this code is ready to
     * call <code>EventTarget.dispatchEvent(Event)</code> or
     * {@link #dispatchEvent(EventTarget,Event,int,long)}
     * many times.
     *
     * <p>Use the thread created by this method to send events fired in the server
     * to the browser simulating user actions.
     * </p>
     *
     * <p>This method must be called using a servlet-request thread.</p>
     *
     * @param task the task to execute.
     */
    public void startEventDispatcherThread(Runnable task);

   /**
     * Sends the specified event to the browser and waits until this event is dispatched
     * calling dispatchEvent (W3C browsers) or fireEvent (MSIE) on the browser.
     * The call returns when the event is processed by the browser.
     *
     * <p>Used the default communication mode of the document ({@link ItsNatDocument#getCommMode()}) and 
    * event timeout ({@link ItsNatDocument#getEventTimeout()}).
     * </p>
     *
     * @param target the event target DOM object.
     * @param evt the DOM event to send to target.
     * @return The return value is false if at least one of the event handlers which handled this event called Event.preventDefault(). Otherwise it returns true.
     * @see #dispatchEvent(EventTarget,Event,int,long)
     */
    public boolean dispatchEvent(EventTarget target,Event evt) throws EventException;


   /**
     * Sends the specified event to the browser and waits until this event is dispatched
     * calling dispatchEvent (W3C browsers) or fireEvent (MSIE) on the browser.
     * The call returns when the event is processed by the browser.
     *
     * <p>This method must not be called using a servlet-request thread.
     * See the Reference Manual ("Events fired by the server" chapter) about requirements of the caller thread.
     * </p>
     *
     * <p>The <code>commMode</code> parameter and <code>eventTimeout</code> are used to specify the communication
     * mode and timeout of the internal event sent to the server to notify the client has
     * already dispatched the event.
     * </p>
     *
     * @param target the event target DOM object.
     * @param evt the DOM event to send to target.
     * @param commMode communication mode.
     * @param eventTimeout the timeout of the event when asynchronous. If negative no timeout is defined.
     * @return The return value is false if at least one of the event handlers which handled this event called Event.preventDefault(). Otherwise it returns true.
     * @see ItsNatDocument#dispatchEvent(EventTarget,Event)
     * @see #startEventDispatcherThread(Runnable code)
     */
    public boolean dispatchEvent(EventTarget target,Event evt,int commMode,long eventTimeout) throws EventException;

    /**
     * Registers a "continue" <code>EventListener</code> with the
     * document default communication mode and event timeout, no extra parameters and no custom JavaScript code.
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param listener the listener to receive the event.
     * @see #addContinueEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,int,org.itsnat.core.event.ParamTransport[],String,long)
     */
    public void addContinueEventListener(EventTarget target,EventListener listener);

    /**
     * Registers a "continue" <code>EventListener</code>.
     *
     * <p>When this listener is registered the server sends custom JavaScript code to fire automatically
     * from browser a {@link org.itsnat.core.event.ItsNatContinueEvent} sent to the listener. This event usually carries
     * client data necesary to continue a pending server task (and any JavaScript code sent to the client
     * prior to register the listener was executed, this new client state may be the carried data if any).</p>
     *
     * <p>There is no "remove listener" method because the listener is automatically
     * removed when receives and processes the event.</p>
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param listener the listener to receive the event.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events. If negative no timeout is defined.
     * @see CommMode
     */
    public void addContinueEventListener(EventTarget target,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Creates a timer utility object associated to this client.
     *
     * @return a timer object.
     */
    public ItsNatTimer createItsNatTimer();

    /**
     * Executes the specified <code>task</code> in a new thread and ensures that any DOM modification performed by this task
     * is sent to the client when the task ends.
     *
     * <p>If <code>lockDoc</code> is true this document is synchronized during
     * the task execution, but if <code>lockDoc</code> is false the task code
     * must synchronize the document before any access.</p>
     *
     * <p>In a long running task is highly recommended to set <code>lockDoc</code>
     * as false to avoid a long document lock.</p>
     *
     * @param task the task to execute in a new thread.
     * @param lockDoc whether the document is synchronized by the framework.
     * @param maxWait maximum time in milliseconds to wait until the task ends. 0 means unlimited wait.
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param listener the listener to receive the event when the client is going to be notified. May be null.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout for the event used to notify the client. If negative no timeout is defined.
     */
    public void addAsynchronousTask(Runnable task,boolean lockDoc,int maxWait,EventTarget target,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Executes the specified <code>task</code> in a new thread and ensures that any DOM modification performed by this task
     * is sent to the client when the task ends. The document is not locked and waits indefinitely.
     *
     * @param task the task to execute in a new thread.
     * @param listener the listener to receive the event when the client is going to be notified. May be null.
     * @see #addAsynchronousTask(Runnable,boolean,int,EventTarget,EventListener,int,ParamTransport[],String,long)
     */
    public void addAsynchronousTask(Runnable task,EventListener listener);

    /**
     * Registers a new DOM <code>org.w3c.dom.events.EventListener</code> with the
     * document default communication mode and timeout, no extra parameters and no custom JavaScript code.
     *
     * @param target target element. Can not be null.
     * @param type the DOM event type name (click, change etc).
     * @param listener the listener to receive events.
     * @param useCapture if event capture is enabled. False is the most portable value (MSIE v6 does not support event capture).
     * @see #addEventListener(org.w3c.dom.events.EventTarget,String,org.w3c.dom.events.EventListener,boolean,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ItsNatDocument#addEventListener(EventTarget,String,EventListener,boolean)
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
     * @see ItsNatDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,int )
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
     * @see ItsNatDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,ParamTransport )
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
     * @see ItsNatDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,ParamTransport[] )
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
     * @see ItsNatDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,String )
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
     * @see ItsNatDocument#addEventListener(EventTarget ,String ,EventListener ,boolean ,int ,ParamTransport[] ,String ,long )
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
     * @see ItsNatDocument#removeEventListener(org.w3c.dom.events.EventTarget, java.lang.String, org.w3c.dom.events.EventListener, boolean)
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
     * @see ItsNatDocument#addMutationEventListener(org.w3c.dom.events.EventTarget, org.w3c.dom.events.EventListener, boolean)
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
     * @see ItsNatDocument#addMutationEventListener(EventTarget ,EventListener ,boolean ,int ,String ,long )
     */
    public void addMutationEventListener(EventTarget target,EventListener listener,boolean useCapture,int commMode,String preSendCode,long eventTimeout);


    /**
     * Removes the mutation listener registration with the specified node target, listener and capture.
     *
     * @param target the target element. Can not be null.
     * @param listener the registered listener.
     * @param useCapture event capture mode.
     * @see #addMutationEventListener(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,boolean,int,String,long)
     * @see ItsNatDocument#removeMutationEventListener(EventTarget ,EventListener ,boolean )
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
     * @see ItsNatDocument#addUserEventListener(org.w3c.dom.events.EventTarget, java.lang.String, org.w3c.dom.events.EventListener, int, org.itsnat.core.event.ParamTransport[], java.lang.String, long)
     * @see CommMode
     */
    public void addUserEventListener(EventTarget target,String name,EventListener listener,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Registers a "user" <code>EventListener</code>.
     *
     * <p>This method calls {@link #addUserEventListener(EventTarget,String,EventListener,int,ParamTransport[],String,long)}
     * with the document default values (communication mode, event timeout) and no extra parameters and custom code.</p>
     *
     * @param target target element. Can not be null.
     * @param name the user defined event type name.
     * @param listener the listener to receive events.
     * @see ItsNatDocument#addUserEventListener(EventTarget ,String ,EventListener )
     */
    public void addUserEventListener(EventTarget target,String name,EventListener listener);

    /**
     * Removes the listener registration with the specified node target, name and listener.
     *
     * @param target the target element. Can not be null.
     * @param name the user defined event type name.
     * @param listener the registered listener.
     * @see #addUserEventListener(org.w3c.dom.events.EventTarget,String,EventListener,int,org.itsnat.core.event.ParamTransport[],String,long)
     * @see ItsNatDocument#removeUserEventListener(EventTarget ,String ,EventListener)
     */
    public void removeUserEventListener(EventTarget target,String name,EventListener listener);

    /**
     * Adds a global event listener to this client. This listener is called when any DOM event
     * (standard or extended) is received by this client.
     *
     * <p>The listener is called <i>before</i> calling any DOM event listener registered in
     * this client. This listener registry is <i>passive</i>, in no way the client
     * is modified (no listener is registered on the client) and usually used
     * for monitoring.</p>
     *
     * @param listener the listener to add.
     * @see #removeEventListener(EventListener)
     * @see ItsNatServlet#addEventListener(EventListener)
     * @see org.itsnat.core.tmpl.ItsNatDocumentTemplate#addEventListener(EventListener)
     * @see ItsNatDocument#addEventListener(org.w3c.dom.events.EventListener) 
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes the specified global event listener registered in this client.
     *
     * @param listener the listener to remove.
     * @see #addEventListener(EventListener)
     * @see ItsNatDocument#removeEventListener(org.w3c.dom.events.EventListener)
     */
    public void removeEventListener(EventListener listener);
}

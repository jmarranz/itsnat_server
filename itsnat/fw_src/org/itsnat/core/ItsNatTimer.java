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

import org.itsnat.core.event.ItsNatTimerHandle;
import java.util.Date;
import org.itsnat.core.event.ParamTransport;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/**
 * Utility interface to manage scheduled remote tasks (timer tasks).
 * An ItsNat scheduled or timer task is a task executed following a time based schedule,
 * when the task must be executed the client sends an event received
 * by a user defined listener (the task to execute).
 *
 * <p>The ItsNatTimer features and API are inspired in <code>java.util.Timer</code>
 * but the main differences are that timer events are sent by the client
 * and the server does not use any special thread to schedule the event sequence.</p>
 *
 * <p>Note: big documentation parts of this interface are copied from
 * <code>java.util.Timer</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 * @see ClientDocument#createItsNatTimer()
 */
public interface ItsNatTimer
{
    /**
     * Returns the parent client document this object belongs to.
     *
     * @return the parent client document.
     */
    public ClientDocument getClientDocument();

    /**
     * Returns the parent document this object belongs to.
     *
     * @return the parent document.
     */
    public ItsNatDocument getItsNatDocument();

    /**
     * Terminates this timer manager, discarding any currently scheduled tasks.
     * Does not interfere with a currently executing task (if it exists).
     * Once a timer has been terminated no more tasks may be scheduled on it.
     *
     * <p>Note that calling this method from within the
     * <code>EventListener.handleEvent(Event)</code>
     * method of a timer task that was invoked by this timer absolutely guarantees that
     * the ongoing task execution is the last task execution that will ever
     * be performed by this timer.
     *
     * <p>This method may be called repeatedly; the second and subsequent
     * calls have no effect.</p>
     */
    public void cancel();


    /**
     * Schedules the specified task (the <code>EventListener</code> object) for execution at the specified time.  If
     * the time is in the past, the task is scheduled for immediate execution.
     *
     * <p>When the scheduled task is fully finished or cancelled is automatically unregistered.</p>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task task to be scheduled. It receives timer events.
     * @param time time at which task is to be executed.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date time,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task (the <code>EventListener</code> object) for execution at the specified time.  If
     * the time is in the past, the task is scheduled for immediate execution.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *        int commMode = getItsNatDocument().getCommMode();
     *        long eventTimeout = getItsNatDocument().getEventTimeout();
     *        return schedule(target,task,time,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task task to be scheduled. It receives timer events.
     * @param time time at which task is to be executed.
     * @return a timer handle, this object represents the scheduled task.
     * @see #schedule(EventTarget,EventListener,Date,int,ParamTransport[],String,long)
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date time);

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason, subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period.
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * <p>When the scheduled task is fully finished or cancelled is automatically unregistered.</p>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param firstTime first time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date firstTime, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *        int commMode = getItsNatDocument().getCommMode();
     *        long eventTimeout = getItsNatDocument().getEventTimeout();
     *        return schedule(target,task,firstTime,period,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param firstTime first time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a timer handle, this object represents the scheduled task.
     * @see #schedule(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,Date,long,int,org.itsnat.core.event.ParamTransport[],String,long)
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, Date firstTime, long period);

    /**
     * Schedules the specified task for execution after the specified delay.
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task  task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task for execution after the specified delay.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *        int commMode = getItsNatDocument().getCommMode();
     *        long eventTimeout = getItsNatDocument().getEventTimeout();
     *        return schedule(target,task,delay,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task  task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @return a timer handle, this object represents the scheduled task.
     * @see #schedule(org.w3c.dom.events.EventTarget,org.w3c.dom.events.EventListener,long,int,org.itsnat.core.event.ParamTransport[],String,long)
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay);

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     *
     * <p>In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason, subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period.
     *
     * <p>Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     *
     * <p>When the scheduled task is fully finished or cancelled is automatically unregistered.</p>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *        int commMode = getItsNatDocument().getCommMode();
     *        long eventTimeout = getItsNatDocument().getEventTimeout();
     *        return schedule(target,task,delay,period,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle schedule(EventTarget target,EventListener task, long delay, long period);

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason, two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period.
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * <p>When the scheduled task is fully finished or cancelled is automatically unregistered.</p>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, Date firstTime, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
     *        int commMode = getItsNatDocument().getCommMode();
     *        long eventTimeout = getItsNatDocument().getEventTimeout();
     *        return scheduleAtFixedRate(target,task,firstTime,period,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     *
     * @param target optional target element usually useful along with {@link org.itsnat.core.event.ParamTransport} objects. May be null.
     * @param task   task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, Date firstTime, long period);


    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     *
     * <p>In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason, two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period.
     *
     * <p>Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     *
     * <p>When the scheduled task is fully finished or cancelled is automatically unregistered.</p>
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @param commMode communication mode.
     * @param extraParams optional client to server data transport and synchronization rules. May be null.
     * @param preSendCode custom JavaScript code to execute before an event of this listener type is fired. May be null.
     * @param eventTimeout the timeout of asynchronous events sent by the timer. If negative no timeout is defined.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, long delay, long period,int commMode,ParamTransport[] extraParams,String preSendCode,long eventTimeout);

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     *
     * <p>The default communication mode is used.</p>
     *
     * <p>Current implementation does the following:</p>
     * <blockquote><pre>
        int commMode = getItsNatDocument().getCommMode();
        long eventTimeout = getItsNatDocument().getEventTimeout();
        return scheduleAtFixedRate(target,task,delay,period,commMode,null,null,eventTimeout);
     * </pre></blockquote>
     *
     * @param task   task to be scheduled.
     * @param delay  delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     * @return a timer handle, this object represents the scheduled task.
     */
    public ItsNatTimerHandle scheduleAtFixedRate(EventTarget target,EventListener task, long delay, long period);
}

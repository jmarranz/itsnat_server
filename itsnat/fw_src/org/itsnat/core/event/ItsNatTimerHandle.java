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

package org.itsnat.core.event;

import org.itsnat.core.ItsNatTimer;
import org.itsnat.core.ItsNatUserData;

/**
 * Represents a scheduled/timer task by an {@link ItsNatTimer}
 *
 * @author Jose Maria Arranz Santamaria
 */
public interface ItsNatTimerHandle extends ItsNatUserData
{
    /**
     * Returns the timer manager this scheduled task belongs to.
     *
     * @return the timer manager.
     */
    public ItsNatTimer getItsNatTimer();

    /**
     * Returns the time, in the format returned by <code>Date.getTime()</code>, at which
     * the task was (or is going to be) executed by the first time.
     *
     * @return the time of the first time.
     */
    public long getFirstTime();

    /**
     * Returns the time in milliseconds between successive task executions.
     *
     * @return the period in milliseconds. 0 if the task is executed only once.
     */
    public long getPeriod();

    /**
     * Cancels this timer task.  If the task has been scheduled for one-time
     * execution and has not yet run, or has not yet been scheduled, it will
     * never run.  If the task has been scheduled for repeated execution, it
     * will never run again.  (If the task is running when this call occurs,
     * the task will run to completion, but will never run again.)
     *
     * <p>Note that calling this method from within the
     * <code>EventListener.handleEvent(Event)</code>
     * method of a repeating timer task absolutely guarantees that the timer task will
     * not run again.
     *
     * <p>This method may be called repeatedly; the second and subsequent
     * calls have no effect.
     *
     * <p>Note: documentation copied (and slightly modified) from
     * <code>java.util.TimerTask.cancel()</code>.</p>
     *
     * @return true if this task is scheduled for one-time execution and has
     *         not yet run, or this task is scheduled for repeated execution.
     *         Returns false if the task was scheduled for one-time execution
     *         and has already run, or if the task was never scheduled, or if
     *         the task was already cancelled.  (Loosely speaking, this method
     *         returns <tt>true</tt> if it prevents one or more scheduled
     *         executions from taking place.)
     */
    public boolean cancel();

    /**
     * Informs whether this timer task is cancelled
     *
     * @return true if this task is cancelled.
     * @see #cancel()
     */
    public boolean isCancelled();

    /**
     * Returns the <i>scheduled</i> execution time of the most recent
     * <i>actual</i> execution of this task.  (If this method is invoked
     * while task execution is in progress, the return value is the scheduled
     * execution time of the ongoing task execution.)
     *
     * <p>This method is typically invoked from within a task's
     * <code>EventListener.handleEvent(Event)</code>
     * method, to determine whether the current execution of the task is sufficiently
     * timely to warrant performing the scheduled activity:
     * <pre>
     *   public void run() {
     *       if (System.currentTimeMillis() - scheduledExecutionTime() >=
     *           MAX_TARDINESS)
     *               return;  // Too late; skip this execution.
     *       // Perform the task
     *   }
     * </pre>
     * This method is typically <i>not</i> used in conjunction with
     * <i>fixed-delay execution</i> repeating tasks, as their scheduled
     * execution times are allowed to drift over time, and so are not terribly
     * significant.
     *
     * <p>Note: documentation copied (and slightly modified) from
     * <code>java.util.TimerTask.scheduledExecutionTime()</code>.</p>
     *
     * @return the time at which the most recent execution of this task was
     *         scheduled to occur, in the format returned by Date.getTime().
     *         The return value is the same as {@link #getFirstTime()} if the task has yet to commence
     *         its first execution.
     */
    public long scheduledExecutionTime();
}

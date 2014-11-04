package org.itsnat.droid.impl.xmlinflater.layout;

import java.util.LinkedList;

/**
 * Created by jmarranz on 4/09/14.
 */
public class PendingPostInsertChildrenTasks
{
    protected LinkedList<Runnable> taskList;

    public PendingPostInsertChildrenTasks()
    {
    }

    public void addTask(Runnable task)
    {
        if (taskList == null) this.taskList = new LinkedList<Runnable>();
        taskList.add(task);
    }

    public void executeTasks()
    {
        if (taskList != null)
        {
            for (Runnable task : taskList) task.run();
        }
    }

}

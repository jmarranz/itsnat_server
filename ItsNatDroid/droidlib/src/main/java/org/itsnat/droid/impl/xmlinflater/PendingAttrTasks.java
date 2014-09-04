package org.itsnat.droid.impl.xmlinflater;

import java.util.LinkedList;

/**
 * Created by jmarranz on 4/09/14.
 */
public class PendingAttrTasks
{
    protected LinkedList<Runnable> postInsertChildrenTask;

    public void addPostInsertChildrenTask(Runnable task)
    {
        if (postInsertChildrenTask == null) this.postInsertChildrenTask = new LinkedList<Runnable>();
        postInsertChildrenTask.add(task);
    }

    public void executePostInsertChildrenTasks()
    {
        if (postInsertChildrenTask == null) return;
        for(Runnable task : postInsertChildrenTask) task.run();
    }
}

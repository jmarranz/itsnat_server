package org.itsnat.droid.impl.xmlinflater;

import android.view.View;
import android.widget.GridLayout;

import java.util.LinkedList;

/**
 * Created by jmarranz on 6/05/14.
 */
public abstract class OneTimeAttrProcess
{
    protected View view;
    protected boolean neededSetLayoutParams;
    protected LinkedList<Runnable> taskLastList;

    public OneTimeAttrProcess(View view)
    {
        this.view = view;
    }

    public static OneTimeAttrProcess createOneTimeAttrProcess(View view)
    {
        return (view.getParent() instanceof GridLayout) ? new OneTimeAttrProcessChildGridLayout(view) : new OneTimeAttrProcessDefault(view);
    }

    public void setNeededSetLayoutParams()
    {
        this.neededSetLayoutParams = true;
    }

    public void addLastTask(Runnable task)
    {
        if (taskLastList == null) this.taskLastList = new LinkedList<Runnable>();
        taskLastList.add(task);
    }

    public void finish()
    {
        if (neededSetLayoutParams)
            view.setLayoutParams(view.getLayoutParams()); // Para que los cambios que se han hecho en los objetos "stand-alone" *.LayoutParams se entere el View asociado (esa llamada hace requestLayout creo recordar), al hacerlo al final evitamos múltiples llamadas por cada cambio en LayoutParams

        if (taskLastList != null)
        {
            for (Runnable task : taskLastList) task.run();
        }
    }
}

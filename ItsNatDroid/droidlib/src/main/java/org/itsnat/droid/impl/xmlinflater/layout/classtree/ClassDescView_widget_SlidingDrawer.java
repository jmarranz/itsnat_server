package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.SlidingDrawer;

import org.itsnat.droid.R;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldSetId;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_SlidingDrawer_orientation;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_SlidingDrawer extends ClassDescViewBased
{
    protected MethodContainer<Void> methodOnFinishInflate;

    public ClassDescView_widget_SlidingDrawer(ClassDescViewMgr classMgr, ClassDescView_view_ViewGroup parentClass)
    {
        super(classMgr,"android.widget.SlidingDrawer",parentClass);
        this.methodOnFinishInflate = new MethodContainer<Void>(View.class,"onFinishInflate",null); // Existe el método onFinishInflate en SlidingDrawer class
    }

    @Override
    //@SuppressWarnings("deprecation")
    protected View createViewObject(Context ctx,int idStyle,PendingPostInsertChildrenTasks pending)
    {
        final SlidingDrawer view;
        // En tiempo de creación del componente deben estar definidos los atributos id "content" y "handle" sino da error
        // por ello suministramos unos por defecto cuyos ids existen en Resources aunque luego los cambiemos
        AttributeSet attrSetDefault = readAttributeSetLayout(ctx,R.layout.slidingdrawer_default);
        if (idStyle != 0)
        {
            view = new SlidingDrawer(new ContextThemeWrapper(ctx,idStyle),attrSetDefault,idStyle);
        }
        else
        {
            // Es importante llamar a estos constructores y no pasar un idStyle con 0 pues Spinner define un style por defecto en el constructor que es "mandatory" si no especificamos uno explícitamente
            view = new SlidingDrawer(ctx,attrSetDefault); // El constructor de un sólo param también vale
        }

        if (pending != null)
        {
            pending.addTask(new Runnable(){
                @Override
                public void run()
                {
                    // Se nota que SlidingDrawer está abandonado porque no funciona creado programáticamente, necesita ser ejecutado el método
                    // OnFinishInflate manualmente (por tanto parseado desde XML nativo compilado) para que se definan el content y el handle
                    // Los View hijos content y handle deben estar ya añadidos por eso usamos PendingPostInsertChildrenTasks
                    methodOnFinishInflate.invoke(view);
                }
            });
        }

        return view;
    }


    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecFieldSetBoolean(this, "allowSingleTap","mAllowSingleTap",true));
        addAttrDesc(new AttrDescViewReflecFieldSetBoolean(this, "animateOnClick","mAnimateOnClick",true));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this, "bottomOffset","mBottomOffset",0));
        addAttrDesc(new AttrDescViewReflecFieldSetId(this, "content","mContentId",-1));
        addAttrDesc(new AttrDescViewReflecFieldSetId(this, "handle","mHandleId",-1));
        addAttrDesc(new AttrDescView_widget_SlidingDrawer_orientation(this));
        addAttrDesc(new AttrDescViewReflecFieldSetDimensionInt(this, "topOffset","mTopOffset",0));
    }
}


package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescReflecFieldSetFloat;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDesc_widget_ToggleButton_textOffandOn;

/**
 * Nota: ToggleButton ha sido reemplazado totalmente por Switch, lo implementamos para los despistados
 * Created by jmarranz on 30/04/14.
 */
public class ClassDesc_widget_ToggleButton extends ClassDescViewBased
{
    public ClassDesc_widget_ToggleButton(ClassDescViewMgr classMgr, ClassDesc_widget_CompoundButton parentClass)
    {
        super(classMgr,"android.widget.ToggleButton",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecFieldSetFloat(this,"disabledAlpha","mDisabledAlpha",0.5f));
        addAttrDesc(new AttrDesc_widget_ToggleButton_textOffandOn(this,"textOff"));
        addAttrDesc(new AttrDesc_widget_ToggleButton_textOffandOn(this,"textOn"));
    }
}


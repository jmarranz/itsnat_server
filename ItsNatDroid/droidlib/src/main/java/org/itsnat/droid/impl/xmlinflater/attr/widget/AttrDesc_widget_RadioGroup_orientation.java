package org.itsnat.droid.impl.xmlinflater.attr.widget;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * El único motivo de hacer esta clase es porque el valor por defecto del orientation de LinearLayout es "vertical" en RadioGroup
 * y no horizontal, el método setOrientation es el de LinearLayout pero es llamado desde RadioGroup, aquí es más o menos igual la idea
 * Llamamos a getParentClassDescViewBased() en el constructor porque setOrientation está en LinearLayout (clase base de RadioGroup)
 *
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_RadioGroup_orientation extends AttrDesc_widget_LinearLayout_orientation
{
    public AttrDesc_widget_RadioGroup_orientation(ClassDescViewBased parent)
    {
        super(parent.getParentClassDescViewBased(),"vertical");
    }

}

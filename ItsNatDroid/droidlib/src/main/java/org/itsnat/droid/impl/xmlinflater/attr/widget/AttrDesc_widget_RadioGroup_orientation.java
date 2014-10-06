package org.itsnat.droid.impl.xmlinflater.attr.widget;

import org.itsnat.droid.impl.xmlinflater.attr.AttrDescReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.attr.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.attr.OrientationUtil;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

/**
 * El único motivo de hacer esta clase es porque el valor por defecto del orientation de LinearLayout es "vertical" en RadioGroup
 * y no horizontal, el método setOrientation es el de LinearLayout pero es llamado desde RadioGroup
 * Llamamos a getParentClassDescViewBased() en el constructor porque setOrientation está en LinearLayout (clase base de RadioGroup)
 *
 * Created by jmarranz on 6/10/14.
 */
public class AttrDesc_widget_RadioGroup_orientation extends AttrDescReflecMethodSingleName
{
    public AttrDesc_widget_RadioGroup_orientation(ClassDescViewBased parent)
    {
        super(parent,"orientation",int.class, OrientationUtil.valueMap,"vertical");

        // Redefinimos el atributo method para cambiar el Class contenedor a LinearLayout pues setOrientation está ahí no en RadioGroup
        this.method = new MethodContainer(parent.getParentClassDescViewBased(),method.getMethodName(),method.getParamClasses());
    }
}

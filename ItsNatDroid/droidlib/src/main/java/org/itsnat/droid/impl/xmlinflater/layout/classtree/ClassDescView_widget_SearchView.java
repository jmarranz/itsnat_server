package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodBoolean;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodCharSequence;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodDimensionInt;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodMultipleName;
import org.itsnat.droid.impl.xmlinflater.layout.attr.ImeOptionsUtil;
import org.itsnat.droid.impl.xmlinflater.layout.attr.InputTypeUtil;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_SearchView extends ClassDescViewBased
{
    public ClassDescView_widget_SearchView(ClassDescViewMgr classMgr, ClassDescView_widget_LinearLayout parentClass)
    {
        super(classMgr,"android.widget.SearchView",parentClass);
    }

    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescViewReflecMethodBoolean(this,"iconifiedByDefault",true));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"imeOptions", ImeOptionsUtil.valueMap,"actionUnspecified"));
        addAttrDesc(new AttrDescViewReflecMethodMultipleName(this,"inputType", InputTypeUtil.valueMap,"text")); // No estoy seguro que el valor por defecto sea "text" pero parece el más razonable
        addAttrDesc(new AttrDescViewReflecMethodDimensionInt(this,"maxWidth",0f));
        addAttrDesc(new AttrDescViewReflecMethodCharSequence(this,"queryHint",""));
    }
}


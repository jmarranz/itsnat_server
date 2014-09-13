package org.itsnat.droid.impl.xmlinflater.attr;

import android.widget.Spinner;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_Spinner_spinnerMode extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>();
    static
    {
        valueMap.put("dialog", Spinner.MODE_DIALOG);
        valueMap.put("dropdown",Spinner.MODE_DROPDOWN);
    }

    public AttrDesc_widget_Spinner_spinnerMode(ClassDescViewBased parent)
    {
        super(parent,"spinnerMode",valueMap,"dialog");
    }

}

package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.AbsListView;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_AbsListView_choiceMode extends AttrDescViewReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 4 );
    static
    {
        valueMap.put("none", AbsListView.CHOICE_MODE_NONE);
        valueMap.put("singleChoice",AbsListView.CHOICE_MODE_SINGLE);
        valueMap.put("multipleChoice",AbsListView.CHOICE_MODE_MULTIPLE);
        valueMap.put("multipleChoiceModal",AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    public AttrDescView_widget_AbsListView_choiceMode(ClassDescViewBased parent)
    {
        super(parent,"choiceMode",int.class,valueMap,"none");
    }


}

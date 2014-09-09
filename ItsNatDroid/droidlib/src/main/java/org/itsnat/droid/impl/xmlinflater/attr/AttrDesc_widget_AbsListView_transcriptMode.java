package org.itsnat.droid.impl.xmlinflater.attr;

import android.widget.AbsListView;

import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_AbsListView_transcriptMode extends AttrDescReflecSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>();
    static
    {
        valueMap.put("disabled", AbsListView.TRANSCRIPT_MODE_DISABLED);
        valueMap.put("normal",AbsListView.TRANSCRIPT_MODE_NORMAL);
        valueMap.put("alwaysScroll",AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    public AttrDesc_widget_AbsListView_transcriptMode(ClassDescViewBased parent)
    {
        super(parent,"transcriptMode",valueMap,"disabled");
    }


}

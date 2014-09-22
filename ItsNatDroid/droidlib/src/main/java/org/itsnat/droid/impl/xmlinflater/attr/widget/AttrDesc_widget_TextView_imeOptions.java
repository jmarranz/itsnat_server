package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import org.itsnat.droid.impl.xmlinflater.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDesc_widget_TextView_imeOptions extends AttrDesc
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 16 );
    static
    {
        valueMap.put("normal", 0x00000000);
        valueMap.put("actionUnspecified",EditorInfo.IME_NULL); // Es también 0 como "normal"
        valueMap.put("actionNone",EditorInfo.IME_ACTION_NONE);
        valueMap.put("actionGo",EditorInfo.IME_ACTION_GO);
        valueMap.put("actionSearch",EditorInfo.IME_ACTION_SEARCH);
        valueMap.put("actionSend",EditorInfo.IME_ACTION_SEND);
        valueMap.put("actionNext",EditorInfo.IME_ACTION_NEXT);
        valueMap.put("actionDone",EditorInfo.IME_ACTION_DONE);
        valueMap.put("actionPrevious",EditorInfo.IME_ACTION_PREVIOUS);
        valueMap.put("flagNoFullscreen",EditorInfo.IME_FLAG_NO_FULLSCREEN);
        valueMap.put("flagNavigatePrevious",EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS);
        valueMap.put("flagNavigateNext",EditorInfo.IME_FLAG_NAVIGATE_NEXT);
        valueMap.put("flagNoExtractUi",EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        valueMap.put("flagNoAccessoryAction",EditorInfo.IME_FLAG_NO_ACCESSORY_ACTION);
        valueMap.put("flagNoEnterAction",EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        valueMap.put("flagForceAscii",EditorInfo.IME_FLAG_FORCE_ASCII);
    }

    public AttrDesc_widget_TextView_imeOptions(ClassDescViewBased parent)
    {
        super(parent,"imeOptions");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = parseMultipleName(value, valueMap);

        TextView textView = (TextView)view;
        textView.setImeOptions(convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "actionUnspecified", null,null);
    }

}

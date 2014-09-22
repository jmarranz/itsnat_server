package org.itsnat.droid.impl.xmlinflater.attr.widget;

import android.text.InputType;
import android.view.View;
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
public class AttrDesc_widget_TextView_inputType extends AttrDesc
{
    static Map<String,Integer> valueMap = new HashMap<String,Integer>( 32 );
    static
    {
        valueMap.put("none", 0x00000000);
        valueMap.put("text", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        valueMap.put("textCapCharacters",InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        valueMap.put("textCapWords",InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        valueMap.put("textCapSentences",InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        valueMap.put("textAutoCorrect",InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        valueMap.put("textAutoComplete",InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        valueMap.put("textMultiLine",InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        valueMap.put("textImeMultiLine",InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        valueMap.put("textNoSuggestions",InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        valueMap.put("textUri",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        valueMap.put("textEmailAddress",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        valueMap.put("textEmailSubject",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
        valueMap.put("textShortMessage",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        valueMap.put("textLongMessage",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        valueMap.put("textPersonName",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        valueMap.put("textPostalAddress",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
        valueMap.put("textPassword",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        valueMap.put("textVisiblePassword",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        valueMap.put("textWebEditText",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        valueMap.put("textFilter",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_FILTER);
        valueMap.put("textPhonetic",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PHONETIC);
        valueMap.put("textWebEmailAddress",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        valueMap.put("textWebPassword",InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        valueMap.put("number",InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        valueMap.put("numberSigned",InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        valueMap.put("numberDecimal",InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        valueMap.put("numberPassword",InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        valueMap.put("phone",InputType.TYPE_CLASS_PHONE);
        valueMap.put("datetime",InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        valueMap.put("date",InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        valueMap.put("time",InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
    }

    public AttrDesc_widget_TextView_inputType(ClassDescViewBased parent)
    {
        super(parent,"inputType");
    }

    public void setAttribute(View view, String value, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = parseMultipleName(value, valueMap);

        TextView textView = (TextView)view;
        textView.setInputType(convertedValue);
    }

    public void removeAttribute(View view)
    {
        setAttribute(view, "text", null,null);
    } // No estoy seguro que el valor por defecto sea "text" pero parece el más razonable

}
